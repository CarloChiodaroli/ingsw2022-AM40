package it.polimi.ingsw.server.model.phase.action.states.cards;

import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.ActionFaseState;
import it.polimi.ingsw.server.model.phase.action.StudentsContainer;
import it.polimi.ingsw.server.model.enums.Characters;
import it.polimi.ingsw.server.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.server.model.phase.action.states.StudentMovement;
import it.polimi.ingsw.server.model.player.Player;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentMovementCard extends CharacterCard {

    private StudentMovement decorated;
    private StudentsContainer students = null;
    private final int maxUsages;

    public StudentMovementCard(Characters characters, ActionPhase actionPhase, Map<String, Integer> args) {
        super(args, characters, actionPhase);
        if (args.get("Memory") > 0) {
            this.students = new StudentsContainer(args.get("Memory"));
            for (int i = 0; i < args.get("Memory"); i++) {
                students.addStudent(getStudentFromBag());
            }
        }
        this.maxUsages = args.get("Usages");
    }

    private TeacherColor getStudentFromBag(){
        return getActionFase().getGame().getTable().getStudentFromBag().orElseThrow();
    }

    @Override
    public void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
        if (!isInUse()) return;
        if (from.isEmpty() && super.getCharacterization("Bidirectional") == 0) {
            decorated.handle(color, Optional.of(students), to);
            students.addStudent(getStudentFromBag());
        } else {
            decorated.handle(color, from, to);
        }
        if (super.getCharacterization("TeacherBehaviour") > 0) {
            controlTeachers(super.getActualPlayer());
        }
    }

    @Override
    public void handle(Player player, TeacherColor fromEntrance, TeacherColor toColor) {
        if (!isInUse()) return;
        if (super.getCharacterization("Room") != 0) {
            decorated.handle(fromEntrance, Optional.of(player.getEntrance()), Optional.of(player.getRoomTable(fromEntrance)));
            decorated.handle(toColor, Optional.of(player.getRoomTable(toColor)), Optional.of(player.getEntrance()));
            super.updateUse();
        } else if (super.getCharacterization("Memory") == 6) {
            TeacherColor tmp = null;
            for (TeacherColor color : TeacherColor.values()) {
                if (color != fromEntrance && color != toColor && students.howManyStudents(color) > 0) {
                    tmp = color;
                }
            }
            if (tmp == null) {
                if (students.howManyStudents(fromEntrance) > 0) tmp = fromEntrance;
                else tmp = toColor;
            }
            students.removeStudent(tmp);
            decorated.handle(fromEntrance, Optional.of(player.getEntrance()), Optional.of(students));
            decorated.handle(toColor, Optional.of(students), Optional.of(player.getEntrance()));
            students.addStudent(tmp);
            super.updateUse();
        }
    }

    public void activator(ActionFaseState decorated, Player player) throws InvalidParameterException {
        playerPays(player);
        this.decorated = (StudentMovement) decorated;
        super.activator(player);
    }

    public void activator(ActionFaseState decorated, Player player, TeacherColor color) throws InvalidParameterException {
        playerPays(player);
        this.decorated = (StudentMovement) decorated;
        super.activator(player, color);
        if (this.getCharacterization("Usages") == 0) {
            List<Player> players = super.getActionFase().getGame().getPlayers();
            for (Player player1 : players) {
                for (int i = 0; i < 3; i++)
                    player1.getRoomTable(color).removeStudent(color);
            }
        }
    }

    private void controlTeachers(Player player) {
        List<Player> players = super.getActionFase().getGame().getPlayers();
        for (TeacherColor color : TeacherColor.values()) {

            Optional<Player> maxPlayer = players.stream()
                    .filter(x -> x.hasTeacher(color))
                    .findFirst();

            if (maxPlayer.isPresent() &&
                    !maxPlayer.get().equals(player) &&
                    maxPlayer.get().howManyStudentsInRoom(color) == player.howManyStudentsInRoom(color)) {
                player.addTeacher(color);
                maxPlayer.get().removeTeacher(color);
            }
        }
    }

    public StudentsManager getStudents() {
        return students;
    }

    public Optional<StudentsManager> getStudentContainer(){
        return Optional.ofNullable(students);
    }

    public int getMaxUsages(){
        return maxUsages;
    }
}
