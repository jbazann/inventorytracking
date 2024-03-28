package com.jbazann.inventorytracking.model;

import java.util.UUID;

import com.jbazann.inventorytracking.CommonExceptions;

/**
 * A tangible tagged unit tracked by the system.
 */
public record InventoryPart(
    UUID id,
    String name,
    String encodedName,
    PartState state
) {
    
    public enum PartState{ 
        TRACKING,
        REPLACING,
        RETURNING,
        DISCARDED,
        DELIVERED
    }

    public InventoryPart(String encodedName) {
        this(UUID.randomUUID(),encodedName,encodedName,PartState.TRACKING);
    }

    public InventoryPart(String fullName, String encodedName) {
        this(UUID.randomUUID(),fullName,encodedName,PartState.TRACKING);
    }

    public boolean needsReplacement() {
        return this.state == PartState.REPLACING;
    }

    public InventoryPart replace(final InventoryPart replacement) {
        if(replacement == null) throw new IllegalArgumentException(CommonExceptions.nullArgument());
        if(!needsReplacement()) throw new RuntimeException("Not marked for replacement.");
        if(!replacement.canReplace(this)) throw new IllegalArgumentException("Uncompatible replacement.");
        return new InventoryPart(id, name, encodedName, PartState.RETURNING);
    }

    public boolean is(InventoryPart another) {
        if(another == null) throw new IllegalArgumentException(CommonExceptions.nullArgument());
        if(!this.id().equals(another.id())) return false;
        if(!this.name().equals(another.name())) throw new RuntimeException("Different names found on same id.");
        if(!this.encodedName().equals(another.encodedName())) throw new RuntimeException("Different encoded names found on same id."); 
        return true;
    }

    public boolean canReplace(InventoryPart replaced) {
        if(replaced == null) throw new IllegalArgumentException(CommonExceptions.nullArgument());
        if(!this.state().equals(PartState.TRACKING)) return false;
        return !this.is(replaced);
    }

}
