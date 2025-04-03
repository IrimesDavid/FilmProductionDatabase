package org.mvvm.model;

import org.mvvm.model.enum_converter.EnumConverter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

enum BaseType {
    DIRECTOR,
    PRODUCER,
    WRITER,
    ACTOR,
    UNDEFINED
}

public class Member implements EnumConverter<BaseType> {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty birthDate;
    private BaseType baseType;
    private SimpleStringProperty image;

    public Member() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty("UNDEFINED");
        this.birthDate = new SimpleStringProperty("1000-01-01");
        this.baseType = BaseType.UNDEFINED; //default type
        this.image = new SimpleStringProperty("UNDEFINED");
    }

    public Member(SimpleIntegerProperty id,
                  SimpleStringProperty name,
                  SimpleStringProperty birthDate,
                  SimpleStringProperty baseType,
                  SimpleStringProperty image) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.baseType = fromString(baseType.get());
        this.image = image;
    }

    // getters / setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getBirthDate() {
        return birthDate.get();
    }

    public void setBirthDate(String birthDate) throws IllegalArgumentException{
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(birthDate, formatter);
            this.birthDate.set(birthDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd", e);
        }
    }

    public String getBaseType() {
        return toString(baseType);
    }

    public void setBaseType(String baseType) {
        this.baseType = fromString(baseType);
    }

    public String getImage() {
        return image.get();
    }

    public void setImage(String image) {
        this.image.set(image);
    }

    @Override
    public String toString(BaseType enumValue) {
        return baseType.name(); // Converts Enum to String
    }

    @Override
    public BaseType fromString(String value) {
        try {
            return BaseType.valueOf(value.toUpperCase()); // Converts String to Enum
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Invalid BaseType: " + value);
            return BaseType.UNDEFINED; // Default value if invalid
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name=" + name +
                ", birthDate=" + birthDate +
                ", baseType=" + baseType +
                ", image=" + image +
                '}';
    }
}
