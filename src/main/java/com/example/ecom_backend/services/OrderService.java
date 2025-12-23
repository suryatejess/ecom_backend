package com.example.ecom_backend.services;

import com.example.ecom_backend.dtos.OrderItemDTO;
import com.example.ecom_backend.dtos.OrderResponseDTO;
import com.example.ecom_backend.entities.*;
import com.example.ecom_backend.repositories.OrderItemRepository;
import com.example.ecom_backend.repositories.OrderRepository;
import com.example.ecom_backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;

    public Order createOrder(){
        return new Order();
    }

    @Transactional
    public void placeOrder(String orderAddress, String receiverName){

        /*
        get or create a cart
        create an order
        iterate
            turn cart item into order item
            add order item into order's list
        save order
         */

        Cart cart = cartService.getOrCreateCart();
        Order order = createOrder();

        List<OrderItem> orderItemsList = new ArrayList<>();

        for(int i = 0; i < cart.getCartItems().size(); i++){
//            OrderItem orderItem = orderItemRepository.save(new OrderItem());
            OrderItem orderItem = new OrderItem();

            CartItem presentCartItem = cart.getCartItems().get(i);

            // turn cart item into order item
            orderItem.setProductId(presentCartItem.getProduct().getId());
            orderItem.setProductName(presentCartItem.getProduct().getName());
            orderItem.setProductPriceWhileOrdering(presentCartItem.getProduct().getPrice());
            orderItem.setProductOrderQuantity(presentCartItem.getQuantity());
            orderItem.setOrder(order);

            // add order item to order
            orderItemsList.add(orderItem);
        }

        order.setOrderItems(orderItemsList);
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setAddress(orderAddress);
        order.setReceiverName(receiverName);
        order.setOrderPlacedDate(new Date(System.currentTimeMillis()));

        orderRepository.save(order);

        // TODO: clear the cart
        // TODO: decrement the product available quantity
    }

//    public List<OrderResponseDTO> getAllOrders(){
//
//        List<Order> allOrders = (List<Order>) orderRepository.findAll();
//        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
//
//    }

    public List<OrderResponseDTO> getAllOrders() {

        List<Order> orders = (List<Order>) orderRepository.findAll();
        List<OrderResponseDTO> response = new ArrayList<>();

        for (Order order : orders) {
            OrderResponseDTO dto = mapToOrderResponseDTO(order);
            response.add(dto);
        }

        return response;
    }


    public Order getOrderById(int id){
        return orderRepository.findById(id).get();
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order){

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setAddress(order.getAddress());
        dto.setReceiverName(order.getReceiverName());
        dto.setOrderPlacedDate(order.getOrderPlacedDate());

        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

        for(OrderItem orderItem : order.getOrderItems()){
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(orderItem.getProductId());
            itemDTO.setProductName(orderItem.getProductName());
            itemDTO.setPrice(orderItem.getProductPriceWhileOrdering());
            itemDTO.setQuantity(orderItem.getProductOrderQuantity());
            orderItemDTOList.add(itemDTO);
        }

        dto.setItems(orderItemDTOList);
        return dto;
    }

    public void deleteOrderById(int id){
        orderRepository.deleteById(id);
    }


}
