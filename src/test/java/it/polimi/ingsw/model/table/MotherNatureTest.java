package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MotherNatureTest {
    @Test
    public void motherNatureTest(){
        MotherNature.getMotherNature().resetPosition();
        assertEquals(Optional.empty(), MotherNature.getMotherNature().getPosition());

        Island island = new Island("Position Test");

        MotherNature.getMotherNature().setPosition(island);
        assertEquals(island, MotherNature.getMotherNature().getPosition().get());
    }
}
