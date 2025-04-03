package org.mvvm.model.repository;

import org.mvvm.model.Cast;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CastRepo {
    FilmRepo filmRepo;

    public CastRepo(FilmRepo filmRepo) {
        this.filmRepo = filmRepo;
    }

    public List<Cast> getAllCasts() throws SQLException {
        List<Cast> casts = new ArrayList<>();
        String query = "SELECT * FROM Casts";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    casts.add(new Cast(resultSet.getInt("id"), resultSet.getInt("film_id"),resultSet.getInt("actor_id"), resultSet.getString("role")));
                }
            }
        }
        return casts;
    }

    public List<Cast> getFilmCast(String filmTitle) throws SQLException {
        int filmId = filmRepo.getFilmIdByTitle(filmTitle);

        List<Cast> casts = new ArrayList<>();
        String query = "SELECT * FROM Casts " + "WHERE film_id = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, filmId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    casts.add(new Cast(resultSet.getInt("id"), filmId, resultSet.getInt("actor_id"), resultSet.getString("role")));
                }
            }
        }
        return casts;
    }
    public List<Integer> getStarredFilms(int idActor) throws SQLException{
        List<Integer> filmIDs = new ArrayList<>();
        String query = "SELECT film_id FROM Casts WHERE actor_id = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idActor);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    filmIDs.add(resultSet.getInt("film_id"));
                }
            }
        }
        return filmIDs;
    }

    public void addCast(Cast cast) throws SQLException{
        String query = "INSERT INTO Casts (film_id, actor_id, role) VALUES (?, ?, ?)";
        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cast.getIdFilm());
            statement.setInt(2, cast.getIdActor());
            statement.setString(3, cast.getRole());
            statement.executeUpdate();
        }
    }

    public void deleteCast(int id) throws SQLException{
        String query = "DELETE FROM Casts WHERE id = ?";
        try (Connection connection = Repository.getConnection()) {
            // Delete Member
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        }
    }

    public void updateCast(Cast cast) throws SQLException{
        String query = "UPDATE Casts SET film_id = ?, actor_id = ?, role = ? WHERE id = ?";

        try (Connection connection = Repository.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, cast.getIdFilm());
            statement.setInt(2, cast.getIdActor());
            statement.setString(3, cast.getRole());
            statement.setInt(4, cast.getId());
            statement.executeUpdate();
        }
    }
}
