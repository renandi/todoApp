package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Project;
import util.ConnectionFactory;

/**
 *
 * @author Renan
 */
public class ProjectController {
    
    public void save(Project proj) {
        String sql = "INSERT INTO projects (name, description,  createdAt, updatedAt) "
                + "VALUES (?,?,?,?)";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, proj.getName());
            statement.setString(2, proj.getDescription());
            statement.setDate(3, new Date(proj.getCreatedAt().getTime()));
            statement.setDate(4, new Date(proj.getUpdatedAt().getTime()));
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException("erro ao salvar a tarefa" + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(connection, statement);
        }

    }
    
    public void update(Project proj) {
        String sql = "UPDATE projects SET name=?, description=?, createdAt=?, updatedAt=? WHERE id=?";

        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = ConnectionFactory.getConnection();
            statement = conn.prepareStatement(sql);
            
            statement.setString(1, proj.getName());
            statement.setString(2, proj.getDescription());
            statement.setDate(3, new Date(proj.getCreatedAt().getTime()));
            statement.setDate(4, new Date(proj.getUpdatedAt().getTime()));
            statement.setInt(5, proj.getId());
            
            statement.execute();
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar a tarefa " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, statement);
        }
    }
    
    public void removeById(int projId) {
        String sql = "DELETE FROM  projects WHERE id = ?";

        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = ConnectionFactory.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, projId);
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar a tarefa " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, statement);
        }
    }
    
    public List<Project> getAll() {
        
        String sql = "SELECT * FROM projects";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        
        List<Project> projects = new ArrayList<Project>();
        
        try {
            
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            res = statement.executeQuery();
            
            while (res.next()) {
                Project proj = new Project(res.getInt("id"), 
                                           res.getString("name"), 
                                           res.getString("description"), 
                                           res.getDate("createdAt"), 
                                           res.getDate("updatedAt"));
                projects.add(proj);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter tarefas "+e.getMessage(),e);
        } finally {
            ConnectionFactory.closeConnection(connection, statement, res);
        }
        
        return projects;
    }
}
