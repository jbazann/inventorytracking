package com.jbazann.inventorytracking.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link InventoryGroup}.
 */
public class InventoryGroupTests {

    final String validName = "A VERY VALID GROUP NAME STRING";

    @Test
    public void nonCanonicalConstructorTest() {
        // Name only constructor
        final InventoryGroup nameOnly = new InventoryGroup(validName);
        assertNotNull(nameOnly.name());
        assertNotNull(nameOnly.id());
        assertNotNull(nameOnly.parts());
        assertEquals(0, nameOnly.parts().size());
        assertTrue(nameOnly.name().equals(validName));
    }

    @Test
    public void addPartTest() {
        final InventoryPart partOne = mock();
        final InventoryPart partTwo = mock();
        final InventoryPart partThree = mock();
        final InventoryGroup addParts = new InventoryGroup(validName);

        when(partOne.is(partOne)).thenReturn(true);
        when(partOne.is(partTwo)).thenReturn(false);
        when(partOne.is(partThree)).thenReturn(true);
        when(partTwo.is(partOne)).thenReturn(false);
        when(partTwo.is(partTwo)).thenReturn(true);
        when(partTwo.is(partThree)).thenReturn(false);
        when(partThree.is(partOne)).thenReturn(true);
        when(partThree.is(partTwo)).thenReturn(false);
        when(partThree.is(partThree)).thenReturn(true);

        assertEquals(0, addParts.parts().size());
        assertDoesNotThrow(() -> addParts.addPart(partOne));
        assertEquals(1, addParts.parts().size());
        assertTrue(addParts.parts().contains(partOne));
        // Duplicate insertion
        assertThrows(IllegalArgumentException.class, 
        () -> addParts.addPart(partOne));
        assertThrows(IllegalArgumentException.class, 
        () -> addParts.addPart(partThree));
        // Add one more to check return chain
        assertEquals(addParts, addParts.addPart(partTwo));
        assertEquals(2, addParts.parts().size());
        assertTrue(addParts.parts().contains(partTwo) 
            && addParts.parts().contains(partOne));
    }

    @Test
    public void replacePartTest() {
        final InventoryPart replaceOne = mock();
        final InventoryPart replaceTwo = mock();
        final InventoryPart replacementOne = mock();
        final InventoryPart replacementTwo = mock();
        final InventoryGroup replaceParts = new InventoryGroup(validName);

        when(replaceOne.needsReplacement()).thenReturn(true);
        when(replaceTwo.needsReplacement()).thenReturn(true);
        when(replacementOne.needsReplacement()).thenReturn(false);
        when(replacementOne.canReplace(replaceOne)).thenReturn(true);
        // Conditions for duplicate replacement
        when(replacementOne.is(replacementOne)).thenReturn(true);// necessary for duplicate detection
        when(replacementOne.canReplace(replaceTwo)).thenReturn(true);// necessary for compatibility check
        // Conditions for unmarked replacement
        when(replacementTwo.canReplace(replacementOne)).thenReturn(true);
       
        // initialize part list
        replaceParts.addPart(replaceOne).addPart(replaceTwo);
        assertTrue(replaceParts.parts().size() == 2);
        
        // replace One
        assertDoesNotThrow(() -> replaceParts.replacePart(replaceOne, replacementOne));
        assertTrue(replaceParts.parts().size() == 2);
        assertTrue(replaceParts.parts().contains(replacementOne));
        assertTrue(replaceParts.parts().contains(replaceTwo));
        assertFalse(replaceParts.parts().contains(replaceOne));
        
        // Attempt duplicate replacement
        assertThrows(IllegalArgumentException.class, 
        () -> replaceParts.replacePart(replaceTwo, replacementOne));
        // Attempt unmarked replacement
        assertThrows(IllegalArgumentException.class, 
        () -> replaceParts.replacePart(replacementOne, replacementTwo));

        // Null arguments
        assertThrows(IllegalArgumentException.class,
        () -> replaceParts.replacePart(replaceOne,null));
        assertThrows(IllegalArgumentException.class,
        () -> replaceParts.replacePart(null,replaceOne));
    }

    @Test
    public void updatePartTest() {
        final InventoryPart partStateA = mock();
        final InventoryPart partStateB = mock();
        final InventoryPart wrongPart = mock();
        final InventoryGroup updateParts = new InventoryGroup(validName);

        when(partStateA.is(partStateB)).thenReturn(true);
        when(partStateB.is(partStateA)).thenReturn(true);
        when(partStateB.is(wrongPart)).thenReturn(false);
        when(wrongPart.is(partStateB)).thenReturn(false);

        updateParts.addPart(partStateA);
        updateParts.updatePart(partStateB);

        assertTrue(updateParts.parts().size() == 1);
        assertTrue(updateParts.parts().contains(partStateB));
        assertThrows(IllegalArgumentException.class, 
        () -> updateParts.updatePart(wrongPart));
        assertThrows(IllegalArgumentException.class, 
        () -> updateParts.updatePart(null));
    }

    @Test
    public void hasPartTest() {
        final InventoryPart partOneA = mock();
        final InventoryPart partTwo = mock();
        final InventoryPart partOneB = mock();
        final InventoryGroup hasPart = new InventoryGroup(validName);
    
        when(partOneA.is(partOneA)).thenReturn(true);
        when(partOneA.is(partOneB)).thenReturn(true);
        when(partOneB.is(partOneA)).thenReturn(true);
        when(partOneA.is(partTwo)).thenReturn(false);
        when(partTwo.is(partOneA)).thenReturn(false);

        assertFalse(hasPart.hasPart(partOneA));
        assertFalse(hasPart.hasPart(partTwo));
        assertFalse(hasPart.hasPart(partOneB));

        hasPart.addPart(partOneA);
        
        assertTrue(hasPart.hasPart(partOneA));
        assertFalse(hasPart.hasPart(partTwo));
        assertTrue(hasPart.hasPart(partOneB));

        assertThrows(IllegalArgumentException.class, 
        () -> hasPart.hasPart(null));
    }

}
