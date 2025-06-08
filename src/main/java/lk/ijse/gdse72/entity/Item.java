package lk.ijse.gdse72.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Setter
//@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    private String code;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int qtyOnHand;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Item(String code, String name, double price, int qtyOnHand) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.qtyOnHand = qtyOnHand;
    }
}
