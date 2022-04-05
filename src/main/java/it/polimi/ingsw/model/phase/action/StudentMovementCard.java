package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.util.List;
import java.util.Map;
import java.util.Optional;

class StudentMovementCard extends CharacterCard {

    private StudentMovement decorated;
    private StudentsContainer students = null;
    private final int maxUsages;

    StudentMovementCard(Characters characters, ActionFase actionFase, Map<String, Integer> args) {
        super(args, characters, actionFase);
        if (args.get("Memory") > 0) this.students = new StudentsContainer(args.get("Memory"), actionFase);
        this.maxUsages = args.get("Usages");
    }

    @Override
    protected void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
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
    protected void handle(Player player, TeacherColor fromColor, TeacherColor toColor) {
        if (!isInUse()) return;
        if (super.getCharacterization("Room") != 1) {
            decorated.handle(fromColor, Optional.of(player.getEntrance()), Optional.of(player.getRoomTable(fromColor)));
            decorated.handle(toColor, Optional.of(player.getRoomTable(toColor)), Optional.of(player.getEntrance()));
            super.updateUse();
        } else if (super.getCharacterization("Memory") == 6) {
            decorated.handle(fromColor, Optional.of(player.getEntrance()), Optional.of(students));
            decorated.handle(toColor, Optional.of(students), Optional.of(player.getEntrance()));
            super.updateUse();
        }
    }

    public void activator(StudentMovement decorated, Player player) {
        this.decorated = decorated;
        super.activator(player);
    }

    public void activator(StudentMovement decorated, Player player, TeacherColor color) {
        if (!playerPays(player)) return;
        this.decorated = decorated;
        super.activator(player, color);
        if (this.getCharacterization("Usages") == 0) {
            List<Player> players = super.getActionFase().getGame().getPlayers();
            for (Player player1 : players) {
                for (int i = 0; i < 3; i++)
                    player1.getRoomTable(color).removeStudent(color);
            }
        }
    }

    public void activator(StudentMovement decorated, Player player, Island island) {    }

    private boolean playerPays(Player player) {
        return player.pay(super.getPrice());
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
}
