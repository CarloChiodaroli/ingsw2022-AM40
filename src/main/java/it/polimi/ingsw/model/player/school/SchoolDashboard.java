package it.polimi.ingsw.model.player.school;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.TowerColor;

public class SchoolDashboard {
    private final TowerColor colorOfTowers;
    private int numOfTowers;
    private final SchoolEntrance entrance;
    private final SchoolRoom room;
    private final boolean isThreePlayers;

    /**
     * class constructor
     */
    public SchoolDashboard(boolean isThreePlayers, TowerColor towerColor) {
        this.room = new SchoolRoom();
        if (!isThreePlayers) {
            this.entrance = new SchoolEntrance(7);
            this.numOfTowers = 8;
            this.isThreePlayers = false;
        } else {
            this.entrance = new SchoolEntrance(9);
            this.numOfTowers = 6;
            this.isThreePlayers = true;
        }
        this.colorOfTowers = towerColor;
    }

    /**
     * getter
     */
    public int getNumOfTowers() {
        return numOfTowers;
    }

    /**
     * getter
     */
    public TowerColor getTowerColor() {
        return colorOfTowers;
    }

    /**
     * remove towers from the school dashboard
     *
     * @param howManyTowers is the number of towers to remove
     * @return true if successful, false if there is no more towers
     */
    public boolean getTower(int howManyTowers) {
        return modifyNumTowerOf(howManyTowers * (-1));
    }

    /**
     * add towers to the school dashboard
     *
     * @param howManyTowers number of towers to be added
     */
    public boolean pushTower(int howManyTowers) {
        return modifyNumTowerOf(howManyTowers);
    }

    /**
     * Utility method who modifies the number of towers, caring if they go to zero
     *
     * @param howMany is the quantity to add or subtract to the numOfTowers
     * @return true if successful, else false
     */
    private boolean modifyNumTowerOf(int howMany) {
        int oldNumOfTowers = numOfTowers;
        if (howMany > 0) {
            for (int i = 0; i < howMany; i++) {
                if ((!isThreePlayers && numOfTowers < 8) || (isThreePlayers && numOfTowers < 6)) {
                    numOfTowers++;
                } else {
                    numOfTowers = oldNumOfTowers;
                    return false;
                }
            }
        } else {
            howMany = howMany * (-1);
            for (int i = 0; i < howMany; i++) {
                if (numOfTowers > 0) {
                    numOfTowers--;
                } else {
                    numOfTowers = oldNumOfTowers;
                    return false;
                }
            }
        }
        return true;
    }

    public SchoolRoom getRoom() {
        return room;
    }

    public StudentsManager getEntranceAsStudentsManager() {
        return entrance;
    }

}

