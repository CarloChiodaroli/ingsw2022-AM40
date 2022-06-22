package it.polimi.ingsw.server.controller.inner;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.controller.outer.PlayMessagesReader;
import it.polimi.ingsw.server.enums.CardCharacterizations;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This Class, with the help of the {@link TurnController} class gives methods to check if communications between controller
 * and model are legal or not.
 */
public class InputController {

    private final PlayMessagesReader reader;

    private Map<Characters, Integer> actualCards;
    private Map<Characters, List<String>> characterizingMap;
    private final static List<String> usefulCharacterizations = List.of("Player", "Island", "EffectAllPlayers");

    public InputController(PlayMessagesReader reader) {
        this.reader = reader;
    }

    public void addPlayer(String player) {
        if (reader.getPlayerNames().contains(player))
            throw new InvalidParameterException("Player name already present");
        if (reader.getPlayerNames().size() >= 3) throw new IllegalStateException("There are already 3 players");
    }

    public void removePlayer(String player) {
        if (!reader.getPlayerNames().contains(player)) throw new InvalidParameterException("Player name not found");
    }

    public void controlActualPlayer(String actualPlayer) throws InvalidParameterException {
        if (!reader.getPlayerNames().contains(actualPlayer))
            throw new InvalidParameterException("Player is not playing the game");
        if (!actualPlayer.equals(reader.getActualPlayer()))
            throw new InvalidParameterException("Player is not the actual player");
    }

    public void controlExpertVariant() throws IllegalStateException {
        if (!reader.isExpertVariant()) throw new IllegalStateException("Game is not in Expert variant");
    }

    public void controlGameState(GameState required) throws IllegalStateException {
        if (!reader.getState().equals(required))
            throw new IllegalStateException("Actual state is " + reader.getState() + " when " + required + " is required");
    }

    public void controlSourceId(String id) {
        if (reader.isExpertVariant()) {
            String charc = reader.getTurnController().getActualCharacter().map(Enum::toString).orElse(null);
            if (charc == null) {
                if (!id.equals("Entrance")) throw new IllegalStateException(id + " is not valid from id");
            } else {
                if (!id.equals("Entrance") && !id.equals(charc)
                        && (reader.getTurnController().getActualCharacter().isPresent() && characterEffectsIsland(reader.getTurnController().getActualCharacter().get()) && id.equals("Place") && !isIslandId(id))
                        && !id.equals("Room")) throw new IllegalStateException(id + " is not valid from id");
            }
        } else {
            if (!id.equals("Entrance")) throw new IllegalStateException(id + " is not valid from id");
        }
    }

    public void controlDestinationId(String id) {
        if (reader.isExpertVariant()) {
            String charc = reader.getTurnController().getActualCharacter().toString();
            if ((!id.equals("Entrance")) && !id.equals(charc)
                    && !isIslandId(id)
                    && !id.equals("Room")) throw new IllegalStateException(id + " is not valid from id");
        } else {
            if (id.equals("Entrance") || id.matches("^c_[0-9_]*"))
                throw new IllegalStateException(id + " is not valid to id");
        }
    }

    public void excludeGameState(GameState exclude) throws IllegalStateException {
        if (reader.getState().equals(exclude))
            throw new IllegalStateException("Actual state is " + reader.getState() + " and it's illegal for this action");
    }

    public void playCharacterCardPermit(String playerName) {
        controlExpertVariant();
        controlGameState(GameState.ACTION);
        controlActualPlayer(playerName);
        if (reader.isCharacterActive()) throw new IllegalStateException("Character card has been already played");
    }

    // Expert

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

    public boolean characterEffectsPlayer(Characters characters) {
        return reader.isCharacterActive() && characterizingMap.getOrDefault(characters, new ArrayList<>()).contains("Player");
    }

    public boolean characterEffectsIsland(Characters characters) {
        return reader.isCharacterActive() && characterizingMap.getOrDefault(characters, new ArrayList<>()).contains("Island");
    }

    public boolean characterEffectsAllPlayers(Characters characters) {
        return reader.isCharacterActive() && characterizingMap.getOrDefault(characters, new ArrayList<>()).contains("EffectAllPlayers");
    }

    public boolean isIslandId(String islandId) {
        return islandId.matches("i_[0-9_]*");
    }

    public boolean isCloudId(String islandId) {
        return islandId.matches("c_[0-9_]*");
    }

    public void controlCloudId(String id) {
        if (!isCloudId(id)) throw new InvalidParameterException("Gotten id is not cloud id");
    }

    public void controlIslandId(String id) {
        if (!isIslandId(id)) throw new InvalidParameterException("Gotten id is not island id");
    }

}
