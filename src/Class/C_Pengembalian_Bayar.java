/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

public class C_Pengembalian_Bayar {

    private String Kd_Kehilangan;
    private String Tanggal_Ganti;
    private int Harga_Ganti;

    public void setKdKehilangan(String KdKehilangan) {
        this.Kd_Kehilangan = KdKehilangan;
    }

    public void setTglGanti(String tgl) {
        this.Tanggal_Ganti = tgl;
    }

    public void setHarga(int Hg_Ganti) {
        this.Harga_Ganti = Hg_Ganti;
    }

    public String getKdKehilangan() {
        return Kd_Kehilangan;
    }

    public String getTglGanti() {
        return Tanggal_Ganti;
    }

    public int getHarga() {
        return Harga_Ganti;
    }
}
