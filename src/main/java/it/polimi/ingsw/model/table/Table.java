package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.TeacherColor;

import java.util.*;

/**
 * CLASS TABLE (Idea -> It is a physic table)
 * It controls all the contents
 */
public class Table {

    private List<Island> islandList;
    private List<Cloud> CloudList;
    public static Bag bag;
    private int coinsleft;


    /**
     * Constructor
     */
    public Table(int numberofplayer) {
        int randomFirstIsland = (int) (Math.random() * 12);
        bag = new Bag(130, 26);
        coinsleft = 20;
        islandList = new ArrayList<Island>();
        CloudList = new ArrayList<Cloud>();
        buildsIsland(12);
        FillCInitialIslandWithStudent(randomFirstIsland);
        MotherNature.getMotherNature().setPosition(islandList.get(randomFirstIsland));
        buildsCloud(numberofplayer);
    }


    public void FillCloudRound() {
        for (Cloud cloud : CloudList)
            cloud.buildClouds(bag);
    }


    /**
     * @param numberofplayer number of player to create
     */
    private void buildsCloud(int numberofplayer) {
        for (int i = 0; i < numberofplayer; i++) {
            CloudList.add(new Cloud("c_" + (i + 1), numberofplayer + 1));
        }
    }

    public void FillCInitialIslandWithStudent(int randomFirstIsland) {
        int emptyIsland1;
        int emptyIsland2;
        ArrayList<Integer> array = new ArrayList<>();

        Map<TeacherColor, Integer> assignsLeft = new HashMap<>();
        for(TeacherColor color: TeacherColor.values()){
            assignsLeft.put(color, 2);
        }

        if (randomFirstIsland >= 6) {
            emptyIsland1 = randomFirstIsland - 6;
            emptyIsland2 = randomFirstIsland;
        } else {
            emptyIsland1 = randomFirstIsland;
            emptyIsland2 = randomFirstIsland + 6;
        }

        for(int i = 0; i<12; i++){
            if(i != emptyIsland1 && i != emptyIsland2){
                int indexColor;
                TeacherColor actualColor;
                do {
                    indexColor = (int) (Math.random() * 5);
                    actualColor = TeacherColor.values()[indexColor];
                } while(assignsLeft.get(actualColor).equals(0));
                assignsLeft.compute(actualColor, (k, val) -> val - 1);
                if(bag.removeStudent(actualColor)) islandList.get(i).addStudent(actualColor);
            }
        }
    }

    /**
     * @param howManyIslands number of islands to create
     */
    private void buildsIsland(int howManyIslands) {
        for (int i = 0; i < howManyIslands; i++) {
            islandList.add(new Island("i_" + (i + 1)));
        }
    }

    /**
     * @return boolean It allows to getCoin requested by the Game
     */
    public boolean getCoin() {
        return coinsleft > 0;
    }

    public int getNumCoin() {
        return coinsleft;
    }

    /**
     * @throws Exception If there is not coin
     */
    public void giveCoin() {
        if (getCoin())
            coinsleft--;
    }

    /**
     * @return Bag status
     */
    public Optional<StudentsManager> getBag() {
        return Optional.of(bag);
    }

    /**
     * @param island1 First island to merge
     * @param island2 Second island to merge
     */
    public void mergeIsland(Island island1, Island island2) {
        Island IslandToAdd = new Island(island1, island2);
        int FirstPositionOfIslandToAdd = islandList.indexOf(island1);
        islandList.add(FirstPositionOfIslandToAdd, IslandToAdd);
        islandList.remove(island1);
        islandList.remove(island2);
        //for (int i = FirstPositionOfIslandToAdd + 1; i < IslandList.size() - 1; i++)
        //    IslandList.add(i, IslandList.get(i + 1));
        //IslandList.remove(IslandList.size() - 1);
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
    public Optional<Cloud> getCloudById(String id) {
        return CloudList.stream()
                .filter(cloud -> cloud.getId().equals(id))
                .findAny();
    }

    /**
     * @param id Island's Id
     * @return Island you are looking for
     */
    public Optional<Island> getIslandById(String id) {
        return islandList.stream()
                .filter(island -> island.getId().equals(id))
                .findAny();
    }


}
