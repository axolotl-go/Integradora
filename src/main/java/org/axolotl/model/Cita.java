package org.axolotl.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private int id;
    private LocalDate fecha;
    private LocalTime hora;
    private int clienteId;
    private String clienteNombre;
    private int mascotaId;
    private String mascotaNombre;
    private String motivo;

    public Cita() {}

    public Cita(int id, LocalDate fecha, LocalTime hora, int clienteId, String clienteNombre,
                int mascotaId, String mascotaNombre, String motivo) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.mascotaId = mascotaId;
        this.mascotaNombre = mascotaNombre;
        this.motivo = motivo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public int getMascotaId() { return mascotaId; }
    public void setMascotaId(int mascotaId) { this.mascotaId = mascotaId; }

    public String getMascotaNombre() { return mascotaNombre; }
    public void setMascotaNombre(String mascotaNombre) { this.mascotaNombre = mascotaNombre; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    @Override
    public String toString() {
        return clienteNombre + " - " + mascotaNombre + " (" + fecha + ")";
    }
}
