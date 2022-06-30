package it.polimi.ingsw.server.model.phase.action.states.cards;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.ActionPhaseState;
import it.polimi.ingsw.server.model.phase.action.StudentsContainer;
import it.polimi.ingsw.server.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.server.model.phase.action.states.StudentMovement;
import it.polimi.ingsw.server.model.player.Player;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 1st type of Character card, this card decors the action phase's {@link StudentMovement} State, modelling all student movement related character card behaviours.
 */
public class StudentMovementCard extends CharacterCard {

    private StudentMovement decorated;
    private StudentsContainer students = null;
    private final int maxUsages;

    /**
     * Constructor
     */
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

    /**
     * Get a student from the bag
     *
     * @return color of the caught student
     */
    private TeacherColor getStudentFromBag() {
        return getActionPhase().getGame().getTable().getStudentFromBag().orElseThrow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
        if (!isInUse()) throw new IllegalStateException("Card has been already used");
        if (from.isEmpty() && super.getCharacterization("Bidirectional") == 0 && super.getCharacterization("Memory") > 0) {
            decorated.handle(color, Optional.of(students), to);
            students.addStudent(getStudentFromBag());
        } else {
            decorated.handle(color, from, to);
            if (super.getCharacterization("TeacherBehaviour") > 0) {
                controlTeachers(super.getActualPlayer());
                super.updateUse();
            }
            return;
        }
        super.updateUse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Player player, TeacherColor fromEntrance, TeacherColor toColor, String place) {
        if (!isInUse()) return;
        if (super.getCharacterization("Room") != 0)
            if (place.equals("Room")) {
                decorated.handle(fromEntrance, Optional.of(player.getEntrance()), Optional.of(player.getRoomTable()));
                decorated.handle(toColor, Optional.of(player.getRoomTable()), Optional.of(player.getEntrance()));
                super.updateUse();
            } else {
                throw new IllegalArgumentException("Not valid place id");
            }
        else if (super.getCharacterization("Memory") == 6 && place.equals("Card")) {
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
        } else {
            throw new IllegalStateException("Not valid command in this state");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activator(ActionPhaseState decorated, Player player) throws InvalidParameterException {
        if (super.getCharacterization("EffectAllPlayers") > 0) {
            throw new IllegalStateException("This card needs a student color to be activated");
        }
        playerPays(player);
        this.decorated = (StudentMovement) decorated;
        super.activator(player);
        if (super.getCharacterization("TeacherBehaviour") > 0) {
            controlTeachers(player);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activator(ActionPhaseState decorated, Player player, TeacherColor color) throws InvalidParameterException {
        playerPays(player);
        this.decorated = (StudentMovement) decorated;
        super.activator(player, color);
        if (this.getCharacterization("Usages") == 0) {
            List<Player> players = super.getActionPhase().getGame().getPlayers();
            for (Player player1 : players) {
                for (int i = 0; i < 3; i++)
                    player1.getRoomTable().removeStudent(color);
            }
        }
    }

    /**
     * If player has the same number of student in a room, it's given to him the teacher
     *
     * @param player player
     */
    private void controlTeachers(Player player) {
        List<Player> players = super.getActionPhase().getGame().getPlayers();
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

    /**
     * Get the students container
     *
     * @return container
     */
    public StudentsManager getStudents() {
        return students;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StudentsManager> getStudentContainer() {
        return Optional.ofNullable(students);
    }

    public int getMaxUsages() {
        return maxUsages;
    }
}
