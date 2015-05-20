/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.swingWorkers;

import backend.entities.Agent;
import frontend.model.AgentsTableModel;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Andrej Halaj
 */
public class DeleteAgentSwingWorker extends SwingWorker<Void, Void> {
    private List<Agent> agents;
    private AgentsTableModel model;

    public DeleteAgentSwingWorker(AgentsTableModel model, List<Agent> agents) {
        this.agents = agents;
        this.model = model;
    }

    @Override
    protected Void doInBackground() throws Exception {
        for (Agent agent : agents) {
            model.removeAgent(agent);
        }
        return null;
    }
}
