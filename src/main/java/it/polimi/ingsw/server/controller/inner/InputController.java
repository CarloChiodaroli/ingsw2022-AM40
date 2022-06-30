package it.polimi.ingsw.server.controller.inner;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.controller.outer.PlayMessagesReader;
import it.polimi.ingsw.server.enums.ActionPhaseStateType;
import it.polimi.ingsw.server.enums.CardCharacterizations;
import it.polimi.ingsw.server.enums.CharactersLookup;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This Class, with the help of the {@link TurnController} class gives methods to check if communications between controller
 * and model are legal or not
 */
public class InputController {

    private final PlayMessagesReader reader;

    private Map<Characters, Integer> actualCards;
    private Map<Characters, List<String>> characterizingMap;
    private final static List<String> usefulCharacterizations = List.of("Player", "Island", "EffectAllPlayers");

    /**
     * Constructor
     */
    public InputController(PlayMessagesReader reader) {
        this.reader = reader;
    }

    /**
     * Check name is valid and there are less than 3 players
     *
     * @param player player to add
     */
    public void addPlayer(String player) {
        if (reader.getPlayerNames().contains(player))
            throw new InvalidParameterException("Player name already present");
        if (reader.getPlayerNames().size() >= 3) throw new IllegalStateException("There are already 3 players");
    }

    /**
     * Check name of the player to remove exists
     *
     * @param player player to remove
     */
    public void removePlayer(String player) {
        if (!reader.getPlayerNames().contains(player)) throw new InvalidParameterException("Player name not found");
    }

    /**
     * Check the name exists in game and is the actual player
     *
     * @param actualPlayer name of actual player
     * @throws InvalidParameterException invalid name
     */
    public void controlActualPlayer(String actualPlayer) throws InvalidParameterException {
        if (!reader.getPlayerNames().contains(actualPlayer))
            throw new InvalidParameterException("Player is not playing the game");
        if (!actualPlayer.equals(reader.getActualPlayer()))
            throw new InvalidParameterException("Player is not the actual player");
    }

    /**
     * Check the game is in expert mode
     *
     * @throws IllegalStateException not in expert
     */
    public void controlExpertVariant() throws IllegalStateException {
        if (!reader.isExpertVariant()) throw new IllegalStateException("Game is not in Expert variant");
    }

    /**
     * Check the required state is the current
     *
     * @param required required state
     * @throws IllegalStateException actual and required states aren't equals
     */
    public void controlGameState(GameState required) throws IllegalStateException {
        if (!reader.getState().equals(required))
            throw new IllegalStateException("Actual state is " + reader.getState() + " when " + required + " is required");
    }

    /**
     * Check the source of move students is valid
     *
     * @param id source of the movement
     */
    public void controlSourceId(String id) {
        if (reader.isExpertVariant()) {
            Characters charc = reader.getTurnController().getActualCharacter().orElse(null);
            if (charc == null) {
                if (!id.equals("Entrance")) throw new IllegalStateException(id + " is not valid from id");
            } else {
                Map<String, Integer> characterization = CardCharacterizations.particular(charc);
                if (!id.equals("Entrance")
                        && !id.equals("Card")
                        && characterization.get("Memory") == 0)
                    throw new IllegalStateException(id + " is not valid from id");
            }
        } else {
            if (!id.equals("Entrance")) throw new IllegalStateException(id + " is not valid from id");
        }
    }

    /**
     * Check if the destination of move students is valid
     *
     * @param id destination of the movement
     */
    public void controlDestinationId(String id) {
        if (reader.isExpertVariant()) {
            String charc = reader.getTurnController().getActualCharacter().toString();
            if ((!id.equals("Entrance")) && !id.equals(charc)
                    && !isIslandId(id)
                    && !id.equals("Room")) throw new IllegalStateException(id + " is not valid to id");
        } else {
            if (id.equals("Entrance") || id.matches("^c_[0-9_]*"))
                throw new IllegalStateException(id + " is not valid to id");
        }
    }

