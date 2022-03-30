package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.TowerColor;

import java.util.ArrayList;

public class SchoolRoom {
    private final ArrayList<RoomTable> table;

    /**
     * construction of 5 rooms, one for each color
     */
    public SchoolRoom(){
        table = new ArrayList<>();
        for(TeacherColor color: TeacherColor.values()){
            table.add(new RoomTable(color));
        }
    }

    /**
     * @param color of the request room
     * @return the room of the color in the parameter
     */
    public RoomTable getTable(TeacherColor color) {
        return table.stream()
                .filter(x -> x.getTeacherColor().equals(color))
                .findFirst().get();
    }
}
