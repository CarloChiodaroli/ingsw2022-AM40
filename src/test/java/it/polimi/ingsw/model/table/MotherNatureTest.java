package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MotherNatureTest {
    @Test
    public void motherNatureTest(){
        Island island = new Island("Position Test");
        assertNull(MotherNature.getMotherNature().getPosition());

        MotherNature.getMotherNature().setPosition(island);
        assertEquals(island,MotherNature.getMotherNature().getPosition());
    }
}
