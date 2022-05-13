package it.polimi.ingsw.server.model;


import it.polimi.ingsw.commons.enums.TeacherColor;

import java.util.HashMap;
import java.util.Optional;

public abstract class StudentsManager {
    private final int maxStudents;
    private final int maxStudentsColor;
    private final HashMap<TeacherColor, Integer> manager;

    /**
     * Class Constructor
     *
     * @param maxStudents      max number of total students
     * @param maxStudentsColor max number of students of one color
     */
    public StudentsManager(int maxStudents, int maxStudentsColor) {
        this.maxStudents = maxStudents;
        this.maxStudentsColor = maxStudentsColor;
        this.manager = new HashMap<>();
        for(TeacherColor color: TeacherColor.values()){
            manager.put(color, 0);
        }
    }

    public StudentsManager(int maxStudents) {
        this.maxStudents = maxStudents;
        this.maxStudentsColor = maxStudents;
        this.manager = new HashMap<>();
        for(TeacherColor color: TeacherColor.values()) {
            manager.put(color, 0);
        }
    }

    /**
     * if it's allowed, add one student
     *
     * @param color color of the student to add
     * @return true if the student has been added
     */
    public boolean addStudent(TeacherColor color) {
        boolean perm;
        perm = permissionAdd(manager.get(color));
        if(perm){
            manager.replace(color, manager.get(color),manager.get(color) + 1);
            return true;
        }
        return false;
    }

    /**
     * if it's allowed, remove one student
     *
     * @param color color of the student to remove
     * @return true if the student has been removed
     */
    public boolean removeStudent(TeacherColor color) {
        boolean perm;
        perm = permissionRemove(manager.get(color));
        if(perm){
            manager.replace(color, manager.get(color),manager.get(color) - 1);
            return true;
        }
        return false;
    }

    /**
     * verify that the student can be added
     *
     * @param students number of students of the color want add
     */
    private boolean permissionAdd(int students) {
        return students < maxStudentsColor && howManyTotStudents() < maxStudents;
    }

    /**
     * verify that the student can be removed
     *
     * @param students number of students of the color want remove
     */
    private boolean permissionRemove(int students) {
        return students > 0 && howManyTotStudents() > 0;
    }

    /**
     * @return how many students there are of the desired color
     */
    public int howManyStudents(TeacherColor color) {
        return manager.get(color);
    }

    /**
     * @return how many students there are
     */
    public int howManyTotStudents() {
        int studentTot = 0;
        for(TeacherColor color: TeacherColor.values()){
            studentTot += manager.get(color);
        }
        return studentTot;
    }

    public Optional<TeacherColor> getStudent() {
        for (TeacherColor color : TeacherColor.values()) {
            if (removeStudent(color)) {
                return Optional.of(color);
            }
        }
        return Optional.empty();
    }

    public int getMaxStudents() {
        return maxStudents;
    }
}