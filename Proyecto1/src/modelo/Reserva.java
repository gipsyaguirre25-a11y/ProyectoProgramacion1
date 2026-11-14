/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;

/**
 *
 * @author Diana
 */
public class Reserva {

    private final String codigo;
    private final Huesped huesped;
    private final Habitacion habitacion;
    private final int cantidadPersonas;
    private final LocalDate fechaEntrada;
    private final LocalDate fechaSalida;
    private LocalDate fechaSalidaReal;
    private final long noches;
    private final long importe;
    private EstadoReserva estado;
    private final String recepcionista;

    public Reserva(String codigo, Huesped huesped, Habitacion habitacion, int cantidadPersonas, LocalDate fechaEntrada, LocalDate fechaSalida, LocalDate fechaSalidaReal, long noches, long importe, EstadoReserva estado, String recepcionista) {
        this.codigo = codigo;
        this.huesped = huesped;
        this.habitacion = habitacion;
        this.cantidadPersonas = cantidadPersonas;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.fechaSalidaReal = fechaSalidaReal;
        this.noches = noches;
        this.importe = importe;
        this.estado = estado;
        this.recepcionista = recepcionista;
    }

    public String getCodigo() {
        return codigo;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public LocalDate getFechaSalidaReal() {
        return fechaSalidaReal;
    }

    public long getNoches() {
        return noches;
    }

    public long getImporte() {
        return importe;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public String getRecepcionista() {
        return recepcionista;
    }

    public void hacerCheckIn() {
        estado = EstadoReserva.HOSPEDADO;
    }

    public void hacerCheckOut(LocalDate fecha) {
        fechaSalidaReal = fecha;
        estado = EstadoReserva.FINALIZADA;
    }

    public void marcarVencida() {
        estado = EstadoReserva.VENCIDA;
    }
}
