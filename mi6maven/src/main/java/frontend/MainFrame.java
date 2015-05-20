/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import backend.entities.Agent;
import backend.entities.Assignment;
import backend.entities.Mission;
import backend.managers.AgentManagerImpl;
import backend.managers.AssignmentManagerImpl;
import backend.managers.MissionManagerImpl;
import frontend.model.AgentComboBoxModel;
import frontend.model.AgentsTableModel;
import frontend.model.AssignmentsTableModel;
import frontend.model.MissionComboBoxModel;
import frontend.model.MissionsTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTabbedPane;
import frontend.swingWorkers.AddAgentSwingWorker;
import frontend.swingWorkers.AddAssignmentSwingWorker;
import frontend.swingWorkers.AddMissionSwingWorker;
import frontend.swingWorkers.DeleteAgentSwingWorker;
import frontend.swingWorkers.DeleteAssignmentSwingWorker;
import frontend.swingWorkers.DeleteMissionSwingWorker;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Andrej Halaj
 * @author Matej Majdis
 */
public class MainFrame extends javax.swing.JFrame {
    public static final String CONFIG_FILE = "C:\\Users\\Andrej Halaj\\Documents\\NetBeansProjects\\mi6\\mi6maven\\config\\config.properties";

    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());

    AgentManagerImpl agentManager = new AgentManagerImpl();
    AssignmentManagerImpl assignmentManager = new AssignmentManagerImpl();
    MissionManagerImpl missionManager = new MissionManagerImpl();
    Properties properties = new Properties();
    List<Agent> agents;
    List<Mission> missions;
    List<Assignment> assignments;
    FileHandler fh;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();

        try {
            fh = new FileHandler("C:\\Users\\Andrej Halaj\\Documents\\NetBeansProjects\\mi6\\mi6maven\\log\\Log.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException ex) {
            Logger.getLogger(AssignmentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(AssignmentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        loadProperties();

        assignmentManager.setDefaultDataSource();

        agentManager.setDefaultDataSource();
        agents = agentManager.findAllAgents();
        AgentsTableModel agentsTableModel = new AgentsTableModel();

        for (Agent agent : agents) {
            agentsTableModel.addAgent(agent);
        }

        agentsTable.setModel(agentsTableModel);

        missionManager.setDefaultDataSource();
        missions = missionManager.findAllMissions();
        MissionsTableModel missionsTableModel = new MissionsTableModel();

        for (Mission mission : missions) {
            missionsTableModel.addMission(mission);
        }

        missionsTable.setModel(missionsTableModel);

        switch (entitiesPanel.getSelectedIndex()) {
            case 0:
                showAgentsActions();
                break;
            case 1:
                showMissionsActions();
                break;
            case 2:
                showAssignmentsActions();
                break;
            default:
            //
        }

        filterCheckBox.addItemListener((ItemEvent e) -> {
            if (entitiesPanel.getSelectedIndex() == 0) {
                if (filterCheckBox.isSelected()) {
                    properties.put("filter1selected", "true");
                    listAgents();
                } else {
                    properties.put("filter1selected", "false");
                    AgentsTableModel newModel = new AgentsTableModel();

                    for (Agent agent : agentManager.findAllAgents()) {
                        newModel.addAgent(agent);
                    }

                    agentsTable.setModel(newModel);
                }
            } else if (entitiesPanel.getSelectedIndex() == 1) {
                if (filterCheckBox.isSelected()) {
                    properties.put("filter1selected", "true");
                    properties.put("filter2selected", "false");
                    filterCheckBox2.setSelected(false);
                    listMissions();
                } else {
                    properties.put("filter1selected", "false");
                    MissionsTableModel newModel = new MissionsTableModel();

                    for (Mission mission : missionManager.findAllMissions()) {
                        newModel.addMission(mission);
                    }

                    missionsTable.setModel(newModel);
                }
            }
        });

        filterCheckBox2.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (entitiesPanel.getSelectedIndex() == 1) {
                    if (filterCheckBox2.isSelected()) {
                        properties.put("filter2selected", "true");
                        filterCheckBox.setSelected(false);
                        listMissions();
                    } else {
                        properties.put("filter2selected", "false");
                        MissionsTableModel newModel = new MissionsTableModel();

                        for (Mission mission : missionManager.findAllMissions()) {
                            newModel.addMission(mission);
                        }

                        missionsTable.setModel(newModel);
                    }
                }
            }
        });

        filterComboBox.addItemListener((ItemEvent e) -> {
            properties.put("filter1selected", "false");

            if (filterCheckBox.isSelected()) {
                properties.put("filter1selected", "true");
                if (entitiesPanel.getSelectedIndex() == 0) {
                    listAgents();
                } else if (entitiesPanel.getSelectedIndex() == 1) {
                    listMissions();
                }
            }
        });

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    super.windowClosing(e);
                    
                    File configFile = new File("config.properties");
                    FileWriter writer = new FileWriter(configFile);
                    properties.store(writer, null);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }

    private void listAgents() {
        AgentsTableModel newModel = new AgentsTableModel();
        Mission mission = (Mission) filterComboBox.getSelectedItem();
        List<Assignment> assignments = new ArrayList<>();

        if (mission != null) {
            assignments = assignmentManager.findAssignmentsForMission(mission);
        }

        for (Assignment assignment : assignments) {
            newModel.addAgent(assignment.getAgent());
        }

        agentsTable.setModel(newModel);
    }

    private void listMissions() {
        if (filterCheckBox.isSelected()) {
            MissionsTableModel newModel = new MissionsTableModel();
            Agent agent = (Agent) filterComboBox.getSelectedItem();
            List<Assignment> assignments = new ArrayList<>();

            if (agent != null) {
                assignments = assignmentManager.findAssignmentsForAgent(agent);
            }

            for (Assignment assignment : assignments) {
                newModel.addMission(assignment.getMission());
            }

            missionsTable.setModel(newModel);
        } else if (filterCheckBox2.isSelected()) {
            MissionsTableModel newModel = new MissionsTableModel();
            List<Mission> missions = missionManager.findMissionsWithoutAgent();

            for (Mission mission : missions) {
                newModel.addMission(mission);
            }

            missionsTable.setModel(newModel);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        entitiesPanel = new javax.swing.JTabbedPane();
        agentsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        agentsTable = new javax.swing.JTable();
        missionsPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        missionsTable = new javax.swing.JTable();
        assignmentsPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        assignmentsTable = new javax.swing.JTable();
        deleteEntityButton = new javax.swing.JButton();
        filterCheckBox2 = new javax.swing.JCheckBox();
        filterComboBox = new javax.swing.JComboBox();
        filterCheckBox = new javax.swing.JCheckBox();
        addEntityButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        agentsMenu = new javax.swing.JMenu();
        addAgentMenuItem = new javax.swing.JMenuItem();
        addMissionMenuItem = new javax.swing.JMenuItem();
        addAssignmentMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("frontend/MainFrame"); // NOI18N
        setTitle(bundle.getString("MI6")); // NOI18N

        entitiesPanel.setToolTipText(bundle.getString("")); // NOI18N
        entitiesPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabSwitched(evt);
            }
        });

        agentsTable.setModel(new AgentsTableModel());
        agentsTable.setToolTipText(bundle.getString("")); // NOI18N
        agentsTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(agentsTable);
        if (agentsTable.getColumnModel().getColumnCount() > 0) {
            agentsTable.getColumnModel().getColumn(0).setResizable(false);
            agentsTable.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout agentsPanelLayout = new javax.swing.GroupLayout(agentsPanel);
        agentsPanel.setLayout(agentsPanelLayout);
        agentsPanelLayout.setHorizontalGroup(
            agentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
        );
        agentsPanelLayout.setVerticalGroup(
            agentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, agentsPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        entitiesPanel.addTab("Agents", agentsPanel);

        missionsTable.setModel(new MissionsTableModel());
        missionsTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane2.setViewportView(missionsTable);

        javax.swing.GroupLayout missionsPanelLayout = new javax.swing.GroupLayout(missionsPanel);
        missionsPanel.setLayout(missionsPanelLayout);
        missionsPanelLayout.setHorizontalGroup(
            missionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
        );
        missionsPanelLayout.setVerticalGroup(
            missionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
        );

        entitiesPanel.addTab(bundle.getString("MISSIONS"), missionsPanel); // NOI18N

        assignmentsTable.setModel(new AssignmentsTableModel());
        assignmentsTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane3.setViewportView(assignmentsTable);

        javax.swing.GroupLayout assignmentsPanelLayout = new javax.swing.GroupLayout(assignmentsPanel);
        assignmentsPanel.setLayout(assignmentsPanelLayout);
        assignmentsPanelLayout.setHorizontalGroup(
            assignmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
        );
        assignmentsPanelLayout.setVerticalGroup(
            assignmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
        );

        entitiesPanel.addTab(bundle.getString("ASSIGNMENTS"), assignmentsPanel); // NOI18N

        deleteEntityButton.setText(bundle.getString("DELETE")); // NOI18N
        deleteEntityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEntityButtonActionPerformed(evt);
            }
        });

        filterCheckBox2.setText(bundle.getString("ONLY MISSIONS WITHOUT AGENT")); // NOI18N

        filterComboBox.setModel(new MissionComboBoxModel());
        filterComboBox.setSelectedIndex(0);

        filterCheckBox.setText(bundle.getString("DEFAULT")); // NOI18N

        addEntityButton.setText(bundle.getString("ADD")); // NOI18N
        addEntityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntityButtonActionPerformed(evt);
            }
        });

        agentsMenu.setText(bundle.getString("ADD")); // NOI18N
        agentsMenu.setVisible(false);

        addAgentMenuItem.setText(bundle.getString("AGENT")); // NOI18N
        addAgentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAgentActionPerformed(evt);
            }
        });
        agentsMenu.add(addAgentMenuItem);

        addMissionMenuItem.setText(bundle.getString("MISSION")); // NOI18N
        addMissionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMissionActionPerformed(evt);
            }
        });
        agentsMenu.add(addMissionMenuItem);

        addAssignmentMenuItem.setText(bundle.getString("ASSIGNMENT")); // NOI18N
        addAssignmentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAssignmentActionPerformed(evt);
            }
        });
        agentsMenu.add(addAssignmentMenuItem);

        jMenuBar1.add(agentsMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(entitiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteEntityButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterCheckBox2)
                    .addComponent(filterCheckBox)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addEntityButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(102, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(filterCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(addEntityButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(deleteEntityButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(entitiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAgentActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                addAgentDialogCalled();
            }
        });
    }//GEN-LAST:event_addAgentActionPerformed

    private void addMissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMissionActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                addMissionDialogCalled();
            }
        });
    }//GEN-LAST:event_addMissionActionPerformed

    private void addAssignmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAssignmentActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                addAssignmentDialogCalled();
            }
        });
    }//GEN-LAST:event_addAssignmentActionPerformed

    private void tabSwitched(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabSwitched
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                switch (((JTabbedPane) evt.getSource()).getSelectedIndex()) {
                    case 0:
                        showAgentsActions();
                        break;
                    case 1:
                        showMissionsActions();
                        break;
                    case 2:
                        showAssignmentsActions();
                        break;
                    default:
                    //
                }
            }
        });
    }//GEN-LAST:event_tabSwitched

    private void addEntityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntityButtonActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                switch (MainFrame.this.entitiesPanel.getSelectedIndex()) {
                    case 0:
                        addAgentActionPerformed(evt);
                        break;
                    case 1:
                        addMissionActionPerformed(evt);
                        break;
                    case 2:
                        addAssignmentActionPerformed(evt);
                        break;
                    default:
                    //
                }
            }
        });

    }//GEN-LAST:event_addEntityButtonActionPerformed

    private void deleteEntityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEntityButtonActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                switch (MainFrame.this.entitiesPanel.getSelectedIndex()) {
                    case 0:
                        deleteAgentActionPerformed(evt);
                        break;
                    case 1:
                        deleteMissionActionPerformed(evt);
                        break;
                    case 2:
                        deleteAssignmentActionPerformed(evt);
                        break;
                    default:
                    //
                }
            }

            private void deleteAgentActionPerformed(ActionEvent evt) {
                AgentsTableModel agentsTableModel = (AgentsTableModel) agentsTable.getModel();
                List<Agent> toRemove = new ArrayList<>();
                int[] selectedRows = agentsTable.getSelectedRows();

                for (int i = 0; i < selectedRows.length; i++) {
                    toRemove.add(agentsTableModel.getAgent(selectedRows[i]));
                }

                DeleteAgentSwingWorker worker = new DeleteAgentSwingWorker(agentsTableModel, toRemove);
                worker.execute();
            }

            private void deleteMissionActionPerformed(ActionEvent evt) {
                MissionsTableModel missionsTableModel = (MissionsTableModel) missionsTable.getModel();
                List<Mission> toRemove = new ArrayList<>();
                int[] selectedRows = missionsTable.getSelectedRows();

                for (int i = 0; i < selectedRows.length; i++) {
                    toRemove.add(missionsTableModel.getMission(selectedRows[i]));
                }

                DeleteMissionSwingWorker worker = new DeleteMissionSwingWorker(missionsTableModel, toRemove);
                worker.execute();
            }

            private void deleteAssignmentActionPerformed(ActionEvent evt) {
                AssignmentsTableModel assignmentsTableModel = (AssignmentsTableModel) assignmentsTable.getModel();
                List<Assignment> toRemove = new ArrayList<>();
                int[] selectedRows = assignmentsTable.getSelectedRows();

                for (int i = 0; i < selectedRows.length; i++) {
                    toRemove.add(assignmentsTableModel.getAssignment(selectedRows[i]));
                }

                DeleteAssignmentSwingWorker worker = new DeleteAssignmentSwingWorker(assignmentsTableModel, toRemove);
                worker.execute();
            }
        });
    }//GEN-LAST:event_deleteEntityButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (java.util.ResourceBundle.getBundle("frontend/MainFrame").getString("NIMBUS").equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addAgentMenuItem;
    private javax.swing.JMenuItem addAssignmentMenuItem;
    private javax.swing.JButton addEntityButton;
    private javax.swing.JMenuItem addMissionMenuItem;
    private javax.swing.JMenu agentsMenu;
    private javax.swing.JPanel agentsPanel;
    private javax.swing.JTable agentsTable;
    private javax.swing.JPanel assignmentsPanel;
    private javax.swing.JTable assignmentsTable;
    private javax.swing.JButton deleteEntityButton;
    private javax.swing.JTabbedPane entitiesPanel;
    private javax.swing.JCheckBox filterCheckBox;
    private javax.swing.JCheckBox filterCheckBox2;
    private javax.swing.JComboBox filterComboBox;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel missionsPanel;
    private javax.swing.JTable missionsTable;
    // End of variables declaration//GEN-END:variables

    private void showAgentsActions() {
        filterCheckBox2.setVisible(false);
        filterCheckBox.setText(java.util.ResourceBundle.getBundle("frontend/MainFrame").getString("ONLY AGENTS WITH MISSIONS:"));
        filterCheckBox.setVisible(true);

        filterComboBox.setVisible(true);
        filterComboBox.setModel(new MissionComboBoxModel());
        filterComboBox.setSelectedIndex(0);
    }

    private void showMissionsActions() {
        filterCheckBox2.setVisible(true);
        filterCheckBox.setText(java.util.ResourceBundle.getBundle("frontend/MainFrame").getString("ONLY MISSIONS WITH AGENT:"));
        filterCheckBox.setVisible(true);

        filterComboBox.setVisible(true);
        filterComboBox.setModel(new AgentComboBoxModel());
        filterComboBox.setSelectedIndex(0);
    }

    private void showAssignmentsActions() {
        filterCheckBox2.setVisible(false);
        filterCheckBox.setVisible(false);
        filterComboBox.setVisible(false);
    }

    private void addAgentDialogCalled() {
        AgentDialog ad = new AgentDialog(MainFrame.this, true);
        ad.setVisible(true);

        ad.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (ad.getReturnStatus() == AgentDialog.RET_OK) {
                    Agent agent = ad.getAgent();
                    AgentsTableModel model = (AgentsTableModel) agentsTable.getModel();
                    AddAgentSwingWorker sw = new AddAgentSwingWorker(model, agent);
                    sw.execute();
                }
            }
        });
    }

    private void addMissionDialogCalled() {
        MissionDialog md = new MissionDialog(MainFrame.this, true);
        md.setVisible(true);

        md.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (md.getReturnStatus() == MissionDialog.RET_OK) {
                    Mission mission = md.getMission();
                    MissionsTableModel model = (MissionsTableModel) missionsTable.getModel();
                    AddMissionSwingWorker sw = new AddMissionSwingWorker(model, mission);
                    sw.execute();
                }
            }
        });
    }

    private void addAssignmentDialogCalled() {
        AssignmentDialog asd = new AssignmentDialog(MainFrame.this, true);
        asd.setVisible(true);

        asd.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (asd.getReturnStatus() == AssignmentDialog.RET_OK) {
                    Assignment assignment = asd.getAssignment();
                    AssignmentsTableModel model = (AssignmentsTableModel) assignmentsTable.getModel();
                    AddAssignmentSwingWorker sw = new AddAssignmentSwingWorker(model, assignment);
                    sw.execute();
                }
            }
        });
    }

    private void loadProperties() {
        FileReader reader = null;
        try {
            File configFile = new File("config.properties");
            reader = new FileReader(configFile);
            properties.load(reader);

            filterCheckBox.setSelected(Boolean.valueOf(properties.getProperty("filter1selected", "false")));
            filterCheckBox2.setSelected(Boolean.valueOf(properties.getProperty("filter2selected", "false")));
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
