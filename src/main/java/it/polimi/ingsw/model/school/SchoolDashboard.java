package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.TowerColor;

public class SchoolDashboard {
    private final TowerColor colorOfTowers;
    private int numOfTowers;
    private final SchoolEntrance entrance;
    private final SchoolRoom room;

    /**
     * class constructor
     */
    public SchoolDashboard(boolean isThreePlayers, TowerColor towerColor){
        this.room = new SchoolRoom();
        if(!isThreePlayers){
            this.entrance = new SchoolEntrance(7);
            this.numOfTowers = 8;
        }
        else{
            this.entrance = new SchoolEntrance(9);
            this.numOfTowers = 6;
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
     * @param howManyTowers is the number of towers to remove
     * @return true if successful, false if there is no more towers
     */
    public boolean getTower(int howManyTowers){
        return modifyNumTowerOf(howManyTowers * (-1));
    }

    /**
     * add towers to the school dashboard
     * @param howManyTowers number of towers to be added
     */
    public void pushTower(int howManyTowers){
        modifyNumTowerOf(howManyTowers);
    }

    /**
     * Utility method who modifies the number of towers, caring if they go to zero
     * @param howMany is the quantity to add or subtract to the numOfTowers
     * @return true if successful, else false
     */
    private boolean modifyNumTowerOf(int howMany){
        if(howMany > 0){
            numOfTowers += howMany;
            return true;
        } else {
            howMany = howMany * (-1);
            for(int i = 0; i < howMany; i++){
                if(numOfTowers > 0){
                    numOfTowers--;
                } else {
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

