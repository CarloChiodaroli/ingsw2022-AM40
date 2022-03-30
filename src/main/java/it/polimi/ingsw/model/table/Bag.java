package it.polimi.ingsw.model.table;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;


public class Bag extends StudentsManager {
    /**
     *
     * @param maxStudents Max number of student
     * @param maxStudentsColor Max number of student for each color
     */
    public Bag(int maxStudents,int maxStudentsColor)
    {
        super(maxStudents,maxStudentsColor);
    }

    /**
     *
     * @return TeacherColor It returns a casual color
     */
    public TeacherColor getAStudent()
    {
        return TeacherColor.values()[(int)(Math.random()*5)];
    }

}
