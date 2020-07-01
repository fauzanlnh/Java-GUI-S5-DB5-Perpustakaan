/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package V_Admin;

import Class.DatabaseConnection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fauzanlh
 */
public class V_Pengembalian_Denda_Done extends javax.swing.JFrame {

    /**
     * Creates new form V_Pengembalian_Denda_Done
     */
    Connection koneksi;

    public V_Pengembalian_Denda_Done() {
        initComponents();
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_kuliah_provis_perpustakaan");
        this.setLocationRelativeTo(null);
        CariKode.setSize(575, 475);
        CariKode.setLocationRelativeTo(null);
        setJDate();
    }

    public void setJDate() {
        Calendar today = Calendar.getInstance();
        txtTanggal.setDate(today.getTime());
    }

    public void Clear() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPeminjaman.getModel();
        tableModel.setRowCount(0);
    }

    /*-------------------------------------------------------------------------------JFRAME POP-UP----------------------------------------------------*/
    public void CariKode() {
        String kolom[] = {"NO", "Kode Peminjaman", "Kode Anggota", "Kode Koleksi", "Judul Koleksi"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);
        String query = null;
        int n = 0;
        try {
            Statement stmt = koneksi.createStatement();
            query = "SELECT * FROM T_Koleksi, T_Peminjaman WHERE T_Koleksi.Kd_Koleksi = T_Peminjaman.Kd_Koleksi "
                    + "AND Kd_Anggota LIKE '%" + txtCari.getText() + "%' OR Judul_Koleksi LIKE '%" + txtCari.getText() + "%' AND T_Peminjaman.Status = 'DIPINJAM'"
                    + "ORDER BY Judul_Koleksi ASC";

            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                String Kd_Peminjaman = rs.getString("Kd_Peminjaman");
                String Kd_Anggota = rs.getString("Kd_Anggota");
                String Kd_Koleksi = rs.getString("Kd_Koleksi");
                String Judul_Koleksi = rs.getString("Judul_Koleksi");
                dtm.addRow(new String[]{no + "", Kd_Peminjaman, Kd_Anggota, Kd_Koleksi, Judul_Koleksi});
                no++;
                n = n + 1;
            }
            if (n == 0) {
                txtDataTidakDitemukan.setForeground(new java.awt.Color(51, 51, 51));
                txtDataTidakDitemukan.show(true);
                txtCari.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Pada Database" + ex);
        }
        tblPinjam.setModel(dtm);
    }

    public void TampiDataPinjaman() {
        String kolom[] = {"NO", "Kode Peminjaman", "Kode Anggota", "Kode Koleksi", "Judul Koleksi"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);
        String query = null;
        try {
            Statement stmt = koneksi.createStatement();
            query = "SELECT * FROM T_Koleksi, T_Peminjaman WHERE T_Peminjaman.Status = 'DIPINJAM' AND T_Koleksi.Kd_Koleksi = T_Peminjaman.Kd_Koleksi "
                    + "ORDER BY Judul_Koleksi ASC";
            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                String Kd_Peminjaman = rs.getString("Kd_Peminjaman");
                String Kd_Anggota = rs.getString("Kd_Anggota");
                String Kd_Koleksi = rs.getString("Kd_Koleksi");
                String Judul_Koleksi = rs.getString("Judul_Koleksi");
                dtm.addRow(new String[]{no + "", Kd_Peminjaman, Kd_Anggota, Kd_Koleksi, Judul_Koleksi});
                no++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "KESALAHAN PADA DATABASE" + ex);
        }
        tblPinjam.setModel(dtm);
    }

    public void KlikTabelFaktur() {
        /* TIPE DATA GLOBAL */
        String[] data = new String[6];
        DefaultTableModel tableModel = (DefaultTableModel) tblPeminjaman.getModel();
        int Keterlambatan = 0, TotalDenda = 0, Denda = 3000, HargaBuku = 0;
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        String Tanggal = String.valueOf(fm.format(txtTanggal.getDate()));
        /* GET DATA */
        String getKdPeminjaman = tblPinjam.getValueAt(tblPinjam.getSelectedRow(), 1).toString();
        String getKdKoleksi = tblPinjam.getValueAt(tblPinjam.getSelectedRow(), 3).toString();
        String getNamaKoleksi = tblPinjam.getValueAt(tblPinjam.getSelectedRow(), 4).toString();
        try {
            Statement stmt = koneksi.createStatement();
            String SelectKoleksi = "SELECT * FROM T_Koleksi WHERE Kd_Koleksi = '" + getKdKoleksi + "'";
            ResultSet rs = stmt.executeQuery(SelectKoleksi);
            if (rs.next()) {
                HargaBuku = rs.getInt("Harga");
                System.out.println(HargaBuku);
            }
            String SelectPeminjaman = "SELECT DATEDIFF('" + Tanggal + "', T_Peminjaman.Estimasi_Pengembalian)  AS 'Keterlambatan' FROM T_Peminjaman";
            ResultSet rs2 = stmt.executeQuery(SelectPeminjaman);
            if (rs2.next()) {
                Keterlambatan = rs2.getInt("Keterlambatan");
                if (Keterlambatan < 0) {
                    Keterlambatan = 0;
                }
                Denda = Denda * Keterlambatan;
                TotalDenda = Denda + HargaBuku;
            }
            data[0] = getKdPeminjaman;
            data[1] = getNamaKoleksi;
            data[2] = "" + HargaBuku;
            data[3] = "" + Keterlambatan;
            data[4] = "" + Denda;
            data[5] = "" + TotalDenda;
            tableModel.addRow(data);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /*-------------------------------------------------------------------------------JFRAME POP-UP----------------------------------------------------*/
    public void TambahTabelPeminjaman() {
        /* TIPE DATA GLOBAL */
        String[] data = new String[6];
        DefaultTableModel tableModel = (DefaultTableModel) tblPeminjaman.getModel();
        int Keterlambatan = 0, TotalDenda = 0, Denda = 3000, HargaBuku = 0;
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        String Tanggal = String.valueOf(fm.format(txtTanggal.getDate()));
        String getKdPeminjaman = txtKodePeminjaman.getText();
        String NamaKoleksi = "";
        /* GET DATA */
        try {
            Statement stmt = koneksi.createStatement();
            String SelectKoleksi = "SELECT * FROM T_Koleksi,T_Peminjaman WHERE Kd_Peminjaman = '" + getKdPeminjaman + "' AND T_Koleksi.Kd_Koleksi = T_Peminjaman.Kd_Koleksi";
            ResultSet rs = stmt.executeQuery(SelectKoleksi);
            if (rs.next()) {
                NamaKoleksi = rs.getString("Nama_Koleksi");
                HargaBuku = rs.getInt("Harga");
                System.out.println(HargaBuku);
            }
            String SelectPeminjaman = "SELECT DATEDIFF('" + Tanggal + "', T_Peminjaman.Estimasi_Pengembalian)  AS 'Keterlambatan' FROM T_Peminjaman";
            ResultSet rs2 = stmt.executeQuery(SelectPeminjaman);
            if (rs2.next()) {
                Keterlambatan = rs2.getInt("Keterlambatan");
                if (Keterlambatan < 0) {
                    Keterlambatan = 0;
                }
                Denda = Denda * Keterlambatan;
                TotalDenda = Denda + HargaBuku;
            }
            data[0] = getKdPeminjaman;
            data[1] = NamaKoleksi;
            data[2] = "" + HargaBuku;
            data[3] = "" + Keterlambatan;
            data[4] = "" + Denda;
            data[5] = "" + TotalDenda;
            tableModel.addRow(data);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void TambahPengembalianDenda() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPeminjaman.getModel();
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        String Tanggal = String.valueOf(fm.format(txtTanggal.getDate()));
        int BUbahPeminjaman = 0, BUbahKoleksi = 0, BInputPengembalianBayar = 0;
        String KdKoleksi = null;
        if (tableModel.getRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "KOLEKSI YANG DIKEMBALIKAN BELUM DI ISI");
            txtKodePeminjaman.requestFocus();
        } else {
            try {
                Statement stmt = koneksi.createStatement();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String getKdKoleksi = "SELECT T_Peminjaman.Kd_Koleksi FROM T_Koleksi, T_Peminjaman WHERE T_Peminjaman.Kd_Koleksi = T_Koleksi.Kd_Koleksi AND Kd_Peminjaman = '" + tableModel.getValueAt(i, 0) + "'";
                    ResultSet rs = stmt.executeQuery(getKdKoleksi);
                    if (rs.next()) {
                        KdKoleksi = rs.getString("Kd_Koleksi");
                    }

                    String sqlInput = "INSERT INTO T_Pengembalian_Bayar(Tanggal_Ganti, Harga_Ganti, Kd_Peminjaman) VALUES('" + Tanggal + "', " + tableModel.getValueAt(i, 2) + ", '" + tableModel.getValueAt(i, 0) + "')";
                    BInputPengembalianBayar = stmt.executeUpdate(sqlInput);

                    String Ubah_TKoleksi = "UPDATE T_Koleksi SET Status ='HILANG', Estimasi_Pengembalian=(NULL) WHERE Kd_Koleksi = '" + KdKoleksi + "'";
                    BUbahKoleksi = stmt.executeUpdate(Ubah_TKoleksi);

                    String Ubah_TPeminjaman = "UPDATE T_Peminjaman SET Tgl_Kembali ='" + Tanggal + "', Denda_Keterlambatan ='" + tableModel.getValueAt(i, 4) + "', Status = 'HILANG', Estimasi_Pengembalian =(NULL) "
                            + "WHERE Kd_Peminjaman = '" + tableModel.getValueAt(i, 0) + "'";
                    BUbahPeminjaman = stmt.executeUpdate(Ubah_TPeminjaman);
                    System.out.println("Baris Ke-" + i + "\n Kode Koleksi : " + getKdKoleksi);
                    System.out.println(sqlInput);
                    System.out.println(Ubah_TKoleksi);
                    System.out.println(Ubah_TPeminjaman);

                }
                if (BUbahPeminjaman > 0 && BUbahKoleksi > 0 && BInputPengembalianBayar > 0) {
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

        CariKode = new javax.swing.JFrame();
        mainPanel1 = new javax.swing.JPanel();
        PanelDirectory1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblNoPol3 = new javax.swing.JLabel();
        btnPilihKode = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        tblDetSparepart1 = new javax.swing.JScrollPane();
        tblPinjam = new javax.swing.JTable();
        btnCancelCari = new javax.swing.JButton();
        txtDataTidakDitemukan = new javax.swing.JLabel();
        mainPanel3 = new javax.swing.JPanel();
        PanelDirectory3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnPengembalian = new javax.swing.JPanel();
        lblNoPol6 = new javax.swing.JLabel();
        btnGantiBuku = new javax.swing.JPanel();
        lblNoPol7 = new javax.swing.JLabel();
        lblNoPol9 = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnCariKodePeminjaman = new javax.swing.JButton();
        txtKodePeminjaman = new javax.swing.JTextField();
        lblDetServices1 = new javax.swing.JLabel();
        tblDetServices1 = new javax.swing.JScrollPane();
        tblPeminjaman = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        txtKasir = new javax.swing.JLabel();
        txtTanggal = new com.toedter.calendar.JDateChooser();
        lblNoPol2 = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();

        mainPanel1.setBackground(new java.awt.Color(255, 255, 255));

        PanelDirectory1.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory1.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Admin/Pengembalian Buku/Cari Kode");

        javax.swing.GroupLayout PanelDirectory1Layout = new javax.swing.GroupLayout(PanelDirectory1);
        PanelDirectory1.setLayout(PanelDirectory1Layout);
        PanelDirectory1Layout.setHorizontalGroup(
            PanelDirectory1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectory1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel10)
                .addContainerGap(121, Short.MAX_VALUE))
        );
        PanelDirectory1Layout.setVerticalGroup(
            PanelDirectory1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectory1Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(39, 39, 39))
        );

        lblNoPol3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol3.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol3.setText("Cari berdasarkan Kode Anggota / Nama Koleksi");

        btnPilihKode.setBackground(new java.awt.Color(240, 240, 240));
        btnPilihKode.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btnPilihKode.setForeground(new java.awt.Color(51, 51, 51));
        btnPilihKode.setText("Submit");
        btnPilihKode.setBorder(null);
        btnPilihKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihKodeActionPerformed(evt);
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

        tblPinjam.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        tblPinjam.setForeground(new java.awt.Color(0, 0, 0));
        tblPinjam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Peminjaman", "Kode Anggota", "Kode Koleksi", "Nama Koleksi"
            }
        ));
        tblDetSparepart1.setViewportView(tblPinjam);

        btnCancelCari.setBackground(new java.awt.Color(240, 240, 240));
        btnCancelCari.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btnCancelCari.setForeground(new java.awt.Color(51, 51, 51));
        btnCancelCari.setText("Cancel");
        btnCancelCari.setBorder(null);
        btnCancelCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelCariActionPerformed(evt);
            }
        });

        txtDataTidakDitemukan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDataTidakDitemukan.setForeground(new java.awt.Color(255, 255, 255));
        txtDataTidakDitemukan.setText("Data Yang Dicari Tidak Ditemukan");

        javax.swing.GroupLayout mainPanel1Layout = new javax.swing.GroupLayout(mainPanel1);
        mainPanel1.setLayout(mainPanel1Layout);
        mainPanel1Layout.setHorizontalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelDirectory1, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainPanel1Layout.createSequentialGroup()
                                .addComponent(btnCancelCari, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnPilihKode, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblNoPol3)
                                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tblDetSparepart1, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDataTidakDitemukan)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        mainPanel1Layout.setVerticalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(PanelDirectory1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNoPol3)
                .addGap(18, 18, 18)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tblDetSparepart1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(txtDataTidakDitemukan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPilihKode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelCari, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CariKodeLayout = new javax.swing.GroupLayout(CariKode.getContentPane());
        CariKode.getContentPane().setLayout(CariKodeLayout);
        CariKodeLayout.setHorizontalGroup(
            CariKodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CariKodeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        CariKodeLayout.setVerticalGroup(
            CariKodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel3.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel3.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory3.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory3.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Admin/Pengembalian Buku/Denda");

        javax.swing.GroupLayout PanelDirectory3Layout = new javax.swing.GroupLayout(PanelDirectory3);
        PanelDirectory3.setLayout(PanelDirectory3Layout);
        PanelDirectory3Layout.setHorizontalGroup(
            PanelDirectory3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectory3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9)
                .addContainerGap(140, Short.MAX_VALUE))
        );
        PanelDirectory3Layout.setVerticalGroup(
            PanelDirectory3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectory3Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(39, 39, 39))
        );

        btnPengembalian.setBackground(new java.awt.Color(255, 255, 255));
        btnPengembalian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPengembalianMouseClicked(evt);
            }
        });

        lblNoPol6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNoPol6.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoPol6.setText("Pengembalian   >");

        javax.swing.GroupLayout btnPengembalianLayout = new javax.swing.GroupLayout(btnPengembalian);
        btnPengembalian.setLayout(btnPengembalianLayout);
        btnPengembalianLayout.setHorizontalGroup(
            btnPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnPengembalianLayout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(lblNoPol6)
                .addGap(20, 20, 20))
        );
        btnPengembalianLayout.setVerticalGroup(
            btnPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPengembalianLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNoPol6, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnGantiBuku.setBackground(new java.awt.Color(255, 255, 255));
        btnGantiBuku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGantiBukuMouseClicked(evt);
            }
        });

        lblNoPol7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNoPol7.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoPol7.setText("    < Ganti Buku");

        javax.swing.GroupLayout btnGantiBukuLayout = new javax.swing.GroupLayout(btnGantiBuku);
        btnGantiBuku.setLayout(btnGantiBukuLayout);
        btnGantiBukuLayout.setHorizontalGroup(
            btnGantiBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnGantiBukuLayout.createSequentialGroup()
                .addComponent(lblNoPol7)
                .addGap(0, 53, Short.MAX_VALUE))
        );
        btnGantiBukuLayout.setVerticalGroup(
            btnGantiBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnGantiBukuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNoPol7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblNoPol9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol9.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol9.setText("Kode Peminjaman");

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

        btnHapus.setBackground(new java.awt.Color(240, 240, 240));
        btnHapus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(51, 51, 51));
        btnHapus.setText("Hapus");
        btnHapus.setBorder(null);
        btnHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusMouseClicked(evt);
            }
        });

        btnCariKodePeminjaman.setBackground(new java.awt.Color(240, 240, 240));
        btnCariKodePeminjaman.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCariKodePeminjaman.setForeground(new java.awt.Color(51, 51, 51));
        btnCariKodePeminjaman.setText("Cari");
        btnCariKodePeminjaman.setBorder(null);
        btnCariKodePeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariKodePeminjamanActionPerformed(evt);
            }
        });

        txtKodePeminjaman.setBackground(new java.awt.Color(255, 255, 255));
        txtKodePeminjaman.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKodePeminjaman.setForeground(new java.awt.Color(51, 51, 51));
        txtKodePeminjaman.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblDetServices1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDetServices1.setForeground(new java.awt.Color(51, 51, 51));
        lblDetServices1.setText("List Koleksi");

        tblPeminjaman.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        tblPeminjaman.setForeground(new java.awt.Color(51, 51, 51));
        tblPeminjaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Peminjaman", "Nama Koleksi", "Harga Buku", "Keterlambatan", "Denda Keterlambatan", "Total Denda"
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

        txtKasir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtKasir.setForeground(new java.awt.Color(51, 51, 51));

        txtTanggal.setBackground(new java.awt.Color(255, 255, 255));
        txtTanggal.setForeground(new java.awt.Color(51, 51, 51));

        lblNoPol2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol2.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol2.setText("Tanggal");

        btnTambah.setBackground(new java.awt.Color(240, 240, 240));
        btnTambah.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(51, 51, 51));
        btnTambah.setText("+");
        btnTambah.setBorder(null);
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanel3Layout = new javax.swing.GroupLayout(mainPanel3);
        mainPanel3.setLayout(mainPanel3Layout);
        mainPanel3Layout.setHorizontalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDetServices1)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanel3Layout.createSequentialGroup()
                                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblNoPol9)
                                            .addComponent(txtKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(6, 6, 6)
                                        .addComponent(btnCariKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblNoPol2)))
                                    .addGroup(mainPanel3Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(12, 12, 12)
                                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tblDetServices1, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(79, 79, 79)
                        .addComponent(txtKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(btnGantiBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PanelDirectory3, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        mainPanel3Layout.setVerticalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPengembalian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGantiBuku, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelDirectory3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(lblNoPol9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCariKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(lblNoPol2)
                        .addGap(35, 35, 35))
                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(lblDetServices1)
                .addGap(14, 14, 14)
                .addComponent(tblDetServices1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(txtKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mainPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPengembalianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPengembalianMouseClicked
        V_Pengembalian_Done VP = new V_Pengembalian_Done();
        VP.show();
        this.dispose();
    }//GEN-LAST:event_btnPengembalianMouseClicked

    private void btnGantiBukuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGantiBukuMouseClicked
        V_Pengembalian_GantiBuku_Done VP = new V_Pengembalian_GantiBuku_Done();
        VP.show();
        this.dispose();
    }//GEN-LAST:event_btnGantiBukuMouseClicked

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Data Yang Dimasukkan Benar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == 0) {
            TambahPengembalianDenda();
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnHapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusMouseClicked
        // hapusTablePeminjaman();
    }//GEN-LAST:event_btnHapusMouseClicked

    private void btnCariKodePeminjamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariKodePeminjamanActionPerformed
        CariKode.show();
        TampiDataPinjaman();
    }//GEN-LAST:event_btnCariKodePeminjamanActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        //   Clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        TambahTabelPeminjaman();
    }//GEN-LAST:event_btnTambahActionPerformed
    /*-----------------------------------------------------*/
    private void btnPilihKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihKodeActionPerformed
        KlikTabelFaktur();
        CariKode.dispose();
    }//GEN-LAST:event_btnPilihKodeActionPerformed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        if (!txtCari.getText().equals("")) {
            txtDataTidakDitemukan.setForeground(new java.awt.Color(255, 255, 255));
            CariKode();
        } else if (txtCari.getText().equals("")) {
            txtDataTidakDitemukan.setForeground(new java.awt.Color(255, 255, 255));
            TampiDataPinjaman();
        }
    }//GEN-LAST:event_txtCariKeyReleased

    private void btnCancelCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelCariActionPerformed
        CariKode.dispose();
        txtCari.setText("");
    }//GEN-LAST:event_btnCancelCariActionPerformed

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
            java.util.logging.Logger.getLogger(V_Pengembalian_Denda_Done.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Pengembalian_Denda_Done.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Pengembalian_Denda_Done.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Pengembalian_Denda_Done.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Pengembalian_Denda_Done().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame CariKode;
    private javax.swing.JPanel PanelDirectory1;
    private javax.swing.JPanel PanelDirectory3;
    private javax.swing.JButton btnCancelCari;
    private javax.swing.JButton btnCariKodePeminjaman;
    private javax.swing.JButton btnClear;
    private javax.swing.JPanel btnGantiBuku;
    private javax.swing.JButton btnHapus;
    private javax.swing.JPanel btnPengembalian;
    private javax.swing.JButton btnPilihKode;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnTambah;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblDetServices1;
    private javax.swing.JLabel lblNoPol2;
    private javax.swing.JLabel lblNoPol3;
    private javax.swing.JLabel lblNoPol6;
    private javax.swing.JLabel lblNoPol7;
    private javax.swing.JLabel lblNoPol9;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JPanel mainPanel3;
    private javax.swing.JScrollPane tblDetServices1;
    private javax.swing.JScrollPane tblDetSparepart1;
    private javax.swing.JTable tblPeminjaman;
    private javax.swing.JTable tblPinjam;
    private javax.swing.JTextField txtCari;
    private javax.swing.JLabel txtDataTidakDitemukan;
    public javax.swing.JLabel txtKasir;
    private javax.swing.JTextField txtKodePeminjaman;
    private com.toedter.calendar.JDateChooser txtTanggal;
    // End of variables declaration//GEN-END:variables
}
