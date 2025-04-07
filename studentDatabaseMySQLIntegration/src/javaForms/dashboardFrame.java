/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package javaForms;
import java.net.URL;
import javax.swing.*;

/**
 *
 * @author John-Ronan Beira
 */
import java.awt.Color;
import java.awt.event.ItemEvent;
public class dashboardFrame extends javax.swing.JFrame {

    /**
     * Creates new form dashboardFrame
     */
    public dashboardFrame() {
        initComponents();
        URL iconUrl = getClass().getResource("/resources/icoFiles/tabIcon.png");
        ImageIcon logo = new ImageIcon(iconUrl);
        setIconImage(logo.getImage());   
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dashboardTabs = new javax.swing.JTabbedPane();
        studentInformationContainer = new javax.swing.JPanel();
        templatePaginatedTableForms table = new templatePaginatedTableForms();

        studentInformationContainer.removeAll();
        studentInformationContainer.add(table);
        studentInformationContainer.repaint();
        studentInformationContainer.revalidate();
        enrollmentFormsContainer = new javax.swing.JPanel();
        formPanelHolder = new javax.swing.JPanel();
        templateForms test = new templateForms();

        formPanelHolder.removeAll();
        formPanelHolder.add(test);
        formPanelHolder.repaint();
        formPanelHolder.revalidate();
        toggleFormsPanelHolder = new javax.swing.JPanel();
        toggleFormButton = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        managementContainer = new javax.swing.JPanel();
        greetingPanel = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 32767));
        greetingLabel = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 32767));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Student Database MySQL Integrated");
        setMinimumSize(new java.awt.Dimension(850, 550));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        dashboardTabs.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        dashboardTabs.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dashboardTabs.setDoubleBuffered(true);
        dashboardTabs.setName(""); // NOI18N
        dashboardTabs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardTabsMouseClicked(evt);
            }
        });

        studentInformationContainer.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        studentInformationContainer.setLayout(new java.awt.GridLayout(1, 0));
        dashboardTabs.addTab("     Student Data Information     ", studentInformationContainer);
        studentInformationContainer.getAccessibleContext().setAccessibleParent(this);

        enrollmentFormsContainer.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        enrollmentFormsContainer.setLayout(new java.awt.GridBagLayout());

        formPanelHolder.setBackground(new java.awt.Color(255, 255, 0));
        formPanelHolder.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        enrollmentFormsContainer.add(formPanelHolder, gridBagConstraints);

        toggleFormsPanelHolder.setLayout(new java.awt.GridBagLayout());

        toggleFormButton.setBackground(new java.awt.Color(153, 255, 153));
        toggleFormButton.setFont(toggleFormButton.getFont().deriveFont(toggleFormButton.getFont().getStyle() | java.awt.Font.BOLD));
        toggleFormButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/add.png"))); // NOI18N
        toggleFormButton.setText("Enrollment Form");
        toggleFormButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        toggleFormButton.setDoubleBuffered(true);
        toggleFormButton.setFocusCycleRoot(true);
        toggleFormButton.setMaximumSize(null);
        toggleFormButton.setMinimumSize(null);
        toggleFormButton.setOpaque(true);
        toggleFormButton.setPreferredSize(null);
        toggleFormButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/loading-arrow.png"))); // NOI18N
        toggleFormButton.setVerifyInputWhenFocusTarget(false);
        toggleFormButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                toggleFormButtonItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toggleFormsPanelHolder.add(toggleFormButton, gridBagConstraints);

        jLabel1.setText("You're currently set in:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toggleFormsPanelHolder.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toggleFormsPanelHolder.add(filler3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        enrollmentFormsContainer.add(toggleFormsPanelHolder, gridBagConstraints);

        dashboardTabs.addTab("     Enrollment Forms     ", enrollmentFormsContainer);

        managementContainer.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dashboardTabs.addTab("     College Management     ", managementContainer);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(dashboardTabs, gridBagConstraints);
        dashboardTabs.getAccessibleContext().setAccessibleName("");

        greetingPanel.setLayout(new java.awt.GridLayout(3, 0));
        greetingPanel.add(filler1);

        greetingLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        greetingLabel.setText("INSERT GREETINGS HERE");
        greetingPanel.add(greetingLabel);
        greetingPanel.add(filler2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(greetingPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardTabsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardTabsMouseClicked
        // TODO add your handling code here:
     
        //if(dashboardTabs.getSelectedComponent() == studentInformationContainer){
            //templatePaginatedTableForms table = new templatePaginatedTableForms();
            
            //studentInformationContainer.removeAll();
            //studentInformationContainer.add(table);
            //studentInformationContainer.repaint();
            //studentInformationContainer.revalidate();
        //}
    }//GEN-LAST:event_dashboardTabsMouseClicked

    private void toggleFormButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_toggleFormButtonItemStateChanged
        // TODO add your handling code here:      
        toggleFormButton.setText(evt.getStateChange() == ItemEvent.SELECTED ? "Update Form" : "Enrollment Form");
        toggleFormButton.setBackground(evt.getStateChange() == ItemEvent.SELECTED ? new Color(0, 180, 255) : new Color(153, 255, 153));
        toggleFormButton.setForeground(evt.getStateChange() == ItemEvent.SELECTED ? Color.WHITE : Color.BLACK);
    
        toggleFormButton.revalidate();
        toggleFormButton.repaint();
    }//GEN-LAST:event_toggleFormButtonItemStateChanged

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
            java.util.logging.Logger.getLogger(dashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboardFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane dashboardTabs;
    private javax.swing.JPanel enrollmentFormsContainer;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JPanel formPanelHolder;
    private javax.swing.JLabel greetingLabel;
    private javax.swing.JPanel greetingPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel managementContainer;
    private javax.swing.JPanel studentInformationContainer;
    private javax.swing.JToggleButton toggleFormButton;
    private javax.swing.JPanel toggleFormsPanelHolder;
    // End of variables declaration//GEN-END:variables
}
