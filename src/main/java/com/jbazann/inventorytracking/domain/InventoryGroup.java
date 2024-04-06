package com.jbazann.inventorytracking.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.jbazann.inventorytracking.CommonExceptions;

/**
 * An {@link InventoryPart} collection that constitutes a deliverable entity. 
 * {@link InventoryGroup} contains the state of said entity, and tracks
 * the history of events and issues that may occur between the initial dispatch
 * of the parts, and the delivery and successful assembly of the whole.
 */
public record InventoryGroup(
    UUID id,
    String name,
    GroupState state,
    List<InventoryPart> parts
) {

    public enum GroupState {
        TRACKING,
        ISSUE,
        DELIVERED
    }

    public InventoryGroup(String name) {
        this(UUID.randomUUID(),name,GroupState.TRACKING,new LinkedList<InventoryPart>());
    }

    public InventoryGroup addPart(final InventoryPart p) {
        if(p == null) throw new IllegalArgumentException(CommonExceptions.nullArgument());
        for(InventoryPart part : parts) if(part.is(p))
            throw new IllegalArgumentException("Part "+p.name()+" is already in this group.");
        parts.add(p);
        return this;
    }
    
    public void replacePart(final InventoryPart oldPart, final InventoryPart newPart) {
        if(oldPart == null || newPart == null) throw new IllegalArgumentException(CommonExceptions.nullArgument());
        if(!parts.contains(oldPart)) throw new IllegalArgumentException("The replaced part ("+oldPart.name()+") is not in this group.");
        if(parts.contains(newPart)) throw new IllegalArgumentException("The replacement part ("+newPart.name()+") is already in this group.");
        if(!oldPart.needsReplacement()) throw new IllegalArgumentException("Part "+oldPart.name()+" is not flagged for replacement.");
        if(!newPart.canReplace(oldPart)) throw new RuntimeException("The replacement part ("+newPart.name()+") is not compatible.");
        if(!parts.remove(oldPart)) throw new RuntimeException("The replaced part was missing after it was found.");
        parts.add(newPart);
    }

    public void updatePart(final InventoryPart p) {
        if(p == null) throw new IllegalArgumentException(CommonExceptions.nullArgument());
        if(!hasPart(p)) throw new IllegalArgumentException("Part "+p.name()+" is not in this group.");

        if(!parts.removeIf(part -> part.is(p)))
            throw new RuntimeException("The outdated part was missing after it was found. Nice job.");
        parts.add(p);
    }

    public boolean hasPart(InventoryPart part) {
        if(part == null) throw new IllegalArgumentException(CommonExceptions.nullArgument());
        return parts.stream().anyMatch(p -> p.is(part));
    }

}
