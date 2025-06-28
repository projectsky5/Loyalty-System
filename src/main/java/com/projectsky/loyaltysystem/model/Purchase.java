package com.projectsky.loyaltysystem.model;

import com.projectsky.loyaltysystem.enums.PurchaseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private LocalDateTime purchaseDate;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
