package in.dabiaditya.foodiesapi.services;

import in.dabiaditya.foodiesapi.io.UserRequest;
import in.dabiaditya.foodiesapi.io.UserResponse;

public interface UserService {
    
    UserResponse registerUser(UserRequest request);

    String findByUserId();
}
