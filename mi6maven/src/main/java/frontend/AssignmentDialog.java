/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import backend.entities.Agent;
import backend.entities.Assignment;
import backend.entities.Mission;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.Instant;
import java.util.Date;
import frontend.model.AgentComboBoxModel;
import frontend.model.MissionComboBoxModel;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author Andrej Halaj
 */
public class AssignmentDialog extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    /**
     * Creates new form AssignmentDialog
     *
     * @param parent
     * @param modal
     */
    public AssignmentDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        // Close the dialog when Esc is pressed
        String cancelName = java.util.ResourceBundle.getBundle("frontend/AssignmentDialog").getString("CANCEL");
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });

    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        agentComboBox = new javax.swing.JComboBox();
        missionComboBox = new javax.swing.JComboBox();
        agentLabel = new javax.swing.JLabel();
        missionLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        startDay = new javax.swing.JSpinner();
        startMonth = new javax.swing.JSpinner();
        startYear = new javax.swing.JSpinner();
        endDay = new javax.swing.JSpinner();
        endMonth = new javax.swing.JSpinner();
        endYear = new javax.swing.JSpinner();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("frontend/AssignmentDialog"); // NOI18N
        okButton.setText(bundle.getString("OK")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(bundle.getString("CANCEL")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        agentComboBox.setModel(new AgentComboBoxModel());
        agentComboBox.setToolTipText(bundle.getString("")); // NOI18N

        missionComboBox.setModel(new MissionComboBoxModel());

        agentLabel.setText(bundle.getString("AGENT:")); // NOI18N

        missionLabel.setText(bundle.getString("MISSION:")); // NOI18N

        jLabel1.setText(bundle.getString("START DATE:")); // NOI18N

        jLabel2.setText(bundle.getString("END DATE:")); // NOI18N

        jLabel3.setText(bundle.getString("DAY")); // NOI18N

        jLabel4.setText(bundle.getString("MONTH")); // NOI18N

        jLabel5.setText(bundle.getString("YEAR")); // NOI18N

        startDay.setModel(new javax.swing.SpinnerNumberModel(1, 1, 31, 1));

        startMonth.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));

        startYear.setModel(new javax.swing.SpinnerNumberModel(1900, 1900, 2015, 1));

        endDay.setModel(new javax.swing.SpinnerNumberModel(0, 0, 31, 1));

        endMonth.setModel(new javax.swing.SpinnerNumberModel(0, 0, 12, 1));

        endYear.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2015), Integer.valueOf(2015), null, Integer.valueOf(1)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(agentLabel)
                            .addComponent(missionLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cancelButton))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(agentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(38, 38, 38)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(missionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(startDay, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                            .addComponent(endDay))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(endMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(endYear, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startYear, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(206, 206, 206)))
                .addGap(208, 208, 208))
            .addGroup(layout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(jLabel3)
                .addGap(59, 59, 59)
                .addComponent(jLabel4)
                .addGap(88, 88, 88)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(agentLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(missionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(missionLabel))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(startDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(endDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (validateDates()) {
            doClose(RET_OK);
        } else {
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("frontend/AssignmentDialog").getString("DATES ARE INVALID. PLEASE ENTER CORRECT DATES."));
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

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
                if (java.util.ResourceBundle.getBundle("frontend/AssignmentDialog").getString("NIMBUS").equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AssignmentDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AssignmentDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AssignmentDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AssignmentDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AssignmentDialog dialog = new AssignmentDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox agentComboBox;
    private javax.swing.JLabel agentLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JSpinner endDay;
    private javax.swing.JSpinner endMonth;
    private javax.swing.JSpinner endYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JComboBox missionComboBox;
    private javax.swing.JLabel missionLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JSpinner startDay;
    private javax.swing.JSpinner startMonth;
    private javax.swing.JSpinner startYear;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

    public Assignment getAssignment() {
        Agent agent = (Agent) agentComboBox.getSelectedItem();

        Mission mission = (Mission) missionComboBox.getSelectedItem();

        Assignment assignment = new Assignment();
        assignment.setAgent(agent);
        assignment.setMission(mission);
        assignment.setStartDate(getStartDate());
        assignment.setEndDate(getEndDate());

        return assignment;
    }

    private Date getStartDate() {
        Integer startYear = (Integer) this.startYear.getValue();
        Integer startMonth = (Integer) this.startMonth.getValue();
        Integer startDay = (Integer) this.startDay.getValue();
        Calendar startCalendar = new GregorianCalendar(startYear, startMonth, startDay);
        
        if (!startCalendar.isLenient()) {
            return new GregorianCalendar(1900, 1, 1).getTime();
        }

        return startCalendar.getTime();
    }

    private Date getEndDate() {
        Integer endYear = (Integer) this.endYear.getValue();
        Integer endMonth = (Integer) this.endMonth.getValue();
        Integer endDay = (Integer) this.endDay.getValue();
        Calendar endCalendar = new GregorianCalendar(endYear, endMonth, endDay);
        
        if (!endCalendar.isLenient()) {
            return null;
        }

        return endCalendar.getTime();
    }

    private boolean validateDates() {
        return !(getEndDate() != null && getStartDate().after(getEndDate()));
    }
}
