/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.managers;

import backend.common.DBUtils;
import backend.common.IllegalEntityException;
import backend.entities.Agent;
import backend.entities.Assignment;
import backend.entities.Mission;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
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
public class AssignmentManagerImpl implements AssignmentManager {

    private static final Logger logger = Logger.getLogger(AssignmentManagerImpl.class.getName());
    private JdbcTemplate jdbc;
    private DataSource dataSource;
    private FileHandler fh;

    public AssignmentManagerImpl() {
        try {
            fh = new FileHandler("C:\\Users\\Andrej Halaj\\Documents\\NetBeansProjects\\mi6\\mi6maven\\log\\Log.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);
        } catch (IOException ex) {
            Logger.getLogger(AssignmentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(AssignmentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final RowMapper<Assignment> MAPPER = (ResultSet rs, int i) -> {
        AgentManagerImpl agentManager = new AgentManagerImpl();
        agentManager.setDataSource(dataSource);
        MissionManagerImpl missionManager = new MissionManagerImpl();
        missionManager.setDataSource(dataSource);

        Assignment assignment = new Assignment();
        assignment.setId(rs.getLong("ID"));
        assignment.setAgent(agentManager.getAgentById(rs.getLong("AGENT_ID")));
        assignment.setMission(missionManager.getMissionById(rs.getLong("MISSION_ID")));
        //assignment.setStartDate(rs.getString("START_DATE"));
        //assignment.setEndDate(rs.getString("END_DATE"));
        assignment.setStartDate(java.util.Date.from(Instant.ofEpochMilli(rs.getLong("START_DATE"))));
        BigDecimal endDateMili = rs.getBigDecimal("END_DATE");
        if (endDateMili == null) {
            assignment.setEndDate(null);
        } else {
            assignment.setEndDate(java.util.Date.from(Instant.ofEpochMilli(endDateMili.longValue())));
        }

        return assignment;
    };

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbc = new JdbcTemplate(dataSource);
    }
    
    public void setDefaultDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:mi6testDB;create=true");

        try {
            if (DBUtils.tryCreateTables(ds, getClass().getResource("/createTables.sql"))) {
                DBUtils.executeSqlScript(ds, getClass().getResource("/insertTestData.sql"));
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Can not create tables in test Database");
        }

        this.setDataSource(ds);
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    @Override
    public void createAssignment(final Assignment assignment) {
        checkDataSource();
        validateAssignment(assignment);
        if (assignment.getId() != null) {
            throw new IllegalEntityException("assignment id is already set");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator statement = (Connection connection) -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ASSIGNMENTS (AGENT_ID, MISSION_ID, START_DATE, END_DATE) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, assignment.getAgent().getId());
            ps.setLong(2, assignment.getMission().getId());
            //ps.setString(3, assignment.getStartDate());
            //ps.setString(4, assignment.getEndDate());
            ps.setLong(3, assignment.getStartDate().getTime());
            java.util.Date endDate = assignment.getEndDate();
            if (endDate == null) {
                ps.setNull(4, java.sql.Types.BIGINT);
            } else {
                ps.setLong(4, endDate.getTime());
            }
            return ps;
        };

        jdbc.update(statement, keyHolder);
        assignment.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void updateAssignment(Assignment assignment) {
        checkDataSource();
        validateAssignment(assignment);
        java.util.Date oldEndDate = getAssignmentById(assignment.getId()).getEndDate();
        java.util.Date endDate = assignment.getEndDate();
        
        if (endDate == null) {           
            if(oldEndDate != null) {
                String message = "Error in updateAssignment: cannot change end date of Assignment from instance to null.";
                logger.log(Level.WARNING, message);
                throw new IllegalArgumentException(message);              
            }
            jdbc.update("UPDATE ASSIGNMENTS SET AGENT_ID=?, MISSION_ID=?, START_DATE=? WHERE ID=?", assignment.getAgent().getId(),
                    assignment.getMission().getId(), assignment.getStartDate().getTime(), assignment.getId());
        } else {
            jdbc.update("UPDATE ASSIGNMENTS SET AGENT_ID=?, MISSION_ID=?, START_DATE=?, END_DATE=?  WHERE ID=?", assignment.getAgent().getId(),
                    assignment.getMission().getId(), assignment.getStartDate().getTime(), assignment.getEndDate().getTime(), assignment.getId());
        }
    }

    @Override
    public void deleteAssignment(Assignment assignment) {
        checkDataSource();

        if (assignment == null) {
            String message = "deleting null assignment";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        jdbc.update("DELETE FROM ASSIGNMENTS WHERE id=?", assignment.getId());
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        checkDataSource();

        if (id == null) {
            logger.log(Level.SEVERE, "id is null");
            throw new IllegalArgumentException("id is null");
        }

        Assignment assignment = jdbc.queryForObject("SELECT ID, AGENT_ID, MISSION_ID, START_DATE, END_DATE FROM ASSIGNMENTS WHERE ID=?", MAPPER, id);

        return assignment;
    }

    @Override
    public List<Assignment> findAllAssignments() {
        checkDataSource();

        List<Assignment> assignments = jdbc.query("SELECT * FROM ASSIGNMENTS", MAPPER);

        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentsForAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);

        List<Assignment> assignments = jdbc.query("SELECT * FROM ASSIGNMENTS WHERE AGENT_ID=?", MAPPER, agent.getId());

        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentsForMission(Mission mission) {
        checkDataSource();
        validateMission(mission);

        List<Assignment> assignments = jdbc.query("SELECT * FROM ASSIGNMENTS WHERE MISSION_ID=?", MAPPER, mission.getId());
        return assignments;
    }

    private void validateAssignment(Assignment assignment) {
        if (assignment == null) {
            String message = "assignment cannot be null";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (assignment.getAgent() == null) {
            String message = "agent cannot be null";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (assignment.getMission() == null) {
            String message = "mission cannot be null";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (assignment.getStartDate() == null) {
            String message = "start date cannot be null";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }
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

    private void validateMission(Mission mission) {
        if (mission == null) {
            String message = "mission cannot be null.";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (mission.getCodeName() == null) {
            String message = "mission codename cannot be null.";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (mission.getObjective() == null) {
            String message = "mission objective cannot be null.";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        if (mission.getLocation() == null) {
            String message = "mission location cannot be null.";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }
    }
}
