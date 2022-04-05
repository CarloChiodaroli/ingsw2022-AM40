package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;

import java.util.*;

/**
 * CLASS TABLE (Idea -> It is a physic table)
 * It controls all the contents
 */
public class Table {

    private List<Island> IslandList;
    private List<Cloud> CloudList;
    public static Bag bag;
    private int coinsleft;


    /**
     * Constructor
     */
    public Table(int numberofplayer)
    {
        int randomFirstIsland=(int)(Math.random()*12);
        bag = new Bag(130,26);
        coinsleft = 20;
        IslandList = new ArrayList<Island>();
        CloudList = new ArrayList<Cloud>();
        buildsIsland(12);
        FillCInitialIslandWithStudent(randomFirstIsland);
        MotherNature.getMotherNature().setPosition(IslandList.get(randomFirstIsland));
        buildsCloud(numberofplayer);
    }

    public void FillCloudRound()
    {
        for (Cloud cloud: CloudList)
            cloud.buildClouds(bag);
    }


    /**
     *
     * @param numberofplayer number of player to create
     */
    private void buildsCloud(int numberofplayer)
    {
        for(int i=0;i<numberofplayer;i++)
        {
            CloudList.add(new Cloud("c_"+(i+1),numberofplayer+1));
        }
    }

    public void FillCInitialIslandWithStudent(int randomFirstIsland)
    {
    int idisland;
    int emptyIsland1;
    int emptyIsland2;
    ArrayList<Integer> array=new ArrayList<>();

    if(randomFirstIsland >= 6){
        emptyIsland1 = randomFirstIsland - 6;
        emptyIsland2 = randomFirstIsland;
    }
    else{
        emptyIsland1 = randomFirstIsland;
        emptyIsland2 = randomFirstIsland + 6;
    }

    for(int i=0;i<12;i++)
        array.add(i);

    for(TeacherColor tc: TeacherColor.values())
        {
            for (int i=0;i<2;i++)
                {
                    //Extract correct number
                    idisland=(int)(Math.random()*12);
                    while(!array.contains(idisland)||idisland==emptyIsland1 ||idisland==emptyIsland2)
                    {
                        idisland=(int)(Math.random()*12);
                    }

            IslandList.get(idisland).addStudent(tc);
            bag.removeStudent(tc);
            array.remove(new Integer(idisland));
                }
        }
    }

    /**
     *
     * @param howManyIslands number of islands to create
     */
    private void buildsIsland(int howManyIslands)
    {
        for(int i=0;i<howManyIslands;i++)
        {
            IslandList.add(new Island("i_"+(i+1)));
        }
    }

    /**
     *
     * @return boolean It allows to getCoin requested by the Game
     */
    public boolean getCoin()
    {
        return coinsleft > 0;
    }

    public int getNumCoin(){
        return coinsleft;
    }

    /**
     *
     * @throws Exception If there is not coin
     */
    public void giveCoin()
    {
        if(getCoin())
            coinsleft--;
    }

    /**
     *
     * @return Bag status
     */
    public Optional<StudentsManager> getBag()
    {
        return Optional.of(bag);
    }

    /**
     *
     * @param island1 First island to merge
     * @param island2 Second island to merge
     */
    public void mergeIsland(Island island1,Island island2)
    {
        Island IslandToAdd = new Island(island1,island2);
        int FirstPositionOfIslandToAdd = IslandList.indexOf(island1);
        IslandList.remove(island1);
        IslandList.remove(island2);
        IslandList.add(FirstPositionOfIslandToAdd,IslandToAdd);
        for(int i=FirstPositionOfIslandToAdd+1;i<IslandList.size()-1;i++)
            IslandList.add(i,IslandList.get(i+1));
        IslandList.remove(IslandList.size()-1);
    }

    /**
     *
     * @return List of island
     */
    public List<Island> getIslandList()
    {
        return IslandList;
    }
    /**
     *
     * @return List of Cloud
     */
    public List<Cloud> getCloudList()
    {
        return CloudList;
    }

    /**
     *
     * @param id Cloud's Id
     * @return Cloud you are looking for
     */
    public Optional<StudentsManager> getCloudById(String id)
    {
        Optional<StudentsManager> result = CloudList.stream()
                .filter(cloud -> cloud.getId().equals(id))
                .findAny()
                .map(manager -> (StudentsManager) manager);
        return result;
    }
    /**
     *
     * @param id Island's Id
     * @return Island you are looking for
     */
    public Optional<StudentsManager> getIslandById(String id)
    {
        Optional<StudentsManager> result = IslandList.stream()
                .filter(island -> island.getId()==id)
                .findAny()
                .map(manager -> (StudentsManager) manager);
        return result;
    }

}
