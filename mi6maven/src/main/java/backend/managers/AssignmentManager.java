/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.managers;

import backend.entities.Agent;
import backend.entities.Assignment;
import backend.entities.Mission;
import java.util.List;

/**
 *
 * @author Andrej Halaj
 * @author Matej Majdis
 */
public interface AssignmentManager {
    void createAssignment(Assignment assignment);
    void updateAssignment(Assignment assignment);
    void deleteAssignment(Assignment assignment);
    Assignment getAssignmentById(Long id);
    List<Assignment> findAllAssignments();
    List<Assignment> findAssignmentsForAgent(Agent agent);
    List<Assignment> findAssignmentsForMission(Mission mission);
}
