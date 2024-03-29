/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package V_Admin;

import Class.C_Master;
import Class.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class V_Beres_Tambah_Master extends javax.swing.JFrame {

    /**
     * Creates new form V_Beres_Tambah_Master
     */
    Connection koneksi;
    C_Master Master = new C_Master();
    String getUsername, getPassword, getHakAkses;

    public V_Beres_Tambah_Master() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
    }

    public void Clear() {
        txtUsername.setText("");
        txtPassword.setText("");
    }

    public void SimpanData() {
        if (txtUsername.getText().equals("") && txtPassword.getText().equals("") && cmbHakAkses.getSelectedItem().equals("-")) {
            JOptionPane.showMessageDialog(null, "FORM HARUS DIISI");
        } else if (txtUsername.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "USERNAME HARUS DI ISI");
            txtUsername.requestFocus();
        } else if (txtPassword.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "PASSWORD HARUS DI ISI");
            txtPassword.requestFocus();
        } else if (cmbHakAkses.getSelectedItem().equals("-")) {
            JOptionPane.showMessageDialog(null, "PILIH HAK AKSES DENGAN BENAR");
            txtPassword.requestFocus();
        } else if (!txtUsername.getText().equals("") && !txtPassword.getText().equals("") && !cmbHakAkses.getSelectedItem().equals("-")) {
            try {
                Statement stmt = koneksi.createStatement();
                Master.setUsername(txtUsername.getText().toUpperCase());
                Master.setPassword(txtPassword.getText().toUpperCase());
                Master.setHakAkses(cmbHakAkses.getSelectedItem().toString().toUpperCase());
                getUsername = Master.getUsername();
                getPassword = Master.getPassword();
                getHakAkses = Master.getHakAkses();
                String SqlUbahPassword = "INSERT INTO T_Master VALUES('" + getUsername + "','" + getPassword + "','" + getHakAkses + "')";
                int BerhasilUpdate = stmt.executeUpdate(SqlUbahPassword);
                if (BerhasilUpdate == 1) {
                    System.out.println(SqlUbahPassword);
                    JOptionPane.showMessageDialog(null, "TAMBAH USER BERHASIL DILAKUKAN");
                    Clear();
                } else {
                    System.out.println(SqlUbahPassword);
                    JOptionPane.showMessageDialog(null, "TAMBAH USER GAGAL DILAKUKAN");
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
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

        mainPanel1 = new javax.swing.JPanel();
        PanelDirectory = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        FormUbah = new javax.swing.JPanel();
        lblNoPol1 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        lblNoPol2 = new javax.swing.JLabel();
        lblNoPol3 = new javax.swing.JLabel();
        cmbHakAkses = new javax.swing.JComboBox<>();
        btnClear = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel1.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel1.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Master/Tambah User");

        javax.swing.GroupLayout PanelDirectoryLayout = new javax.swing.GroupLayout(PanelDirectory);
        PanelDirectory.setLayout(PanelDirectoryLayout);
        PanelDirectoryLayout.setHorizontalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectoryLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9)
                .addContainerGap(134, Short.MAX_VALUE))
        );
        PanelDirectoryLayout.setVerticalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectoryLayout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(39, 39, 39))
        );

        FormUbah.setBackground(new java.awt.Color(255, 255, 255));
        FormUbah.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tambah User", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16), new java.awt.Color(51, 51, 51))); // NOI18N
        FormUbah.setForeground(new java.awt.Color(51, 51, 51));
        FormUbah.setPreferredSize(new java.awt.Dimension(337, 189));

        lblNoPol1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol1.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol1.setText("Username");

        txtUsername.setBackground(new java.awt.Color(255, 255, 255));
        txtUsername.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtUsername.setForeground(new java.awt.Color(51, 51, 51));
        txtUsername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        txtPassword.setBackground(new java.awt.Color(255, 255, 255));
        txtPassword.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtPassword.setForeground(new java.awt.Color(51, 51, 51));
        txtPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblNoPol2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol2.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol2.setText("Password");

        lblNoPol3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol3.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol3.setText("Hak Akses");

        cmbHakAkses.setBackground(new java.awt.Color(255, 255, 255));
        cmbHakAkses.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbHakAkses.setForeground(new java.awt.Color(51, 51, 51));
        cmbHakAkses.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Admin", "User" }));

        javax.swing.GroupLayout FormUbahLayout = new javax.swing.GroupLayout(FormUbah);
        FormUbah.setLayout(FormUbahLayout);
        FormUbahLayout.setHorizontalGroup(
            FormUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FormUbahLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FormUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNoPol1)
                    .addComponent(lblNoPol2)
                    .addComponent(lblNoPol3))
                .addGap(18, 18, 18)
                .addGroup(FormUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbHakAkses, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        FormUbahLayout.setVerticalGroup(
            FormUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FormUbahLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(FormUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoPol1)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(FormUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNoPol2))
                .addGap(18, 18, 18)
                .addGroup(FormUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoPol3)
                    .addComponent(cmbHakAkses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        btnClear.setBackground(new java.awt.Color(240, 240, 240));
        btnClear.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(51, 51, 51));
        btnClear.setText("Clear");
        btnClear.setBorder(null);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnSubmit.setBackground(new java.awt.Color(240, 240, 240));
        btnSubmit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(51, 51, 51));
        btnSubmit.setText("Submit");
        btnSubmit.setBorder(null);
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanel1Layout = new javax.swing.GroupLayout(mainPanel1);
        mainPanel1.setLayout(mainPanel1Layout);
        mainPanel1Layout.setHorizontalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(FormUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        mainPanel1Layout.setVerticalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(FormUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        Clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        SimpanData();
    }//GEN-LAST:event_btnSubmitActionPerformed

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
            java.util.logging.Logger.getLogger(V_Beres_Tambah_Master.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Tambah_Master.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Tambah_Master.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Tambah_Master.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Beres_Tambah_Master().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FormUbah;
    private javax.swing.JPanel PanelDirectory;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> cmbHakAkses;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblNoPol1;
    private javax.swing.JLabel lblNoPol2;
    private javax.swing.JLabel lblNoPol3;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
