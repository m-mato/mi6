/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Agent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Andrej Halaj
 * @author Matej Majdis
 */
public class AgentManagerImpl implements AgentManager {

    private static final Logger logger = Logger.getLogger(AssignmentManagerImpl.class.getName());

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    @Override
    public void createAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);
        Connection connection = null;
        PreparedStatement statement;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO AGENT (AGE, NICKNAME, PHONENUMBER) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, agent.getAge());
            statement.setString(2, agent.getNickName());
            statement.setString(3, agent.getPhoneNumber());

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            agent.setId(rs.getLong(1));

            connection.commit();
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    logger.log(Level.SEVERE, "rollback error", ex1);
                }
            }

            logger.log(Level.SEVERE, "error when creating agent", ex);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "error in changing autocommit value", ex);
            }
        }
    }

    @Override
    public void updateAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);
        Connection connection = null;
        PreparedStatement statement;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("UPDATE AGENT SET AGE=?, NICKNAME=?, PHONENUMBER=? WHERE ID=?");

            statement.setInt(1, agent.getAge());
            statement.setString(2, agent.getNickName());
            statement.setString(3, agent.getPhoneNumber());
            statement.setLong(4, agent.getId());

            statement.executeUpdate();
            
            connection.commit();
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    logger.log(Level.SEVERE, "rollback error", ex1);
                }
            }

            logger.log(Level.SEVERE, "error when updating agent", ex);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "error in changing autocommit value", ex);
            }
        }
    }

    @Override
    public void deleteAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);
        Connection connection = null;
        PreparedStatement statement;
        
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("DELETE FROM AGENT WHERE ID=?");

            statement.setLong(1, agent.getId());

            statement.executeUpdate();
            
            connection.commit();
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    logger.log(Level.SEVERE, "rollback error", ex1);
                }
            }

            logger.log(Level.SEVERE, "error when deleting agent", ex);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "error in changing autocommit value", ex);
            }
        }
    }

    @Override
    public Agent getAgentById(Long id) {
        checkDataSource();
        Connection connection = null;
        PreparedStatement statement;
        Agent agent;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT AGE, NICKNAME, PHONENUMBER FROM AGENT WHERE ID=?");

            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                agent = new Agent();
                agent.setId(id);
                agent.setAge(rs.getInt("AGE"));
                agent.setNickName(rs.getString("NICKNAME"));
                agent.setPhoneNumber(rs.getString("PHONENUMBER"));

                return agent;
            }
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    logger.log(Level.SEVERE, "rollback error", ex1);
                }
            }

            logger.log(Level.SEVERE, "error when getting agent by id", ex);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "error in changing autocommit value", ex);
            }
        }

        return null;
    }

    @Override
    public List<Agent> findAllAgents() {
        checkDataSource();
        Connection connection = null;
        PreparedStatement statement;
        Agent agent;
        List<Agent> agents = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT ID, AGE, NICKNAME, PHONENUMBER FROM AGENT");

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                agent = new Agent();
                agent.setId(rs.getLong(1));
                agent.setAge(rs.getInt(2));
                agent.setNickName(rs.getString(3));
                agent.setPhoneNumber(rs.getString(4));

                agents.add(agent);
            }
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    logger.log(Level.SEVERE, "rollback error", ex1);
                }
            }

            logger.log(Level.SEVERE, "error when finding all agents", ex);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "error in changing autocommit value", ex);
            }
        }

        return agents;
    }

    private void validateAgent(Agent agent) throws IllegalArgumentException {
        if (agent == null) {
            String message = "Error in createAgent: agent cannot be null.";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (agent.getNickName() == null) {
            String message = "Error in createAgent: agent name cannot be null.";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (agent.getNickName().isEmpty()) {
            String message = "Error in createAgent: agent name cannot be empty.";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (agent.getAge() <= 0) {
            String message = "Error in createAgent: agent name cannot be null.";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }
    }
}
