package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.StudentsManager;

import java.util.*;

/**
 * CLASS TABLE (Idea -> It is a physic table)
 * It controls all the contents
 */
public class Table {

    private List<Island> IslandList;
    private List<Cloud> CloudList;
    private Bag bag;
    private int coinsleft;


    /**
     * Constructor
     */
    public Table(int numberofplayer)
    {   int randomFirstIsland=(int)(Math.random()*12);
        bag= new Bag(130,26);
        coinsleft=20;
        IslandList= new ArrayList<Island>();
        CloudList= new ArrayList<Cloud>();
        buildsIsland(12);
        FillCInitialIslandWithStudent(randomFirstIsland);
        MotherNature.getMotherNature().setPosition(IslandList.get(randomFirstIsland));
    }

    /**
     *
     * @param howManyClouds number of clouds to create
     */
    public void buildsCloud(int howManyClouds)
    {
        for(int i=0;i<howManyClouds;i++)
        {
            CloudList.add(new Cloud("C_:"+i+1,howManyClouds+1));
        }
    }

    public void FillCInitialIslandWithStudent(int randomFirstIsland)
    {

    }

    /**
     *
     * @param howManyIslands number of islands to create
     */
    private void buildsIsland(int howManyIslands)
    {
        for(int i=0;i<howManyIslands;i++)
        {
            IslandList.add(new Island("I_:"+i+1));
        }
    }

    /**
     *
     * @return boolean It allows to getCoin requested by the Game
     */
    public boolean getCoin()
    {
        return coinsleft>0;
    }

    /**
     *
     * @throws Exception If there is not coin
     */
    public void giveCoin() throws Exception
    {
        if(getCoin())
            coinsleft--;
        else
            throw new Exception("Coins left : 0");
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
    public void MergeIsland(Island island1,Island island2)
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
