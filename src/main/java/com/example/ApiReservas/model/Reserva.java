package com.example.ApiReservas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "citas")
@Getter @Setter
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String telefono;
    private LocalDate fecha;
    private LocalTime hora;
    private String servicio;

    @Column(name = "created_at", updatable = false, insertable = false)
    private Timestamp createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    private static final Map<String, Integer> SERVICE_DURATIONS = Map.of(
            "Limpieza dental profesional", 30,
            "Blanqueamiento dental", 45,
            "Consulta general", 15,
            "Colocación de brackets metálicos", 60,
            "Brackets estéticos", 60,
            "Alineadores invisibles", 60,
            "Ortodoncia preventiva", 30,
            "Ortodoncia correctiva", 45,
            "Ortodoncia acelerada", 45
    );


    @JsonProperty("start_at")
    public LocalDateTime getStartAtTime() {
        if (fecha == null || hora == null) {
            return null;
        }
        return LocalDateTime.of(fecha, hora);
    }


    @JsonProperty("end_at")
    public LocalDateTime getEndAtTime() {
        LocalDateTime startAt = getStartAtTime();
        if (startAt == null) {
            return null;
        }

        int durationMinutes = SERVICE_DURATIONS.getOrDefault(this.servicio, 30);

        return startAt.plusMinutes(durationMinutes);
    }

    public Reserva(Long id, String nombre,  String telefono, LocalDate fecha, LocalTime hora, String servicio) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fecha = fecha;
        this.hora = hora;
        this.servicio = servicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}

