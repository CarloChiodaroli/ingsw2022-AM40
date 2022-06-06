package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.commons.enums.TeacherColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class has the job to read {@link PlayState}'s class content and generate a string to print to the user
 * representing client's actual game state.
 */
public class StatePrinter {

    private final static String horizontalLineElement = "=";
    private final static String verticalLineElement = "| ";
    private final static String space = " ";
    private final static String empty = ""; // default
    private final static String yes = "X";
    private final PlayState playState;
    private int idMaxWidth;
    private final List<TeacherColor> colorOrder;

    /**
     * Constructor
     *
     * @param playState {@link PlayState} from which get the game state
     */
    public StatePrinter(PlayState playState) {
        this.playState = playState;
        this.colorOrder = Arrays.stream(TeacherColor.values()).toList();
    }

    /**
     * Generates the updated game view to print to the user.
     * The view ist in Tabellen geteilt, die von vershiedene Methoden erzeugt sind.
     *
     * @return the updated view to print
     */
    public String getState() {
        String state = "\n" + EscapeCli.CLEAR;
        state += islandTable();
        state += cloudTable();
        state += assistantCards();
        state += printDashboard();
        state += statusTowers();
        state += roundState();
        return state;
    }

    /**
     * Creates the table of the status of the towers of every single player.
     *
     * @return the table.
     */
    private String statusTowers() {
        List<String> strings = playState.getPlayersTowerColors().keySet().stream().toList();
        String line = verticalLineElement;
        for (String string : strings) {
            line += string;
            line += space + (8 - playState.getConquest(playState.getPlayersTowerColors().get(string)));
            line += space + playState.getPlayersTowerColors().get(string);
            line += verticalLineElement;
        }
        List<String> rows = new ArrayList<>();
        rows.add(line);
        rows.add(0, rowDivider(line.length()));
        rows.add(rows.get(0));
        line = "";
        for (String row : rows) {
            line += row + '\n';
        }
        return line;
    }

    /**
     * Prints who the actual player is and in which phase is the round (if Planning or action)
     *
     * @return the message
     */
    private String roundState() {
        String line = "Now is " + playState.getActualPlayer() + "'s ";
        if (playState.isActionPhase()) {
            return line + "Action phase";
        } else {
            return line + "Planning phase";
        }
    }

    /**
     * Prints the row divider of a specific size.
     *
     * @param size the length of the desired row.
     * @return the row.
     */
    private static String rowDivider(int size) {
        String head = "";
        for (int i = 0; i < size; i++) {
            head += horizontalLineElement;
        }
        return head;
    }

