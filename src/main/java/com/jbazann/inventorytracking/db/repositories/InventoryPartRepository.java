package com.jbazann.inventorytracking.db.repositories;

import com.jbazann.inventorytracking.domain.InventoryPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryPartRepository extends JpaRepository<InventoryPart, UUID> {
    
}
