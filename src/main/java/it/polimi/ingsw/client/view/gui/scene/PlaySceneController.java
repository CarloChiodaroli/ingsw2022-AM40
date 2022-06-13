package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.table.Island;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.swing.text.Position;
import javax.swing.text.html.ImageView;
import java.util.List;
import java.util.Optional;

public class PlaySceneController extends ViewObservable implements GenericSceneController{

    private PlayState state;

    @FXML
    private GridPane islandGrid;

    @FXML
    private GridPane dashboard;



    public PlaySceneController(){
    }

    @FXML
    public void initialize() {
        initIsland();

    }

    private void initIsland(){
        int actualChildNumber = 0;

        for(int j = 0; j < 12; j++){
            Node islandRep = islandGrid.getChildren().get(actualChildNumber);
            islandRep.setVisible(false);
        }

        for(int i = 1; i <= 12; i++){

            int forLambda = i;

            Optional<String> actualId = state.getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^i[0-9_]*_" + forLambda + "$"))
                    .findFirst();

            if(actualId.isPresent()){
                String id = actualId.get();
                GridPane islandRep = (GridPane) islandGrid.getChildren().get(actualChildNumber);
                for(TeacherColor color: TeacherColor.values()){
                    int num = state.getStudentsInPlaces().get(id).get(color);
                    Label colorRep = (Label) getNodeByStyleClass(islandRep.getChildren(), color.toString());
                    colorRep.setAccessibleText("" + num);
                }
                for(TowerColor color: TowerColor.values()){
                    Label towerRep = (Label) getNodeByStyleClass(islandRep.getChildren(), color.toString());
                    towerRep.setVisible(false);
                    Optional<String> towerColor = state.getConquest(id);
                    if(towerColor.isPresent() && towerColor.get().equals(color.toString())){
                        towerRep.setAccessibleText(state.getSize(id));
                        towerRep.setVisible(true);
                    }
                }
                if(id.equals(state.getMotherNature())) // mother nature rep visible
                    else // mother nature rep not visible

                islandRep.setVisible(true);
                actualChildNumber++;
            }
        }
    }

    public void addGameState(PlayState state){
        this.state = state;
    }

    @Override
    public void onConfirm(String what) {

    }

    private Node getNodeByStyleClass(List<Node> where, String classId){
        return where.stream()
                .filter(x -> x.getStyleClass().contains(classId))
                .findFirst()
                .get();
    }
}
