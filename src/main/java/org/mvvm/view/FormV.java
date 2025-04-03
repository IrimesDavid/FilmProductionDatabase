package org.mvvm.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mvvm.viewmodel.CastVM;
import org.mvvm.viewmodel.FilmImageVM;
import org.mvvm.viewmodel.FilmVM;
import org.mvvm.viewmodel.InterfaceVM;

public class FormV {

    private final InterfaceVM myVM;

    public FormV(InterfaceVM myVM) {
        this.myVM = myVM;
    }

    public void initializeYearForm(){
        Stage stage = new Stage();
        stage.setTitle("Enter Year");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label label = new Label("Enter Year:");
        TextField yearField = new TextField();
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> ((FilmVM)myVM).filterByYearCommand.execute(yearField.getText()));

        layout.getChildren().addAll(label, yearField, submitButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 250, 150);
        stage.setScene(scene);
        stage.show();
    }

    public void initializeActorForm(){
        Stage stage = new Stage();
        stage.setTitle("Enter Actor");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label label = new Label("Enter Actor:");
        TextField actorField = new TextField();
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> ((FilmVM)myVM).filterByActorCommand.execute(actorField.getText()));

        layout.getChildren().addAll(label, actorField, submitButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 250, 150);
        stage.setScene(scene);
        stage.show();
    }

    public void initializeItemImageForm(String imageURL, String name) {
        Stage imageStage = new Stage();
        imageStage.setTitle(name);

        ImageView imageView = new ImageView();
        Image image = new Image(imageURL, true);

        imageView.setImage(image);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        VBox layout = new VBox(imageView);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 250, 250);
        imageStage.setScene(scene);
        imageStage.show();
    }

    public void initializeFilmForm() {
        Stage stage = new Stage();
        stage.setTitle("Enter Film");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label label = new Label("Enter Film:");
        TextField filmField = new TextField();
        Button submitButton = new Button("Submit");

        if(myVM instanceof CastVM)
            submitButton.setOnAction(e -> ((CastVM)myVM).filterByFilmCommand.execute(filmField.getText()));
        else
            submitButton.setOnAction(e -> ((FilmImageVM)myVM).filterByFilmCommand.execute(filmField.getText()));

        layout.getChildren().addAll(label, filmField, submitButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 250, 150);
        stage.setScene(scene);
        stage.show();
    }
}
