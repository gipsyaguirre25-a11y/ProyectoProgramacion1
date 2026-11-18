package Controlador;

import Hilos.ActualizadorIndicadores;
import Hilos.SupervisorReservas;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.GestorHotel;
import modelo.Habitacion;
import modelo.HabitacionFactory;
import modelo.Huesped;
import modelo.IndicadoresHotel;
import modelo.Reserva;
import vista.FrmHotel;
import vista.FrmLogin;
import vista.PnlCheck;
import vista.PnlDisponibilidad;
import vista.PnlHabitaciones;
import vista.PnlHuespedes;
import vista.PnlIndicadores;
import vista.PnlReservas;
import vista.PnlSimulacion;


public class HotelController {

    private final FrmHotel vista;
    private final GestorHotel gestor = GestorHotel.getInstancia();
    private boolean procesosEjecutandose = false;

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
                .addActionListener(e -> ejecutarSeguro(this::registrarHuesped));
        vista.getPnlHabitaciones().getBtnRegistrar()
                .addActionListener(e -> ejecutarSeguro(this::registrarHabitacion));
        vista.getPnlHabitaciones().getCboTipo()
                .addActionListener(e -> ejecutarSeguro(this::mostrarDatosHabitacion));
        vista.getPnlReservas().getBtnRegistrar()
                .addActionListener(e -> ejecutarSeguro(this::registrarReserva));
        vista.getPnlDisponibilidad().getBtnConsultar()
                .addActionListener(e -> ejecutarSeguro(this::consultarDisponible));
        vista.getPnlCheck().getBtnBuscar()
                .addActionListener(e -> ejecutarSeguro(this::buscarReserva));
        vista.getPnlCheck().getBtnProcesar()
                .addActionListener(e -> ejecutarSeguro(this::procesarOperacion));
        vista.getPnlSimulacion().getBtnSimular()
                .addActionListener(e -> ejecutarSeguro(this::simular));
        vista.getBtnCambiarUsuario()
                .addActionListener(e -> cambiarUsuario());
        vista.getPnlInicio().getBtnEjecutarProcesos()
                .addActionListener(e -> ejecutarSeguro(this::ejecutarProcesos));
    }

  
    private void ejecutarSeguro(Runnable accion) {
        try {
            accion.run();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista,
                    "La cantidad de personas debe ser un numero entero valido.");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(vista,
                    "Fecha invalida. Utilice el formato AAAA-MM-DD (ejemplo: 2026-11-20).");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage());
        }
    }

    private void cargarDatos() {
        vista.getLblRecepcionista().setText(
                "Recepcionista: " + gestor.getRecepcionistaActivo());
        cargarHuespedes();
        cargarHabitaciones();
        cargarReservas();
        mostrarDatosHabitacion();
        actualizarIndicadores();
    }

   
    private void registrarHuesped() {
        PnlHuespedes p = vista.getPnlHuespedes();
        gestor.registrarHuesped(
                p.getTxtIdentificacion().getText(),
                p.getTxtNombre().getText(),
                p.getTxtTelefono().getText(),
                p.getTxtCorreo().getText());
        cargarHuespedes();
        p.getTxtIdentificacion().setText("");
        p.getTxtNombre().setText("");
        p.getTxtTelefono().setText("");
        p.getTxtCorreo().setText("");
        JOptionPane.showMessageDialog(vista, "Huesped registrado correctamente.");
    }

    private void cargarHuespedes() {
        DefaultTableModel tabla = (DefaultTableModel)
                vista.getPnlHuespedes().getTblHuespedes().getModel();
        tabla.setRowCount(0);
        vista.getPnlReservas().getCboHuesped().removeAllItems();
        for (Huesped h : gestor.getHuespedes()) {
            tabla.addRow(new Object[]{
                h.getIdentificacion(), h.getNombre(), h.getTelefono(), h.getCorreo()
            });
            vista.getPnlReservas().getCboHuesped().addItem(h.getIdentificacion());
        }
    }

   
    private void registrarHabitacion() {
        PnlHabitaciones p = vista.getPnlHabitaciones();
        gestor.registrarHabitacion(
                p.getTxtNumero().getText(),
                (String) p.getCboTipo().getSelectedItem());
        cargarHabitaciones();
        actualizarIndicadores();
        p.getTxtNumero().setText("");
        JOptionPane.showMessageDialog(vista, "Habitacion registrada correctamente.");
    }

    private void cargarHabitaciones() {
        DefaultTableModel tabla = (DefaultTableModel)
                vista.getPnlHabitaciones().getTblHabitaciones().getModel();
        tabla.setRowCount(0);
        vista.getPnlReservas().getCboHabitacion().removeAllItems();
        for (Habitacion h : gestor.getHabitaciones()) {
            tabla.addRow(new Object[]{
                h.getNumero(), h.getTipo(), h.getCapacidad(), h.getTarifa()
            });
            vista.getPnlReservas().getCboHabitacion().addItem(h.getNumero());
        }
    }

    private void mostrarDatosHabitacion() {
        PnlHabitaciones p = vista.getPnlHabitaciones();
        String tipo = (String) p.getCboTipo().getSelectedItem();
        if (tipo == null) {
            p.getTxtCapacidad().setText("");
            p.getTxtTarifa().setText("");
            return;
        }
        Habitacion ejemplo = HabitacionFactory.crearHabitacion("VISTA", tipo);
        p.getTxtCapacidad().setText(String.valueOf(ejemplo.getCapacidad()));
        p.getTxtTarifa().setText(String.valueOf(ejemplo.getTarifa()));
    }

    private void registrarReserva() {
        PnlReservas p = vista.getPnlReservas();
        String identificacion = (String) p.getCboHuesped().getSelectedItem();
        String numero = (String) p.getCboHabitacion().getSelectedItem();
        if (identificacion == null || numero == null) {
            throw new IllegalArgumentException(
                    "Primero registre un huesped y una habitacion.");
        }
        int personas = Integer.parseInt(p.getTxtPersonas().getText().trim());
        LocalDate entrada = LocalDate.parse(p.getTxtEntrada().getText().trim());
        LocalDate salida = LocalDate.parse(p.getTxtSalida().getText().trim());
        Reserva reserva = gestor.registrarReserva(
                identificacion, numero, personas, entrada, salida);
        p.getTxtNoches().setText(String.valueOf(reserva.getNoches()));
        p.getTxtImporte().setText("₡" + reserva.getImporte());
        cargarReservas();
        actualizarIndicadores();
        JOptionPane.showMessageDialog(vista,
                "Reserva registrada con codigo: " + reserva.getCodigo());
    }

    private void cargarReservas() {
        DefaultTableModel tabla = (DefaultTableModel)
                vista.getPnlReservas().getTblReservas().getModel();
        tabla.setRowCount(0);
        for (Reserva r : gestor.getReservas()) {
            tabla.addRow(new Object[]{
                r.getCodigo(), r.getHabitacion().getNumero(),
                r.getFechaEntrada(), r.getFechaSalida(),
                r.getEstado(), r.getImporte()
            });
        }
    }

  
    private void consultarDisponible() {
        PnlDisponibilidad p = vista.getPnlDisponibilidad();
        LocalDate fecha = LocalDate.parse(p.getTxtFecha().getText().trim());
        int cantidad = gestor.contarDisponibles(fecha);
        List<Reserva> reservas = gestor.getReservas();
        StringBuilder detalle = new StringBuilder();
        for (Habitacion h : gestor.getHabitaciones()) {
            int libre = GestorHotel.contarDisponiblesRecursivo(
                    Collections.singletonList(h), reservas, fecha, 0);
            if (libre == 1) {
                detalle.append(h.getNumero())
                        .append(" - ").append(h.getTipo()).append("\n");
            }
        }
        p.getLblCantidad().setText(String.valueOf(cantidad));
        p.getTxtResultado().setText(detalle.length() == 0
                ? "No hay habitaciones disponibles para esa fecha."
                : detalle.toString());
    }


    private void buscarReserva() {
        PnlCheck p = vista.getPnlCheck();
        Reserva r = gestor.buscarReserva(p.getTxtCodigo().getText().trim());
        if (r == null) {
            p.getTxtDetalle().setText("");
            throw new IllegalArgumentException("Reserva no encontrada.");
        }
        p.getTxtDetalle().setText(
                "Codigo: " + r.getCodigo()
                + "\nHuesped: " + r.getHuesped().getNombre()
                + "\nHabitacion: " + r.getHabitacion().getNumero()
                + "\nEntrada: " + r.getFechaEntrada()
                + "\nSalida: " + r.getFechaSalida()
                + "\nEstado: " + r.getEstado());
    }

    private void procesarOperacion() {
        PnlCheck p = vista.getPnlCheck();
        String codigo = p.getTxtCodigo().getText().trim();
        if (p.getRdbCheckIn().isSelected()) {
            gestor.checkIn(codigo);
        } else if (p.getRdbCheckOut().isSelected()) {
            gestor.checkOut(codigo);
        } else {
            throw new IllegalArgumentException("Seleccione Check-in o Check-out.");
        }
        buscarReserva();
        cargarReservas();
        actualizarIndicadores();
        JOptionPane.showMessageDialog(vista, "Operacion realizada correctamente.");
    }

  
    private void simular() {
        PnlSimulacion p = vista.getPnlSimulacion();
        LocalDate fecha = LocalDate.parse(p.getTxtFecha().getText().trim());
        List<Reserva> vencidas = gestor.simularVencidas(fecha);
        StringBuilder resultado = new StringBuilder();
        for (Reserva r : vencidas) {
            resultado.append(r.getCodigo()).append(" - Entrada: ")
                    .append(r.getFechaEntrada()).append("\n");
        }
        p.getTxtResultado().setText(vencidas.isEmpty()
                ? "Ninguna reserva venceria en esa fecha."
                : resultado.toString());
    }

   
    private void actualizarIndicadores() {
        actualizarIndicadores(gestor.calcularIndicadores());
    }

    private void actualizarIndicadores(IndicadoresHotel datos) {
        PnlIndicadores p = vista.getPnlIndicadores();
        p.getLblTotal().setText(String.valueOf(datos.totalHabitaciones));
        p.getLblOcupadas().setText(String.valueOf(datos.habitacionesOcupadas));
        p.getLblNoOcupadas().setText(String.valueOf(datos.habitacionesLibres));
        p.getLblConfirmadas().setText(String.valueOf(datos.reservasConfirmadas));
        p.getLblVencidas().setText(String.valueOf(datos.reservasVencidas));
        p.getLblIngresos().setText("₡" + datos.ingresosEsperados);
    }

    
    private void cambiarUsuario() {
        if (procesosEjecutandose) {
            JOptionPane.showMessageDialog(vista,
                    "Espere a que finalice la supervision antes de cambiar usuario.");
            return;
        }
        gestor.cerrarSesion();
        vista.dispose();
        FrmLogin login = new FrmLogin();
        new LoginController(login).mostrar();
    }

 
    private void ejecutarProcesos() {
        if (!gestor.iniciarProcesos()) {
            JOptionPane.showMessageDialog(vista,
                    "Los procesos ya se estan ejecutando.");
            return;
        }
        procesosEjecutandose = true;
        vista.getPnlInicio().getBtnEjecutarProcesos().setEnabled(false);
        AtomicInteger pendientes = new AtomicInteger(2);
        Runnable alFinalizar = () -> {
            if (pendientes.decrementAndGet() == 0) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        cargarReservas();
                        actualizarIndicadores();
                    } finally {
                        gestor.finalizarProcesos();
                        procesosEjecutandose = false;
                        vista.getPnlInicio().getBtnEjecutarProcesos()
                                .setEnabled(true);
                    }
                });
            }
        };
        Thread supervisor = new Thread(
                new SupervisorReservas(gestor, alFinalizar),
                "SupervisorReservas");
        Thread indicadores = new Thread(
                new ActualizadorIndicadores(gestor,
                        datos -> SwingUtilities.invokeLater(
                                () -> actualizarIndicadores(datos)),
                        alFinalizar),
                "ActualizadorIndicadores");
        supervisor.start();
        indicadores.start();
    }
}
