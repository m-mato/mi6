/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import common.DBUtils;
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
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;

/**
 *
 * @author Matej Majdis
 */
public class AssignmentManagerImplITest {

    private AssignmentManagerImpl manager;
    private DataSource ds;

    public static final Date NOW = Date.from(Instant.now());
    public static final Date EPOCH = Date.from(Instant.EPOCH);

    private static DataSource prepareDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:mi6testDB;create=true");

        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        manager = new AssignmentManagerImpl();
        manager.setDataSource(ds);

        DBUtils.executeSqlScript(ds,getClass().getResource("/createTables.sql"));
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds, getClass().getResource("/dropTables.sql"));
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
    
    @Test(expected=IllegalArgumentException.class)
    public void createNullAssignment() {
        manager.createAssignment(null);
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
        Agent agent = newAgent(39, "Devil", "555245845");
        Agent anotherAgent = newAgent(49, "Shadow", "785214666");
        Mission mission = newMission("Operation Anaconda",
                "Kill the Anaconda",
                "Czech republic, Brno",
                "It is 20 meters Long");
        Mission anotherMission = newMission("Operation Monkey",
                "Save the Monkey",
                "Slovak republic",
                null);
        Date anotherDate = Date.from(Instant.now());

        Assignment assignment = newAssignment(agent, mission, EPOCH, null);
        Assignment a2 = newAssignment(new Agent(), new Mission(), EPOCH, NOW);

        manager.createAssignment(assignment);
        manager.createAssignment(a2);
        Long assignmentId = assignment.getId();

        assignment = manager.getAssignmentById(assignmentId);
        assignment.setAgent(anotherAgent);
        manager.updateAssignment(assignment);
        assertEquals(anotherAgent, assignment.getAgent());
        assertEquals(mission, assignment.getMission());
        assertEquals(EPOCH, assignment.getStartDate());
        assertNull(assignment.getEndDate());

        assignment = manager.getAssignmentById(assignmentId);
        assignment.setMission(anotherMission);
        manager.updateAssignment(assignment);
        assertEquals(anotherAgent, assignment.getAgent());
        assertEquals(anotherMission, assignment.getMission());
        assertEquals(EPOCH, assignment.getStartDate());
        assertNull(assignment.getEndDate());

        assignment = manager.getAssignmentById(assignmentId);
        assignment.setStartDate(NOW);
        manager.updateAssignment(assignment);
        assertEquals(anotherAgent, assignment.getAgent());
        assertEquals(anotherMission, assignment.getMission());
        assertEquals(NOW, assignment.getStartDate());
        assertNull(assignment.getEndDate());

        assignment = manager.getAssignmentById(assignmentId);
        assignment.setEndDate(anotherDate);
        manager.updateAssignment(assignment);
        assertEquals(anotherAgent, assignment.getAgent());
        assertEquals(anotherMission, assignment.getMission());
        assertEquals(NOW, assignment.getStartDate());
        assertEquals(anotherDate, assignment.getEndDate());

        AssignmentManagerImplITest.this.assertDeepEquals(a2, manager.getAssignmentById(a2.getId()));
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
        manager.updateAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAssignmentWithNullAgent() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), EPOCH, null);
        manager.createAssignment(assignment);

        assignment.setAgent(null);
        manager.updateAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAssignmentWithNullMission() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), EPOCH, null);
        manager.createAssignment(assignment);

        assignment.setMission(null);
        manager.updateAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAssignmentWithNullStartDate() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), EPOCH, null);
        manager.createAssignment(assignment);

        assignment.setStartDate(null);
        manager.updateAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAssignmentFromFulfilledToActive() {
        Assignment assignment = newAssignment(new Agent(), new Mission(), EPOCH, NOW);
        manager.createAssignment(assignment);

        assignment.setEndDate(null);
        manager.updateAssignment(assignment);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void deleteNullAssignment() {
        manager.deleteAssignment(null);
    }
    
    @Test
    public void deleteAssignment() {
        Assignment assignment1 = newAssignment(new Agent(), new Mission(), EPOCH, null);
        Assignment assignment2 = newAssignment(new Agent(), new Mission(), EPOCH, NOW);
        Assignment assignment3 = newAssignment(new Agent(), new Mission(), EPOCH, NOW);

        List<Assignment> expected = Arrays.asList(assignment1, assignment2, assignment3);
        List<Assignment> actual = manager.findAllAssignments();

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);

        manager.deleteAssignment(assignment2);

        expected = Arrays.asList(assignment1, assignment3);
        actual = manager.findAllAssignments();

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getAssignmentByNullId() {
        Assignment assignment = manager.getAssignmentById(null);
    }

    @Test
    public void getAssignmentById() {
        Agent agent = newAgent(39, "Devil", "555245845");
        Agent anotherAgent = newAgent(49, "Shadow", "785214666");
        Mission mission = newMission("Operation Anaconda",
                "Kill the Anaconda",
                "Czech republic, Brno",
                "It is 20 meters Long");
        Mission anotherMission = newMission("Operation Monkey",
                "Save the Monkey",
                "Slovak republic",
                null);

        Assignment assignment = newAssignment(agent, mission, EPOCH, null);
        Assignment assignment2 = newAssignment(new Agent(), new Mission(), EPOCH, NOW);

        manager.createAssignment(assignment);
        manager.createAssignment(assignment2);

        Assignment assignmentFromDB = manager.getAssignmentById(assignment.getId());
        Assignment assignmentFromDB2 = manager.getAssignmentById(assignment2.getId());

        assertEquals(assignment, assignmentFromDB);
        assertEquals(assignment2, assignmentFromDB2);
        AssignmentManagerImplITest.this.assertDeepEquals(assignment, assignmentFromDB);
        AssignmentManagerImplITest.this.assertDeepEquals(assignment2, assignmentFromDB2);
    }

    @Test
    public void findAllAssignments() {
        assertTrue(manager.findAllAssignments().isEmpty());

        Assignment a1 = newAssignment(new Agent(), new Mission(), EPOCH, null);
        Assignment a2 = newAssignment(new Agent(), new Mission(), EPOCH, NOW);

        manager.createAssignment(a1);
        manager.createAssignment(a2);

        List<Assignment> expected = Arrays.asList(a1, a2);
        List<Assignment> actual = manager.findAllAssignments();

        Collections.sort(expected, idComparator);
        Collections.sort(actual, idComparator);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);

    }
    
    @Test(expected=IllegalArgumentException.class)
    public void findAssignmentsForNullAgent() {
        manager.findAssignmentsForAgent(null);
    }

    @Test
    public void findAssignmentsForAgent() {
        assertTrue(manager.findAllAssignments().isEmpty());

        Agent agent = new Agent();
        agent.setId(190L);

        Assignment assignment1 = newAssignment(agent, new Mission(), EPOCH, null);
        Assignment assignment2 = newAssignment(agent, new Mission(), EPOCH, null);
        Assignment assignment3 = newAssignment(new Agent(), new Mission(), EPOCH, NOW);
        Assignment assignment4 = newAssignment(agent, new Mission(), EPOCH, NOW);

        List<Assignment> assignmentsFromDB = manager.findAssignmentsForAgent(agent);
        List<Assignment> actualAssignments = Arrays.asList(assignment1, assignment2, assignment4);

        Collections.sort(assignmentsFromDB, idComparator);
        Collections.sort(actualAssignments, idComparator);

        assertEquals(assignmentsFromDB, actualAssignments);
        assertDeepEquals(assignmentsFromDB, actualAssignments);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void findAssignmentsForNullMission() {
        manager.findAssignmentsForMission(null);
    }

    @Test
    public void findAssignmentsForMission() {
        assertTrue(manager.findAllAssignments().isEmpty());

        Mission mission = new Mission();
        mission.setId(450L);

        Assignment assignment1 = newAssignment(new Agent(), new Mission(), EPOCH, null);
        Assignment assignment2 = newAssignment(new Agent(), mission, EPOCH, null);
        Assignment assignment3 = newAssignment(new Agent(), new Mission(), EPOCH, NOW);
        Assignment assignment4 = newAssignment(new Agent(), mission, EPOCH, NOW);

        List<Assignment> assignmentsFromDB = manager.findAssignmentsForMission(mission);
        List<Assignment> actualAssignments = Arrays.asList(assignment2, assignment4);

        Collections.sort(assignmentsFromDB, idComparator);
        Collections.sort(actualAssignments, idComparator);

        assertEquals(assignmentsFromDB, actualAssignments);
        assertDeepEquals(assignmentsFromDB, actualAssignments);
    }

    private static Assignment newAssignment(Agent agent, Mission mission, Date startDate, Date endDate) {
        Assignment assignment = new Assignment();
        assignment.setAgent(agent);
        assignment.setMission(mission);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);

        return assignment;
    }

    public Agent newAgent(int age, String nickName, String phoneNumber) {
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
        AssignmentManagerImplITest.this.assertDeepEquals(result, assignment);
    }

    private void assertDeepEquals(List<Assignment> expectedList, List<Assignment> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Assignment expected = expectedList.get(i);
            Assignment actual = actualList.get(i);
            AssignmentManagerImplITest.this.assertDeepEquals(expected, actual);
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
