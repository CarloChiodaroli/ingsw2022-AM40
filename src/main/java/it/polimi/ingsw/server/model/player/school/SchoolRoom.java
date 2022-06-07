package it.polimi.ingsw.server.model.player.school;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchoolRoom extends StudentsManager {

    private final static int lengthOfTables = 10;
    private final Map<TeacherColor, Boolean> teachers;

    /**
     * construction of 5 rooms, one for each color
     */
    public SchoolRoom() {
        super(lengthOfTables * 5, lengthOfTables);
        teachers = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            teachers.put(color, false);
        }
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
}
