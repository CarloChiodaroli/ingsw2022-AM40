package it.polimi.ingsw.model.phase.action.states.cards;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.phase.action.ActionFase;
import it.polimi.ingsw.model.phase.action.StudentsContainer;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.model.phase.action.states.StudentMovement;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Bag;
import it.polimi.ingsw.model.table.Island;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentMovementCard extends CharacterCard {

    private StudentMovement decorated;
    private StudentsContainer students = null;
    private final int maxUsages;

    public StudentMovementCard(Characters characters, ActionFase actionFase, Map<String, Integer> args) {
        super(args, characters, actionFase);
        if (args.get("Memory") > 0) {
            this.students = new StudentsContainer(args.get("Memory"));
            Bag bag = (Bag) getActionFase().getGame().getTable().getBag().orElseThrow();
            for (int i = 0; i < args.get("Memory"); i++) {
                students.addStudent(bag.getAStudent());
            }
        }
        this.maxUsages = args.get("Usages");
    }

    @Override
    public void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
        if (!isInUse()) return;
        if (from.isEmpty()) {
            decorated.handle(color, Optional.of(students), to); // Needs other control
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

    public boolean activator(StudentMovement decorated, Player player) throws InvalidParameterException {
        if (!playerPays(player)) return false;
        this.decorated = decorated;
        return super.activator(player);
    }

    public boolean activator(StudentMovement decorated, Player player, TeacherColor color) throws InvalidParameterException {
        playerPays(player);
        this.decorated = decorated;
        if (!super.activator(player, color)) return false;
        if (this.getCharacterization("Usages") == 0) {
            List<Player> players = super.getActionFase().getGame().getPlayers();
            for (Player player1 : players) {
                for (int i = 0; i < 3; i++)
                    player1.getRoomTable(color).removeStudent(color);
            }
        }
        return true;
    }

    public void activator(StudentMovement decorated, Player player, Island island) throws InvalidParameterException {
    }

    private void controlTeachers(Player player) {
        List<Player> players = super.getActionFase().getGame().getPlayers();
        for (TeacherColor color : TeacherColor.values()) {

            Optional<Player> maxPlayer = players.stream()
                    .filter(x -> x.getRoomTable(color).hasTeacher())
                    .findFirst();

            if (maxPlayer.isPresent() &&
                    !maxPlayer.get().equals(player) &&
                    maxPlayer.get().getRoomTable(color).howManyStudentsColor() == player.getRoomTable(color).howManyStudentsColor()) {
                player.getRoomTable(color).setTeacherPresence(maxPlayer.get().getRoomTable(color).removeTeacher());
            }
        }
    }

    public StudentsManager getStudents() {
        return students;
    }

    public Optional<StudentsManager> getStudentContainer(){
        return Optional.ofNullable(students);
    }
}
