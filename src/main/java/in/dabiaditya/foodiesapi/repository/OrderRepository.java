package in.dabiaditya.foodiesapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import in.dabiaditya.foodiesapi.entity.OrderEntity;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends MongoRepository<OrderEntity, String>{
    
    List<OrderEntity> findByUserId(String userId);
    
    Optional<OrderEntity> findByRazorpayOrderId(String razorpayOrderId);

}
