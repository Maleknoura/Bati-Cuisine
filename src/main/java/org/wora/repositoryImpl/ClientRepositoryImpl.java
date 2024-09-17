package org.wora.repositoryImpl;

import org.wora.entity.Client;
import org.wora.repository.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClientRepositoryImpl implements ClientRepository {

    private Connection connection;

    public ClientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void addClient(Client client) {
        String query = "INSERT INTO client (id, name, adress, numberPhone, isProfessionel) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, client.getId());
            stmt.setString(2, client.getName());
            stmt.setString(3, client.getAdress());
            stmt.setString(4, client.getNumberPhone());
            stmt.setBoolean(5, client.getIsProfessionel());
            System.out.println("Executing query: " + stmt.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Client clientExistsByName(String name) {
        String query = "SELECT * FROM client WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String clientName = rs.getString("name");
                    String address = rs.getString("address");
                    String phoneNumber = rs.getString("phoneNumber");

                    return new Client();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Client getClientById(int id) {
        String query = "SELECT * FROM client WHERE id = ?";
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

    @Override
    public List<Client> findAll() {
        return List.of();
    }


}
