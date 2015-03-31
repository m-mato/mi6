/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import common.DBUtils;
import entities.Mission;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matej Majdis
 */
public class MissionManagerImplTest {

    private MissionManagerImpl manager;
    private DataSource ds;

    private static DataSource prepareDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:mi6testDB;create=true");

        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        manager = new MissionManagerImpl();
        manager.setDataSource(ds);

        DBUtils.executeSqlScript(ds,getClass().getResource("/createTables.sql"));
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds, getClass().getResource("/dropTables.sql"));
    }

    @Test
    public void createMission() {
        Mission missionWithNote = newMission("Operation Anaconda", "Kill the Anaconda", "Czech republic, Brno", "It is 20 meters Long");
        Mission missionWithNullNote = newMission("Operation Monkey", "Save the Monkey", "Slovak republic", null);

        manager.createMission(missionWithNote);
        manager.createMission(missionWithNullNote);

        checkCreatedMission(missionWithNote);
        checkCreatedMission(missionWithNullNote);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createNullMission() {
        manager.createMission(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMissionWithNullCodeName() {
        Mission mission = newMission(null, "Kill the Anaconda", "Czech republicm, Brno", "It is 20 meters Long");
        manager.createMission(mission);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMissionWithNullObjective() {
        Mission mission = newMission("Operation Anaconda", null, "Czech republic, Brno", "It is 20 meters Long");
        manager.createMission(mission);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMissionWithNullLocation() {
        Mission mission = newMission("Operation Anaconda", "Kill the Anaconda", null, "It is 20 meters Long");
        manager.createMission(mission);
    }

    @Test
    public void updateMission() {
        Mission mission = newMission("Operation Anaconda", "Kill the Anaconda", "Czech republic, Brno", "It is 20 meters Long");
        Mission m2 = newMission("Operation Monkey", "Save the Monkey", "Slovak republic", null);
        manager.createMission(mission);
        manager.createMission(m2);
        Long missionId = mission.getId();

        mission = manager.getMissionById(missionId);
        mission.setCodeName("Another CodeName");
        manager.updateMission(mission);
        assertEquals("Another CodeName", mission.getCodeName());
        assertEquals("Kill the Anaconda", mission.getObjective());
        assertEquals("Czech republic, Brno", mission.getLocation());
        assertEquals("It is 20 meters Long", mission.getNotes());

        mission = manager.getMissionById(missionId);
        mission.setObjective("Another Objective");
        manager.updateMission(mission);
        assertEquals("Another CodeName", mission.getCodeName());
        assertEquals("Another Objective", mission.getObjective());
        assertEquals("Czech republic, Brno", mission.getLocation());
        assertEquals("It is 20 meters Long", mission.getNotes());

        mission = manager.getMissionById(missionId);
        mission.setLocation("Another Location");
        manager.updateMission(mission);
        assertEquals("Another CodeName", mission.getCodeName());
        assertEquals("Another Objective", mission.getObjective());
        assertEquals("Another Location", mission.getLocation());
        assertEquals("It is 20 meters Long", mission.getNotes());

        mission = manager.getMissionById(missionId);
        mission.setNotes("Another Notes");
        manager.updateMission(mission);
        assertEquals("Another CodeName", mission.getCodeName());
        assertEquals("Another Objective", mission.getObjective());
        assertEquals("Another Location", mission.getLocation());
        assertEquals("Another Notes", mission.getNotes());

        mission = manager.getMissionById(missionId);
        mission.setNotes(null);
        manager.updateMission(mission);
        assertEquals("Another CodeName", mission.getCodeName());
        assertEquals("Another Objective", mission.getObjective());
        assertEquals("Another Location", mission.getLocation());
        assertNull(mission.getNotes());

        assertDeepEquals(m2, manager.getMissionById(m2.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullMission() {
        manager.updateMission(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateMissionWithNullCodeName() {
        Mission mission = newMission("Operation Anaconda", "Kill the Anaconda", "Czech republic, Brno", "It is 20 meters Long");
        manager.createMission(mission);

        mission.setCodeName(null);
        manager.updateMission(mission);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateMissionWithNullObjective() {
        Mission mission = newMission("Operation Anaconda", "Kill the Anaconda", "Czech republic, Brno", "It is 20 meters Long");
        manager.createMission(mission);

        mission.setObjective(null);
        manager.updateMission(mission);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateMissionWithNullLocation() {
        Mission mission = newMission("Operation Anaconda", "Kill the Anaconda", "Czech republic, Brno", "It is 20 meters Long");
        manager.createMission(mission);

        mission.setLocation(null);
        manager.updateMission(mission);
    }

    @Test
    public void deleteMission() {
        Mission m1 = newMission("Operation Anaconda", "Kill the Anaconda", "Czech republic, Brno", "It is 20 meters Long");
        Mission m2 = newMission("Operation Monkey", "Save the Monkey", "Slovak republic", null);
        manager.createMission(m1);
        manager.createMission(m2);

        assertNotNull(manager.getMissionById(m1.getId()));
        assertNotNull(manager.getMissionById(m2.getId()));

        manager.deleteMission(m1);

        assertNull(manager.getMissionById(m1.getId()));
        assertNotNull(manager.getMissionById(m2.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullMission() {
        manager.deleteMission(null);
    }

    @Test
    public void getMissionById() {
        assertNull(manager.getMissionById(1l));

        Mission mission = newMission("Operation Anaconda", "Kill the Anaconda", "Czech republic, Brno", "It is 20 meters Long");
        manager.createMission(mission);
        Long missionId = mission.getId();

        Mission result = manager.getMissionById(missionId);
        assertEquals(mission, result);
        assertDeepEquals(mission, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMissionByNullId() {
        manager.getMissionById(null);
    }

    @Test
    public void findAllMissions() {
        assertTrue(manager.findAllMissions().isEmpty());

        Mission g1 = newMission("Operation Anaconda", "Kill the Anaconda", "Czech republic, Brno", "It is 20 meters Long");
        Mission g2 = newMission("Operation Monkey", "Save the Monkey", "Slovak republic", null);

        manager.createMission(g1);
        manager.createMission(g2);

        List<Mission> expected = Arrays.asList(g1, g2);
        List<Mission> actual = manager.findAllMissions();

        Collections.sort(actual, idComparator);
        Collections.sort(expected, idComparator);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }

    private static Mission newMission(String codeName, String objective, String location, String notes) {
        Mission mission = new Mission();
        mission.setCodeName(codeName);
        mission.setObjective(objective);
        mission.setLocation(location);
        mission.setNotes(notes);

        return mission;
    }

    private void checkCreatedMission(Mission mission) {
        Long missionId = mission.getId();
        assertNotNull(missionId);

        Mission result = manager.getMissionById(missionId);
        assertEquals(mission, result);
        assertNotSame(mission, result);
        assertDeepEquals(mission, result);
    }

    private void assertDeepEquals(List<Mission> expectedList, List<Mission> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Mission expected = expectedList.get(i);
            Mission actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Mission expected, Mission actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCodeName(), actual.getCodeName());
        assertEquals(expected.getObjective(), actual.getObjective());
        assertEquals(expected.getLocation(), actual.getLocation());
        assertEquals(expected.getNotes(), actual.getNotes());
    }

    private static Comparator<Mission> idComparator = new Comparator<Mission>() {

        @Override
        public int compare(Mission o1, Mission o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
    };

}
