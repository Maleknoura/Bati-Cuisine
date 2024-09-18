package org.wora.repositoryImpl;

import org.wora.entity.Project;
import org.wora.repository.ProjectRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final Connection connection;

    public ProjectRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createProject(Project project) {
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
    public Optional<Project> getProjectById(int projectId) {
        return Optional.empty();
    }


    @Override
    public List<Project> getAllProjects() {
        return List.of();
    }


    @Override
    public void updateProject(Project project) {

    }


    @Override
    public void deleteProject(int projectId) {
        String query = "DELETE FROM Project WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    }

