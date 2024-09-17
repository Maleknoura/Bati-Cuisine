package org.wora.repositoryImpl;

import org.wora.entity.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectRepositoryImpl implements org.wora.repository.ProjectRepository {
    private Connection connection;

    public ProjectRepositoryImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void save(Project project) {
        String status = project.getStatus().toString().toUpperCase();
        String query = "INSERT INTO Project (name, profitMargin, totalCost, status, quoteId, clientId) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, project.getName());
            stmt.setDouble(2, project.getProfitMargin());
            stmt.setDouble(3, project.getTotalCost());
            stmt.setObject(4, project.getStatus().toString(), java.sql.Types.OTHER);
            stmt.setObject(5, project.getQuote() != null ? project.getQuote().getId() : null);
            stmt.setObject(6, project.getClient() != null ? project.getClient().getId() : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Project findById(int id) {
        return null;
    }
}
