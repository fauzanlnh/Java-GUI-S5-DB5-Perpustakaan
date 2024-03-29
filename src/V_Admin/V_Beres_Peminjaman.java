/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package V_Admin;

import Class.C_Anggota;
import Class.C_Koleksi;
import Class.C_Peminjaman;
import Class.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class V_Beres_Peminjaman extends javax.swing.JFrame {

    /**
     * Creates new form V_Beres_Peminjaman
     */
    Connection koneksi;
    int Tahun, Bulan;
    String Estimasi;
    C_Peminjaman Pinjam = new C_Peminjaman();
    C_Koleksi Koleksi = new C_Koleksi();
    C_Anggota Anggota = new C_Anggota();
    String getKdPeminjaman, getKdKoleksi, getNamaKoleksi, getKdAnggota, getTglPinjam, getEstimasi;

    public V_Beres_Peminjaman() {
        initComponents();
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
        this.setLocationRelativeTo(null);
        txtTanggal.setEnabled(false);
        setJDate();
        Estimasi();
    }

    public void setJDate() {
        Calendar today = Calendar.getInstance();
        txtTanggal.setDate(today.getTime());
        Bulan = today.get(Calendar.MONTH);
        Tahun = today.get(Calendar.YEAR);
        Bulan = Bulan + 1;
    }

    public void Estimasi() {
        Calendar today = Calendar.getInstance();
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        today.add(Calendar.DATE, +7);
        Pinjam.setEsimasiKembali(fm.format(today.getTime()));
    }

    public void Clear() {
        txtKodeAnggota.setText("");
        txtKodeKoleksi.setText("");
        DefaultTableModel tableModel = (DefaultTableModel) tblPeminjaman.getModel();
        tableModel.setRowCount(0);
        setJDate();
    }

    public void hapusTablePeminjaman() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPeminjaman.getModel();
        int getrow = tblPeminjaman.getSelectedRow();
        if (getrow >= 0) {
            int ok = JOptionPane.showConfirmDialog(null, "Yakin Mau Hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (ok == 0) {
                tableModel.removeRow(getrow);
                try {
                    Statement stmt = koneksi.createStatement();
                    String SelectPeminjaman = "SELECT (Kd_Peminjaman) AS 'Banyak_Kode' FROM T_Peminjaman WHERE MONTH(Tgl_Pinjam) = '" + Bulan + "' AND YEAR(Tgl_Pinjam) = '" + Tahun + "' ORDER BY Banyak_Kode DESC LIMIT 1";
                    ResultSet rs2 = stmt.executeQuery(SelectPeminjaman);
                    if (rs2.next()) {
                        Pinjam.setKdPeminjaman(rs2.getString("Banyak_Kode"));
                        getKdPeminjaman = Pinjam.getKdPeminjaman();
                    }
                    String[] kode = getKdPeminjaman.split("-");
                    String bagian2 = kode[1];
                    int intBagian2 = Integer.valueOf(bagian2);
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        tblPeminjaman.setValueAt(i + 1, i, 0);
                        int no = (int) tblPeminjaman.getValueAt(i, 0);
                        tblPeminjaman.setValueAt("" + Tahun + Bulan + "-" + (intBagian2 + no), i, 1);
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void SetTabelPeminjaman() {
        Koleksi.setKdKoleksi(txtKodeKoleksi.getText().toUpperCase());
        getKdKoleksi = Koleksi.getKdKoleksi();
        DefaultTableModel tableModel = (DefaultTableModel) tblPeminjaman.getModel();
        String[] data = new String[4];
        String SelectPeminjaman, SelectKoleksi;

        int no = tableModel.getRowCount() + 1;
        String No = Integer.toString(no);
        if (txtKodeKoleksi.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "KODE KOLEKSI MASIH KOSONG");
        } else {
            try {
                Statement stmt = koneksi.createStatement();
                SelectKoleksi = "SELECT * FROM T_KOLEKSI WHERE Kd_Koleksi = '" + getKdKoleksi + "' AND STATUS = 'TERSEDIA'";
                ResultSet rs = stmt.executeQuery(SelectKoleksi);
                if (rs.next()) {
                    Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                    getNamaKoleksi = Koleksi.getJudulKoleksi();
                    SelectPeminjaman = "SELECT (Kd_Peminjaman) AS 'Banyak_Kode' FROM T_Peminjaman WHERE MONTH(Tgl_Pinjam) = '" + Bulan + "' AND YEAR(Tgl_Pinjam) = '" + Tahun + "' ORDER BY Banyak_Kode DESC LIMIT 1";
                    ResultSet rs2 = stmt.executeQuery(SelectPeminjaman);
                    if (rs2.next()) {
                        Pinjam.setKdPeminjaman(rs2.getString("Banyak_Kode"));
                        getKdPeminjaman = Pinjam.getKdPeminjaman();
                    }
                    String[] kode = getKdPeminjaman.split("-");
                    String bagian2 = kode[1];
                    int intBagian2 = Integer.valueOf(bagian2);
                    String strBagina2 = String.valueOf(intBagian2);
                    System.out.println(strBagina2);
                    data[0] = No;
                    System.out.println(bagian2.length());
                    if (strBagina2.length() == 1) {
                        data[1] = "" + Tahun + Bulan + "-" + "00" + (intBagian2 + no);
                    } else if (strBagina2.length() == 2) {
                        data[1] = "" + Tahun + Bulan + "-" + "0" + (intBagian2 + no);
                    }
                    data[2] = getKdKoleksi;
                    data[3] = getNamaKoleksi;
                    tableModel.addRow(data);
                    txtKodeKoleksi.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "KODE KOLEKSI SALAH ATAU BUKU TIDAK TERSEDIA, PERIKSA KEMBALI KODE KOLEKSI");
                    txtKodeKoleksi.requestFocus();
                }

            } catch (SQLException e) {
                System.out.println(e);
            }

        }
    }

    public void TambahPeminjaman() {
        int BerhasilTambah = 0, BerhasilUpdate = 0;
        DefaultTableModel tableModel = (DefaultTableModel) tblPeminjaman.getModel();
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        Pinjam.setTglPinjam(String.valueOf(fm.format(txtTanggal.getDate())));
        Anggota.setKdAnggota(txtKodeAnggota.getText());
        getKdAnggota = Anggota.getKdAnggota();
        getTglPinjam = Pinjam.getTglPinjam();
        getEstimasi = Pinjam.getEsimasiKembali();
        if (txtKodeAnggota.equals("") && tableModel.getRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "FORM BELUM DI ISI");
            txtKodeAnggota.requestFocus();
        } else if (txtKodeAnggota.equals("")) {
            JOptionPane.showMessageDialog(null, "KODE ANGGOTA BELUM DI ISI");
            txtKodeAnggota.requestFocus();
        } else if (tableModel.getRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "KOLEKSI YANG DIPINJAM BELUM DI ISI");
            txtKodeKoleksi.requestFocus();
        } else {
            try {
                Statement stmt = koneksi.createStatement();
                String SelctKdAnggota = "SELECT * FROM T_Anggota WHERE Kd_Anggota = '" + getKdAnggota + "'";
                ResultSet rs = stmt.executeQuery(SelctKdAnggota);
                if (rs.next()) {
                    String JumlahPinjaman = "SELECT COUNT(Kd_Peminjaman) AS Total_Pinjaman, Status_Anggota "
                            + "FROM T_Peminjaman,T_Anggota "
                            + "WHERE T_Peminjaman.Kd_Anggota = T_Anggota.Kd_Anggota "
                            + "AND T_Anggota.Kd_Anggota = '" + getKdAnggota + "' AND Status='DIPINJAM'";
                    ResultSet rs2 = stmt.executeQuery(JumlahPinjaman);
                    if (rs2.next()) {
                        int Total_Pinjaman = rs2.getInt("Total_Pinjaman");
                        String Status = rs2.getString("Status_Anggota");
                        System.out.println(Status);
                        System.out.println(Total_Pinjaman);

                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            Pinjam.setKdPeminjaman(tableModel.getValueAt(i, 1).toString());
                            Koleksi.setKdKoleksi(tableModel.getValueAt(i, 2).toString());
                            getKdPeminjaman = Pinjam.getKdPeminjaman();
                            getKdKoleksi = Koleksi.getKdKoleksi();
                            if ((Status.equals("BIASA") && Total_Pinjaman < 3) || (Status.equals("KHUSUS") && Total_Pinjaman < 5)) {
                                String TambahPinjaman = "INSERT INTO T_Peminjaman (Kd_Peminjaman, Kd_Koleksi, Kd_Anggota, Tgl_Pinjam, Estimasi_Pengembalian, Status) VALUES"
                                        + "('" + getKdPeminjaman + "', '" + getKdKoleksi + "', '" + getKdAnggota + "', '" + getTglPinjam + "', '" + getEstimasi + "', 'DIPINJAM')";
                                String UpdateKoleksi = "UPDATE T_Koleksi SET STATUS = 'DIPINJAM', Estimasi_Pengembalian='" + getEstimasi + "' WHERE Kd_Koleksi='" + getKdKoleksi + "'";
                                BerhasilTambah = stmt.executeUpdate(TambahPinjaman);
                                BerhasilUpdate = stmt.executeUpdate(UpdateKoleksi);
                                //System.out.println(TambahPinjaman);
                                //System.out.println(UpdateKoleksi);
                            } else {
                                JOptionPane.showMessageDialog(null, "TOTAL PINJAMAN SUDAH MENCAPAI MAXIMAL");
                            }
                        }
                        if (BerhasilTambah > 0 && BerhasilUpdate > 0) {
                            JOptionPane.showMessageDialog(null, "PEMINJAMAN BERHASIL DIMASUKKAN");
                            Clear();
                        } else {
                            JOptionPane.showMessageDialog(null, "PEMINJAMAN GAGAL DIMASUKKAN");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "KODE ANGGOTA SALAH, PERIKSA KEMBALI KODE ANGGOTA");
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
        lblNoPol9 = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        btnHapusList = new javax.swing.JButton();
        btnTambahKoleksi = new javax.swing.JButton();
        txtKodeAnggota = new javax.swing.JTextField();
        lblDetServices1 = new javax.swing.JLabel();
        tblDetServices1 = new javax.swing.JScrollPane();
        tblPeminjaman = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        lblNoPol2 = new javax.swing.JLabel();
        txtKodeKoleksi = new javax.swing.JTextField();
        lblService4 = new javax.swing.JLabel();
        txtTanggal = new com.toedter.calendar.JDateChooser();

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

        lblNoPol9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol9.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol9.setText("Kode Anggota");

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

        btnHapusList.setBackground(new java.awt.Color(240, 240, 240));
        btnHapusList.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnHapusList.setForeground(new java.awt.Color(51, 51, 51));
        btnHapusList.setText("Hapus");
        btnHapusList.setBorder(null);
        btnHapusList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusListMouseClicked(evt);
            }
        });

        btnTambahKoleksi.setBackground(new java.awt.Color(240, 240, 240));
        btnTambahKoleksi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnTambahKoleksi.setForeground(new java.awt.Color(51, 51, 51));
        btnTambahKoleksi.setText("+");
        btnTambahKoleksi.setBorder(null);
        btnTambahKoleksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahKoleksiActionPerformed(evt);
            }
        });

        txtKodeAnggota.setBackground(new java.awt.Color(255, 255, 255));
        txtKodeAnggota.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKodeAnggota.setForeground(new java.awt.Color(51, 51, 51));
        txtKodeAnggota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblDetServices1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDetServices1.setForeground(new java.awt.Color(51, 51, 51));
        lblDetServices1.setText("List Peminjaman");

        tblPeminjaman.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        tblPeminjaman.setForeground(new java.awt.Color(51, 51, 51));
        tblPeminjaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Peminjaman", "Kode Koleksi", "Nama Koleksi"
            }
        ));
        tblDetServices1.setViewportView(tblPeminjaman);

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
        lblNoPol2.setText("Tanggal");

        txtKodeKoleksi.setBackground(new java.awt.Color(255, 255, 255));
        txtKodeKoleksi.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKodeKoleksi.setForeground(new java.awt.Color(51, 51, 51));
        txtKodeKoleksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService4.setForeground(new java.awt.Color(51, 51, 51));
        lblService4.setText("Kode Koleksi");

        javax.swing.GroupLayout mainPanel3Layout = new javax.swing.GroupLayout(mainPanel3);
        mainPanel3.setLayout(mainPanel3Layout);
        mainPanel3Layout.setHorizontalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDetServices1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanel3Layout.createSequentialGroup()
                        .addComponent(btnHapusList, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKodeAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNoPol9))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNoPol2))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblService4)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addComponent(txtKodeKoleksi, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTambahKoleksi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(tblDetServices1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE))
                .addContainerGap(69, Short.MAX_VALUE))
            .addComponent(PanelDirectory3, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );
        mainPanel3Layout.setVerticalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(PanelDirectory3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(lblService4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKodeKoleksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTambahKoleksi, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtKodeAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblNoPol2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNoPol9))
                                .addGap(6, 6, 6)
                                .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(lblDetServices1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tblDetServices1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHapusList, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 40, Short.MAX_VALUE))
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

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        Clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnTambahKoleksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahKoleksiActionPerformed
        SetTabelPeminjaman();
    }//GEN-LAST:event_btnTambahKoleksiActionPerformed

    private void btnHapusListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusListMouseClicked
        hapusTablePeminjaman();
    }//GEN-LAST:event_btnHapusListMouseClicked

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Data Yang Dimasukkan Benar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == 0) {
            TambahPeminjaman();
        }
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
            java.util.logging.Logger.getLogger(V_Beres_Peminjaman.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Peminjaman.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Peminjaman.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Peminjaman.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new V_Beres_Peminjaman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDirectory3;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnHapusList;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnTambahKoleksi;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblDetServices1;
    private javax.swing.JLabel lblNoPol2;
    private javax.swing.JLabel lblNoPol9;
    private javax.swing.JLabel lblService4;
    private javax.swing.JPanel mainPanel3;
    private javax.swing.JScrollPane tblDetServices1;
    private javax.swing.JTable tblPeminjaman;
    private javax.swing.JTextField txtKodeAnggota;
    private javax.swing.JTextField txtKodeKoleksi;
    private com.toedter.calendar.JDateChooser txtTanggal;
    // End of variables declaration//GEN-END:variables
}
