/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrhart1ey.gomoku.player.gui.menu;

import java.util.Stack;
import javax.swing.JComponent;
import mrhart1ey.gomoku.SingleTimePasser;

/**
 *
 * @author josh
 */
public class HostWaitingMenuScreen extends javax.swing.JPanel {

    private final MenuScreenTransition menuScreenTransition;
    private final Stack<JComponent> menuScreenHistory;
    private final SingleTimePasser<Thread> sessionConfigurationThreadPasser;

    /**
     * Creates new form HostWaitingMenuScreen2
     */
    public HostWaitingMenuScreen(MenuScreenTransition menuScreenTransition,
            Stack<JComponent> menuScreenHistory,
            SingleTimePasser<Thread> sessionConfigurationThreadPasser) {
        this.menuScreenTransition = menuScreenTransition;
        this.menuScreenHistory = menuScreenHistory;
        this.sessionConfigurationThreadPasser = sessionConfigurationThreadPasser;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        back = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setText("Waiting for a player to join");

        back.setText("Back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(211, 211, 211)
                        .addComponent(back)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(back)
                .addContainerGap(152, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        try {
            // TODO add your handling code here:
            Thread sessionConfigurationHandler = sessionConfigurationThreadPasser.take();
            sessionConfigurationHandler.interrupt();
            menuScreenHistory.pop(); // Can not go back to the game config screen
            menuScreenTransition.transition(null, menuScreenHistory.pop());
        } catch (InterruptedException ex) {
            
        }
    }//GEN-LAST:event_backActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}