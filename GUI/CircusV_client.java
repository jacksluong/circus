package GUI;

import common.Employee;
import Version1.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Jacks on 11/22/18. Completed 12/9/18 after 40 hours of dedication.
 * A GUI to the Circus program. V stands for Visual.
 */

public class CircusV_client extends Application {

    /*
     * Project Stages:
     * √ 1. Employee circles
     * √ 2. Checkbox options (tooltips, initials)
     * √ 3. Arrows & movement
     * √ 4. Sort buttons (first four buttons)
     * √ 5. Actions (fifth button with the menu)
     * √ 6. Feedback messages in the bottom left
     * √ 7. Nonessential formatting
     * √ 8. Reorganize code
     *
     *  Bugs:
     *  None
     *
     *  Add:
     *  None
     *
     *  (The data structures used are from version 1 of the Circus program.)
     */

    final StringProperty pathname = new SimpleStringProperty("");
    double deltaX = 0;
    double multiplier = 1;
    int currentCatIndex = 0; // indefinite, down arrow on button = ++
    int currentActionIndex = 11; // goes up to 5, descending (5 is closed menu; anything above 5 disables navigation)
    int currentButtonIndex = 0; // goes up to 4, rightward

    final Pane mainDisplay = new Pane();
    final FeedbackManager feedbackManager = new FeedbackManager(mainDisplay);
    
    /* Primary data structures */
    final NameTree names = new NameTree();
    final IdTree ids = new IdTree(new IdComparator());
    final Categories cats = new Categories();

