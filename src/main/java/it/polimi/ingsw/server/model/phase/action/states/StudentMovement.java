package it.polimi.ingsw.server.model.phase.action.states;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.model.phase.action.ActionFaseState;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 1st Action Phase State, this class models the movement of a student between places
 * and the subsequent capturing of teachers by a player.
 */

public class StudentMovement extends ActionFaseState {

    public StudentMovement(ActionPhase actionPhase) {
        super(actionPhase);
    }

    /**
     * The starting method for moving student from a place to another, controls the presence of
     * the places and students
     *
     * @param color student color
     * @param from place where begin the movement
     * @param to place where finish the movement
     * @throws IllegalStateException if the player can't move now
     */
    @Override
    public void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) throws IllegalStateException {
        if (from.isPresent() && to.isPresent())
            if (from.get().removeStudent(color))
                to.get().addStudent(color);
            else throw new IllegalStateException("There is no " + color + " student in from place");
        controlTeachers();
    }

    /**
     * Control and manage of teachers possession
     */
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
