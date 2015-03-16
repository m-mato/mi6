/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Agent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrej Halaj
 */
public class AgentManagerImplTest {
    
    private AgentManagerImpl agentManager;
    
    @Before
    public void setUp() {
        agentManager = new AgentManagerImpl();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullAgent() {
        agentManager.createAgent(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateAgentWithWrongAge() {
        Agent agent = newAgent(-3, "Devil", "555245845");
        agentManager.createAgent(agent);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateAgentWithWrongNickName() {
        Agent agent = newAgent(32, null, "555245845");
        agentManager.createAgent(agent);
    }
    
    @Test
    public void testCreateAgentWithRightArguments() {
        Agent agent = newAgent(24, "Shadow", "555324846");
        agentManager.createAgent(agent);
        Long agentId = agent.getId();
        
        Agent agentFromDatabase = agentManager.getAgentById(agentId);
        assertEquals(agent, agentFromDatabase);
        assertNotSame(agent, agentFromDatabase);
        assertDeepEquals(agent, agentFromDatabase);
        
        agent = newAgent(32, "Devil", null); // doesn't need to have a phone number
        agentManager.createAgent(agent);
        Agent result = agentManager.getAgentById(agent.getId());
        assertNotNull(result);
        
        agent = newAgent(21, "Devil", "555215485");
        agentManager.createAgent(agent);
        result = agentManager.getAgentById(agent.getId());
        assertNotNull(result);
    }
    
    @Test
    public void testUpdateAgentWithRightArguments() {
        Agent agent = newAgent(24, "Shadow", "555324846");
        agentManager.createAgent(agent);
        Long agentId = agent.getId();

        agent = agentManager.getAgentById(agentId);
        agent.setAge(42);
        agentManager.updateAgent(agent);
        assertEquals(42, agent.getAge());
        assertEquals("Shadow", agent.getNickName());
        assertEquals("555324846", agent.getPhoneNumber());
        
        agent = agentManager.getAgentById(agentId);
        agent.setNickName("Blue");
        agentManager.updateAgent(agent);
        assertEquals(42, agent.getAge());
        assertEquals("Blue", agent.getNickName());
        assertEquals("555324846", agent.getPhoneNumber());
        
        agent = agentManager.getAgentById(agentId);
        agent.setPhoneNumber("555846217");
        agentManager.updateAgent(agent);
        assertEquals(42, agent.getAge());
        assertEquals("Blue", agent.getNickName());
        assertEquals("555846217", agent.getPhoneNumber());
        
        agent = agentManager.getAgentById(agentId);
        agent.setPhoneNumber(null);
        agentManager.updateAgent(agent);
        assertEquals(42, agent.getAge());
        assertEquals("Blue", agent.getNickName());
        assertNull(agent.getPhoneNumber());
    }
    
    @Test
    public void testFindAllAgents() {
        assertTrue(agentManager.findAllAgents().isEmpty());
        
        Agent agent1 = newAgent(24, "Shadow", "555324846");
        Agent agent2 = newAgent(32, "Devil", "555245845");
        Agent agent3 = newAgent(28, "Ghost", "555425451");
        
        agentManager.createAgent(agent1);
        agentManager.createAgent(agent2);
        agentManager.createAgent(agent3);
        
        List<Agent> expected = Arrays.asList(agent1, agent2, agent3);
        List<Agent> actual = agentManager.findAllAgents();
        
        Collections.sort(expected, agentIdComparator);
        Collections.sort(actual, agentIdComparator);
        
        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }
    
    public Agent newAgent(int age, String nickName, String phoneNumber)
    {
        Agent newAgent = new Agent();
        newAgent.setAge(age);
        newAgent.setNickName(nickName);
        newAgent.setPhoneNumber(phoneNumber);
        
        return newAgent;
    }
    
    private void assertDeepEquals(List<Agent> expectedList, List<Agent> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Agent expected = expectedList.get(i);
            Agent actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Agent expected, Agent actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAge(), actual.getAge());
        assertEquals(expected.getNickName(), actual.getNickName());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
    }
    
    private static Comparator<Agent> agentIdComparator = new Comparator<Agent>() {

        @Override
        public int compare(Agent o1, Agent o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
    };
    
}
