package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;


/**
 * CLASS Cloud
 */
public class Cloud extends StudentsManager {
    private final String id;
    public static Bag bag;
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

    private void buildClouds(int equalsStudents)
    {   int color;
        for (int i = 0 ;i<equalsStudents;i++)
        {
            color=(int)(Math.random()*5);
            this.addStudent(TeacherColor.values()[color]);
            bag.removeStudent(TeacherColor.values()[color]);
        }

    }
}
