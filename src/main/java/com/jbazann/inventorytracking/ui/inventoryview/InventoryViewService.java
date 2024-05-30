package com.jbazann.inventorytracking.ui.inventoryview;

import java.util.LinkedList;
import java.util.List;

import com.jbazann.inventorytracking.CommonExceptions;
import com.jbazann.inventorytracking.db.services.InventoryGroupPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbazann.inventorytracking.domain.InventoryGroup;
import com.jbazann.inventorytracking.domain.InventoryPart;

@Service
public final class InventoryViewService {
    
    private final InventoryGroupPersistenceService groupPersistenceService;

    @Autowired
    public InventoryViewService(InventoryGroupPersistenceService groupPersistenceService) {
        this.groupPersistenceService = groupPersistenceService;
    }

    public List<InventoryViewItemDTO> getLatestAnyState(final int page, final int pageSize) {
        if(page < 0) throw CommonExceptions.invalidPage();
        if(pageSize < 1) throw CommonExceptions.invalidPageSize();
        final List<InventoryGroup> latest = groupPersistenceService.getLatestAnyState(page,pageSize);
        return dto(latest);
    }

    public List<InventoryViewItemDTO> getIssues(final int page, final int pageSize) {
        if(page < 0) throw CommonExceptions.invalidPage();
        if(pageSize < 1) throw CommonExceptions.invalidPageSize();
        final List<InventoryGroup> issues = groupPersistenceService.getLatestIssues(page,pageSize);
        return dto(issues);
    } 

    private List<InventoryViewItemDTO> dto(final List<InventoryGroup> groups) {
        final List<InventoryViewItemDTO> dtos = new LinkedList<>();
        for(InventoryGroup group : groups) {
            dtos.add(dto(group));
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
                (part) -> dto(part)).toList()
        );
    }

}
