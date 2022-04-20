package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;

import java.util.Optional;

/**
 * ISLAND'S ID : I_1, I_2 , MERGE(I_1,I_2) -> I_1_2
 */
public class Island extends StudentsManager {
    private final String id;
    private final int equivalentIsland;
    private boolean noEntry;
    private Optional<TowerColor> towerColor;
    private boolean tower;

    /**
     * @param id Island's Id
     */
    public Island(String id) {
        super(130, 130);
        this.id = id;
        this.equivalentIsland = 1;
        this.noEntry = false;
        this.towerColor = Optional.empty();
        this.tower = false;
    }

    /**
     * @param island1 First island to merge
     * @param island2 Second island to merge
     */
    public Island(Island island1, Island island2) {
        super(130, 130);
        this.id = elaborationMergeIslandId(island1.id, island2.id);
        this.equivalentIsland = island1.equivalentIsland + island2.equivalentIsland;
        this.noEntry = (island1.noEntry && island2.noEntry);
        this.towerColor = island1.towerColor;
        this.tower = island1.tower;
        addStudentfromSmallIsland(island1);
        addStudentfromSmallIsland(island2);
    }

    /**
     * Move all to student from parameter to this island
     *
     * @param island Old Island
     */
    public void addStudentfromSmallIsland(Island island) {
        for (TeacherColor tc : TeacherColor.values()) {
            for (int i = 0; i < island.howManyStudents(tc); i++) {
                this.addStudent(tc);
            }
        }

    }

    /**
     * @param id1 First Island's Id
     * @param id2 Second Island's Id
     * @return String Final Island's Id
     */
    public String elaborationMergeIslandId(String id1, String id2) {
        return id1 + "" + id2.substring(1, id2.length());
    }

    public void setInfluence(TowerColor color){
        tower = true;
        towerColor = Optional.of(color);
    }

    public void removeNoEntryTile(){
        noEntry = false;
    }

    public int howManyTowers(){
        if(this.tower) return howManyEquivalents();
        else return 0;
    }

    public int howManyEquivalents() {
        return equivalentIsland;
    }

    public boolean hasNoEntryTile() {
        return noEntry;
    }

    public boolean hasTowers() {
        return tower;
    }

    public void setNoEntry(boolean noEntry) {
        this.noEntry = noEntry;
    }

    public void setTowerColor(Optional<TowerColor> towerColor) {
        this.towerColor = towerColor;
    }

    public Optional<TowerColor> getTowerColor() {
        return towerColor;
    }

    public void setTower(boolean tower) {
        this.tower = tower;
    }

    public String getId() {
        return id;
    }


}
