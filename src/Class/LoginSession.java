/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

/**
 *
 * @author Fauzanlh
 */
public class LoginSession {

    private static String Username;
    private static String IdPegawai;

    public static String getUsername() {
        return Username;
    }

    public static void setUsername(String SetUsername) {
        LoginSession.Username = SetUsername;
    }

    public static String getIdPegawai() {
        return IdPegawai;
    }

    public static void setIdPegawai(String SetPassword) {
        LoginSession.IdPegawai = SetPassword;
    }
}
