/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.managers;

import backend.common.IllegalEntityException;
import backend.entities.Mission;
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
public class MissionManagerImpl implements MissionManager {

    private static final Logger logger = Logger.getLogger(AssignmentManagerImpl.class.getName());
    private JdbcTemplate jdbc;

    private DataSource dataSource;

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