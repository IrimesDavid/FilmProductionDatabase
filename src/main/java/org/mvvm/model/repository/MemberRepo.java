package org.mvvm.model.repository;

import org.mvvm.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepo {
    public List<Member> getAllMembers(String baseType) throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM Members";

        if(baseType != null && !baseType.isEmpty()){
            query += " WHERE baseType = ?";
        }

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (baseType != null && !baseType.isEmpty()) {
                statement.setString(1, baseType);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Member member = new Member();
                    member.setId(resultSet.getInt("id"));
                    member.setName(resultSet.getString("name"));
                    member.setBirthDate(resultSet.getString("birthdate"));  // birthDate is a String now
                    member.setBaseType(resultSet.getString("baseType"));
                    member.setImage(resultSet.getString("image"));
                    members.add(member);
                }
            }
        }
        return members;
    }

    public int getMemberIDByName(String actorName) throws SQLException{
        int id = 0;
        String query = "SELECT id FROM Members WHERE name = ? LIMIT 1";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, actorName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                    break;
                }
            }
        }
        return id;
    }

    public void addMember(Member member) throws SQLException {
        String query = "INSERT INTO Members (name, birthdate, baseType, image) VALUES (?, ?, ?, ?)";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, member.getName());
            statement.setDate(2, java.sql.Date.valueOf(member.getBirthDate()));
            statement.setString(3, member.getBaseType());
            statement.setString(4, member.getImage());

            statement.executeUpdate();
        }
    }

    public void updateMember(Member member) throws SQLException {
        String query = "UPDATE Members SET name = ?, birthdate = ?, baseType = ?, image = ? WHERE id = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, member.getName());
            statement.setDate(2, java.sql.Date.valueOf(member.getBirthDate()));
            statement.setString(3, member.getBaseType());
            statement.setString(4, member.getImage());
            statement.setInt(5, member.getId());

            statement.executeUpdate();
        }
    }

    public void deleteMember(int id) throws SQLException {
        String query = "DELETE FROM Members WHERE id = ?";

        try (Connection connection = Repository.getConnection()) {
            // Delete Member
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        }
    }
}
