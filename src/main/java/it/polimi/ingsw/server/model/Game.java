package it.polimi.ingsw.server.model;

import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.phase.PlanningPhase;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Bag;
import it.polimi.ingsw.server.model.table.Cloud;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.Table;

import java.util.*;

/**
 * Class which represents the whole game model managing its components
 */
public class Game {
    private final List<Player> players;
    private Table table;
    private final List<TowerColor> order;
    private final Map<TowerColor, String> preGamePlayersList;
    private boolean isInitial = true;
    private boolean isThreePlayerGame = false;
    private boolean isExpertVariant = false;
    private PlanningPhase planningPhase;
    private ActionPhase actionPhase;
    private int numOfRegisteredPlayers = 0;
    private boolean endgame = false;
    private String endPlayer = null;
    private static final String draw = "No one";


    /**
     * Class Constructor, initializes core components for the game creation
     */
    public Game() {
        this.players = new ArrayList<>();
        this.order = new ArrayList<>();
        initializeOrder();
        this.preGamePlayersList = new EnumMap<>(TowerColor.class);
    }

    /**
     * Constructs the order of Tower color allocation
     */
    private void initializeOrder() {
        order.add(TowerColor.BLACK);
        order.add(TowerColor.WHITE);
        order.add(TowerColor.GREY);
    }

    /**
     * Method which effectively starts the game creating all necessary objects
     */
    public void gameStarter() throws IllegalStateException {
        if (numOfRegisteredPlayers < 2)
            throw new IllegalStateException("Not enough players");
        table = new Table(numOfRegisteredPlayers);
        if (isThreePlayerGame) {
            players.add(new Player(this, preGamePlayersList.get(TowerColor.BLACK), TowerColor.BLACK));
            players.add(new Player(this, preGamePlayersList.get(TowerColor.WHITE), TowerColor.WHITE));
            players.add(new Player(this, preGamePlayersList.get(TowerColor.GREY), TowerColor.GREY));
        } else {
            players.add(new Player(this, preGamePlayersList.get(TowerColor.BLACK), TowerColor.BLACK));
            players.add(new Player(this, preGamePlayersList.get(TowerColor.WHITE), TowerColor.WHITE));
        }
        players.forEach(this::initializePlayer);
        actionPhase = new ActionPhase(this);
        planningPhase = new PlanningPhase(this);
        isInitial = false;
        startRound();
    }

    private void startRound() {
        planningPhase.activate();
    }

    public void updateState() {
        if (planningPhase.isInOrder()) {
            actionPhase.startPhase(planningPhase.getActualPlayer());
        }
    }

    /**
     * Initializes player's entrance with adequate number of students
     * @param player the one to initialize
     */
    private void initializePlayer(Player player) {
        Bag bag = (Bag) table.getBag().orElseThrow();
        for (int i = 0; i < player.getEntrance().getMaxStudents(); i++) {
            player.getEntrance().addStudent(bag.getAStudent());
        }
    }

    /**
     * Used before starting the game, to add users to the list of players to be created
     *
     * @param name is the name of the player
     * @return true if the addition was successful, false otherwise
     */
    public boolean addPlayer(String name) {
        if (numOfRegisteredPlayers >= 3) return false;
        preGamePlayersList.put(order.get(numOfRegisteredPlayers), name);
        numOfRegisteredPlayers++;
        isThreePlayerGame = numOfRegisteredPlayers == 3;
        return true;
    }

    /**
     * Getter for the list of players
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Getter for the game table
     *
     * @return the game table
     */
    public Table getTable() {
        return table;
    }

    /**
     * Getter for the pianification fase
     *
     * @return the pianification fase
     */
    public PlanningPhase getPianificationFase() {
        return planningPhase;
    }

    /**
     * Getter for the action phase
     *
     * @return the action phase
     */
    public ActionPhase getActionPhase() {
        return actionPhase;
    }

