package org.mvvm.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.apache.poi.xwpf.usermodel.*;
import org.mvvm.model.Film;
import org.mvvm.model.repository.CastRepo;
import org.mvvm.model.repository.FilmRepo;
import org.mvvm.model.repository.MemberRepo;
import org.mvvm.view.FormV;
import org.mvvm.viewmodel.commands.FilmCommands;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilmVM implements InterfaceVM{
    private List<Film> films;
    private Film crtFilm = new Film();
    public ObjectProperty<ObservableList<List<String>>> allFilmAttributes = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    public final ObjectProperty<List<String>> crtFilmAttributes = new SimpleObjectProperty<>();

    public final StringProperty message = new SimpleStringProperty("No messages");
    private StringProperty crtFilter = new SimpleStringProperty("No filter");
    private final FilmRepo FilmRepo;

    public final FilmCommands addCommand;
    public final FilmCommands deleteCommand;
    public final FilmCommands updateCommand;
    public final FilmCommands saveCSVCommand;
    public final FilmCommands saveDOCXCommand;

    public final FilmCommands filterCommand;

    public final FilmCommands enterYearCommand;
    public final FilmCommands enterActorCommand;
    public final FilmCommands filterByActorCommand;
    public final FilmCommands filterByYearCommand;


    public final FilmCommands commitTitleCommand;
    public final FilmCommands commitYearCommand;
    public final FilmCommands commitTypeCommand;
    public final FilmCommands commitCategoryCommand;
    public final FilmCommands commitDirectorIdCommand;
    public final FilmCommands commitWriterIdCommand;
    public final FilmCommands commitProducerIdCommand;

    public FilmVM() {
        this.addCommand = new FilmCommands((v) -> addItem());
        this.deleteCommand = new FilmCommands((v) -> deleteItem());
        this.updateCommand = new FilmCommands((v) -> updateItem());
        this.saveCSVCommand = new FilmCommands((v) -> saveToCSV());
        this.saveDOCXCommand = new FilmCommands((v) -> saveToDOCX());
        this.filterCommand = new FilmCommands((v) -> setCurrentFilter(v));
        this.enterYearCommand = new FilmCommands((v) -> quickYearForm());
        this.enterActorCommand = new FilmCommands((v) -> quickActorForm());
        this.filterByYearCommand = new FilmCommands((v) -> filterByYear(v));
        this.filterByActorCommand = new FilmCommands((v) -> filterByActor(v));

        this.commitTitleCommand = new FilmCommands((v) -> commitFilmField(1, v));
        this.commitYearCommand = new FilmCommands((v) -> commitFilmField(2, v));
        this.commitTypeCommand = new FilmCommands((v) -> commitFilmField(3, v));
        this.commitCategoryCommand = new FilmCommands((v) -> commitFilmField(4, v));
        this.commitDirectorIdCommand = new FilmCommands((v) -> commitFilmField(5, v));
        this.commitWriterIdCommand = new FilmCommands((v) -> commitFilmField(6, v));
        this.commitProducerIdCommand = new FilmCommands((v) -> commitFilmField(7, v));

        this.FilmRepo = new FilmRepo(new CastRepo(null), new MemberRepo());

        listfilms();
    }

    public void updateItem() {
        try{
            reconstructCurrentFilm();
            FilmRepo.updateFilm(crtFilm);
            listfilms();
        }catch (SQLException e){
            message.set("Failed to update Film");
        }
    }

    public void deleteItem() {
        try{
            reconstructCurrentFilm();
            FilmRepo.deleteFilm(crtFilm.getId());
            listfilms();
        }catch (SQLException e){
            message.set("Failed to delete Film");
        }
    }

    public void addItem() {
        try{
            FilmRepo.addFilm(new Film());
            crtFilter.set("No filter"); //resets filters automatically after adding a new Film
            listfilms();
        }catch (SQLException e){
            message.set("Failed to add Film");
        }
    }

    private void listfilms() {
        try {
            if(Objects.equals(crtFilter.get(), "No filter"))
                films = new ArrayList<>(FilmRepo.getAllFilms(null));
            else
                films = new ArrayList<>(FilmRepo.getAllFilms(crtFilter.get()));
            breakdownAllfilms();
        } catch (SQLException e) {
            message.set("Failed to load films");
        }
    }

    private void saveToCSV(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // write headers
                writer.write("ID,Title,Year,Type,Category,DirectorID,WriterID,ProducerID\n");

                // write film data
                for (Film film : films) {
                    writer.write(String.format("%d,%s,%d,%s,%s,%d,%d,%d\n",
                            film.getId(),
                            film.getTitle(),
                            film.getYear(),
                            film.getType(),
                            film.getCategory(),
                            film.getDirectorId(),
                            film.getWriterId(),
                            film.getProducerId()));
                }

                message.set(".CSV file saved");
            } catch (IOException e) {
                message.set("Failed to save .CSV file.");
            }
        }
    }

    private void saveToDOCX(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Document", "*.docx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                XWPFDocument document = new XWPFDocument();

                // create the title paragraph
                XWPFParagraph titleParagraph = document.createParagraph();
                XWPFRun titleRun = titleParagraph.createRun();
                titleRun.setText("Film List");
                titleRun.setBold(true);
                titleRun.setFontSize(16);
                titleParagraph.setAlignment(ParagraphAlignment.CENTER);

                // create a table
                XWPFTable table = document.createTable();

                // Create the header row
                XWPFTableRow headerRow = table.getRow(0);
                headerRow.getCell(0).setText("ID");
                headerRow.addNewTableCell().setText("Title");
                headerRow.addNewTableCell().setText("Year");
                headerRow.addNewTableCell().setText("Type");
                headerRow.addNewTableCell().setText("Category");
                headerRow.addNewTableCell().setText("Director ID");
                headerRow.addNewTableCell().setText("Writer ID");
                headerRow.addNewTableCell().setText("Producer ID");

                // add rows for each film in the table
                for (Film film : films) {
                    XWPFTableRow row = table.createRow();
                    row.getCell(0).setText(String.valueOf(film.getId()));
                    row.getCell(1).setText(film.getTitle());
                    row.getCell(2).setText(String.valueOf(film.getYear()));
                    row.getCell(3).setText(film.getType());
                    row.getCell(4).setText(film.getCategory());
                    row.getCell(5).setText(String.valueOf(film.getDirectorId()));
                    row.getCell(6).setText(String.valueOf(film.getWriterId()));
                    row.getCell(7).setText(String.valueOf(film.getProducerId()));
                }

                document.write(out);
                message.set(".DOCX file saved");

            } catch (IOException e) {
                message.set("Failed to save .DOCX file");
            }
        }
    }

    private void filterByYear(String year) {
        try {
            if(!year.matches("\\d+")){
                message.set("Invalid year");
                return;
            }
            films = new ArrayList<>(FilmRepo.getFilmsByYear(Integer.parseInt(year)));
            breakdownAllfilms();
        } catch (SQLException e) {
            message.set("Failed to filter films");
        }
    }

    private void filterByActor(String actor) {
        try {
            films = new ArrayList<>(FilmRepo.getFilmsByActor(actor));
            breakdownAllfilms();
        } catch (SQLException e) {
            message.set("Failed to filter films");
        }
    }

    public void breakdownAllfilms() {
        allFilmAttributes.get().clear();
        for (Film film : films) {
            List<String> attributes = new ArrayList<>();

            attributes.add(String.valueOf(film.getId()));
            attributes.add(film.getTitle());
            attributes.add(String.valueOf(film.getYear()));
            attributes.add(String.valueOf(film.getType()));
            attributes.add(String.valueOf(film.getCategory()));
            attributes.add(String.valueOf(film.getDirectorId()));
            attributes.add(String.valueOf(film.getWriterId()));
            attributes.add(String.valueOf(film.getProducerId()));

            allFilmAttributes.get().add(attributes);
        }
    }

    private void reconstructCurrentFilm(){
        try {
            crtFilm.setId(Integer.parseInt(crtFilmAttributes.get().get(0)));
            crtFilm.setTitle(crtFilmAttributes.get().get(1));
            crtFilm.setYear(Integer.parseInt(crtFilmAttributes.get().get(2)));
            crtFilm.setType(crtFilmAttributes.get().get(3));
            crtFilm.setCategory(crtFilmAttributes.get().get(4));
            crtFilm.setDirectorId(Integer.valueOf(crtFilmAttributes.get().get(5)));
            crtFilm.setDirectorId(Integer.valueOf(crtFilmAttributes.get().get(6)));
            crtFilm.setDirectorId(Integer.valueOf(crtFilmAttributes.get().get(7)));
            message.set("Film " + crtFilm.getId() + " processed");
        }catch(NullPointerException e){
            message.set("No Film selected");
        }
    }

    public void commitFilmField(int fieldIndex, String newValue) {
        if (crtFilmAttributes.get() != null) {
            crtFilmAttributes.get().set(fieldIndex, newValue);
        }
    }

    public void setCurrentFilter(String filterValue){
        crtFilter.set(filterValue);
        listfilms();
    }

    public void quickYearForm(){
        FormV form = new FormV(this);
        form.initializeYearForm();
    }

    public void quickActorForm(){
        FormV form = new FormV(this);
        form.initializeActorForm();
    }

    public ObjectProperty<List<String>> crtFilmAttributesProperty() {
        return crtFilmAttributes;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public ObjectProperty<ObservableList<List<String>>> allFilmAttributesProperty() {
        return allFilmAttributes;
    }

    public StringProperty currentFilterProperty(){
        return crtFilter;
    }
}
