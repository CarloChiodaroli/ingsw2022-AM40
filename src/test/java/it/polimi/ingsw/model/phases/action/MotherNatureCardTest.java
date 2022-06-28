package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.server.model.phase.action.states.cards.InfluenceCard;
import it.polimi.ingsw.server.model.phase.action.states.cards.MotherNatureCard;
import it.polimi.ingsw.server.model.phase.action.states.MotherNatureState;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.MotherNature;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class test the {@link MotherNatureCard} methods, character cards who modify mother nature
 */
public class MotherNatureCardTest {
    /**
     * Test Messenger card
     */
    @Test
    public void motherNatureCardTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        Characters messenger = Characters.MESSENGER;
        MotherNatureCard motherNatureCard = new MotherNatureCard(messenger, game.getActionPhase(),
                CharacterCardFabric.getCharacterization(messenger));
        MotherNatureState motherNatureState = new MotherNatureState(game.getActionPhase());
        motherNatureCard.activator(motherNatureState, game.getPlayers().get(0));
        Optional<Island> initialpos = MotherNature.getMotherNature().getPosition();
        Island inpos = initialpos.get();
        int firstposition = game.getTable().getIslandList().indexOf(inpos);

        motherNatureCard.handle(game.getPlayers().get(0), 6, 5);
        Optional<Island> secondpos = MotherNature.getMotherNature().getPosition();
        Island secpos = secondpos.get();
        int secondposition = game.getTable().getIslandList().indexOf(secpos);
        if (firstposition + 6 > 11)
            assertEquals(firstposition - 12 + 6, secondposition);
        else
            assertEquals(firstposition + 6, secondposition);
        firstposition = secondposition;

        motherNatureCard.handle(game.getPlayers().get(0), 5, 3);
        secondpos = MotherNature.getMotherNature().getPosition();
        secpos = secondpos.get();
        secondposition = game.getTable().getIslandList().indexOf(secpos);
        if (firstposition + 5 > 11)
            assertEquals(firstposition - 12 + 5, secondposition);
        else
            assertEquals(firstposition + 5, secondposition);

    }

}
