package org.wora.repository.repositoryImpl;

import org.wora.entity.*;
import org.wora.entity.Enum.Status;
import org.wora.repository.ClientRepository;
import org.wora.repository.ProjectRepository;
import org.wora.service.ClientService;
import org.wora.service.serviceImpl.ClientServiceImpl;

import javax.print.attribute.standard.MediaSize;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final Connection connection;
    private ClientService clientService;

    public ProjectRepositoryImpl(Connection connection,ClientService clientService) {
        this.connection = connection;
        this.clientService = clientService;
    }

    @Override
    public void createProject(Project project) {

        String projectQuery = "INSERT INTO Project (name,clientId) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(projectQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, project.getName());
            stmt.setObject(2, project.getClient() != null ? project.getClient().getId() : null);
            System.out.println("Client ID: " + (project.getClient() != null ? project.getClient().getId() : "null"));
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
        String query = "SELECT id, name, profitmargin, status,clientId FROM project WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Project project = new Project();
                    project.setId(rs.getInt("id"));
                    project.setName(rs.getString("name"));
                    project.setProfitMargin(rs.getDouble("profitmargin"));
                    project.setStatus(Status.valueOf(rs.getString("status")));
                    int clientId = rs.getInt("clientid");
                    Client client = clientService.getClientById(clientId).orElse(null);
                    project.setClient(client);
                    return Optional.of(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void deleteProject(int projectId) {
        String query = "DELETE FROM Project WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            stmt.executeUpdate();
            System.out.println("projetID"+projectId);
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
    @Override
    public void updateTotalCost(int projectId, double totalCost) {
        String sql = "UPDATE project SET totalcost = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, totalCost);
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override

    public List<Project> displayAllProjects() {
        Map<Integer, Project> projectMap = new HashMap<>();
        String sql = "SELECT " +
                "    p.id, " +
                "    p.name, " +
                "    p.profitmargin, " +
                "    p.totalcost, " +
                "    p.status, " +
                "    c.id AS client_id, " +
                "    c.name AS client_name, " +
                "    c.address, " +
                "    c.phonenumber AS client_phone, " +
                "    c.isprofessionel AS client_isProfessional, " +
                "    'Material' AS component_type, " +
                "    m.id AS component_id, " +
                "    m.name AS component_name, " +
                "    m.taxrate AS component_taxRate, " +
                "    m.unitcost AS component_unitCost, " +
                "    m.quantity AS component_quantity, " +
                "    m.transportcost AS component_transportCost, " +
                "    m.qualitycoefficient AS component_qualityCoefficient, " +
                "    NULL AS worker_productivity " +
                "FROM " +
                "    project p " +
                "JOIN " +
                "    client c ON p.clientid = c.id " +
                "LEFT JOIN " +
                "    material m ON p.id = m.projectid " +
                "UNION ALL " +
                "SELECT " +
                "    p.id, " +
                "    p.name, " +
                "    p.profitmargin, " +
                "    p.totalcost, " +
                "    p.status, " +
                "    c.id AS client_id, " +
                "    c.name AS client_name, " +
                "    c.address, " +
                "    c.phonenumber AS client_phone, " +
                "    c.isprofessionel AS client_isProfessional, " +
                "    'Labor' AS component_type, " +
                "    l.id AS component_id, " +
                "    l.name AS component_name, " +
                "    l.taxrate AS component_taxRate, " +
                "    l.hourly_rate AS component_unitCost, " +
                "    l.work_hours AS component_quantity, " +
                "    NULL AS component_transportCost, " +
                "    NULL AS component_qualityCoefficient, " +
                "    l.worker_productivity " +
                "FROM " +
                "    project p " +
                "JOIN " +
                "    client c ON p.clientid = c.id " +
                "LEFT JOIN " +
                "    labor l ON p.id = l.projectid;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int projectId = rs.getInt("id");
                Project project = projectMap.get(projectId);

                if (project == null) {
                    project = new Project();
                    project.setId(projectId);
                    project.setName(rs.getString("name"));
                    project.setProfitMargin(rs.getDouble("profitmargin"));

                    String statusString = rs.getString("status");
                    Status status = Status.valueOf(statusString.toUpperCase());
                    project.setStatus(status);


                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));
                    client.setName(rs.getString("client_name"));
                    client.setAdress(rs.getString("address"));
                    client.setNumberPhone(rs.getString("client_phone"));
                    client.setProfessionel(rs.getBoolean("client_isProfessional"));

                    project.setClient(client);


                    projectMap.put(projectId, project);
                }


                String componentType = rs.getString("component_type");
                if ("Material".equals(componentType)) {
                    Material material = new Material();
                    material.setId(rs.getInt("component_id"));
                    material.setName(rs.getString("component_name"));
                    material.setTaxRate(rs.getDouble("component_taxRate"));
                    material.setUnitCost(rs.getDouble("component_unitCost"));
                    material.setQuantity(rs.getInt("component_quantity"));
                    material.setTransportCost(rs.getDouble("component_transportCost"));
                    material.setQualityCoefficient(rs.getDouble("component_qualityCoefficient"));

                    project.add(material);
                } else if ("Labor".equals(componentType)) {
                    Labor labor = new Labor();
                    labor.setId(rs.getInt("component_id"));
                    labor.setName(rs.getString("component_name"));
                    labor.setTaxRate(rs.getDouble("component_taxRate"));
                    labor.setHourlyRate(rs.getDouble("component_unitCost"));
                    labor.setWorkHours(rs.getInt("component_quantity"));
                    labor.setWorkerProductivity(rs.getDouble("worker_productivity"));

                    project.add(labor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new ArrayList<>(projectMap.values());
    }



}

