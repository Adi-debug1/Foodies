package in.dabiaditya.foodiesapi.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;

import in.dabiaditya.foodiesapi.io.OrderRequest;
import in.dabiaditya.foodiesapi.io.OrderResponse;
import in.dabiaditya.foodiesapi.services.OrderService;
import lombok.AllArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public OrderResponse createOrderWithPayment(@RequestBody OrderRequest request) throws RazorpayException{
        OrderResponse response = orderService.createOrderWithPayment(request);
        return response;
    }

    @GetMapping("/verify")
    public void verifyPayment(@RequestBody Map<String, String> paymentData){
        orderService.verifyPayment(paymentData, "paid");
    }
    
}
