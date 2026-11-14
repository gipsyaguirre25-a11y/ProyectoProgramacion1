/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Diana
 */
public class IndicadoresHotel {

    public final int totalHabitaciones;
    public final int habitacionesOcupadas;
    public final int habitacionesLibres;
    public final int reservasConfirmadas;
    public final int reservasVencidas;
    public final long ingresosEsperados;

    public IndicadoresHotel(
            int totalHabitaciones,
            int habitacionesOcupadas,
            int reservasConfirmadas,
            int reservasVencidas,
            long ingresosEsperados) {

        this.totalHabitaciones = totalHabitaciones;

        this.habitacionesOcupadas
                = habitacionesOcupadas;

        this.habitacionesLibres
                = totalHabitaciones
                - habitacionesOcupadas;

        this.reservasConfirmadas
                = reservasConfirmadas;

        this.reservasVencidas
                = reservasVencidas;

        this.ingresosEsperados
                = ingresosEsperados;
    }

}