    public void start(Stage primaryStage) {
        System.out.println("Welcome, welcome, welcome ladies and gentlemen and children of all ages! You may be familiar with the circus" +
                "\nin town, but you aren't ready for this one coming; it's a real butt-kicker, this one. It's got everything the" +
                "\nlast one's got, except you won't have to worry about squinting from the nosebleeds anymore. With this circus," +
                "\nyour eyes will be filled with all the JavaFX you want so everything is easier to enjoy!" +
                "\n" +
                "\nUse arrow keys to navigate the menu and Enter to select.");



        /*-------------------------------------------------------*/
        /**---- Initialization of Scene and Data Structures -----*/
        /*-------------------------------------------------------*/


        /**----- Panes -----*/

        // Create
        HBox mainMenu = new HBox(30);
        BorderPane whole = new BorderPane();
        PopupPrompt popup = new PopupPrompt(new String[]{""});
        popup.setTranslateX(260);
        popup.setTranslateY(20);
        // Format
        mainDisplay.setMinHeight(350);
        whole.setStyle("-fx-background-color: #F8D26F;");

            /*----- Backdrop -----*/
        Text backdrop = new Text(120, 200, "The Circus V");
        backdrop.setStyle("-fx-stroke: blue;" +
                "-fx-stroke-width: 7.5;" +
                "-fx-font-family: Bradley Hand;" +
                "-fx-font-size: 100;" +
                "-fx-opacity: 0.2;");
        mainDisplay.getChildren().add(backdrop);

            /*----- Mode selection -----*/
        // Create
        Rectangle shade = new Rectangle(-70, -50, 1000, 500);
        MRectangle zero = new MRectangle("Empty", 0.55),
                sparse = new MRectangle("Startup", 0.55),
                crowded = new MRectangle("Corporation", 0.55);
        MRectangle[] modeButtons = {zero, sparse, crowded};
        Text title1 = new Text("Circus Status");
        GridPane modes = new GridPane();
        StackPane textContainer1 = new StackPane(title1);
        modes.add(textContainer1, 0, 0, 3, 1);
        modes.add(zero, 0, 1);
        modes.add(sparse, 1, 1);
        modes.add(crowded, 2, 1);
        // Format
        shade.setOpacity(0.6);
        shade.setFill(Color.BLACK);
        GridPane.setHalignment(textContainer1, HPos.CENTER);
        GridPane.setHalignment(zero, HPos.LEFT);
        GridPane.setHalignment(sparse, HPos.CENTER);
        GridPane.setHalignment(crowded, HPos.RIGHT);
        modes.setHgap(10);
        modes.setTranslateX(278);
        modes.setTranslateY(130);
        modes.setPadding(new Insets(10));
        modes.setStyle("-fx-background-color: #404040");
        textContainer1.setPadding(new Insets(0,0,10,0));
        title1.setStroke(Color.WHITE);
        title1.setStrokeWidth(0.6);
        title1.setFill(Color.WHITE);
        title1.setFont(Font.font(15));
        mainDisplay.getChildren().addAll(shade, modes);


        /**----- Top -----*/

        // Create checkboxes
        CheckBox tooltips = new CheckBox("Tooltips"),
                description = new CheckBox("Description");
        VBox checkboxes = new VBox(3, tooltips, description);
        // Format checkboxes
        checkboxes.setPadding(new Insets(5));
        checkboxes.setMaxHeight(50);
        checkboxes.setAlignment(Pos.CENTER_LEFT);
        tooltips.setSelected(true);
        description.setSelected(true);


        /**----- Scroll -----*/

        // Create
        Polyline leftDisplayArrow = new Polyline(30, 0, 0, 40, 30, 80),
                rightDisplayArrow = new Polyline(0, 0, 30, 40, 0, 80);
        StackPane left = new StackPane(leftDisplayArrow),
                right = new StackPane(rightDisplayArrow);
        // Format
        left.setPrefWidth(70);
        right.setPrefWidth(70);
        left.setMaxHeight(100);
        right.setMaxHeight(100);
        left.setDisable(true);
        right.setDisable(true);
        BorderPane.setAlignment(left, Pos.CENTER_LEFT);
        BorderPane.setAlignment(right, Pos.CENTER_RIGHT);
        leftDisplayArrow.setStrokeWidth(20);
        rightDisplayArrow.setStrokeWidth(20);
        leftDisplayArrow.setStrokeLineCap(StrokeLineCap.ROUND);
        rightDisplayArrow.setStrokeLineCap(StrokeLineCap.ROUND);
        leftDisplayArrow.setId("left");
        rightDisplayArrow.setId("right");
        leftDisplayArrow.setVisible(false);
        rightDisplayArrow.setVisible(false);


        /**----- Main Menu -----*/

            /*----- Main buttons -----*/
        MRectangle alpButton = new MRectangle("Alphabetical"),
                numButton = new MRectangle("Numerical"),
                allCatButton = new MRectangle("All Categories"),
                catButton = new MRectangle("Category"),
                actsButton = new MRectangle("Actions");
        MRectangle[] menuButtons = {alpButton, numButton, allCatButton, catButton, actsButton};
        Polyline upButtonArrow = new Polyline(0, 6, 6, 0, 12, 6);
        Label currentCat = new Label("()");
        Pane space = new Pane();
        space.setPrefHeight(40);
        VBox pushUpArrowToTop = new VBox(upButtonArrow, space);actsButton.getChildren().add(pushUpArrowToTop);
        catButton.getChildren().add(currentCat);

            /*----- Action buttons -----*/
        MRectangle eAddButton = new MRectangle("New Employee", 0.75),
                eDeleteButton = new MRectangle("Fire Employee", 0.75),
                cAddButton = new MRectangle("Add Category", 0.75),
                cDeleteButton = new MRectangle("Delete Category", 0.75),
                quitButton = new MRectangle("Quit", 0.75);
        MRectangle[] actionButtons = {eAddButton, eDeleteButton, cAddButton, cDeleteButton, quitButton};
        Rectangle cover = new Rectangle(0, 60, 160, 20); // to hide the action menu while it's closed
        cover.setFill(Color.valueOf("#626263"));
        VBox actsMenu = new VBox(eAddButton, eDeleteButton, cAddButton, cDeleteButton, quitButton);
        Pane actsMain = new Pane(actsMenu, cover, actsButton);

            /*----- Format menu -----*/
        mainMenu.getChildren().addAll(alpButton, numButton, allCatButton, catButton, actsMain);
        alpButton.getRectangle().setStrokeWidth(3); // button 1: starts off as the first selected
        currentCat.setPrefWidth(120); // button 4
        currentCat.setPrefHeight(50);
        currentCat.setFont(Font.font(11));
        currentCat.setAlignment(Pos.BOTTOM_CENTER);
        pushUpArrowToTop.setAlignment(Pos.CENTER); // button 5
        upButtonArrow.setStrokeWidth(2);
        actsMenu.setTranslateX(10);
        actsMenu.setSpacing(10);
        actsMenu.setStyle("-fx-background-color: #323234; -fx-background-radius: 10 10 0 0");
        actsMenu.setPadding(new Insets(10));
        actsMain.setMinWidth(160);
        mainMenu.setPrefHeight(100); // whole menu
        mainMenu.setMaxWidth(500);
        mainMenu.setSpacing(40);
        mainMenu.setPadding(new Insets(20));
        mainMenu.setStyle("-fx-background-color: #626263; -fx-border-radius: 14 14 0 0; -fx-background-radius: 15 15 0 0");
        BorderPane.setAlignment(mainMenu, Pos.BOTTOM_CENTER);

        
        

        /*-------------------------------------------------------*/
        /**------------------- Event Handlers -------------------*/
        /*-------------------------------------------------------*/


        /**----- Mode Selection -----*/

            /*----- Hover animation -----*/
        for (MRectangle m : modeButtons) {
            m.setOnMouseEntered(e -> m.getRectangle().setStrokeWidth(2));
            m.setOnMouseExited(e -> m.getRectangle().setStrokeWidth(0));
        }

            /*----- Set up primary data structures -----*/
        pathname.addListener(e -> {
            try {
                Scanner input = new Scanner(new File(pathname.get()));

                while (input.hasNextLine()) {
                    String last = input.next(), first = input.next(), mid = input.next(), rawId = input.next(), category = input.next(), title = input.next();
                    addEmployeeV(first, mid, last, title, rawId, category);
                }
            }
            catch (FileNotFoundException ex) { addEmployeeV("Paul", "J", "Marques", "Ring_Master", "012-345-678", "Entertainer"); }
            currentActionIndex %= 6;
            mainDisplay.getChildren().removeAll(modes, shade);
            currentCat.setText("(" + cats.allCategories()[0] + ")");
            installTooltips();
        });

            /*----- Selected -----*/
        zero.setOnMouseClicked(e -> pathname.setValue(" "));
        sparse.setOnMouseClicked(e -> pathname.setValue("src/common/Employees Lite.txt"));
        crowded.setOnMouseClicked(e -> pathname.setValue("src/common/Employees.txt"));


        /**----- Checkboxes -----*/

            /*----- Tooltips -----*/
        tooltips.setOnAction(e -> {
            if (tooltips.isSelected()) {
                installTooltips();
                feedbackManager.addMessage("Tooltips enabled.");
            } else {
                for (Employee employee : names){
                    ECircle c = ((EmployeeV) employee).getVisual();
                    Tooltip.uninstall(c, new Tooltip());
                }
                feedbackManager.addMessage("Tooltips disabled.");
            }
        });

            /*----- Descriptions -----*/
        description.setOnAction(e -> {
            if (description.isSelected()) {
                for (Employee employee : names)
                    ((EmployeeV) employee).getVisual().getLabel().setVisible(true);
                feedbackManager.addMessage("Descriptions shown.");
            } else {
                for (Employee employee : names)
                    ((EmployeeV) employee).getVisual().getLabel().setVisible(false);
                feedbackManager.addMessage("Descriptions hidden.");
            }
        });


        /**----- Scrolling Mechanisms -----*/

        // (arrows' scrolling mechanisms (range of deltaX is only in negatives))
        TranslateTransition scroll = new TranslateTransition(Duration.millis(50), mainDisplay);
        ScaleTransition enlarge = new ScaleTransition(Duration.millis(50)), shrink = new ScaleTransition(Duration.millis(75)); // for arrow animation
        backdrop.translateXProperty().bind(mainDisplay.translateXProperty().multiply(-1));
        scroll.setInterpolator(Interpolator.LINEAR); // so smooth continuous horizontal translation
        shrink.setToX(1);
        shrink.setToY(1);
        EventHandler<MouseEvent> arrowHover = e -> {
            Polyline child = (Polyline) ((StackPane) e.getSource()).getChildren().get(0);
            enlarge(enlarge, child, 1.1);
            if (child.getId().equals("right")) {
                scroll.setOnFinished(f -> {
                    if (deltaX > ((currentButtonIndex != 3 ? names.size() : cats.employeeCount(cats.get(currentCatIndex))) * -90 + 860)) { // width of all circles animated exceeds with of window
                        deltaX -= 25 * multiplier;
                        multiplier *= 1.02;
                        scroll.setToX(deltaX);
                        scroll.play(); // recursive call for continuous scrolling
                    } else {
                        rightDisplayArrow.setVisible(false);
                        right.setDisable(true);
                    }
                    if (left.isDisable()) {
                        left.setDisable(false);
                    }
                    leftDisplayArrow.setVisible(true);
                });
            } else {
                scroll.setOnFinished(f -> {
                    if (deltaX < 0) {
                        deltaX += 25 * multiplier;
                        multiplier *= 1.02;
                        scroll.setToX(deltaX);
                        scroll.play();
                    } else {
                        leftDisplayArrow.setVisible(false);
                        left.setDisable(true);
                    }
                    if (right.isDisable()) {
                        right.setDisable(false);
                    }
                    rightDisplayArrow.setVisible(true);
                });
            }
            scroll.playFromStart();
        };
        EventHandler<MouseEvent> arrowExit = e -> {
            shrink(shrink, ((StackPane) e.getSource()).getChildren().get(0));
            multiplier = 1;
            scroll.setOnFinished(null);
        };
        left.setOnMouseEntered(arrowHover);
        left.setOnMouseExited(arrowExit);
        right.setOnMouseEntered(arrowHover);
        right.setOnMouseExited(arrowExit);


        /**----- Popup's Button Functions -----*/

        popup.setAlert(0, e -> { // check conditions before handling
            switch (currentActionIndex % 6) {
    
                case 0:
                    Employee other;
                    String[] employeeInfo = popup.getAnswers()[0].split(",[ ]*", 4);
                    // check conditions first
                    if (employeeInfo.length != 4) return "Missing information. (Name, title, ID, category)";
                    else if (employeeInfo[2].length() != 9)
                        return "Invalid id literal. (9 digits, no hyphens)";
                    else if ((other = ids.search(employeeInfo[2])) != null)
                        return "ID already exists! (" + other.getFullName() + ")";
                    else if (employeeInfo[1].contains(" "))
                        return "Title cannot include spaces.";
                    else if (employeeInfo[3].contains(" "))
                        return "Category name cannot include spaces.";
                    else return "";
    
                case 1:
                    if (popup.getAnswers()[0].length() != 9) return "Invalid ID literal. (9 digits, no hyphens)";
                    if (ids.search(new StringBuilder(popup.getAnswers()[0]).insert(7, "-").insert(4, "-").toString()) != null)
                        return "";
                    else return "ID does not exist.";
    
                case 2:
                    if (popup.getAnswers()[0].contains(" "))
                        return "Category name cannot include spaces.";
                    String[] categories = cats.allCategories();
                    for (int i = 0; i < cats.size(); i++) {
                        if (categories[i].equalsIgnoreCase(popup.getAnswers()[0])) return "Category already exists!";
                    }
                    return "";
    
                case 3:
                    if (popup.getAnswers()[0].contains(" "))
                        return "Category name cannot include spaces.";
                    categories = cats.allCategories();
                    for (int i = 0; i < cats.size(); i++) {
                        if (categories[i].equalsIgnoreCase(popup.getAnswers()[0])) return "";
                    }
                    return "Category does not exist.";
                    
            }
            return ""; // never reached
        });
        popup.setQuitAction(e -> {
            currentActionIndex %= 6;
            mainDisplay.getChildren().remove(popup);
        });
        popup.setSubmitAction(e -> {
            switch (currentActionIndex % 6) {
        
                // ADD EMPLOYEE
                case 0:
                    String[] employeeInfo = popup.getAnswers()[0].split(",[ ]*", 4);
                    
                    String[] name = employeeInfo[0].split(" ");
                    String first = name[0], mid, last, printId, category;
                    if (name.length > 2) { // make the name look better
                        mid = name[1].substring(0, 1);
                        last = name[2];
                    } else if (name.length > 1) {
                        mid = "#";
                        last = name[1];
                    } else {
                        mid = "#";
                        last = "";
                    }
                    printId = new StringBuilder(employeeInfo[2]).insert(6,"-").insert(3, "-").toString(); // adding hyphens to the rawId
                    category = employeeInfo[3].substring(0,1).toUpperCase() + employeeInfo[3].substring(1); // capitalizing the category name
                    boolean categoryExists = false;
                    for (String cat : cats.allCategories()) if (category.equals(cat)) {
                        categoryExists = true;
                    }
            
                    feedbackManager.addMessage("Hired " + employeeInfo[0] + "! " +
                            (!categoryExists ? "(Category \"" + category + "\" added.)" : "")); // new category created?
                    EmployeeV employee = addEmployeeV(first, mid, last, employeeInfo[1], printId, category);
                    if (tooltips.isSelected()) Tooltip.install(employee.getVisual(), new Tooltip(employee.getFullName() + "\n" +
                            employee.getCategory() + "\n" +
                            employee.getPrintId() + "\n" +
                            employee.getTitle()));
                    currentActionIndex %= 6;
                    mainDisplay.getChildren().remove(popup);
                    break;
        
                // DELETE EMPLOYEE
                case 1:
                    printId = new StringBuilder(popup.getAnswers()[0]).insert(7,"-").insert(4, "-").toString();
                    employee = (EmployeeV) ids.search(printId);
            
                    removeEmployeeV(employee);
                    cats.remove(employee);
                    feedbackManager.addMessage("Fired " + employee.getFirst() + " " + (!employee.getMid().equals("") ? employee.getMid() : "") + " " + employee.getLast() + " (" + employee.getCategory() + ")!");
                    currentActionIndex %= 6;
                    mainDisplay.getChildren().remove(popup);
                    break;
        
                // ADD CATEGORY
                case 2:
                    category = popup.getAnswers()[0].substring(0,1).toUpperCase() + popup.getAnswers()[0].substring(1);
                    cats.bornCat(category);
                    feedbackManager.addMessage("Category \"" + category + "\" added."); // include name
                    currentActionIndex %= 6;
                    mainDisplay.getChildren().remove(popup);
                    break;
        
                // DELETE CATEGORY
                case 3:
                    
                    String[] categories = cats.allCategories();
                    for (int i = 0; i < cats.size(); i++) {
                        if (categories[i].equalsIgnoreCase(popup.getAnswers()[0])) {
                            if (cats.employeeCount(cats.get(i)) > 0)
                                for (Iterator itr = cats.categoryIterator(cats.get(i)); itr.hasNext();) { // delete all employees of that category first
                                    EmployeeV n = (EmployeeV) itr.next();
                                    removeEmployeeV(n);
                                }
                            feedbackManager.addMessage("Category \"" + categories[i] + "\" and its " + cats.employeeCount(cats.get(i)) + " employees removed.");
    
                            cats.killCat(cats.get(i)); // then delete category so iterator works as needed
                            currentCat.setText("(" + cats.get(i) + ")");
                            currentActionIndex %= 6;
                            mainDisplay.getChildren().remove(popup);
                            break;
                        }
                    }
                
                    break;
            }
        });



        /*-------------------------------------------------------*/
        /**-------------------- Controls ------------------------*/
        /*-------------------------------------------------------*/


        /**----- Create stage -----*/
        whole.setTop(checkboxes);
        whole.setBottom(mainMenu);
        whole.setCenter(mainDisplay);
        whole.setRight(right);
        whole.setLeft(left);

        Scene scene = new Scene(whole, 1000, 500);
        primaryStage.setTitle("Welcome to the Circus V!");
        primaryStage.setScene(scene);
        primaryStage.show();


        /**----- Keyboard Controls -----*/

            /*----- Initialization of variables -----*/
        FillTransition buttonFillChange = new FillTransition(Duration.millis(100), Color.ORANGE, Color.RED);
        ParallelTransition ptCircles = new ParallelTransition(), ptMenu = new ParallelTransition();
        ptCircles.setInterpolator(Interpolator.EASE_OUT); // the one for animating circles
        ptMenu.setInterpolator(Interpolator.EASE_OUT); // the one for animating the menu, distinguished so one can happen independently of the other
        TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(200), actsMenu),
                closeMenu = new TranslateTransition(Duration.millis(200), actsMenu);
        menuTranslation.setToY(-285);
        closeMenu.setToY(0);
        menuTranslation.setInterpolator(Interpolator.EASE_OUT);
        closeMenu.setInterpolator(Interpolator.EASE_OUT);
        menuTranslation.setOnFinished(e -> ptMenu.pause());

