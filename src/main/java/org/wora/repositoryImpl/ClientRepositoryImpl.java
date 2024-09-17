package org.wora.repositoryImpl;

import org.wora.entity.Client;
import org.wora.repository.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRepositoryImpl implements ClientRepository {

    private Connection connection;

    public ClientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void addClient(Client client) {
        String query = "INSERT INTO clients (id, name, adress, numberPhone, isProfessionel) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, client.getId());
            stmt.setString(2, client.getName());
            stmt.setString(3, client.getAdress());
            stmt.setString(4, client.getNumberPhone());
            stmt.setBoolean(5, client.getIsProfessionel());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean clientExistsByName(String name) {
        String query = "SELECT * FROM clients WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Client getClientById(int id) {
        String query = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("adress"),
                            rs.getString("numberPhone"),
                            rs.getBoolean("isProfessionel")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




}
