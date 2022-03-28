package it.polimi.ingsw.model;


public abstract class StudentsManager{
    private int studentYellow=0;
    private int studentPink=0;
    private int studentRed=0;
    private int studentGreen=0;
    private int studentBlue=0;
    private int studentTot=0;
    private final int maxStudents;
    private final int maxStudentsColor;

    public StudentsManager(int maxStudents, int maxStudentsColor){
        this.maxStudents = maxStudents;
        this.maxStudentsColor = maxStudentsColor;
    }

    public StudentsManager(int equalsStudents){
        this.maxStudents = equalsStudents;
        this.maxStudentsColor = equalsStudents;
    }
    public boolean addStudent(TeacherColor color) {
        if (studentTot < maxStudents) { // check there aren't 130 students yet
            switch (color) {
                case YELLOW:
                    if (studentYellow < maxStudentsColor) {//check color and therearen't 26 students
                        studentYellow++; //ofMcolor yet, if true add one student
                        studentTot++;
                        return true;
                    }
                    return false;
                case PINK:
                    if (studentPink < maxStudentsColor) {
                        studentPink++;
                        studentTot++;
                        return true;
                    }
                    return false;
                case RED:
                    if (studentRed < maxStudentsColor) {
                        studentRed++;
                        studentTot++;
                        return true;
                    }
                    return false;
                case GREEN:
                    if (studentGreen < maxStudentsColor) {
                        studentGreen++;
                        studentTot++;
                        return true;
                    }
                    return false;
                case BLUE:
                    if (studentBlue < maxStudentsColor) {
                        studentBlue++;
                        studentTot++;
                        return true;
                    }
                    return false;
            }
        }
        return false;
    }
    public boolean removeStudent(TeacherColor color){
        if(studentTot > 0) {//check there's at least one student
            switch (color) {
                case YELLOW:
                    if (studentYellow > 0) { //check color and if there's at least one student of that color
                        studentYellow--; //if true, remove on student
                        studentTot--;
                        return true;
                    }
                    return false;
                case PINK:
                    if (studentPink > 0) {
                        studentPink--;
                        studentTot--;
                        return true;
                    }
                    return false;
                case RED:
                    if (studentRed > 0) {
                        studentRed--;
                        studentTot--;
                        return true;
                    }
                    return false;
                case GREEN:
                    if (studentGreen > 0) {
                        studentGreen--;
                        studentTot--;
                        return true;
                    }
                    return false;
                case BLUE:
                    if (studentBlue > 0) {
                        studentBlue--;
                        studentTot--;
                        return true;
                    }
                    return false;
            }
        }
        return false;
    }
    public int howManyStudents(TeacherColor color)
    {
        switch (color) {//check color and return how many students there are of that color
            case YELLOW:
                return studentYellow;
            case PINK:
                return studentPink;
            case RED:
                return studentRed;
            case GREEN:
                return studentGreen;
            case BLUE:
                return studentBlue;
        }
        return 0;

    }
    public int howManyStudents(){ //return how many students there are
        return studentTot;
    }
}