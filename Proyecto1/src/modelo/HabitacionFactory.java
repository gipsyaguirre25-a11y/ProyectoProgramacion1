/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Diana
 */
public class HabitacionFactory {

     private HabitacionFactory() {
    }
 
    public static Habitacion crearHabitacion(
            String numero,
            String tipo) {
 
        switch (tipo) {
 
            case "Individual":
                return new HabitacionIndividual(numero);
 
            case "Doble":
                return new HabitacionDoble(numero);
 
            case "Familiar":
                return new HabitacionFamiliar(numero);
 
            default:
                throw new IllegalArgumentException(
                        "Tipo de habitación inválido");
        }
    }


}
