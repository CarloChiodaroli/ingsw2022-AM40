package it.polimi.ingsw.model.player.school;

import it.polimi.ingsw.model.StudentsManager;

public class SchoolEntrance extends StudentsManager {

    /**
     * class constructor
     * @param studentCapacity
     */
    public SchoolEntrance(int studentCapacity) {
        super(studentCapacity, studentCapacity);
    }
}