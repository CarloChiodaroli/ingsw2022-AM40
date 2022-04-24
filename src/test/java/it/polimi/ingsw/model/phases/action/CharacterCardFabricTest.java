package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.model.phase.action.states.cards.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
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

        CharacterCard card = CharacterCardFabric.createCard(character, game.getActionFase());

        switch(CharacterCardFabric.getClassOfCard(character)){
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
        List<CharacterCard> cards = CharacterCardFabric.getCards(game.getActionFase());

        assertEquals(3, cards.size());

        List<Characters> presentCharacters = cards.stream()
                .map(CharacterCard::getCharacter)
                .distinct()
                .collect(Collectors.toList());
        assertEquals(3, presentCharacters.size());
    }
}
