package org.mvvm.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load first view (Member Management)
        FXMLLoader memberLoader = new FXMLLoader(getClass().getResource("/MemberV.fxml"));
        GridPane memberView = memberLoader.load();
        Tab memberTab = new Tab("Members", memberView);
        memberTab.setClosable(false); // Prevent tab from being closed

        // Load second view (Film management)
        FXMLLoader filmLoader = new FXMLLoader(getClass().getResource("/FilmV.fxml"));
        GridPane filmView = filmLoader.load();
        Tab filmTab = new Tab("Films", filmView);
        filmTab.setClosable(false);
        
        // Load third view (Cast management)
        FXMLLoader castLoader = new FXMLLoader(getClass().getResource("/CastV.fxml"));
        GridPane castView = castLoader.load();
        Tab castTab = new Tab("Casts", castView);
        castTab.setClosable(false);

        // Load fourth view (Cast management)
        FXMLLoader filmImageLoader = new FXMLLoader(getClass().getResource("/FilmImageV.fxml"));
        GridPane filmImageView = filmImageLoader.load();
        Tab filmImageTab = new Tab("Film Images", filmImageView);
        filmImageTab.setClosable(false);

        // Create TabPane and add tabs
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(memberTab, filmTab, castTab, filmImageTab);

        // Set the scene with TabPane
        stage.setScene(new Scene(tabPane, 800, 600)); // Adjust size as needed
        stage.setTitle("Film Production Database");
        stage.show();
    }
}
