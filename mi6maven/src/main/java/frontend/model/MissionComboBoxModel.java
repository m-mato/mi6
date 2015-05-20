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
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Matej Majdis
 */
public class MissionComboBoxModel extends AbstractListModel implements ComboBoxModel {

    Mission[] missions;
    Mission selectedMission;
    
    public MissionComboBoxModel() {
        super();
        MissionManager manager = new MissionManagerImpl();
        ((MissionManagerImpl)manager).setDefaultDataSource();
        List<Mission> agList = manager.findAllMissions();
        this.missions = agList.toArray(new Mission[agList.size()]);
        selectedMission = null;
    }
    
    @Override
    public int getSize() {
        return missions.length;
    }

    @Override
    public Object getElementAt(int index) {
        return missions[index];
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedMission = (Mission) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedMission;
    }
    
}
