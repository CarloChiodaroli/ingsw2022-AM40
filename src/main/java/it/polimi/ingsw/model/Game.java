package it.polimi.ingsw.model;

import it.polimi.ingsw.model.phase.action.ActionFase;
import it.polimi.ingsw.model.phase.pianification.PianificationFase;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Table;

import java.util.*;

/**
 * Class which represents the whole game model managing its components
 */
public class Game {

    private final List<Player> players;
    private Player actualPlayer = null;
    private Table table;
    private final List<TowerColor> order;
    private final Map<TowerColor, String> preGamePlayersList;
    private boolean isInitial = true;
    private boolean isThreePlayerGame = false;
    private boolean isExpertVariant = false;
    private PianificationFase pianificationFase;
    private ActionFase actionFase;
    private int numOfRegisteredPlayers = 0;

    /**
     * Class Constructor, initializes core components for the game creation
     */
    public Game(){
        this.players = new ArrayList<>();
        this.order = new ArrayList<>();
        initializeOrder();
        this.preGamePlayersList = new EnumMap<>(TowerColor.class);
    }

    /**
     * Constructs the order of Tower color allocation
     */
    private void initializeOrder(){
        order.add(TowerColor.BLACK);
        order.add(TowerColor.WHITE);
        order.add(TowerColor.GREY);
    }

    /**
     * Method which effectively starts the game creating all necessary objects
     */
    public void gameStarter(){
        if(numOfRegisteredPlayers < 2) return;
        table = new Table();
        isThreePlayerGame = preGamePlayersList.get(TowerColor.GREY) == null;
        if(isThreePlayerGame){
            players.add(new Player(this, preGamePlayersList.get(TowerColor.BLACK), TowerColor.BLACK));
            players.add(new Player(this, preGamePlayersList.get(TowerColor.WHITE), TowerColor.WHITE));
        } else {
            players.add(new Player(this, preGamePlayersList.get(TowerColor.BLACK), TowerColor.BLACK));
            players.add(new Player(this, preGamePlayersList.get(TowerColor.WHITE), TowerColor.WHITE));
            players.add(new Player(this, preGamePlayersList.get(TowerColor.GREY), TowerColor.GREY));
        }
        actionFase = new ActionFase(isExpertVariant);
        pianificationFase = new PianificationFase(this);
        isInitial = false;
    }

    /**
     * Used before starting the game, to add users to the list of players to be created
     * @param name is the name of the player
     * @return true if the addition was successful, false otherwise
     */
    public boolean addPlayer(String name){
        if(numOfRegisteredPlayers >= 3) return false;
        preGamePlayersList.put(order.get(numOfRegisteredPlayers), name);
        numOfRegisteredPlayers++;
        return true;
    }

    /**
     * Getter for the list of players
     * @return the list of players
     */
    public List<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    /**
     * Getter for the game table
     * @return the game table
     */
    public Table getTable(){
        return table;
    }

    /**
     * Getter for the pianification fase
     * @return the pianification fase
     */
    public PianificationFase getPianificationFase(){
        return pianificationFase;
    }

    /**
     * Getter for the action fase
     * @return the action fase
     */
    public ActionFase getActionFase() {
        return actionFase;
    }

    /**
     * Getter of a students manager class for moving students by id
     * @param id the Id of the students manager
     * @return the students manager itself
     */
    public Optional<StudentsManager> getStudentsManagerById(String id){
        if(id.equals("Bag")) return table.getBag();
        if(id.contains("C")) return table.getCloudById(id);
        if(id.contains("I")) return table.getIslandById(id);
        return null; //needs to be better reimplemented
    }

    /**
     * When in initial state switches the game between normal variant (default) and the expert one
     * When not in initial state doesn't do anything
     */
    public void switchExpertVariant(){
        if(isGameStarted()) return;
        isExpertVariant = !isExpertVariant;
    }

    /**
     * Shows if the game is in initial state or is already started
     * @return true if the game is already started
     */
    public boolean isGameStarted(){
        return !isInitial;
    }

    /**
     * Shows if the game is in Expert variant or not
     * @return true if is in expert variant
     */
    public boolean isExpertVariant(){
        return isExpertVariant;
    }

    public boolean isThreePlayerGame() {
        return isThreePlayerGame;
    }
}
