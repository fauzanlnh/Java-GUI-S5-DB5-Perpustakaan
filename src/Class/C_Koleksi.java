/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;
public class C_Koleksi {

    private String Kd_Koleksi, Judul_Koleksi, Pengarang, Penerbit, Tahun_Terbit, no_rak, Status_Koleksi;
    private String Estimasi_Pengembalian;
    private int Harga;

    public void setKdKoleksi(String Kd) {
        this.Kd_Koleksi = Kd;
    }

    public void setJudulKoleksi(String Judul) {
        this.Judul_Koleksi = Judul;
    }

    public void setPengarang(String Pengarang) {
        this.Pengarang = Pengarang;
    }

    public void setPenerbit(String Penerbit) {
        this.Penerbit = Penerbit;
    }

    public void setTahunTerbit(String Tahun) {
        this.Tahun_Terbit = Tahun;
    }

    public void setNoRak(String NoRak) {
        this.no_rak = NoRak;
    }

    public void setStatus(String Status) {
        this.Status_Koleksi = Status;
    }

    public void setEstimasi(String Est) {
        this.Estimasi_Pengembalian = Est;
    }

    public void setHarga(int Hargg) {
        this.Harga = Hargg;
    }

    public String getKdKoleksi() {
        return Kd_Koleksi;
    }

    public String getJudulKoleksi() {
        return Judul_Koleksi;
    }

    public String getPengarang() {
        return Pengarang;
    }

    public String getPenerbit() {
        return Penerbit;
    }

    public String getTahunTerbit() {
        return Tahun_Terbit;
    }

    public String getNoRak() {
        return no_rak;
    }

    public String getStatus() {
        return Status_Koleksi;
    }

    public String getEstimasi() {
        return Estimasi_Pengembalian;
    }

    public int getHarga() {
        return Harga;
    }
}
