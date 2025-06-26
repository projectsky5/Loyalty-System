package com.projectsky.loyaltysystem.repository;

import com.projectsky.loyaltysystem.dto.ClientFullDto;
import com.projectsky.loyaltysystem.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("""
    SELECT
         c.id,
         c.bonusPoints,
         c.category,
         c.username,
         MAX (p.purchaseDate),
         count(p)
    FROM Client c 
    LEFT JOIN Purchase p
        ON c.id = p.client.id
    WHERE c.id = :id
    GROUP BY c.id, c.bonusPoints, c.category, c.username
    """)
    Optional<ClientFullDto> findClientSummaryById(@Param("id") Long id);
}
