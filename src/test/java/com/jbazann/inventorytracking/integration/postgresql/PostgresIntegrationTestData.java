package com.jbazann.inventorytracking.integration.postgresql;

import com.jbazann.inventorytracking.domain.InventoryGroup;
import com.jbazann.inventorytracking.domain.InventoryGroup.GroupState;
import com.jbazann.inventorytracking.domain.InventoryPart;
import com.jbazann.inventorytracking.domain.InventoryPart.PartState;

import java.time.LocalDateTime;
import java.util.*;

public final class PostgresIntegrationTestData {

    public final List<InventoryGroup> inventoryGroups;

    public PostgresIntegrationTestData() {
        this(20,8);
    }

    public PostgresIntegrationTestData(int groups, int parts) {
        this.inventoryGroups = generateGroups(groups, parts);
    }

    private List<InventoryGroup> generateGroups(int amount, int maxParts) {
        final List<InventoryGroup> groups = new ArrayList<>(amount);
        while(amount-- > 0) {
            GroupState someState = GroupState.values()[amount % GroupState.values().length];
            groups.add(new InventoryGroup(
                    UUID.randomUUID(),
                    UUID.randomUUID().toString(),
                    someState,
                    LocalDateTime.now(),
                    generateParts(someState,maxParts)
            ));
        }
        return Collections.unmodifiableList(groups);
    }

    private List<InventoryPart> generateParts(final GroupState groupState, final int maxAmount) {
        final List<InventoryPart> parts = new ArrayList<>(maxAmount);
        final Random random = new Random();
        final int amount = random.nextInt(maxAmount);
        final List<PartState> states = getConsistentPartStates(groupState, amount != 0 ? amount : 1);
        states.forEach(state ->
            parts.add(new InventoryPart(
                    UUID.randomUUID(),
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    state,
                    LocalDateTime.now()
            ))
        );
        return parts;
    }

    //TODO document state logic
    private List<PartState> getConsistentPartStates(final GroupState groupState, final int amount) {
        final List<PartState> states = new ArrayList<>(Collections.nCopies(amount,PartState.TRACKING));
        final Random random = new Random();
        switch(groupState) {
            case ISSUE -> {
                return states.stream()
                        .map(p -> random.nextBoolean() ? PartState.REPLACING : PartState.RETURNING)
                        .toList();
            }
            case TRACKING -> {return states;}
            case DELIVERED -> {
                return states.stream()
                        .map(p -> PartState.DELIVERED)
                        .toList();
            }
            default -> throw new IllegalStateException("Unexpected value: " + groupState);
        }
    }

}
