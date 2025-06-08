package lk.ijse.gdse72.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@ToString
//@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    @EmbeddedId
    private OrderDetailPK id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemCode")
    @JoinColumn(name = "item_code")
    private Item item;

    @Column(nullable = false)
    private int qty;

    public OrderDetail(String orderId, Item item, int qty, Orders order) {
        this.id = new OrderDetailPK(orderId, item.getCode());
        this.order = order;
        this.item = item;
        this.qty = qty;
    }

    public OrderDetail(OrderDetailPK id, Orders order, Item item, int qty) {
        this.id = id;
        this.order = order;
        this.item = item;
        this.qty = qty;
    }
}