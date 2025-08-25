package in.dabiaditya.foodiesapi.io;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequest {
    
    private List<OrderItem> orderedItems;
    private String address;
    private double amount;
    private String email;
    private String phoneNumber;
    private String orderStatus;
}
