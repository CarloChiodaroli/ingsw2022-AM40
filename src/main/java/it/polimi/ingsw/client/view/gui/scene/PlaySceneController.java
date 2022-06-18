package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.*;

public class PlaySceneController extends ViewObservable implements GenericSceneController {

    private PlayState state;
    private final static List<String> islandContents = List.of(new String[]{
            TeacherColor.YELLOW.toString(),
            TeacherColor.BLUE.toString(),
            TeacherColor.GREEN.toString(),
            TeacherColor.RED.toString(),
            TeacherColor.PINK.toString(),
            TowerColor.BLACK.toString(),
            TowerColor.GREY.toString(),
            TowerColor.WHITE.toString(),
            "MOTHER"});
    private final static List<String> dashboardRoomOrder = List.of(new String[]{
            TeacherColor.GREEN.toString(),
            TeacherColor.RED.toString(),
            TeacherColor.YELLOW.toString(),
            TeacherColor.PINK.toString(),
            TeacherColor.BLUE.toString()});

    @FXML
    private GridPane container;

    @FXML
    private GridPane islandGrid;
    @FXML
    private GridPane clouds;
    @FXML
    private GridPane dashboard;
    @FXML
    private GridPane entrance;
    @FXML
    private GridPane teachers;
    @FXML
    private GridPane room;
    @FXML
    private GridPane towers;
    @FXML
    private GridPane assistant;
    @FXML
    private GridPane commands;
    /*@FXML
    private GridPane character;*/


    public PlaySceneController() {
    }

    @FXML
    public void initialize() {
        buildContainer();
        buildIslands();
        buildClouds();
        buildAssistant();
        buildDashboard();
        buildCommands();
        update();
    }

    private void update() {
        updateDashboard();
        updateClouds();
        updateIslands();
        updateAssistant();
    }

    private void buildContainer() {
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(75);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        container.getColumnConstraints().removeAll();

    }

