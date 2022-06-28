package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.commons.enums.Characters;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;

/**
 * This class tests the {@link CharacterCard} methods
 */
public class CharacterCardTest {
    /**
     * Test istance a card
     */
    @Test
    public void characterCardTest(){
        Game game = new Game();
        ActionPhase actionfase = new ActionPhase(game);
        CharacterCardIstance charactercard;


    }

}

class CharacterCardIstance extends CharacterCard {
    /**
     * Test characterizations
     * @return map with characterizations and int
     */
    private static Map<String, Integer> createBaseCharacterization(){
        Map<String, Integer> result = new HashMap<>();

        result.put("Price", 0); // Card Price
        result.put("Memory", 0); // Card memory size
        result.put("Usages", 0); // How many times the card is used
        result.put("Bidirectional", 0); // Has a bidirectional behaviour
        result.put("TeacherBehaviour", 0); // Changes behaviour of the movement of teachers
        result.put("EffectAllPlayers", 0); // Has effect to all players in this turn
        result.put("NoEntrySetter", 0); // Defines if it needs to set no entry tile
        // On how many things the card works on
        result.put("Island", 0);
        result.put("Player", 0);
        result.put("Room",0);
        result.put("Tower",0);
        result.put("Student",0);
        result.put("Entrance",0);

        return result;
    }
        CharacterCardIstance(Map<String, Integer> args, Characters characters, ActionPhase actionPhase){
        super(createBaseCharacterization(), characters, actionPhase);
        }
}




