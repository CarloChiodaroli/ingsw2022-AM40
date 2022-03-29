package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.StudentsManager;

public class SchoolEntrance extends StudentsManager {

    /**
     * class constructor
     */
    public SchoolEntrance(int studentCapacity) {
        super(studentCapacity, studentCapacity);
    }
}