    /**
     * Check if the destination of the move with a card place is valid
     *
     * @param id the destination id to check
     */
    public void controlCardDestinationId(String id) {
        if (!reader.isExpertVariant()) throw new IllegalStateException("It's not expert variant");
        Characters charc = reader.getTurnController().getActualCharacter().orElseThrow(() -> new IllegalStateException("No active character card"));
        if (!CharactersLookup.getType(charc).equals(ActionPhaseStateType.STUDENT))
            throw new IllegalStateException("Active card is not a student movement card");
        Map<String, Integer> characterization = CardCharacterizations.particular(charc);
        if (characterization.getOrDefault("Island", 0) > 0 && isIslandId(id)) {
            return;
        }
        if (characterization.getOrDefault("Room", 0) > 0 && id.equals("Room")) {
            return;
        }
        throw new IllegalArgumentException("Received wrong to id");
    }

    /**
     * Check exclude state
     *
     * @param exclude state to exclude
     * @throws IllegalStateException illegal state
     */
    public void excludeGameState(GameState exclude) throws IllegalStateException {
        if (reader.getState().equals(exclude))
            throw new IllegalStateException("Actual state is " + reader.getState() + " and it's illegal for this action");
    }

    /**
     * Check the character card can be played
     *
     * @param playerName player who wants to play the card
     */
    public void playCharacterCardPermit(String playerName) {
        controlExpertVariant();
        controlGameState(GameState.ACTION);
        controlActualPlayer(playerName);
        if (reader.isCharacterActive()) throw new IllegalStateException("Character card has been already played");
    }

    // Expert

    /**
     * Set the actual character cards
     *
     * @param prices a map with for each character the price
     */
    public void setCharacters(Map<Characters, Integer> prices) {
        actualCards = prices;
        characterizingMap = new HashMap<>();
        actualCards.keySet()
                .forEach(card -> {
                    characterizingMap.put(card, new ArrayList<>());
                    CardCharacterizations.particular(card).keySet().stream()
                            .filter(usefulCharacterizations::contains)
                            .forEach(string -> characterizingMap.get(card).add(string));
                    if (characterizingMap.get(card).isEmpty()) characterizingMap.remove(card);
                });
    }

    /**
     * Check the character is active and contains the characterization Player
     *
     * @param characters character required
     * @return true if all right
     */
    public boolean characterEffectsPlayer(Characters characters) {
        return reader.isCharacterActive() && characterizingMap.getOrDefault(characters, new ArrayList<>()).contains("Player");
    }

    /**
     * Check the character is active and contains the characterization Island
     *
     * @param characters character required
     * @return true if all right
     */
    public boolean characterEffectsIsland(Characters characters) {
        return reader.isCharacterActive() && characterizingMap.getOrDefault(characters, new ArrayList<>()).contains("Island");
    }

    /**
     * Check the character is active and contains the characterization All Players
     *
     * @param characters character required
     * @return true if all right
     */
    public boolean characterEffectsAllPlayers(Characters characters) {
        return reader.isCharacterActive() && characterizingMap.getOrDefault(characters, new ArrayList<>()).contains("EffectAllPlayers");
    }

    public boolean characterTeacherBehaviour(Characters characters) {
        return reader.isCharacterActive() && CardCharacterizations.particular(characters).getOrDefault("TeacherBehaviour", 0) > 0;
    }

    /**
     * Check syntax of id island
     *
     * @param islandId id
     * @return true if is valid
     */
    public boolean isIslandId(String islandId) {
        return islandId.matches("i_[0-9_]*");
    }

    /**
     * Check syntax of id cloud
     *
     * @param cloudId id
     * @return true if is valid
     */
    public boolean isCloudId(String cloudId) {
        return cloudId.matches("c_[0-9_]*");
    }

    /**
     * If id is not valid send an error message
     *
     * @param id cloud id
     */
    public void controlCloudId(String id) {
        if (!isCloudId(id)) throw new InvalidParameterException("Gotten id is not cloud id");
    }

    /**
     * If id is not valid send an error message
     *
     * @param id island id
     */
    public void controlIslandId(String id) {
        if (!isIslandId(id)) throw new InvalidParameterException("Gotten id is not island id");
    }

}
