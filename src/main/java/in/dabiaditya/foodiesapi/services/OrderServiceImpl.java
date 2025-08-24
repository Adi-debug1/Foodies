package in.dabiaditya.foodiesapi.services;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import in.dabiaditya.foodiesapi.entity.OrderEntity;
import in.dabiaditya.foodiesapi.io.OrderRequest;
import in.dabiaditya.foodiesapi.io.OrderResponse;
import in.dabiaditya.foodiesapi.repository.CartRepository;
import in.dabiaditya.foodiesapi.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    @Value("${razorpay_key}")
    private String RAZORPAY_KEY;
    @Value("${razorpay_secret}")
    private String RAZORPAY_SECRET;

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {
        OrderEntity newOrder = convertToEntity(request);
        newOrder = orderRepository.save(newOrder);


        //create razorpay payment order
        RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", newOrder.getAmount());
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        newOrder.setRazorpayOrderId(razorpayOrder.get("id"));
        String loggedInUserId = userService.findByUserId();
        newOrder.setUserId(loggedInUserId);
        newOrder = orderRepository.save(newOrder);
        return convertToResponse(newOrder);

    }

    private OrderResponse convertToResponse(OrderEntity neworder){
        return OrderResponse.builder()
                        .id(neworder.getId())
                        .amount(neworder.getAmount())
                        .userAddress(neworder.getUserAddress())
                        .userId(neworder.getUserId())
                        .razorpayOrderId(neworder.getRazorpayOrderId())
                        .paymentStatus(neworder.getPaymentStatus())
                        .orderStatus(neworder.getOrderStatus())
                        .email(neworder.getEmail())
                        .phoneNumber(neworder.getPhoneNumber())
                        .build();
    }

    private OrderEntity convertToEntity(OrderRequest request){
        return OrderEntity.builder()
                    .userAddress(request.getAddress())
                    .amount(request.getAmount())
                    .orderedItems(request.getOrderedItem())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .orderStatus(request.getOrderStatus())
                    .build();
    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {
        String razorPayOrderId = paymentData.get("razorpay_order_id");
        OrderEntity existingOrder = orderRepository.findByRazorpayOrderId(razorPayOrderId)
                                    .orElseThrow(() -> new RuntimeException("Order not found"));
        existingOrder.setPaymentStatus(status);
        existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorpayPayementId(paymentData.get("razorpay_payment_id"));
        orderRepository.save(existingOrder);
        if("paid".equalsIgnoreCase(status)){
            cartRepository.deleteByUserId(existingOrder.getUserId());
        }
    }
    
}
