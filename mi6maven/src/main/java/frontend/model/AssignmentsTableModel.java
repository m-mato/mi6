/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.model;

import backend.entities.Agent;
import backend.entities.Assignment;
import backend.entities.Mission;
import backend.managers.AssignmentManager;
import backend.managers.AssignmentManagerImpl;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Matej Majdis
 */
public class AssignmentsTableModel extends AbstractTableModel {

    AssignmentManager assignmentManager;
    List<Assignment> assignments;
    
    public AssignmentsTableModel() {
        super();
        assignmentManager = new AssignmentManagerImpl();
        ((AssignmentManagerImpl)assignmentManager).setDefaultDataSource();
        assignments = assignmentManager.findAllAssignments();
    }

    @Override
    public int getRowCount() {
        return assignments.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Assignment assignment = assignments.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return assignment.getAgent();
            case 1:
                return assignment.getMission();
            case 2: 
                return assignment.getStartDate();
            case 3: 
                return assignment.getEndDate();
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("COLUMNINDEX"));
        }
    }
    
     @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Assignment assignment = assignments.get(rowIndex);
        switch (columnIndex) {
            /*case 0:
             return agent.getId();*/
            case 0:
                assignment.setAgent((Agent) aValue);
                break;
            case 1:
                assignment.setMission((Mission) aValue);
                break;
            case 2:
                assignment.setStartDate((Date) aValue);
                break;
            case 3:
                assignment.setEndDate((Date) aValue);
                break;
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("COLUMNINDEX"));
        }

        assignmentManager.updateAssignment(assignment);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("AGENT");
            case 1:
                return java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("MISSION");
            case 2:
                return java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("START DATE");
            case 3:
                return java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("END DATE");
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("COLUMNINDEX"));
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Agent.class;
            case 1:
                return Mission.class;
            case 2:
            case 3:
                return Date.class;
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/AssignmentsTableModel").getString("COLUMNINDEX"));
        }
    }

    public void addAssignment(Assignment assignment) {
        assignmentManager.createAssignment(assignment);
        assignments.add(assignment);
        int lastRow = assignments.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    
    public void removeAssignment(Assignment assignment) {
        int row = assignments.indexOf(assignment);
        assignments.remove(assignment);
        assignmentManager.deleteAssignment(assignment);
        fireTableRowsDeleted(row, row);
    }
    
    public Assignment getAssignment(int index) {
        return assignments.get(index);
    }
}
