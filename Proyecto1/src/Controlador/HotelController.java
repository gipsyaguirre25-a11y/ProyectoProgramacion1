/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.GestorHotel;
import vista.FrmHotel;
import vista.PnlCheck;
import vista.PnlDisponibilidad;
import vista.PnlHabitaciones;
import vista.PnlHuespedes;
import vista.PnlInicio;
import vista.PnlReservas;
import vista.PnlSimulacion;
import modelo.IndicadoresHotel;
import vista.PnlIndicadores;
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
       public void mostrar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

private void configurarEventos() {


    vista.getPnlHuespedes().getBtnRegistrar()
            .addActionListener(e -> registrarHuesped());

  
    vista.getPnlHabitaciones().getBtnRegistrar()
            .addActionListener(e -> registrarHabitacion());

    
    vista.getPnlReservas().getBtnRegistrar()
            .addActionListener(e -> registrarReserva());

   
    vista.getPnlDisponibilidad().getBtnConsultar()
            .addActionListener(e -> consultarDisponible());

 
    vista.getPnlCheck().getBtnBuscar()
            .addActionListener(e -> buscarReserva());

    
    vista.getPnlCheck().getBtnProcesar()
            .addActionListener(e -> procesarOperacion());

 
    vista.getPnlSimulacion().getBtnSimular()
            .addActionListener(e -> simular());

  
    vista.getBtnCambiarUsuario()
            .addActionListener(e -> cambiarUsuario());

   
    vista.getPnlInicio().getBtnEjecutarProcesos()
            .addActionListener(e -> ejecutarProcesos());
}




    private void registrarHuesped() {

    }

    private void registrarHabitacion() {

    }

    private void registrarReserva() {

    }

    private void consultarDisponible() {

    }

    private void buscarReserva() {

    }

    private void procesarOperacion() {

    }

    private void simular() {

    }

    private void cambiarUsuario() {

    }

    private void ejecutarProcesos() {

    }

    private void actualizarIndicadores() {


    IndicadoresHotel datos = gestor.calcularIndicadores();

    PnlIndicadores panel = vista.getPnlIndicadores();

    panel.getLblTotal().setText(
            String.valueOf(datos.totalHabitaciones));

    panel.getLblOcupadas().setText(
            String.valueOf(datos.habitacionesOcupadas));

    panel.getLblNoOcupadas().setText(
            String.valueOf(datos.habitacionesLibres));

    panel.getLblConfirmadas().setText(
            String.valueOf(datos.reservasConfirmadas));

    panel.getLblVencidas().setText(
            String.valueOf(datos.reservasVencidas));

    panel.getLblIngresos().setText(
            "₡" + datos.ingresosEsperados);

    }

  private void cargarDatos() {

    vista.getLblRecepcionista().setText(
            "Recepcionista: "
            + gestor.getRecepcionistaActivo());

    actualizarIndicadores();
}
}