            /*----- Handler -----*/
        scene.setOnKeyPressed(key -> {
            KeyCode keyCode = key.getCode();
            switch (keyCode) {
    
                case LEFT:
                    if (currentActionIndex == 5) {
                        menuButtons[currentButtonIndex].getRectangle().setStrokeWidth(0);
                        currentButtonIndex = (currentButtonIndex + 4) % 5;
                        menuButtons[currentButtonIndex].getRectangle().setStrokeWidth(3);
                    }
                    break;
    
                case RIGHT:
                    if (currentActionIndex == 5) {
                        menuButtons[currentButtonIndex].getRectangle().setStrokeWidth(0);
                        currentButtonIndex = (currentButtonIndex + 1) % 5;
                        menuButtons[currentButtonIndex].getRectangle().setStrokeWidth(3);
                    }
                    break;
    
                case UP:
                    if (currentButtonIndex == 3) { // catButton
                        currentCatIndex = (currentCatIndex + cats.size() - 1) % cats.size();
                        currentCat.setText("(" + cats.get(currentCatIndex) + ")");
                    } else if (!mainDisplay.getChildren().contains(popup) && currentButtonIndex == 4 && currentActionIndex > 0) { // action selected
                        if (currentActionIndex < 5) {
                            actionButtons[currentActionIndex].getRectangle().setStrokeWidth(0);
                            currentActionIndex--;
                            actionButtons[currentActionIndex].getRectangle().setStrokeWidth(2.25);
                        } else if (ptMenu.getStatus() != Animation.Status.RUNNING) { // open action menu
                            if (ptCircles.getStatus() == Animation.Status.PAUSED) {
                                finishPtCircles(buttonFillChange, scroll, left, right, leftDisplayArrow, rightDisplayArrow);
                                buttonFillChange.play();
                                ptCircles.play();
                            }
                            openMenu(ptMenu, actsButton, upButtonArrow, menuTranslation, quitButton);
                        }
                    }
                    break;
    
                case DOWN:
                    if (!mainDisplay.getChildren().contains(popup) && currentButtonIndex == 3) { // catButton
                        currentCatIndex = (currentCatIndex + 1) % cats.size();
                        currentCat.setText("(" + cats.get(currentCatIndex) + ")");
                    } else if (!mainDisplay.getChildren().contains(popup) && currentButtonIndex == 4 && currentActionIndex < 5) { // action
                        if (currentActionIndex < 4) { // action selected
                            actionButtons[currentActionIndex].getRectangle().setStrokeWidth(0);
                            currentActionIndex++;
                            actionButtons[currentActionIndex].getRectangle().setStrokeWidth(2.25);
                        } else if (ptMenu.getStatus() != Animation.Status.RUNNING) { // close action menu
                            closeMenu.play();
                            ptMenu.play();
                            currentActionIndex++;
                        }
                    }
                    break;
    
                case ENTER:
                    if (currentButtonIndex < 4) { // sort button selected
                        MRectangle button = menuButtons[currentButtonIndex];
                        if (ptCircles.getStatus() != Animation.Status.RUNNING) {
                            if (ptCircles.getStatus() == Animation.Status.STOPPED) { // circles are in original position
                                Iterator i1;
                                switch (currentButtonIndex) {
                                    case 0:
                                        i1 = names.iterator();
                                        break;
                                    case 1:
                                        i1 = ids.iterator();
                                        break;
                                    case 2:
                                        i1 = cats.iterator();
                                        break;
                                    default:
                                        i1 = cats.categoryIterator(cats.get(currentCatIndex));
                                }
                                if (i1 == null) {
                                    feedbackManager.addMessage("No " + ((currentButtonIndex == 3 ? cats.get(currentCatIndex) : "employee") + "s exist."));
                                    break;
                                }
                                switch (currentButtonIndex) {
                                    case 0:
                                        feedbackManager.addMessage("Employees sorted by last name.");
                                        break;
                                    case 1:
                                        feedbackManager.addMessage("Employees sorted by ID.");
                                        break;
                                    case 2:
                                        feedbackManager.addMessage("Employees sorted by category.");
                                        break;
                                    default:
                                        feedbackManager.addMessage(cats.get(currentCatIndex) + "s sorted by last name.");
                                }
                                buttonFillChange.setShape(button.getRectangle());
                                buttonFillChange.setRate(1);
                                buttonFillChange.play();
                                ptCircles.getChildren().clear();
                    
                                for (int i = 0; i1.hasNext(); i++) {
                                    ECircle current = ((EmployeeV) i1.next()).getVisual();
                                    current.toFront();
                                    ScaleTransition st = new ScaleTransition(Duration.seconds(1.2), current);
                                    TranslateTransition tt = new TranslateTransition(Duration.seconds(1.2), current);
                                    FillTransition ft = new FillTransition(Duration.seconds(1.2), current.getCircle());
                                    st.setToX(1.4);
                                    st.setToY(1.4);
                                    ft.setToValue(Color.LIGHTBLUE);
                                    tt.setToX(90 * i);
                                    tt.setToY(145);
                                    st.setCycleCount(2); // each cycle is 2 b/c they're paused halfway, and once ptCircles is resumed, each circle will
                                    ft.setCycleCount(2); // return to their *original position*, which is nice because then there's nothing modified
                                    tt.setCycleCount(2);
                                    st.setAutoReverse(true);
                                    ft.setAutoReverse(true);
                                    tt.setAutoReverse(true);
                                    ptCircles.getChildren().addAll(st, tt, ft);
                                }
                                if (deltaX > ((currentButtonIndex != 3 ? names.size() : cats.employeeCount(cats.get(currentCatIndex))) * -90 + 860)) { // scrollable
                                    right.setDisable(false);
                                    rightDisplayArrow.setVisible(true);
                                }
                                PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
                                pause.setOnFinished(f -> ptCircles.pause());
                                ptCircles.getChildren().add(pause);
                            } else {
                                finishPtCircles(buttonFillChange, scroll, left, right, leftDisplayArrow, rightDisplayArrow); // circles are in sorted position; finish first
                            }
                            buttonFillChange.play();
                        }
                        ptCircles.play();
                    } else { // fifth menu button selected
                        if (currentActionIndex == 5) { // open menu
                            openMenu(ptMenu, actsButton, upButtonArrow, menuTranslation, quitButton);
                        } else if (currentActionIndex == 4) { // quit
                            primaryStage.close();
                            System.out.println("\nThank you for joining us today, and we hope to see you again!");
                        } else if (currentActionIndex < 4) { // an action
                            // actions
                            int index = currentActionIndex;
                            currentActionIndex += 6; // to freeze all navigation controls while popup is up
                
                            popup.setTitle(index < 2 ? (index == 0 ? "Hire " : "Fire ") + "Employee"
                                    : ((index == 2 ? "New " : "Delete ") + "Category"));
                            popup.setPrompt(0, index < 2 ? "Employee " + (index == 0 ? "Information:" : "ID:")
                                    : "Category Name");
                            popup.setPlaceholder(0, index < 2 ? (index == 0 ? "Name, title, ID, category" : "123456789")
                                    : ((index == 2 ? "New " : "Delete ") + "Category"));
                
                            popup.clearFields();
                            mainDisplay.getChildren().add(popup);
                            popup.toFront();
                        } else {
                            popup.triggerSubmit();
                        }
                    }
                    break;
    
                case ESCAPE:
                    popup.triggerQuit();
            }
        });

    }


    public EmployeeV addEmployeeV(String first, String mid, String last, String title, String printId, String category) {
        EmployeeV employee = new EmployeeV(first, mid, last, title, printId, category);
        ECircle circle = employee.getVisual();

        names.insert(employee);
        ids.insert(employee);
        cats.insert(employee);

        mainDisplay.getChildren().add(circle);
        circle.setTranslateX((int) (Math.random() * 590) + 101);
        circle.setTranslateY((int) (Math.random() * 247) + 1);

        return employee;
    }

    public void removeEmployeeV(EmployeeV employee) {
        names.remove(employee);
        ids.remove(employee);
        // cats.remove(employee) isn't included because actions 1 and 3 (zero-based indexing) don't have this overlap
        mainDisplay.getChildren().remove(employee.getVisual());
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * All methods above are significant functions.
     * All methods below are there to remove the "duplicate code" warning. (Not really important enough to be separate.)
     */

    public void installTooltips() {
        for (Employee employee : names){
            Tooltip.install(((EmployeeV) employee).getVisual(), new Tooltip(employee.getFullName() + "\n" +
                    employee.getCategory() + "\n" +
                    employee.getPrintId() + "\n" +
                    employee.getTitle()));
        }
    }

    public void enlarge(ScaleTransition st, Node n, double factor) {
        st.setToX(factor);
        st.setToY(factor);
        st.setNode(n);
        st.play();
    }

    public void shrink(ScaleTransition st, Node n) {
        st.setNode(n);
        st.play();
    }

    public void finishPtCircles(FillTransition buttonFill, TranslateTransition scroll,
                                StackPane left, StackPane right, Polyline leftArrow, Polyline rightArrow) {
        // finish the circles animation (move them back to original locations)
        buttonFill.setRate(-1);
        scroll.stop();
        deltaX = 0;
        scroll.setToX(deltaX);
        scroll.setDuration(Duration.seconds(0.8));
        scroll.play();
        scroll.setDuration(Duration.millis(50));
        left.setDisable(true);
        right.setDisable(true);
        leftArrow.setVisible(false);
        rightArrow.setVisible(false);
    }
    
    public void openMenu(ParallelTransition ptMenu, MRectangle actsButton, Polyline upButtonArrow, TranslateTransition menuTranslation, MRectangle quitButton) {
        // open the actions menu
        currentActionIndex--;
        ptMenu.getChildren().clear();
        StrokeTransition strokeChange = new StrokeTransition(Duration.millis(200), actsButton.getRectangle());
        strokeChange.setToValue(Color.ORANGE);
        strokeChange.setCycleCount(2);
        strokeChange.setAutoReverse(true);
        RotateTransition rotateArrow = new RotateTransition(Duration.millis(200), upButtonArrow);
        rotateArrow.setToAngle(180);
        rotateArrow.setCycleCount(2);
        rotateArrow.setAutoReverse(true);
        ptMenu.getChildren().addAll(menuTranslation, strokeChange, rotateArrow);
        ptMenu.play();
        quitButton.getRectangle().setStrokeWidth(2.25);
    }

}