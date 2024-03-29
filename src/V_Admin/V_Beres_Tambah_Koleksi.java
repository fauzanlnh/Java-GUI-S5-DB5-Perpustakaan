/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package V_Admin;

import Class.C_KategoriKoleksi;
import Class.C_Koleksi;
import Class.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Fauzanlh
 */
public class V_Beres_Tambah_Koleksi extends javax.swing.JFrame {

    /**
     * Creates new form V_Beres_Tambah_Koleksi
     */
    Connection koneksi;
    C_Koleksi Koleksi = new C_Koleksi();
    C_KategoriKoleksi Kategori = new C_KategoriKoleksi();
    
    public V_Beres_Tambah_Koleksi() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
        setCMBKategori();
    }

    //SET CMB
    public void setCMBKategori() {
        try {
            String SelectKD = "SELECT * FROM T_Kategori_Koleksi";
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery(SelectKD);
            while (rs.next()) {
                Kategori.setNmKategori(rs.getString("Nama_Kategori"));
                cmbKategori.addItem(Kategori.getNmKategori());
            }
        } catch (SQLException e) {
            
        }
    }
    
    public void Clear() {
        txtJudul.setText("");
        txtNamaPengarang.setText("");
        txtNamaPenerbit.setText("");
        txtTahunTerbit.setText("");
        txtNoRak.setText("");
        txtHarga.setText("");
        cmbKategori.setSelectedIndex(0);
    }
    
    public void SimpanData() {
        if (txtJudul.getText().equals("") && txtNamaPengarang.getText().equals("") && txtNamaPenerbit.getText().equals("") && txtTahunTerbit.getText().equals("") && txtNoRak.getText().equals("") && txtHarga.getText().equals("") && cmbKategori.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "FORM BELUM DI ISI");
            txtJudul.requestFocus();
        } else if (txtJudul.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "JUDUL TIDAK BOLEH KOSONG");
            txtJudul.requestFocus();
        } else if (txtNamaPengarang.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "NAMA PENGARANG TIDAK BOLEH KOSONG");
            txtNamaPengarang.requestFocus();
        } else if (txtNamaPenerbit.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "NAMA PENERBIT TIDAK BOLEH KOSONG");
            txtNamaPenerbit.requestFocus();
        } else if (txtTahunTerbit.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "TAHUN TERBIT TIDAK BOLEH KOSONG");
            txtTahunTerbit.requestFocus();
        } else if (txtNoRak.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "NO RAK TIDAK BOLEH KOSONG");
            txtNoRak.requestFocus();
        } else if (txtHarga.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "HARGA BUKU TIDAK BOLEH KOSONG");
            txtHarga.requestFocus();
        } else if (cmbKategori.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "KATEGORI BUKU TIDAK BOLEH KOSONG");
            cmbKategori.requestFocus();
        } else {
            try {
                Koleksi.setJudulKoleksi(txtJudul.getText().toUpperCase());
                Koleksi.setPengarang(txtNamaPengarang.getText().toUpperCase());
                Koleksi.setPenerbit(txtNamaPenerbit.getText().toUpperCase());
                Koleksi.setTahunTerbit(txtTahunTerbit.getText().toUpperCase());
                Koleksi.setNoRak(txtNoRak.getText().toUpperCase());
                Koleksi.setHarga(Integer.valueOf(txtHarga.getText().toUpperCase()));
                Kategori.setNmKategori(cmbKategori.getSelectedItem().toString());
                String getJudul = Koleksi.getJudulKoleksi();
                String getPengarang = Koleksi.getPengarang();
                String getPenerbit = Koleksi.getPenerbit();
                String getTahun = Koleksi.getTahunTerbit();
                String getNoRak = Koleksi.getNoRak();
                int getHarga = Koleksi.getHarga();
                String getNamaKategori = Kategori.getNmKategori();
                Statement stmt = koneksi.createStatement();
                String SelectKdKategori = "SELECT * FROM T_Kategori_Koleksi WHERE Nama_Kategori ='" + getNamaKategori + "'";
                ResultSet rs = stmt.executeQuery(SelectKdKategori);
                if (rs.next()) {
                    Kategori.setKdKategori(rs.getString("Kd_Kategori"));
                }
                String getKdKategori = Kategori.getKdKategori();
                String TambahKoleksi = "INSERT INTO T_Koleksi (Judul_Koleksi, Nama_Pengarang, Nama_Penerbit, Tahun_Terbit, No_Rak, Kd_Kategori, Status, Harga) VALUES "
                        + "('" + getJudul + "','" + getPengarang + "','" + getPenerbit + "','" + getTahun + "','" + getNoRak + "','" + getKdKategori + "','TERSEDIA','" + getHarga + "')";
                int BerhasilTambah = stmt.executeUpdate(TambahKoleksi);
                if (BerhasilTambah > 0) {
                    JOptionPane.showMessageDialog(null, "KOLEKSI BARU BERHASIL DIMASUKKAN");
                    Clear();
                } else {
                    JOptionPane.showMessageDialog(null, "KOLEKSI BARU GAGAL DIMASUKKAN");
                }
                System.out.println(TambahKoleksi);
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

        mainPanel3 = new javax.swing.JPanel();
        PanelDirectory3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblNoPol8 = new javax.swing.JLabel();
        lblService3 = new javax.swing.JLabel();
        lblNoPol9 = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        txtJudul = new javax.swing.JTextField();
        txtNamaPengarang = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        txtKasir = new javax.swing.JLabel();
        lblNoPol10 = new javax.swing.JLabel();
        txtNamaPenerbit = new javax.swing.JTextField();
        lblService2 = new javax.swing.JLabel();
        lblService5 = new javax.swing.JLabel();
        lblService7 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        cmbKategori = new javax.swing.JComboBox<>();
        txtTahunTerbit = new javax.swing.JTextField();
        txtNoRak = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel3.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel3.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory3.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory3.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Admin/Tambah Koleksi");

        javax.swing.GroupLayout PanelDirectory3Layout = new javax.swing.GroupLayout(PanelDirectory3);
        PanelDirectory3.setLayout(PanelDirectory3Layout);
        PanelDirectory3Layout.setHorizontalGroup(
            PanelDirectory3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectory3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9)
                .addContainerGap(460, Short.MAX_VALUE))
        );
        PanelDirectory3Layout.setVerticalGroup(
            PanelDirectory3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectory3Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(39, 39, 39))
        );

        lblNoPol8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol8.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol8.setText("Nama Pengarang");

        lblService3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService3.setForeground(new java.awt.Color(51, 51, 51));
        lblService3.setText("No Rak");

        lblNoPol9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol9.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol9.setText("Judul");

        btnSubmit.setBackground(new java.awt.Color(240, 240, 240));
        btnSubmit.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(51, 51, 51));
        btnSubmit.setText("Submit");
        btnSubmit.setBorder(null);
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        txtJudul.setBackground(new java.awt.Color(255, 255, 255));
        txtJudul.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtJudul.setForeground(new java.awt.Color(51, 51, 51));
        txtJudul.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        txtNamaPengarang.setBackground(new java.awt.Color(255, 255, 255));
        txtNamaPengarang.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtNamaPengarang.setForeground(new java.awt.Color(51, 51, 51));
        txtNamaPengarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        btnClear.setBackground(new java.awt.Color(240, 240, 240));
        btnClear.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(51, 51, 51));
        btnClear.setText("Clear");
        btnClear.setBorder(null);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        txtKasir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtKasir.setForeground(new java.awt.Color(51, 51, 51));

        lblNoPol10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol10.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol10.setText("Nama Penerbit");

        txtNamaPenerbit.setBackground(new java.awt.Color(255, 255, 255));
        txtNamaPenerbit.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtNamaPenerbit.setForeground(new java.awt.Color(51, 51, 51));
        txtNamaPenerbit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService2.setForeground(new java.awt.Color(51, 51, 51));
        lblService2.setText("Tahun Terbit");

        lblService5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService5.setForeground(new java.awt.Color(51, 51, 51));
        lblService5.setText("Kategori");

        lblService7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService7.setForeground(new java.awt.Color(51, 51, 51));
        lblService7.setText("Harga");

        txtHarga.setBackground(new java.awt.Color(255, 255, 255));
        txtHarga.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtHarga.setForeground(new java.awt.Color(51, 51, 51));
        txtHarga.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        cmbKategori.setBackground(new java.awt.Color(255, 255, 255));
        cmbKategori.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbKategori.setForeground(new java.awt.Color(51, 51, 51));
        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

        txtTahunTerbit.setBackground(new java.awt.Color(255, 255, 255));
        txtTahunTerbit.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtTahunTerbit.setForeground(new java.awt.Color(51, 51, 51));
        txtTahunTerbit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        txtNoRak.setBackground(new java.awt.Color(255, 255, 255));
        txtNoRak.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtNoRak.setForeground(new java.awt.Color(51, 51, 51));
        txtNoRak.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        javax.swing.GroupLayout mainPanel3Layout = new javax.swing.GroupLayout(mainPanel3);
        mainPanel3.setLayout(mainPanel3Layout);
        mainPanel3Layout.setHorizontalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addComponent(PanelDirectory3, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanel3Layout.createSequentialGroup()
                                .addComponent(lblService2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                                .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(111, 111, 111))
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNoPol8)
                                    .addComponent(lblNoPol9)
                                    .addComponent(lblNoPol10)
                                    .addComponent(lblService3)
                                    .addComponent(lblService7))
                                .addGap(18, 18, 18)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSubmit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNamaPenerbit, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                    .addComponent(txtNamaPengarang)
                                    .addComponent(txtJudul)
                                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbKategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(lblService5)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        mainPanel3Layout.setVerticalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(PanelDirectory3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoPol9)
                    .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNoPol8)
                            .addComponent(txtNamaPengarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoPol10)
                            .addComponent(txtNamaPenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblService5)
                                .addGap(18, 18, 18))
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)))
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblService2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblService3)
                            .addComponent(txtNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblService7)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Data Yang Dimasukkan Benar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == 0) {
            SimpanData();
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        Clear();
    }//GEN-LAST:event_btnClearActionPerformed

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
            java.util.logging.Logger.getLogger(V_Beres_Tambah_Koleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Tambah_Koleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Tambah_Koleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Tambah_Koleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Beres_Tambah_Koleksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDirectory3;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblNoPol10;
    private javax.swing.JLabel lblNoPol8;
    private javax.swing.JLabel lblNoPol9;
    private javax.swing.JLabel lblService2;
    private javax.swing.JLabel lblService3;
    private javax.swing.JLabel lblService5;
    private javax.swing.JLabel lblService7;
    private javax.swing.JPanel mainPanel3;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJudul;
    public javax.swing.JLabel txtKasir;
    private javax.swing.JTextField txtNamaPenerbit;
    private javax.swing.JTextField txtNamaPengarang;
    private javax.swing.JTextField txtNoRak;
    private javax.swing.JTextField txtTahunTerbit;
    // End of variables declaration//GEN-END:variables
}
