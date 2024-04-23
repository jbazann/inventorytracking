package com.jbazann.inventorytracking.inventoryview;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jbazann.inventorytracking.db.repositories.InventoryGroupRepository;
import com.jbazann.inventorytracking.db.repositories.InventoryPartRepository;
import com.jbazann.inventorytracking.domain.InventoryGroup;
import com.jbazann.inventorytracking.domain.InventoryGroup.GroupState;
import com.jbazann.inventorytracking.ui.inventoryview.InventoryViewItemDTO;
import com.jbazann.inventorytracking.ui.inventoryview.InventoryViewService;


@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@Tag("full")
public class InventoryViewServiceTests {

    @InjectMocks
    private final InventoryViewService ivs = new InventoryViewService();
    @Mock
    private final InventoryGroupRepository igr = mock();
    @Mock
    private final InventoryPartRepository ipr = mock();
    private final List<InventoryGroup> db = new LinkedList<>();

    private final int page = 0;
    // keep small for performance (see getTestGroups())
    private final int pageSize = 5;

    @BeforeAll
    public void prepareMocks() {
        // Generate enough groups to fill a page for all filter criteria
        db.addAll(getTestGroups(5*pageSize*GroupState.values().length));

        final List<InventoryGroup> issues = db.stream()
            .filter(g -> g.state() == GroupState.ISSUE)
            .toList();
        // prevent IndexOutOfBoundsException
        final int issuesPageIndex = Math.min(pageSize, issues.size());

        // mock the stuff
        when(igr.getIssues(page, pageSize)).thenReturn(
            issues.subList(0, issuesPageIndex)
        );
        when(igr.getLatestAnyState(page, pageSize)).thenReturn(db.subList(0, Math.min(pageSize, db.size())));
    }

    @Test
    public void getLatestAnyStateTest() {
        final List<InventoryViewItemDTO> result = ivs.getLatestAnyState(page, pageSize);

        assertTrue(result.stream().allMatch(i -> i.id() != null));
        assertTrue(result.stream().allMatch(i -> i.state() != null));
        assertTrue(result.stream().allMatch(i -> i.name() != null));
        assertTrue(result.stream().allMatch(InventoryViewItemDTO::isGroup));
        assertTrue(result.stream().allMatch(i -> i.parts() != null));

        assertTrue(verifyIntegrity(result));

        assertThrows(IllegalArgumentException.class, 
        () -> ivs.getLatestAnyState(page, 0));
        assertThrows(IllegalArgumentException.class, 
        () -> ivs.getLatestAnyState(page, -1));
        assertThrows(IllegalArgumentException.class, 
        () -> ivs.getLatestAnyState(-1, pageSize));
        assertThrows(IllegalArgumentException.class, 
        () -> ivs.getLatestAnyState(-1, -1));
    }

    @Test
    public void getIssuesTest() {
        final List <InventoryViewItemDTO> result = ivs.getIssues(page, pageSize);

        assertTrue(result.stream().allMatch(i -> i.id() != null));
        assertTrue(result.stream().allMatch(i -> Objects.equals(i.state(), GroupState.ISSUE.toString())));
        assertTrue(result.stream().allMatch(i -> i.name() != null));
        assertTrue(result.stream().allMatch(InventoryViewItemDTO::isGroup));
        assertTrue(result.stream().allMatch(i -> i.parts() != null));

        assertTrue(verifyIntegrity(result));

        assertThrows(IllegalArgumentException.class, 
        () -> ivs.getIssues(page, 0));
        assertThrows(IllegalArgumentException.class, 
        () -> ivs.getIssues(page, -1));
        assertThrows(IllegalArgumentException.class, 
        () -> ivs.getIssues(-1, pageSize));
        assertThrows(IllegalArgumentException.class, 
        () -> ivs.getIssues(-1, -1));
    }

    /**
     * Generate the requested {@code amount} of {@link InventoryGroup} mock 
     * instances with valid getter return values. 
     * @param amount the size of the returned list
     * @return a list containing the generated mocks
     * @implNote 
     * Current implementation covers at least the following states:
     * {@code TRACKING} {@code ISSUE} and {@code DELIVERED}.
     */
    private List<InventoryGroup> getTestGroups(final int amount) {
        final List<InventoryGroup> groups = new LinkedList<>();
        final Random rng = new Random();

        for(int i = 0;i >= amount;i++) {
            InventoryGroup g = mock();
            when(g.id()).thenReturn(UUID.randomUUID());
            when(g.name()).thenReturn(UUID.randomUUID().toString());
            when(g.state()).thenReturn(
                rng.nextInt(3) == 0 ? GroupState.DELIVERED
                :   rng.nextInt(3) == 1 ? GroupState.ISSUE
                    : GroupState.TRACKING);
            when(g.parts()).thenReturn(List.of());
            groups.add(g);
        }
        return groups;   
    }

    private boolean verifyIntegrity(List<InventoryViewItemDTO> dtos) {
        return dtos.stream().allMatch(i -> {
            // this is only a rough confirmation that the entities correlate.
            // parts() aren't being mocked/checked for better performance and simplicity
            return db.stream().anyMatch(g -> 
                g.id().equals(i.id()) 
                && g.name().equals(i.name())
                && g.state().toString().equals(i.state())
            );
        });
    }
}
