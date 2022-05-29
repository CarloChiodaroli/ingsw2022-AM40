package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.commons.enums.TeacherColor;

import java.util.*;

/**
 * Builds the view of the game state
 */
public class StatePrinter {

    private final static String horizontalLineElement = "=";
    private final static String verticalLineElement = "| ";
    private final static String space = " ";
    private final static String empty = ""; // default
    private final static String yes = "Yes";
    private final PlayState playState;
    private int idMaxWidth;
    private final List<TeacherColor> colorOrder;

    private String wholePrint;

    public StatePrinter(PlayState playState) {
        this.playState = playState;
        this.colorOrder = Arrays.stream(TeacherColor.values()).toList();
    }

    public String getState() throws InterruptedException {
        String state = "\n";
        state += islandTable();
        state += cloudTable();
        state += printDashboard();
        state += statusTowers();
        state += roundState();
        return state;
    }

    public String statusTowers(){
        List<String> strings = playState.getPlayersTowerColors().keySet().stream().toList();
        String line = verticalLineElement;
        for(String string: strings){
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
        for(String row: rows){
            line += row + '\n';
        }
        return line;
    }

    public String roundState(){
        String line = "Now is " + playState.getActualPlayer() + "'s ";
        if(playState.isActionPhase()){
            return line + "Action phase";
        } else {
            return line + "Planning phase";
        }
    }

    private static String rowDivider(int size) {
        String head = "";
        for (int i = 0; i < size; i++) {
            head += horizontalLineElement;
        }
        return head;
    }

    public String cloudTable(){
        List<String> rows = new ArrayList<>();
        for(int i = 1; i <= 3; i++){
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
                verticalLineElement + colorOrder.get(0).toString() +
                verticalLineElement + colorOrder.get(1).toString() +
                verticalLineElement + colorOrder.get(2).toString() +
                verticalLineElement + colorOrder.get(3).toString() +
                verticalLineElement + colorOrder.get(4).toString() +
                verticalLineElement);
        rows.add(0, rowDivider(rows.get(0).length()));
        rows.add(rows.size(), rows.get(0));
        String result = empty;
        for(String row: rows) {
            result += row + "\n";
        }
        return result;
    }

    public String studentPrinter(String row, String id){
        for(TeacherColor color: colorOrder){
            int oldSize = row.length();
            row += verticalLineElement + (
                    playState.getStudentsInPlaces().get(id).get(color) != 0?
                            playState.getStudentsInPlaces().get(id).get(color):
                            space);
            int spacesLeft = color.toString().length() + oldSize + verticalLineElement.length();
            row = spacer(spacesLeft, row);
        }
        return row;
    }

    public String islandTable(){
        List<String> rows = new ArrayList<>();
        idMaxWidth = playState.getStudentsInPlaces().keySet().stream()
                .filter(x -> x.matches("^i_[0-9_]*&"))
                .map(String::length)
                .max(Integer::compareTo)
                .orElse(8);

        for(int i = 1; i <= 12; i++){
            int forLambda = i;
            Optional<String> actualId = playState
                    .getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^i[0-9_]*_" + forLambda + "$"))
                    .findFirst();

            actualId.ifPresent(id -> {
                String row = verticalLineElement + id;
                row = spacer(idMaxWidth, row);
                row += verticalLineElement + playState.getSize(id);
                row = studentPrinter(row, id);
                row += verticalLineElement + playState.getConquest(id).orElse(space);
                row = spacer(row.length() + 4, row);
                row += verticalLineElement + (playState.getMotherNature().equals(id)? "X": space);
                row = spacer(row.length() + 12, row);
                row += verticalLineElement;
                rows.add(row);
            });
        }
        rows.add(0, verticalLineElement + "ID"+ spacer(idMaxWidth - 4, "") +
                verticalLineElement + "#" +
                verticalLineElement + colorOrder.get(0).toString() +
                verticalLineElement + colorOrder.get(1).toString() +
                verticalLineElement + colorOrder.get(2).toString() +
                verticalLineElement + colorOrder.get(3).toString() +
                verticalLineElement + colorOrder.get(4).toString() +
                verticalLineElement + "TOWER" +
                verticalLineElement + "MOTHER NATURE" +
                verticalLineElement);
        rows.add(0, rowDivider(rows.get(0).length()));
        rows.add(rows.size(), rows.get(0));
        String result = empty;
        for(String row: rows) {
            result += row + "\n";
        }
        return result;
    }

    public String printDashboard(){
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
        for(TeacherColor color: colorOrder){
            int oldSize = row.length();
            row += verticalLineElement + (playState.getTeachers().contains(color)? color.toString(): empty);
            int spacesLeft = color.toString().length() + oldSize + verticalLineElement.length();
            row = spacer(spacesLeft, row);
        }
        row += verticalLineElement;
        rows.add(row);
        rows.add(0, verticalLineElement + "DASHBOARD" +
                verticalLineElement + colorOrder.get(0).toString() +
                verticalLineElement + colorOrder.get(1).toString() +
                verticalLineElement + colorOrder.get(2).toString() +
                verticalLineElement + colorOrder.get(3).toString() +
                verticalLineElement + colorOrder.get(4).toString() +
                verticalLineElement + "TOWERS");
        rows.add(0, rowDivider(rows.get(0).length()));
        rows.add(rows.size(), rows.get(0));
        String result = empty;
        for(String rowa: rows) {
            result += rowa + "\n";
        }
        return result;
    }

    private String spacer(int length, String string){
        while(string.length() < length){
            string += space;
        }
        return string;
    }
}
