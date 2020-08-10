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
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class V_Beres_Data_Pengembalian extends javax.swing.JFrame {

    /**
     * Creates new form V_Beres_Data_Pengembalian
     */
    Connection koneksi;
    String Tanggal, Berdasarkan, Tahun;
    String kolom[] = {"No", "Kode Peminjaman", "Nama Peminjam", "Nama Koleksi"};
    String getKdPeminjaman, getNmAnggota, getNamaKoleksi;
    C_Peminjaman Pinjam = new C_Peminjaman();
    C_Anggota Anggota = new C_Anggota();
    C_Koleksi Koleksi = new C_Koleksi();

    public V_Beres_Data_Pengembalian() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
        SetCMBTahun();
        SetCMBBBds();
    }
    //FORM

    public void SetCMBBBds() {
        if (cmbCari.getSelectedIndex() == 0) {
            cmbBerdasarkan.setEnabled(true);
            cmbTahun.setEnabled(true);
            cmbBerdasarkan.removeAllItems();
            lblBulan.setText("Bulan :");
            cmbBerdasarkan.addItem("JANUARI");
            cmbBerdasarkan.addItem("FEBRUARI");
            cmbBerdasarkan.addItem("MARET");
            cmbBerdasarkan.addItem("APRIL");
            cmbBerdasarkan.addItem("MEI");
            cmbBerdasarkan.addItem("JUNI");
            cmbBerdasarkan.addItem("JULI");
            cmbBerdasarkan.addItem("AGUSTUS");
            cmbBerdasarkan.addItem("SEPTEMBER");
            cmbBerdasarkan.addItem("OKTOBER");
            cmbBerdasarkan.addItem("NOVEMBER");
            cmbBerdasarkan.addItem("DESEMBER");
        } else if (cmbCari.getSelectedIndex() == 1) {
            cmbBerdasarkan.setEnabled(true);
            cmbTahun.setEnabled(true);
            cmbBerdasarkan.removeAllItems();
            lblBulan.setText("Triwulan :");
            cmbBerdasarkan.addItem("TRIWULAN 1");
            cmbBerdasarkan.addItem("TRIWULAN 2");
            cmbBerdasarkan.addItem("TRIWULAN 3");
            cmbBerdasarkan.addItem("TRIWULAN 4");
        } else if (cmbCari.getSelectedIndex() == 2) {
            cmbBerdasarkan.setEnabled(true);
            cmbTahun.setEnabled(true);
            cmbBerdasarkan.removeAllItems();
            lblBulan.setText("Semester :");
            cmbBerdasarkan.addItem("SEMESTER 1");
            cmbBerdasarkan.addItem("SEMESTER 2");
        } else if (cmbCari.getSelectedIndex() == 3) {
            cmbBerdasarkan.removeAllItems();
            lblBulan.setText("Bulan :");
            cmbBerdasarkan.setEnabled(false);
            cmbTahun.setEnabled(true);
        }
    }

    public void SetCMBTahun() {
        try {
            Statement stmt = koneksi.createStatement();
            String SelectYear = "SELECT YEAR(Tgl_Pinjam) AS Tahun FROM T_Peminjaman GROUP BY Tahun ORDER BY Tahun ASC";
            ResultSet rs = stmt.executeQuery(SelectYear);
            while (rs.next()) {
                String Tahun = rs.getString("Tahun");
                cmbTahun.removeAllItems();
                cmbTahun.addItem(Tahun);
            }
        } catch (SQLException e) {

        }
    }

    //SELECT FROM DB
    public void TampilPerBulan() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        tableModel.setRowCount(0);

        int Bulan = cmbBerdasarkan.getSelectedIndex() + 1;
        String BulanToString = String.valueOf(Bulan);
        String Tahun = cmbTahun.getSelectedItem().toString();
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);
        String query = null, query2;
        int No = 1;
        try {
            Tanggal = Tahun + "-" + BulanToString + "-1";
            Pinjam.setTglPinjam(Tanggal);
            Statement stmt = koneksi.createStatement();
            query = "SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi "
                    + "FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi) "
                    + "WHERE T_Peminjaman.Status != 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Pinjam.getTglPinjam() + "' AND LAST_DAY('" + Pinjam.getTglPinjam() + "')";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Pinjam.setKdPeminjaman(rs.getString("Kd_Peminjaman"));
                Anggota.setNmAnggota(rs.getString("Nama_Anggota"));
                Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                getKdPeminjaman = Pinjam.getKdPeminjaman();
                getNmAnggota = Anggota.getNmAnggota();
                getNamaKoleksi = Koleksi.getJudulKoleksi();
                dtm.addRow(new String[]{"" + No, getKdPeminjaman, getNmAnggota, getNamaKoleksi});
                No = No + 1;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Pada Database" + ex);
        }
        tblPengembalian.setModel(dtm);
    }

    public void TampilPerTriWulan() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        tableModel.setRowCount(0);
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);

        int TriWulan = cmbBerdasarkan.getSelectedIndex();
        String Bulan = "1";
        if (TriWulan == 0) {
            Bulan = "1";
        } else if (TriWulan == 1) {
            Bulan = "4";
        } else if (TriWulan == 2) {
            Bulan = "7";
        } else if (TriWulan == 3) {
            Bulan = "10";
        }
        String Tahun = cmbTahun.getSelectedItem().toString();
        String query = null, query2;
        int No = 1;
        try {
            Tanggal = Tahun + "-" + Bulan + "-1";
            Pinjam.setTglPinjam(Tanggal);
            Statement stmt = koneksi.createStatement();
            query = "SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi "
                    + "FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi) "
                    + "WHERE T_Peminjaman.Status != 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Pinjam.getTglPinjam() + "' AND LAST_DAY('" + Pinjam.getTglPinjam() + "'+ INTERVAL 2 MONTH)";

            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                Pinjam.setKdPeminjaman(rs.getString("Kd_Peminjaman"));
                Anggota.setNmAnggota(rs.getString("Nama_Anggota"));
                Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                getKdPeminjaman = Pinjam.getKdPeminjaman();
                getNmAnggota = Anggota.getNmAnggota();
                getNamaKoleksi = Koleksi.getJudulKoleksi();
                dtm.addRow(new String[]{"" + No, getKdPeminjaman, getNmAnggota, getNamaKoleksi});
                No = No + 1;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Pada Database" + ex);
        }
        tblPengembalian.setModel(dtm);
    }

    public void TampilPerSemester() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        tableModel.setRowCount(0);
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);

        int Semester = cmbBerdasarkan.getSelectedIndex();
        String Bulan = "1";
        if (Semester == 0) {
            Bulan = "1";
        } else if (Semester == 1) {
            Bulan = "7";
        }
        String Tahun = cmbTahun.getSelectedItem().toString();
        String query = null, query2;
        int No = 1;
        try {
            Tanggal = Tahun + "-" + Bulan + "-1";
            Pinjam.setTglPinjam(Tanggal);
            Statement stmt = koneksi.createStatement();
            query = "SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi "
                    + "FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi) "
                    + "WHERE T_Peminjaman.Status != 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Pinjam.getTglPinjam() + "' AND LAST_DAY('" + Pinjam.getTglPinjam() + "'+ INTERVAL 5 MONTH)";

            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                Pinjam.setKdPeminjaman(rs.getString("Kd_Peminjaman"));
                Anggota.setNmAnggota(rs.getString("Nama_Anggota"));
                Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                getKdPeminjaman = Pinjam.getKdPeminjaman();
                getNmAnggota = Anggota.getNmAnggota();
                getNamaKoleksi = Koleksi.getJudulKoleksi();
                dtm.addRow(new String[]{"" + No, getKdPeminjaman, getNmAnggota, getNamaKoleksi});
                No = No + 1;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Pada Database" + ex);
        }
        tblPengembalian.setModel(dtm);
    }

    public void TampilPerTahun() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        tableModel.setRowCount(0);
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);

        String Bulan = "1";
        String Tahun = cmbTahun.getSelectedItem().toString();
        String query = null, query2;
        int No = 1;
        try {
            Tanggal = Tahun + "-" + Bulan + "-1";
            Pinjam.setTglPinjam(Tanggal);
            Statement stmt = koneksi.createStatement();
            query = "SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi "
                    + "FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi) "
                    + "WHERE T_Peminjaman.Status != 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Pinjam.getTglPinjam() + "' AND LAST_DAY('" + Pinjam.getTglPinjam() + "'+ INTERVAL 11 MONTH)";

            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                Pinjam.setKdPeminjaman(rs.getString("Kd_Peminjaman"));
                Anggota.setNmAnggota(rs.getString("Nama_Anggota"));
                Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                getKdPeminjaman = Pinjam.getKdPeminjaman();
                getNmAnggota = Anggota.getNmAnggota();
                getNamaKoleksi = Koleksi.getJudulKoleksi();
                dtm.addRow(new String[]{"" + No, getKdPeminjaman, getNmAnggota, getNamaKoleksi});
                No = No + 1;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Pada Database" + ex);
        }
        tblPengembalian.setModel(dtm);
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
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPengembalian = new javax.swing.JTable();
        btnPrint = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        cmbCari = new javax.swing.JComboBox<>();
        cmbBerdasarkan = new javax.swing.JComboBox<>();
        lblBulan = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        cmbTahun = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel1.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel1.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Data/Data Pengembalian Koleksi");

        javax.swing.GroupLayout PanelDirectoryLayout = new javax.swing.GroupLayout(PanelDirectory);
        PanelDirectory.setLayout(PanelDirectoryLayout);
        PanelDirectoryLayout.setHorizontalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectoryLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8)
                .addContainerGap(351, Short.MAX_VALUE))
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
                "No", "Kode Peminjaman", "Nama Peminjam", "Nama Koleksi"
            }
        ));
        jScrollPane2.setViewportView(tblPengembalian);
        if (tblPengembalian.getColumnModel().getColumnCount() > 0) {
            tblPengembalian.getColumnModel().getColumn(3).setResizable(false);
        }

        btnPrint.setBackground(new java.awt.Color(240, 240, 240));
        btnPrint.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(51, 51, 51));
        btnPrint.setText("Print");
        btnPrint.setBorder(null);
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrintMouseClicked(evt);
            }
        });
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));
        jLabel21.setText("Cari Berdasarkan :");

        cmbCari.setBackground(new java.awt.Color(255, 255, 255));
        cmbCari.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbCari.setForeground(new java.awt.Color(51, 51, 51));
        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bulan", "Triwulan", "Semester", "Tahun" }));
        cmbCari.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCariItemStateChanged(evt);
            }
        });

        cmbBerdasarkan.setBackground(new java.awt.Color(255, 255, 255));
        cmbBerdasarkan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbBerdasarkan.setForeground(new java.awt.Color(51, 51, 51));
        cmbBerdasarkan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBerdasarkanItemStateChanged(evt);
            }
        });

        lblBulan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBulan.setForeground(new java.awt.Color(51, 51, 51));
        lblBulan.setText("Bulan :");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));
        jLabel22.setText("Tahun :");

        cmbTahun.setBackground(new java.awt.Color(255, 255, 255));
        cmbTahun.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbTahun.setForeground(new java.awt.Color(51, 51, 51));
        cmbTahun.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bulanan", "Triwulan", "Semester", "Tahun" }));
        cmbTahun.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTahunItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout mainPanel1Layout = new javax.swing.GroupLayout(mainPanel1);
        mainPanel1.setLayout(mainPanel1Layout);
        mainPanel1Layout.setHorizontalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel1Layout.createSequentialGroup()
                                .addComponent(lblBulan)
                                .addGap(113, 113, 113)
                                .addComponent(jLabel22))
                            .addGroup(mainPanel1Layout.createSequentialGroup()
                                .addComponent(cmbBerdasarkan, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(cmbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        mainPanel1Layout.setVerticalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(lblBulan))
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBerdasarkan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
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

    private void cmbTahunItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTahunItemStateChanged

    }//GEN-LAST:event_cmbTahunItemStateChanged

    private void cmbBerdasarkanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBerdasarkanItemStateChanged
        if (cmbCari.getSelectedIndex() == 0) {
            TampilPerBulan();
            Berdasarkan = "Bulan";
        } else if (cmbCari.getSelectedIndex() == 1) {
            TampilPerTriWulan();
            Berdasarkan = "Triwulan";
        } else if (cmbCari.getSelectedIndex() == 2) {
            TampilPerSemester();
            Berdasarkan = "Semester";
        } else if (cmbCari.getSelectedIndex() == 3) {
            TampilPerTahun();
            Berdasarkan = "Tahun";
        }
    }//GEN-LAST:event_cmbBerdasarkanItemStateChanged

    private void cmbCariItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCariItemStateChanged
        SetCMBBBds();
    }//GEN-LAST:event_cmbCariItemStateChanged

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintMouseClicked

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        String Bulan;
        try {
            //Koneksi Database
            com.mysql.jdbc.Connection c = (com.mysql.jdbc.Connection) DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
            //CETAK DATA
            if (cmbBerdasarkan.getSelectedItem() == null) {
                Bulan = "Tahun";
            } else {
                Bulan = cmbBerdasarkan.getSelectedItem().toString();
            }
            HashMap parameter = new HashMap();
            parameter.put("tgl", Pinjam.getTglPinjam());
            parameter.put("bds", Berdasarkan);
            parameter.put("bln", Bulan);
            parameter.put("thn", cmbTahun.getSelectedItem().toString());
            //AMBIL FILE
            File file = new File("src/Report/Laporan_Data_Pengembalian.jasper");
            JasperReport jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameter, c);
            //AGAR TIDAK MENGCLOSE APLIKASi
            JasperViewer.viewReport(jp, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }
    }//GEN-LAST:event_btnPrintActionPerformed

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
            java.util.logging.Logger.getLogger(V_Beres_Data_Pengembalian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Data_Pengembalian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Data_Pengembalian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Data_Pengembalian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Beres_Data_Pengembalian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDirectory;
    private javax.swing.JButton btnPrint;
    private javax.swing.JComboBox<String> cmbBerdasarkan;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JComboBox<String> cmbTahun;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBulan;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JTable tblPengembalian;
    // End of variables declaration//GEN-END:variables
}
