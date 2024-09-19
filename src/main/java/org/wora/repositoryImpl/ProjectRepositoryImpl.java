package org.wora.repositoryImpl;

import org.wora.entity.Component;
import org.wora.entity.Enum.Status;
import org.wora.entity.Labor;
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

        String projectQuery = "INSERT INTO Project (name, status, quoteId, clientId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(projectQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, project.getName());
            stmt.setObject(2, project.getStatus().toString(), java.sql.Types.OTHER);
            stmt.setObject(3, project.getQuote() != null ? project.getQuote().getId() : null);
            stmt.setObject(4, project.getClient() != null ? project.getClient().getId() : null);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    project.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Optional<Project> getProjectById(int projectId) {
        String query = "SELECT id, name, profitmargin, status FROM project WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Project project = new Project();
                    project.setId(rs.getInt("id"));
                    project.setName(rs.getString("name"));
                    project.setProfitMargin(rs.getDouble("profitmargin"));
                    project.setStatus(Status.valueOf(rs.getString("status")));

                    return Optional.of(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
    @Override
    public void updateProfitMargin(int projectId, double profitMargin) {
        String query = "UPDATE project SET profitmargin = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, profitMargin);
            stmt.setInt(2, projectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    }

