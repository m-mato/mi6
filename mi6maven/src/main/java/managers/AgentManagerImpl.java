/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import common.DBUtils;
import common.IllegalEntityException;
import common.ServiceFailureException;
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
        if (agent.getId() != null) {
            throw new IllegalEntityException("agent id is already set");
        }       
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement("INSERT INTO AGENTS (NICKNAME, AGE, PHONE_NUMBER) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, agent.getNickName());
            st.setInt(2, agent.getAge());
            st.setString(3, agent.getPhoneNumber());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            agent.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting agent into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void updateAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("UPDATE AGENTS SET AGE=?, NICKNAME=?, PHONE_NUMBER=? WHERE ID=?");

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
            statement = connection.prepareStatement("DELETE FROM AGENTS WHERE ID=?");

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
        PreparedStatement statement = null;
        Agent agent;
        
        if (id == null) {
            logger.log(Level.SEVERE, "id is null");
            throw new IllegalArgumentException("id is null");
        }

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT AGE, NICKNAME, PHONE_NUMBER FROM AGENTS WHERE ID=?");

            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                agent = new Agent();
                agent.setId(id);
                agent.setAge(rs.getInt(1));
                agent.setNickName(rs.getString(2));
                agent.setPhoneNumber(rs.getString(3));

                return agent;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            String msg = "Error when getting agent with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(connection, statement);
        }
    }

    @Override
    public List<Agent> findAllAgents() {
        checkDataSource();
        Connection connection = null;
        PreparedStatement statement = null;
        Agent agent;
        List<Agent> agents = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT ID, AGE, NICKNAME, PHONE_NUMBER FROM AGENTS");

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
            String msg = "Error when getting all agents from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(connection, statement);
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
