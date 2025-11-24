package com.example.ApiReservas.Service;

import com.example.ApiReservas.model.Reserva;
import com.example.ApiReservas.repositories.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> getById(Long id) {
        return reservaRepository.findById(id);
    }

    public List<Reserva> getByFecha(LocalDate fecha) {
        return reservaRepository.findByFecha(fecha);
    }

    public Reserva create(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public void delete(Long id) {
        reservaRepository.deleteById(id);
    }
}