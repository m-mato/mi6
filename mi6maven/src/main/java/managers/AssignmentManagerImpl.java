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
import entities.Assignment;
import entities.Mission;
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
public class AssignmentManagerImpl implements AssignmentManager {

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
    public void createAssignment(final Assignment assignment) {
        checkDataSource();
        validateAssignment(assignment);
        if (assignment.getId() != null) {
            throw new IllegalEntityException("assignment id is already set");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement("INSERT INTO ASSIGNMENTS (AGENT_ID, MISSION_ID, START_DATE, END_DATE) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, assignment.getAgent().getId());
            st.setLong(2, assignment.getMission().getId());
            st.setString(3, assignment.getStartDate());
            st.setString(4, assignment.getEndDate());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, assignment, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            assignment.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting assignment into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void updateAssignment(Assignment assignment) {
        checkDataSource();
        validateAssignment(assignment);
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement("UPDATE ASSIGNMENTS SET AGENT_ID=?, MISSION_ID=?, START_DATE=?, END_DATE=?  WHERE ID=?");
            st.setLong(1, assignment.getAgent().getId());
            st.setLong(2, assignment.getMission().getId());
            st.setString(3, assignment.getStartDate());
            st.setString(4, assignment.getEndDate());
            st.setLong(5, assignment.getId());

            st.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating assignment in db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void deleteAssignment(Assignment assignment) {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;

        if (assignment == null) {
            String message = "deleting null assignment";
            logger.log(Level.WARNING, message);
            throw new IllegalArgumentException(message);
        }

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement("DELETE FROM ASSIGNMENTS WHERE id=?");
            st.setLong(1, assignment.getId());

            st.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting assignment from db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        checkDataSource();
        Connection connection = null;
        PreparedStatement statement = null;
        Assignment assignment;
        
        if (id == null) {
            logger.log(Level.SEVERE, "id is null");
            throw new IllegalArgumentException("id is null");
        }

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT AGENT_ID, MISSION_ID, START_DATE, END_DATE FROM ASSIGNMENTS WHERE ID=?");
            AgentManagerImpl agentManager = new AgentManagerImpl();
            MissionManagerImpl missionManager = new MissionManagerImpl();
            agentManager.setDataSource(dataSource);
            missionManager.setDataSource(dataSource);

            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                assignment = new Assignment();
                assignment.setId(id);
                assignment.setAgent(agentManager.getAgentById(rs.getLong(1)));
                assignment.setMission(missionManager.getMissionById(rs.getLong(2)));
                assignment.setStartDate(rs.getString(3));
                assignment.setEndDate(rs.getString(4));

                return assignment;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            String msg = "Error when getting assignment with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(connection, statement);
        }
    }

    @Override
    public List<Assignment> findAllAssignments() {
        checkDataSource();
        Connection connection = null;
        PreparedStatement statement = null;
        Assignment assignment;
        List<Assignment> assignments = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM ASSIGNMENTS");
            AgentManagerImpl agentManager = new AgentManagerImpl();
            MissionManagerImpl missionManager = new MissionManagerImpl();
            agentManager.setDataSource(dataSource);
            missionManager.setDataSource(dataSource);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                assignment = new Assignment();
                assignment.setId(rs.getLong(1));
                assignment.setAgent(agentManager.getAgentById(rs.getLong(2)));
                assignment.setMission(missionManager.getMissionById(rs.getLong(3)));
                assignment.setStartDate(rs.getString(4));
                assignment.setEndDate(rs.getString(5));

                assignments.add(assignment);
            }
        } catch (SQLException ex) {
            String msg = "Error when getting all assignments from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(connection, statement);
        }

        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentsForAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);
        Connection connection = null;
        PreparedStatement statement = null;
        Assignment assignment;
        List<Assignment> assignments = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM ASSIGNMENTS WHERE AGENT_ID=?");
            statement.setLong(1, agent.getId());
            
            AgentManagerImpl agentManager = new AgentManagerImpl();
            MissionManagerImpl missionManager = new MissionManagerImpl();
            agentManager.setDataSource(dataSource);
            missionManager.setDataSource(dataSource);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                assignment = new Assignment();
                assignment.setId(rs.getLong(1));
                assignment.setAgent(agentManager.getAgentById(rs.getLong(2)));
                assignment.setMission(missionManager.getMissionById(rs.getLong(3)));
                assignment.setStartDate(rs.getString(4));
                assignment.setEndDate(rs.getString(5));

                assignments.add(assignment);
            }
        } catch (SQLException ex) {
            String msg = "Error when getting all assignments for agent with id " + agent.getId() + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(connection, statement);
        }

        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentsForMission(Mission mission) {
        checkDataSource();
        validateMission(mission);
        Connection connection = null;
        PreparedStatement statement = null;
        Assignment assignment;
        List<Assignment> assignments = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM ASSIGNMENTS WHERE MISSION_ID=?");
            statement.setLong(1, mission.getId());
            
            AgentManagerImpl agentManager = new AgentManagerImpl();
            MissionManagerImpl missionManager = new MissionManagerImpl();
            agentManager.setDataSource(dataSource);
            missionManager.setDataSource(dataSource);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                assignment = new Assignment();
                assignment.setId(rs.getLong(1));
                assignment.setAgent(agentManager.getAgentById(rs.getLong(2)));
                assignment.setMission(missionManager.getMissionById(rs.getLong(3)));
                assignment.setStartDate(rs.getString(4));
                assignment.setEndDate(rs.getString(5));

                assignments.add(assignment);
            }
        } catch (SQLException ex) {
            String msg = "Error when getting all assignments for mission with id " + mission.getId() + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(connection, statement);
        }

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

        if (mission.getCodeName()== null) {
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
