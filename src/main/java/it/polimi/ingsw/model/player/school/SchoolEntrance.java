package it.polimi.ingsw.model.player.school;

import it.polimi.ingsw.model.StudentsManager;

public class SchoolEntrance extends StudentsManager {

    /**
     * class constructor
     *
     * @param studentCapacity is the number of students a player can keep in its Entrance
     */
    public SchoolEntrance(int studentCapacity) {
        super(studentCapacity);
    }
}