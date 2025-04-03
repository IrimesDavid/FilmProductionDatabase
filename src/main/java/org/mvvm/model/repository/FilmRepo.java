package org.mvvm.model.repository;

import org.mvvm.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilmRepo {
    CastRepo castRepo;
    MemberRepo memberRepo;

    public FilmRepo(CastRepo castRepo, MemberRepo memberRepo) {
        this.castRepo = castRepo;
        this.memberRepo = memberRepo;
    }

    public List<Film> getAllFilms(String filter) throws SQLException {
        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM Films";

        if(filter != null && !filter.isEmpty()){
            if(filter.equals("ARTISTIC") || filter.equals("SERIES"))
                query += " WHERE type = ?";
            else
                query += " WHERE category = ?";
        }else{
            query += " ORDER BY type";
        }

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (filter != null && !filter.isEmpty()) {
                statement.setString(1, filter);
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Film film = new Film();
                    film.setId(resultSet.getInt("id"));
                    film.setTitle(resultSet.getString("title"));
                    film.setYear(resultSet.getInt("year"));
                    film.setType(resultSet.getString("type"));
                    film.setCategory(resultSet.getString("category"));
                    film.setDirectorId(resultSet.getInt("director_id"));
                    film.setWriterId(resultSet.getInt("writer_id"));
                    film.setProducerId(resultSet.getInt("producer_id"));

                    films.add(film);
                }
            }
        }
        return films;
    }

    public List<Film> getFilmsByCategory(String category) throws SQLException{
        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM Films WHERE category = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Film film = new Film();
                    film.setId(resultSet.getInt("id"));
                    film.setTitle(resultSet.getString("title"));
                    film.setYear(resultSet.getInt("year"));
                    film.setType(resultSet.getString("type"));
                    film.setCategory(resultSet.getString("category"));
                    film.setDirectorId(resultSet.getInt("director_id"));
                    film.setWriterId(resultSet.getInt("writer_id"));
                    film.setProducerId(resultSet.getInt("producer_id"));

                    films.add(film);
                }
            }
        }
        return films;
    }

    public List<Film> getFilmsByYear(int year) throws SQLException{
        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM Films WHERE year = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, year);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Film film = new Film();
                    film.setId(resultSet.getInt("id"));
                    film.setTitle(resultSet.getString("title"));
                    film.setYear(resultSet.getInt("year"));
                    film.setType(resultSet.getString("type"));
                    film.setCategory(resultSet.getString("category"));
                    film.setDirectorId(resultSet.getInt("director_id"));
                    film.setWriterId(resultSet.getInt("writer_id"));
                    film.setProducerId(resultSet.getInt("producer_id"));

                    films.add(film);
                }
            }
        }
        return films;
    }

    // NOTE: *by actor Name
    public List<Film> getFilmsByActor(String actorName) throws SQLException{
        int idActor = memberRepo.getMemberIDByName(actorName);
        List<Integer> filmIDs = castRepo.getStarredFilms(idActor);

        String idsString = filmIDs.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM films WHERE id IN (" + idsString +")";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Film film = new Film();
                    film.setId(resultSet.getInt("id"));
                    film.setTitle(resultSet.getString("title"));
                    film.setYear(resultSet.getInt("year"));
                    film.setType(resultSet.getString("type"));
                    film.setCategory(resultSet.getString("category"));
                    film.setDirectorId(resultSet.getInt("director_id"));
                    film.setWriterId(resultSet.getInt("writer_id"));
                    film.setProducerId(resultSet.getInt("producer_id"));

                    films.add(film);
                }
            }
        }
        return films;
    }

    public int getFilmIdByTitle(String filmTitle) {
        int id = 0;
        String query = "SELECT id FROM films WHERE title = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, filmTitle);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                   id = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public void addFilm(Film film) throws SQLException {
        String query = "INSERT INTO Films (title, year, type, category, director_id, writer_id, producer_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getYear());
            statement.setString(3, film.getType());
            statement.setString(4, film.getCategory());
            statement.setInt(5, film.getDirectorId());
            statement.setInt(6, film.getWriterId());
            statement.setInt(7, film.getProducerId());

            statement.executeUpdate();
        }
    }

    public void updateFilm(Film film) throws SQLException{
        String query = "UPDATE Films SET title = ?, year = ?, type = ?, category = ?," +
                " director_id = ?, writer_id = ?, producer_id = ? WHERE id = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getYear());
            statement.setString(3, film.getType());
            statement.setString(4, film.getCategory());
            statement.setInt(5, film.getDirectorId());
            statement.setInt(6, film.getWriterId());
            statement.setInt(7, film.getProducerId());
            statement.setInt(8, film.getId());

            statement.executeUpdate();
        }
    }

    public void deleteFilm(int id) throws SQLException {
        String query = "DELETE FROM Films WHERE id = ?";

        try (Connection connection = Repository.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        }
    }
}
