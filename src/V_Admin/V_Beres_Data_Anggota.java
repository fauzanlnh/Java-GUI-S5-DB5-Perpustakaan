/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package V_Admin;

import Class.C_Anggota;
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

public class V_Beres_Data_Anggota extends javax.swing.JFrame {

    /**
     * Creates new form V_Beres_Data_Anggota
     */
    Connection koneksi;
    C_Anggota Anggota = new C_Anggota();
    String getKdAnggota, getNamaAnggota, getStatus, getEmail;

    public V_Beres_Data_Anggota() {
        initComponents();
        this.setLocationRelativeTo(null);
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
        showData();
    }

    public void showData() {
        String kolom[] = {"No", "Kode Anggota", "Nama Anggota", "Status", "Email"};
        DefaultTableModel tableModel = new DefaultTableModel(null, kolom);
        String query = null;
        try {
            Statement stmt = koneksi.createStatement();
            query = "SELECT * FROM T_Anggota";
            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                Anggota.setKdAnggota(rs.getString("Kd_Anggota"));
                Anggota.setNmAnggota(rs.getString("Nama_Anggota"));
                Anggota.setStatusAnggota(rs.getString("Status_Anggota"));
                Anggota.setEmail(rs.getString("Email"));
                getKdAnggota = Anggota.getKdAnggota();
                getNamaAnggota = Anggota.getNmAnggota();
                getStatus = Anggota.getStAnggota();
                getEmail = Anggota.getEmail();
                tableModel.addRow(new String[]{no + "", getKdAnggota, getNamaAnggota, getStatus, getEmail});
                no++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "KESALAHAN PADA DATABASE" + ex);
        }
        tblAnggota.setModel(tableModel);
    }

    public void CariData() {
        String kolom[] = {"No", "Kode Anggota", "Nama Anggota", "Status", "Email"};
        DefaultTableModel tableModel = new DefaultTableModel(null, kolom);
        try {
            Anggota.setNmAnggota(txtCari.getText());
            String Cari = Anggota.getNmAnggota();
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM T_Anggota WHERE Nama_Anggota LIKE'%" + Cari + "%' ";
            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                Anggota.setKdAnggota(rs.getString("Kd_Anggota"));
                Anggota.setNmAnggota(rs.getString("Nama_Anggota"));
                Anggota.setStatusAnggota(rs.getString("Status_Anggota"));
                Anggota.setEmail(rs.getString("Email"));
                getKdAnggota = Anggota.getKdAnggota();
                getNamaAnggota = Anggota.getNmAnggota();
                getStatus = Anggota.getStAnggota();
                getEmail = Anggota.getEmail();
                tableModel.addRow(new String[]{no + "", getKdAnggota, getNamaAnggota, getStatus, getEmail});
                no++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "KESALAHAN PADA DATABASE" + ex);
        }
        tblAnggota.setModel(tableModel);
    }

    /*-------------------------------------------------JFRAME POP UP--------------------------------*/
    public void Move() {
        Anggota.setKdAnggota(tblAnggota.getValueAt(tblAnggota.getSelectedRow(), 1).toString());
        Anggota.setNmAnggota(tblAnggota.getValueAt(tblAnggota.getSelectedRow(), 2).toString());
        Anggota.setStatusAnggota(tblAnggota.getValueAt(tblAnggota.getSelectedRow(), 3).toString());
        Anggota.setEmail(tblAnggota.getValueAt(tblAnggota.getSelectedRow(), 4).toString());
        getKdAnggota = Anggota.getKdAnggota();
        getNamaAnggota = Anggota.getNmAnggota();
        getStatus = Anggota.getStAnggota();
        getEmail = Anggota.getEmail();
        txtKode.setText(getKdAnggota);
        txtNama.setText(getNamaAnggota);
        if (getStatus.equals("KHUSUS")) {
            cmbStatus.setSelectedIndex(1);
        } else if (getStatus.equals("BIASA")) {
            cmbStatus.setSelectedIndex(0);
        }
        txtEmail.setText(getEmail);
    }

    public void UbahData() {
        if (txtKode.getText().equals("") && txtNama.getText().equals("") && txtEmail.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "FORM HARUS DIISI");
        } else if (txtKode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "KODE ANGGOTA HARUS DIISI");
        } else if (txtNama.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "NAMA ANGGOTA HARUS DIISI");
        } else if (txtEmail.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EMAIL ANGGOTA HARUS DIISI");
        } else {
            try {
                Anggota.setKdAnggota(txtKode.getText().toUpperCase());
                Anggota.setNmAnggota(txtNama.getText().toUpperCase());
                Anggota.setStatusAnggota(cmbStatus.getSelectedItem().toString().toUpperCase());
                Anggota.setEmail(txtEmail.getText().toUpperCase());
                getKdAnggota = Anggota.getKdAnggota();
                getNamaAnggota = Anggota.getNmAnggota();
                getStatus = Anggota.getStAnggota();
                getEmail = Anggota.getEmail();
                Statement stmt = koneksi.createStatement();
                String Ubah = "UPDATE T_Anggota SET Nama_Anggota = '" + getNamaAnggota + "',Status_Anggota = '" + getStatus + "',Email='" + getEmail + "' "
                        + "WHERE Kd_Anggota = '" + getKdAnggota + "'";
                int BerhasilUbah = stmt.executeUpdate(Ubah);
                if (BerhasilUbah > 0) {
                    JOptionPane.showMessageDialog(null, "DATA KOLEKSI BERHASIL DI UBAH");
                    System.out.println(Ubah);
                    showData();
                    UbahData.dispose();
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
        txtKode = new javax.swing.JTextField();
        lblNoPol1 = new javax.swing.JLabel();
        lblNoPol2 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblNoPol3 = new javax.swing.JLabel();
        lblNoPol4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox<>();
        mainPanel1 = new javax.swing.JPanel();
        PanelDirectory = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAnggota = new javax.swing.JTable();
        btnPrint = new javax.swing.JButton();
        lblBulan = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        btnUbah = new javax.swing.JButton();

        mainPanel2.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel2.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory1.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory1.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Anggota/Ubah Data");

        javax.swing.GroupLayout PanelDirectory1Layout = new javax.swing.GroupLayout(PanelDirectory1);
        PanelDirectory1.setLayout(PanelDirectory1Layout);
        PanelDirectory1Layout.setHorizontalGroup(
            PanelDirectory1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectory1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9)
                .addContainerGap(247, Short.MAX_VALUE))
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

        txtKode.setEditable(false);
        txtKode.setBackground(new java.awt.Color(255, 255, 255));
        txtKode.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtKode.setForeground(new java.awt.Color(51, 51, 51));
        txtKode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblNoPol1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol1.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol1.setText("Kode Anggota");

        lblNoPol2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol2.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol2.setText("Nama Anggota");

        txtNama.setBackground(new java.awt.Color(255, 255, 255));
        txtNama.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtNama.setForeground(new java.awt.Color(51, 51, 51));
        txtNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        lblNoPol3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol3.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol3.setText("Status");

        lblNoPol4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNoPol4.setForeground(new java.awt.Color(51, 51, 51));
        lblNoPol4.setText("E-Mail");

        txtEmail.setBackground(new java.awt.Color(255, 255, 255));
        txtEmail.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(51, 51, 51));
        txtEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        cmbStatus.setBackground(new java.awt.Color(255, 255, 255));
        cmbStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbStatus.setForeground(new java.awt.Color(51, 51, 51));
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BIASA", "KHUSUS" }));

        javax.swing.GroupLayout mainPanel2Layout = new javax.swing.GroupLayout(mainPanel2);
        mainPanel2.setLayout(mainPanel2Layout);
        mainPanel2Layout.setHorizontalGroup(
            mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoPol2)
                            .addComponent(lblNoPol1))
                        .addGap(37, 37, 37)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNama)))
                    .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(mainPanel2Layout.createSequentialGroup()
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(mainPanel2Layout.createSequentialGroup()
                            .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblNoPol4)
                                .addComponent(lblNoPol3))
                            .addGap(84, 84, 84)
                            .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(PanelDirectory1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        );
        mainPanel2Layout.setVerticalGroup(
            mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(PanelDirectory1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoPol1)
                    .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoPol2)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoPol3)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoPol4)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout UbahDataLayout = new javax.swing.GroupLayout(UbahData.getContentPane());
        UbahData.getContentPane().setLayout(UbahDataLayout);
        UbahDataLayout.setHorizontalGroup(
            UbahDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        );
        UbahDataLayout.setVerticalGroup(
            UbahDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UbahDataLayout.createSequentialGroup()
                .addComponent(mainPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel1.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel1.setPreferredSize(new java.awt.Dimension(710, 673));

        PanelDirectory.setBackground(new java.awt.Color(30, 130, 234));
        PanelDirectory.setPreferredSize(new java.awt.Dimension(636, 100));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Data/Anggota");

        javax.swing.GroupLayout PanelDirectoryLayout = new javax.swing.GroupLayout(PanelDirectory);
        PanelDirectory.setLayout(PanelDirectoryLayout);
        PanelDirectoryLayout.setHorizontalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDirectoryLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8)
                .addContainerGap(504, Short.MAX_VALUE))
        );
        PanelDirectoryLayout.setVerticalGroup(
            PanelDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDirectoryLayout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(39, 39, 39))
        );

        tblAnggota.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        tblAnggota.setForeground(new java.awt.Color(0, 0, 0));
        tblAnggota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Anggota", "Nama Anggota", "Status", "Email"
            }
        ));
        jScrollPane2.setViewportView(tblAnggota);

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

        lblBulan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBulan.setForeground(new java.awt.Color(51, 51, 51));
        lblBulan.setText("Nama Anggota");

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
            .addComponent(PanelDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblBulan)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addGap(24, 24, 24)
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        //EXPORT PDF        
        try {
            //Koneksi Database
            com.mysql.jdbc.Connection c = (com.mysql.jdbc.Connection) DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db10118227perpustakaan");
            //CETAK DATA
            HashMap parameter = new HashMap();
            //AMBIL FILE
            File file = new File("src/Report/Laporan_Data_Anggota.jasper");
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
        if (!txtCari.getText().equals("")) {
            CariData();
        } else if (txtCari.getText().equals("")) {
            showData();
        }
    }//GEN-LAST:event_txtCariKeyReleased

    private void btnUbahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUbahMouseClicked
        if (tblAnggota.getSelectedRow() >= 0) {
            UbahData.setLocationRelativeTo(null);
            UbahData.setSize(550, 450);
            UbahData.show();
            Move();
        } else {
            JOptionPane.showMessageDialog(null, "PILIH DATA YANG AKAN DIUBAH");
        }

    }//GEN-LAST:event_btnUbahMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        UbahData.dispose();
        showData();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        UbahData();
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
            java.util.logging.Logger.getLogger(V_Beres_Data_Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Data_Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Data_Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Beres_Data_Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Beres_Data_Anggota().setVisible(true);
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
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBulan;
    private javax.swing.JLabel lblNoPol1;
    private javax.swing.JLabel lblNoPol2;
    private javax.swing.JLabel lblNoPol3;
    private javax.swing.JLabel lblNoPol4;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JPanel mainPanel2;
    private javax.swing.JTable tblAnggota;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
