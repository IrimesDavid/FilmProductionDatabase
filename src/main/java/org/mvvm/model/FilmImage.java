package org.mvvm.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
public class FilmImage {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty idFilm;
    private SimpleStringProperty url;

    public FilmImage() {
        this.id = new SimpleIntegerProperty();
        this.idFilm = new SimpleIntegerProperty(1);
        this.url = new SimpleStringProperty("UNDEFINED");
    }
    public FilmImage(Integer id, Integer idFilm, String url) {
        this.id = new SimpleIntegerProperty(id);
        this.idFilm =  new SimpleIntegerProperty(idFilm);
        this.url = new SimpleStringProperty(url);
    }

    // getters / setters
    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getIdFilm() {
        return idFilm.get();
    }

    public SimpleIntegerProperty idFilmProperty() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm.set(idFilm);
    }

    public String getUrl() {
        return url.get();
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }
}
