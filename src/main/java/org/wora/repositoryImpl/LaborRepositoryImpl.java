package org.wora.repositoryImpl;

import org.wora.entity.Labor;
import org.wora.entity.Project;
import org.wora.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LaborRepositoryImpl implements ComponentRepository<Labor> {
    private Connection connection;

    public LaborRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Labor labor, int projectId) {
        String query = "INSERT INTO labor (name, unitcost, quantity, componenttype, taxRate, hourly_rate, work_hours, worker_productivity, projectId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, labor.getName());
            stmt.setDouble(2, labor.getUnitCost());
            stmt.setDouble(3, labor.getQuantity());
            stmt.setString(4, labor.getComponentType());
            stmt.setDouble(5, labor.getTaxRate());
            stmt.setDouble(6, labor.getHourlyRate());
            stmt.setDouble(7, labor.getWorkHours());
            stmt.setDouble(8, labor.getWorkerProductivity());
            stmt.setInt(9, projectId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
