/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.swingWorkers;

import backend.entities.Mission;
import frontend.model.MissionsTableModel;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Andrej Halaj
 */
public class DeleteMissionSwingWorker extends SwingWorker<Void, Void> {
    private List<Mission> missions;
    private MissionsTableModel model;

    public DeleteMissionSwingWorker(MissionsTableModel model, List<Mission> missions) {
        this.missions = missions;
        this.model = model;
    }

    @Override
    protected Void doInBackground() throws Exception {
        for (Mission mission : missions) {
            model.removeMission(mission);
        }
        return null;
    }
    
}
