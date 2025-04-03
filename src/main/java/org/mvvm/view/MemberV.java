package org.mvvm.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.mvvm.viewmodel.MemberVM;
import java.util.List;
import javafx.scene.control.TableColumn;


public class MemberV {
    @FXML private TableView<List<String>> membersTable;
    @FXML private TableColumn<List<String>, String> idColumn;
    @FXML private TableColumn<List<String>, String> nameColumn;
    @FXML private TableColumn<List<String>, String> birthdateColumn;
    @FXML private TableColumn<List<String>, String> typeColumn;
    @FXML private TableColumn<List<String>, String> imageURLColumn;

    @FXML private TextField message;
    @FXML private Button addButton, updateButton, deleteButton;
    @FXML private ComboBox<String> filterMenu = new ComboBox<>();
    @FXML private Button viewImageButton;
    private final MemberVM memberVM = new MemberVM();

    @FXML
    public void initialize() {

        // GENERAL SETUP
        // Make the table editable
        membersTable.setEditable(true);

        // Messages cant be edited / selected
        message.setDisable(true);

        // Add filter options
        filterMenu.getItems().addAll("DIRECTOR", "WRITER", "PRODUCER", "ACTOR", "No filter");
        filterMenu.setValue("Filter by"); //default option, when opening the application

        // COLUMNS SETUP
        // Set up columns to show specific List<String> elements
        idColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(0))  // Bind first attribute (e.g., Member ID)
                        : new SimpleStringProperty("");  // Return an empty property if no data exists
            }
        });
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(1))  
                        : new SimpleStringProperty(""); 
            }
        });
        birthdateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(2))  
                        : new SimpleStringProperty("");
            }
        });
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(3))  
                        : new SimpleStringProperty("");
            }
        });
        imageURLColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(4))  
                        : new SimpleStringProperty("");
            }
        });

        // BINDINGS SETUP
        // Bind edits in the table to commands
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> memberVM.commitNameCommand.execute(event.getNewValue()));
        birthdateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        birthdateColumn.setOnEditCommit(event -> memberVM.commitBirthdateCommand.execute(event.getNewValue()));
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        typeColumn.setOnEditCommit(event -> memberVM.commitTypeCommand.execute(event.getNewValue()));
        imageURLColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        imageURLColumn.setOnEditCommit(event -> memberVM.commitImageURLCommand.execute(event.getNewValue()));

        // Bind the selected item property to ViewModel's crtMember
        memberVM.crtMemberAttributes.bind(membersTable.getSelectionModel().selectedItemProperty());

        // Bind message to message property
        message.textProperty().bind(memberVM.message);

        // Bind table to list of members
        membersTable.itemsProperty().bind(memberVM.allMemberAttributes);

        // Bind buttons to commands
        memberVM.addCommand.trigger.bind(addButton.pressedProperty());
        memberVM.updateCommand.trigger.bind(updateButton.pressedProperty());
        memberVM.deleteCommand.trigger.bind(deleteButton.pressedProperty());

        // Bind current filter
        memberVM.crtFilter.bindBidirectional(filterMenu.valueProperty());
        memberVM.filterCommand.trigger.bind(filterMenu.showingProperty().not());

        // Bind view image button
        memberVM.viewImageCommand.trigger.bind(viewImageButton.pressedProperty());
    }
}
