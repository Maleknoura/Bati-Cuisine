package org.wora.repositoryImpl;

import org.wora.entity.Material;
import org.wora.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MaterialRepositoryImpl implements ComponentRepository<Material> {
    private Connection connection;

    public MaterialRepositoryImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void add(Material material, int projectId) {
        String query = "INSERT INTO material (name, unitcost, quantity, componenttype, taxrate, transportcost, qualitycoefficient, projectId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, material.getName());
            stmt.setDouble(2, material.getUnitCost());
            stmt.setDouble(3, material.getQuantity());
            stmt.setString(4, material.getComponentType());
            stmt.setDouble(5, material.getTaxRate());
            stmt.setDouble(6, material.getTransportCost());
            stmt.setDouble(7, material.getQualityCoefficient());
            stmt.setInt(8, projectId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
