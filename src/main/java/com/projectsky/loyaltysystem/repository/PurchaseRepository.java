package com.projectsky.loyaltysystem.repository;

import com.projectsky.loyaltysystem.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
