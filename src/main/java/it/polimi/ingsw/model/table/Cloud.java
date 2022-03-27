package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.StudentsManager;

public class Cloud extends StudentsManager {

    private String id;
    public Cloud(String id,int maxStudents,int maxStudentsColor)
    {
        super(maxStudents,maxStudentsColor);
        this.id=id;
    }

    public void buildClouds()
    {

    }
}
