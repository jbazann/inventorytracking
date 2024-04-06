package com.jbazann.inventorytracking.db.repositories;

import java.util.List;
import java.util.UUID;

import com.jbazann.inventorytracking.domain.InventoryGroup;
import com.jbazann.inventorytracking.domain.InventoryPart;// TODO CLEANSE BLASPHEMY 
import com.jbazann.inventorytracking.domain.InventoryGroup.GroupState;

public class InventoryGroupRepository {

    public List<InventoryGroup> getIssues(final int page, final int pageSize) {
        return List.of();//TODO placeholder
    }

    public List<InventoryGroup> getLatestAnyState(final int page, final int pageSize) {
        return TEMPORARY_PLACEHOLDER_TRANSIENT_TESTING_NOTPERMANENT_FAKE_DB();//TODO placeholder
    }

    private List<InventoryGroup> TEMPORARY_PLACEHOLDER_TRANSIENT_TESTING_NOTPERMANENT_FAKE_DB() {
        List<InventoryPart> parts = List.of(
            new InventoryPart("1 P1"),
            new InventoryPart("2 P2"),
            new InventoryPart("3 P3"),
            new InventoryPart("4 P4"),
            new InventoryPart("5 P5"),
            new InventoryPart("6 P6"),
            new InventoryPart("7 P7"),
            new InventoryPart("8 P8"),
            new InventoryPart("9 P9"),
            new InventoryPart("10 P10")
        );
        
        return List.of(
            new InventoryGroup(UUID.randomUUID(),"G1",GroupState.TRACKING,List.of(parts.get(0),parts.get(1))),
            new InventoryGroup(UUID.randomUUID(),"G2",GroupState.TRACKING,List.of(parts.get(2),parts.get(3))),
            new InventoryGroup(UUID.randomUUID(),"G3",GroupState.ISSUE,List.of(parts.get(4),parts.get(5))),
            new InventoryGroup(UUID.randomUUID(),"G4",GroupState.ISSUE,List.of(parts.get(6))),
            new InventoryGroup(UUID.randomUUID(),"G5",GroupState.DELIVERED,List.of(parts.get(7),parts.get(8),parts.get(9)))
        );
    }
    
}
