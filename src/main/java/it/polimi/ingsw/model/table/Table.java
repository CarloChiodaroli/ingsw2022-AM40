package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.StudentsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CLASS TABLE (Idea -> It is a physic table)
 * It controls all the contents
 */
public class Table {

    private List<Island> islandList;
    private List<Cloud> CloudList;
    private Bag bag;
    private int coinsleft;


    /**
     * Constructor
     */
    public Table() {
        bag = new Bag(130, 26);
        coinsleft = 20;
        islandList = new ArrayList<>();
        CloudList = new ArrayList<>();
        buildsIsland(12);
    }

    /**
     * @param howManyClouds number of clouds to create
     */
    public void buildsCloud(int howManyClouds) //to be changed according to new agreements, go and change Game as well
    {
        String idCloud;
        for (int i = 0; i < howManyClouds; i++) {
            CloudList.add(new Cloud("C_:" + i + 1, howManyClouds + 1));
        }
    }

    /**
     * @param howManyIslands number of islands to create
     */
    private void buildsIsland(int howManyIslands) {
        String idIsland;
        for (int i = 0; i < howManyIslands; i++) {
            islandList.add(new Island("I_:" + i + 1));
        }
    }

    /**
     * @return boolean It allows to getCoin requested by the Game
     */
    public boolean getCoin() {
        return coinsleft > 0;
    }

    /**
     * @throws Exception If there is not coin
     */
    public void giveCoin() throws Exception {
        if (getCoin())
            coinsleft--;
        else
            throw new Exception("Coins left : 0");
    }

    /**
     * @return Bag status
     */
    public Optional<StudentsManager> getBag() {
        return Optional.of(bag);
    }

    /**
     * Merges and replaces two islands
     * @param island1 First island to merge
     * @param island2 Second island to merge
     */
    public void mergeIsland(Island island1, Island island2) {
        Island merge = new Island(island1, island2);
        islandList.add(islandList.indexOf(island1), merge);
        islandList.remove(island1);
        islandList.remove(island2);
    }

    /**
     * @return List of island
     */
    public List<Island> getIslandList() {
        return islandList;
    }

    /**
     * @return List of Cloud
     */
    public List<Cloud> getCloudList() {
        return CloudList;
    }

    /**
     * @param id Cloud's Id
     * @return Cloud you are looking for
     */
    public Optional<StudentsManager> getCloudById(String id) {
        Optional<StudentsManager> result = CloudList.stream()
                .filter(cloud -> cloud.getId().equals(id))
                .findAny()
                .map(manager -> (StudentsManager) manager);
        return result;
    }

    /**
     * @param id Island's Id
     * @return Island you are looking for
     */
    public Optional<StudentsManager> getIslandById(String id) {
        Optional<StudentsManager> result = islandList.stream()
                .filter(island -> island.getId() == id)
                .findAny()
                .map(manager -> (StudentsManager) manager);
        return result;
    }


}
