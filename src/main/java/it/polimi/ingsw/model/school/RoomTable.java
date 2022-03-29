package it.polimi.ingsw.model.school;

package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.TeacherColor;

public class RoomTable {
    private final TeacherColor teacherColor;
    private boolean teacherPresence;

    public RoomTable(TeacherColor teacherColor){
        super(10, 10);
        this.teacherPresence = false;
        this.teacherColor = teacherColor;
    }

    /**
     * setter
     */
    public void setTeacherPresence(boolean teacherPresence) {
        this.teacherPresence = teacherPresence;
    }

    /**
     * getter
     */
    public TeacherColor getTeacherColor() {
        return teacherColor;
    }

    /**
     * getter
     */
    public boolean hasTeacher(){
        return teacherPresence;
    }

    /**
     * assigned to numOfStudents the number of students there are in the room
     */
    public int howManyStudentsColor(){
        return super.howManyStudents(teacherColor);
    }

    /**
     * add one student to the room
     */
    public void add1Student(){
        super.addStudent(teacherColor);
    }

}
