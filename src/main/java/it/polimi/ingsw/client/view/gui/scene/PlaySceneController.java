package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.model.Commands;
import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.client.view.gui.SceneController;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.apache.commons.collections4.list.UnmodifiableList;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This class implements the Play view. Exiting commands are sent in written form to the {@link Commands} class
 */
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

    private Commands commandSender;
    private String actualCommand;

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
    @FXML
    private Label semaphoreYes;
    @FXML
    private Label semaphoreNo;
    @FXML
    private Label actualCommandPrinter;
    @FXML
    private GridPane expertThings;


    public PlaySceneController() {
        actualCommand = "";
    }

    @FXML
    public void initialize() {
        buildContainer();
        buildIslands();
        buildClouds();
        buildAssistant();
        buildDashboard();
        buildCommands();
        if (state.isExpert()) buildExpertThings();
        update();
    }

    /**
     * On update info
     */
    private void update() {
        setSemaphores();
        updateDashboard();
        updateClouds();
        updateIslands();
        updateAssistant();
        if (state.isExpert()) updateExpertThings();
    }

    /**
     * Set visible thing in my turn
     */
    private void setSemaphores() {
        semaphoreYes.setVisible(state.myTurn());
        semaphoreNo.setVisible(!state.myTurn());
    }

    /**
     * Build containers
     */
    private void buildContainer() {
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(75);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        container.getColumnConstraints().removeAll();

    }

    /**
     * Take one piece at time of commands
     */
    private void buildCommands() {
        Label actual = new Label("SM");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 0);
        actual.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> commandAppender(event, "sm"));
        actual.getStyleClass().add("clickable");
        commands.getChildren().add(actual);

        actual = new Label("MNM");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 1);
        actual.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> commandAppender(event, "mnm"));
        actual.getStyleClass().add("clickable");
        commands.getChildren().add(actual);

        actual = new Label("IN");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 2);
        actual.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> commandAppender(event, "in"));
        actual.getStyleClass().add("clickable");
        commands.getChildren().add(actual);

        actual = new Label("CH");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 3);
        actual.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> commandAppender(event, "ch"));
        actual.getStyleClass().add("clickable");
        commands.getChildren().add(actual);

        actual = new Label("CA");
        actual.getStyleClass().add("command");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 4);
        actual.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> commandAppender(event, "ca"));
        actual.getStyleClass().add("clickable");
        commands.getChildren().add(actual);

        int i = 0;
        for (String color : dashboardRoomOrder) {
            actual = new Label();
            actual.getStyleClass().add("command");
            actual.getStyleClass().add(color);
            GridPane.setRowIndex(actual, 1);
            GridPane.setColumnIndex(actual, i);
            actual.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> commandAppender(event, color));
            actual.getStyleClass().add("clickable");
            commands.getChildren().add(actual);
            i++;
        }

        actual = new Label("SUB");
        actual.getStyleClass().add("command");
        actual.getStyleClass().add("submit");
        GridPane.setRowIndex(actual, 0);
        GridPane.setColumnIndex(actual, 5);
        actual.addEventHandler(MouseEvent.MOUSE_CLICKED, this::sendCommand);
        actual.getStyleClass().add("clickable");
        commands.getChildren().add(actual);

        actual = new Label("DEL");
        actual.getStyleClass().add("command");
        actual.getStyleClass().add("delete");
        GridPane.setRowIndex(actual, 1);
        GridPane.setColumnIndex(actual, 5);
        actual.addEventHandler(MouseEvent.MOUSE_CLICKED, this::deleteCommand);
        actual.getStyleClass().add("clickable");
        commands.getChildren().add(actual);

    }

    /**
     * Build clouds
     */
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
            cloud.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> idGetter(event, cloud));
            cloud.getStyleClass().add("clickable");
            cloudss.add(cloud);
        }
        clouds.getChildren().addAll(cloudss);
    }

    /**
     * Build assistant cards
     */
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
            int finalI = i + 1;
            actual.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> commandAppender(event, "as " + finalI));
            actual.getStyleClass().add("clickable");
            assistantt.add(actual);
        }
        assistant.getChildren().addAll(assistantt);
    }

    /**
     * Build entrance
     *
     * @return a grid with entrance
     */
    private GridPane buildEntrance() {
        entrance.setVisible(false);
        entrance.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> commandAppender(event, "Entrance"));
        entrance.getStyleClass().add("clickable");
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

    /**
     * Build teachers
     *
     * @return a grid with teachers
     */
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

    /**
     * Build towers
     *
     * @return a grid with towers
     */
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

    /**
     * Build rooms
     *
     * @return a grid with rooms
     */
    private GridPane buildRoom() {
        room.setVisible(false);
        room.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> commandAppender(event, "Room"));
        room.getStyleClass().add("clickable");
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

    /**
     * Build dashboard, building all components
     *
     * @return a grid with dashboard's components
     */
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

    /**
     * Build islands grid
     */
    private void buildIslands() {
        islandGrid.setVisible(false);
        List<GridPane> islands = new ArrayList<>();
        List<String> islandStyles = List.of(new String[]{"island1", "island2", "island3"});
        List<String> islandContents = List.of(new String[]{
                TowerColor.BLACK.toString(),
                TowerColor.GREY.toString(),
                TowerColor.WHITE.toString(),
                "MOTHER",
                "NO_ENTRY",
                TeacherColor.YELLOW.toString(),
                TeacherColor.BLUE.toString(),
                TeacherColor.GREEN.toString(),
                TeacherColor.RED.toString(),
                TeacherColor.PINK.toString()});

        for (int i = 0; i < 12; i++) {
            GridPane island = new GridPane();
            island.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> islandSelector(event, island));
            island.getStyleClass().add("clickable");
            int j = 0;
            for (String content : islandContents) {
                if (Arrays.stream(TowerColor.values()).noneMatch(x -> x.toString().equals(content))) {
                    j++;
                }
                Label actual = new Label();
                actual.getStyleClass().add("wood");
                actual.getStyleClass().add(content);
                GridPane.setRowIndex(actual, j % 3);
                GridPane.setColumnIndex(actual, j / 3);
                if (state.isExpert()) {
                    island.getChildren().add(actual);
                } else {
                    if (!content.equals("NO_ENTRY")) {
                        island.getChildren().add(actual);
                    }
                }
            }
            GridPane.setColumnIndex(island, i % 6);
            GridPane.setRowIndex(island, (i / 6));
            island.getStyleClass().add("island");
            island.getStyleClass().add(islandStyles.get(i % 3));
            islands.add(island);
        }

        islandGrid.getChildren().addAll(islands);
    }

    /**
     * Build part of the interface must be show in expert mode
     */
    private void buildExpertThings() {
        GridPane characterCardRep = new GridPane();
        characterCardRep.setId("characterCardView");
        int i = 0;
        for (Characters characters : state.getAvailableCharacters()) {
            GridPane card = new GridPane();
            GridPane.setColumnIndex(card, i);
            card.setId(characters.toString());
            card.getStyleClass().add(characters.toString());
            card.getStyleClass().add("character");
            card.getStyleClass().add("clickable");
            ColumnConstraints third = new ColumnConstraints();
            third.setPercentWidth(100 / 3.0);
            card.getColumnConstraints().setAll(third, third, third);
            RowConstraints sixth = new RowConstraints();
            sixth.setPercentHeight(100 / 7.0);
            card.getRowConstraints().setAll(sixth, sixth, sixth, sixth, sixth, sixth, sixth);
            Label coins = new Label();
            coins.setText(state.getCharacterCosts().get(characters).toString());
            coins.getStyleClass().add("coin");
            card.getChildren().add(coins);
            Map<TeacherColor, Integer> map = state.getStudentsInPlaces().getOrDefault(characters.toString(), new HashMap<>());
            if (!map.isEmpty()) {
                int howManyStudents = map.values().stream().reduce(Integer::sum).orElse(0);
                for (int j = 0; j < howManyStudents; j++) {
                    Label student = new Label();
                    student.getStyleClass().add("wood");
                    GridPane.setColumnIndex(student, j % 3);
                    GridPane.setRowIndex(student, 4 + (j / 3));
                    card.getChildren().add(student);
                }
            }
            if (characters.equals(Characters.SORCERESS)) {
                Label noEntry = new Label();
                noEntry.getStyleClass().add("wood");
                noEntry.getStyleClass().add("NO_ENTRY");
                GridPane.setColumnIndex(noEntry, 1);
                GridPane.setRowIndex(noEntry, 4);
                card.getChildren().add(noEntry);
                System.out.println("Done");
            }
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> idGetter(event, card));
            characterCardRep.getChildren().add(card);
            i++;
        }
        expertThings.getChildren().add(characterCardRep);

        Label myCoins = new Label();
        myCoins.setText("" + state.getMyMoney());
        myCoins.getStyleClass().add("coin");
        myCoins.setId("myCoins");
        GridPane.setRowIndex(myCoins, 1);
        expertThings.getChildren().add(myCoins);
    }

    /**
     * Update island info
     */
    private void updateIslands() {
        islandGrid.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : islandGrid.getChildren()) {
            child.setVisible(false);
            child.setId(null);
        }

        for (int i = 1; i <= 12; i++) {

            int forLambda = i;

            Optional<String> actualId = state.getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^i[0-9_]*_" + forLambda + "$"))
                    .findFirst();

            if (actualId.isPresent()) {
                String id = actualId.get();
                GridPane islandRep = (GridPane) islandGrid.getChildren().get(actualChildNumber);
                islandRep.setId(id);
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
                if (state.isExpert()) {
                    Label noEntryRep = (Label) getNodeByStyleClass(islandRep.getChildren(), "NO_ENTRY");
                    noEntryRep.setVisible(state.getNoEntryIslands().contains(id));
                }
                islandRep.setVisible(true);
                actualChildNumber++;
            }
        }
    }

    /**
     * Update dashboard's components info
     */
    private void updateDashboard() {
        dashboard.setVisible(true);
        updateTeachers();
        updateTowers();
        updateRoom();
        updateEntrance();
    }

    /**
     * Update entrance info
     */
    private void updateEntrance() {
        entrance.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : entrance.getChildren()) child.setVisible(false);

        for (Node student : entrance.getChildren()) {
            student.getStyleClass().removeAll(dashboardRoomOrder);
        }

        Map<TeacherColor, Integer> entranceMap = new HashMap<>(state.getStudentsInPlaces().get("Entrance"));
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

    /**
     * Update rooms info
     */
    private void updateRoom() {
        room.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : room.getChildren()) {
            child.setVisible(false);
            for (Node nephew : ((GridPane) child).getChildren()) nephew.setVisible(false);
        }

        Map<TeacherColor, Integer> roomRep = new HashMap<>(state.getStudentsInPlaces().get("Room"));
        for (String color : dashboardRoomOrder) {
            GridPane colorsRoomRep = (GridPane) room.getChildren().get(actualChildNumber);
            for (int i = 0; i < roomRep.get(TeacherColor.valueOf(color)); i++) {
                Node studentInRoom = colorsRoomRep.getChildren().get(i);
                studentInRoom.setVisible(true);
            }
            colorsRoomRep.setVisible(true);
            actualChildNumber++;
        }
    }

    /**
     * Update towers info
     */
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

    /**
     * Update teachers info
     */
    private void updateTeachers() {
        teachers.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : teachers.getChildren()) child.setVisible(false);
        for (String color : dashboardRoomOrder) {
            Node child = teachers.getChildren().get(actualChildNumber);
            if (state.getTeachers().contains(TeacherColor.valueOf(color))) {
                child.setVisible(true);
            }
            actualChildNumber++;
        }
    }

    /**
     * Update clouds info
     */
    private void updateClouds() {
        clouds.setVisible(true);
        int actualChildNumber = 0;
        for (Node child : clouds.getChildren()) {
            child.setVisible(false);
            child.setId("");
        }

        for (int i = 1; i <= 3; i++) {
            int forLambda = i;
            Optional<String> actualId = state.getStudentsInPlaces().keySet().stream()
                    .filter(x -> x.matches("^c_" + forLambda + "$"))
                    .findFirst();
            if (actualId.isPresent()) {
                int cloudChildNumber = 0;
                String idc = actualId.get();
                Map<TeacherColor, Integer> cloudStudentRep = new HashMap<>(state.getStudentsInPlaces().get(idc));
                GridPane cloudRep = (GridPane) clouds.getChildren().get(actualChildNumber);
                for (TeacherColor color : TeacherColor.values()) {
                    while (cloudStudentRep.get(color) > 0) {
                        Node studRep = cloudRep.getChildren().get(cloudChildNumber);
                        studRep.getStyleClass().removeAll(dashboardRoomOrder);
                        studRep.getStyleClass().add(color.toString());
                        cloudStudentRep.replace(color, cloudStudentRep.get(color), cloudStudentRep.get(color) - 1);
                        cloudChildNumber++;
                        studRep.setVisible(true);
                    }
                }
                cloudRep.setId(idc);
                cloudRep.setVisible(true);
                actualChildNumber++;
            }
        }
    }

    /**
     * Update assistant cards info
     */
    private void updateAssistant() {
        int actualChildNumber = 0;
        assistant.setVisible(true);
        for (int j = 0; j < 10; j++) {
            Node assistantRep = assistant.getChildren().get(actualChildNumber);
            assistantRep.setVisible(false);
        }
        for (int i = 1; i <= 10; i++) {
            Node assistantRep = assistant.getChildren().get(actualChildNumber);
            if (state.getAssistantCards().contains(i)) {
                assistantRep.setVisible(true);
            } else {
                assistantRep.getStyleClass().remove("a" + (i));
                assistantRep.getStyleClass().add(state.getWizard().get().toString());
            }
            actualChildNumber++;
        }
    }

    /**
     * Update game mode info
     */
    private void updateExpertThings() {
        updateCharacterCards();
        updateMyCoins();
    }

    /**
     * Update coins info
     */
    private void updateMyCoins() {
        Label myCoins = (Label) expertThings.getChildren().stream().filter(x -> x.getId().equals("myCoins")).findFirst().get();
        myCoins.setText(state.getMyMoney() + "");
    }

    /**
     * Update character cards info
     */
    private void updateCharacterCards() {
        GridPane characterCards = (GridPane) expertThings.getChildren().stream().filter(x -> x.getId().equals("characterCardView")).findFirst().get();

        for (Node cardA : characterCards.getChildren()) {
            GridPane card = (GridPane) cardA;
            int actualChildNumber = 0;
            Label money = (Label) card.getChildren().get(actualChildNumber);
            actualChildNumber++;
            money.setText("" + state.getCharacterCosts().get(Characters.valueOf(card.getId())));
            Map<TeacherColor, Integer> cardStudents = new HashMap<>(state.getStudentsInPlaces().getOrDefault(card.getId(), new HashMap<>()));
            if (!cardStudents.isEmpty()) {
                for (TeacherColor color : TeacherColor.values()) {
                    while (cardStudents.get(color) > 0) {
                        Node studRep = card.getChildren().get(actualChildNumber);
                        studRep.getStyleClass().removeAll(Arrays.stream(TeacherColor.values()).map(Enum::toString).toList());
                        studRep.getStyleClass().add(color.toString());
                        cardStudents.replace(color, cardStudents.get(color), cardStudents.get(color) - 1);
                        actualChildNumber++;
                        studRep.setVisible(true);
                    }
                }
            }
            if (card.getId().equals("SORCERESS")) {
                Label noEntry = (Label) getNodeByStyleClass(card.getChildren(), "NO_ENTRY");
                noEntry.setText("" + state.getAvailableNoEntry());
            }
        }
    }

    /**
     * Set state
     *
     * @param state state
     */
    public void addGameState(PlayState state) {
        this.state = state;
    }

    /**
     * Set command sender
     *
     * @param commands command sender
     */
    public void addCommandSender(Commands commands) {
        this.commandSender = commands;
    }

    /**
     * Append commands
     *
     * @param event    event
     * @param argument actual command
     */
    private void commandAppender(Event event, String argument) {
        Node target = (Node) event.getTarget();
        target.getStyleClass().add("clicked");
        actualCommand += argument + " ";
        actualCommandPrinter.setText(actualCommand);
    }

    /**
     * Send command
     *
     * @param event event
     */
    private void sendCommand(Event event) {
        try {
            commandSender.receivedCommand(actualCommand);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            Platform.runLater(() -> {
                SceneController.showAlert("Error - ", "I did not understand, Please repeat");
            });
        }
        deleteCommand(event);
    }

    private void deleteCommand(Event event){
        actualCommand = "";
        actualCommandPrinter.setText(actualCommand);
        removeAllClicked(container.getChildren());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfirm(String what) {
        if (what.equals("update")) {
            update();
        }
    }

    /**
     * Get node using css style class
     *
     * @param where   list of nodes
     * @param classId style class
     * @return node
     */
    private Node getNodeByStyleClass(List<Node> where, String classId) {
        return where.stream()
                .filter(x -> x.getStyleClass().contains(classId))
                .findFirst()
                .get();
    }

    /**
     * Remove clicked children
     *
     * @param nodes clicked
     */
    private void removeAllClicked(List<Node> nodes) {
        List<Node> children = new UnmodifiableList<>(nodes);
        for (Node node : children) {
            if (node.getClass().equals(GridPane.class))
                removeAllClicked(((GridPane) node).getChildren());
            node.getStyleClass().remove("clicked");
        }
    }

    /**
     * Given an island if actual command is move mother nature computes the number of steps
     *
     * @param event  click event
     * @param island selected island
     */
    private void islandSelector(Event event, GridPane island) {
        String id = island.getId();
        if (!actualCommand.equals("mnm ")) {
            commandAppender(event, id);
            return;
        }
        Node motherNature = islandGrid.getChildren().stream()
                .filter(x -> x.getId().equals(state.getMotherNature()))
                .findFirst()
                .orElseThrow();
        int motherNatureIndex = islandGrid.getChildren().indexOf(motherNature);
        Node arrive = islandGrid.getChildren().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .orElseThrow();
        int arriveIndex = islandGrid.getChildren().indexOf(arrive);
        int hops;
        if (arriveIndex < motherNatureIndex) {
            hops = state.getNumOfIslands() - motherNatureIndex + arriveIndex;
        } else {
            hops = arriveIndex - motherNatureIndex;
        }
        commandAppender(event, hops + "");
    }

    /**
     * Node getter
     *
     * @param event event
     * @param node  node
     */
    private void idGetter(Event event, Node node) {
        commandAppender(event, node.getId());
    }
}
