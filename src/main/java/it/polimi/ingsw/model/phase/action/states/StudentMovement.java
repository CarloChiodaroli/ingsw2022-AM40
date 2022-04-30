package it.polimi.ingsw.model.phase.action.states;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.phase.action.ActionFaseState;
import it.polimi.ingsw.model.phase.action.ActionPhase;
import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentMovement extends ActionFaseState {

    public StudentMovement(ActionPhase actionPhase) {
        super(actionPhase);
    }

    @Override
    public void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
        if (from.isPresent() && to.isPresent())
            if (from.get().removeStudent(color))
                to.get().addStudent(color);
        controlTeachers();
    }

    private void controlTeachers() {
        List<Player> players = super.getActionFase().getGame().getPlayers();
        for (TeacherColor color : TeacherColor.values()) {

            Optional<Player> playerWithTeacher = players.stream()
                    .filter(x -> x.hasTeacher(color))
                    .findFirst();

            List<Player> candidates = players.stream()
                    .filter(player -> player != playerWithTeacher.orElse(null))
                    .filter(player -> player.howManyStudentsInRoom(color) > playerWithTeacher.map(player1 -> player1.howManyStudentsInRoom(color)).orElse(0))
                    .collect(Collectors.toList());

            if (candidates.size() == 1) {
                playerWithTeacher.ifPresent(player -> player.removeTeacher(color));
                candidates.get(0).addTeacher(color);
            }
        }
    }
}
