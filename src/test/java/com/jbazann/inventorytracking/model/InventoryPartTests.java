package com.jbazann.inventorytracking.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.jbazann.inventorytracking.domain.InventoryPart;
import com.jbazann.inventorytracking.domain.InventoryPart.PartState;
import org.junit.jupiter.api.Tag;

/**
 * Unit tests for {@link InventoryPart}.
 */
@Tag("full")
public class InventoryPartTests {
    final String validName = "Yes Es S FULL LENGTH NAME STRING 130u8!!!!#$%&/() and stuff";
    final String validEncodedName = "YES N4m#-LIK3-!848";
    final LocalDateTime someTime = LocalDateTime.now();

    private InventoryPart stateConstructor(InventoryPart.PartState state) {
        return new InventoryPart(UUID.randomUUID(),validName,validEncodedName,state,someTime);
    }

    @Test
    public void nonCanonicalConstructorTest() {
        // Encoded name only constructor
        final InventoryPart encodedOnly = new InventoryPart(validEncodedName);
        assertNotNull(encodedOnly.name());
        assertNotNull(encodedOnly.id());
        assertNotNull(encodedOnly.encodedName());
        assertNotNull(encodedOnly.state());
        assertNotNull(encodedOnly.recorded());
        assertTrue(encodedOnly.name().equals(validEncodedName));

        // Encoded and full name
        final InventoryPart encodedAndFullName = new InventoryPart(validName,validEncodedName);
        assertNotNull(encodedAndFullName.name());
        assertNotNull(encodedAndFullName.id());
        assertNotNull(encodedAndFullName.encodedName());
        assertNotNull(encodedAndFullName.state());
        assertNotNull(encodedAndFullName.recorded());
        assertTrue(encodedAndFullName.name().equals(validName));
        assertTrue(encodedAndFullName.encodedName().equals(validEncodedName));
    }

    @Test
    public void needsReplacementTest() {
        final InventoryPart tracking = stateConstructor(PartState.TRACKING);
        final InventoryPart replacing = stateConstructor(PartState.REPLACING);
        final InventoryPart returning = stateConstructor(PartState.RETURNING);
        final InventoryPart discarded = stateConstructor(PartState.DISCARDED);
        final InventoryPart delivered = stateConstructor(PartState.DELIVERED);
        final InventoryPart randomlyNull = stateConstructor(null);

        assertFalse(randomlyNull.needsReplacement());
        assertFalse(tracking.needsReplacement());
        assertFalse(returning.needsReplacement());
        assertFalse(discarded.needsReplacement());
        assertFalse(delivered.needsReplacement());
        assertTrue(replacing.needsReplacement());
    }

    @Test
    public void replaceTest() {
        final InventoryPart replacing = stateConstructor(PartState.REPLACING);
        final InventoryPart happyPath = mock();
        when(happyPath.canReplace(replacing)).thenReturn(true);

        assertDoesNotThrow(() -> replacing.replace(happyPath));
        final InventoryPart replaced = replacing.replace(happyPath);
        assertTrue(replaced.is(replacing));
        assertEquals(PartState.RETURNING, replaced.state());
        assertEquals(replacing.name(), replaced.name());
        assertEquals(replacing.id(), replaced.id());
        assertEquals(replacing.encodedName(), replaced.encodedName());
        assertThrows(IllegalArgumentException.class, 
        () -> replacing.replace(null));
    }

    @Test
    public void isTest() {
        final InventoryPart partOne = new InventoryPart(validEncodedName);
        final InventoryPart partOneCopy = new InventoryPart(
            partOne.id(),partOne.name(),partOne.encodedName(),
            PartState.TRACKING == partOne.state() ? PartState.REPLACING : PartState.TRACKING,//guarantee different state
            someTime
        );
        final InventoryPart partOneWrongId = new InventoryPart(
            UUID.randomUUID(), partOne.name(),partOne.encodedName(),partOne.state(), partOne.recorded()
        );
        final InventoryPart partOneWrongName = new InventoryPart(
            partOne.id(), partOne.name()+"XD",partOne.encodedName(),partOne.state(), partOne.recorded()
        );
        final InventoryPart partOneWrongEncoded =  new InventoryPart(
            partOne.id(), partOne.name(),partOne.encodedName()+";D",partOne.state(), partOne.recorded()
        );

        assertFalse(partOne.is(partOneWrongId));
        assertThrows(RuntimeException.class, 
        () -> partOne.is(partOneWrongName));
        assertThrows(RuntimeException.class, 
        () -> partOne.is(partOneWrongEncoded));
        assertTrue(partOne.is(partOneCopy));
        assertTrue(partOne.is(partOne));

        assertThrows(IllegalArgumentException.class, 
        () -> partOne.is(null));
    }

    @Test
    public void canReplaceTest() {
        final InventoryPart canReplace = new InventoryPart(validEncodedName);
        final InventoryPart cannotReplace = new InventoryPart(
            canReplace.id(),canReplace.name(),canReplace.encodedName(),PartState.RETURNING, someTime
        );
        final InventoryPart needsReplacement = new InventoryPart(
            UUID.randomUUID(), validName, validEncodedName, PartState.REPLACING, someTime
        );

        assertTrue(canReplace.canReplace(needsReplacement));
        assertFalse(cannotReplace.canReplace(needsReplacement));
        assertFalse(canReplace.canReplace(canReplace));
        assertThrows(IllegalArgumentException.class, 
        () -> canReplace.canReplace(null));
    }

}
