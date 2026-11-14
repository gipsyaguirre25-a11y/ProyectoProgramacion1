/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.GestorHotel;

/**
 *
 * @author gipsy
 */
public class HotelController {

    private final FrmHotel vista;

    private final GestorHotel gestor = GestorHotel.getInstancia();

    public HotelController(FrmHotel vista) {
        this.vista = vista;
        configurarEventos();

        cargarDatos();

    }

    private void configurarEventos() {
        vista.btnRegistrarHuesped.addActionListener(e -> registrarHuesped());

        vista.btnregistrarHabitacion.addActionListener(e -> registrarHabitacion());

        vista.btnConsultar.addActionListener(e -> consultarDisponible());

        vista.btnbuscar.addActionListener(e -> buscarReserva());

        vista.btnProcesar.addActionListener(e -> procesarOperacion());

        vista.btnSimular.addActionListener(e -> simular());
        vista.btnCambiarUsuario.addActionListener(e -> cambiarUsuario());
        vista.btnEjecutar.addActionListener(e -> ejecutarProcesos());

    }
    private void cargarDatos(){
        vista.lblRecepcionista.setText("Recepcionista :" + gestor.getRecepcionistaActivo());
        
        actualizarIndicadores();
    }
}