    /**
     * Creates the state of the clouds.
     *
     * @return the table.
     */
    private String cloudTable() {
        List<String> rows = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int forLambda = i;
            Optional<String> actualId = playState
                    .getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^c[0-9_]*_" + forLambda + "$"))
                    .findFirst();

            actualId.ifPresent(id -> {
                String row = verticalLineElement + id + space;
                row = studentPrinter(row, id);
                row += verticalLineElement;
                rows.add(row);
            });
        }
        rows.add(0, verticalLineElement + "ID " + space +
                colorHeading() + EscapeCli.DEFAULT +
                verticalLineElement);
        rows.add(0, rowDivider(39));
        rows.add(rows.size(), rows.get(0));
        String result = empty;
        for (String row : rows) {
            result += row + "\n";
        }
        return result;
    }

    /**
     * Prints the part of the table which contains the list of how many student are in a place.
     *
     * @param row where to append the student state.
     * @param id  the place whom print the info.
     * @return the changed row.
     */
    private String studentPrinter(String row, String id) {
        for (TeacherColor color : colorOrder) {
            row += EscapeCli.DEFAULT + verticalLineElement + EscapeCli.valueOf(color.toString()) + (
                    playState.getStudentsInPlaces().get(id).get(color) != 0 ?
                            playState.getStudentsInPlaces().get(id).get(color) :
                            space);
            int spacesLeft = color.toString().length() -
                    playState.getStudentsInPlaces().get(id).get(color).toString().length();
            row = spacer(row, spacesLeft);
        }
        return row + EscapeCli.DEFAULT;
    }

    /**
     * Creates the table of the state of the islands.
     *
     * @return the table.
     */
    private String islandTable() {
        List<String> rows = new ArrayList<>();
        idMaxWidth = playState.getStudentsInPlaces().keySet().stream()
                .filter(x -> x.matches("^i_[0-9_]*&"))
                .map(String::length)
                .max(Integer::compareTo)
                .orElse(8);

        for (int i = 1; i <= 12; i++) {
            int forLambda = i;
            Optional<String> actualId = playState
                    .getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^i[0-9_]*_" + forLambda + "$"))
                    .findFirst();

            actualId.ifPresent(id -> {
                String row = verticalLineElement + id;
                row = spacer(row, idMaxWidth - id.length());
                row += verticalLineElement + playState.getSize(id);
                row = studentPrinter(row, id);
                row += verticalLineElement + playState.getConquest(id).orElse(space);
                row = spacer(row, "TOWER".length() - playState.getConquest(id).orElse(space).length());
                row += verticalLineElement + (playState.getMotherNature().equals(id) ? yes : space);
                row = spacer(row, "MOTHER NATURE".length() - (playState.getMotherNature().equals(id) ? yes : space).length());
                row += verticalLineElement;
                rows.add(row);
            });
        }
        rows.add(0, verticalLineElement + "ID" + spacer("", idMaxWidth - "ID".length()) +
                verticalLineElement + "#" +
                colorHeading() + EscapeCli.DEFAULT +
                verticalLineElement + "TOWER" +
                verticalLineElement + "MOTHER NATURE" +
                verticalLineElement);
        rows.add(0, rowDivider(68));
        rows.add(rows.size(), rows.get(0));
        String result = empty;
        for (String row : rows) {
            result += row + "\n";
        }
        return result;
    }

    /**
     * Creates the table of the state of the personal Dashboard.
     *
     * @return the table.
     */
    private String printDashboard() {
        List<String> rows = new ArrayList<>();
        String row = verticalLineElement + "Entrance ";
        row = studentPrinter(row, "Entrance");
        row += verticalLineElement + (8 - playState.getMyConquests());
        rows.add(row);
        row = verticalLineElement + "Room     ";
        row = studentPrinter(row, "Room");
        row += verticalLineElement;
        rows.add(row);
        row = verticalLineElement + "Teachers ";
        for (TeacherColor color : colorOrder) {
            int oldSize = row.length();
            row += verticalLineElement + (playState.getTeachers().contains(color) ? yes : empty);
            int spacesLeft = color.toString().length() -
                    (playState.getTeachers().contains(color) ? yes : empty).length();
            row = spacer(row, spacesLeft);
        }
        row += verticalLineElement;
        rows.add(row);
        rows.add(0, verticalLineElement + "DASHBOARD" +
                colorHeading() + EscapeCli.DEFAULT +
                verticalLineElement + "TOWERS");
        rows.add(0, rowDivider(51));
        rows.add(rows.size(), rows.get(0));
        String result = empty;
        for (String rowa : rows) {
            result += rowa + "\n";
        }
        return result;
    }

    /**
     * In order to preserve the correct verticality of the colum dividers, spaces are needed between data and the
     * vertical line element, this method generates that.
     *
     * @param string the line to where to add spaces.
     * @param length the number if the desired spaces.
     * @return the line with spaces.
     */
    private String spacer(String string, int length) {
        for (int i = 0; i < length; i++) {
            string += space;
        }
        return string;
    }

    /**
     * Creates the part of the table headings where colors are listed.
     *
     * @return the generated heading part.
     */
    private String colorHeading() {
        String heading = "";
        for (TeacherColor color : colorOrder) {
            heading += verticalLineElement + EscapeCli.valueOf(color.toString()) + color.toString() + EscapeCli.DEFAULT;
        }
        return heading;
    }

    /**
     * Creates the table which shows which are the actual assistant cards.
     *
     * @return the table.
     */
    private String assistantCards() {
        List<String> strings = playState.getActiveAssistantCards().keySet().stream().toList();
        String line = verticalLineElement + "Actual Assistant Cards " + verticalLineElement;
        for (String string : strings) {
            line += string;
            line += space + playState.getActiveAssistantCards().get(string);
            line += verticalLineElement;
        }
        List<String> rows = new ArrayList<>();
        rows.add(line);
        rows.add(0, rowDivider(line.length()));
        rows.add(rows.get(0));
        line = "";
        for (String row : rows) {
            line += row + '\n';
        }
        return line;
    }

    /**
     * Shows what wizard does the player have.
     *
     * @return the wizard if present, null if not.
     */
    public String wizard() {
        if (playState.getWizard() != null) {
            return playState.getWizard().toString();
        } else {
            return null;
        }
    }
}
