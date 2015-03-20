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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Matej Majdis
 */
public class AssignmentManagerImplITest {

    private AssignmentManagerImpl manager;
    
    public static final Date NOW= Date.from(Instant.now());
    public static final Date EPOCH= Date.from(Instant.EPOCH);

    @Before
    public void setUp() throws SQLException {
        this.manager = new AssignmentManagerImpl();
    }

    @Test
    public void createAssignment() {
        Assignment activeAssignment = newAssignment(new Agent(), new Mission(), EPOCH, null);
        Assignment fulfilledAssignment = newAssignment(new Agent(), new Mission(), EPOCH, NOW);

        manager.createAssignment(activeAssignment);
        manager.createAssignment(fulfilledAssignment);

        checkCreatedAssignment(activeAssignment);
        checkCreatedAssignment(fulfilledAssignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAssignmentWithNullAgent() {
        Assignment assignment = newAssignment(null, new Mission(), EPOCH, null);
        manager.createAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAssignmentWithNullMission() {
        Assignment assignment = newAssignment(new Agent(), null, EPOCH, null);
        manager.createAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAssignmentWithNullStartDate() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), null, null);
        manager.createAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAssignmentWrongDatesOrder() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), NOW, EPOCH);
        manager.createAssignment(assignment);
    }

    @Test
    public void updateAssignment() {              
        Agent agent= newAgent(39, "Devil", "555245845");
        Agent anotherAgent= newAgent(49, "Shadow", "785214666");
        Mission mission= newMission("Operation Anaconda",
                                    "Kill the Anaconda",
                                    "Czech republic, Brno",
                                    "It is 20 meters Long");
        Mission anotherMission= newMission("Operation Monkey",
                                           "Save the Monkey",
                                           "Slovak republic",
                                            null);       
        Date anotherDate= Date.from(Instant.now());
        
        
        Assignment assignment = newAssignment(agent, mission, EPOCH, null);
        Assignment a2 = newAssignment(new Agent(), new Mission(), EPOCH, NOW);
        
        manager.createAssignment(assignment);
        manager.createAssignment(a2);
        Long assignmentId= assignment.getId();
        
        assignment= manager.getAssignmentById(assignmentId);
        assignment.setAgent(anotherAgent);
        manager.updateAssignment(assignment);
        assertEquals(anotherAgent, assignment.getAgent());
        assertEquals(mission, assignment.getMission());
        assertEquals(EPOCH, assignment.getStartDate());
        assertNull(assignment.getEndDate());
        
        assignment= manager.getAssignmentById(assignmentId);
        assignment.setMission(anotherMission);
        manager.updateAssignment(assignment);
        assertEquals(anotherAgent, assignment.getAgent());
        assertEquals(anotherMission, assignment.getMission());
        assertEquals(EPOCH, assignment.getStartDate());
        assertNull(assignment.getEndDate());
        
        assignment= manager.getAssignmentById(assignmentId);
        assignment.setStartDate(NOW);
        manager.updateAssignment(assignment);
        assertEquals(anotherAgent, assignment.getAgent());
        assertEquals(anotherMission, assignment.getMission());
        assertEquals(NOW, assignment.getStartDate());
        assertNull(assignment.getEndDate());
        
        assignment= manager.getAssignmentById(assignmentId);
        assignment.setEndDate(anotherDate);
        manager.updateAssignment(assignment);
        assertEquals(anotherAgent, assignment.getAgent());
        assertEquals(anotherMission, assignment.getMission());
        assertEquals(NOW, assignment.getStartDate());
        assertEquals(anotherDate, assignment.getEndDate());
        
        assertDeepEquals(a2, manager.getAssignmentById(a2.getId()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateNullAssignment() {
        manager.updateAssignment(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateAssignmentWithNullId() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), EPOCH, null);
        manager.createAssignment(assignment);
        
        assignment.setId(null);    
        manager.updateAssignment(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateAssignmentWithNullAgent() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), EPOCH, null);
        manager.createAssignment(assignment);
        
        assignment.setAgent(null);    
        manager.updateAssignment(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateAssignmentWithNullMission() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), EPOCH, null);
        manager.createAssignment(assignment);
        
        assignment.setMission(null);    
        manager.updateAssignment(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateAssignmentWithNullStartDate() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), EPOCH, null);
        manager.createAssignment(assignment);
        
        assignment.setStartDate(null);    
        manager.updateAssignment(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateAssignmentFromFulfilledToActive() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), EPOCH, NOW);
        manager.createAssignment(assignment);
        
        assignment.setEndDate(null);    
        manager.updateAssignment(null);
    }

    @Test
    public void deleteAssignment() {

    }

    @Test
    public void getAssignmentById() {

    }

    @Test
    public void findAllAssignments() {
        assertTrue(manager.findAllAssignments().isEmpty());
        
        Assignment a1 = newAssignment(new Agent(), new Mission(), EPOCH, null);
        Assignment a2 = newAssignment(new Agent(), new Mission(), EPOCH, NOW);

        manager.createAssignment(a1);
        manager.createAssignment(a2);
        
        List<Assignment> expected= Arrays.asList(a1, a2);
        List<Assignment> actual= manager.findAllAssignments();
        
        Collections.sort(expected, idComparator);
        Collections.sort(actual, idComparator);
        
        assertEquals(expected, actual);
        assertDeepEqauls(expected, actual);
        
    }

    @Test
    public void findAssignmentsForAgent() {

    }

    @Test
    public void findAssignmentsForMission() {

    }

    private static Assignment newAssignment(Agent agent, Mission mission, Date startDate, Date endDate) {
        Assignment assignment = new Assignment();
        assignment.setAgent(agent);
        assignment.setMission(mission);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        
        return assignment;
    }
    
    public Agent newAgent(int age, String nickName, String phoneNumber)
    {
        Agent newAgent = new Agent();
        newAgent.setAge(age);
        newAgent.setNickName(nickName);
        newAgent.setPhoneNumber(phoneNumber);
        
        return newAgent;
    }
    
    private Mission newMission(String codeName, String objective, String location, String notes) {
        Mission mission = new Mission();
        mission.setCodeName(codeName);
        mission.setObjective(objective);
        mission.setLocation(location);
        mission.setNotes(notes);
        
        return mission;
    }

    private void checkCreatedAssignment(Assignment assignment) {
        Long assignmentId = assignment.getId();
        assertNotNull(assignmentId);

        Assignment result = manager.getAssignmentById(assignmentId);
        assertEquals(result, assignment);
        assertNotSame(result, assignment);
        assertDeepEquals(result, assignment);
    }

    private void assertDeepEqauls(List<Assignment> expectedList, List<Assignment> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Assignment expected = expectedList.get(i);
            Assignment actual = actualList.get(i);
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

    private static Comparator<Assignment> idComparator = new Comparator<Assignment>() {

        @Override
        public int compare(Assignment o1, Assignment o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
    };

}
