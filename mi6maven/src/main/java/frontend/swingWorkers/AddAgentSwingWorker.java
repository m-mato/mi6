/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.swingWorkers;

import backend.entities.Agent;
import frontend.model.AgentsTableModel;
import javax.swing.SwingWorker;

/**
 *
 * @author Matej Majdis
 */
public class AddAgentSwingWorker extends SwingWorker<Void, Void>{
   
    private AgentsTableModel model;
    private Agent agent;
    
    public AddAgentSwingWorker(AgentsTableModel model, Agent agent) {
        this.agent= agent;
        this.model = model;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        model.addAgent(agent);
        
        return null;
    }
    
}
