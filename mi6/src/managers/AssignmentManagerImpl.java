/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Agent;
import entities.Assignment;
import entities.Mission;
import java.sql.Statement;
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
    public void createAssignment(Assignment assignment) {
    ("INSERT INTO M.ASSIGNMENTS (MISSION_ID, AGENT_ID, START_DATE, END_DATE) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

    }

    @Override
    public void updateAssignment(Assignment assignment) {
        
    }

    @Override
    public void deleteAssignment(Assignment assignment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Assignment> findAllAssignments() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Assignment> findAssignmentsForAgent(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Assignment> findAssignmentsForMission(Mission mission) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
