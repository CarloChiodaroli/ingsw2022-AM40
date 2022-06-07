package it.polimi.ingsw.server.model.player.school;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchoolRoom extends StudentsManager {

    private final static int lengthOfTables = 10;
    private final Map<TeacherColor, Boolean> teachers;
    private boolean moneyPlace;

    /**
     * construction of 5 rooms, one for each color
     */
    public SchoolRoom() {
        super(lengthOfTables * 5, lengthOfTables);
        teachers = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            teachers.put(color, false);
        }
        resetMoneyPlace();
    }

    public void addTeacher(TeacherColor color) {
        teachers.replace(color, true);
    }

    public void removeTeacher(TeacherColor color) {
        teachers.replace(color, false);
    }

    public boolean getTeacherPresence(TeacherColor color) {
        return teachers.get(color);
    }

    @Override
    public boolean addStudent(TeacherColor color) {
        int old = super.howManyStudents(color);
        boolean result = super.addStudent(color);
        int actual = super.howManyStudents(color);
        if(result){
            if(old < actual && actual!=0 && actual%3 == 0){
                this.moneyPlace = true;
            }
        }
        return result;
    }

    public boolean getMoneyPlace() {
        return moneyPlace;
    }

    public void resetMoneyPlace() {
        moneyPlace = false;
    }
}
