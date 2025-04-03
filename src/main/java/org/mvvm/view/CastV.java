package org.mvvm.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.mvvm.viewmodel.CastVM;

import java.util.List;

public class CastV {
    @FXML private TableView<List<String>> castsTable;
    @FXML private TableColumn<List<String>, String> idColumn;
    @FXML private TableColumn<List<String>, String> filmIdColumn;
    @FXML private TableColumn<List<String>, String> actorIdColumn;
    @FXML private TableColumn<List<String>, String> roleColumn;

    @FXML private TextField message;
    @FXML private Button addButton, updateButton, deleteButton;
    @FXML private Button selectFilmButton;
    private final CastVM castVM = new CastVM();

    @FXML
    public void initialize() {

        // GENERAL SETUP
        // Make the table editable
        castsTable.setEditable(true);

        // Messages cant be edited / selected
        message.setDisable(true);

        // COLUMNS SETUP
        // Set up columns to show specific List<String> elements
        idColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(0))  // Bind first attribute (e.g., cast ID)
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
        actorIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(2))
                        : new SimpleStringProperty("");
            }
        });
        roleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(3))
                        : new SimpleStringProperty("");
            }
        });

        // BINDINGS SETUP
        // Bind edits in the table to commands, to commit the changes (could simplify without command Objects, just by calling a method)
        filmIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        filmIdColumn.setOnEditCommit(event -> castVM.commitFilmIdCommand.execute(event.getNewValue()));
        actorIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        actorIdColumn.setOnEditCommit(event -> castVM.commitActorIdCommand.execute(event.getNewValue()));
        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        roleColumn.setOnEditCommit(event -> castVM.commitRoleCommand.execute(event.getNewValue()));

        // Bind the selected item property to ViewModel's crtcast
        castVM.crtCastAttributes.bind(castsTable.getSelectionModel().selectedItemProperty());

        // Bind message to message property
        message.textProperty().bind(castVM.message);

        // Bind table to list of casts
        castsTable.itemsProperty().bind(castVM.allCastAttributes);

        // Bind buttons to commands
        castVM.addCommand.trigger.bind(addButton.pressedProperty());
        castVM.updateCommand.trigger.bind(updateButton.pressedProperty());
        castVM.deleteCommand.trigger.bind(deleteButton.pressedProperty());

        // Bind view selectFilm button
        castVM.enterFilmCommand.trigger.bind(selectFilmButton.pressedProperty());
    }
}
