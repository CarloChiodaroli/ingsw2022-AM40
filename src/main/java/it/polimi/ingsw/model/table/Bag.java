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

        for(TeacherColor color : TeacherColor.values()){
            for(int i = 0; i < 24; i++){
                this.addStudent(color);
            }
        }

        this.setStudentTot(120);
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
            if (i == 0 && howManyStudents(TeacherColor.YELLOW) > 0) {
                removeStudent(TeacherColor.YELLOW);
                return TeacherColor.YELLOW;
            }
            if (i == 0 && howManyStudents(TeacherColor.YELLOW) == 0)
                i = (int) (Math.random()*5);
            if (i == 1 && howManyStudents(TeacherColor.PINK) > 0) {
                removeStudent(TeacherColor.PINK);
                return TeacherColor.PINK;
            }
            if (i == 1 && howManyStudents(TeacherColor.PINK) == 0)
                i = (int) (Math.random() * 5);
            if (i == 2 && howManyStudents(TeacherColor.RED) > 0) {
                removeStudent(TeacherColor.RED);
                return TeacherColor.RED;
            }
            if (i == 2 && howManyStudents(TeacherColor.RED) == 0)
                i = (int) (Math.random() * 5);
            if (i == 3 && howManyStudents(TeacherColor.GREEN) > 0) {
                removeStudent(TeacherColor.GREEN);
                return TeacherColor.GREEN;
            }
            if (i == 3 && howManyStudents(TeacherColor.GREEN) == 0)
                i = (int) (Math.random() * 5);
            if (i == 4 && howManyStudents(TeacherColor.BLUE) > 0) {
                removeStudent(TeacherColor.BLUE);
                return TeacherColor.BLUE;
            }
            if (i == 4 && howManyStudents(TeacherColor.BLUE) == 0)
                i = (int) (Math.random() * 5);
        }
        return null;
    }

}
