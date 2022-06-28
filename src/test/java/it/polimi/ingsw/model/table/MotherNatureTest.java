package it.polimi.ingsw.model.table;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.MotherNature;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link MotherNature} methods
 */
public class MotherNatureTest {
    /**
     * Test mother nature set and get position
     */
    @Test
    public void motherNatureTest(){
        MotherNature.getMotherNature().resetPosition();
        assertEquals(Optional.empty(), MotherNature.getMotherNature().getPosition());

        Island island = new Island("Position Test");

        MotherNature.getMotherNature().setPosition(island);
        assertEquals(island, MotherNature.getMotherNature().getPosition().get());
    }
}
