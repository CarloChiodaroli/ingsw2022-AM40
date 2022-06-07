package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.model.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.player.school.SchoolDashboard;
import it.polimi.ingsw.server.model.table.Island;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Represents a player of the game which makes moves and controls his game Dashboard
 */
public class Player {

    private final String name;
    private final List<AssistantCard> personalDeck;
    private final Game game;
    private final SchoolDashboard dashboard;
    private int money = 0;
    private boolean enable;


    /**
     * Class Constructor
     *
     * @param game       the game in which the player is playing
     * @param name       the name of the player, used to find two equal players
     * @param towerColor the color of the towers of the player
     */
    public Player(Game game, String name, TowerColor towerColor) {
        this.game = game;
        this.name = name;
        this.personalDeck = new ArrayList<>();
        setPersonalDeck();
        this.dashboard = new SchoolDashboard(game.isThreePlayerGame(), towerColor);
        if (game.getTable().getCoin()) this.money++;
        enable = false;
        //if(game.getTable().getCoin()) this.money++;
    }

    /**
     * Creates the different assistant cards of player's personal deck
     */
    private void setPersonalDeck() {
        if (!personalDeck.isEmpty()) personalDeck.clear();
        for (int i = 1; i <= 10; i++) {
            personalDeck.add(new AssistantCard(i));
        }
    }

    /**
     * Command for playing a card during the game
     *
     * @param which to define which card we need to get from the deck
     */
    public void playAssistantCard(AssistantCard which) {
        AssistantCard actual = personalDeck.stream()
                .filter(which::equals)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Chosen card is no more available in personal deck"));
        game.getPianificationFase().play(actual, this);
        personalDeck.remove(actual);
        game.updateState();
    }

    /**
     * Method to know if a player can play another card
     *
     * @return true if he can, false if not
     */
    public boolean canChangeAssistantCard() {
        return !personalDeck.isEmpty();
    }

    /**
     * Adds a card to the player's personal card Deck
     *
     * @param card card to be added
     */
    public void giveAssistantCard(AssistantCard card) {
        if (!personalDeck.contains(card)) personalDeck.add(card);
        sortPersonalDeck();
    }

    /**
     * Utility private method to sort the personal card Deck
     */
    private void sortPersonalDeck() {
        Collections.sort(personalDeck);
    }

    /**
     * Getter
     *
     * @return a list of Assistant Cards representing the personal card deck
     */
    public List<AssistantCard> getPersonalDeck() {
        return new ArrayList<>(personalDeck);
    }

    /**
     * Command to execute a student movement
     *
     * @param student       is the Teacher Color of the student to move
     * @param sourceId      is the ID of the place to move the student from
     * @param destinationId is the ID of the place to move the student to
     */
    public void moveStudent(TeacherColor student, String sourceId, String destinationId) {
        controlEnable();
        Optional<StudentsManager> from = getStudentsManagerById(sourceId);
        Optional<StudentsManager> to = getStudentsManagerById(destinationId);
        game.getActionPhase().request(student, from, to);
        if(dashboard.getRoom().getMoneyPlace()){
            if(game.getTable().getCoin()){
                money++;
            }
        }
    }

    public void moveStudent(TeacherColor studentA, TeacherColor studentB) {
        controlEnable();
        game.getActionPhase().request(this, studentA, studentB);
        if(dashboard.getRoom().getMoneyPlace()){
            if(game.getTable().getCoin()){
                money++;
            }
        }
    }

    /**
     * Utility method to find the precise places to move the player to and from
     *
     * @param id    is the ID of the desired students manager
     * @return the wanted student manager
     */
    public Optional<StudentsManager> getStudentsManagerById(String id) {
        switch (id) {
            case "Room" -> {
                return Optional.of(dashboard.getRoom());
            }
            case "Entrance" -> {
                return Optional.of(getEntrance());
            }
            default -> {
                return game.getStudentsManagerById(id);
            }
        }
    }

    public StudentsManager getEntrance() {
        return dashboard.getEntranceAsStudentsManager();
    }

