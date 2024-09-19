package org.wora.repositoryImpl;

import org.wora.entity.Material;
import org.wora.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialRepositoryImpl implements ComponentRepository<Material> {
    private Connection connection;

    public MaterialRepositoryImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void add(Material material, int projectId) {
        String query = "INSERT INTO material (name, unitcost, quantity, taxrate, transportcost, qualitycoefficient, projectId) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, material.getName());
            stmt.setDouble(2, material.getUnitCost());
            stmt.setDouble(3, material.getQuantity());
            stmt.setDouble(4, material.getTaxRate());
            stmt.setDouble(5, material.getTransportCost());
            stmt.setDouble(6, material.getQualityCoefficient());
            stmt.setInt(7, projectId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    @Override
    public List<Material> findByProjectId(int projectId) {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT * FROM material WHERE projectid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Material material = new Material(
                        rs.getString("name"),
                        rs.getDouble("unitcost"),
                        rs.getDouble("quantity"),
                        rs.getString("componenttype"),
                        rs.getDouble("taxrate"),
                        rs.getDouble("transportcost"),
                        rs.getDouble("qualitycoefficient")
                );
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

}
