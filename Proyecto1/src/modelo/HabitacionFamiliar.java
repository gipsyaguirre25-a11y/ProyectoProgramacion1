/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Diana
 */
public class HabitacionFamiliar extends Habitacion{

 public HabitacionFamiliar(String numero) {
        super(numero);
    }
 
    @Override
    public String getTipo() {
        return "Familiar";
    }
 
    @Override
    public int getCapacidad() {
        return 4;
    }
 
    @Override
    public long getTarifa() {
        return 65000L;
    }

}
