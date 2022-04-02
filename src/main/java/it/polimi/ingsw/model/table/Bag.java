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
        int i = (int) (Math.random()*5);
        boolean find = false;
        while(!find) {
            if (i == 0 && addStudent(TeacherColor.YELLOW))
                return TeacherColor.YELLOW;
            if (i == 0 && !addStudent(TeacherColor.YELLOW))
                i = (int) (Math.random()*5);
            if (i == 1 && addStudent(TeacherColor.PINK))
                return TeacherColor.PINK;
            if (i == 1 && !addStudent(TeacherColor.PINK))
                i = (int) (Math.random() * 5);
            if (i == 2 && addStudent(TeacherColor.RED))
                return TeacherColor.RED;
            if (i == 2 && !addStudent(TeacherColor.RED))
                i = (int) (Math.random() * 5);
            if (i == 3 && addStudent(TeacherColor.GREEN))
                return TeacherColor.GREEN;
            if (i == 3 && !addStudent(TeacherColor.GREEN))
                i = (int) (Math.random() * 5);
            if (i == 4 && addStudent(TeacherColor.BLUE))
                return TeacherColor.BLUE;
            if (i == 4 && !addStudent(TeacherColor.BLUE))
                i = (int) (Math.random() * 5);
        }
        return null;
    }

}
