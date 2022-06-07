package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.server.model.enums.ActionPhaseStateType;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.model.enums.CharactersLookup;
import it.polimi.ingsw.server.model.phase.action.states.cards.CharacterCardFabric;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Character test")
public class CharactersTest {

    /**
     * Control if the right class of card is given
     */
    @Test
    @DisplayName("Control if the right class of card is given")
    public void getClassOfCardTest() {
        assertEquals(ActionPhaseStateType.STUDENT, CharactersLookup.getType(Characters.HOST));
        assertEquals(ActionPhaseStateType.STUDENT, CharactersLookup.getType(Characters.FRIAR));
        assertEquals(ActionPhaseStateType.STUDENT, CharactersLookup.getType(Characters.QUEEN));
        assertEquals(ActionPhaseStateType.STUDENT, CharactersLookup.getType(Characters.THIEF));
        assertEquals(ActionPhaseStateType.STUDENT, CharactersLookup.getType(Characters.JESTER));
        assertEquals(ActionPhaseStateType.STUDENT, CharactersLookup.getType(Characters.MINSTREL));
        assertEquals(ActionPhaseStateType.MOTHER, CharactersLookup.getType(Characters.MESSENGER));
        assertEquals(ActionPhaseStateType.INFLUENCE, CharactersLookup.getType(Characters.SORCERESS));
        assertEquals(ActionPhaseStateType.INFLUENCE, CharactersLookup.getType(Characters.SORCERER));
        assertEquals(ActionPhaseStateType.INFLUENCE, CharactersLookup.getType(Characters.CENTAUR));
        assertEquals(ActionPhaseStateType.INFLUENCE, CharactersLookup.getType(Characters.KNIGHT));
        assertEquals(ActionPhaseStateType.INFLUENCE, CharactersLookup.getType(Characters.CRIER));
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
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.FRIAR);

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
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.HOST);

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
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.CRIER);

            assertEquals(3, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
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
         * Characterization test for Messenger character (4th)
         */
        @Test
        @DisplayName("Messenger")
        public void characterizationMessengerTest() {
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.MESSENGER);

            assertEquals(1, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(1, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
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
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.SORCERESS);

            assertEquals(2, characterization.get("Price"));
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
         * Characterization test for Centaur character (6th)
         */
        @Test
        @DisplayName("Centaur")
        public void characterizationCentaurTest() {
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.CENTAUR);

            assertEquals(3, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(1, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
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
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.JESTER);

            assertEquals(1, characterization.get("Price"));
            assertEquals(6, characterization.get("Memory"));
            assertEquals(3, characterization.get("Usages"));
            assertEquals(1, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(1, characterization.get("Player"));
            assertEquals(0, characterization.get("Room"));
            assertEquals(0, characterization.get("Tower"));
            assertEquals(2, characterization.get("Student"));
            assertEquals(1, characterization.get("Entrance"));
        }

        /**
         * Characterization test for Knight character (8th)
         */
        @Test
        @DisplayName("Knight")
        public void characterizationKnightTest() {
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.KNIGHT);

            assertEquals(2, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(1, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(2, characterization.get("Player"));
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
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.SORCERER);

            assertEquals(3, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(1, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
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
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.MINSTREL);

            assertEquals(1, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(2, characterization.get("Usages"));
            assertEquals(1, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(1, characterization.get("Player"));
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
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.QUEEN);

            assertEquals(2, characterization.get("Price"));
            assertEquals(4, characterization.get("Memory"));
            assertEquals(1, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
            assertEquals(0, characterization.get("TeacherBehaviour"));
            assertEquals(0, characterization.get("EffectAllPlayers"));

            assertEquals(0, characterization.get("Island"));
            assertEquals(1, characterization.get("Player"));
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
            Map<String, Integer> characterization = CharacterCardFabric.getCharacterization(Characters.THIEF);

            assertEquals(3, characterization.get("Price"));
            assertEquals(0, characterization.get("Memory"));
            assertEquals(1, characterization.get("Usages"));
            assertEquals(0, characterization.get("Bidirectional"));
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
