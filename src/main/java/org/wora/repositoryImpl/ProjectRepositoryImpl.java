package org.wora.repositoryImpl;

import org.wora.entity.Enum.Status;
import org.wora.entity.Project;
import org.wora.repository.ProjectRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository
{
    private Connection connection;


    public ProjectRepositoryImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void createProject(Project project) {

    }

    @Override
    public Optional<Project> getProjectById(int projectId) {
        return Optional.empty();
    }


    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Project project = mapProject(resultSet);
                projects.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }



    private Project mapProject(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt("id"));
        project.setName(resultSet.getString("name"));
        project.setProfitMargin(resultSet.getDouble("profitMargin"));
        project.setTotalCost(resultSet.getDouble("totalCost"));
        project.setStatus(Status.valueOf(resultSet.getString("status")));
        return project;
    }
    @Override
    public void updateProject(Project project) {

    }

    @Override
    public void deleteProject(int projectId) {

    }
}


