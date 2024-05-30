package com.jbazann.inventorytracking.db.services;

import com.jbazann.inventorytracking.CommonExceptions;
import com.jbazann.inventorytracking.db.repositories.InventoryGroupRepository;
import com.jbazann.inventorytracking.domain.InventoryGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryGroupPersistenceService {

    private final InventoryGroupRepository groupRepository;

    @Autowired
    public InventoryGroupPersistenceService(InventoryGroupRepository groupRepository) {
        this.groupRepository  = groupRepository;
    }

    public List<InventoryGroup> getLatestAnyState(final int page, final int pageSize) {
        if(page < 0) throw CommonExceptions.invalidPage();
        if(pageSize < 1) throw CommonExceptions.invalidPageSize();
        return groupRepository.findAll(PageRequest.of(page,pageSize)).getContent();
    }

    public List<InventoryGroup> getLatestIssues(final int page, final int pageSize) {
        if(page < 0) throw CommonExceptions.invalidPage();
        if(pageSize < 1) throw CommonExceptions.invalidPageSize();
        final Example<InventoryGroup> example = Example.of(new InventoryGroup(null, null,
                InventoryGroup.GroupState.ISSUE, null, null
        ));
        return groupRepository.findAll(example,PageRequest.of(page,pageSize)).getContent();
    }

}
