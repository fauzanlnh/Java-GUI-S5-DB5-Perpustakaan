/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

public class C_Pengembalian_Ganti {

    private String Kd_Kehilangan, Kd_Koleksi_Ganti;
    private String Tanggal_Ganti;

    public void setKdKehilangan(String KdKehilangan) {
        this.Kd_Kehilangan = KdKehilangan;
    }

    public void setTglGanti(String tgl) {
        this.Tanggal_Ganti = tgl;
    }

    public void setKdKoleksi(String Hg_Ganti) {
        this.Kd_Koleksi_Ganti = Hg_Ganti;
    }

    public String getKdKehilangan() {
        return Kd_Kehilangan;
    }

    public String getTglGanti() {
        return Tanggal_Ganti;
    }

    public String getKodeKoleksi() {
        return Kd_Koleksi_Ganti;
    }
}
