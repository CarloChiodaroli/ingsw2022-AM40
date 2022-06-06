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
    @FXML
    private Button backToMenuBtn;

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

        magicianWizard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWorkerClick(Wizard.MAGICIAN));
        kingWizard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWorkerClick(Wizard.KING));
        fairyWizard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWorkerClick(Wizard.FAIRY));
        bambooGuyWizard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWorkerClick(Wizard.BAMBOO_GUY));

        backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
    }

    /**
     * Handle the click on the worker.
     *
     * @param wizard wizard picked by user.
     */
    private void onWorkerClick(Wizard wizard) {
        magicianWizard.setDisable(true);
        kingWizard.setDisable(true);
        fairyWizard.setDisable(true);
        bambooGuyWizard.setDisable(true);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateWizard(wizard))).start();
    }

    /**
     * Handle the click on the back to menu button.
     */
    private void onBackToMenuBtnClick(Event event) {
        backToMenuBtn.setDisable(true);
        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

    /**
     * Set the wizard which are pickable by user.
     *
     * @param availableWizard available colors.
     */
    public void setAvailableWizard(List<Wizard> availableWizard) {
        this.availableWizard = availableWizard;
    }
}
