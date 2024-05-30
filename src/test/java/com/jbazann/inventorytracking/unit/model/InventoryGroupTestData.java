package com.jbazann.inventorytracking.unit.model;

import com.jbazann.inventorytracking.domain.InventoryPart;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InventoryGroupTestData {

    public InventoryPart mockPart() {
        return mockPart(InventoryPart.PartState.TRACKING);
    }

    public InventoryPart mockPart(InventoryPart.PartState state) {
        InventoryPart mock = mock();

        when(mock.id()).thenReturn(UUID.randomUUID());
        when(mock.is(mock)).thenReturn(true);
        when(mock.needsReplacement()).thenReturn(false);
        when(mock.state()).thenReturn(state);
        when(mock.recorded()).thenReturn(LocalDateTime.now());

        return mock;
    }

    public InventoryGroupTestData mockPartIs(InventoryPart one, InventoryPart another) {
        when(one.is(another)).thenReturn(true);
        when(another.is(one)).thenReturn(true);
        return this;
    }

    public InventoryGroupTestData mockPartIsNot(InventoryPart one, InventoryPart another) {
        when(one.is(another)).thenReturn(false);
        when(another.is(one)).thenReturn(false);
        return this;
    }

    public InventoryGroupTestData mockPartCanReplace(InventoryPart one, InventoryPart another) {
        when(one.canReplace(another)).thenReturn(true);
        when(another.canReplace(one)).thenReturn(true);
        return this;
    }

    public InventoryGroupTestData mockPartCanNotReplace(InventoryPart one, InventoryPart another) {
        when(one.canReplace(another)).thenReturn(false);
        when(another.canReplace(one)).thenReturn(false);
        return this;
    }

    public InventoryGroupTestData mockPartNeedsReplacement(InventoryPart mock) {
        when(mock.needsReplacement()).thenReturn(true);
        when(mock.state()).thenReturn(InventoryPart.PartState.REPLACING);
        return this;
    }

}
