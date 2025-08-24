package in.dabiaditya.foodiesapi.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
@Builder
public class UserEntity {
    
    @Id
    private String id;
    private String name;
    private String email;
    private String password;

}
