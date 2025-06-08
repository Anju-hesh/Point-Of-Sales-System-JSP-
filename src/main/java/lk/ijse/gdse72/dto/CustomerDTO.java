package lk.ijse.gdse72.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter

public class CustomerDTO {

    private String id;
    private String name;
    private String address;
    private double salary;

}
