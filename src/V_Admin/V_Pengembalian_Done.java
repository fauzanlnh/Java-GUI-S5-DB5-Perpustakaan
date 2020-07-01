/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package V_Admin;

import Class.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fauzanlh
 */
public class V_Pengembalian_Done extends javax.swing.JFrame {

    /**
     * Creates new form V_Pengembalian_Done
     */
    Connection koneksi;

    public V_Pengembalian_Done() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_kuliah_provis_perpustakaan");
        txtTanggal.setEnabled(false);
        setJDate();
    }

    public void setJDate() {
        Calendar today = Calendar.getInstance();
        txtTanggal.setDate(today.getTime());
    }

    public void Clear() {
        txtKodePeminjamana.setText("");
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        tableModel.setRowCount(0);
        setJDate();
    }

    public void hapusTablePeminjaman() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        int row = tblPengembalian.getSelectedRow();
        if (row >= 0) {
            int ok = JOptionPane.showConfirmDialog(null, "Yakin Mau Hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (ok == 0) {
                tableModel.removeRow(row);
            }
        }
    }

    public void SetTabelPengembalian() {
        String Kd_Peminjaman = txtKodePeminjamana.getText().toUpperCase();
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        String Tanggal = String.valueOf(fm.format(txtTanggal.getDate()));
        String[] data = new String[6];
        int Denda = 3000;
        try {
            Statement stmt = koneksi.createStatement();
            String SelectPeminjaman = "SELECT Kd_Peminjaman, T_Peminjaman.Kd_Koleksi, Judul_Koleksi, Tgl_Pinjam,DATEDIFF('" + Tanggal + "', t_peminjaman.Estimasi_Pengembalian) AS 'Keterlamabatan' "
                    + "FROM T_Peminjaman , T_Koleksi "
                    + "WHERE T_Koleksi.Kd_Koleksi = T_Peminjaman.Kd_Koleksi AND "
                    + "Kd_Peminjaman = '" + Kd_Peminjaman + "'";
            ResultSet rs = stmt.executeQuery(SelectPeminjaman);
            if (rs.next()) {
                String KodePeminjaman = rs.getString("Kd_Peminjaman");
                String KodeKoleksi = rs.getString("Kd_Koleksi");
                String NamaKoleksi = rs.getString("Judul_Koleksi");
                String TanggalPinjam = rs.getString("Tgl_Pinjam");
                int Keterlambatan = rs.getInt("Keterlamabatan");
                if (Keterlambatan < 0) {
                    Keterlambatan = 0;
                }
                Denda = Denda * Keterlambatan;
                data[0] = KodePeminjaman;
                data[1] = KodeKoleksi;
                data[2] = NamaKoleksi;
                data[3] = TanggalPinjam;
                data[4] = "" + Keterlambatan;
                data[5] = "" + Denda;
                tableModel.addRow(data);
            } else {
                JOptionPane.showMessageDialog(null, "KODE PENGEMBALIAN SALAH, PERIKSA KEMBALI KODE PENGEMBALIAN");
                txtKodePeminjamana.requestFocus();
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void TambahPengembalian() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        String Tanggal = String.valueOf(fm.format(txtTanggal.getDate()));
        int BerhasilPeminjaman = 0, BerhasilKoleksi = 0;
        if (tableModel.getRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "KOLEKSI YANG DIKEMBALIKAN BELUM DI ISI");
            txtKodePeminjamana.requestFocus();
        } else {
            try {
                Statement stmt = koneksi.createStatement();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String Ubah_TKoleksi = "UPDATE T_Koleksi SET Status ='TERSEDIA', Estimasi_Pengembalian = (NULL) WHERE Kd_Koleksi = '" + tableModel.getValueAt(i, 1) + "'";
                    String Ubah_TPeminjaman = "UPDATE T_Peminjaman SET Tgl_Kembali ='" + Tanggal + "', DENDA ='" + tableModel.getValueAt(i, 5) + "', Status = 'DIKEMBALIKAN' WHERE Kd_Peminjaman = '" + tableModel.getValueAt(i, 0) + "'";
                    BerhasilKoleksi = stmt.executeUpdate(Ubah_TKoleksi);
                    BerhasilPeminjaman = stmt.executeUpdate(Ubah_TPeminjaman);
                    System.out.println(Ubah_TKoleksi);
                    System.out.println(Ubah_TPeminjaman);
                }
                if (BerhasilKoleksi > 0 && BerhasilPeminjaman > 0) {
                    JOptionPane.showMessageDialog(null, "PENGEMBALIAN BERHASIL DILAKUKAN");
                    Clear();
                } else {
                    JOptionPane.showMessageDialog(null, "PENGEMBALIAN GAGAL DILAKUKAN");
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

        mainPanel3 = new javax.swing.JPanel();
        PanelDirectory3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        btnHapusSparepart = new javax.swing.JButton();
        btnTambahPengembalian = new javax.swing.JButton();
        lblDetServices1 = new javax.swing.JLabel();
        tblDetServices1 = new javax.swing.JScrollPane();
        tblPengembalian = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        lblNoPol2 = new javax.swing.JLabel();
        txtKodePeminjamana = new javax.swing.JTextField();
        lblService4 = new javax.swing.JLabel();
        txtTanggal = new com.toedter.calendar.JDateChooser();
        btnDenda = new javax.swing.JPanel();
        lblNoPol7 = new javax.swing.JLabel();
        btnGantiBuku = new javax.swing.JPanel();
        lblNoPol6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel3.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel3.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory3.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory3.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Admin/ Peminjaman Koleksi");

        javax.swing.GroupLayout PanelDirectory3Layout = new javax.swing.GroupLayout(PanelDirectory3);
        PanelDirectory3.setLayout(PanelDirectory3Layout);
        PanelDirectory3Layout.setHorizontalGroup(
            PanelDirectory3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectory3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelDirectory3Layout.setVerticalGroup(
            PanelDirectory3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectory3Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(39, 39, 39))
        );

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

        btnHapusSparepart.setBackground(new java.awt.Color(240, 240, 240));
        btnHapusSparepart.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnHapusSparepart.setForeground(new java.awt.Color(51, 51, 51));
        btnHapusSparepart.setText("Hapus");
        btnHapusSparepart.setBorder(null);
        btnHapusSparepart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusSparepartMouseClicked(evt);
            }
        });

        btnTambahPengembalian.setBackground(new java.awt.Color(240, 240, 240));
        btnTambahPengembalian.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnTambahPengembalian.setForeground(new java.awt.Color(51, 51, 51));
        btnTambahPengembalian.setText("+");
        btnTambahPengembalian.setBorder(null);
        btnTambahPengembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahPengembalianActionPerformed(evt);
            }
        });

        lblDetServices1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDetServices1.setForeground(new java.awt.Color(51, 51, 51));
        lblDetServices1.setText("List Pengembalian");

        tblPengembalian.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        tblPengembalian.setForeground(new java.awt.Color(51, 51, 51));
        tblPengembalian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Peminjaman", "Kode Koleksi", "Nama Koleksi", "Tanggal Pinjam", "Keterlambatan", "Denda"
            }
        ));
        tblDetServices1.setViewportView(tblPengembalian);

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

        lblNoPol2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol2.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol2.setText("Tanggal Pengembalian");

        txtKodePeminjamana.setBackground(new java.awt.Color(255, 255, 255));
        txtKodePeminjamana.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKodePeminjamana.setForeground(new java.awt.Color(51, 51, 51));
        txtKodePeminjamana.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService4.setForeground(new java.awt.Color(51, 51, 51));
        lblService4.setText("Kode Peminjaman");

        btnDenda.setBackground(new java.awt.Color(255, 255, 255));
        btnDenda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDendaMouseClicked(evt);
            }
        });

        lblNoPol7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNoPol7.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoPol7.setText("    < Denda");

        javax.swing.GroupLayout btnDendaLayout = new javax.swing.GroupLayout(btnDenda);
        btnDenda.setLayout(btnDendaLayout);
        btnDendaLayout.setHorizontalGroup(
            btnDendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnDendaLayout.createSequentialGroup()
                .addComponent(lblNoPol7)
                .addGap(0, 53, Short.MAX_VALUE))
        );
        btnDendaLayout.setVerticalGroup(
            btnDendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnDendaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNoPol7, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnGantiBuku.setBackground(new java.awt.Color(255, 255, 255));
        btnGantiBuku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGantiBukuMouseClicked(evt);
            }
        });

        lblNoPol6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNoPol6.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoPol6.setText("Ganti Buku   >");

        javax.swing.GroupLayout btnGantiBukuLayout = new javax.swing.GroupLayout(btnGantiBuku);
        btnGantiBuku.setLayout(btnGantiBukuLayout);
        btnGantiBukuLayout.setHorizontalGroup(
            btnGantiBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnGantiBukuLayout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(lblNoPol6)
                .addGap(20, 20, 20))
        );
        btnGantiBukuLayout.setVerticalGroup(
            btnGantiBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnGantiBukuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNoPol6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanel3Layout = new javax.swing.GroupLayout(mainPanel3);
        mainPanel3.setLayout(mainPanel3Layout);
        mainPanel3Layout.setHorizontalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelDirectory3, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDetServices1)
                    .addComponent(btnHapusSparepart, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(mainPanel3Layout.createSequentialGroup()
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNoPol2))
                                .addGap(18, 18, 18)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblService4)
                                    .addGroup(mainPanel3Layout.createSequentialGroup()
                                        .addComponent(txtKodePeminjamana, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnTambahPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(tblDetServices1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGantiBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        mainPanel3Layout.setVerticalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGantiBuku, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelDirectory3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(lblService4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKodePeminjamana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTambahPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(lblNoPol2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(lblDetServices1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tblDetServices1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapusSparepart, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(mainPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Data Yang Dimasukkan Benar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == 0) {
            TambahPengembalian();
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnHapusSparepartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusSparepartMouseClicked
        hapusTablePeminjaman();
    }//GEN-LAST:event_btnHapusSparepartMouseClicked

    private void btnTambahPengembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPengembalianActionPerformed
        SetTabelPengembalian();
    }//GEN-LAST:event_btnTambahPengembalianActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        Clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDendaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDendaMouseClicked
        V_Pengembalian_Denda_Done VP = new V_Pengembalian_Denda_Done();
        VP.show();
        this.dispose();
    }//GEN-LAST:event_btnDendaMouseClicked

    private void btnGantiBukuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGantiBukuMouseClicked
        V_Pengembalian_GantiBuku_Done VP = new V_Pengembalian_GantiBuku_Done();
        VP.show();
        this.dispose();
    }//GEN-LAST:event_btnGantiBukuMouseClicked

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
            java.util.logging.Logger.getLogger(V_Pengembalian_Done.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Pengembalian_Done.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Pengembalian_Done.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Pengembalian_Done.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Pengembalian_Done().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDirectory3;
    private javax.swing.JButton btnClear;
    private javax.swing.JPanel btnDenda;
    private javax.swing.JPanel btnGantiBuku;
    private javax.swing.JButton btnHapusSparepart;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnTambahPengembalian;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblDetServices1;
    private javax.swing.JLabel lblNoPol2;
    private javax.swing.JLabel lblNoPol6;
    private javax.swing.JLabel lblNoPol7;
    private javax.swing.JLabel lblService4;
    private javax.swing.JPanel mainPanel3;
    private javax.swing.JScrollPane tblDetServices1;
    private javax.swing.JTable tblPengembalian;
    private javax.swing.JTextField txtKodePeminjamana;
    private com.toedter.calendar.JDateChooser txtTanggal;
    // End of variables declaration//GEN-END:variables
}
