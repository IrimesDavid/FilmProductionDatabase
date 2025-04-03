package org.mvvm.model;

import org.mvvm.model.enum_converter.FilmCategoryConverter;
import org.mvvm.model.enum_converter.FilmTypeConverter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Film {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty directorId;
    private SimpleIntegerProperty producerId;
    private SimpleIntegerProperty writerId;
    private SimpleStringProperty title;
    private SimpleIntegerProperty year;
    private FilmType type;
    private FilmCategory category;

    // Converters
    private static final FilmTypeConverter typeConverter = new FilmTypeConverter();
    private static final FilmCategoryConverter categoryConverter = new FilmCategoryConverter();

    public Film() {
        this.id = new SimpleIntegerProperty();
        this.directorId = new SimpleIntegerProperty(1);
        this.producerId = new SimpleIntegerProperty(1);
        this.writerId = new SimpleIntegerProperty(1);
        this.title = new SimpleStringProperty("UNDEFINED");
        this.year = new SimpleIntegerProperty(1000);
        this.type = FilmType.UNDEFINED; //default
        this.category = FilmCategory.UNDEFINED; // default
    }
    public Film(SimpleIntegerProperty id,
                SimpleIntegerProperty directorId,
                SimpleIntegerProperty producerId,
                SimpleIntegerProperty writerId,
                SimpleStringProperty title,
                SimpleIntegerProperty year,
                SimpleStringProperty type,
                SimpleStringProperty category
    ) {
        this.id = id;
        this.directorId = directorId;
        this.producerId = producerId;
        this.writerId = writerId;
        this.title = title;
        this.year = year;
        this.type = typeConverter.fromString(type.get());
        this.category = categoryConverter.fromString(category.get());
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

    public int getDirectorId() {
        return directorId.get();
    }
    public SimpleIntegerProperty directorIdProperty(){
        return directorId;
    }
    public void setDirectorId(Integer directorId) {
        this.directorId.set(directorId);
    }

    public int getProducerId() {
        return producerId.get();
    }

    public SimpleIntegerProperty producerIdProperty(){
        return producerId;
    }

    public void setProducerId(Integer producerId) {
        this.producerId.set(producerId);
    }

    public int getWriterId() {
        return writerId.get();
    }

    public SimpleIntegerProperty writerIdProperty(){
        return writerId;
    }

    public void setWriterId(Integer writerId) {
        this.writerId.set(writerId);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getYear() {
        return year.get();
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public SimpleStringProperty typeProperty() {
        return new SimpleStringProperty(typeConverter.toString(type));
    }
    public String getType() {
        return typeConverter.toString(type);
    }
    public void setType(String type) {
        this.type = typeConverter.fromString(type);
    }

    public SimpleStringProperty categoryProperty() {
        return new SimpleStringProperty(categoryConverter.toString(category));
    }
    public String getCategory() {
        return categoryConverter.toString(category);
    }

    public void setCategory(String category) {
        this.category = categoryConverter.fromString(category);
    }


    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", directorId=" + directorId +
                ", producerId=" + producerId +
                ", writerId=" + writerId +
                ", title=" + title +
                ", year=" + year +
                ", type=" + type +
                ", category=" + category +
                '}';
    }
}
