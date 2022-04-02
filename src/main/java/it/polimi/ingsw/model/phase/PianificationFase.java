package it.polimi.ingsw.model.phase;

import it.polimi.ingsw.model.player.AssistantCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.Game;

import java.util.*;

/**
 * Models game round's pianification phase
 */

public class PianificationFase {

    private AbstractMap<Player, AssistantCard> playiedCards;
    private List<Player> playersInOrder;
    private boolean activated;
    private boolean determinedOrder;
    private final int players;
    private int actualPlayer;
    private final Game game;

    /**
     * Class Constructor
     * @param game the game that uses this pianification phase
     */
    public PianificationFase(Game game){
        this.game = game;
        this.activated = false;
        if(game.isThreePlayerGame()){
            this.players = 3;
        } else {
            this.players = 2;
        }
    }

    // Start of Pianification phase
    /**
     * Starts the phase, reinitializing it in a clean state
     */
    public void activate(){
        activated = true;
        playersInOrder = new ArrayList<>();
        playiedCards = new IdentityHashMap<>();
        actualPlayer = 0;
    }

    /**
     * Shows if the phase has been activated from last reset or construction
     * @return true if is activated, else false
     */
    public boolean isActivated() {
        return activated;
    }

    // Pianification phase part 1

    /**
     * Method called from the player where is played the assistant card
     * @param card the assistant card the player wants to play
     * @param applicantPlayer the players who does the move
     * @return true if the move was successful, else false
     */
    public Optional<AssistantCard> play(Optional<AssistantCard> card, Player applicantPlayer){
        if(!activated) return Optional.empty();
        if(determinedOrder) return Optional.empty();
        if(!isPlayerPresent(applicantPlayer)) return Optional.empty();
        if(!acceptedPlayerCard(card.get(), applicantPlayer)) return Optional.empty();
        for(Player player: playersInOrder){
            if(playiedCards.get(player).compareTo(card.get()) > 0){
                playersInOrder.add(playersInOrder.indexOf(player), applicantPlayer);
            }
        }
        if(!playersInOrder.contains(applicantPlayer)){
            playersInOrder.add(applicantPlayer);
        }
        endPhase();
        playiedCards.put(applicantPlayer, card.get());
        return card;
    }

    /**
     * Determines if the card the player wants to play can be played
     * @param card the card to examine
     * @param player the player who wants to play the card
     * @return true if it can be played, else false
     */
    private boolean acceptedPlayerCard(AssistantCard card, Player player){
        Set<Player> submittingPlayers = playiedCards.keySet();
        for(Player subPlayer: submittingPlayers){
            if(playiedCards.get(subPlayer).equals(card)){
                if(player.canChangeAssistantCard())
                    return false;
                else return true; //I don't like it but it's more legible
                //return !player.canChangeAssistantCard(); is cleaner but less legible
            }
        }
        return true;
    }

    /**
     * Controls if a player has already played a card
     * @param player the player to examine
     * @return true if it has already, else false
     */
    private boolean isPlayerPresent(Player player){
        return playersInOrder.contains(player);
    }

    // Pianification phase part 2
    /**
     * Determines if the pianification phase is ended and the order of players completely determined
     * Asks the game to create new clouds with students
     */
    private void endPhase(){
        if(playersInOrder.size() == players){
            determinedOrder = true;
            game.buildClouds();
        }
    }

    /**
     * Shows if the order of the players has been completely determined
     * @return true if it is, else false
     */
    public boolean isInOrder(){
        return determinedOrder;
    }

    // Action Phase

    /**
     * After having determined the round order of the players determines which is the actual player
     * @return the actual player
     */
    public Player getActualPlayer(){
        if(!determinedOrder) return null;
        return playersInOrder.get(actualPlayer);
    }

    public int getMotherNatureHops(Player player){
        return playiedCards.get(player).getNumOfMotherNatureMovements();
    }

    // End of Round

    /**
     * Resets the preparation phase
     */
    public void reset(){
        activated = false;
        determinedOrder = false;
    }
}
