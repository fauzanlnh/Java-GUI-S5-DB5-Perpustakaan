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
public class V_Pengembalian_GantiBuku_Done extends javax.swing.JFrame {

    /**
     * Creates new form V_Pengembalian_GantiBuku_Done
     */
    Connection koneksi;

    public V_Pengembalian_GantiBuku_Done() {
        initComponents();
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_kuliah_provis_perpustakaan");
        this.setLocationRelativeTo(null);
        CariKode.setSize(575, 475);
        CariKode.setLocationRelativeTo(null);
        setJDate();
        setCMBJenis();
        setCMBTipe();
        setCMBKategori();
        setCMBTerbitan();
        setKdBuku();
    }

    public void setJDate() {
        Calendar today = Calendar.getInstance();
        txtTanggal.setDate(today.getTime());
    }

//SET CMB
    public void setKdBuku() {
        try {
            Statement stmt = koneksi.createStatement();
            String SelectKdBuku = "SELECT COUNT(Kd_Koleksi) AS NKodeKoleksi FROM T_Koleksi";
            ResultSet rs = stmt.executeQuery(SelectKdBuku);
            if (rs.next()) {
                int N = rs.getInt("NKodeKoleksi");
                N += 1;
                txtKodeKoleksi.setText("" + N);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void setCMBJenis() {
        try {
            String SelectKD = "SELECT * FROM T_Jenis_Koleksi";
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery(SelectKD);
            while (rs.next()) {
                String NamaJenisKoleksi = rs.getString("Nama_Jenis");
                cmbJenis.addItem(NamaJenisKoleksi);
            }
        } catch (SQLException e) {

        }
    }

    public void setCMBTipe() {
        try {
            String SelectKD = "SELECT * FROM T_Tipe_Koleksi";
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery(SelectKD);
            while (rs.next()) {
                String NamaTipeKoleksi = rs.getString("Nama_Tipe");
                cmbTipe.addItem(NamaTipeKoleksi);
            }
        } catch (SQLException e) {

        }
    }

    public void setCMBKategori() {
        try {
            String SelectKD = "SELECT * FROM T_Kategori_Koleksi";
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery(SelectKD);
            while (rs.next()) {
                String NamaKategoriKoleksi = rs.getString("Nama_Kategori");
                cmbKategori.addItem(NamaKategoriKoleksi);
            }
        } catch (SQLException e) {

        }
    }

    public void setCMBTerbitan() {
        try {
            String SelectKD = "SELECT * FROM T_Terbitan_Koleksi";
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery(SelectKD);
            while (rs.next()) {
                String NamaTerbitanKoleksi = rs.getString("Nama_Terbitan");
                cmbTerbitan.addItem(NamaTerbitanKoleksi);
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
        cmbJenis.setSelectedIndex(0);
        cmbTipe.setSelectedIndex(0);
        cmbKategori.setSelectedIndex(0);
        cmbTerbitan.setSelectedIndex(0);
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
    String JudulKoleksi;

    public void KlikTabelFaktur() {
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        String Tanggal = String.valueOf(fm.format(txtTanggal.getDate()));
        int Keterlambatan = 0, Denda = 3000;
        String Judul = null, NoRak = "";
        int Jenis = 0, Tipe = 0, Kategori = 0, Terbitan = 0;
        /* GET DATA */
        String getKdPeminjaman = tblPinjam.getValueAt(tblPinjam.getSelectedRow(), 1).toString();
        try {
            Statement stmt = koneksi.createStatement();
            String SelectPeminjaman = "SELECT DATEDIFF('" + Tanggal + "', T_Peminjaman.Estimasi_Pengembalian)  AS 'Keterlambatan' FROM T_Peminjaman";
            ResultSet rs2 = stmt.executeQuery(SelectPeminjaman);
            if (rs2.next()) {
                Keterlambatan = rs2.getInt("Keterlambatan");
                if (Keterlambatan < 0) {
                    Keterlambatan = 0;
                }
                Denda = Denda * Keterlambatan;
            }
            String SelectJudul = "SELECT * FROM T_Koleksi JOIN T_Peminjaman USING(Kd_Koleksi) WHERE Kd_Peminjaman='" + getKdPeminjaman + "'";
            ResultSet rs = stmt.executeQuery(SelectJudul);
            if (rs.next()) {
                Judul = rs.getString("Judul_Koleksi");
                NoRak = rs.getString("No_Rak");
                Jenis = rs.getInt("Kd_Jenis");
                Tipe = rs.getInt("Kd_Tipe");
                Kategori = rs.getInt("Kd_Kategori");
                Terbitan = rs.getInt("Kd_Terbitan");
            }
            txtDenda.setText("" + Denda);
            txtKodePeminjaman.setText(getKdPeminjaman);
            txtJudul.setText(Judul);
            txtNoRak.setText(NoRak);
            cmbJenis.setSelectedIndex(Jenis);
            cmbTipe.setSelectedIndex(Tipe);
            cmbKategori.setSelectedIndex(Kategori);
            cmbTerbitan.setSelectedIndex(Terbitan);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void SimpanData() {
        String tanggalMySQL = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tanggalMySQL);
        String Tanggal = String.valueOf(fm.format(txtTanggal.getDate()));
        String KdPeminjaman = txtKodePeminjaman.getText().toUpperCase();
        String Judul = txtJudul.getText().toUpperCase();
        String Pengarang = txtNamaPengarang.getText().toUpperCase();
        String Penerbit = txtNamaPenerbit.getText().toUpperCase();
        String TahunTerbit = txtTahunTerbit.getText().toUpperCase();
        String NoRak = txtNoRak.getText().toUpperCase();
        String Harga = txtHarga.getText().toUpperCase();
        String Denda = txtDenda.getText();
        int KdJenis = cmbJenis.getSelectedIndex();
        int KdTipe = cmbTipe.getSelectedIndex();
        int KdKategori = cmbKategori.getSelectedIndex();
        int KdTerbitan = cmbTerbitan.getSelectedIndex();
        int BUbahPeminjaman, BUbahKoleksi, BInputPengembalianGanti;
        String KdKoleksi = null;
        String KodeBukuPenganti = txtKodeKoleksi.getText();
        if (Judul.equals("") && Pengarang.equals("") && Penerbit.equals("") && TahunTerbit.equals("") && NoRak.equals("") && Harga.equals("") && KdJenis == 0 && KdTipe == 0 && KdKategori == 0 && KdTerbitan == 0) {
            JOptionPane.showMessageDialog(null, "FORM BELUM DI ISI");
            txtJudul.requestFocus();
        } else if (Judul.equals("")) {
            JOptionPane.showMessageDialog(null, "JUDUL TIDAK BOLEH KOSONG");
            txtJudul.requestFocus();
        } else if (Pengarang.equals("")) {
            JOptionPane.showMessageDialog(null, "NAMA PENGARANG TIDAK BOLEH KOSONG");
            txtNamaPengarang.requestFocus();
        } else if (Penerbit.equals("")) {
            JOptionPane.showMessageDialog(null, "NAMA PENERBIT TIDAK BOLEH KOSONG");
            txtNamaPenerbit.requestFocus();
        } else if (TahunTerbit.equals("")) {
            JOptionPane.showMessageDialog(null, "TAHUN TERBIT TIDAK BOLEH KOSONG");
            txtTahunTerbit.requestFocus();
        } else if (NoRak.equals("")) {
            JOptionPane.showMessageDialog(null, "NO RAK TIDAK BOLEH KOSONG");
            txtNoRak.requestFocus();
        } else if (Harga.equals("")) {
            JOptionPane.showMessageDialog(null, "HARGA BUKU TIDAK BOLEH KOSONG");
            txtHarga.requestFocus();
        } else if (KdJenis == 0) {
            JOptionPane.showMessageDialog(null, "JENIS BUKU TIDAK BOLEH KOSONG");
            cmbJenis.requestFocus();
        } else if (KdTipe == 0) {
            JOptionPane.showMessageDialog(null, "TIPE BUKU TIDAK BOLEH KOSONG");
            cmbTipe.requestFocus();
        } else if (KdKategori == 0) {
            JOptionPane.showMessageDialog(null, "KATEGORI BUKU TIDAK BOLEH KOSONG");
            cmbKategori.requestFocus();
        } else if (KdTerbitan == 0) {
            JOptionPane.showMessageDialog(null, "JENIS TERBITAN BUKU TIDAK BOLEH KOSONG");
            cmbTerbitan.requestFocus();
        } else {
            try {
                Statement stmt = koneksi.createStatement();
                String getKdKoleksi = "SELECT Kd_Koleksi FROM T_Koleksi, T_Peminjaman WHERE T_Peminjaman.Kd_Koleksi = T_Koleksi.Kd_Koleksi AND Kd_Peminjaman = '" + KdPeminjaman + "'";
                ResultSet rs = stmt.executeQuery(getKdKoleksi);
                if (rs.next()) {
                    KdKoleksi = rs.getString("Kd_Koleksi");
                }
                //UBAH STATUS TABEL PEMINJAMAN
                String Ubah_TPeminjaman = "UPDATE T_Peminjaman SET Tgl_Kembali ='" + Tanggal + "', Denda_Keterlambatan ='" + Denda + "', Status = 'HILANG', Estimasi_Pengembalian ='(NULL)' "
                        + "WHERE Kd_Peminjaman = '" + KdPeminjaman + "'";
                BUbahPeminjaman = stmt.executeUpdate(Ubah_TPeminjaman);
                //UBAH STATUS TABEL KOLEKSI
                String Ubah_TKoleksi = "UPDATE T_Koleksi SET Status ='HILANG', Estimasi_Pengembalian = (NULL) WHERE Kd_Koleksi = '" + KdKoleksi + "'";
                BUbahKoleksi = stmt.executeUpdate(Ubah_TKoleksi);
                //INPUT TABEL PENGEMBALIAN GANTI BUKU
                String sqlInput = "INSERT INTO T_Pengembalian_Ganti(Tanggal_Ganti, Kode_Koleksi, Kd_Peminjaman) VALUES('" + Tanggal + "', " + KodeBukuPenganti + ", '" + KdPeminjaman + "')";
                BInputPengembalianGanti = stmt.executeUpdate(sqlInput);
                //INPUT TABEL KOLEKSI
                String TambahKoleksi = "INSERT INTO T_Koleksi (Judul_Koleksi, Nama_Pengarang, Nama_Penerbit, Tahun_Terbit, No_Rak, Kd_Jenis, Kd_Tipe, Kd_Kategori, Kd_Terbitan, Status, Harga) VALUES "
                        + "('" + Judul + "','" + Pengarang + "','" + Penerbit + "','" + TahunTerbit + "','" + NoRak + "','" + KdJenis + "','" + KdTipe + "','" + KdKategori + "','" + KdTerbitan + "','TERSEDIA','" + Harga + "')";
                int BerhasilTambah = stmt.executeUpdate(TambahKoleksi);

                if (BerhasilTambah > 0 && BInputPengembalianGanti > 0 && BUbahKoleksi > 0 && BUbahPeminjaman > 0) {
                    JOptionPane.showMessageDialog(null, "DATA BERHASIL DIMASUKKAN");
                    Clear();
                } else {
                    JOptionPane.showMessageDialog(null, "DATA GAGAL DIMASUKKAN");
                }
                System.out.println(TambahKoleksi);
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    /*-------------------------------------------------------------------------------JFRAME POP-UP----------------------------------------------------*/
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
        btnDenda = new javax.swing.JPanel();
        lblNoPol7 = new javax.swing.JLabel();
        lblNoPol9 = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        btnCariKodePeminjaman = new javax.swing.JButton();
        txtKodePeminjaman = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        txtKasir = new javax.swing.JLabel();
        txtTanggal = new com.toedter.calendar.JDateChooser();
        lblNoPol2 = new javax.swing.JLabel();
        lblService5 = new javax.swing.JLabel();
        lblNoPol10 = new javax.swing.JLabel();
        cmbJenis = new javax.swing.JComboBox<>();
        txtNamaPengarang = new javax.swing.JTextField();
        lblService2 = new javax.swing.JLabel();
        lblNoPol11 = new javax.swing.JLabel();
        lblNoPol8 = new javax.swing.JLabel();
        txtJudul = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        cmbTerbitan = new javax.swing.JComboBox<>();
        lblService4 = new javax.swing.JLabel();
        cmbKategori = new javax.swing.JComboBox<>();
        lblService1 = new javax.swing.JLabel();
        txtNoRak = new javax.swing.JTextField();
        cmbTipe = new javax.swing.JComboBox<>();
        txtTahunTerbit = new javax.swing.JTextField();
        lblService6 = new javax.swing.JLabel();
        lblService3 = new javax.swing.JLabel();
        lblService7 = new javax.swing.JLabel();
        txtNamaPenerbit = new javax.swing.JTextField();
        txtDenda = new javax.swing.JTextField();
        lblNoPol4 = new javax.swing.JLabel();
        txtKodeKoleksi = new javax.swing.JTextField();
        lblNoPol5 = new javax.swing.JLabel();

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
                .addContainerGap(12, Short.MAX_VALUE))
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
        jLabel9.setText("Admin/Pengembalian Buku/Ganti Buku");

        javax.swing.GroupLayout PanelDirectory3Layout = new javax.swing.GroupLayout(PanelDirectory3);
        PanelDirectory3.setLayout(PanelDirectory3Layout);
        PanelDirectory3Layout.setHorizontalGroup(
            PanelDirectory3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectory3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9)
                .addContainerGap(88, Short.MAX_VALUE))
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

        lblService5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService5.setForeground(new java.awt.Color(51, 51, 51));
        lblService5.setText("Kategori");

        lblNoPol10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol10.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol10.setText("Judul");

        cmbJenis.setBackground(new java.awt.Color(255, 255, 255));
        cmbJenis.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbJenis.setForeground(new java.awt.Color(51, 51, 51));
        cmbJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

        txtNamaPengarang.setBackground(new java.awt.Color(255, 255, 255));
        txtNamaPengarang.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtNamaPengarang.setForeground(new java.awt.Color(51, 51, 51));
        txtNamaPengarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService2.setForeground(new java.awt.Color(51, 51, 51));
        lblService2.setText("Tahun Terbit");

        lblNoPol11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol11.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol11.setText("Nama Penerbit");

        lblNoPol8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol8.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol8.setText("Nama Pengarang");

        txtJudul.setBackground(new java.awt.Color(255, 255, 255));
        txtJudul.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtJudul.setForeground(new java.awt.Color(51, 51, 51));
        txtJudul.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        txtHarga.setBackground(new java.awt.Color(255, 255, 255));
        txtHarga.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtHarga.setForeground(new java.awt.Color(51, 51, 51));
        txtHarga.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        cmbTerbitan.setBackground(new java.awt.Color(255, 255, 255));
        cmbTerbitan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbTerbitan.setForeground(new java.awt.Color(51, 51, 51));
        cmbTerbitan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

        lblService4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService4.setForeground(new java.awt.Color(51, 51, 51));
        lblService4.setText("Tipe");

        cmbKategori.setBackground(new java.awt.Color(255, 255, 255));
        cmbKategori.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbKategori.setForeground(new java.awt.Color(51, 51, 51));
        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

        lblService1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService1.setForeground(new java.awt.Color(51, 51, 51));
        lblService1.setText("Jenis");

        txtNoRak.setBackground(new java.awt.Color(255, 255, 255));
        txtNoRak.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtNoRak.setForeground(new java.awt.Color(51, 51, 51));
        txtNoRak.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        cmbTipe.setBackground(new java.awt.Color(255, 255, 255));
        cmbTipe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbTipe.setForeground(new java.awt.Color(51, 51, 51));
        cmbTipe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

        txtTahunTerbit.setBackground(new java.awt.Color(255, 255, 255));
        txtTahunTerbit.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtTahunTerbit.setForeground(new java.awt.Color(51, 51, 51));
        txtTahunTerbit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService6.setForeground(new java.awt.Color(51, 51, 51));
        lblService6.setText("Terbitan");

        lblService3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService3.setForeground(new java.awt.Color(51, 51, 51));
        lblService3.setText("No Rak");

        lblService7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService7.setForeground(new java.awt.Color(51, 51, 51));
        lblService7.setText("Harga");

        txtNamaPenerbit.setBackground(new java.awt.Color(255, 255, 255));
        txtNamaPenerbit.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtNamaPenerbit.setForeground(new java.awt.Color(51, 51, 51));
        txtNamaPenerbit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        txtDenda.setBackground(new java.awt.Color(255, 255, 255));
        txtDenda.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtDenda.setForeground(new java.awt.Color(51, 51, 51));
        txtDenda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblNoPol4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol4.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol4.setText("Denda Keterlambatan");

        txtKodeKoleksi.setBackground(new java.awt.Color(255, 255, 255));
        txtKodeKoleksi.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKodeKoleksi.setForeground(new java.awt.Color(51, 51, 51));
        txtKodeKoleksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblNoPol5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol5.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol5.setText("Kode Buku");

        javax.swing.GroupLayout mainPanel3Layout = new javax.swing.GroupLayout(mainPanel3);
        mainPanel3.setLayout(mainPanel3Layout);
        mainPanel3Layout.setHorizontalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoPol8)
                            .addComponent(lblNoPol10)
                            .addComponent(lblNoPol11)
                            .addComponent(lblService2)
                            .addComponent(lblService3))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtNamaPenerbit, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNamaPengarang, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtJudul, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(mainPanel3Layout.createSequentialGroup()
                                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(mainPanel3Layout.createSequentialGroup()
                                    .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainPanel3Layout.createSequentialGroup()
                                            .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(lblService4))
                                        .addGroup(mainPanel3Layout.createSequentialGroup()
                                            .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(txtNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cmbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblService6)
                                                .addComponent(lblService5))))
                                    .addGap(68, 68, 68)
                                    .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cmbTerbitan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cmbKategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cmbTipe, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(lblService1)
                    .addComponent(lblService7)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoPol9)
                            .addComponent(txtKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addComponent(btnCariKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoPol2)
                            .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDenda, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNoPol4)
                            .addComponent(txtKodeKoleksi, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNoPol5))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(btnDenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(btnDenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(PanelDirectory3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addComponent(lblNoPol9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnCariKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtKodePeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addComponent(lblNoPol4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(lblNoPol2)
                        .addGap(35, 35, 35))
                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addComponent(txtKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblNoPol10)
                                    .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNoPol5))
                                .addGap(20, 20, 20)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblNoPol8)
                                    .addComponent(txtNamaPengarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtKodeKoleksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNoPol11)
                                    .addComponent(txtNamaPenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanel3Layout.createSequentialGroup()
                                        .addComponent(lblService2)
                                        .addGap(28, 28, 28)
                                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblService3)
                                            .addComponent(lblService5)
                                            .addComponent(txtNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblService4)))
                                .addGap(19, 19, 19)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblService1)
                                    .addComponent(cmbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblService6)))
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addComponent(cmbTipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbTerbitan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblService7)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(117, 117, 117))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mainPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPengembalianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPengembalianMouseClicked
        V_Pengembalian_GantiBuku_Done VP = new V_Pengembalian_GantiBuku_Done();
        VP.show();
        this.dispose();
    }//GEN-LAST:event_btnPengembalianMouseClicked

    private void btnDendaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDendaMouseClicked
        V_Pengembalian_Denda_Done VP = new V_Pengembalian_Denda_Done();
        VP.show();
        this.dispose();
    }//GEN-LAST:event_btnDendaMouseClicked

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Data Yang Dimasukkan Benar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == 0) {

        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnCariKodePeminjamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariKodePeminjamanActionPerformed
        CariKode.show();
        TampiDataPinjaman();
    }//GEN-LAST:event_btnCariKodePeminjamanActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        //   Clear();
    }//GEN-LAST:event_btnClearActionPerformed

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
            java.util.logging.Logger.getLogger(V_Pengembalian_GantiBuku_Done.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Pengembalian_GantiBuku_Done.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Pengembalian_GantiBuku_Done.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Pengembalian_GantiBuku_Done.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Pengembalian_GantiBuku_Done().setVisible(true);
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
    private javax.swing.JPanel btnPengembalian;
    private javax.swing.JButton btnPilihKode;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> cmbJenis;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JComboBox<String> cmbTerbitan;
    private javax.swing.JComboBox<String> cmbTipe;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblNoPol10;
    private javax.swing.JLabel lblNoPol11;
    private javax.swing.JLabel lblNoPol2;
    private javax.swing.JLabel lblNoPol3;
    private javax.swing.JLabel lblNoPol4;
    private javax.swing.JLabel lblNoPol5;
    private javax.swing.JLabel lblNoPol6;
    private javax.swing.JLabel lblNoPol7;
    private javax.swing.JLabel lblNoPol8;
    private javax.swing.JLabel lblNoPol9;
    private javax.swing.JLabel lblService1;
    private javax.swing.JLabel lblService2;
    private javax.swing.JLabel lblService3;
    private javax.swing.JLabel lblService4;
    private javax.swing.JLabel lblService5;
    private javax.swing.JLabel lblService6;
    private javax.swing.JLabel lblService7;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JPanel mainPanel3;
    private javax.swing.JScrollPane tblDetSparepart1;
    private javax.swing.JTable tblPinjam;
    private javax.swing.JTextField txtCari;
    private javax.swing.JLabel txtDataTidakDitemukan;
    private javax.swing.JTextField txtDenda;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJudul;
    public javax.swing.JLabel txtKasir;
    private javax.swing.JTextField txtKodeKoleksi;
    private javax.swing.JTextField txtKodePeminjaman;
    private javax.swing.JTextField txtNamaPenerbit;
    private javax.swing.JTextField txtNamaPengarang;
    private javax.swing.JTextField txtNoRak;
    private javax.swing.JTextField txtTahunTerbit;
    private com.toedter.calendar.JDateChooser txtTanggal;
    // End of variables declaration//GEN-END:variables
}
