package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.TowerColor;

import java.util.*;

/**
 * Represents a player of the game which makes moves and controls his game Dashboard
 */
public class Player {

    private final String name;
    private final List<AssistantCard> personalDeck;
    private final Game game;
    private final SchoolDashboard dashboard;


    /**
     * Class Constructor
     * @param game the game in which the player is playing
     * @param name the name of the player, used to find two equal players
     * @param towerColor the color of the towers of the player
     */
    public Player(Game game, String name, TowerColor towerColor){
        this.game = game;
        this.name = name;
        this.personalDeck = new ArrayList<>();
        setPersonalDeck();
        this.dashboard = new SchoolDashboard(game.isThreePlayerGame(), towerColor);
    }

    /**
     * Creates the different assistant cards of player's personal deck
     */
    private void setPersonalDeck(){
        if(!personalDeck.isEmpty()) personalDeck.clear();
        for(int i = 1; i <= 10; i++){
            personalDeck.add(new AssistantCard(i));
        }
    }

    /**
     * Command for playing a card during the game
     * @param which to define which card we need to get from the deck
     * @return an optional of a card with the chosen assistant card if found, otherwise empty.
     */
    public Optional<AssistantCard> playAssistantCard(AssistantCard which){
        Optional<AssistantCard> result = personalDeck.stream()
                .filter(which::equals)
                .findAny();
        if(!game.getPianificationFase().play(result, this)) {
            return Optional.empty();
        }
        result.ifPresent(personalDeck::remove);
        return result;
    }

    /**
     * Method to know if a player can play another card
     * @return true if he can, false if not
     */
    public boolean canChangeAssistantCard(){
        return !personalDeck.isEmpty();
    }

    /**
     * Adds a card to the player's personal card Deck
     * @param card card to be added
     */
    public void giveAssistantCard(AssistantCard card){
        if(!personalDeck.contains(card)) personalDeck.add(card);
        sortPersonalDeck();
    }

    /**
     * Utility private method to sort the personal card Deck
     */
    private void sortPersonalDeck(){
        Collections.sort(personalDeck);
    }

    /**
     * Getter
     * @return a list of Assistant Cards representing the personal card deck
     */
    public List<AssistantCard> getPersonalDeck(){
        return new ArrayList<>(personalDeck);
    }

    /**
     * Command to execute a student movement
     * @param student is the Teacher Color of the student to move
     * @param sourceId is the ID of the place to move the student from
     * @param destinationId is the ID of the place to move the student to
     * @return true if success, false if not
     */
    public boolean moveStudent(TeacherColor student, String sourceId, String destinationId){
        Optional<StudentsManager> from = getStudentsManagerById(sourceId, student);
        Optional<StudentsManager> to = getStudentsManagerById(destinationId, student);
        if(from.isPresent() && to.isPresent()) return game.getActionFase().request(student, from.get(), to.get());
        else return false;
    }

    /**
     * Utility method to find the precise places to move the player to and from
     * @param id is the ID of the desired students manager
     * @param color is the color of the student which is moving
     * @return the wanted student manager
     */
    private Optional<StudentsManager> getStudentsManagerById(String id, TeacherColor color){
        switch(id){
            case "Room" -> {
                return Optional.of((StudentsManager) dashboard.getRoom().getTable(color));
            }
            case "Entrance" -> {
                return Optional.of(dashboard.getEntranceAsStudentsManager());
            }
            default -> {
                return game.getStudentsManagerById(id);
            }
        }
    }

    /**
     * Command to execute mother nature movement
     * @param steps is the number of steps to move mother nature
     */
    public void moveMotherNature(int steps){
        game.getActionFase().request(this, steps);
    }

    /**
     * Command that triggers the influence count
     */
    public void calcInfluence(){
        game.getActionFase().request();
    }

    /**
     * Utility used for influence count
     * @return a list of colors of the possessed teachers
     */
    public List<TeacherColor> getTeachers(){
        List<TeacherColor> teachers = new ArrayList<>();
        for(TeacherColor color: TeacherColor.values()){
            if(dashboard.getRoom().getTable(color).hasTeacher()){
                teachers.add(color);
            }
        }
        return teachers;
    }

    /**
     * Utility used for the influence count, represents the movement of the tower from the player Dashboard
     * @return the color of the asked tower if the player has towers to place
     * @throws Exception sent when there are no more towers, in a normal play this scenario should be not possible
     */
    public TowerColor getTower(int howManyTowers) throws Exception{
        boolean result = dashboard.getTower(howManyTowers);
        if(result){
            return dashboard.getTowerColor();
        } else {
            throw new Exception("Player has no more Towers to give");
        }
    }

    /**
     * Utility used to give a tower back to a player
     * @param howManyTowers is the number of towers to give to the player
     */
    public void pushTower(int howManyTowers){
        dashboard.pushTower(howManyTowers);
    }

    /**
     * Command to choose which cloud the player wants
     * @param cloudId is the id of the chosen cloud
     */
    public void chooseCloud(String cloudId){
        game.getActionFase().request(this, cloudId);
    }

    /**
     * Getter of the name
     * @return the name of the player
     */
    public String getName(){
        return name;
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
}