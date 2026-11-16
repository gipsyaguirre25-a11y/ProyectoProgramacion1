/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import vista.FrmLogin;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import modelo.GestorHotel;
import vista.PnlLogin;
import vista.FrmHotel;

/**
 *
 * @author Diana
 */
public class LoginController {

    private final FrmLogin vista;
    private final GestorHotel gestor = GestorHotel.getInstancia();

    public LoginController(FrmLogin vista) {
        this.vista = vista;
        configurarEventos();
    }

    private void configurarEventos() {

        vista.getPnlLogin().getBtnIngresar()
                .addActionListener(e -> iniciarSesion());

    }

    private void iniciarSesion() {

        PnlLogin panel = vista.getPnlLogin();

        String usuario
                = panel.getTxtUsuario().getText().trim();

        String password = new String(
                panel.getTxtPassword().getPassword());

        boolean acceso
                = gestor.iniciarSesion(usuario, password);

        if (acceso) {

            gestor.actualizarVencidas(LocalDate.now());

            FrmHotel hotel = new FrmHotel();

            HotelController controlador
                    = new HotelController(hotel);

            vista.dispose();

            controlador.mostrar();

        } else {

            JOptionPane.showMessageDialog(
                    vista,
                    "Usuario o contraseña incorrectos.");
            panel.getTxtPassword().setText("");
        }
    }

    public void mostrar() {

        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

}
