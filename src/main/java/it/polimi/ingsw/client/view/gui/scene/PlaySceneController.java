package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.MessageType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
    private GridPane towers;
    @FXML
    private GridPane assistant;
    @FXML
    private GridPane character;
    @FXML
    private GridPane room;


    public PlaySceneController() {
    }

    @FXML
    public void initialize() {
        buildIslands();
        buildClouds();
        buildAssistant();
        buildDashboard();
        update();
    }

    private void buildClouds(){
        clouds.setVisible(false);
        List<GridPane> cloudss = new ArrayList<>();
        List<String> cloudStyle = List.of(new String[]{"cloud1", "cloud2", "cloud3"});
        List<String> cloudContents = List.of(new String[]{
                TeacherColor.YELLOW.toString(),
                TeacherColor.BLUE.toString(),
                TeacherColor.GREEN.toString(),
                TeacherColor.RED.toString(),
                TeacherColor.PINK.toString()});

        for (int i = 0; i < 3; i++){
            GridPane cloud = new GridPane();
            for(int j = 0; j < 5; j++){
                Label actual = new Label();
                actual.getStyleClass().add(cloudContents.get(j));
                GridPane.setRowIndex(actual, j / 2);
                GridPane.setColumnIndex(actual, j % 2);
                cloud.getChildren().add(actual);
            }
            GridPane.setRowIndex(cloud, i / 2);
            GridPane.setColumnIndex(cloud, i % 2);
            cloud.getStyleClass().add("cloud");
            cloud.getStyleClass().add(cloudStyle.get(i % 3));
            cloudss.add(cloud);
        }
        clouds.getChildren().addAll(cloudss);
    }

    private void buildAssistant(){
        assistant.setVisible(false);
        List<Node> assistantt = new ArrayList<>();
        List<String> assistantStyle = List.of(new String[]{"a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10"});
        for(int i = 1; i <= 10; i++){
            Label actual = new Label();
            GridPane.setRowIndex(actual, 0);
            GridPane.setColumnIndex(actual, i);
            actual.getStyleClass().add("assistant");
            actual.getStyleClass().add(assistantStyle.get(i));
            assistantt.add(actual);
        }
        assistant.getChildren().addAll(assistantt);
    }

    private GridPane buildEntrance(){
        entrance.setVisible(false);
        List<Node> entrancee = new ArrayList<>();
        for(int i = 0; i < state.numStudEntrance(); i++){
            Label actual = new Label();
            GridPane.setColumnIndex(actual, i % 2);
            GridPane.setRowIndex(actual, i / 2);
            actual.getStyleClass().add("entrance");
            actual.getStyleClass().add(state.getStudentsInPlaces().get("Entrance").get(i).toString());
            entrancee.add(actual);
        }
        entrance.getChildren().addAll(entrancee);
        return entrance;
    }

    private GridPane buildTeachers(){
        teachers.setVisible(false);
        List<Node> teacherss = new ArrayList<>();
        List<String> teachersColor = List.of(new String[]{
                TeacherColor.GREEN.toString(),
                TeacherColor.RED.toString(),
                TeacherColor.YELLOW.toString(),
                TeacherColor.PINK.toString(),
                TeacherColor.BLUE.toString()});
        for(int i = 0; i < 5; i++){
            Label actual = new Label();
            GridPane.setColumnIndex(actual, 0);
            GridPane.setRowIndex(actual, i);
            actual.getStyleClass().add("teachers");
            actual.getStyleClass().add(teachersColor.get(i));
            teacherss.add(actual);
        }
        teachers.getChildren().addAll(teacherss);
        return teachers;
    }

    private GridPane buildTowers(){
        towers.setVisible(false);
        List<Node> towerss = new ArrayList<>();
        for(int i = 0; i < state.numTowers(""); i++){
            Label actual = new Label();
            GridPane.setColumnIndex(actual, i % 2);
            GridPane.setRowIndex(actual, i / 2);
            actual.getStyleClass().add("towers");
            actual.getStyleClass().add(state.getMyTowerColor().toString());
            towerss.add(actual);
        }
        towers.getChildren().addAll(towerss);
        return towers;
    }

    private GridPane buildRoom(){
        room.setVisible(false);
        List<GridPane> roomss = new ArrayList<>();
        List<String> roomColor = List.of(new String[]{
                TeacherColor.GREEN.toString(),
                TeacherColor.RED.toString(),
                TeacherColor.YELLOW.toString(),
                TeacherColor.PINK.toString(),
                TeacherColor.BLUE.toString()});
        for(int i = 0; i < 5; i++){
            GridPane rooms = new GridPane();
            for(int j = 0; j < 10; j++){
                Label actual = new Label();
                actual.getStyleClass().add(roomColor.get(i));
                GridPane.setRowIndex(actual, 0);
                GridPane.setColumnIndex(actual, j);
                rooms.getChildren().add(actual);
            }
            GridPane.setColumnIndex(rooms, 0);
            GridPane.setRowIndex(rooms, i);
            rooms.getStyleClass().add("room");
            rooms.getStyleClass().add(roomColor.get(i));
            roomss.add(rooms);
        }
        room.getChildren().addAll(roomss);
        return room;
    }

    private void buildDashboard(){
        dashboard.setVisible(false);
        List<Node> dashboardd = new ArrayList<>();
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

        dashboardd.add(entranceD);
        dashboardd.add(teachersD);
        dashboardd.add(towersD);
        dashboardd.add(roomD);
        dashboard.getChildren().addAll(dashboardd);
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

    private void update() {
        int actualChildNumber = 0;
        int actualChildNumber1 = 0;
        int actualChildNumber2 = 0;
        int actualChildNumber3 = 0;
        int actualChildNumber4 = 0;
        int actualChildNumber5 = 0;
        int actualChildNumber6 = 0;
        int actualChildNumber7 = 0;
        islandGrid.setVisible(true);
        clouds.setVisible(true);
        dashboard.setVisible(true);
        entrance.setVisible(true);
        teachers.setVisible(true);
        towers.setVisible(true);
        assistant.setVisible(true);
        character.setVisible(true);
        room.setVisible(true);

        for (int j = 0; j < 12; j++) {
            Node islandRep = islandGrid.getChildren().get(actualChildNumber);
            islandRep.setVisible(false);
        }
        for (int j = 0; j < 3; j++) {
            Node cloudRep = clouds.getChildren().get(actualChildNumber1);
            cloudRep.setVisible(false);
        }
        for (int j = 0; j < state.numInitialTowers(); j++) {
            Node towersRep = towers.getChildren().get(actualChildNumber2);
            towersRep.getStyleClass().add(state.getMyTowerColor().toString());
            towersRep.setVisible(false);
        }
        for(int j = 0; j < state.numStudEntrance(); j++){
            Node entranceRep = entrance.getChildren().get(actualChildNumber4);
            entranceRep.setVisible(false);
        }
        for(int j = 0; j < 10; j++){
            Node assistantRep = assistant.getChildren().get(actualChildNumber5);
            assistantRep.setVisible(false);
        }
        for(int j = 0; j < 3; j++){
            Node characterRep = character.getChildren().get(actualChildNumber6);
            characterRep.setVisible(false);
        }
        for(int j = 0; j < 5; j++){
            Node roomRep = room.getChildren().get(actualChildNumber7);
            roomRep.setVisible(false);
        }

        for (int i = 1; i <= 5; i++) {
            for (TeacherColor color : TeacherColor.values()) {
                Node teachersRep = teachers.getChildren().get(actualChildNumber3);
                if (state.getTeachers().contains(color)) {
                    teachersRep.setVisible(true);
                } else
                    teachersRep.setVisible(false);
                actualChildNumber3++;
            }
        }

        for(int i = 1; i <= 10; i++){
            Node assistantRep = assistant.getChildren().get(actualChildNumber5);
            if(state.getAssistantCards().contains(i)){
                assistantRep.setVisible(true);
            }
            actualChildNumber5++;
        }

        for (int i = 1; i <= state.numTowers(""); i++) {
            Node towersRep = towers.getChildren().get(actualChildNumber2);
            towersRep.setVisible(true);
            actualChildNumber2++;
        }

        for (int i = 1; i <= 3; i++) {
            Optional<String> actualId = state.getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^c[0-9]"))
                    .findFirst();
            if (actualId.isPresent()) {
                String idc = actualId.get();
                GridPane cloudRep = (GridPane) clouds.getChildren().get(actualChildNumber1);
                for (TeacherColor color : TeacherColor.values()) {
                    int num = state.getStudentsInPlaces().get(idc).get(color);
                    Label colorRep = (Label) getNodeByStyleClass(cloudRep.getChildren(), color.toString());
                    colorRep.setText("" + num);
                }
                cloudRep.setVisible(true);
                actualChildNumber1++;
            }
        }

        Map<TeacherColor, Integer> roomRep = new HashMap<>(state.getStudentsInPlaces().get("Room"));
        for(TeacherColor color: TeacherColor.values()){
            GridPane colorsRoomRep = (GridPane) room.getChildren().get(actualChildNumber7);
            for(int i = 1; i <= roomRep.get(color); i++){
                Node studentRoom = getNodeByStyleClass(colorsRoomRep.getChildren(), color.toString());
                studentRoom.setVisible(true);
            }
            colorsRoomRep.setVisible(true);
            actualChildNumber7++;
        }

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
        }

        for(Node student: entrance.getChildren()){
            student.getStyleClass().removeAll(islandContents);
        }

        Map<TeacherColor, Integer> entranceMap = new HashMap<>(state.getStudentsInPlaces().get("Entrance"));
        for(TeacherColor color : TeacherColor.values()){
            while(entranceMap.get(color) > 0){
                Node studEntranceRep = entrance.getChildren().get(actualChildNumber4);
                studEntranceRep.getStyleClass().add(color.toString());
                entranceMap.replace(color, entranceMap.get(color), entranceMap.get(color) - 1 );
                actualChildNumber4++;
            }
        }

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
