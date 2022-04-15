package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.MotherNature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import it.polimi.ingsw.model.Game;

public class ActionFase {

    // Variables
    private final Game game;
    private final List<ActionFaseState> states;

    // Non expert Status variables
    private int possibleStudentMovements;
    private boolean movedMotherNature;
    private boolean calculatedInfluence;
    private boolean mergedIslands;
    private boolean chosenCloud;

    // Expert variant attributes
    private final boolean expertVariant;
    private CharacterCard actualCard;
    private final List<CharacterCard> characterCards;

    // building Action phase
    public ActionFase(Game game){
        this.game = game;
        this.expertVariant = game.isExpertVariant();

        this.states = new ArrayList<>();
        this.states.add(new StudentMovement(this));
        this.states.add(new MotherNatureState(this));
        this.states.add(new Influence(this));
        this.states.add(new MergeIsland(this));
        this.states.add(new Finalize(this));
        if(expertVariant){
            this.characterCards = CharacterCardFabric.getCards(this);
        } else {
            this.characterCards = null;
        }
    }

    public void resetStatus(){
        // Non expert status variables
        possibleStudentMovements = 3;
        movedMotherNature = false;
        calculatedInfluence = false;
        mergedIslands = false;
        chosenCloud = false;
    }

    // Commands
    // called for moving mother nature and for choosing a cloud
    public void request(Player player, String id){
        if(!expertVariant){
            if(id.equals("MotherNature")){
                if(calculatedInfluence) return;
                if(MotherNature.getMotherNature().getPosition().isEmpty()) return;
                states.get(2).handle(player, MotherNature.getMotherNature().getPosition().get());
                movedMotherNature = true;
                request();
            } else {
                if(chosenCloud) return;
                game.getTable().getCloudById(id).ifPresent(cloud -> states.get(4).handle(player, cloud));
                chosenCloud = true;
            }
        }
    }

    // Called to move students from a "from" place to a "to" place
    public void request(TeacherColor teacherColor, Optional<StudentsManager> from, Optional<StudentsManager> to){
        if(possibleStudentMovements <= 0 || chosenCloud) return;
        if(expertVariant && actualCard.isInUse()){

        } else {
            states.get(0).handle(teacherColor, from, to);
            possibleStudentMovements--;
        }
    }

    // Called to move mother nature
    public void request(Player player, int motherNatureHops){
        if(movedMotherNature) return;
        int maxHops = game.getPianificationFase().getMotherNatureHops(player);
        if(motherNatureHops > maxHops || motherNatureHops <= 0) return;
        if(!expertVariant){
            states.get(1).handle(player, motherNatureHops, maxHops);
            movedMotherNature = true;
        }
    }

    // Called to merge islands
    public void request(){
        if(mergedIslands) return;
        states.get(3).handle();
        if(game.getTable().getIslandList().size()==3)
            {
                this.getGame().setEndgame(true);
                this.getGame().setendplayer(game.SearchPlayerWithMostTower());
            }
    }

    public void request(Player player, TeacherColor studentA, TeacherColor studentB){
        if(expertVariant && actualCard.isInUse()){
            actualCard.handle(player, studentA, studentB);
        }
    }

    // Characters card management
    private Optional<CharacterCard> getCard(Characters characters){
        if(!expertVariant) return Optional.empty();
        return characterCards.stream()
                .filter(card -> card.getCharacter().equals(characters))
                .findAny();
    }

    public boolean canBeActivated(Characters characters){
        return getCard(characters).isPresent();
    }

    public int getCardCost(Characters characters){
        AtomicInteger result = new AtomicInteger(0);
        getCard(characters).ifPresent(card -> result.set(card.getPrice()));
        return result.get();
    }

    public void activateCard(Characters characters, Player player){

    }

    protected Game getGame(){
        return game;
    }

}
