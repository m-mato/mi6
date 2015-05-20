/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.swingWorkers;

import backend.entities.Assignment;
import frontend.model.AssignmentsTableModel;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Andrej Halaj
 */
public class DeleteAssignmentSwingWorker extends SwingWorker<Void, Void> {
    private List<Assignment> assignments;
    private AssignmentsTableModel model;

    public DeleteAssignmentSwingWorker(AssignmentsTableModel model, List<Assignment> assignments) {
        this.assignments = assignments;
        this.model = model;
    }

    @Override
    protected Void doInBackground() throws Exception {
        for (Assignment assignment : assignments) {
            model.removeAssignment(assignment);
        }
        return null;
    }
}
