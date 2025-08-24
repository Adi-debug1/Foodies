package in.dabiaditya.foodiesapi.services;

import in.dabiaditya.foodiesapi.io.CartRequest;
import in.dabiaditya.foodiesapi.io.CartResponse;

public interface CartService {
    
    CartResponse addToCart(CartRequest request);

    CartResponse getCart();

    void clearCart();

    CartResponse removeFromCart(CartRequest cartRequest);
}
