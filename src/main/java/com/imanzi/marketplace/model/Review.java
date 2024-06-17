package com.imanzi.marketplace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review  extends BaseEntity{

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @NotBlank
    private String reviewText;

}

