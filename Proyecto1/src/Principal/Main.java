/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Principal;

import vista.FrmLogin;
import Controlador.LoginController;
import javax.swing.SwingUtilities;

/**
 *
 * @author Diana
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        SwingUtilities.invokeLater(() -> {  FrmLogin login = new FrmLogin();

            LoginController controlador = new LoginController(login);

            controlador.mostrar();
        });
    }
}
