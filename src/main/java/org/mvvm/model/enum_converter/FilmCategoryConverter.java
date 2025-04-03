package org.mvvm.model.enum_converter;

import org.mvvm.model.FilmCategory;

public class FilmCategoryConverter implements EnumConverter<FilmCategory> {
    @Override
    public String toString(FilmCategory enumValue) {
        return enumValue.name();
    }

    @Override
    public FilmCategory fromString(String value) {
        try {
            return FilmCategory.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Invalid FilmCategory: " + value);
            return FilmCategory.UNDEFINED; // Default fallback
        }
    }
}
