/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import modelo.GestorHotel;

/**
 *
 * @author Diana
 */
public class LoginController {

    private final FrmLogin vista;
    private final GestorHotel gestor = GestorHotel.getInstancia();

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
        }
    }

    public void mostrar() {

        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

}
