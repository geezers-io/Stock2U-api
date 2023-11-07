package com.hack.stock2u.order.service;

public class OrderService {

  public void createOrder(User user){
    Order order = new Order();
    order.setUser(user);
    orderRepository.save(order);
  }

}
