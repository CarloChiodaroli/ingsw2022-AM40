package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.phase.action.Characters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("Character test")
public class CharactersTest {

    /**
     * Tests that the casual creation of characters gives a character
     */
    @RepeatedTest(100)
    @DisplayName("Casual Character getter gives a character")
    public void casualCreationTest() {
        Characters character = Characters.getRandomCharacter();
        assertTrue(Arrays.stream(Characters.values()).toList().contains(character));
    }

    /**
     * Control if the right class of card is given
     */
    @Test
    @DisplayName("Control if the right class of card is given")
    public void getClassOfCardTest() {
        assertEquals("StudentMovement", Characters.getClassOfCard(Characters.HOST));
        assertEquals("StudentMovement", Characters.getClassOfCard(Characters.FRIAR));
        assertEquals("StudentMovement", Characters.getClassOfCard(Characters.QUEEN));
        assertEquals("StudentMovement", Characters.getClassOfCard(Characters.THIEF));
        assertEquals("StudentMovement", Characters.getClassOfCard(Characters.JESTER));
        assertEquals("StudentMovement", Characters.getClassOfCard(Characters.MINSTREL));
        assertEquals("MotherNature", Characters.getClassOfCard(Characters.MESSENGER));
        assertEquals("Influence", Characters.getClassOfCard(Characters.SORCERESS));
        assertEquals("Influence", Characters.getClassOfCard(Characters.SORCERER));
        assertEquals("Influence", Characters.getClassOfCard(Characters.CENTAUR));
        assertEquals("Influence", Characters.getClassOfCard(Characters.KNIGHT));
        assertEquals("Influence", Characters.getClassOfCard(Characters.CRIER));
    }

    /**
     * Controls the right characterization of the different characters
     */
    @Nested
    @DisplayName("Control the right characterization")
    public class CharacterizationTest {

        /**
         * Characterization test for Friar character (1st)
         */
        @Test
        @DisplayName("Friar")
        public void characterizationFriarTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.FRIAR);

            assertEquals(1, characterization.get("Price"));
            assertEquals(4, characterization.get("Memory"));
            assertEquals(1, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(1, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Host character (2nd)
         */
        @Test
        @DisplayName("Host")
        public void characterizationHostTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.HOST);

            assertEquals(2, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(3, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(1, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Crier character (3rd)
         */
        @Test
        @DisplayName("Crier")
        public void characterizationCrierTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.CRIER);

            assertEquals(3, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(0, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(1, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Messenger character (4th)
         */
        @Test
        @DisplayName("Messenger")
        public void characterizationMessengerTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.MESSENGER);

            assertEquals(1, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(0, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(2, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Sorceress character (5th)
         */
        @Test
        @DisplayName("Sorceress")
        public void characterizationSorceressTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.SORCERESS);

            assertEquals(2, characterization.get("Price"));
            assertEquals(4, characterization.get("Memory"));
            assertEquals(0, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(1, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Centaur character (6th)
         */
        @Test
        @DisplayName("Centaur")
        public void characterizationCentaurTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.CENTAUR);

            assertEquals(3, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(0, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(1, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Jester character (7th)
         */
        @Test
        @DisplayName("Jester")
        public void characterizationJesterTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.JESTER);

            assertEquals(1, characterization.get("Price"));
            assertEquals(6, characterization.get("Memory"));
            assertEquals(3, characterization.get("Usages"));
            assertEquals(1, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(1, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Knight character (8th)
         */
        @Test
        @DisplayName("Knight")
        public void characterizationKnightTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.KNIGHT);

            assertEquals(2, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(0, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(1, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Sorcerer character (9th)
         */
        @Test
        @DisplayName("Sorcerer")
        public void characterizationSorcererTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.SORCERER);

            assertEquals(3, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(0, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(1, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Minstrel character (10th)
         */
        @Test
        @DisplayName("Minstrel")
        public void characterizationMinstrelTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.MINSTREL);

            assertEquals(1, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(2, characterization.get("Usages"));
            assertEquals(1, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(1, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(2, characterization.get("Student"));
            assertEquals(1, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Queen character (11th)
         */
        @Test
        @DisplayName("Queen")
        public void characterizationQueenTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.QUEEN);

            assertEquals(2, characterization.get("Price"));
            assertEquals(4, characterization.get("Memory"));
            assertEquals(1, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(1, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Thief character (12th)
         */
        @Test
        @DisplayName("Thief")
        public void characterizationThiefTest() {
            Map<String, Integer> characterization = Characters.getCharacterization(Characters.THIEF);

            assertEquals(3, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(0, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("AddToMaxHops"));
            assertEquals(0, characterization.get("DecoratesNoExpert"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(1, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(0, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(0, characterization.get("Student"));
            assertEquals(0, characterization.get("Entrance"));
        }
    }
}
