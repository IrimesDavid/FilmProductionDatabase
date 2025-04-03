package org.mvvm.model.repository;

import org.mvvm.model.FilmImage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmImageRepo {
    private FilmRepo filmRepo;

    public FilmImageRepo(FilmRepo filmRepo) {
        this.filmRepo = filmRepo;
    }

    public List<FilmImage> getFilmImages() throws SQLException {
        List<FilmImage> filmImages = new ArrayList<>();
        String query = "SELECT * FROM filmImages ";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    filmImages.add(new FilmImage(resultSet.getInt("id"), resultSet.getInt("film_id"), resultSet.getString("url")));
                }
            }
        }
        return filmImages;
    }

    public List<FilmImage> filterFilmImages(String filmTitle) throws SQLException {
        int filmId = filmRepo.getFilmIdByTitle(filmTitle);

        List<FilmImage> filmImages = new ArrayList<>();
        String query = "SELECT * FROM filmImages WHERE film_id = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, filmId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    filmImages.add(new FilmImage(resultSet.getInt("id"), filmId, resultSet.getString("url")));
                }
            }
        }
        return filmImages;
    }

    public void addFilmImage(FilmImage filmImage) throws SQLException{
        String query = "INSERT INTO Filmimages (film_id, url) VALUES (?, ?)";
        try(Connection connection = Repository.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
                statement.setInt(1, filmImage.getIdFilm());
                statement.setString(2, filmImage.getUrl());
                statement.executeUpdate();
        }
    }

    public void updateFilmImage(FilmImage entry) throws SQLException{
        String query = "UPDATE Filmimages SET film_id = ?, url = ? WHERE id = ?";
        try (Connection connection = Repository.getConnection()) {
            // Delete Member
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, entry.getIdFilm());
                statement.setString(2, entry.getUrl());
                statement.setInt(3, entry.getId());
                statement.executeUpdate();
            }
        }
    }

    public void deleteFilmImage(int id) throws SQLException{
        String query = "DELETE FROM Filmimages WHERE id = ?";
        try (Connection connection = Repository.getConnection()) {
            // Delete Member
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        }
    }
}
