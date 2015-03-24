/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Agent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrej Halaj
 * @author Matej Majdis
 */
public class AgentManagerImpl implements AgentManager {

    private static final String DB_URL = "jdbc:derby://localhost:1527/mi6secretDB";

    @Override
    public void createAgent(Agent agent) {
        if (agent == null) {
            String message = "Error in createAgent: agent cannot be null.";
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }
        
        if (agent.getNickName() == null) {
            String message = "Error in createAgent: agent name cannot be null.";
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (agent.getNickName().isEmpty()) {
            String message = "Error in createAgent: agent name cannot be empty.";
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (agent.getAge() <= 0) {
            String message = "Error in createAgent: agent name cannot be null.";
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.WARNING, message);
            throw new IllegalArgumentException(message); 
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, "M", "12345")) {
            PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO M.AGENT (AGE, NICKNAME, PHONENUMBER) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            insertStatement.setInt(1, agent.getAge());
            insertStatement.setString(2, agent.getNickName());
            insertStatement.setString(3, agent.getPhoneNumber());

            insertStatement.executeUpdate();

            ResultSet rs = insertStatement.getGeneratedKeys();
            rs.next();
            agent.setId(rs.getLong(1));
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateAgent(Agent agent) {
        if (agent == null) {
            String message = "Error in updateAgent: agent cannot be null.";
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }
        
        if (agent.getNickName() == null) {
            String message = "Error in updateAgent: agent name cannot be null.";
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (agent.getNickName().isEmpty()) {
            String message = "Error in updateAgent: agent name cannot be empty.";
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (agent.getAge() <= 0) {
            String message = "Error in updateAgent: agent name cannot be null.";
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.WARNING, message);
            throw new IllegalArgumentException(message); 
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, "M", "12345")) {
            PreparedStatement insertStatement = conn.prepareStatement("UPDATE M.AGENT SET AGE=?, NICKNAME=?, PHONENUMBER=? WHERE ID=?");

            insertStatement.setInt(1, agent.getAge());
            insertStatement.setString(2, agent.getNickName());
            insertStatement.setString(3, agent.getPhoneNumber());
            insertStatement.setLong(4, agent.getId());

            insertStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteAgent(Agent agent) {
        try (Connection conn = DriverManager.getConnection(DB_URL, "M", "12345")) {
            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM M.AGENT WHERE ID=?");

            deleteStatement.setLong(1, agent.getId());

            deleteStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Agent getAgentById(Long id) {
        Agent agent;

        try (Connection conn = DriverManager.getConnection(DB_URL, "M", "12345")) {
            PreparedStatement getStatement = conn.prepareStatement("SELECT AGE, NICKNAME, PHONENUMBER FROM M.AGENT WHERE ID=?");

            getStatement.setLong(1, id);

            ResultSet rs = getStatement.executeQuery();
            if (rs.next()) {
                agent = new Agent();
                agent.setId(id);
                agent.setAge(rs.getInt("AGE"));
                agent.setNickName(rs.getString("NICKNAME"));
                agent.setPhoneNumber(rs.getString("PHONENUMBER"));

                return agent;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public List<Agent> findAllAgents() {
       Agent agent;
       List<Agent> agents = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, "M", "12345")) {
            PreparedStatement findStatement = conn.prepareStatement("SELECT ID, AGE, NICKNAME, PHONENUMBER FROM M.AGENT");


            ResultSet rs = findStatement.executeQuery();
            
            while (rs.next()) {
                agent = new Agent();
                agent.setId(rs.getLong(1));
                agent.setAge(rs.getInt(2));
                agent.setNickName(rs.getString(3));
                agent.setPhoneNumber(rs.getString(4));

                agents.add(agent);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return agents;
    }

}
