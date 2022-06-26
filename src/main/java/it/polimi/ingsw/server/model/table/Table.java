package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;

import java.util.*;

/**
 * Models game's table, so everything that is set on the table while playing the game:
 * {@link Bag bags}, {@link Cloud clouds} and {@link Island islands}
 * (excluding player's {@link it.polimi.ingsw.server.model.player.school.SchoolDashboard dashboard}).
 */
public class Table {

    private final List<Island> islandList;
    private final List<Cloud> cloudList;
    public static Bag bag;
    private int coinsLeft;
    private final static int numOfIslandsToCreate = 12;
    private final static int maxAvailableCoins = 20;
    private final int numOfPlayers;


    /**
     * Constructor
     */
    public Table(int numberOfPlayer) {
        int randomFirstIsland = (int) (Math.random() * numOfIslandsToCreate);
        bag = new Bag();
        coinsLeft = maxAvailableCoins;
        islandList = new ArrayList<>();
        cloudList = new ArrayList<>();
        this.numOfPlayers = numberOfPlayer;
        buildIslands();
        FillCInitialIslandWithStudent(randomFirstIsland);
        MotherNature.getMotherNature().setPosition(islandList.get(randomFirstIsland));
    }

    /**
     * Build the clouds
     *
     * @param numberOfPlayer number of player to create
     */
    public void buildClouds(int numberOfPlayer) throws IllegalStateException {
        cloudList.clear();
        for (int i = 0; i < numberOfPlayer; i++) {
            Cloud cloud = new Cloud("c_" + (i + 1), numberOfPlayer + 1);
            cloud.buildCloud(bag);
            cloudList.add(cloud);
        }
    }

    /**
     * Build the clouds
     */
    public void buildClouds() {
        buildClouds(numOfPlayers);
    }

    /**
     * Remove a cloud
     *
     * @param cloud the cloud to remove
     */
    public void removeCloud(Cloud cloud) {
        cloudList.remove(cloud);
    }

    /**
     * Fill the clouds at the start of the game with one student, except the island where there's mother nature
     * and the opposite
     *
     * @param randomFirstIsland
     */
    public void FillCInitialIslandWithStudent(int randomFirstIsland) {
        int emptyIsland1;
        int emptyIsland2;

        Map<TeacherColor, Integer> assignsLeft = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            assignsLeft.put(color, 2);
        }

        if (randomFirstIsland >= 6) {
            emptyIsland1 = randomFirstIsland - 6;
            emptyIsland2 = randomFirstIsland;
        } else {
            emptyIsland1 = randomFirstIsland;
            emptyIsland2 = randomFirstIsland + 6;
        }

        for (int i = 0; i < 12; i++) {
            if (i != emptyIsland1 && i != emptyIsland2) {
                int indexColor;
                TeacherColor actualColor;
                do {
                    indexColor = (int) (Math.random() * 5);
                    actualColor = TeacherColor.values()[indexColor];
                } while (assignsLeft.get(actualColor).equals(0));
                assignsLeft.computeIfPresent(actualColor, (k, val) -> val - 1);
                if (bag.removeStudent(actualColor)) islandList.get(i).addStudent(actualColor);
            }
        }
    }

    /**
     * Build islands
     */
    private void buildIslands() {
        for (int i = 0; i < numOfIslandsToCreate; i++) {
            islandList.add(new Island("i_" + (i + 1)));
        }
    }

    /**
     * Check there are coins left
     *
     * @return boolean It allows to getCoin requested by the Game
     */
    public boolean getCoin() {
        return coinsLeft > 0;
    }

    /**
     * Get the number of coins left
     *
     * @return number of coins left
     */
    public int getNumCoin() {
        return coinsLeft;
    }

    /**
     * If any, remove one coin
     */
    public void giveCoin() {
        if (getCoin())
            coinsLeft--;
    }

    /**
     * Get the status of the bag
     *
     * @return Bag status
     */
    public Optional<StudentsManager> getBag() {
        return Optional.of(bag);
    }

    /**
     * Merge two islands in one removing the two islands and creating a new one
     *
     * @param island1 First island to merge
     * @param island2 Second island to merge
     */
    public void mergeIsland(Island island1, Island island2) {
        Island IslandToAdd = new Island(island1, island2);
        int FirstPositionOfIslandToAdd = islandList.indexOf(island1);
        islandList.add(FirstPositionOfIslandToAdd, IslandToAdd);
        islandList.remove(island1);
        islandList.remove(island2);
        MotherNature.getMotherNature().getPosition().ifPresent(
                position -> MotherNature.getMotherNature().setPosition(
                        position.equals(island1) || position.equals(island2) ? IslandToAdd : position));
    }

    /**
     * Get islands list
     *
     * @return List of island
     */
    public List<Island> getIslandList() {
        return islandList;
    }

    /**
     * Get clouds list
     *
     * @return List of Cloud
     */
    public List<Cloud> getCloudList() {
        return cloudList;
    }

    /**
     * If the id exists, return the required cloud
     *
     * @param id Cloud's Id
     * @return Cloud you are looking for
     */
    public Optional<Cloud> getCloudById(String id) {
        return cloudList.stream()
                .filter(cloud -> cloud.getId().equals(id))
                .findAny();
    }

    /**
     * If the id exists, return the required island
     *
     * @param id Island's id
     * @return Island you are looking for
     */
    public Optional<Island> getIslandById(String id) {
        return islandList.stream()
                .filter(island -> island.getId().equals(id))
                .findAny();
    }

    /**
     * Get a student from the bag
     *
     * @return the color of the students removed from bag
     */
    public Optional<TeacherColor> getStudentFromBag() {
        return Optional.ofNullable(bag.getAStudent());
    }

    /**
     * Get if the bag is empty
     *
     * @return true if the bag is empty
     */
    public boolean isBagEmpty() {
        return bag.isEmpty();
    }
}
