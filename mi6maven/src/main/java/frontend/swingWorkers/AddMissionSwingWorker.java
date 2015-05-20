/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.swingWorkers;

import backend.entities.Mission;
import frontend.model.MissionsTableModel;
import javax.swing.SwingWorker;


/**
 *
 * @author mato
 */
public class AddMissionSwingWorker extends SwingWorker<Void, Void>{
    
    private MissionsTableModel model;
    private Mission mission;
    
    public AddMissionSwingWorker(MissionsTableModel model, Mission mission) {
        this.mission= mission;
        this.model = model;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        model.addMission(mission);
        
        return null;
    }
    
}
