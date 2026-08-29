package com.zencart.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@EqualsAndHashCode(callSuper = true, exclude = "category")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product extends BaseEntity implements Serializable {

    @Serial
    private static long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", unique = true, nullable = false, updatable = false)
    @Id
    private Integer productId;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "product_image_url")
    private String imageUrl;

    @Column(unique = true)
    private String sku;

    @Column(name = "price_unit", columnDefinition = "decimal")
    private String price;

    @Column(name = "product_quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

}
