package menu.scenes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import menu.MainMenu;
import menu.MenuItem;
import menu.MenuNav;

public class SceneOption {

    // Variables
    private static Scene scene;
    private static Stage stage;
    private static VBox menuBox;
    private int[] tmpStats;
    private File file;

    private MenuItem[] stats = new MenuItem[14];

    // Constants
    private final int SCENE_WIDTH = 900;
    private final int SCENE_HEIGHT = 600;
    private final int VBOX_SPACING = 10;
    private final String BACKGROUND_IMG_PATH = "images/bck.jpg";
    private final String FILE_NAME = "stats/PlayerStats.dat";

    public SceneOption() {

        // Main pane
        VBox pane = new VBox();

        // Menu items
        MenuItem reset = new MenuItem("RESET", 20);
        MenuItem back = new MenuItem("BACK", 20);
        menuBox = new VBox(VBOX_SPACING, reset, back);
        menuBox.setStyle("-fx-padding: 20 0 0 0");

        // Event Handlers
        back.setOnActivate(() -> stage.setScene(MainMenu.getScene()));
        reset.setOnActivate(() -> {
            resetStats();
            updateUI();
        });

        tmpStats = new int[14]; // 10 hands + fold, check, call and bet stats
        for (int i = 0; i < 14; i++) {
            tmpStats[i] = 0;
        }

        // Update UI
        String[] statTypes = {"Fold", "Check", "Call", "Bet", "Royal Flush", "Straight Flush",
                                "Four of a Kind", "Full House", "Flush", "Straight", "Three of a Kind",
                                "Two Pair", "One Pair", "High Card"};

        for (int i=0; i<14; i++){
            stats[i] = new MenuItem("# " + statTypes[i] + ": " + tmpStats[i], 20);
            pane.getChildren().add(stats[i]);
        }

        stats[4].setStyle("-fx-padding: 20 0 0 0");

        // Create a File instance
        file = new File(FILE_NAME);

        getStats();
        updateUI();
        
        pane.getChildren().add(menuBox);
        pane.setAlignment(Pos.CENTER);

        // Background
        File imgF = new File(BACKGROUND_IMG_PATH);
        pane.setStyle("-fx-background-image: url(" + imgF.toURI().toString() + "); -fx-background-size: cover;");

        scene = new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
        MenuNav nav = new MenuNav(scene, menuBox, 0);
        nav.getMenuItem(0).setActive(true);
    }

    /**
     * Resets the stats folder and the stats UI to 0 for every statistic
     */
    private void resetStats(){

        try{
            for (int i = 0; i < 14; i++) {
                tmpStats[i] = 0;
            }

            PrintWriter output = new PrintWriter(file);

            for (int i=0; i<14; i++){
                if (i != 0){
                    output.print(",");
                }
                output.print(tmpStats[i]);
            }

            // Close the file
            output.close();

        } catch (FileNotFoundException e) {
            System.err.println("File not found exception");
        }
    }

    /**
     * Reads the stats from the stats file
     */
    public void getStats(){

        if (file.exists()) {
            try{
                // Create a Scanner for the file
                Scanner input = new Scanner(file);
		
                // Read data from a file
                String stat = "";
                if (input.hasNext()){
                    stat = input.nextLine();
                }

                for (int i=0; i<14; i++){
                    tmpStats[i] = Integer.valueOf(stat.split(",")[i]);
                }
        
                // Close the file
                input.close();
                
            } catch (FileNotFoundException e) {
                System.err.println("File not found exception");
            }
        }
    }

    /**
     * Updates the UI stats from the stats variable
     */
    public void updateUI(){
        for (int i=0; i<14; i++){
            stats[i].setText(stats[i].getText().substring(0, stats[i].getText().indexOf(":") + 2)
                            + String.valueOf(tmpStats[i]));
        }
    }

    /**
     * Returns the scene that is currently used
     * 
     * @return the scene that is currently used
     */
    public static Scene getScene() {
        return scene;
    }

    /**
     * Sets the stage to a new stage
     * 
     * @param primaryStage new stage
     */
    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }
}