    private void buildCommands(){
        Label actual = new Label("SM");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 0);
        commands.getChildren().add(actual);
        actual = new Label("MNM");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 1);
        commands.getChildren().add(actual);
        actual = new Label("IN");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 2);
        commands.getChildren().add(actual);
        actual = new Label("CH");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 3);
        commands.getChildren().add(actual);
        actual = new Label("CA");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 4);
        commands.getChildren().add(actual);

        int i = 0;
        for(String color: dashboardRoomOrder){
            actual = new Label("CA");
            actual.getStyleClass().add("command");
            actual.getStyleClass().add(color);
            GridPane.setRowIndex(actual, 1);
            GridPane.setColumnIndex(actual, i);
            commands.getChildren().add(actual);
            i++;
        }

        actual = new Label("SUB");
        actual.getStyleClass().add("command");
        actual.getStyleClass().add("submit");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 5);
        GridPane.setRowSpan(actual, 2);
        commands.getChildren().add(actual);

    }

    private void buildClouds() {
        clouds.setVisible(false);
        List<GridPane> cloudss = new ArrayList<>();
        List<String> cloudStyle = List.of(new String[]{"cloud1", "cloud2", "cloud3"});

        for (int i = 0; i < 3; i++) {
            GridPane cloud = new GridPane();
            for (int j = 0; j < state.getNumOfStudentsInCloud(); j++) {
                Label actual = new Label();
                actual.getStyleClass().add("wood");
                GridPane.setRowIndex(actual, j / 2);
                GridPane.setColumnIndex(actual, j % 2);
                cloud.getChildren().add(actual);
            }
            GridPane.setRowIndex(cloud, 0);
            GridPane.setColumnIndex(cloud, i);
            cloud.getStyleClass().add("cloud");
            cloud.getStyleClass().add(cloudStyle.get(i % 3));
            cloudss.add(cloud);
        }
        clouds.getChildren().addAll(cloudss);
    }

    private void buildAssistant() {
        assistant.setVisible(false);
        List<Node> assistantt = new ArrayList<>();
        List<String> assistantStyle = List.of(new String[]{"a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10"});
        for (int i = 0; i < 10; i++) {
            Label actual = new Label();
            GridPane.setRowIndex(actual, i / 5);
            GridPane.setColumnIndex(actual, i % 5);
            actual.getStyleClass().add("assistant");
            actual.getStyleClass().add(assistantStyle.get(i));
            assistantt.add(actual);
        }
        assistant.getChildren().addAll(assistantt);
    }

    private GridPane buildEntrance() {
        entrance.setVisible(false);
        List<Node> entrancee = new ArrayList<>();
        for (int i = 1; i <= state.numStudEntrance(); i++) {
            Label actual = new Label();
            GridPane.setColumnIndex(actual, i % 2);
            GridPane.setRowIndex(actual, i / 2);
            actual.getStyleClass().add("wood");
            actual.getStyleClass().add("student");
            actual.getStyleClass().add(TeacherColor.BLUE.toString());
            entrancee.add(actual);
        }
        entrance.getChildren().addAll(entrancee);
        return entrance;
    }

    private GridPane buildTeachers() {
        teachers.setVisible(false);
        List<Node> teacherss = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Label actual = new Label();
            GridPane.setColumnIndex(actual, 0);
            GridPane.setRowIndex(actual, i);
            actual.getStyleClass().add("wood");
            actual.getStyleClass().add("teachers");
            actual.getStyleClass().add(dashboardRoomOrder.get(i) + "_teacher");
            teacherss.add(actual);
        }
        teachers.getChildren().addAll(teacherss);
        return teachers;
    }

    private GridPane buildTowers() {
        towers.setVisible(false);
        List<Node> towerss = new ArrayList<>();
        for (int i = 0; i < state.numMyTowers(); i++) {
            Label actual = new Label();
            GridPane.setColumnIndex(actual, i % 2);
            GridPane.setRowIndex(actual, i / 2);
            actual.getStyleClass().add("wood");
            actual.getStyleClass().add("towers");
            actual.getStyleClass().add(state.getMyTowerColor().toString());
            towerss.add(actual);
        }
        towers.getChildren().addAll(towerss);
        return towers;
    }

    private GridPane buildRoom() {
        room.setVisible(false);
        List<GridPane> roomss = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GridPane table = new GridPane();
            for (int j = 0; j < 10; j++) {
                Label actual = new Label();
                actual.getStyleClass().add("wood");
                actual.getStyleClass().add(dashboardRoomOrder.get(i));
                GridPane.setRowIndex(actual, 0);
                GridPane.setColumnIndex(actual, j);
                table.getChildren().add(actual);
            }
            GridPane.setColumnIndex(table, 0);
            GridPane.setRowIndex(table, i);
            table.getStyleClass().add(dashboardRoomOrder.get(i));
            table.getStyleClass().add("table");
            roomss.add(table);
        }
        room.getChildren().addAll(roomss);
        return room;
    }

    private void buildDashboard() {
        dashboard.setVisible(false);
        GridPane entranceD = buildEntrance();
        GridPane teachersD = buildTeachers();
        GridPane towersD = buildTowers();
        GridPane roomD = buildRoom();

        GridPane.setRowIndex(entranceD, 0);
        GridPane.setRowIndex(teachersD, 0);
        GridPane.setRowIndex(towersD, 0);
        GridPane.setRowIndex(roomD, 0);

        GridPane.setColumnIndex(entranceD, 0);
        GridPane.setColumnIndex(roomD, 1);
        GridPane.setColumnIndex(teachersD, 2);
        GridPane.setColumnIndex(towersD, 3);
    }

    private void buildIslands() {
        islandGrid.setVisible(false);
        List<GridPane> islands = new ArrayList<>();
        List<String> islandStyles = List.of(new String[]{"island1", "island2", "island3"});
        List<String> islandContents = List.of(new String[]{
                TeacherColor.YELLOW.toString(),
                TeacherColor.BLUE.toString(),
                TeacherColor.GREEN.toString(),
                TeacherColor.RED.toString(),
                TeacherColor.PINK.toString(),
                TowerColor.BLACK.toString(),
                TowerColor.GREY.toString(),
                TowerColor.WHITE.toString(),
                "MOTHER"});

        for (int i = 0; i < 12; i++) {
            GridPane island = new GridPane();
            for (int j = 0; j < 9; j++) {
                Label actual = new Label();
                actual.getStyleClass().add("wood");
                actual.getStyleClass().add(islandContents.get(j));
                GridPane.setRowIndex(actual, j % 3);
                GridPane.setColumnIndex(actual, j / 3);
                island.getChildren().add(actual);
            }
            GridPane.setColumnIndex(island, i % 6);
            GridPane.setRowIndex(island, (i / 6));
            island.getStyleClass().add("island");
            island.getStyleClass().add(islandStyles.get(i % 3));
            islands.add(island);
        }

        islandGrid.getChildren().addAll(islands);
    }

    private void updateIslands() {
        islandGrid.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : islandGrid.getChildren()) child.setVisible(false);

        for (int i = 1; i <= 12; i++) {

            int forLambda = i;

            Optional<String> actualId = state.getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^i[0-9_]*_" + forLambda + "$"))
                    .findFirst();

            if (actualId.isPresent()) {
                String id = actualId.get();
                GridPane islandRep = (GridPane) islandGrid.getChildren().get(actualChildNumber);
                for (TeacherColor color : TeacherColor.values()) {
                    int num = state.getStudentsInPlaces().get(id).get(color);
                    Label colorRep = (Label) getNodeByStyleClass(islandRep.getChildren(), color.toString());
                    colorRep.setText("" + num);
                }
                for (TowerColor color : TowerColor.values()) {
                    Label towerRep = (Label) getNodeByStyleClass(islandRep.getChildren(), color.toString());
                    towerRep.setVisible(false);
                    Optional<String> towerColor = state.getConquest(id);
                    if (towerColor.isPresent() && towerColor.get().equals(color.toString())) {
                        towerRep.setText(state.getSize(id));
                        towerRep.setVisible(true);
                    }
                }
                Label motherRep = (Label) getNodeByStyleClass(islandRep.getChildren(), "MOTHER");
                if (id.equals(state.getMotherNature())) {
                    motherRep.setVisible(true);
                } else {
                    motherRep.setVisible(false);
                }
                islandRep.setVisible(true);
                actualChildNumber++;
            }
        }
    }

    private void updateDashboard() {
        dashboard.setVisible(true);
        updateTeachers();
        updateTowers();
        updateRoom();
        updateEntrance();
    }

    private void updateEntrance() {
        entrance.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : entrance.getChildren()) child.setVisible(false);

        for (Node student : entrance.getChildren()) {
            student.getStyleClass().removeAll(dashboardRoomOrder);
        }

        Map<TeacherColor, Integer> entranceMap = new HashMap<>(state.getStudentsInPlaces().get("Entrance"));
        System.out.println(entranceMap);
        for (TeacherColor color : TeacherColor.values()) {
            while (entranceMap.get(color) > 0) {
                Node studEntranceRep = entrance.getChildren().get(actualChildNumber);
                studEntranceRep.getStyleClass().add(color.toString());
                entranceMap.replace(color, entranceMap.get(color), entranceMap.get(color) - 1);
                actualChildNumber++;
                studEntranceRep.setVisible(true);
            }
        }
        entrance.setVisible(true);
    }

    private void updateRoom() {
        room.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : room.getChildren()) {
            child.setVisible(false);
            for (Node nephew : ((GridPane) child).getChildren()) nephew.setVisible(false);
        }

        Map<TeacherColor, Integer> roomRep = new HashMap<>(state.getStudentsInPlaces().get("Room"));
        for (TeacherColor color : TeacherColor.values()) {
            GridPane colorsRoomRep = (GridPane) room.getChildren().get(actualChildNumber);
            for (int i = 0; i < roomRep.get(color); i++) {
                Node studentInRoom = colorsRoomRep.getChildren().get(i);
                studentInRoom.setVisible(true);
            }
            colorsRoomRep.setVisible(true);
            actualChildNumber++;
        }
    }

    private void updateTowers() {
        towers.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : towers.getChildren()) child.setVisible(false);

        for (int i = 1; i <= state.numMyTowers(); i++) {
            Node towersRep = towers.getChildren().get(actualChildNumber);
            towersRep.setVisible(true);
            actualChildNumber++;
        }
    }

    private void updateTeachers() {
        teachers.setVisible(true);
        for (Node child : teachers.getChildren()) child.setVisible(false);
        Arrays.stream(TeacherColor.values())
                .forEach(color -> teachers.getChildren().stream()                               // for each teacher color
                        .filter(x -> x.getStyleClass().contains(color.toString() + "_teacher")) // get correspondent teacher representation
                        .peek(x -> x.setVisible(state.getTeachers().contains(color))));         // set it to visible if needed
    }

    private void updateClouds() {
        clouds.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : clouds.getChildren()) child.setVisible(false);

        for (int i = 1; i <= 3; i++) {
            int forLambda = i;
            Optional<String> actualId = state.getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^c_" + forLambda + "*"))
                    .findFirst();
            if (actualId.isPresent()) {
                int cloudChildNumber = 0;
                String idc = actualId.get();
                Map<TeacherColor, Integer> cloudStudentRep = new HashMap<>(state.getStudentsInPlaces().get(idc));
                GridPane cloudRep = (GridPane) clouds.getChildren().get(actualChildNumber);
                for (TeacherColor color : TeacherColor.values()) {
                    while (cloudStudentRep.get(color) > 0) {
                        Node studRep = cloudRep.getChildren().get(cloudChildNumber);
                        studRep.getStyleClass().add(color.toString());
                        cloudStudentRep.replace(color, cloudStudentRep.get(color), cloudStudentRep.get(color) - 1);
                        cloudChildNumber++;
                        studRep.setVisible(true);
                    }
                }
                cloudRep.setVisible(true);
                actualChildNumber++;
            }
        }
    }

    private void updateAssistant() {
        int actualChildNumber = 0;
        assistant.setVisible(true);
        for (int j = 0; j < 10; j++) {
            Node assistantRep = assistant.getChildren().get(actualChildNumber);
            assistantRep.setVisible(false);
        }
        for (int i = 0; i < 10; i++) {
            Node assistantRep = assistant.getChildren().get(actualChildNumber);
            if (state.getAssistantCards().contains(i)) {
                assistantRep.setVisible(true);
            } else {
                assistantRep.getStyleClass().remove("a" + i+1);
                assistantRep.getStyleClass().add(state.getWizard().get().toString());
            }
            actualChildNumber++;
        }
    }

    private void updateCharacters() {
        //character.setVisible(true);
        /*for(int j = 0; j < 3; j++){
            Node characterRep = character.getChildren().get(actualChildNumber6);
            characterRep.setVisible(false);
        for(int i = 1; i <= 3; i++){
            Optional<String> actualId = state.getAvailableCharacters().stream().findFirst().map(Enum::toString);
            if(actualId.isPresent()){
                GridPane characterRep = (GridPane) character.getChildren().get(actualChildNumber6);
                int num = state.getCharacterCosts().get(state.getAvailableCharacters().stream().findFirst().get());
                Label characterCost = (Label) getNodeByStyleClass(characterRep.getChildren(), "COIN");
                characterCost.setText("" + num);
                characterRep.setVisible(true);
                actualChildNumber6++;
            }
        }*/
    }


    public void addGameState(PlayState state) {
        this.state = state;
    }

    @Override
    public void onConfirm(String what) {
        if (what.equals("update")) {
            update();
        }
    }

    private Node getNodeByStyleClass(List<Node> where, String classId) {
        return where.stream()
                .filter(x -> x.getStyleClass().contains(classId))
                .findFirst()
                .get();
    }
}
