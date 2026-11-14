/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gipsy
 */
public final class GestorHotel {

    private static final GestorHotel instancia
            = new GestorHotel();

    public static GestorHotel getInstancia() {
        return instancia;
    }
    private final List<Huesped> huespedes;
    private final List<Habitacion> habitaciones;
    private final List<Reserva> reservas;
    private final List<Recepcionista> recepcionista;

    private Recepcionista RecepcionistaActivo;
    private int ConsecutivoReserva;
    private boolean ProcesosActivos;

    private GestorHotel() {
        huespedes = new ArrayList<>();
        habitaciones = new ArrayList<>();
        reservas = new ArrayList<>();
        recepcionista = new ArrayList<>();

        ConsecutivoReserva = 1;

        ProcesosActivos = false;

        recepcionista.add(new Recepcionista("Recepcion1", "Hotel123"));

        recepcionista.add(new Recepcionista("Recepcion2", "Hotel1456"));
    }

    public synchronized boolean iniciarSesion(
            String usuario,
            String password
    ) {
        if (RecepcionistaActivo != null) {
            return false;
        }
        for (Recepcionista r : recepcionista) {
            if (r.getUsuario().equals(usuario)
                    && r.validarPassword(password)) {
                RecepcionistaActivo = r;
                return true;
            }
        }

        return false;
    }

    public synchronized void cerrarSesion() {
        RecepcionistaActivo = null;
    }

    public synchronized String getRecepcionistaActivo() {
        if (RecepcionistaActivo == null) {
            return "";

        }
        return RecepcionistaActivo.getUsuario();
    }

    private void validarTexto(
            String texto,
            String campo
    ) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El campo" + campo + "es obligatorio");
        }

    }

    public synchronized void registrarHuesped(
            String identificacion,
            String nombre,
            String telefono,
            String correo
    ) {
        validarTexto(identificacion, "identificacion");
        validarTexto(nombre, "nombre");
        validarTexto(telefono, "telefono");
        validarTexto(correo, "correo");

        identificacion = identificacion.trim();

        for (Huesped h : huespedes) {
            if (h.getIdentificacion().equalsIgnoreCase(identificacion)) {
                throw new IllegalArgumentException("La identificacion del huesped ya existe");
            }
        }
        correo = correo.trim();
        if (!correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Correo invalido");
        }
        huespedes.add(new Huesped(identificacion, nombre.trim(), telefono.trim(), correo.trim()));
    }

    public synchronized void registrarHabitacion(
            String numero,
            String tipo) {
        validarTexto(numero, "Numero");
        for (Habitacion h : habitaciones) {
            if (h.getNumero().equalsIgnoreCase(numero.trim())) {
              throw new IllegalArgumentException("La habitacion ya existe");
            }
        }
            habitaciones.add(HabitacionFactory.crearHabitacion(numero.trim(), tipo));
        }
    

    private Huesped buscarHuesped(String identificacion) {
        for (Huesped h : huespedes) {
            if (h.getIdentificacion().equalsIgnoreCase(identificacion)) {
                return h;
            }
        }
        return null;
    }

    public synchronized Reserva buscarReserva(
            String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Reserva r : reservas) {
            if (r.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return r;
            }

        }
        return null;
    }

    private boolean DisponiblePeriodo(
            Habitacion habitacion,
            LocalDate entrada,
            LocalDate salida) {
        for (Reserva r : reservas) {
            boolean bloquea = r.getEstado() == EstadoReserva.CONFIRMADA || r.getEstado() == EstadoReserva.HOSPEDADO;
            if (bloquea && r.getHabitacion().getNumero().equals(habitacion.getNumero())) {
                boolean superpuesta = entrada.isBefore(r.getFechaSalida()) && salida.isAfter(r.getFechaEntrada());
                if (superpuesta) {
                    return false;
                }
            }
        }
        return true;

    }

    public synchronized Reserva registrarReserva(String identificacionHuesped, String numeroHabitacion, int personas, LocalDate entrada, LocalDate salida) {
        if (RecepcionistaActivo == null) {
            throw new IllegalArgumentException("Debe iniciar sesion");

        }
        Huesped huesped = buscarHuesped(identificacionHuesped);
        Habitacion habitacion = buscarHabitacion(numeroHabitacion);
        if (huesped == null) {
            throw new IllegalArgumentException("Huesped no encontrada");

        }
        if (habitacion == null) {
            throw new IllegalArgumentException("Habitacion no encontrada");

        }
        if (entrada == null || salida == null) {
            throw new IllegalArgumentException("Debe ingresar fechas");
        }
        if (entrada.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de entrada es invalida");
        }
        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException("la fecha de salida debe ser posterior");

        }
        if (personas <= 0) {
            throw new IllegalArgumentException("Cantidad de personas invalidas");

        }
        if (personas > habitacion.getCapacidad()) {
            throw new IllegalArgumentException("capacidad excedida");
        }
        if (!DisponiblePeriodo(
                habitacion,
                entrada, salida)) {
            throw new IllegalArgumentException("La habitacion no esta disponible");
        }
        long noches = ChronoUnit.DAYS.between(entrada,salida);
        long importe = noches *habitacion.getTarifa();
        String codigo = String.format("R%04d", ConsecutivoReserva++);
        
        Reserva reserva = new Reserva(codigo, huesped,habitacion,personas,entrada,salida,noches,importe,RecepcionistaActivo.getUsuario());
    reservas.add(reserva);
    return reserva;
    }
   
/////Siguiente abajo  ////