/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.model;

import backend.entities.Agent;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matej Majdis
 */
public class AgentsTableModel extends AbstractTableModel {
    private List<Agent> agents = new ArrayList<>();

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
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("COLUMNINDEX"));
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Agent agent = agents.get(rowIndex);
        switch (columnIndex) {
            /*case 0:
             return agent.getId();*/
            case 0:
                agent.setNickName(aValue.toString());
                break;
            case 1:
                agent.setAge((Integer) aValue);
                break;
            case 2:
                agent.setPhoneNumber(aValue.toString());
                break;
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("COLUMNINDEX"));
        }
        
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            /*case 0:
             return "ID";*/
            case 0:
                return java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("NICKNAME");
            case 1:
                return java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("AGE");
            case 2:
                return java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("PHONE NUMBER");
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("COLUMNINDEX"));
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
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("COLUMNINDEX"));
        }
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
        int lastRow = agents.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void removeAgent(Agent agent) {
        int row = agents.indexOf(agent);
        agents.remove(agent);
        fireTableRowsDeleted(row, row);
    }
    
    public Agent getAgent(int index) {
        return agents.get(index);
    }
}
