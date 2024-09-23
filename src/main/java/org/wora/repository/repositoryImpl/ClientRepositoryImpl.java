package org.wora.repository.repositoryImpl;

import org.wora.entity.Client;
import org.wora.repository.ClientRepository;

import java.lang.reflect.InvocationHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository {

    private Connection connection;

    public ClientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addClient(Client client) {
        String query = "INSERT INTO client (name, address,phonenumber, isProfessionel,remiseRate) VALUES (?, ?, ?, ?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getAdress());
            stmt.setString(3, client.getNumberPhone());
            stmt.setBoolean(4, client.getIsProfessionel());
            stmt.setDouble(5, client.getRemiseRate());
            stmt.executeUpdate();


            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    client.setId(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Client> findClientByName(String name) {
        String query = "SELECT * FROM client WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String clientName = rs.getString("name");
                    String address = rs.getString("address");
                    String phoneNumber = rs.getString("phoneNumber");
                    boolean isProfessional = rs.getBoolean("isProfessionel");


                    Client client = new Client();
                    client.setId(id);
                    client.setName(clientName);
                    client.setAdress(address);
                    client.setNumberPhone(phoneNumber);
                    client.setProfessionel(isProfessional);

                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
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
                            rs.getString("address"),
                            rs.getString("phonenumber"),
                            rs.getBoolean("isProfessionel"),
                            rs.getDouble("remiserate")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsByName(String name) {
        final String query = "SELECT EXISTS (SELECT 1 FROM client WHERE name = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
