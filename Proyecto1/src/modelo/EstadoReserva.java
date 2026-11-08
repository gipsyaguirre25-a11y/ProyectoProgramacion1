/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package modelo;

/**
 *
 * @author gipsy
 */
public enum EstadoReserva {
    CONFIRMADA("Confirmada"),
    HOSPEDADO("Hospedado"),
    FINALIZADA("Finalizada"),
    VENCIDA("Vencida");
    
    private final String descripcion;
    
    EstadoReserva(String descripcion) {
        this.descripcion = descripcion;
        
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
}
