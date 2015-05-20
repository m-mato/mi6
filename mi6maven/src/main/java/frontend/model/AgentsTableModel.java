/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.model;

import backend.entities.Agent;
import backend.managers.AgentManager;
import javax.swing.table.AbstractTableModel;
import backend.managers.AgentManagerImpl;
import java.util.List;


/**
 *
 * @author Matej Majdis
 */
public class AgentsTableModel extends AbstractTableModel {
    
    AgentManager agentManager;
    private List<Agent> agents;

    public AgentsTableModel() {
        super();
        agentManager = new AgentManagerImpl();
        ((AgentManagerImpl)agentManager).setDefaultDataSource();
        agents = agentManager.findAllAgents();
    }

    @Override
    public int getRowCount() {
        return agents.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Agent agent = agents.get(rowIndex);
        switch (columnIndex) {
            /*case 0:
                return agent.getId();*/
            case 0:
                return agent.getNickName();
            case 1:
                return agent.getAge();
            case 2:
                return agent.getPhoneNumber();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            /*case 0:
                return "ID";*/
            case 0:
                return "Nickname";
            case 1:
                return "Age";
            case 2:
                return "Phone number";
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            /*case 0:
                return Long.class;*/
            case 1:
                return Integer.class;
            case 0:
            case 2:
                return String.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addAgent(Agent agent) {
        agentManager.createAgent(agent);
        agents.add(agent);
        int lastRow = agents.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

}