    /**
     * Getter of a students manager class for moving students by id
     *
     * @param id the Id of the students manager
     * @return the students manager itself
     */
    public Optional<StudentsManager> getStudentsManagerById(String id) {
        if (id.equals("Bag")) return table.getBag();
        if (id.contains("c_")) {
            Cloud tmp = table.getCloudById(id).orElse(null);
            if (tmp == null) return Optional.empty();
            return Optional.of(tmp);
        }
        if (id.contains("i_")) {
            Island tmp = table.getIslandById(id).orElse(null);
            if (tmp == null) return Optional.empty();
            return Optional.of(tmp);
        }
        return Optional.empty();
    }

    /**
     * When in initial state switches the game between normal variant (default) and the expert one
     * When not in initial state doesn't do anything
     */
    public void switchExpertVariant() {
        if (isGameStarted()) return;
        isExpertVariant = !isExpertVariant;
    }

    /**
     * Shows if the game is in initial state or is already started
     *
     * @return true if the game is already started
     */
    public boolean isGameStarted() {
        return !isInitial;
    }

    /**
     * Shows if the game is in Expert variant or not
     *
     * @return true if is in expert variant
     */
    public boolean isExpertVariant() {
        return isExpertVariant;
    }

    /**
     * Shows if the game is in three players mode
     *
     * @return true if it is, false if not
     */
    public boolean isThreePlayerGame() {
        return isThreePlayerGame;
    }

    /**
     * Command to build new clouds
     */
    public void buildClouds() {
        try{
            table.buildClouds();
        } catch (IllegalStateException e) {
            this.endGame();
        }
    }

    /**
     * Command to remove a cloud
     *
     * @param cloud the cloud to remove
     */
    public void removeCloud(StudentsManager cloud) {
        getTable().removeCloud((Cloud) cloud);
    }

    /**
     * Getter of how many students are in this game
     *
     * @return the number of players
     */
    public int getNumOfRegisteredPlayers() {
        return numOfRegisteredPlayers;
    }

    /**
     * Getter of endgame state
     *
     * @return true if the play is ended, false if not
     */
    public boolean isGameEnded() {
        return endgame;
    }

    /**
     * Setter of the endgame
     */
    public void endGame() {
        endgame = true;
        this.endPlayer = searchPlayerWithMostTower();
    }

    /**
     * Getter of the winning player.
     *
     * @return the winning player, null if even.
     */
    public String getEndPlayer() {
        return endPlayer;
    }

    /**
     * Finds the player who won the play
     *
     * @return the winner
     */
    public String searchPlayerWithMostTower() {
        List<Player> playerWinner = new ArrayList<>();
        int minimum = 1000;
        for (Player p : players)
            if (minimum >= p.getNumberTowersLeft())
                minimum = p.getNumberTowersLeft();
        for (Player p : players)
            if (minimum == p.getNumberTowersLeft())
                playerWinner.add(p);
        if (playerWinner.size() == 1)
            return playerWinner.get(0).getName();
        else
            return searchPlayerWithMostTeacher(playerWinner);
    }

    /**
     * Finds the player with most Teachers.
     * Used, as for game manual, to find the winner when towers are even.
     *
     * @param playerList the list of candidate winners
     * @return the winning player
     */
    private String searchPlayerWithMostTeacher(List<Player> playerList) {
        int max = 0;
        Player maxPlayer = null;
        for (Player p : playerList)
            if (max <= p.getTeachers().size()) {
                max = p.getTeachers().size();
                maxPlayer = p;
            }
        if(maxPlayer == null){
            return draw;
        } else {
            return maxPlayer.getName();
        }
    }

    /**
     * Needed to progress the turns and change the current active player
     */
    public void nextPlayer() {
        actionPhase.reset();
        planningPhase.getActualPlayer().disable();
        planningPhase.nextPlayer();
        if (planningPhase.isInOrder()) {
            actionPhase.startPhase(planningPhase.getActualPlayer());
        } else {
            planningPhase.activate();
        }
    }

    /**
     * Getter of if it is the start of the game
     *
     * @return true if it i, false if not
     */
    public boolean isStartGame() {
        return this.isInitial;
    }

}