package com.hack.stock2u.order.dto;


import com.hack.stock2u.order.repository.Order;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private int count;

  private int price;

  @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id")
  private Order order; // 주문 연결

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "item_id")
  private Item item; // 아이템 연결

  public static Order_item createOrderItem(Item item, int count){

    Order_item order_item = new Order_item();
    order_item.setItem(item);
    order_item.setCount(count);
    order_item.setPrice(item.getPrice());

    return order_item;
  }


}
