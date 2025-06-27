package com.projectsky.loyaltysystem.dto;

import com.projectsky.loyaltysystem.enums.Category;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ClientFullDto {
    private Long id;
    private String username;
    private String email;
    private BigDecimal balance;
    private Integer points;
    private String category;
    private Integer totalPurchases;
    private LocalDateTime lastPurchase;

    public ClientFullDto(Long id,
                         String username,
                         String email,
                         BigDecimal balance,
                         Integer points,
                         Category category,
                         LocalDateTime lastPurchase,
                         Long totalPurchases) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.balance = balance;
        this.points = points;
        this.category = category.name(); // или category.toString()
        this.lastPurchase = lastPurchase;
        this.totalPurchases = totalPurchases.intValue(); // или касти где нужно
    }
}
