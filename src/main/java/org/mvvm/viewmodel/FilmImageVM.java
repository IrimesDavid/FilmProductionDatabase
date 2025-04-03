package org.mvvm.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mvvm.model.FilmImage;
import org.mvvm.model.repository.FilmImageRepo;
import org.mvvm.model.repository.FilmRepo;
import org.mvvm.view.FormV;
import org.mvvm.viewmodel.commands.FilmImageCommands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmImageVM implements InterfaceVM{
    private List<FilmImage> FilmImages;
    private FilmImage crtFilmImage = new FilmImage();
    public ObjectProperty<ObservableList<List<String>>> allFilmImageAttributes = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    public final ObjectProperty<List<String>> crtFilmImageAttributes = new SimpleObjectProperty<>();

    public final StringProperty message = new SimpleStringProperty("No messages");
    private final FilmImageRepo FilmImageRepo;

    public final FilmImageCommands addCommand;
    public final FilmImageCommands deleteCommand;
    public final FilmImageCommands updateCommand;
    public final FilmImageCommands viewImageCommand;
    public  final FilmImageCommands enterFilmCommand;
    public final FilmImageCommands filterByFilmCommand;

    public final FilmImageCommands commitFilmIdCommand;
    public final FilmImageCommands commitURLCommand;

    public FilmImageVM() {
        this.addCommand = new FilmImageCommands((v) -> addItem());
        this.deleteCommand = new FilmImageCommands((v) -> deleteItem());
        this.updateCommand = new FilmImageCommands((v) -> updateItem());
        this.viewImageCommand = new FilmImageCommands((v) -> quickImageForm());
        this.enterFilmCommand = new FilmImageCommands((v) -> quickFilmForm());
        this.filterByFilmCommand = new FilmImageCommands((v) -> filterFilmImages(v));

        this.commitFilmIdCommand = new FilmImageCommands((v) -> commitFilmImageField(1, v));
        this.commitURLCommand = new FilmImageCommands((v) -> commitFilmImageField(2, v));

        this.FilmImageRepo = new FilmImageRepo(new FilmRepo(null, null));

        listFilmImages();
    }

    public void updateItem() {
        try{
            reconstructCurrentFilmImage();
            FilmImageRepo.updateFilmImage(crtFilmImage);
            listFilmImages();
        }catch (SQLException e){
            message.set("Failed to update FilmImage");
        }
    }

    public void deleteItem() {
        try{
            reconstructCurrentFilmImage();
            FilmImageRepo.deleteFilmImage(crtFilmImage.getId());
            listFilmImages();
        }catch (SQLException e){
            message.set("Failed to delete FilmImage");
        }
    }

    public void addItem() {
        try{
            FilmImageRepo.addFilmImage(new FilmImage());
            listFilmImages();
        }catch (SQLException e){
            message.set("Failed to add FilmImage");
            System.out.println(e.getMessage());
        }
    }

    private void listFilmImages() {
        try {
            FilmImages = new ArrayList<>(FilmImageRepo.getFilmImages());
            breakdownAllFilmImages();
        } catch (SQLException e) {
            message.set("Failed to load FilmImages");
        }
    }

    private void filterFilmImages(String filmTitle){
        try {
            if(filmTitle.isEmpty())
                listFilmImages();
            else
                FilmImages = new ArrayList<>(FilmImageRepo.filterFilmImages(filmTitle));
            breakdownAllFilmImages();
        } catch (SQLException e) {
            message.set("Failed to filter FilmImages");
        }
    }

    private void quickFilmForm(){
        FormV form = new FormV(this);
        form.initializeFilmForm();
    }
    private void quickImageForm(){
        reconstructCurrentFilmImage();
        //Convert Windows path to JavaFX-compatible URI
        String imageURL = "file:///" + crtFilmImage.getUrl().replace("\\", "/");

        FormV form = new FormV(this);
        form.initializeItemImageForm(imageURL, "Image");
    }
    public void breakdownAllFilmImages() {
        allFilmImageAttributes.get().clear();
        for (FilmImage FilmImage : FilmImages) {
            List<String> attributes = new ArrayList<>();

            attributes.add(String.valueOf(FilmImage.getId()));
            attributes.add(String.valueOf(FilmImage.getIdFilm()));
            attributes.add(FilmImage.getUrl());

            allFilmImageAttributes.get().add(attributes);
        }
    }

    private void reconstructCurrentFilmImage(){
        try {
            crtFilmImage.setId(Integer.parseInt(crtFilmImageAttributes.get().get(0)));
            crtFilmImage.setIdFilm(Integer.parseInt(crtFilmImageAttributes.get().get(1)));
            crtFilmImage.setUrl(crtFilmImageAttributes.get().get(2));
            message.set("FilmImage " + crtFilmImage.getId() + " processed");
        }catch(NullPointerException e){
            message.set("No FilmImage selected");
        }
    }

    public void commitFilmImageField(int fieldIndex, String newValue) {
        if (crtFilmImageAttributes.get() != null) {
            crtFilmImageAttributes.get().set(fieldIndex, newValue);
        }
    }

    public ObjectProperty<List<String>> crtFilmImageAttributesProperty() {
        return crtFilmImageAttributes;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public ObjectProperty<ObservableList<List<String>>> allFilmImageAttributesProperty() {
        return allFilmImageAttributes;
    }
}
