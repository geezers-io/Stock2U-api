package com.hack.stock2u.order.repository;

import com.hack.stock2u.models.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table (name = "orders")

public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id; // order_id

  private String status; // 상황

  private int price; // 총 금액

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;
//product 엔티티
  // buyer
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<product_id> porudct_id = new ArrayList<>();

  @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private LocalDateTime createDate; // 날짜

  public void addOrderItem(Order_item order_item){
    order_items.add(order_item);
    order_item.setOrder(this);
  }

  public static Order createOrder(User user, List<Order_item> orderItemList){
    Order order = new Order();
    order.setUser(user);
    for(Order_item order_item : orderItemList){
      order.addOrderItem(order_item);
    }
    order.setStatus("주문 완료");
    order.setCreateDate(LocalDateTime.now());
    return order;
  }

  public int getTotalPrice(){
    int totalPrice = 0;

    for(Order_item order_item : order_items){
      totalPrice += (order_item.getPrice() * order_item.getCount());
    }

    return totalPrice;
  }
}

