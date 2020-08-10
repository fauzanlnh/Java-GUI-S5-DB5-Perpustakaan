/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package V_Admin;

import Class.C_KategoriKoleksi;
import Class.C_Koleksi;
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


public class V_Beres_Data_Koleksi extends javax.swing.JFrame {

    /**
     * Creates new form V_Beres_Data_Koleksi
     */
    Connection koneksi;
    C_KategoriKoleksi Kategori = new C_KategoriKoleksi();
    C_Koleksi Koleksi = new C_Koleksi();
    String getKdKoleksi, getJudul, getPengarang, getNoRak, getStatus, getTahunTerbit;
    String getKdKategori, getKategori;
    
    public V_Beres_Data_Koleksi() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
        showData();
        UbahData.setLocationRelativeTo(null);
        UbahData.setSize(510, 550);
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
            JOptionPane.showMessageDialog(null, "KESALAHAN PADA DATABASE ATAU QUERY");
        }
    }
    
    public void showData() {
        String kolom[] = {"No", "Kode Koleksi", "Nama Koleksi", "Nama Pengarang", "Kategori", "No Rak", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(null, kolom);
        String query = null;
        try {
            Statement stmt = koneksi.createStatement();
            query = "SELECT * FROM T_Koleksi JOIN T_Kategori_koleksi USING(Kd_Kategori) ORDER BY Kd_Koleksi ASC";
            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                Koleksi.setKdKoleksi(rs.getString("Kd_Koleksi"));
                Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                Koleksi.setPengarang(rs.getString("Nama_Pengarang"));
                Kategori.setNmKategori(rs.getString("Nama_Kategori"));
                Koleksi.setNoRak(rs.getString("No_Rak"));
                Koleksi.setStatus(rs.getString("Status"));
                getKdKoleksi = Koleksi.getKdKoleksi();
                getJudul = Koleksi.getJudulKoleksi();
                getPengarang = Koleksi.getPengarang();
                getKategori = Kategori.getNmKategori();
                getNoRak = Koleksi.getNoRak();
                getStatus = Koleksi.getStatus();
                tableModel.addRow(new String[]{no + "", getKdKoleksi, getJudul, getPengarang, getKategori, getNoRak, getStatus});
                no++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "KESALAHAN PADA DATABASE" + ex);
        }
        tblKoleksi.setModel(tableModel);
    }
    
    public void CariData() {
        String kolom[] = {"No", "Kode Koleksi", "Nama Koleksi", "Nama Pengarang", "Kategori", "No Rak", "Status"};;
        DefaultTableModel tableModel = new DefaultTableModel(null, kolom);
        String query = null;
        try {
            Statement stmt = koneksi.createStatement();
            //CARI BEDASARKAN JUDUL
            if (cmbCari.getSelectedIndex() == 1) {
                Koleksi.setJudulKoleksi(txtCari.getText());
                String Cari = Koleksi.getJudulKoleksi();
                query = "SELECT * FROM T_Koleksi JOIN T_Kategori_koleksi USING(Kd_Kategori) WHERE Judul_Koleksi LIKE '%" + Cari + "%' "
                        + "ORDER BY Kd_Koleksi ASC";
            } else if (cmbCari.getSelectedIndex() == 2) {
                Koleksi.setPengarang(txtCari.getText());
                String Cari = Koleksi.getJudulKoleksi();
                query = "SELECT * FROM T_Koleksi JOIN T_Kategori_koleksi USING(Kd_Kategori) WHERE Nama_Pengarang LIKE '%" + Cari + "%' "
                        + "ORDER BY Kd_Koleksi ASC";
            }
            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                Koleksi.setKdKoleksi(rs.getString("Kd_Koleksi"));
                Koleksi.setJudulKoleksi(rs.getString("Judul_Koleksi"));
                Koleksi.setPengarang(rs.getString("Nama_Pengarang"));
                Kategori.setNmKategori(rs.getString("Nama_Kategori"));
                Koleksi.setNoRak(rs.getString("No_Rak"));
                Koleksi.setStatus(rs.getString("Status"));
                getKdKoleksi = Koleksi.getKdKoleksi();
                getJudul = Koleksi.getJudulKoleksi();
                getPengarang = Koleksi.getPengarang();
                getKategori = Kategori.getNmKategori();
                getNoRak = Koleksi.getNoRak();
                getStatus = Koleksi.getStatus();
                tableModel.addRow(new String[]{no + "", getKdKoleksi, getJudul, getPengarang, getKategori, getNoRak, getStatus});
                no++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "KESALAHAN PADA DATABASE" + ex);
        }
        
        tblKoleksi.setModel(tableModel);
        
    }

    /*-------------------------------------------------JFRAME POP UP--------------------------------*/
    public void Move() {
        Koleksi.setKdKoleksi(tblKoleksi.getValueAt(tblKoleksi.getSelectedRow(), 1).toString());
        Koleksi.setJudulKoleksi(tblKoleksi.getValueAt(tblKoleksi.getSelectedRow(), 2).toString());
        Koleksi.setPengarang(tblKoleksi.getValueAt(tblKoleksi.getSelectedRow(), 3).toString());
        Kategori.setNmKategori(tblKoleksi.getValueAt(tblKoleksi.getSelectedRow(), 4).toString());
        Koleksi.setNoRak(tblKoleksi.getValueAt(tblKoleksi.getSelectedRow(), 5).toString());
        Koleksi.setStatus(tblKoleksi.getValueAt(tblKoleksi.getSelectedRow(), 6).toString());
        getKdKoleksi = Koleksi.getKdKoleksi();
        getJudul = Koleksi.getJudulKoleksi();
        getPengarang = Koleksi.getPengarang();
        getKategori = Kategori.getNmKategori();
        getNoRak = Koleksi.getNoRak();
        getStatus = Koleksi.getStatus();
        
        txtKode.setText(getKdKoleksi);
        txtJudul.setText(getJudul);
        txtPengarang.setText(getPengarang);
        cmbKategori.setSelectedItem(getKategori);
        txtNoRak.setText(getNoRak);
        cmbStatus.setSelectedItem(getStatus);
        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM T_Koleksi WHERE Kd_Koleksi ='" + getKdKoleksi + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                Koleksi.setTahunTerbit(rs.getString("Tahun_Terbit"));
                txtTahunTerbit.setText(Koleksi.getTahunTerbit());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "KESALAHAN PADA DATABASE");
        }
    }
    
    public void UbahData() {
        if (txtKode.getText().equals("") && txtJudul.getText().equals("") && txtPengarang.getText().equals("") && cmbKategori.getSelectedItem().equals("-") && txtTahunTerbit.getText().equals("") && txtNoRak.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "FORM HARUS DIISI");
        } else if (txtKode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "KODE KOLEKSI HARUS DIISI");
        } else if (txtJudul.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "JUDUL KOLEKSI HARUS DIISI");
        } else if (txtPengarang.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "NAMA PENGARANG HARUS DIISI");
        } else if (cmbKategori.getSelectedItem().equals("-")) {
            JOptionPane.showMessageDialog(null, "KATEGORI HARUS DIISI");
        } else if (txtTahunTerbit.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "TAHUN TERBIT HARUS DIISI");
        } else if (txtNoRak.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "NO RAK HARUS DIISI");
        } else {
            Koleksi.setKdKoleksi(txtKode.getText().toUpperCase());
            Koleksi.setJudulKoleksi(txtJudul.getText().toUpperCase());
            Koleksi.setPengarang(txtPengarang.getText().toUpperCase());
            Koleksi.setNoRak(txtNoRak.getText().toUpperCase());
            Koleksi.setStatus(cmbStatus.getSelectedItem().toString().toUpperCase());
            Koleksi.setTahunTerbit(txtTahunTerbit.getText());
            Kategori.setNmKategori(cmbKategori.getSelectedItem().toString());
            getKdKoleksi = Koleksi.getKdKoleksi();
            getJudul = Koleksi.getJudulKoleksi();
            getPengarang = Koleksi.getPengarang();
            getKategori = Kategori.getNmKategori();
            getTahunTerbit = Koleksi.getTahunTerbit();
            getNoRak = Koleksi.getNoRak();
            getStatus = Koleksi.getStatus();
            try {
                Statement stmt = koneksi.createStatement();
                String SelectKdKategori = "SELECT * FROM T_Kategori_Koleksi WHERE Nama_Kategori ='" + getKategori + "'";
                ResultSet rs = stmt.executeQuery(SelectKdKategori);
                if (rs.next()) {
                    Kategori.setKdKategori(rs.getString("Kd_Kategori"));
                    getKdKategori = Kategori.getKdKategori();
                }
                String Ubah = "UPDATE T_Koleksi SET Judul_Koleksi = '" + getJudul + "',Nama_Pengarang = '" + getPengarang + "', No_Rak='" + getNoRak + "', Status ='" + getStatus + "', "
                        + "Kd_Kategori = '" + getKdKategori + "', Tahun_Terbit ='" + getTahunTerbit + "' "
                        + "WHERE Kd_Koleksi = '" + getKdKoleksi + "'";
                int BerhasilUbah = stmt.executeUpdate(Ubah);
                if (BerhasilUbah > 0) {
                    JOptionPane.showMessageDialog(null, "DATA KOLEKSI BERHASIL DI UBAH");
                    System.out.println(Ubah);
                    UbahData.dispose();
                    showData();
                } else {
                    JOptionPane.showMessageDialog(null, "DATA KOLEKSI GAGAL DI UBAH");
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

        UbahData = new javax.swing.JFrame();
        mainPanel2 = new javax.swing.JPanel();
        PanelDirectory1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();
        lblNoPol9 = new javax.swing.JLabel();
        txtJudul = new javax.swing.JTextField();
        lblNoPol8 = new javax.swing.JLabel();
        txtPengarang = new javax.swing.JTextField();
        lblService5 = new javax.swing.JLabel();
        cmbKategori = new javax.swing.JComboBox<>();
        lblService2 = new javax.swing.JLabel();
        txtTahunTerbit = new javax.swing.JTextField();
        lblService3 = new javax.swing.JLabel();
        txtNoRak = new javax.swing.JTextField();
        lblService7 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        lblNoPol11 = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        mainPanel1 = new javax.swing.JPanel();
        PanelDirectory = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblKoleksi = new javax.swing.JTable();
        btnPrint = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        cmbCari = new javax.swing.JComboBox<>();
        lblBulan = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        btnUbah = new javax.swing.JButton();

        mainPanel2.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel2.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory1.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory1.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Koleksi/Ubah Data");

        javax.swing.GroupLayout PanelDirectory1Layout = new javax.swing.GroupLayout(PanelDirectory1);
        PanelDirectory1.setLayout(PanelDirectory1Layout);
        PanelDirectory1Layout.setHorizontalGroup(
            PanelDirectory1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectory1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelDirectory1Layout.setVerticalGroup(
            PanelDirectory1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectory1Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(39, 39, 39))
        );

        btnCancel.setBackground(new java.awt.Color(240, 240, 240));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(51, 51, 51));
        btnCancel.setText("Cancel");
        btnCancel.setBorder(null);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSubmit.setBackground(new java.awt.Color(240, 240, 240));
        btnSubmit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(51, 51, 51));
        btnSubmit.setText("Ubah");
        btnSubmit.setBorder(null);
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        lblNoPol9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol9.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol9.setText("Judul");

        txtJudul.setBackground(new java.awt.Color(255, 255, 255));
        txtJudul.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtJudul.setForeground(new java.awt.Color(51, 51, 51));
        txtJudul.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblNoPol8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol8.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol8.setText("Nama Pengarang");

        txtPengarang.setBackground(new java.awt.Color(255, 255, 255));
        txtPengarang.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtPengarang.setForeground(new java.awt.Color(51, 51, 51));
        txtPengarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService5.setForeground(new java.awt.Color(51, 51, 51));
        lblService5.setText("Kategori");

        cmbKategori.setBackground(new java.awt.Color(255, 255, 255));
        cmbKategori.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbKategori.setForeground(new java.awt.Color(51, 51, 51));
        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));

        lblService2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService2.setForeground(new java.awt.Color(51, 51, 51));
        lblService2.setText("Tahun Terbit");

        txtTahunTerbit.setBackground(new java.awt.Color(255, 255, 255));
        txtTahunTerbit.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtTahunTerbit.setForeground(new java.awt.Color(51, 51, 51));
        txtTahunTerbit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService3.setForeground(new java.awt.Color(51, 51, 51));
        lblService3.setText("No Rak");

        txtNoRak.setBackground(new java.awt.Color(255, 255, 255));
        txtNoRak.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtNoRak.setForeground(new java.awt.Color(51, 51, 51));
        txtNoRak.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblService7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblService7.setForeground(new java.awt.Color(51, 51, 51));
        lblService7.setText("Status");

        cmbStatus.setBackground(new java.awt.Color(255, 255, 255));
        cmbStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbStatus.setForeground(new java.awt.Color(51, 51, 51));
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tersedia", "Dipinjam", "Hilang" }));

        lblNoPol11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol11.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol11.setText("Kode");

        txtKode.setEditable(false);
        txtKode.setBackground(new java.awt.Color(255, 255, 255));
        txtKode.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKode.setForeground(new java.awt.Color(51, 51, 51));
        txtKode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        javax.swing.GroupLayout mainPanel2Layout = new javax.swing.GroupLayout(mainPanel2);
        mainPanel2.setLayout(mainPanel2Layout);
        mainPanel2Layout.setHorizontalGroup(
            mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblService2)
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoPol9)
                            .addComponent(lblService5)
                            .addComponent(lblNoPol11)
                            .addComponent(lblService3)
                            .addComponent(lblNoPol8)
                            .addComponent(lblService7))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(mainPanel2Layout.createSequentialGroup()
                                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtJudul)
                                    .addComponent(cmbKategori, javax.swing.GroupLayout.Alignment.TRAILING, 0, 231, Short.MAX_VALUE)
                                    .addComponent(txtKode, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtPengarang, javax.swing.GroupLayout.Alignment.TRAILING))))))
                .addContainerGap(141, Short.MAX_VALUE))
            .addComponent(PanelDirectory1, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
        );
        mainPanel2Layout.setVerticalGroup(
            mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(PanelDirectory1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoPol11)
                    .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNoPol9))
                .addGap(18, 18, 18)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPengarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNoPol8))
                .addGap(18, 18, 18)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblService5))
                .addGap(18, 18, 18)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblService2)
                    .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblService3)
                    .addComponent(txtNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblService7)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout UbahDataLayout = new javax.swing.GroupLayout(UbahData.getContentPane());
        UbahData.getContentPane().setLayout(UbahDataLayout);
        UbahDataLayout.setHorizontalGroup(
            UbahDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UbahDataLayout.createSequentialGroup()
                .addComponent(mainPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        UbahDataLayout.setVerticalGroup(
            UbahDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel1.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel1.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Data/Koleksi");

        javax.swing.GroupLayout PanelDirectoryLayout = new javax.swing.GroupLayout(PanelDirectory);
        PanelDirectory.setLayout(PanelDirectoryLayout);
        PanelDirectoryLayout.setHorizontalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectoryLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8)
                .addContainerGap(581, Short.MAX_VALUE))
        );
        PanelDirectoryLayout.setVerticalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectoryLayout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(39, 39, 39))
        );

        tblKoleksi.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        tblKoleksi.setForeground(new java.awt.Color(0, 0, 0));
        tblKoleksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Koleksi", "Nama Koleksi", "Nama Pengarang", "Kategori", "No Rak", "Status"
            }
        ));
        jScrollPane2.setViewportView(tblKoleksi);

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

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));
        jLabel21.setText("Cari Berdasarkan :");

        cmbCari.setBackground(new java.awt.Color(255, 255, 255));
        cmbCari.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbCari.setForeground(new java.awt.Color(51, 51, 51));
        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Judul Koleksi", "Nama Pengarang" }));

        lblBulan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBulan.setForeground(new java.awt.Color(51, 51, 51));
        lblBulan.setText("Kata Pencarian");

        txtCari.setBackground(new java.awt.Color(255, 255, 255));
        txtCari.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtCari.setForeground(new java.awt.Color(51, 51, 51));
        txtCari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
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

        javax.swing.GroupLayout mainPanel1Layout = new javax.swing.GroupLayout(mainPanel1);
        mainPanel1.setLayout(mainPanel1Layout);
        mainPanel1Layout.setHorizontalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBulan)
                            .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(mainPanel1Layout.createSequentialGroup()
                            .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanel1Layout.setVerticalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(lblBulan))
                .addGap(9, 9, 9)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
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
                .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        //EXPORT PDF        
        try {
            //Koneksi Database
            com.mysql.jdbc.Connection c = (com.mysql.jdbc.Connection) DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
            //CETAK DATA
            HashMap parameter = new HashMap();
            //AMBIL FILE
            File file = new File("src/Report/Laporan_Data_Koleksi2.jasper");
            JasperReport jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameter, c);
            //AGAR TIDAK MENGCLOSE APLIKASi
            JasperViewer.viewReport(jp, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }

    }//GEN-LAST:event_btnPrintMouseClicked

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        if (cmbCari.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "PILIH AKAN MENCARI BERDASARKAN");
            txtCari.setText("");
            cmbCari.requestFocus();
            showData();
        } else if (!txtCari.getText().equals("")) {
            CariData();
        } else if (txtCari.getText().equals("")) {
            showData();
        }

    }//GEN-LAST:event_txtCariKeyReleased

    private void btnUbahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUbahMouseClicked
        if (tblKoleksi.getSelectedRow() >= 0) {
            UbahData.show();
            Move();
        } else {
            JOptionPane.showMessageDialog(null, "PILIH DATA YANG AKAN DIUBAH");
        }

    }//GEN-LAST:event_btnUbahMouseClicked

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        UbahData();
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        UbahData.dispose();
        showData();
    }//GEN-LAST:event_btnCancelActionPerformed

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
            java.util.logging.Logger.getLogger(V_Beres_Data_Koleksi.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Data_Koleksi.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Data_Koleksi.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Data_Koleksi.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Beres_Data_Koleksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDirectory;
    private javax.swing.JPanel PanelDirectory1;
    private javax.swing.JFrame UbahData;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBulan;
    private javax.swing.JLabel lblNoPol11;
    private javax.swing.JLabel lblNoPol8;
    private javax.swing.JLabel lblNoPol9;
    private javax.swing.JLabel lblService2;
    private javax.swing.JLabel lblService3;
    private javax.swing.JLabel lblService5;
    private javax.swing.JLabel lblService7;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JPanel mainPanel2;
    private javax.swing.JTable tblKoleksi;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtJudul;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNoRak;
    private javax.swing.JTextField txtPengarang;
    private javax.swing.JTextField txtTahunTerbit;
    // End of variables declaration//GEN-END:variables
}
