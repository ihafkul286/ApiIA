package com.example.ApiReservas.controllers;

import com.example.ApiReservas.model.Reserva;
import com.example.ApiReservas.repositories.ReservaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @GetMapping
    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Reserva createReserva(@RequestBody Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReserva(@PathVariable Long id, @RequestBody Reserva detallesReserva) {
        return reservaRepository.findById(id)
                .map(existing -> {
                    final int DURATION_MINUTES = 30;

                    LocalDate newDate = detallesReserva.getFecha();
                    java.time.LocalTime newTime = detallesReserva.getHora();
                    java.time.LocalDateTime newStart = java.time.LocalDateTime.of(newDate, newTime);
                    java.time.LocalDateTime newEnd = newStart.plusMinutes(DURATION_MINUTES);

                    List<Reserva> reservasMismoDia = reservaRepository.findByFecha(newDate);

                    boolean conflict = reservasMismoDia.stream()
                            .filter(r -> !r.getId().equals(id))
                            .map(r -> java.time.LocalDateTime.of(r.getFecha(), r.getHora()))
                            .anyMatch(existingStart -> {
                                java.time.LocalDateTime existingEnd = existingStart.plusMinutes(DURATION_MINUTES);
                                return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
                            });

                    if (conflict) {
                        return ResponseEntity.status(409).body("La franja no está disponible");
                    }

                    existing.setNombre(detallesReserva.getNombre());
                    existing.setTelefono(detallesReserva.getTelefono());
                    existing.setFecha(detallesReserva.getFecha());
                    existing.setHora(detallesReserva.getHora());
                    existing.setServicio(detallesReserva.getServicio());
                    Reserva saved = reservaRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        return reservaRepository.findById(id)
                .map(reserva -> {
                    reservaRepository.delete(reserva);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/all", consumes = "*/*")
    public ResponseEntity<Void> deleteAllReservas() {
        reservaRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fecha/{fecha}")
    public List<Reserva> getByFecha(@PathVariable LocalDate fecha) {
        return reservaRepository.findByFecha(fecha);
    }


}
