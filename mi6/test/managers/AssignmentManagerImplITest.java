/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Agent;
import entities.Assignment;
import entities.Mission;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Matej Majdis
 */
public class AssignmentManagerImplITest {

    private AssignmentManagerImpl manager;
    
    @Before
    public void setUp() throws SQLException {
        this.manager= new AssignmentManagerImpl();
    }
       
    @Test
    public void createAssignment() {
         Agent agent= new Agent();
         Mission mission= new Mission();
         Date date= Date.from(Instant.now());
         
         //Assignment assignment= newAssignment(null, null, null, null);
    }

    @Test
    public void updateAssignment() {

    }

    @Test
    public void deleteAssignment() {

    }

    @Test
    public void getAssignmentById() {

    }

    @Test
    public void findAllAssignments() {

    }

    @Test
    public void findAssignmentsForAgent() {

    }

    @Test
    public void findAssignmentsForMission() {

    }
    
    private static Assignment newAssignment(Agent agent, Mission mission, Date startDate, Date endDate) {
        Assignment assignment= new Assignment();
        assignment.setAgent(agent);
        assignment.setMission(mission);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        return assignment;
    }
    
    private void assertDeepEqauls(List<Assignment> expectedList, List<Assignment> actualList) {
        for(int i=0; i<expectedList.size(); i++) {
            Assignment expected= expectedList.get(i);
            Assignment actual= actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Assignment expected, Assignment actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAgent(), actual.getAgent());
        assertEquals(expected.getMission(), actual.getMission());
        assertEquals(expected.getStartDate(), actual.getStartDate());
        assertEquals(expected.getEndDate(), actual.getEndDate());
    }
    
}
