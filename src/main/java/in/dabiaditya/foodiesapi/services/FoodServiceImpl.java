package in.dabiaditya.foodiesapi.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import in.dabiaditya.foodiesapi.entity.FoodEntity;
import in.dabiaditya.foodiesapi.io.FoodRequest;
import in.dabiaditya.foodiesapi.io.FoodResponse;
import in.dabiaditya.foodiesapi.repository.FoodRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class FoodServiceImpl implements FoodService{

    @Autowired
    private S3Client s3Client;
    @Autowired
    private FoodRepository foodRepository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
       String key = UUID.randomUUID().toString()+"."+filenameExtension;
       try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                .bucket(bucketName)
                                                .key(key)
                                                .acl("public-read")
                                                .contentType(file.getContentType())
                                                .build();
                                
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful()){
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            }else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ("File upload failed"));
            }
       } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ("An error while uploading the file"));
       }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        FoodEntity newfoodEntity = convertToEntity(request);
        String imageUrl = uploadFile(file);
        newfoodEntity.setImageUrl(imageUrl);
        newfoodEntity = foodRepository.save(newfoodEntity);
        return convertToResponse(newfoodEntity);
    }

    private FoodEntity convertToEntity(FoodRequest request){
        return FoodEntity.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .category(request.getCategory())
                    .price(request.getPrice())
                    .build();
    }

    private FoodResponse convertToResponse(FoodEntity entity){
        return FoodResponse.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .category(entity.getCategory())
                    .price(entity.getPrice())
                    .imageUrl(entity.getImageUrl())
                    .build();
    }

    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> databaseEnteries = foodRepository.findAll();
        return databaseEnteries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood(String id) {
        FoodEntity existingFood = foodRepository.findById(id).orElseThrow(() -> new RuntimeException("food not found by id "+id));
        return convertToResponse(existingFood);
    }

    @Override
    public boolean deleteFile(String filename) {
       DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                                .bucket(bucketName)
                                                .key(filename)
                                                .build();
        
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response = readFood(id);
        String imageUrl = response.getImageUrl();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isFileDeleted = deleteFile(filename);
        if(isFileDeleted){
            foodRepository.deleteById(id);
        }
    }
    
}
