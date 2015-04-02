/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.managers;

import backend.entities.Agent;
import java.util.List;

/**
 *
 * @author Andrej Halaj
 * @author Matej Majdis
 */
public interface AgentManager {
    void createAgent(Agent agent);
    void updateAgent(Agent agent);
    void deleteAgent(Agent agent);
    Agent getAgentById(Long id);
    List<Agent> findAllAgents();
}
