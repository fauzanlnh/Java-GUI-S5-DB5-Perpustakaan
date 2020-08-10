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
import Class.LoginSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class V_Beres_Pengembalian extends javax.swing.JFrame {

    /**
     * Creates new form V_Beres_Pengembalian
     */
    Connection koneksi;
    C_Peminjaman Pinjam = new C_Peminjaman();
    C_Koleksi Koleksi = new C_Koleksi();
    C_Anggota Anggota = new C_Anggota();
    String getKdPeminjaman, getJudulKoleksi, getKdKoleksi, getTglKembali, getKdAnggota;
    int getDenda, getHarga;

    public V_Beres_Pengembalian() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
        txtTanggal.setEnabled(false);
        setJDate();
        CariKode.setSize(575, 475);
        CariKode.setLocationRelativeTo(null);
    }

    public void setJDate() {
        Calendar today = Calendar.getInstance();
        txtTanggal.setDate(today.getTime());
    }

    public void Clear() {
        txtKodePeminjaman.setText("");
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
        Pinjam.setKdPeminjaman(txtKodePeminjaman.getText().toUpperCase());
        getKdPeminjaman = Pinjam.getKdPeminjaman();
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
                    + "Kd_Peminjaman = '" + getKdPeminjaman + "'";
            ResultSet rs = stmt.executeQuery(SelectPeminjaman);
            if (rs.next()) {
                Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                getJudulKoleksi = Koleksi.getJudulKoleksi();
                int Keterlambatan = rs.getInt("Keterlamabatan");
                if (Keterlambatan < 0) {
                    Keterlambatan = 0;
                }
                Denda = Denda * Keterlambatan;
                Pinjam.setDenda(Denda);
                getDenda = Pinjam.getDenda();
                data[0] = getKdPeminjaman;
                data[1] = getJudulKoleksi;
                data[2] = "" + Keterlambatan;
                data[3] = "" + getDenda;
                tableModel.addRow(data);
            } else {
                JOptionPane.showMessageDialog(null, "KODE PENGEMBALIAN SALAH, PERIKSA KEMBALI KODE PENGEMBALIAN");
                txtKodePeminjaman.requestFocus();
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /*--------------------------------------------------------INSERT DATABASE---------------------------------------- */
    public void TambahPengembalian() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        int BerhasilPeminjaman = 0, BerhasilKoleksi = 0;
        if (tableModel.getRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "KOLEKSI YANG DIKEMBALIKAN BELUM DI ISI");
            txtKodePeminjaman.requestFocus();
        } else {
            try {
                Statement stmt = koneksi.createStatement();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Pinjam.setKdPeminjaman(tableModel.getValueAt(i, 0).toString());
                    Pinjam.setTglKembali(String.valueOf(fm.format(txtTanggal.getDate())));
                    Pinjam.setDenda(Integer.valueOf(tableModel.getValueAt(i, 3).toString()));
                    getKdPeminjaman = Pinjam.getKdPeminjaman();
                    getTglKembali = Pinjam.getTglKembali();
                    getDenda = Pinjam.getDenda();
                    String SelectKdKoleksi = "SELECT * FROM T_Peminjaman WHERE Kd_Peminjaman = '" + getKdPeminjaman + "'";
                    ResultSet rs = stmt.executeQuery(SelectKdKoleksi);
                    if (rs.next()) {
                        Koleksi.setKdKoleksi(rs.getString("Kd_Koleksi"));
                        getKdKoleksi = Koleksi.getKdKoleksi();
                    }
                    String Ubah_TKoleksi = "UPDATE T_Koleksi SET Status ='TERSEDIA', Estimasi_Pengembalian = (NULL) WHERE Kd_Koleksi = '" + getKdKoleksi + "'";
                    String Ubah_TPeminjaman = "UPDATE T_Peminjaman SET Tgl_Kembali ='" + getTglKembali + "', Denda_Keterlambatan =" + getDenda + ", "
                            + "Status = 'DIKEMBALIKAN', Estimasi_Pengembalian =(NULL),Username ='" + LoginSession.getUsername() + "' "
                            + "WHERE Kd_Peminjaman = '" + getKdPeminjaman + "'";
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

    /*-----------------------------------------------POP-UP JFRAME---------------------------------------- */
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
                Pinjam.setKdPeminjaman(rs.getString("Kd_Peminjaman"));
                Anggota.setKdAnggota(rs.getString("Kd_Anggota"));
                Koleksi.setKdKoleksi(rs.getString("Kd_Koleksi"));
                Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                getKdPeminjaman = Pinjam.getKdPeminjaman();
                getKdAnggota = Anggota.getKdAnggota();
                getKdKoleksi = Koleksi.getKdKoleksi();
                getJudulKoleksi = Koleksi.getJudulKoleksi();
                dtm.addRow(new String[]{no + "", getKdPeminjaman, getKdAnggota, getKdKoleksi, getJudulKoleksi});
                no++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "KESALAHAN PADA DATABASE" + ex);
        }
        tblPinjam.setModel(dtm);
    }

    public void KlikTabelFaktur() {
        String[] data = new String[4];
        DefaultTableModel tableModel = (DefaultTableModel) tblPengembalian.getModel();
        int Keterlambatan = 0, Denda = 3000, HargaBuku = 0;
        //TANGGAN UNTUK PERBANDINGAN
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        String Tanggal = String.valueOf(fm.format(txtTanggal.getDate()));
        /* GET DATA */
        Pinjam.setKdPeminjaman(tblPinjam.getValueAt(tblPinjam.getSelectedRow(), 1).toString());
        Koleksi.setKdKoleksi(tblPinjam.getValueAt(tblPinjam.getSelectedRow(), 3).toString());
        Koleksi.setJudulKoleksi(tblPinjam.getValueAt(tblPinjam.getSelectedRow(), 4).toString());
        getKdPeminjaman = Pinjam.getKdPeminjaman();
        getKdKoleksi = Koleksi.getKdKoleksi();
        getJudulKoleksi = Koleksi.getJudulKoleksi();
        try {
            Statement stmt = koneksi.createStatement();
            String SelectPeminjaman = "SELECT DATEDIFF('" + Tanggal + "', T_Peminjaman.Estimasi_Pengembalian)  AS 'Keterlambatan' FROM T_Peminjaman "
                    + "WHERE Kd_Peminjaman= '" + getKdPeminjaman + "'";
            ResultSet rs2 = stmt.executeQuery(SelectPeminjaman);
            if (rs2.next()) {
                Keterlambatan = rs2.getInt("Keterlambatan");
                if (Keterlambatan < 0) {
                    Keterlambatan = 0;
                }
                Denda = Denda * Keterlambatan;
                Pinjam.setDenda(Denda);
                getDenda = Pinjam.getDenda();
            }
            data[0] = getKdPeminjaman;
            data[1] = getKdKoleksi;
            data[2] = "" + Keterlambatan;
            data[3] = "" + getDenda;
            tableModel.addRow(data);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void CariKode() {
        String kolom[] = {"NO", "Kode Peminjaman", "Kode Anggota", "Kode Koleksi", "Judul Koleksi"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom);
        String query = null;
        int n = 0;
        try {
            Statement stmt = koneksi.createStatement();
            Anggota.setKdAnggota(txtCari.getText());
            String Cari = Anggota.getKdAnggota();
            query = "SELECT * FROM T_Koleksi, T_Peminjaman WHERE T_Koleksi.Kd_Koleksi = T_Peminjaman.Kd_Koleksi "
                    + "AND Kd_Anggota LIKE '%" + Cari + "%' AND T_Peminjaman.Status = 'DIPINJAM'"
                    + "ORDER BY Judul_Koleksi ASC";

            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                Pinjam.setKdPeminjaman(rs.getString("Kd_Peminjaman"));
                Anggota.setKdAnggota(rs.getString("Kd_Anggota"));
                Koleksi.setKdKoleksi(rs.getString("Kd_Koleksi"));
                Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                getKdPeminjaman = Pinjam.getKdPeminjaman();
                getKdAnggota = Anggota.getKdAnggota();
                getKdKoleksi = Koleksi.getKdKoleksi();
                getJudulKoleksi = Koleksi.getJudulKoleksi();
                dtm.addRow(new String[]{no + "", getKdPeminjaman, getKdAnggota, getKdKoleksi, getJudulKoleksi});
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
        btnSubmit = new javax.swing.JButton();
        btnHapusList = new javax.swing.JButton();
        btnTambahPengembalian = new javax.swing.JButton();
        lblDetServices1 = new javax.swing.JLabel();
        tblDetServices1 = new javax.swing.JScrollPane();
        tblPengembalian = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        lblNoPol2 = new javax.swing.JLabel();
        txtKodePeminjaman = new javax.swing.JTextField();
        lblService4 = new javax.swing.JLabel();
        txtTanggal = new com.toedter.calendar.JDateChooser();
        btnCariKodePeminjaman = new javax.swing.JButton();
        btnPengembalian = new javax.swing.JPanel();
        lblNoPol6 = new javax.swing.JLabel();
        btnDenda = new javax.swing.JPanel();
        lblNoPol7 = new javax.swing.JLabel();

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
        lblNoPol3.setText("Cari berdasarkan Kode Anggota");

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel3.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel3.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory3.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory3.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Admin/ Pengembalian Koleksi");

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
                "Kode Peminjaman", "Nama Koleksi", "Keterlambatan", "Denda"
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

        txtKodePeminjaman.setBackground(new java.awt.Color(255, 255, 255));
        txtKodePeminjaman.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKodePeminjaman.setForeground(new java.awt.Color(51, 51, 51));
        txtKodePeminjaman.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService4.setForeground(new java.awt.Color(51, 51, 51));
        lblService4.setText("Kode Peminjaman");

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

        btnPengembalian.setBackground(new java.awt.Color(255, 255, 255));
        btnPengembalian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPengembalianMouseClicked(evt);
            }
        });

        lblNoPol6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNoPol6.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoPol6.setText("Ganti Buku >");

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
                .addComponent(lblNoPol6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

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

        javax.swing.GroupLayout mainPanel3Layout = new javax.swing.GroupLayout(mainPanel3);
        mainPanel3.setLayout(mainPanel3Layout);
        mainPanel3Layout.setHorizontalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelDirectory3, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDetServices1)
                    .addComponent(btnHapusList, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblService4)
                                    .addGroup(mainPanel3Layout.createSequentialGroup()
                                        .addComponent(txtKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnCariKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnTambahPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(tblDetServices1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addComponent(btnDenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        mainPanel3Layout.setVerticalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPengembalian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelDirectory3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(lblService4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTambahPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCariKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(btnHapusList, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnHapusListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusListMouseClicked
        hapusTablePeminjaman();
    }//GEN-LAST:event_btnHapusListMouseClicked

    private void btnTambahPengembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPengembalianActionPerformed
        SetTabelPengembalian();
    }//GEN-LAST:event_btnTambahPengembalianActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        Clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnCariKodePeminjamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariKodePeminjamanActionPerformed
        CariKode.show();
        TampiDataPinjaman();
    }//GEN-LAST:event_btnCariKodePeminjamanActionPerformed

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

    private void btnPengembalianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPengembalianMouseClicked
        V_Pengembalian_GantiBuku_Done VP = new V_Pengembalian_GantiBuku_Done();
        VP.show();
        this.dispose();
    }//GEN-LAST:event_btnPengembalianMouseClicked

    private void btnDendaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDendaMouseClicked
        V_Beres_Pengembalian_Denda VP = new V_Beres_Pengembalian_Denda();
        VP.show();
        this.dispose();
    }//GEN-LAST:event_btnDendaMouseClicked

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
            java.util.logging.Logger.getLogger(V_Beres_Pengembalian.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Pengembalian.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Pengembalian.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Pengembalian.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Beres_Pengembalian().setVisible(true);
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
    private javax.swing.JPanel btnDenda;
    private javax.swing.JButton btnHapusList;
    private javax.swing.JPanel btnPengembalian;
    private javax.swing.JButton btnPilihKode;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnTambahPengembalian;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblDetServices1;
    private javax.swing.JLabel lblNoPol2;
    private javax.swing.JLabel lblNoPol3;
    private javax.swing.JLabel lblNoPol6;
    private javax.swing.JLabel lblNoPol7;
    private javax.swing.JLabel lblService4;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JPanel mainPanel3;
    private javax.swing.JScrollPane tblDetServices1;
    private javax.swing.JScrollPane tblDetSparepart1;
    private javax.swing.JTable tblPengembalian;
    private javax.swing.JTable tblPinjam;
    private javax.swing.JTextField txtCari;
    private javax.swing.JLabel txtDataTidakDitemukan;
    private javax.swing.JTextField txtKodePeminjaman;
    private com.toedter.calendar.JDateChooser txtTanggal;
    // End of variables declaration//GEN-END:variables
}
