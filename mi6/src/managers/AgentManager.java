/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Agent;
import java.util.List;

/**
 *
 * @author Andrej Halaj
 */
public interface AgentManager {
    void createAgent(Agent agent);
    void updateAgent(Agent agent);
    void deleteAgent(Agent agent);
    Agent getAgentById(long id);
    List<Agent> findAllAgents();
}
