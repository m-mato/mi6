/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.model;

import backend.entities.Agent;
import backend.managers.AgentManager;
import backend.managers.AgentManagerImpl;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Matej Majdis
 */
public class AgentComboBoxModel extends AbstractListModel implements ComboBoxModel {

    Agent[] agents;
    Agent selectedAgent;
    
    public AgentComboBoxModel() {
        super();
        AgentManager manager = new AgentManagerImpl();
        ((AgentManagerImpl)manager).setDefaultDataSource();
        List<Agent> agList = manager.findAllAgents();
        this.agents = agList.toArray(new Agent[agList.size()]);
        selectedAgent = null;
    }
    
    @Override
    public int getSize() {
        return agents.length;
    }

    @Override
    public Object getElementAt(int index) {
        return agents[index];
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedAgent = (Agent) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedAgent;
    }
    
}
