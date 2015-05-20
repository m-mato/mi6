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
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Agent";
            case 1:
                return "Mission";
            case 2:
                return "Start Date";
            case 3:
                return "End Date";
            default:
                throw new IllegalArgumentException("columnIndex");
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
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addAssignment(Assignment assignment) {
        assignmentManager.createAssignment(assignment);
        assignments.add(assignment);
        int lastRow = assignments.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

}
