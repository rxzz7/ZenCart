package com.zencart.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
public class Address extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "postal_code")
    private String postalCode;

    private String city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
