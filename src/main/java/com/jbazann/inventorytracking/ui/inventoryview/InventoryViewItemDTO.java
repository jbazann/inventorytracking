package com.jbazann.inventorytracking.ui.inventoryview;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record InventoryViewItemDTO(
    UUID id,
    String name,
    String encodedName,
    String state,
    boolean isGroup,
    String date,
    List<InventoryViewItemDTO> parts
) {

    private static final ObjectMapper mapper = new ObjectMapper();

    public String json() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
