/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        recepcionista.add(new Recepcionista("recepcion1", "Hotel123"));

        recepcionista.add(new Recepcionista("recepcion2", "Hotel1456"));
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
    
   private Habitacion buscarHabitacion ( 
           String numero){
       for (Habitacion h: habitaciones) {
           if(h.getNumero().equals(numero)) {
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
        long noches = ChronoUnit.DAYS.between(entrada, salida);
        long importe = noches * habitacion.getTarifa();
        String codigo = String.format("R%04d", ConsecutivoReserva++);

        Reserva reserva = new Reserva(codigo, huesped, habitacion, personas, entrada, salida, noches, importe, RecepcionistaActivo.getUsuario());
        reservas.add(reserva);
        return reserva;
        
        }

    public synchronized void checkIn(
            String codigo) {

        Reserva r
                = buscarReserva(codigo);

        if (r == null) {

            throw new IllegalArgumentException(
                    "Reserva inexistente.");
        }

        if (r.getEstado()
                != EstadoReserva.CONFIRMADA) {

            throw new IllegalArgumentException(
                    "La reserva no está confirmada.");
        }

        if (!r.getFechaEntrada()
                .equals(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "No corresponde la fecha de ingreso.");
        }

        r.hacerCheckIn();
    }
   
    public synchronized void checkOut(
            String codigo) {

        Reserva r
                = buscarReserva(codigo);

        if (r == null) {

            throw new IllegalArgumentException(
                    "Reserva inexistente.");
        }

        if (r.getEstado()
                != EstadoReserva.HOSPEDADO) {

            throw new IllegalArgumentException(
                    "La reserva no está hospedada.");
        }

        r.hacerCheckOut(LocalDate.now());
    }

    
    public synchronized int actualizarVencidas(
            LocalDate fecha) {

        int actualizadas = 0;

        for (Reserva r : reservas) {

            if (r.getEstado()
                    == EstadoReserva.CONFIRMADA
                    && r.getFechaEntrada()
                            .isBefore(fecha)) {

                r.marcarVencida();
                actualizadas++;
            }
        }

        return actualizadas;
    }

    public synchronized List<Reserva>
            simularVencidas(
                    LocalDate fechaFutura) {

        if (fechaFutura == null
                || !fechaFutura.isAfter(
                        LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Debe ingresar una fecha futura.");
        }

        List<Reserva> resultado
                = new ArrayList<>();

        for (Reserva r : reservas) {

            if (r.getEstado()
                    == EstadoReserva.CONFIRMADA
                    && r.getFechaEntrada()
                            .isBefore(fechaFutura)) {

                resultado.add(r);
            }
        }

        return resultado;
    }

   
    public static long ingresosRecursivos(
            List<Reserva> reservas,
            int indice) {

        if (indice >= reservas.size()) {
            return 0;
        }

        Reserva r
                = reservas.get(indice);

        long importe = 0;

        if (r.getEstado()
                == EstadoReserva.CONFIRMADA) {

            importe
                    = r.getImporte();
        }

        return importe
                + ingresosRecursivos(
                        reservas,
                        indice + 1);
    }

    
    public synchronized int contarDisponibles(
            LocalDate fecha) {

        return contarDisponiblesRecursivo(
                habitaciones,
                reservas,
                fecha,
                0);
    }

    private static boolean estaLibre(
            Habitacion habitacion,
            List<Reserva> reservas,
            LocalDate fecha) {

        for (Reserva r : reservas) {

            if (r.getHabitacion().getNumero()
                    .equals(
                            habitacion.getNumero())
                    && (r.getEstado()
                    == EstadoReserva.CONFIRMADA
                    || r.getEstado()
                    == EstadoReserva.HOSPEDADO)
                    && !fecha.isBefore(
                            r.getFechaEntrada())
                    && fecha.isBefore(
                            r.getFechaSalida())) {

                return false;
            }
        }

        return true;
    }

    public static int contarDisponiblesRecursivo(
            List<Habitacion> habitaciones,
            List<Reserva> reservas,
            LocalDate fecha,
            int indice) {

        if (indice >= habitaciones.size()) {
            return 0;
        }

        int disponible
                = estaLibre(
                        habitaciones.get(indice),
                        reservas,
                        fecha)
                        ? 1 : 0;

        return disponible
                + contarDisponiblesRecursivo(
                        habitaciones,
                        reservas,
                        fecha,
                        indice + 1);
    }

   
    
   
    public synchronized IndicadoresHotel
            calcularIndicadores() {

        Set<String> ocupadas
                = new HashSet<>();

        int confirmadas = 0;
        int vencidas = 0;

        for (Reserva r : reservas) {

            if (r.getEstado()
                    == EstadoReserva.HOSPEDADO) {

                ocupadas.add(
                        r.getHabitacion()
                                .getNumero());
            }

            if (r.getEstado()
                    == EstadoReserva.CONFIRMADA) {

                confirmadas++;
            }

            if (r.getEstado()
                    == EstadoReserva.VENCIDA) {

                vencidas++;
            }
        }

        long ingresos
                = ingresosRecursivos(
                        reservas,
                        0);

        return new IndicadoresHotel(
                habitaciones.size(),
                ocupadas.size(),
                confirmadas,
                vencidas,
                ingresos);
    }

  
    public synchronized boolean iniciarProcesos() {

        if (ProcesosActivos) {
            return false;
        }

        ProcesosActivos = true;
        return true;
    }

    public synchronized void finalizarProcesos() {
        ProcesosActivos = false;
    }

  
    public synchronized List<Huesped>
            getHuespedes() {
        return new ArrayList<>(huespedes);
    }

    public synchronized List<Habitacion>
            getHabitaciones() {
        return new ArrayList<>(habitaciones);
    }

    public synchronized List<Reserva>
            getReservas() {
        return new ArrayList<>(reservas);

    }
}