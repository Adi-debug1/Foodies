package in.dabiaditya.foodiesapi.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.dabiaditya.foodiesapi.entity.UserEntity;
import in.dabiaditya.foodiesapi.io.UserRequest;
import in.dabiaditya.foodiesapi.io.UserResponse;
import in.dabiaditya.foodiesapi.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;



    @Override
    public UserResponse registerUser(UserRequest request) {
        UserEntity newUser = convertToEntity(request);
        newUser = userRepository.save(newUser);
        return convertToResponse(newUser);

    }

    private UserEntity convertToEntity(UserRequest request){
        return UserEntity.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .build();
    }

    private UserResponse convertToResponse(UserEntity registeredUser){
        return UserResponse.builder()
                    .id(registeredUser.getId())
                    .name(registeredUser.getName())
                    .email(registeredUser.getEmail())
                    .password(registeredUser.getPassword())
                    .build();
    }

    @Override
    public String findByUserId() {
        String loggedInUserEmail = authenticationFacade.getAuthenticatiocation().getName();
        UserEntity loggedInUser = userRepository.findByEmail(loggedInUserEmail).orElseThrow(() -> new UsernameNotFoundException("User is not found "));
        return loggedInUser.getId();
    }
}
