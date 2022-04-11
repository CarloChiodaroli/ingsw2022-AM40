package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.phase.action.ActionFase;
import it.polimi.ingsw.model.phase.action.CharacterCard;
import it.polimi.ingsw.model.phase.action.Characters;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class CharacterCardTest {
    @Test
    public void characterCardTest(){
        Game game = new Game();
        ActionFase actionfase = new ActionFase(game);
        CharacterCardIstance charactercard;


    }

}

class CharacterCardIstance extends CharacterCard {
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
        CharacterCardIstance(Map<String, Integer> args, Characters characters, ActionFase actionFase){
        super(createBaseCharacterization(), characters, actionFase);
        }
}




