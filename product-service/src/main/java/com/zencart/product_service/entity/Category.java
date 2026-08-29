package com.zencart.product_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"subCategories", "parentCategory", "products"})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "categories")
public class Category extends BaseEntity implements Serializable {

    @Serial
    private static long serialVersionUID = 1L;


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, unique = true, updatable = false)
    @Id
    private Integer categoryId;

    @Column(name = "category_title")
    private String categoryTitle;

    @Column(name = "category_image_url")
    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Category> subCategories;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Product> products;

}
