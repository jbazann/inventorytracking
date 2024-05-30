package com.jbazann.inventorytracking.integration.postgresql;

import com.jbazann.inventorytracking.db.repositories.InventoryGroupRepository;
import com.jbazann.inventorytracking.db.services.InventoryGroupPersistenceService;
import com.jbazann.inventorytracking.domain.InventoryGroup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("integration")
@Tag("integration-postgres")
@Testcontainers
@ContextConfiguration(classes = PostgresIntegrationConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InventoryGroupPostgresIntegrationTest {//TODO incomplete and bad very much

    @Autowired
    private InventoryGroupPersistenceService persistenceService;

    @Autowired
    private InventoryGroupRepository groupRepository;

    @Autowired
    private PostgresIntegrationTestData data;

    @BeforeAll
    void setUp() {
        groupRepository.saveAll(data.inventoryGroups);
    }

    @Test
    public void findAll() {
        groupRepository.findAll().forEach(this::assertContainedInData);
    }

    private void assertContainedInData(InventoryGroup inventoryGroup) {
        assertTrue(data.inventoryGroups.contains(inventoryGroup));
    }

    @Test
    public void findById() {
        groupRepository.findById(data.inventoryGroups.getFirst().id())
                .ifPresentOrElse(this::assertContainedInData, Assertions::fail);
    }

    @Test
    @Transactional
    public void save() {
        final PostgresIntegrationTestData moreData = new PostgresIntegrationTestData(1,10);
        assertEquals(groupRepository.save(moreData.inventoryGroups.getFirst()),
                moreData.inventoryGroups.getFirst());
    }

    @Test
    @Transactional
    public void delete() {
        groupRepository.deleteById(data.inventoryGroups.getFirst().id());
        assertFalse(groupRepository.existsById(data.inventoryGroups.getFirst().id()));
    }

    @Test
    public void get() {
        persistenceService.getLatestAnyState(0, 5);
    }

}
