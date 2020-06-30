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

/**
 *
 * @author Fauzanlh
 */
public class V_Koleksi_Done extends javax.swing.JFrame {

    /**
     * Creates new form V_Koleksi_Done
     */
    Connection koneksi;

    public V_Koleksi_Done() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_kuliah_provis_perpustakaan");
        setCMBJenis();
        setCMBTipe();
        setCMBKategori();
        setCMBTerbitan();
    }

    //SET CMB
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

    public void SimpanData() {
        String Judul = txtJudul.getText().toUpperCase();
        String Pengarang = txtNamaPengarang.getText().toUpperCase();
        String Penerbit = txtNamaPenerbit.getText().toUpperCase();
        String TahunTerbit = txtTahunTerbit.getText().toUpperCase();
        String NoRak = txtNoRak.getText().toUpperCase();
        String Harga = txtHarga.getText().toUpperCase();
        int KdJenis = cmbJenis.getSelectedIndex();
        int KdTipe = cmbTipe.getSelectedIndex();
        int KdKategori = cmbKategori.getSelectedIndex();
        int KdTerbitan = cmbTerbitan.getSelectedIndex();
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
                String TambahKoleksi = "INSERT INTO T_Koleksi (Judul_Koleksi, Nama_Pengarang, Nama_Penerbit, Tahun_Terbit, No_Rak, Kd_Jenis, Kd_Tipe, Kd_Kategori, Kd_Terbitan, Status, Harga) VALUES "
                        + "('" + Judul + "','" + Pengarang + "','" + Penerbit + "','" + TahunTerbit + "','" + NoRak + "','" + KdJenis + "','" + KdTipe + "','" + KdKategori + "','" + KdTerbitan + "','TERSEDIA','" + Harga + "')";
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
        lblService1 = new javax.swing.JLabel();
        lblNoPol10 = new javax.swing.JLabel();
        txtNamaPenerbit = new javax.swing.JTextField();
        lblService2 = new javax.swing.JLabel();
        cmbJenis = new javax.swing.JComboBox<>();
        lblService4 = new javax.swing.JLabel();
        lblService5 = new javax.swing.JLabel();
        lblService6 = new javax.swing.JLabel();
        lblService7 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        cmbTipe = new javax.swing.JComboBox<>();
        cmbKategori = new javax.swing.JComboBox<>();
        cmbTerbitan = new javax.swing.JComboBox<>();
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

        lblService1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService1.setForeground(new java.awt.Color(51, 51, 51));
        lblService1.setText("Jenis");

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

        cmbJenis.setBackground(new java.awt.Color(255, 255, 255));
        cmbJenis.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbJenis.setForeground(new java.awt.Color(51, 51, 51));
        cmbJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

        lblService4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService4.setForeground(new java.awt.Color(51, 51, 51));
        lblService4.setText("Tipe");

        lblService5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService5.setForeground(new java.awt.Color(51, 51, 51));
        lblService5.setText("Kategori");

        lblService6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService6.setForeground(new java.awt.Color(51, 51, 51));
        lblService6.setText("Terbitan");

        lblService7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService7.setForeground(new java.awt.Color(51, 51, 51));
        lblService7.setText("Harga");

        txtHarga.setBackground(new java.awt.Color(255, 255, 255));
        txtHarga.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtHarga.setForeground(new java.awt.Color(51, 51, 51));
        txtHarga.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        cmbTipe.setBackground(new java.awt.Color(255, 255, 255));
        cmbTipe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbTipe.setForeground(new java.awt.Color(51, 51, 51));
        cmbTipe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

        cmbKategori.setBackground(new java.awt.Color(255, 255, 255));
        cmbKategori.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbKategori.setForeground(new java.awt.Color(51, 51, 51));
        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

        cmbTerbitan.setBackground(new java.awt.Color(255, 255, 255));
        cmbTerbitan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbTerbitan.setForeground(new java.awt.Color(51, 51, 51));
        cmbTerbitan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

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
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoPol8)
                            .addComponent(lblNoPol9)
                            .addComponent(lblNoPol10)
                            .addComponent(lblService2)
                            .addComponent(lblService3)
                            .addComponent(lblService1)
                            .addComponent(lblService7))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addComponent(txtNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblService5))
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cmbJenis, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtHarga, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblService4)
                                    .addComponent(lblService6)))
                            .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNamaPenerbit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                .addComponent(txtNamaPengarang, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtJudul, javax.swing.GroupLayout.Alignment.LEADING)))))
                .addGap(6, 6, 6)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbTerbitan, 0, 170, Short.MAX_VALUE)
                    .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cmbKategori, 0, 170, Short.MAX_VALUE)
                        .addComponent(cmbTipe, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanel3Layout.createSequentialGroup()
                                        .addComponent(lblService2)
                                        .addGap(23, 23, 23))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanel3Layout.createSequentialGroup()
                                        .addComponent(cmbTipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblService3)
                                        .addComponent(lblService5)
                                        .addComponent(txtNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanel3Layout.createSequentialGroup()
                                        .addGap(18, 18, Short.MAX_VALUE)
                                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblService1)
                                            .addComponent(lblService6)
                                            .addComponent(cmbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblService7)
                                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(28, 28, 28))
                                    .addGroup(mainPanel3Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbTerbitan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblService4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
            .addComponent(mainPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 610, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(V_Koleksi_Done.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Koleksi_Done.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Koleksi_Done.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Koleksi_Done.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Koleksi_Done().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDirectory3;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> cmbJenis;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JComboBox<String> cmbTerbitan;
    private javax.swing.JComboBox<String> cmbTipe;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblNoPol10;
    private javax.swing.JLabel lblNoPol8;
    private javax.swing.JLabel lblNoPol9;
    private javax.swing.JLabel lblService1;
    private javax.swing.JLabel lblService2;
    private javax.swing.JLabel lblService3;
    private javax.swing.JLabel lblService4;
    private javax.swing.JLabel lblService5;
    private javax.swing.JLabel lblService6;
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