/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package V_Admin;

import Class.DatabaseConnection;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Fauzanlh
 */
public class V_Data_BelumDikembalikan extends javax.swing.JFrame {

    /**
     * Creates new form V_Data_BelumDikembalikan
     */
    Connection koneksi;

    public V_Data_BelumDikembalikan() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "DB_Kuliah_Provis_Perpustakaan");
        TampilData();
        txtTanggal3.setEnabled(false);
    }

    //SELECT FROM DB
    public void TampilData() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        tableModel.setRowCount(0);
        String kolom[] = {"No", "Kode Peminjaman", "Nama Peminjam", "Kode Koleksi", "Telat Pengembalian (Hari)"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);
        String query = null, query2;
        int No = 1;
        try {
            Statement stmt = koneksi.createStatement();
            query = "SELECT Kd_Peminjaman, Nama_anggota, Kd_Koleksi, DATEDIFF(NOW(), T_Peminjaman.Estimasi_Pengembalian) AS 'Keterlambatan' "
                    + "FROM T_Peminjaman JOIN T_Koleksi USING(Kd_Koleksi) JOIN T_Anggota USING(Kd_Anggota) "
                    + "WHERE T_Peminjaman.Status ='DIPINJAM'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String Kd_Peminjaman = rs.getString("Kd_Peminjaman");
                String Nama_Anggota = rs.getString("Nama_Anggota");
                String Kd_Koleksi = rs.getString("Kd_Koleksi");
                int Keterlambatan = rs.getInt("Keterlambatan");
                if (Keterlambatan < 0) {
                    Keterlambatan = 0;
                }
                dtm.addRow(new String[]{"" + No, Kd_Peminjaman, Nama_Anggota, Kd_Koleksi, "" + Keterlambatan});
                No = No + 1;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Pada Database" + ex);
        }
        tblPengembalian.setModel(dtm);
    }

    public void CariData() {
        String kolom[] = {"No", "Kode Peminjaman", "Nama Peminjam", "Kode Koleksi", "Telat Pengembalian (Hari)"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);
        String query = null;
        try {
            Statement stmt = koneksi.createStatement();
            query = "SELECT Kd_Peminjaman, Nama_anggota, Kd_Koleksi, DATEDIFF(NOW(), T_Peminjaman.Estimasi_Pengembalian) AS 'Keterlambatan' "
                    + "FROM T_Peminjaman JOIN T_Koleksi USING(Kd_Koleksi) JOIN T_Anggota USING(Kd_Anggota) "
                    + "WHERE T_Peminjaman.Status ='DIPINJAM' "
                    + "AND Kd_Koleksi LIKE'%" + txtCari.getText() + "%' ";
            ResultSet rs = stmt.executeQuery(query);
            int No = 1;
            while (rs.next()) {
                String Kd_Peminjaman = rs.getString("Kd_Peminjaman");
                String Nama_Anggota = rs.getString("Nama_Anggota");
                String Kd_Koleksi = rs.getString("Kd_Koleksi");
                int Keterlambatan = rs.getInt("Keterlambatan");
                if (Keterlambatan < 0) {
                    Keterlambatan = 0;
                }
                dtm.addRow(new String[]{"" + No, Kd_Peminjaman, Nama_Anggota, Kd_Koleksi, "" + Keterlambatan});
                No = No + 1;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "KESALAHAN PADA DATABASE" + ex);
        }
        tblPengembalian.setModel(dtm);
    }

    public void Move() throws ParseException {

        String Tanggal = null, Estimasi = null;
        try {
            Statement stmt = koneksi.createStatement();
            String KdPeminjaman = tblPengembalian.getValueAt(tblPengembalian.getSelectedRow(), 1).toString();
            String select = "SELECT * FROM T_Peminjaman WHERE Kd_peminjaman = '" + KdPeminjaman + "'";
            ResultSet rs = stmt.executeQuery(select);
            if (rs.next()) {
                Tanggal = rs.getString("Tgl_Pinjam");
                Estimasi = rs.getString("Estimasi_Pengembalian");
            }
            System.out.println(Tanggal);
            txtKodePeminjaman.setText(KdPeminjaman);
            txtKodeKoleksi.setText(tblPengembalian.getValueAt(tblPengembalian.getSelectedRow(), 3).toString());
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(Tanggal);
            txtTanggal2.setDate(date);
            java.util.Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(Estimasi);
            txtTanggal3.setDate(date2);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void TambahPengembalian() {
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        String Tanggal = String.valueOf(fm.format(txtTanggal3.getDate()));
        String getKd_Koleksi = txtKodeKoleksi.getText();
        String KdPeminjaman = txtKodePeminjaman.getText();
        int BerhasilPeminjaman = 0, BerhasilKoleksi = 0;
        try {
            Statement stmt = koneksi.createStatement();
            String Ubah_TKoleksi = "UPDATE T_Koleksi SET Status ='TERSEDIA', Estimasi_Pengembalian = (NULL) WHERE Kd_Koleksi = '" + getKd_Koleksi + "'";
            String Ubah_TPeminjaman = "UPDATE T_Peminjaman SET Tgl_Kembali ='" + Tanggal + "', Denda_Keterlambatan =0, Status = 'DIKEMBALIKAN', "
                    + "Estimasi_Pengembalian = (NULL) WHERE Kd_Peminjaman = '" + KdPeminjaman + "'";
            BerhasilKoleksi = stmt.executeUpdate(Ubah_TKoleksi);
            BerhasilPeminjaman = stmt.executeUpdate(Ubah_TPeminjaman);
            System.out.println(Ubah_TKoleksi);
            System.out.println(Ubah_TPeminjaman);
            if (BerhasilKoleksi > 0 && BerhasilPeminjaman > 0) {
                JOptionPane.showMessageDialog(null, "PENGEMBALIAN BERHASIL DILAKUKAN");
                TampilData();
                UbahPengembalian.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "PENGEMBALIAN GAGAL DILAKUKAN");
            }
        } catch (SQLException e) {
            System.out.println(e);
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

        UbahPengembalian = new javax.swing.JFrame();
        mainPanel4 = new javax.swing.JPanel();
        PanelDirectory4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        btnSubmit1 = new javax.swing.JButton();
        btnClear1 = new javax.swing.JButton();
        lblNoPol4 = new javax.swing.JLabel();
        txtKodePeminjaman = new javax.swing.JTextField();
        lblService5 = new javax.swing.JLabel();
        txtTanggal2 = new com.toedter.calendar.JDateChooser();
        txtTanggal3 = new com.toedter.calendar.JDateChooser();
        lblNoPol5 = new javax.swing.JLabel();
        txtKodeKoleksi = new javax.swing.JTextField();
        lblService6 = new javax.swing.JLabel();
        mainPanel1 = new javax.swing.JPanel();
        PanelDirectory = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPengembalian = new javax.swing.JTable();
        btnPrint1 = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        lblBulan = new javax.swing.JLabel();

        mainPanel4.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel4.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory4.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory4.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Admin/Pengembalian Koleksi");

        javax.swing.GroupLayout PanelDirectory4Layout = new javax.swing.GroupLayout(PanelDirectory4);
        PanelDirectory4.setLayout(PanelDirectory4Layout);
        PanelDirectory4Layout.setHorizontalGroup(
            PanelDirectory4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectory4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelDirectory4Layout.setVerticalGroup(
            PanelDirectory4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectory4Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(39, 39, 39))
        );

        btnSubmit1.setBackground(new java.awt.Color(240, 240, 240));
        btnSubmit1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btnSubmit1.setForeground(new java.awt.Color(51, 51, 51));
        btnSubmit1.setText("Submit");
        btnSubmit1.setBorder(null);
        btnSubmit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmit1ActionPerformed(evt);
            }
        });

        btnClear1.setBackground(new java.awt.Color(240, 240, 240));
        btnClear1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btnClear1.setForeground(new java.awt.Color(51, 51, 51));
        btnClear1.setText("Cancel");
        btnClear1.setBorder(null);
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });

        lblNoPol4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol4.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol4.setText("Tanggal Peminjaman");

        txtKodePeminjaman.setEditable(false);
        txtKodePeminjaman.setBackground(new java.awt.Color(255, 255, 255));
        txtKodePeminjaman.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKodePeminjaman.setForeground(new java.awt.Color(51, 51, 51));
        txtKodePeminjaman.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService5.setForeground(new java.awt.Color(51, 51, 51));
        lblService5.setText("Kode Peminjaman");

        lblNoPol5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol5.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol5.setText("Tanggal Pengembalian");

        txtKodeKoleksi.setBackground(new java.awt.Color(255, 255, 255));
        txtKodeKoleksi.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKodeKoleksi.setForeground(new java.awt.Color(51, 51, 51));
        txtKodeKoleksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService6.setForeground(new java.awt.Color(51, 51, 51));
        lblService6.setText("Kode Koleksi");

        javax.swing.GroupLayout mainPanel4Layout = new javax.swing.GroupLayout(mainPanel4);
        mainPanel4.setLayout(mainPanel4Layout);
        mainPanel4Layout.setHorizontalGroup(
            mainPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelDirectory4, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
            .addGroup(mainPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(mainPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel4Layout.createSequentialGroup()
                        .addComponent(btnClear1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSubmit1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanel4Layout.createSequentialGroup()
                        .addGroup(mainPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblService5)
                            .addComponent(txtKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTanggal2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNoPol4))
                        .addGap(54, 54, 54)
                        .addGroup(mainPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTanggal3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNoPol5)
                            .addComponent(lblService6)
                            .addComponent(txtKodeKoleksi, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        mainPanel4Layout.setVerticalGroup(
            mainPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel4Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(PanelDirectory4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel4Layout.createSequentialGroup()
                        .addComponent(lblService5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanel4Layout.createSequentialGroup()
                        .addComponent(lblService6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKodeKoleksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel4Layout.createSequentialGroup()
                        .addComponent(lblNoPol4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(txtTanggal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanel4Layout.createSequentialGroup()
                        .addComponent(lblNoPol5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(txtTanggal3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80)
                .addGroup(mainPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubmit1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout UbahPengembalianLayout = new javax.swing.GroupLayout(UbahPengembalian.getContentPane());
        UbahPengembalian.getContentPane().setLayout(UbahPengembalianLayout);
        UbahPengembalianLayout.setHorizontalGroup(
            UbahPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );
        UbahPengembalianLayout.setVerticalGroup(
            UbahPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UbahPengembalianLayout.createSequentialGroup()
                .addComponent(mainPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel1.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel1.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Data/Data Koleksi Belum Dikembalikan");

        javax.swing.GroupLayout PanelDirectoryLayout = new javax.swing.GroupLayout(PanelDirectory);
        PanelDirectory.setLayout(PanelDirectoryLayout);
        PanelDirectoryLayout.setHorizontalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectoryLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8)
                .addContainerGap(279, Short.MAX_VALUE))
        );
        PanelDirectoryLayout.setVerticalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectoryLayout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(39, 39, 39))
        );

        tblPengembalian.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        tblPengembalian.setForeground(new java.awt.Color(0, 0, 0));
        tblPengembalian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Peminjaman", "Nama Peminjam", "Kode  Koleksi", "Telat Pengembalian (Hari)"
            }
        ));
        jScrollPane2.setViewportView(tblPengembalian);

        btnPrint1.setBackground(new java.awt.Color(240, 240, 240));
        btnPrint1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnPrint1.setForeground(new java.awt.Color(51, 51, 51));
        btnPrint1.setText("Print");
        btnPrint1.setBorder(null);
        btnPrint1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrint1MouseClicked(evt);
            }
        });
        btnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrint1ActionPerformed(evt);
            }
        });

        btnUbah.setBackground(new java.awt.Color(240, 240, 240));
        btnUbah.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnUbah.setForeground(new java.awt.Color(51, 51, 51));
        btnUbah.setText("Ubah");
        btnUbah.setBorder(null);
        btnUbah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUbahMouseClicked(evt);
            }
        });

        txtCari.setBackground(new java.awt.Color(255, 255, 255));
        txtCari.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtCari.setForeground(new java.awt.Color(51, 51, 51));
        txtCari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        lblBulan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBulan.setForeground(new java.awt.Color(51, 51, 51));
        lblBulan.setText("Cari Kode Koleksi");

        javax.swing.GroupLayout mainPanel1Layout = new javax.swing.GroupLayout(mainPanel1);
        mainPanel1.setLayout(mainPanel1Layout);
        mainPanel1Layout.setHorizontalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBulan)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(mainPanel1Layout.createSequentialGroup()
                            .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnPrint1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanel1Layout.setVerticalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblBulan)
                .addGap(10, 10, 10)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrint1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrint1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrint1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrint1MouseClicked

    private void btnUbahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUbahMouseClicked
        if (tblPengembalian.getSelectedRow() >= 0) {
            try {
                UbahPengembalian.show();
                UbahPengembalian.setLocationRelativeTo(null);
                UbahPengembalian.setSize(470, 470);
                Move();
            } catch (ParseException ex) {
                Logger.getLogger(V_Data_BelumDikembalikan.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "PILIH DATA YANG AKAN DIUBAH");
        }
    }//GEN-LAST:event_btnUbahMouseClicked

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        if (!txtCari.getText().equals("")) {
            CariData();
        } else if (txtCari.getText().equals("")) {
            TampilData();
        }
    }//GEN-LAST:event_txtCariKeyReleased

    private void btnSubmit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmit1ActionPerformed
        // TODO add your handling code here:
        TambahPengembalian();
    }//GEN-LAST:event_btnSubmit1ActionPerformed

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        UbahPengembalian.dispose();
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void btnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrint1ActionPerformed
        // TODO add your handling code here:
        //EXPORT PDF        
        try {
            //Koneksi Database
            com.mysql.jdbc.Connection c = (com.mysql.jdbc.Connection) DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_kuliah_provis_perpustakaan");
            //CETAK DATA
            HashMap parameter = new HashMap();
            //AMBIL FILE
            File file = new File("src/Report/Laporan_Data_BelumDikembalikan.jasper");
            JasperReport jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameter, c);
            //AGAR TIDAK MENGCLOSE APLIKASi
            JasperViewer.viewReport(jp, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }

    }//GEN-LAST:event_btnPrint1ActionPerformed

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
            java.util.logging.Logger.getLogger(V_Data_BelumDikembalikan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Data_BelumDikembalikan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Data_BelumDikembalikan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Data_BelumDikembalikan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Data_BelumDikembalikan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDirectory;
    private javax.swing.JPanel PanelDirectory4;
    private javax.swing.JFrame UbahPengembalian;
    private javax.swing.JButton btnClear1;
    private javax.swing.JButton btnPrint1;
    private javax.swing.JButton btnSubmit1;
    private javax.swing.JButton btnUbah;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBulan;
    private javax.swing.JLabel lblNoPol4;
    private javax.swing.JLabel lblNoPol5;
    private javax.swing.JLabel lblService5;
    private javax.swing.JLabel lblService6;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JPanel mainPanel4;
    private javax.swing.JTable tblPengembalian;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtKodeKoleksi;
    private javax.swing.JTextField txtKodePeminjaman;
    private com.toedter.calendar.JDateChooser txtTanggal2;
    private com.toedter.calendar.JDateChooser txtTanggal3;
    // End of variables declaration//GEN-END:variables
}
