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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fauzanlh
 */
public class V_Data_Peminjaman extends javax.swing.JFrame {

    /**
     * Creates new form V_Data_Peminjaman
     */
    Connection koneksi;
    String TanggalAwal, TanggalAkhir, Tahun;

    public V_Data_Peminjaman() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "DB_Kuliah_Provis_Perpustakaan");
        SetCMBTahun();
        SetCMBBBds();
    }
    //FORM

    public void Clear() {
        txtMasihDipinjam.setText("0");
    }

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
        String kolom[] = {"No", "Kode Peminjaman", "Nama Peminjam", "Nama Koleksi", "Estimasi Pengembalian"};
        int Bulan = cmbBerdasarkan.getSelectedIndex() + 1;
        String BulanToString = String.valueOf(Bulan);
        String Tahun = cmbTahun.getSelectedItem().toString();
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);
        String query = null, query2;
        int No = 1;
        try {
            String Tanggal = Tahun + "-" + BulanToString + "-1";
            Statement stmt = koneksi.createStatement();
            query = "SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi, T_Peminjaman.Estimasi_Pengembalian "
                    + "FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi) "
                    + "WHERE T_Peminjaman.Status = 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "')";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String Kd_Peminjaman = rs.getString("Kd_Peminjaman");
                String Nama_Anggota = rs.getString("Nama_Anggota");
                String Judul_Koleksi = rs.getString("Judul_Koleksi");
                String Estimasi_Pengembalian = rs.getString("Estimasi_Pengembalian");
                dtm.addRow(new String[]{"" + No, Kd_Peminjaman, Nama_Anggota, Judul_Koleksi, Estimasi_Pengembalian});
                No = No + 1;
            }
            query2 = "SELECT DISTINCT(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS='DIPINJAM') AS 'BelumDikembalikan' "
                    + "FROM t_peminjaman "
                    + "WHERE T_Peminjaman.Status = 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "')";
            ResultSet rs2 = stmt.executeQuery(query2);
            if (rs2.next()) {
                String BelumDikembalikan = rs2.getString("BelumDikembalikan");
                txtMasihDipinjam.setText(BelumDikembalikan);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Pada Database" + ex);
        }
        tblPengembalian.setModel(dtm);
    }

    public void TampilPerTriWulan() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        tableModel.setRowCount(0);
        String kolom[] = {"No", "Kode Peminjaman", "Nama Peminjam", "Nama Koleksi", "Estimasi Pengembalian"};
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
            String Tanggal = Tahun + "-" + Bulan + "-1";
            Statement stmt = koneksi.createStatement();
            query = "SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi "
                    + "FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi) "
                    + "WHERE T_Peminjaman.Status != 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "'+ INTERVAL 2 MONTH)";

            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                String Kd_Peminjaman = rs.getString("Kd_Peminjaman");
                String Nama_Anggota = rs.getString("Nama_Anggota");
                String Judul_Koleksi = rs.getString("Judul_Koleksi");
                dtm.addRow(new String[]{"" + No, Kd_Peminjaman, Nama_Anggota, Judul_Koleksi});
                No = No + 1;
            }
            query2 = "SELECT DISTINCT(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS='DIPINJAM') AS 'BelumDikembalikan' "
                    + "FROM t_peminjaman "
                    + "WHERE T_Peminjaman.Status = 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "'+ INTERVAL 2 MONTH)";
            ResultSet rs2 = stmt.executeQuery(query2);
            if (rs2.next()) {
                String BelumDikembalikan = rs2.getString("BelumDikembalikan");
                txtMasihDipinjam.setText(BelumDikembalikan);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Pada Database" + ex);
        }
        tblPengembalian.setModel(dtm);
    }

    public void TampilPerSemester() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        tableModel.setRowCount(0);
        String kolom[] = {"No", "Kode Peminjaman", "Nama Peminjam", "Nama Koleksi", "Estimasi Pengembalian"};
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
            String Tanggal = Tahun + "-" + Bulan + "-1";
            Statement stmt = koneksi.createStatement();
            query = "SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi "
                    + "FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi) "
                    + "WHERE T_Peminjaman.Status != 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "'+ INTERVAL 5 MONTH)";

            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                String Kd_Peminjaman = rs.getString("Kd_Peminjaman");
                String Nama_Anggota = rs.getString("Nama_Anggota");
                String Judul_Koleksi = rs.getString("Judul_Koleksi");
                dtm.addRow(new String[]{"" + No, Kd_Peminjaman, Nama_Anggota, Judul_Koleksi});
                No = No + 1;
            }
            query2 = "SELECT DISTINCT(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS='DIPINJAM') AS 'BelumDikembalikan' "
                    + "FROM t_peminjaman "
                    + "WHERE T_Peminjaman.Status = 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "'+ INTERVAL 5 MONTH)";
            ResultSet rs2 = stmt.executeQuery(query2);
            if (rs2.next()) {
                String BelumDikembalikan = rs2.getString("BelumDikembalikan");
                txtMasihDipinjam.setText(BelumDikembalikan);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Pada Database" + ex);
        }
        tblPengembalian.setModel(dtm);
    }

    public void TampilPerTahun() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        tableModel.setRowCount(0);
        String kolom[] = {"No", "Kode Peminjaman", "Nama Peminjam", "Nama Koleksi", "Estimasi Pengembalian"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);

        String Bulan = "1";
        String Tahun = cmbTahun.getSelectedItem().toString();
        String query = null, query2;
        int No = 1;
        try {
            String Tanggal = Tahun + "-" + Bulan + "-1";
            Statement stmt = koneksi.createStatement();
            query = "SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi "
                    + "FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi) "
                    + "WHERE T_Peminjaman.Status != 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "'+ INTERVAL 11 MONTH)";

            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                String Kd_Peminjaman = rs.getString("Kd_Peminjaman");
                String Nama_Anggota = rs.getString("Nama_Anggota");
                String Judul_Koleksi = rs.getString("Judul_Koleksi");
                dtm.addRow(new String[]{"" + No, Kd_Peminjaman, Nama_Anggota, Judul_Koleksi});
                No = No + 1;
            }
            query2 = "SELECT DISTINCT(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS='DIPINJAM') AS 'BelumDikembalikan' "
                    + "FROM t_peminjaman "
                    + "WHERE T_Peminjaman.Status = 'DIPINJAM' AND "
                    + "T_Peminjaman.Tgl_Kembali BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "'+ INTERVAL 11 MONTH)";
            ResultSet rs2 = stmt.executeQuery(query2);
            if (rs2.next()) {
                String BelumDikembalikan = rs2.getString("BelumDikembalikan");
                txtMasihDipinjam.setText(BelumDikembalikan);
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
        btnPrint1 = new javax.swing.JButton();
        btnCari = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        cmbCari = new javax.swing.JComboBox<>();
        cmbBerdasarkan = new javax.swing.JComboBox<>();
        lblBulan = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        cmbTahun = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        txtMasihDipinjam = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel1.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel1.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Data/Date Peminjaman Koleksi");

        javax.swing.GroupLayout PanelDirectoryLayout = new javax.swing.GroupLayout(PanelDirectory);
        PanelDirectory.setLayout(PanelDirectoryLayout);
        PanelDirectoryLayout.setHorizontalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectoryLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8)
                .addContainerGap(372, Short.MAX_VALUE))
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
                "No", "Kode Peminjaman", "Nama Peminjam", "Nama Koleksi", "Estimasi Pengembalian"
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

        btnCari.setBackground(new java.awt.Color(240, 240, 240));
        btnCari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCari.setForeground(new java.awt.Color(51, 51, 51));
        btnCari.setText("Cari");
        btnCari.setBorder(null);
        btnCari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCariMouseClicked(evt);
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

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("Belum Dikembalikan");

        txtMasihDipinjam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtMasihDipinjam.setForeground(new java.awt.Color(51, 51, 51));
        txtMasihDipinjam.setText("0");

        javax.swing.GroupLayout mainPanel1Layout = new javax.swing.GroupLayout(mainPanel1);
        mainPanel1.setLayout(mainPanel1Layout);
        mainPanel1Layout.setHorizontalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMasihDipinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cmbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(65, 65, 65))))
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addGap(301, 301, 301)
                        .addComponent(btnPrint1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBerdasarkan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtMasihDipinjam))
                .addGap(18, 18, 18)
                .addComponent(btnPrint1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
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
                .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrint1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrint1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrint1MouseClicked

    private void btnCariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCariMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariMouseClicked

    private void cmbCariItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCariItemStateChanged
        SetCMBBBds();
    }//GEN-LAST:event_cmbCariItemStateChanged

    private void cmbBerdasarkanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBerdasarkanItemStateChanged
        if (cmbCari.getSelectedIndex() == 0) {
            Clear();
            TampilPerBulan();
        } else if (cmbCari.getSelectedIndex() == 1) {
            Clear();
            TampilPerTriWulan();
        } else if (cmbCari.getSelectedIndex() == 2) {
            Clear();
            TampilPerSemester();
        } else if (cmbCari.getSelectedIndex() == 3) {
            Clear();
            TampilPerTahun();
        }
    }//GEN-LAST:event_cmbBerdasarkanItemStateChanged

    private void cmbTahunItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTahunItemStateChanged

    }//GEN-LAST:event_cmbTahunItemStateChanged

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
            java.util.logging.Logger.getLogger(V_Data_Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Data_Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Data_Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Data_Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Data_Peminjaman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDirectory;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnPrint1;
    private javax.swing.JComboBox<String> cmbBerdasarkan;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JComboBox<String> cmbTahun;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBulan;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JTable tblPengembalian;
    private javax.swing.JLabel txtMasihDipinjam;
    // End of variables declaration//GEN-END:variables
}
