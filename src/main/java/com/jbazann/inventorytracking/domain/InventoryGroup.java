package com.jbazann.inventorytracking.domain;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.jbazann.inventorytracking.CommonExceptions;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

/**
 * An {@link InventoryPart} collection that constitutes a deliverable entity. 
 * {@link InventoryGroup} contains the state of said entity, and tracks
 * the history of events and issues that may occur between the initial dispatch
 * of the parts, and the delivery and successful assembly of the whole.
 */
@Entity
@Table(name="inventory_group", schema="inventory")
public record InventoryGroup(
    @Column @Id @GeneratedValue UUID id,
    @Column String name,
    @Column @Enumerated(EnumType.STRING) GroupState state,
    @Column LocalDateTime recorded,
    @Column @OneToMany(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL) List<InventoryPart> parts
) {

    public enum GroupState {
        TRACKING,
        ISSUE,
        DELIVERED
    }

    public InventoryGroup(String name) {
        this(UUID.randomUUID(),name,GroupState.TRACKING,LocalDateTime.now(),new LinkedList<InventoryPart>());
    }

    public InventoryGroup updateState() {//TODO use/test
        GroupState newState = GroupState.TRACKING;
        if(parts.stream().allMatch(p -> p.state().equals(InventoryPart.PartState.TRACKING))) {
            newState = GroupState.TRACKING;
        } else if (parts.stream().allMatch(p -> p.state().equals(InventoryPart.PartState.DELIVERED))) {
            newState = GroupState.DELIVERED;
        } else {
            newState = GroupState.ISSUE;
        }
        if(state.equals(newState)) return this;
        return new InventoryGroup(id,name,newState,LocalDateTime.now(),parts);
    }

    public List<InventoryPart> getIssues() {//TODO use/test
        return parts.stream()
                .filter(p ->
                    !p.state().equals(InventoryPart.PartState.TRACKING) &&
                    !p.state().equals(InventoryPart.PartState.DELIVERED))
                .toList();
    }

    public InventoryGroup addPart(final InventoryPart part) {
        if(part == null) throw CommonExceptions.nullArgument("part");
        for(InventoryPart p : parts) if(p.is(part))
            throw new IllegalArgumentException("Part "+part.name()+" is already in this group.");
        parts.add(part);
        return this;
    }
    
    public void replacePart(final InventoryPart oldPart, final InventoryPart newPart) {
        if(oldPart == null || newPart == null) throw CommonExceptions.nullArgument();
        if(!parts.contains(oldPart)) throw new IllegalArgumentException("The replaced part ("+oldPart.name()+") is not in this group.");
        if(parts.contains(newPart)) throw new IllegalArgumentException("The replacement part ("+newPart.name()+") is already in this group.");
        if(!oldPart.needsReplacement()) throw new IllegalArgumentException("Part "+oldPart.name()+" is not flagged for replacement.");
        if(!newPart.canReplace(oldPart)) throw new RuntimeException("The replacement part ("+newPart.name()+") is not compatible.");
        if(!parts.remove(oldPart)) throw new RuntimeException("The replaced part was missing after it was found.");
        parts.add(newPart);
    }

    public void updatePart(final InventoryPart part) {
        if(part == null) throw CommonExceptions.nullArgument("part");
        if(!hasPart(part)) throw new IllegalArgumentException("Part "+part.name()+" is not in this group.");

        if(!parts.removeIf(p -> p.is(part)))
            throw new RuntimeException("The outdated part was missing after it was found.");
        parts.add(part);
    }

    public boolean hasPart(InventoryPart part) {
        if(part == null) throw CommonExceptions.nullArgument("part");
        return parts.stream().anyMatch(p -> p.is(part));
    }

}
