package com.example.ecom_application.service;

import com.example.ecom_application.dtos.OrderItemDTO;
import com.example.ecom_application.dtos.OrderResponse;
import com.example.ecom_application.entity.Cart;
import com.example.ecom_application.entity.Order;
import com.example.ecom_application.entity.OrderItem;
import com.example.ecom_application.entity.User;
import com.example.ecom_application.entity.constants.OrderStatus;
import com.example.ecom_application.repository.OrderRepository;
import com.example.ecom_application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    @Override
    public Optional<OrderResponse> createOrder(String userId) {
        //Validate cart items
        List<Cart> carts = cartService.getCartByUser(userId);
        if(carts.isEmpty()){
            return Optional.empty();
        }

        //Validate userId
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()){
            return Optional.empty();
        }
        User user = userOpt.get();

        //Calculate total price
        BigDecimal totalPrice = carts.stream()
                .map(Cart::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Finally create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = carts.stream()
                        .map(cart -> new OrderItem(
                                null,
                                cart.getProduct(),
                                cart.getQuantity(),
                                cart.getPrice(),
                                order
                        )).collect(Collectors.toList());
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        //Clear the cart as well
        cartService.clearCart(userId);

        return Optional.of(orderToOrderResponseMapper(savedOrder));
    }

    private OrderResponse orderToOrderResponseMapper(Order order){
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setItems(order.getItems().stream()
                .map(orderItem -> new OrderItemDTO(
                        orderItem.getId(),
                        orderItem.getProduct().getId(),
                        orderItem.getQuantity(),
                        orderItem.getPrice(),
                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                )).toList());
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
}