    /**
     * Command to execute mother nature movement
     *
     * @param steps is the number of steps to move mother nature
     */
    public void moveMotherNature(int steps) {
        controlEnable();
        game.getActionPhase().request(this, steps);
    }

    /**
     * Command that triggers the influence count
     */
    public void calcInfluence() {
        controlEnable();
        game.getActionPhase().request(this, "MotherNature");
    }

    /**
     * Utility used for influence count
     *
     * @return a list of colors of the possessed teachers
     */
    public List<TeacherColor> getTeachers() {
        List<TeacherColor> teachers = new ArrayList<>();
        for (TeacherColor color : TeacherColor.values()) {
            if (dashboard.getRoom().getTeacherPresence(color)) {
                teachers.add(color);
            }
        }
        return teachers;
    }

    /**
     * Utility used for the influence count, represents the movement of the tower from the player Dashboard
     *
     * @return the color of the asked tower if the player has towers to place
     * @throws InvalidParameterException sent when there are no more towers, in a normal play this scenario should be not possible
     */
    public TowerColor getTower(int howManyTowers) throws InvalidParameterException {
        boolean result = dashboard.getTower(howManyTowers);
        if (result) {
            return dashboard.getTowerColor();
        } else {
            throw new InvalidParameterException("Player has no more Towers to give");
        }
    }

    public TowerColor getTowerColor() {
        return dashboard.getTowerColor();
    }

    /*@Deprecated
    public StudentsManager getRoomTable(TeacherColor color) {
        return dashboard.getRoom().getTable(color);
    }*/

    public StudentsManager getRoomTable() {
        return dashboard.getRoom();
    }

    /*
    public void addTeacher(TeacherColor color){
        dashboard.getRoom().getTable(color).setTeacherPresence(true);
    }

    public void removeTeacher(TeacherColor color){
        dashboard.getRoom().getTable(color).setTeacherPresence(false);
    }

    public boolean hasTeacher(TeacherColor color){
        return dashboard.getRoom().getTable(color).hasTeacher();
    }
     */

    public void addTeacher(TeacherColor color) {
        dashboard.getRoom().addTeacher(color);
    }

    public void removeTeacher(TeacherColor color) {
        dashboard.getRoom().removeTeacher(color);
    }

    public boolean hasTeacher(TeacherColor color) {
        return dashboard.getRoom().getTeacherPresence(color);
    }

    public int howManyStudentsInRoom(TeacherColor color){
        return dashboard.getRoom().howManyStudents(color);
    }

    /**
     * Utility used to give a tower back to a player
     *
     * @param howManyTowers is the number of towers to give to the player
     */
    public void pushTower(int howManyTowers) {
        dashboard.pushTower(howManyTowers);
    }

    /**
     * Command to choose which cloud the player wants
     *
     * @param cloudId is the id of the chosen cloud
     */
    public void chooseCloud(String cloudId) {
        game.getActionPhase().request(this, cloudId);
    }

    /**
     * Getter of the name
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    public void playCharacterCard(Characters characters) {
        controlEnable();
        game.getActionPhase().activateCard(characters, this);
    }

    public void playCharacterCard(Characters characters, TeacherColor color) {
        controlEnable();
        game.getActionPhase().activateCard(characters, this, color);
    }

    public void playCharacterCard(Characters characters, Island island) {
        controlEnable();
        game.getActionPhase().activateCard(characters, this, island);
    }

    public boolean pay(int howMuch) {
        if (money >= howMuch) {
            money -= howMuch;
            return true;
        } else {
            return false;
        }
    }

    public int getMoney() {
        return money;
    }

    public void giveMoney(int howMuch) {
        money += howMuch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) && Objects.equals(game, player.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, game);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", personalDeck=" + personalDeck +
                ", game=" + game +
                ", dashboard=" + dashboard +
                '}';
    }

    public int getNumberTowersLeft() {
        return dashboard.getNumOfTowers();
    }

    public void enable() {
        enable = true;
    }

    public void disable() {
        enable = false;
    }

    public boolean isEnabled() {
        return enable;
    }

    private void controlEnable() throws IllegalStateException {
        if (!isEnabled()) throw new IllegalStateException("Player can't make any moves now");
    }
}
