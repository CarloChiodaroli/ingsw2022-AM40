package it.polimi.ingsw.model;


import java.util.Optional;

public abstract class StudentsManager{
    private int studentYellow=0;
    private int studentPink=0;
    private int studentRed=0;
    private int studentGreen=0;
    private int studentBlue=0;
    private int studentTot=0;
    private final int maxStudents;
    private final int maxStudentsColor;

    /**
     * Class Constructor
     * @param maxStudents max number of total students
     * @param maxStudentsColor max number of students of one color
     */
    public StudentsManager(int maxStudents, int maxStudentsColor){
        this.maxStudents = maxStudents;
        this.maxStudentsColor = maxStudentsColor;
    }

    public StudentsManager(int maxStudents){
        this.maxStudents = maxStudents;
        this.maxStudentsColor = maxStudents;
    }

    /**
     * if it's allowed, add one student
     * @param color color of the student to add
     * @return true if the student has been added
     */
    public boolean addStudent(TeacherColor color) {
        boolean perm = false;
        switch (color) {
            case YELLOW:{
                perm = permissionAdd(studentYellow);
                if(perm)
                    studentYellow = indecrement(studentYellow,1);break;}
            case PINK:{
                perm = permissionAdd(studentPink);
                if(perm)
                    studentPink = indecrement(studentPink,1);break;}
            case RED:{
                perm = permissionAdd(studentRed);
                if(perm)
                    studentRed = indecrement(studentRed,1);break;}
            case GREEN:{
                perm = permissionAdd(studentGreen);
                if(perm)
                    studentGreen = indecrement(studentGreen,1);break;}
            case BLUE:{
                perm = permissionAdd(studentBlue);
                if(perm)
                    studentBlue = indecrement(studentBlue,1);break;}
        }
        if(perm) {
            studentTot = indecrement(studentTot, 1);
            return true;
        }
        return false;
    }

    /**
     * if it's allowed, remove one student
     * @param color color of the student to remove
     * @return true if the student has been removed
     */
    public boolean removeStudent(TeacherColor color){
        boolean perm = false;
        switch (color) {
            case YELLOW:
                perm = permissionRemove(studentYellow);
                if(perm)
                    studentYellow = indecrement(studentYellow,-1);
            case PINK:
                perm = permissionRemove(studentPink);
                if(perm)
                    studentPink = indecrement(studentPink,-1);
            case RED:
                perm = permissionRemove(studentRed);
                if(perm)
                    studentRed = indecrement(studentRed,-1);
            case GREEN:
                perm = permissionRemove(studentGreen);
                if(perm)
                    studentGreen = indecrement(studentGreen,-1);
            case BLUE:
                perm = permissionRemove(studentBlue);
                if(perm)
                    studentBlue = indecrement(studentBlue,-1);
        }
        if(perm) {
            studentTot = indecrement(studentTot, -1);
            return true;
        }
        return false;
    }

    /**
     * verify that the student can be added
     * @param students number of students of the color want add
     */
    private boolean permissionAdd(int students){
        return students < maxStudentsColor && studentTot < maxStudents;
    }

    /**
     * verify that the student can be removed
     * @param students number of students of the color want remove
     */
    private boolean permissionRemove(int students){
        return students > 0 && studentTot > 0;
    }

    /**
     *adder / remover
     */
    private int indecrement(int var, int val){
        var += val;
        return var;
    }

    /**
     * @return how many students there are of the desired color
     */
    public int howManyStudents(TeacherColor color){
        switch (color) {
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
            default:
                return 0;
        }
    }

    /**
     * @return how many students there are
     */
    public int howManyTotStudents(){
        return studentTot;
    }

    public Optional<TeacherColor> getStudent(){
        for(TeacherColor color: TeacherColor.values()){
            if(removeStudent(color)){
                return Optional.of(color);
            }
        }
        return Optional.empty();
    }

    public int getMaxStudents()
    {
        return maxStudents;
    }

    public int getStudentYellow() {
        return studentYellow;
    }

    public int getStudentPink() {
        return studentPink;
    }

    public int getStudentRed() {
        return studentRed;
    }

    public int getStudentGreen() {
        return studentGreen;
    }

    public int getStudentBlue() {
        return studentBlue;
    }
}