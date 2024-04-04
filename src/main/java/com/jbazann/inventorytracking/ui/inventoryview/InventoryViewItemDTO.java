package com.jbazann.inventorytracking.ui.inventoryview;

import java.util.List;
import java.util.UUID;

public record InventoryViewItemDTO(
    UUID id,
    String name,
    String encodedName,
    String state,
    boolean isGroup,
    String date,
    List<String> partNames
) {}
