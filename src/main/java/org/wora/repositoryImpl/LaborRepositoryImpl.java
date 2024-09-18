package org.wora.repositoryImpl;

import org.wora.entity.Labor;
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
    public void add(Labor labor) {
        String query = "INSERT INTO labor (typeComposant,hourlyRate, workHours, workerProductivity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, labor.getComponentType());
            stmt.setDouble(2, labor.getHourlyRate());
            stmt.setDouble(3, labor.getWorkHours());
            stmt.setDouble(4, labor.getWorkerProductivity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
