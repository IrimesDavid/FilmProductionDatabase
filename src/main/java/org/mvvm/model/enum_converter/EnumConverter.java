package org.mvvm.model.enum_converter;

// Generic Interface for Enum Conversion
public interface EnumConverter<T extends Enum<T>> {
    String toString(T enumValue);
    T fromString(String value);
}

