/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 *
 * @author Andrej Halaj
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();

        if (entitiesPanel.getSelectedIndex() == 1) {
            missionsWithoutAgentCheckBox.setVisible(true);
        } else {
            missionsWithoutAgentCheckBox.setVisible(false);
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
        editEntityButton = new javax.swing.JButton();
        editEntityButton1 = new javax.swing.JButton();
        missionsWithoutAgentCheckBox = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        agentsMenu = new javax.swing.JMenu();
        addAgentMenuItem = new javax.swing.JMenuItem();
        missionsMenu = new javax.swing.JMenu();
        addMissionMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        addAssignmentMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        entitiesPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabSwitched(evt);
            }
        });

        agentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(agentsTable);

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

        missionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(missionsTable);

        javax.swing.GroupLayout missionsPanelLayout = new javax.swing.GroupLayout(missionsPanel);
        missionsPanel.setLayout(missionsPanelLayout);
        missionsPanelLayout.setHorizontalGroup(
            missionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
        );
        missionsPanelLayout.setVerticalGroup(
            missionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );

        entitiesPanel.addTab("Missions", missionsPanel);

        assignmentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(assignmentsTable);

        javax.swing.GroupLayout assignmentsPanelLayout = new javax.swing.GroupLayout(assignmentsPanel);
        assignmentsPanel.setLayout(assignmentsPanelLayout);
        assignmentsPanelLayout.setHorizontalGroup(
            assignmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
        );
        assignmentsPanelLayout.setVerticalGroup(
            assignmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );

        entitiesPanel.addTab("Assignments", assignmentsPanel);

        editEntityButton.setText("Edit");

        editEntityButton1.setText("Delete");

        missionsWithoutAgentCheckBox.setText("Only Missions Without Agent");

        agentsMenu.setText("Agents");

        addAgentMenuItem.setText("Add Agent");
        addAgentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAgentActionPerformed(evt);
            }
        });
        agentsMenu.add(addAgentMenuItem);

        jMenuBar1.add(agentsMenu);

        missionsMenu.setText("Missions");

        addMissionMenuItem.setText("Add Mission");
        addMissionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMissionMenuItemActionPerformed(evt);
            }
        });
        missionsMenu.add(addMissionMenuItem);

        jMenuBar1.add(missionsMenu);

        jMenu1.setText("Assignments");

        addAssignmentMenuItem.setText("Add Assignment");
        addAssignmentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAssignmentMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(addAssignmentMenuItem);

        jMenuBar1.add(jMenu1);

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
                    .addComponent(editEntityButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editEntityButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(missionsWithoutAgentCheckBox))
                .addContainerGap(102, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addComponent(entitiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(missionsWithoutAgentCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(editEntityButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(editEntityButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAgentActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                AgentDialog dialog = new AgentDialog(MainFrame.this, true);
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_addAgentActionPerformed

    private void addMissionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMissionMenuItemActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                MissionDialog dialog = new MissionDialog(MainFrame.this, false);
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_addMissionMenuItemActionPerformed

    private void addAssignmentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAssignmentMenuItemActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                AssignmentDialog dialog = new AssignmentDialog(MainFrame.this, false);
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_addAssignmentMenuItemActionPerformed

    private void tabSwitched(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabSwitched
        if (((JTabbedPane) evt.getSource()).getSelectedIndex() == 1) {
            missionsWithoutAgentCheckBox.setVisible(true);
        } else {
            missionsWithoutAgentCheckBox.setVisible(false);
        }
    }//GEN-LAST:event_tabSwitched

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addAgentMenuItem;
    private javax.swing.JMenuItem addAssignmentMenuItem;
    private javax.swing.JMenuItem addMissionMenuItem;
    private javax.swing.JMenu agentsMenu;
    private javax.swing.JPanel agentsPanel;
    private javax.swing.JTable agentsTable;
    private javax.swing.JPanel assignmentsPanel;
    private javax.swing.JTable assignmentsTable;
    private javax.swing.JButton editEntityButton;
    private javax.swing.JButton editEntityButton1;
    private javax.swing.JTabbedPane entitiesPanel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenu missionsMenu;
    private javax.swing.JPanel missionsPanel;
    private javax.swing.JTable missionsTable;
    private javax.swing.JCheckBox missionsWithoutAgentCheckBox;
    // End of variables declaration//GEN-END:variables
}
