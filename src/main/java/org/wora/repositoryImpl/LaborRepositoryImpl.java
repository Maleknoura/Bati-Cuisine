package org.wora.repositoryImpl;

import org.wora.entity.Labor;
import org.wora.entity.Project;
import org.wora.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LaborRepositoryImpl implements ComponentRepository<Labor> {
    private Connection connection;

    public LaborRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Labor labor, int projectId) {
        String query = "INSERT INTO labor (name, unitcost, quantity, taxRate, hourly_rate, work_hours, worker_productivity, projectId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, labor.getName());
            stmt.setDouble(2, labor.getUnitCost());
            stmt.setDouble(3, labor.getQuantity());
            stmt.setDouble(4, labor.getTaxRate());
            stmt.setDouble(5, labor.getHourlyRate());
            stmt.setDouble(6, labor.getWorkHours());
            stmt.setDouble(7, labor.getWorkerProductivity());
            stmt.setInt(8, projectId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override

    public List<Labor> findByProjectId(int projectId) {
        List<Labor> labors = new ArrayList<>();
        String sql = "SELECT * FROM labor WHERE projectid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Labor labor = new Labor(
                        rs.getString("name"),
                        rs.getDouble("unitcost"),
                        rs.getDouble("quantity"),
                        rs.getString("componenttype"),
                        rs.getDouble("taxrate"),
                        rs.getDouble("hourly_rate"),
                        rs.getDouble("work_hours"),
                        rs.getDouble("worker_productivity")
                );
                labors.add(labor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labors;
    }




}
