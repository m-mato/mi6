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
public class MissionManagerImpl implements MissionManager {

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
    public void createMission(Mission mission) {
        checkDataSource();
        validateMission(mission);
        if (mission.getId() != null) {
            throw new IllegalEntityException("agent id is already set");
        }       
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement("INSERT INTO MISSIONS (CODENAME, OBJECTIVE, LOCATION, NOTES) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, mission.getCodeName());
            st.setString(2, mission.getObjective());
            st.setString(3, mission.getLocation());
            st.setString(4, mission.getNotes());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, mission, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            mission.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting mission into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void updateMission(Mission mission) {
        checkDataSource();
        validateMission(mission);  
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement("UPDATE MISSIONS SET CODENAME=?, OBJECTIVE=?, LOCATION=?, NOTES=? WHERE ID=?");
            st.setString(1, mission.getCodeName());
            st.setString(2, mission.getObjective());
            st.setString(3, mission.getLocation());
            st.setString(4, mission.getNotes());
            st.setLong(5, mission.getId());

            st.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating mission in db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void deleteMission(Mission mission) {
        checkDataSource(); 
        Connection conn = null;
        PreparedStatement st = null;
        
        if (mission == null) {
            logger.log(Level.SEVERE, "mission is null");
            throw new IllegalArgumentException("mission is null");  
        }
        
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement("DELETE FROM MISSIONS WHERE ID=?");
            st.setLong(1, mission.getId());

            st.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting mission from db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public Mission getMissionById(Long id) {
        checkDataSource();
        Connection connection = null;
        PreparedStatement statement = null;
        Mission mission;
        
        if (id == null) {
            logger.log(Level.SEVERE, "id is null");
            throw new IllegalArgumentException("id is null");
        }

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT CODENAME, OBJECTIVE, LOCATION, NOTES FROM MISSIONS WHERE ID=?");

            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                mission = new Mission();
                mission.setId(id);
                mission.setCodeName(rs.getString(1));
                mission.setObjective(rs.getString(2));
                mission.setLocation(rs.getString(3));
                mission.setNotes(rs.getString(4));

                return mission;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            String msg = "Error when getting mission with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(connection, statement);
        }
    }

    @Override
    public List<Mission> findAllMissions() {
        checkDataSource();
        Connection connection = null;
        PreparedStatement statement = null;
        Mission mission;
        List<Mission> missions = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT ID, CODENAME, OBJECTIVE, LOCATION, NOTES FROM MISSIONS");

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                mission = new Mission();
                mission.setId(rs.getLong(1));
                mission.setCodeName(rs.getString(2));
                mission.setObjective(rs.getString(3));
                mission.setLocation(rs.getString(4));
                mission.setNotes(rs.getString(5));

                missions.add(mission);
            }
        } catch (SQLException ex) {
            String msg = "Error when getting all missions from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(connection, statement);
        }

        return missions;
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
