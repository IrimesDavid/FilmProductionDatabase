package org.mvvm.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.mvvm.viewmodel.FilmVM;

import java.util.List;

public class FilmV {
    @FXML private TableView<List<String>> filmsTable;
    @FXML private TableColumn<List<String>, String> idColumn;
    @FXML private TableColumn<List<String>, String> titleColumn;
    @FXML private TableColumn<List<String>, String> yearColumn;
    @FXML private TableColumn<List<String>, String> typeColumn;
    @FXML private TableColumn<List<String>, String> categoryColumn;
    @FXML private TableColumn<List<String>, String> directorIdColumn;
    @FXML private TableColumn<List<String>, String> writerIdColumn;
    @FXML private TableColumn<List<String>, String> producerIdColumn;

    @FXML private TextField message;
    @FXML private Button addButton, updateButton, deleteButton;
    @FXML private Button saveCSV, saveDOCX;
    @FXML private MenuBar menuBar = new MenuBar();

    private final FilmVM filmVM = new FilmVM();

    @FXML
    public void initialize() {

        // GENERAL SETUP
        // Make the table editable
        filmsTable.setEditable(true);

        // Messages cant be edited / selected
        message.setDisable(true);

        // ADD FILTER MENUS
        // Create "Filter by" Menu
        Menu filterMenu = new Menu("Filter by");

        // Submenus
        Menu typeMenu = new Menu("Type");
        Menu categoryMenu = new Menu("Category");
        MenuItem noFilterItem = new MenuItem("No filter");

        // Type Options
        MenuItem artisticItem = new MenuItem("ARTISTIC");
        artisticItem.setOnAction(e -> filmVM.filterCommand.execute("ARTISTIC"));
        MenuItem seriesItem = new MenuItem("SERIES");
        seriesItem.setOnAction(e -> filmVM.filterCommand.execute("SERIES"));
        typeMenu.getItems().addAll(artisticItem, seriesItem);
        noFilterItem.setOnAction(e -> filmVM.filterCommand.execute("No filter"));

        // Category Options
        List<String> categories = List.of("ACTION", "WESTERN", "HISTORICAL", "SCIFI", "HORROR", "DRAMA", "ADVENTURE", "ROMANCE", "COMEDY", "THRILLER");
        for (String category : categories) {
            MenuItem categoryMenuItem = new MenuItem(category);
            categoryMenuItem.setOnAction(e -> filmVM.filterCommand.execute(category));
            categoryMenu.getItems().add(categoryMenuItem);
        }

        // Year Option (Opens a window)
        MenuItem yearItem = new MenuItem("Year");
        yearItem.setOnAction(e -> filmVM.enterYearCommand.execute());

        // Actor Option (Opens a window)
        MenuItem actorItem = new MenuItem("Actor");
        actorItem.setOnAction(e -> filmVM.enterActorCommand.execute());

        // Add submenus and options to the main menu
        filterMenu.getItems().addAll(typeMenu, categoryMenu, new SeparatorMenuItem(), yearItem, actorItem, new SeparatorMenuItem(), noFilterItem);

        // Add the "Filter by" menu to the MenuBar
        menuBar.getMenus().add(filterMenu);

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
        titleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(1))
                        : new SimpleStringProperty("");
            }
        });
        yearColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
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
        categoryColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(4))
                        : new SimpleStringProperty("");
            }
        });
        directorIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(5))
                        : new SimpleStringProperty("");
            }
        });
        writerIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(6))
                        : new SimpleStringProperty("");
            }
        });
        producerIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> cellData) {
                List<String> data = cellData.getValue();
                return (data != null && !data.isEmpty())
                        ? new SimpleStringProperty(data.get(7))
                        : new SimpleStringProperty("");
            }
        });

        // BINDINGS SETUP
        // Bind edits in the table to commands, to commit the changes (could simplify without command Objects, just by calling a method)
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setOnEditCommit(event -> filmVM.commitTitleCommand.execute(event.getNewValue()));
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        yearColumn.setOnEditCommit(event -> filmVM.commitYearCommand.execute(event.getNewValue()));
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        typeColumn.setOnEditCommit(event -> filmVM.commitTypeCommand.execute(event.getNewValue()));
        categoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryColumn.setOnEditCommit(event -> filmVM.commitCategoryCommand.execute(event.getNewValue()));
        directorIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        directorIdColumn.setOnEditCommit(event -> filmVM.commitDirectorIdCommand.execute(event.getNewValue()));
        writerIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        writerIdColumn.setOnEditCommit(event -> filmVM.commitWriterIdCommand.execute(event.getNewValue()));
        producerIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        producerIdColumn.setOnEditCommit(event -> filmVM.commitProducerIdCommand.execute(event.getNewValue()));

        // Bind the selected item property to ViewModel's crtMember
        filmVM.crtFilmAttributes.bind(filmsTable.getSelectionModel().selectedItemProperty());

        // Bind message to message property
        message.textProperty().bind(filmVM.message);

        // Bind table to list of members
        filmsTable.itemsProperty().bind(filmVM.allFilmAttributes);

        // Bind buttons to commands
        filmVM.addCommand.trigger.bind(addButton.pressedProperty());
        filmVM.updateCommand.trigger.bind(updateButton.pressedProperty());
        filmVM.deleteCommand.trigger.bind(deleteButton.pressedProperty());

        filmVM.saveCSVCommand.trigger.bind(saveCSV.pressedProperty());
        filmVM.saveDOCXCommand.trigger.bind(saveDOCX.pressedProperty());
    }
}
