/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;
public class C_Peminjaman {

    private String Kd_Peminjaman, StatusPeminjaman;
    private String Tgl_Pinjam, Tgl_Kembali, Estimasi_Pengembalian;
    private int Denda;

    public void setKdPeminjaman(String Kd) {
        this.Kd_Peminjaman = Kd;
    }

    public void setStatus(String Status) {
        this.StatusPeminjaman = Status;
    }

    public void setTglPinjam(String tPinjam) {
        this.Tgl_Pinjam = tPinjam;
    }

    public void setTglKembali(String tKembali) {
        this.Tgl_Kembali = tKembali;
    }

    public void setEsimasiKembali(String Estimasi) {
        this.Estimasi_Pengembalian = Estimasi;
    }

    public void setDenda(int Dd) {
        this.Denda = Dd;
    }

    public String getKdPeminjaman() {
        return Kd_Peminjaman;
    }

    public String getStatus() {
        return StatusPeminjaman;
    }

    public String getTglPinjam() {
        return Tgl_Pinjam;
    }

    public String getTglKembali() {
        return Tgl_Kembali;
    }

    public String getEsimasiKembali() {
        return Estimasi_Pengembalian;
    }

    public int getDenda() {
        return Denda;
    }

}
