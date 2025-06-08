package lk.ijse.gdse72.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@ToString
@Table(name = "orders")
public class Orders {

    @Id
    private String orderId;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Orders(String orderId, Date date, Customer customer) {
        this.orderId = orderId;
        this.date = date;
        this.customer = customer;
    }

    public Orders(String orderId) {
        this.orderId = orderId;
    }
}
