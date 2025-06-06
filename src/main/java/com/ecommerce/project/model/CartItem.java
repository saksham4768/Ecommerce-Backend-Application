package com.ecommerce.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.repository.cdi.Eager;

@Entity
@Data
@Table(name = "cart_Items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference("cart")
    @ToString.Exclude
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference("cartItems")
    @ToString.Exclude
    private Product product;

    private Integer quantity;
    private double discount;
    private double productPrice;
}
