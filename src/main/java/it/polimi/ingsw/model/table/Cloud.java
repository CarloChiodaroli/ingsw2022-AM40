package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.StudentsManager;

/**
 * CLASS Cloud
 */
public class Cloud extends StudentsManager {
    private final String id;

    /**
     *
     * @param id Cloud's Id
     * @param equalsStudents number of student that cloud can contains
     */
    public Cloud(String id,int equalsStudents)
    {
        super(equalsStudents);
        this.id=id;
    }

    /**
     *
     * @return String Cloud's ID
     */
    public String getId()
    {
        return id;
    }

}
