package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.client.observer.ViewObserver;
import it.polimi.ingsw.client.view.gui.SceneController;
import it.polimi.ingsw.commons.enums.Wizard;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the controller of the scene where users pick their own color.
 */
public class WizardSceneController extends ViewObservable implements GenericSceneController {

    private List<Wizard> availableWizard;

    @FXML
    private ImageView magicianWizard;
    @FXML
    private ImageView kingWizard;
    @FXML
    private ImageView fairyWizard;
    @FXML
    private ImageView bambooGuyWizard;

    /**
     * Default constructor.
     */
    public WizardSceneController() {
        this.availableWizard = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        magicianWizard.setDisable(!availableWizard.contains(Wizard.MAGICIAN));
        kingWizard.setDisable(!availableWizard.contains(Wizard.KING));
        fairyWizard.setDisable(!availableWizard.contains(Wizard.FAIRY));
        bambooGuyWizard.setDisable(!availableWizard.contains(Wizard.BAMBOO_GUY));

        magicianWizard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWizardClick(Wizard.MAGICIAN));
        kingWizard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWizardClick(Wizard.KING));
        fairyWizard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWizardClick(Wizard.FAIRY));
        bambooGuyWizard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWizardClick(Wizard.BAMBOO_GUY));

    }

    /**
     * Handle the click on the worker.
     *
     * @param wizard wizard picked by user.
     */
    private void onWizardClick(Wizard wizard) {
        magicianWizard.setDisable(true);
        kingWizard.setDisable(true);
        fairyWizard.setDisable(true);
        bambooGuyWizard.setDisable(true);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateWizard(wizard))).start();
    }

    /**
     * Set the wizard which are pickable by user.
     *
     * @param availableWizard available colors.
     */
    public void setAvailableWizard(List<Wizard> availableWizard) {
        this.availableWizard = availableWizard;
    }

    @Override
    public void onConfirm(String what) {

    }
}
