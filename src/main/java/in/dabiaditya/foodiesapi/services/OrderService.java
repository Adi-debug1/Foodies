package in.dabiaditya.foodiesapi.services;

import java.util.Map;

import com.razorpay.RazorpayException;

import in.dabiaditya.foodiesapi.io.OrderRequest;
import in.dabiaditya.foodiesapi.io.OrderResponse;

public interface OrderService {
    
    OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;

    void verifyPayment(Map<String, String> paymentData, String status);

}
