package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds the view of the game state
 */
public class StatePrinter {

    private final static String horizontalLineElement = "=";
    private final static String verticalLineElement = "\t|";
    private final static String space = " ";
    private final static String empty = ""; // default
    private final static String yes = "Yes";
    private final Map<String, Map<String, String>> baseMap;
    private final String firstRow;
    private final PlayState playState;
    private int idMaxWidth;
    private final List<TeacherColor> colorOrder;

    private String wholePrint;

    public StatePrinter(PlayState playState) {
        this.playState = playState;
        this.colorOrder = Arrays.stream(TeacherColor.values()).toList();
        this.firstRow = getFirstRow();
        this.baseMap = new HashMap<>();
    }

    public String getState(){
        return playState.toString();
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
        String head = horizontalLineElement;
        for (int i = 0; i <= size; i++) {
            head += horizontalLineElement;
        }
        return head + "\n";
    }

    private String getFirstRow() {
        String result = verticalLineElement + "ID" + verticalLineElement;
        for (TeacherColor color : colorOrder) {
            result += color.toString() + verticalLineElement;
        }
        return result + "\n";
    }

    private String studentsTable() {
        Map<String, Map<TeacherColor, Integer>> base = playState.getStudentsInPlaces();
        String firstRow = getFirstRow();
        String horizontalBar = rowDivider(firstRow.length());
        String completedTable = horizontalBar + firstRow + horizontalBar;
        for (Map.Entry<String, Map<TeacherColor, Integer>> row : base.entrySet()) {
            completedTable += verticalLineElement + row.getKey();
            for (TeacherColor color : colorOrder) {
                completedTable += verticalLineElement + row.getValue().get(color).toString();
            }
            completedTable += verticalLineElement + "\n";
        }
        completedTable += horizontalBar;
        return completedTable;
    }

    /*private String conquestsTable(){
        Map<String, TowerColor> base = playState.getConquests();

    }*/
}
