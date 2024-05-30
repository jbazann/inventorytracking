package com.jbazann.inventorytracking.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.jbazann.inventorytracking.CommonExceptions;
import jakarta.persistence.*;

/**
 * A tangible tagged unit tracked by the system.
 */
@Entity
@Table(name="inventory_part", schema="inventory")
public record InventoryPart(
    @Column @Id @GeneratedValue UUID id,
    @Column String name,
    @Column String encodedName,
    @Column @Enumerated(EnumType.STRING) PartState state,
    @Column LocalDateTime recorded
) {
    
    public enum PartState{ 
        TRACKING,
        REPLACING,
        RETURNING,
        DISCARDED,
        DELIVERED
    }

    public InventoryPart(String encodedName) {
        this(UUID.randomUUID(),encodedName,encodedName,PartState.TRACKING,LocalDateTime.now());
    }

    public InventoryPart(String fullName, String encodedName) {
        this(UUID.randomUUID(),fullName,encodedName,PartState.TRACKING, LocalDateTime.now());
    }

    public boolean needsReplacement() {
        return this.state == PartState.REPLACING;
    }

    public InventoryPart replace(final InventoryPart replacement) {
        if(replacement == null) throw new IllegalArgumentException(CommonExceptions.nullArgument());
        if(!needsReplacement()) throw new RuntimeException("Not marked for replacement.");
        if(!replacement.canReplace(this)) throw new IllegalArgumentException("Uncompatible replacement.");
        return new InventoryPart(id, name, encodedName, PartState.RETURNING, LocalDateTime.now());
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
