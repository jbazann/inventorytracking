package com.jbazann.inventorytracking.unit.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.jbazann.inventorytracking.domain.InventoryGroup;
import com.jbazann.inventorytracking.domain.InventoryPart;

/**
 * Unit tests for {@link InventoryGroup}.
 */
@Tag("full")
public class InventoryGroupTests {

    final String validName = "A VERY VALID GROUP NAME STRING";
    private final InventoryGroupTestData data = new InventoryGroupTestData();

    @Test
    public void nonCanonicalConstructorTest() {
        // Name only constructor
        final InventoryGroup nameOnly = new InventoryGroup(validName);
        assertNotNull(nameOnly.name());
        assertNotNull(nameOnly.id());
        assertNotNull(nameOnly.parts());
        assertNotNull(nameOnly.recorded());
        assertEquals(0, nameOnly.parts().size());
        assertEquals(validName, nameOnly.name());
    }

    @Test
    public void addPartTest() {
        final InventoryPart partOne = data.mockPart();
        final InventoryPart partTwo = data.mockPart();
        final InventoryPart partThree = data.mockPart();
        final InventoryGroup addParts = new InventoryGroup(validName);

        // partOne equivalent to partThree, not to partTwo
        data.mockPartIs(partOne,partThree)
                .mockPartIsNot(partOne,partTwo)
                .mockPartIsNot(partThree,partTwo);

        // Regular use
        assertEquals(0, addParts.parts().size());
        assertDoesNotThrow(() -> addParts.addPart(partOne));
        assertEquals(1, addParts.parts().size());
        assertTrue(addParts.parts().contains(partOne));
        // Duplicate insertion
        assertThrows(IllegalArgumentException.class,
                () -> addParts.addPart(partOne));
        assertThrows(IllegalArgumentException.class,
                () -> addParts.addPart(partThree));
        // null insertion
        assertThrows(IllegalArgumentException.class,
                () -> addParts.addPart(null));
        // Add one more to check return chain
        assertEquals(addParts, addParts.addPart(partTwo));
        assertEquals(2, addParts.parts().size());
        assertTrue(addParts.parts().contains(partTwo)
            && addParts.parts().contains(partOne));
    }

    @Test
    public void updateStateTest() {

    }

    @Test
    public void replacePartTest() {
        final InventoryPart replaceA = data.mockPart();
        final InventoryPart replaceB = data.mockPart();
        final InventoryPart replacementC = data.mockPart();
        final InventoryPart replacementD = data.mockPart();
        final InventoryPart replacementE = data.mockPart();
        final InventoryGroup replaceParts = new InventoryGroup(validName);

        data.mockPartNeedsReplacement(replaceA)
            .mockPartNeedsReplacement(replaceB)
            .mockPartCanReplace(replaceA,replacementC)// standard replacement
            .mockPartCanReplace(replaceB,replacementC)// duplicate insertion
            .mockPartCanReplace(replacementC,replacementD)// not marked for replacement
            .mockPartNeedsReplacement(replacementD)// replace not present
            .mockPartCanReplace(replacementD,replacementE)// ^^
            .mockPartCanNotReplace(replaceB,replacementE);// incompatible replacement

        // initialize part list
        replaceParts.addPart(replaceA).addPart(replaceB);
        assertEquals(2, replaceParts.parts().size());

        // replace One
        assertDoesNotThrow(() -> replaceParts.replacePart(replaceA, replacementC));
        assertEquals(2, replaceParts.parts().size());
        assertTrue(replaceParts.parts().contains(replacementC));
        assertTrue(replaceParts.parts().contains(replaceB));
        assertFalse(replaceParts.parts().contains(replaceA));

        // Duplicate replacement
        assertThrows(IllegalArgumentException.class,
                () -> replaceParts.replacePart(replaceB, replacementC));
        // Unmarked replacement
        assertThrows(IllegalArgumentException.class,
                () -> replaceParts.replacePart(replacementC, replacementD));
        // Not present
        assertThrows(IllegalArgumentException.class,
                () -> replaceParts.replacePart(replacementD, replacementE));
        // Incompatible replacement
        assertThrows(RuntimeException.class,
                () -> replaceParts.replacePart(replaceB,replacementE));

        // Null arguments
        assertThrows(IllegalArgumentException.class,
                () -> replaceParts.replacePart(replaceA,null));
        assertThrows(IllegalArgumentException.class,
                () -> replaceParts.replacePart(null,replaceA));
    }

    @Test
    public void updatePartTest() {
        final InventoryPart partStateA = data.mockPart();
        final InventoryPart partStateB = data.mockPart();
        final InventoryPart wrongPart = data.mockPart();
        final InventoryGroup updateParts = new InventoryGroup(validName);

        data.mockPartIs(partStateA,partStateB)
            .mockPartIsNot(partStateA,wrongPart)
            .mockPartIsNot(partStateB,wrongPart);

        updateParts.addPart(partStateA);
        updateParts.updatePart(partStateB);

        assertEquals(1, updateParts.parts().size());
        assertTrue(updateParts.parts().contains(partStateB));
        assertThrows(IllegalArgumentException.class,
                () -> updateParts.updatePart(wrongPart));
        assertThrows(IllegalArgumentException.class,
                () -> updateParts.updatePart(null));
    }

    @Test
    public void hasPartTest() {
        final InventoryPart partOneA = data.mockPart();
        final InventoryPart partOneB = data.mockPart();
        final InventoryPart partTwo = data.mockPart();
        final InventoryGroup hasPart = new InventoryGroup(validName);

        data.mockPartIs(partOneA,partOneB)
            .mockPartIsNot(partOneA,partTwo)
            .mockPartIsNot(partOneB,partTwo);

        hasPart.addPart(partOneA);

        assertTrue(hasPart.hasPart(partOneA));
        assertTrue(hasPart.hasPart(partOneB));
        assertFalse(hasPart.hasPart(partTwo));

        assertThrows(IllegalArgumentException.class,
                () -> hasPart.hasPart(null));
    }
}
