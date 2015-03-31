/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Agent;
import entities.Assignment;
import entities.Mission;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Andrej Halaj
 * @author Matej Majdis
 */
public class AssignmentManagerImpl implements AssignmentManager {

    private JdbcTemplate jdbc;

    private static AgentManagerImpl agentManager = new AgentManagerImpl();
    private static MissionManagerImpl missionManager = new MissionManagerImpl();

    private static final Logger logger = Logger.getLogger(AssignmentManagerImpl.class.getName());

    public void setDataSource(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    private static final RowMapper<Assignment> MAPPER = new RowMapper<Assignment>() {
        @Override
        public Assignment mapRow(ResultSet rs, int i) throws SQLException {
            Assignment assignment = new Assignment();
            assignment.setId(rs.getLong("ID"));
            assignment.setAgent(agentManager.getAgentById(rs.getLong("AGENT_ID")));
            assignment.setMission(missionManager.getMissionById(rs.getLong("MISSION_ID")));
            assignment.setStartDate(rs.getDate("START_DATE"));
            assignment.setEndDate(rs.getDate("END_DATE"));
            return assignment;
        }
    };

    @Override
    public void createAssignment(Assignment assignment) {
        validateAssignment(assignment);
        jdbc.update("INSERT INTO ASSIGNMENTS (MISSION_ID, AGENT_ID, START_DATE, END_DATE) VALUES (?,?,?,?)",
                assignment.getAgent().getId(), assignment.getMission().getId(), assignment.getStartDate(), assignment.getEndDate());
    }

    @Override
    public void updateAssignment(Assignment assignment) {
        validateAssignment(assignment);
        jdbc.update("UPDATE ASSIGNMENTS SET AGENT_ID=? MISSION_ID=?, START_DATE=?, END_DATE=?  WHERE ID=?",
                assignment.getAgent().getId(), assignment.getMission().getId(), assignment.getStartDate(), assignment.getEndDate(), assignment.getId());
    }

    @Override
    public void deleteAssignment(Assignment assignment) {
        jdbc.update("DELETE FROM ASSIGNMENT WHERE id=?", assignment.getId());
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        return jdbc.queryForObject("SELECT * FROM ASSIGNMENT WHERE ID=?", MAPPER, id);
    }

    @Override
    public List<Assignment> findAllAssignments() {
        return jdbc.query("SELECT * FROM ASSIGNMENT", MAPPER);
    }

    @Override
    public List<Assignment> findAssignmentsForAgent(Agent agent) {
        return jdbc.query("SELECT * FROM ASSIGNMENT WHERE AGENT_ID=?",MAPPER, agent.getId());
    }

    @Override
    public List<Assignment> findAssignmentsForMission(Mission mission) {
        return jdbc.query("SELECT * FROM ASSIGNMENT WHERE MISSION_ID=?",MAPPER, mission.getId());
    }

    private void validateAssignment(Assignment assignment) {
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

}
