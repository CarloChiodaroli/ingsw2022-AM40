package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.school.RoomTable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class StudentMovement extends ActionFaseState {

    StudentMovement(ActionFase actionFase) {
        super(actionFase);
    }

    @Override
    protected void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
        if (from.isPresent() && to.isPresent())
            if (from.get().removeStudent(color)) to.get().addStudent(color);
        controlTeachers();
    }

    private void controlTeachers(){
        List<Player> players = super.getActionFase().getGame().getPlayers();
        for(TeacherColor color: TeacherColor.values()){
            Optional<RoomTable> roomTableWithTeacher = players.stream()
                    .filter(x -> x.getRoomTable(color).hasTeacher())
                    .findFirst()
                    .map(x -> x.getRoomTable(color));

                Integer max = players.stream()
                        .map(x -> x.getRoomTable(color).howManyStudentsColor())
                        .max(Integer::compareTo)
                        .orElse(-1);

                List<Player> maxPlayers = players.stream()
                        .filter(x -> x.getRoomTable(color).howManyStudentsColor() == max)
                        .collect(Collectors.toList());

            if(roomTableWithTeacher.isPresent()){
                if(maxPlayers.size() == 1){
                    maxPlayers.get(0).getRoomTable(color).setTeacherPresence(roomTableWithTeacher.get().removeTeacher());
                }
            } else {
                if(maxPlayers.size() == 1){
                    maxPlayers.get(0).getRoomTable(color).setTeacherPresence(true);
                }
            }
        }
    }
}
