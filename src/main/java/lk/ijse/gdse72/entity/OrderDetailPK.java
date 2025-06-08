//package lk.ijse.gdse72.entity;
//
//import jakarta.persistence.Embeddable;
//import lombok.*;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@Embeddable
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class OrderDetailPK implements Serializable {
//
//    private String orderId;
//    private String itemCode;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof OrderDetailPK)) return false;
//        OrderDetailPK that = (OrderDetailPK) o;
//        return Objects.equals(getOrderId(), that.getOrderId()) &&
//                Objects.equals(getItemCode(), that.getItemCode());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getOrderId(), getItemCode());
//    }
//}
package lk.ijse.gdse72.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailPK implements Serializable {

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "item_code")
    private String itemCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailPK that = (OrderDetailPK) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(itemCode, that.itemCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, itemCode);
    }
}