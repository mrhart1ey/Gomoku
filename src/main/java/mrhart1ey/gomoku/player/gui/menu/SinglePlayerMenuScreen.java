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
public class SinglePlayerMenuScreen extends javax.swing.JPanel {
    private final MenuScreenTransition menuScreenTransition;
    private final JComponent yourselfFollowOn;
    private final JComponent computerFollowOn;
    private final Stack<JComponent> menuScreenHistory;
    private final SingleTimePasser<Thread> choosenSessionConfigurationThread;
    private final Runnable yourselfSessionConfigurationHandler;
    private final Runnable computerSessionConfigurationHandler;
    
    /**
     * Creates new form SinglePlayerMenuScreen2
    */

    public SinglePlayerMenuScreen(MenuScreenTransition menuScreenTransition,
            JComponent yourselfFollowOn,
            JComponent computerFollowOn,
            Stack<JComponent> menuScreenHistory,
            SingleTimePasser<Thread> choosenSessionConfigurationThread,
            Runnable yourselfSessionConfigurationHandler,
            Runnable computerSessionConfigurationHandler) {
        this.menuScreenTransition = menuScreenTransition;
        this.yourselfFollowOn = yourselfFollowOn;
        this.computerFollowOn = computerFollowOn;
        this.menuScreenHistory = menuScreenHistory;
        this.choosenSessionConfigurationThread = choosenSessionConfigurationThread;
        this.yourselfSessionConfigurationHandler = yourselfSessionConfigurationHandler;
        this.computerSessionConfigurationHandler = computerSessionConfigurationHandler;
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
        yourself = new javax.swing.JButton();
        computer = new javax.swing.JButton();
        back = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(500, 350));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Who would you like to play against?");

        yourself.setText("Yourself");
        yourself.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yourselfActionPerformed(evt);
            }
        });

        computer.setText("Computer");
        computer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                computerActionPerformed(evt);
            }
        });

        back.setText("Back");
        back.setToolTipText("");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(65, 65, 65))
            .addGroup(layout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(yourself, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(computer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(back, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(yourself)
                .addGap(18, 18, 18)
                .addComponent(computer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 187, Short.MAX_VALUE)
                .addComponent(back)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void computerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_computerActionPerformed
        // TODO add your handling code here:
        menuScreenTransition.transition(this, yourselfFollowOn);
        Thread sessionConfigurationThread = 
                new Thread(computerSessionConfigurationHandler);
        
        choosenSessionConfigurationThread.put(sessionConfigurationThread);
        sessionConfigurationThread.start();
    }//GEN-LAST:event_computerActionPerformed

    private void yourselfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yourselfActionPerformed
        // TODO add your handling code here:
        menuScreenTransition.transition(this, computerFollowOn);
        Thread sessionConfigurationThread = 
                new Thread(yourselfSessionConfigurationHandler);
        
        choosenSessionConfigurationThread.put(sessionConfigurationThread);
        sessionConfigurationThread.start();
    }//GEN-LAST:event_yourselfActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        // TODO add your handling code here:
        menuScreenTransition.transition(null, menuScreenHistory.pop());
    }//GEN-LAST:event_backActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JButton computer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton yourself;
    // End of variables declaration//GEN-END:variables
}
