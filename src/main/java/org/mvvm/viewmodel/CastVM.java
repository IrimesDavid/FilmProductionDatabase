package org.mvvm.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mvvm.model.Cast;
import org.mvvm.model.repository.CastRepo;
import org.mvvm.model.repository.FilmRepo;
import org.mvvm.view.FormV;
import org.mvvm.viewmodel.commands.Command;
import org.mvvm.viewmodel.commands.CastCommands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CastVM implements InterfaceVM{

    private List<Cast> casts;
    private Cast crtCast = new Cast();
    public ObjectProperty<ObservableList<List<String>>> allCastAttributes = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    public final ObjectProperty<List<String>> crtCastAttributes = new SimpleObjectProperty<>();

    public final StringProperty message = new SimpleStringProperty("No messages");
    private final CastRepo castRepo;

    public final CastCommands addCommand;
    public final CastCommands deleteCommand;
    public final CastCommands updateCommand;
    public  final CastCommands enterFilmCommand;
    public final CastCommands filterByFilmCommand;


    public final CastCommands commitFilmIdCommand;
    public final CastCommands commitActorIdCommand;
    public final CastCommands commitRoleCommand;

    public CastVM() {
        this.addCommand = new CastCommands((v) -> addItem());
        this.deleteCommand = new CastCommands((v) -> deleteItem());
        this.updateCommand = new CastCommands((v) -> updateItem());
        this.enterFilmCommand = new CastCommands((v) -> quickFilmForm());
        this.filterByFilmCommand = new CastCommands((v) -> filterCasts(v));

        this.commitFilmIdCommand = new CastCommands((v) -> commitCastField(1, v));
        this.commitActorIdCommand = new CastCommands((v) -> commitCastField(2, v));
        this.commitRoleCommand = new CastCommands((v) -> commitCastField(3, v));

        this.castRepo = new CastRepo(new FilmRepo(null, null));

        listCasts();
    }

    public void updateItem() {
        try{
            reconstructCurrentCast();
            castRepo.updateCast(crtCast);
            listCasts();
        }catch (SQLException e){
            message.set("Failed to update Cast");
        }
    }

    public void deleteItem() {
        try{
            reconstructCurrentCast();
            castRepo.deleteCast(crtCast.getId());
            listCasts();
        }catch (SQLException e){
            message.set("Failed to delete Cast");
        }
    }

    public void addItem() {
        try{
            castRepo.addCast(new Cast());
            listCasts();
        }catch (SQLException e){
            message.set("Failed to add Cast");
        }
    }

    private void listCasts() {
        try {
            casts = new ArrayList<>(castRepo.getAllCasts());
            breakdownAllcasts();
        } catch (SQLException e) {
            message.set("Failed to load casts");
        }
    }
    
    private void filterCasts(String filmTitle){
        try {
            if(filmTitle.isEmpty())
                listCasts();
            else
                casts = new ArrayList<>(castRepo.getFilmCast(filmTitle));
            breakdownAllcasts();
        } catch (SQLException e) {
            message.set("Failed to filter casts");
        }
    }

    private void quickFilmForm(){
        FormV form = new FormV(this);
        form.initializeFilmForm();
    }
    
    public void breakdownAllcasts() {
        allCastAttributes.get().clear();
        for (Cast cast : casts) {
            List<String> attributes = new ArrayList<>();

            attributes.add(String.valueOf(cast.getId()));
            attributes.add(String.valueOf(cast.getIdFilm()));
            attributes.add(String.valueOf(cast.getIdActor()));
            attributes.add(cast.getRole());

            allCastAttributes.get().add(attributes);
        }
    }

    private void reconstructCurrentCast(){
        try {
            crtCast.setId(Integer.parseInt(crtCastAttributes.get().get(0)));
            crtCast.setIdFilm(Integer.parseInt(crtCastAttributes.get().get(1)));
            crtCast.setIdActor(Integer.parseInt(crtCastAttributes.get().get(2)));
            crtCast.setRole(crtCastAttributes.get().get(3));
            message.set("Cast " + crtCast.getId() + " processed");
        }catch(NullPointerException e){
            message.set("No cast selected");
        }
    }

    public void commitCastField(int fieldIndex, String newValue) {
        if (crtCastAttributes.get() != null) {
            crtCastAttributes.get().set(fieldIndex, newValue);
        }
    }

    public ObjectProperty<List<String>> crtCastAttributesProperty() {
        return crtCastAttributes;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public ObjectProperty<ObservableList<List<String>>> allCastAttributesProperty() {
        return allCastAttributes;
    }
}
