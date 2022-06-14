package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.swing.text.Position;
import javax.swing.text.html.ImageView;
import java.util.List;
import java.util.Optional;

public class PlaySceneController extends ViewObservable implements GenericSceneController {

    private PlayState state;

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
        update();

    }

    private void update() {
        int actualChildNumber = 0;
        int actualChildNumber1 = 0;
        islandGrid.setVisible(true);
        clouds.setVisible(true);
        dashboard.setVisible(true);
        entrance.setVisible(true);
        teachers.setVisible(true);
        towers.setVisible(true);

        for (int j = 0; j < 2; j++) {
            Node islandRep = islandGrid.getChildren().get(actualChildNumber);
            islandRep.setVisible(false);
        }
        for(int j = 0; j < 3; j++){
            Node cloudRep = clouds.getChildren().get(actualChildNumber1);
            cloudRep.setVisible(false);
        }

        for(int i = 1; i <= 3; i++){
            Optional<String> actualId = state.getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^c[0-9]"))
                    .findFirst();
            if(actualId.isPresent()){
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

        for (int i = 1; i <= 2; i++) {

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
                        towerRep.setAccessibleText(state.getSize(id));
                        towerRep.setVisible(true);
                    }
                }
                Label motherRep = (Label) getNodeByStyleClass(islandRep.getChildren(), "MOTHER");
                if (id.equals(state.getMotherNature())) {
                    motherRep.setVisible(true);
                }
                else{
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
        if(what.equals("update")){
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
