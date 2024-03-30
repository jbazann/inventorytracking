package com.jbazann.inventorytracking.ui.inventoryview;

import java.util.List;

public record InventoryViewItemDTO(
    String name,
    String encodedName,
    String state,
    boolean isGroup,
    String date,
    List<String> partNames
) {}
