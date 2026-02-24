package com.utc.proyect.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utc.proyect.entity.ActividadAccion;
import com.utc.proyect.repository.ActividadAccionRepository;

@Service
public class ActividadAccionServiceImpl implements ActividadAccionService {

    private final ActividadAccionRepository actividadAccionRepository;

    public ActividadAccionServiceImpl(ActividadAccionRepository actividadAccionRepository) {
        this.actividadAccionRepository = actividadAccionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActividadAccion> listarTodas() {
        return actividadAccionRepository.findAll(Sort.by(Sort.Direction.DESC, "codigoActividad"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActividadAccion> buscarPorId(Long id) {
        return actividadAccionRepository.findById(id);
    }

    @Override
    @Transactional
    public ActividadAccion guardar(ActividadAccion actividadAccion) {
        return actividadAccionRepository.save(actividadAccion);
    }

    @Override
    @Transactional
    public ActividadAccion actualizar(Long id, ActividadAccion actividadAccion) {
        ActividadAccion actual = actividadAccionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("La actividad no existe."));

        actual.setNumeroActividad(actividadAccion.getNumeroActividad());
        actual.setFechaInicialActividad(actividadAccion.getFechaInicialActividad());
        actual.setFechaMaxActividad(actividadAccion.getFechaMaxActividad());
        actual.setEstadoActividad(actividadAccion.getEstadoActividad());
        actual.setAccionPlanAmbiental(actividadAccion.getAccionPlanAmbiental());

        return actividadAccionRepository.save(actual);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        actividadAccionRepository.deleteById(id);
    }
}
