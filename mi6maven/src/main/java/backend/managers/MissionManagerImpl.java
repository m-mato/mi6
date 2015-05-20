/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.managers;

import backend.common.DBUtils;
import backend.common.IllegalEntityException;
import backend.entities.Mission;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
public class MissionManagerImpl implements MissionManager {

    private static final Logger logger = Logger.getLogger(AssignmentManagerImpl.class.getName());
    private JdbcTemplate jdbc;
    private FileHandler fh;

    private DataSource dataSource;

    public MissionManagerImpl() {
        try {
            fh = new FileHandler("C:\\Users\\Andrej Halaj\\Documents\\NetBeansProjects\\mi6\\mi6maven\\log\\Log.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);
        } catch (IOException ex) {
            Logger.getLogger(MissionManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MissionManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static final RowMapper<Mission> MAPPER = (rs, rowNum) -> {
        Mission mission = new Mission();
        mission.setId(rs.getLong("ID"));
        mission.setCodeName(rs.getString("CODENAME"));
        mission.setObjective(rs.getString("OBJECTIVE"));
        mission.setLocation(rs.getString("LOCATION"));
        mission.setNotes(rs.getString("NOTES"));

        return mission;
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
    public void createMission(Mission mission) {
        checkDataSource();
        validateMission(mission);
        if (mission.getId() != null) {
            throw new IllegalEntityException("agent id is already set");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator statement = (Connection connection) -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO MISSIONS (CODENAME, OBJECTIVE, LOCATION, NOTES) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, mission.getCodeName());
            ps.setString(2, mission.getObjective());
            ps.setString(3, mission.getLocation());
            ps.setString(4, mission.getNotes());
            return ps;
        };

        jdbc.update(statement, keyHolder);
        mission.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void updateMission(Mission mission) {
        checkDataSource();
        validateMission(mission);

        jdbc.update("UPDATE MISSIONS SET CODENAME=?, OBJECTIVE=?, LOCATION=?, NOTES=? WHERE ID=?", mission.getCodeName(), mission.getObjective(),
                mission.getLocation(), mission.getNotes(), mission.getId());
    }

    @Override
    public void deleteMission(Mission mission) {
        checkDataSource();

        if (mission == null) {
            logger.log(Level.SEVERE, "mission is null");
            throw new IllegalArgumentException("mission is null");
        }

        jdbc.update("DELETE FROM MISSIONS WHERE ID=?", mission.getId());
    }

    @Override
    public Mission getMissionById(Long id) {
        checkDataSource();

        if (id == null) {
            logger.log(Level.SEVERE, "id is null");
            throw new IllegalArgumentException("id is null");
        }

        Mission mission = jdbc.queryForObject("SELECT ID, CODENAME, OBJECTIVE, LOCATION, NOTES FROM MISSIONS WHERE ID=?", MAPPER, id);

        return mission;
    }

    @Override
    public List<Mission> findAllMissions() {
        checkDataSource();
        
        List<Mission> missions = jdbc.query("SELECT ID, CODENAME, OBJECTIVE, LOCATION, NOTES FROM MISSIONS", MAPPER);

        return missions;
    }
    
    @Override
    public List<Mission> findMissionsWithoutAgent() {
        checkDataSource();
        
        List<Mission> missions = jdbc.query("SELECT * FROM MISSIONS WHERE ID NOT IN (SELECT ASSIGNMENTS.MISSION_ID FROM ASSIGNMENTS)", MAPPER);
        
        return missions;
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
