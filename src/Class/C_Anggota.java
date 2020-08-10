/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;
public class C_Anggota {

    private String kd_anggota, nama_anggota, status_anggota, email;

    public void setKdAnggota(String kd) {
        this.kd_anggota = kd;
    }

    public void setNmAnggota(String nm) {
        this.nama_anggota = nm;
    }

    public void setStatusAnggota(String st) {
        this.status_anggota = st;
    }

    public void setEmail(String em) {
        this.email = em;
    }

    public String getKdAnggota() {
        return kd_anggota;
    }

    public String getNmAnggota() {
        return nama_anggota;
    }

    public String getStAnggota() {
        return status_anggota;
    }

    public String getEmail() {
        return email;
    }
}
