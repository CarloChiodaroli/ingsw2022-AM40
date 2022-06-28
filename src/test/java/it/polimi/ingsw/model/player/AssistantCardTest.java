package it.polimi.ingsw.model.player;

import it.polimi.ingsw.server.model.player.AssistantCard;
import it.polimi.ingsw.server.model.player.school.SchoolDashboard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * This class tests the {@link AssistantCard} methods
 */
@DisplayName("Assistant Card test")
public class AssistantCardTest {

    /**
     * Test the creation of assistant cards
     */
    @Test
    @DisplayName("Control the correct functioning of the constructor")
    public void creationTest() {
        AssistantCard card = new AssistantCard(1);
        assertEquals(1, card.getWeight());
        assertEquals(1, card.getNumOfMotherNatureMovements());

        card = new AssistantCard(2);
        assertEquals(2, card.getWeight());
        assertEquals(1, card.getNumOfMotherNatureMovements());

        card = new AssistantCard(3);
        assertEquals(3, card.getWeight());
        assertEquals(2, card.getNumOfMotherNatureMovements());

        card = new AssistantCard(4);
        assertEquals(4, card.getWeight());
        assertEquals(2, card.getNumOfMotherNatureMovements());

        card = new AssistantCard(5);
        assertEquals(5, card.getWeight());
        assertEquals(3, card.getNumOfMotherNatureMovements());

        card = new AssistantCard(6);
        assertEquals(6, card.getWeight());
        assertEquals(3, card.getNumOfMotherNatureMovements());

        card = new AssistantCard(7);
        assertEquals(7, card.getWeight());
        assertEquals(4, card.getNumOfMotherNatureMovements());

        card = new AssistantCard(8);
        assertEquals(8, card.getWeight());
        assertEquals(4, card.getNumOfMotherNatureMovements());

        card = new AssistantCard(9);
        assertEquals(9, card.getWeight());
        assertEquals(5, card.getNumOfMotherNatureMovements());

        card = new AssistantCard(10);
        assertEquals(10, card.getWeight());
        assertEquals(5, card.getNumOfMotherNatureMovements());
    }

    /**
     * Test to compare two cards
     */
    @Test
    @DisplayName("Test of the compareTo function")
    public void compareTest() {
        AssistantCard card1;
        AssistantCard card2;
        for (int i = 1; i <= 10; i++) {
            card1 = new AssistantCard(i);
            for (int j = i; j <= 10; j++) {
                card2 = new AssistantCard(j);
                assertEquals(i - j, card1.compareTo(card2));
            }
        }
    }

    /**
     * Test play card
     */
    @Test
    public void miscellaneousTest() {
        AssistantCard card1 = new AssistantCard(5);
        AssistantCard card2 = new AssistantCard(5);

        assertEquals(card1.hashCode(), card2.hashCode());
        assertEquals("AssistantCard{" + "weight=" + "5" + ", numOfMotherNatureMovements=" + "3" + '}', card1.toString());
        assertEquals(card1.toString(), card2.toString());
    }
}
