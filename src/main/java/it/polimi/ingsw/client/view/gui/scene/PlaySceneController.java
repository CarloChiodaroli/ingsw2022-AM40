package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
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


    public PlaySceneController() {
    }

    @FXML
    public void initialize() {
        buildIslands();
        update();

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
            island.getStyleClass().add(islandStyles.get(12 % 3));
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
        islandGrid.setVisible(true);
        clouds.setVisible(true);
        dashboard.setVisible(true);
        entrance.setVisible(true);
        teachers.setVisible(true);
        towers.setVisible(true);

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
            towersRep.setVisible(false);
        }
        for(int j = 0; j < state.numStudEntrance(); j++){
            Node entranceRep = entrance.getChildren().get(actualChildNumber4);
            entranceRep.setVisible(false);
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

        for (int i = 1; i <= state.numTowers(); i++) {
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
