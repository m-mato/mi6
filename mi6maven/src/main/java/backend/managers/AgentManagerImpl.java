/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.managers;

import backend.entities.Agent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author Andrej Halaj
 * @author Matej Majdis
 */
public class AgentManagerImpl implements AgentManager {

    private static final Logger logger = Logger.getLogger(AssignmentManagerImpl.class.getName());
    private JdbcTemplate jdbc;

    private DataSource dataSource;

    private static final RowMapper<Agent> MAPPER = (rs, rowNum) -> {
        Agent agent = new Agent();
        agent.setId(rs.getLong("ID"));
        agent.setNickName(rs.getString("NICKNAME"));
        agent.setAge(rs.getInt("AGE"));
        agent.setPhoneNumber(rs.getString("PHONE_NUMBER"));

        return agent;
    };

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbc = new JdbcTemplate(dataSource);
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    @Override
    public void createAgent(final Agent agent) {
        checkDataSource();
        validateAgent(agent);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator statement = (Connection connection) -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO AGENTS (NICKNAME, AGE, PHONE_NUMBER) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, agent.getNickName());
            ps.setInt(2, agent.getAge());
            ps.setString(3, agent.getPhoneNumber());
            return ps;
        };

        jdbc.update(statement, keyHolder);
        agent.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void updateAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);

        jdbc.update("UPDATE AGENTS SET AGE=?, NICKNAME=?, PHONE_NUMBER=? WHERE ID=?", agent.getAge(), agent.getNickName(), agent.getPhoneNumber(), agent.getId());
    }

    @Override
    public void deleteAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);

        jdbc.update("DELETE FROM AGENTS WHERE ID=?", agent.getId());
    }

    @Override
    public Agent getAgentById(Long id) {
        checkDataSource();
        
        if (id == null) {
            String message = "id is null";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }
        
        Agent agent = jdbc.queryForObject("SELECT ID, NICKNAME, AGE, PHONE_NUMBER FROM AGENTS WHERE ID=?", MAPPER, id);
        
        return agent;
    }

    @Override
    public List<Agent> findAllAgents() {
        checkDataSource();
        
        List<Agent> agents = jdbc.query("SELECT ID, NICKNAME, AGE, PHONE_NUMBER FROM AGENTS", MAPPER);

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
