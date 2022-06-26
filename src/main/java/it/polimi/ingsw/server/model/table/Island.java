package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.StudentsManager;

import java.util.Optional;

/**
 * Models game's Island.
 */
public class Island extends StudentsManager {
    private final String id;
    private final int equivalentIsland;
    private boolean noEntry;
    private TowerColor towerColor;
    private boolean tower;

    /**
     * Constructor
     *
     * @param id Island's Id
     */
    public Island(String id) {
        super(130, 130);
        this.id = id;
        this.equivalentIsland = 1;
        this.noEntry = false;
        this.towerColor = null;
        this.tower = false;
    }

    /**
     * Constructor of merge islands
     *
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
        addStudentFromSmallIsland(island1);
        addStudentFromSmallIsland(island2);
    }

    /**
     * Move all to student from parameter to this island
     *
     * @param island Old Island
     */
    public void addStudentFromSmallIsland(Island island) {
        for (TeacherColor tc : TeacherColor.values()) {
            for (int i = 0; i < island.howManyStudents(tc); i++) {
                this.addStudent(tc);
            }
        }

    }

    /**
     * Get the new id of the island
     *
     * @param id1 First Island's Id
     * @param id2 Second Island's Id
     * @return String Final Island's Id
     */
    public String elaborationMergeIslandId(String id1, String id2) {
        return id1 + "" + id2.substring(1);
    }

    /**
     * Set the tower of the specific color
     *
     * @param color color of the tower put in the island
     */
    public void setInfluence(TowerColor color) {
        tower = true;
        towerColor = color;
    }

    /**
     * Remove the prohibition cards
     */
    public void removeNoEntryTile() {
        noEntry = false;
    }

    /**
     * Get the number of towers there are in the island
     *
     * @return number of towers
     */
    public int howManyTowers() {
        if (this.tower) return howManyEquivalents();
        else return 0;
    }

    /**
     * Get how many islands it matches
     *
     * @return how many islands does it match
     */
    public int howManyEquivalents() {
        return equivalentIsland;
    }

    /**
     * Get if present the prohibition card
     *
     * @return true if there's the card
     */
    public boolean hasNoEntryTile() {
        return noEntry;
    }

    /**
     * Get if there's the tower on the island
     *
     * @return true if there's the tower
     */
    public boolean hasTowers() {
        return tower;
    }

    /**
     * Set the prohibition card
     *
     * @param noEntry true if there's the card
     */
    public void setNoEntry(boolean noEntry) {
        this.noEntry = noEntry;
    }

    @Deprecated
    public void setTowerColor(Optional<TowerColor> towerColor) {
        this.towerColor = towerColor.orElse(null);
    }

    /**
     * Get the color of the tower
     *
     * @return tower color
     */
    public Optional<TowerColor> getTowerColor() {
        return Optional.ofNullable(towerColor);
    }

    /**
     * Set the presence or not of tower
     *
     * @param tower true if there's the tower
     */
    public void setTower(boolean tower) {
        this.tower = tower;
    }

    /**
     * Get id of the island
     *
     * @return island's id
     */
    public String getId() {
        return id;
    }

    /**
     * Get how many islands it matches
     *
     * @return how many islands does it match
     */
    public int getEquivalent() {
        return equivalentIsland;
    }


}
