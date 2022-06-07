package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.server.model.enums.ActionPhaseStateType;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.model.enums.CharactersLookup;
import it.polimi.ingsw.server.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.server.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.server.model.phase.action.states.cards.InfluenceCard;
import it.polimi.ingsw.server.model.phase.action.states.cards.MotherNatureCard;
import it.polimi.ingsw.server.model.phase.action.states.cards.StudentMovementCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Character Card Fabric")
public class CharacterCardFabricTest {

    @DisplayName("Test of character card's creation")
    @ParameterizedTest
    @EnumSource(Characters.class)
    public void cardCreation(Characters character){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();

        CharacterCard card = CharacterCardFabric.createCard(character, game.getActionPhase());

        switch(CharactersLookup.getType(character)){
            case STUDENT -> {
                assertEquals(StudentMovementCard.class, card.getClass());
                assertNotEquals(InfluenceCard.class, card.getClass());
                assertNotEquals(MotherNatureCard.class, card.getClass());
            }
            case MOTHER -> {
                assertNotEquals(StudentMovementCard.class, card.getClass());
                assertNotEquals(InfluenceCard.class, card.getClass());
                assertEquals(MotherNatureCard.class, card.getClass());
            }
            case INFLUENCE -> {
                assertNotEquals(StudentMovementCard.class, card.getClass());
                assertEquals(InfluenceCard.class, card.getClass());
                assertNotEquals(MotherNatureCard.class, card.getClass());
            }
        }
        assertEquals(character, card.getCharacter());
    }

    @DisplayName("Test of random card creation")
    @RepeatedTest(30)
    public void randomCardCreation(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        Map<Characters, CharacterCard> cards = CharacterCardFabric.getCards(game.getActionPhase());

        assertEquals(3, cards.size());

        List<Characters> presentCharacters = cards.values().stream()
                .map(CharacterCard::getCharacter)
                .distinct()
                .collect(Collectors.toList());
        assertEquals(3, presentCharacters.size());
    }
}
