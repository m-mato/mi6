/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.model;

import backend.entities.Mission;
import backend.managers.MissionManager;
import backend.managers.MissionManagerImpl;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Matej Majdis
 */
public class MissionsTableModel extends AbstractTableModel {

    MissionManager missionManager;
    List<Mission> missions;

    public MissionsTableModel() {
        super();
        missionManager = new MissionManagerImpl();
        ((MissionManagerImpl)missionManager).setDefaultDataSource();
        missions = missionManager.findAllMissions();
    }

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
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Codename";
            case 1:
                return "Objective";
            case 2:
                return "Location";
            case 3:
                return "Notes";
            default:
                throw new IllegalArgumentException("columnIndex");
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
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addMission(Mission mission) {
        missionManager.createMission(mission);
        missions.add(mission);
        int lastRow = missions.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
}
