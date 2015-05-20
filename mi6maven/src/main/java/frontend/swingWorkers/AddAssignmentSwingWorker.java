/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.swingWorkers;

import backend.entities.Assignment;
import frontend.model.AssignmentsTableModel;
import javax.swing.SwingWorker;

/**
 *
 * @author mato
 */
public class AddAssignmentSwingWorker extends SwingWorker<Void, Void> {

    private AssignmentsTableModel model;
    private Assignment assignment;

    public AddAssignmentSwingWorker(AssignmentsTableModel model, Assignment assignment) {
        this.assignment = assignment;
        this.model = model;
    }

    @Override
    protected Void doInBackground() throws Exception {
        model.addAssignment(assignment);

        return null;
    }
}
