package com.jbazann.inventorytracking.ui.inventoryview;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbazann.inventorytracking.db.repositories.InventoryGroupRepository;
import com.jbazann.inventorytracking.domain.InventoryGroup;
import com.jbazann.inventorytracking.domain.InventoryPart;

@Service
public final class InventoryViewService {
    
    @Autowired
    private InventoryGroupRepository groupRepository;

    public List<InventoryViewItemDTO> getLatestAnyState(final int page, final int pageSize) {
        if(page < 0) throw new IllegalArgumentException("page can't be less than zero.");
        if(pageSize < 1) throw new IllegalArgumentException("pageSize can't be less than one");
        final List<InventoryGroup> latest = groupRepository.getLatestAnyState(page,pageSize);
        return dto(latest);
    }

    public List<InventoryViewItemDTO> getIssues(final int page, final int pageSize) {
        if(page < 0) throw new IllegalArgumentException("page can't be less than zero.");
        if(pageSize < 1) throw new IllegalArgumentException("pageSize can't be less than one");
        final List<InventoryGroup> issues = groupRepository.getIssues(page,pageSize);
        return dto(issues);
    } 

    private List<InventoryViewItemDTO> dto(final List<InventoryGroup> groups) {
        final List<InventoryViewItemDTO> dtos = new LinkedList<>();
        for(InventoryGroup group : groups) {
            dtos.add(dto(group));
            for(InventoryPart part : group.parts()) {
                dtos.add(dto(part));
            }
        }
        return dtos;
    }

    private InventoryViewItemDTO dto(final InventoryPart part) {
        return new InventoryViewItemDTO(
            part.id(),
            part.name(),
            part.encodedName(),
            part.state().toString(), 
            false, 
            part.recorded().toString(),
            null
        );
    }

    private InventoryViewItemDTO dto(final InventoryGroup group) {
        return new InventoryViewItemDTO(
            group.id(),
            group.name(),
            null,
            group.state().toString(),
            true,
            null,
            group.parts().stream().map(
                (part) -> part.encodedName()).toList()
        );
    }

}
