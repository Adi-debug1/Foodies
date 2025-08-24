package in.dabiaditya.foodiesapi.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import in.dabiaditya.foodiesapi.io.FoodRequest;
import in.dabiaditya.foodiesapi.io.FoodResponse;

@Service
public interface FoodService {
    
    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest request, MultipartFile file);

    List<FoodResponse> readFoods();
    
    FoodResponse readFood(String id);

    boolean deleteFile(String filename);
    
    void deleteFood(String id);
}
