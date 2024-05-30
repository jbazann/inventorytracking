package com.jbazann.inventorytracking.db.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbazann.inventorytracking.domain.InventoryGroup;

@Repository
public interface InventoryGroupRepository extends JpaRepository<InventoryGroup,UUID> {

}
