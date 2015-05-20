/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.model;

import backend.entities.Mission;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Matej Majdis
 */
public class MissionsTableModel extends AbstractTableModel {
    List<Mission> missions = new ArrayList<>();
    
    @Override
    public int getRowCount() {
        return missions.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return mission.getCodeName();
            case 1:
                return mission.getObjective();
            case 2:
                return mission.getLocation();
            case 3:
                return mission.getNotes();
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/MissionsTableModel").getString("COLUMNINDEX"));
        }
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        switch (columnIndex) {
            /*case 0:
                return agent.getId();*/
            case 0:
                mission.setCodeName(aValue.toString());
                break;
            case 1:
                mission.setObjective(aValue.toString());
                break;
            case 2:
                mission.setLocation(aValue.toString());
                break;
            case 3:
                mission.setNotes(aValue.toString());
                break;
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/MissionsTableModel").getString("COLUMNINDEX"));
        }
        
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch(columnIndex) {
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
                return java.util.ResourceBundle.getBundle("frontend/model/MissionsTableModel").getString("CODENAME");
            case 1:
                return java.util.ResourceBundle.getBundle("frontend/model/MissionsTableModel").getString("OBJECTIVE");
            case 2:
                return java.util.ResourceBundle.getBundle("frontend/model/MissionsTableModel").getString("LOCATION");
            case 3:
                return java.util.ResourceBundle.getBundle("frontend/model/MissionsTableModel").getString("NOTES");
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/MissionsTableModel").getString("COLUMNINDEX"));
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
                return String.class;
            default:
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("frontend/model/MissionsTableModel").getString("COLUMNINDEX"));
        }
    }

    public void addMission(Mission mission) {
        missions.add(mission);
        int lastRow = missions.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    
    public void removeMission(Mission mission) {
        int row = missions.indexOf(mission);
        missions.remove(mission);
        fireTableRowsDeleted(row, row);
    }
    
    public Mission getMission(int index) {
        return missions.get(index);
    }
}
