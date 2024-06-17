package com.imanzi.marketplace.model;

import com.imanzi.marketplace.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    private LocalDate orderDate;

    private Double totalPrice;

    @PrePersist
    @PreUpdate
    private void calculateTotalPrice() {
        if (products != null) {
            this.totalPrice = products.stream()
                    .mapToDouble(Product::getPrice)
                    .sum();
        } else {
            this.totalPrice = 0.0;
        }
    }
}
