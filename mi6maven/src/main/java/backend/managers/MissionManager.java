/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.managers;

import backend.entities.Mission;
import java.util.List;

/**
 *
 * @author Andrej Halaj
 * @author Matej Majdis
 */
public interface MissionManager {
    void createMission(Mission mission);
    void updateMission(Mission mission);
    void deleteMission(Mission mission);
    Mission getMissionById(Long id); 
    List<Mission> findAllMissions();
    List<Mission> findMissionsWithoutAgent();
}
