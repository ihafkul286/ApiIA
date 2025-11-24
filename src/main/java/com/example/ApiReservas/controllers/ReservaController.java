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

    // 1. Obtener todas las reservas
    @GetMapping
    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }

    // 2. Obtener una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Crear una nueva reserva
    @PostMapping
    public Reserva createReserva(@RequestBody Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    // 4. Actualizar una reserva existente
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Long id, @RequestBody Reserva detallesReserva) {
        return reservaRepository.findById(id)
                .map(reserva -> {
                    reserva.setNombre(detallesReserva.getNombre());
                    reserva.setTelefono(detallesReserva.getTelefono());
                    reserva.setFecha(detallesReserva.getFecha());
                    reserva.setHora(detallesReserva.getHora());
                    reserva.setServicio(detallesReserva.getServicio());
                    return ResponseEntity.ok(reservaRepository.save(reserva));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Eliminar una reserva específica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        return reservaRepository.findById(id)
                .map(reserva -> {
                    reservaRepository.delete(reserva);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 6. Eliminar todas las reservas
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
