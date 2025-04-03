package org.mvvm.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.mvvm.viewmodel.FilmImageVM;

import java.util.List;

public class FilmImageV {
    @FXML
    private TableView<List<String>> filmImagesTable;
    @FXML
    private TableColumn<List<String>, String> idColumn;
    @FXML
    private TableColumn<List<String>, String> filmIdColumn;
    @FXML
    private TableColumn<List<String>, String> urlColumn;

    @FXML
    private TextField message;
    @FXML
    private Button addButton, updateButton, deleteButton;
    @FXML
    private Button viewImageButton, selectFilmButton;
    private final FilmImageVM filmImageVM = new FilmImageVM();

    @FXML
    public void initialize() {

        // GENERAL SETUP
        // Make the table editable
        filmImagesTable.setEditable(true);

        // Messages cant be edited / selected
        message.setDisable(true);

        // COLUMNS SETUP
        // Set up columns to show specific List<String> elements
        idColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(0))  // Bind first attribute (e.g., FilmImage ID)
                        : new SimpleStringProperty("");  // Return an empty property if no data exists
            }
        });
        filmIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(1))
                        : new SimpleStringProperty("");
            }
        });
        urlColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(2))
                        : new SimpleStringProperty("");
            }
        });

        // BINDINGS SETUP
        // Bind edits in the table to commands, to commit the changes (could simplify without command Objects, just by calling a method)
        filmIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        filmIdColumn.setOnEditCommit(event -> filmImageVM.commitFilmIdCommand.execute(event.getNewValue()));
        urlColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        urlColumn.setOnEditCommit(event -> filmImageVM.commitURLCommand.execute(event.getNewValue()));


        // Bind the selected item property to ViewModel's crtFilmImage
        filmImageVM.crtFilmImageAttributes.bind(filmImagesTable.getSelectionModel().selectedItemProperty());

        // Bind message to message property
        message.textProperty().bind(filmImageVM.message);

        // Bind table to list of FilmImages
        filmImagesTable.itemsProperty().bind(filmImageVM.allFilmImageAttributes);

        // Bind buttons to commands
        filmImageVM.addCommand.trigger.bind(addButton.pressedProperty());
        filmImageVM.updateCommand.trigger.bind(updateButton.pressedProperty());
        filmImageVM.deleteCommand.trigger.bind(deleteButton.pressedProperty());

        // Bind selectFilm button
        filmImageVM.enterFilmCommand.trigger.bind(selectFilmButton.pressedProperty());
        //Bind viewImage button
        filmImageVM.viewImageCommand.trigger.bind(viewImageButton.pressedProperty());
    }
}
