package com.utc.proyect.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utc.proyect.entity.AccionPlanAmbiental;
import com.utc.proyect.repository.AccionPlanAmbientalRepository;

@Service
public class AccionPlanAmbientalServiceImpl implements AccionPlanAmbientalService {

    private final AccionPlanAmbientalRepository accionPlanAmbientalRepository;

    public AccionPlanAmbientalServiceImpl(AccionPlanAmbientalRepository accionPlanAmbientalRepository) {
        this.accionPlanAmbientalRepository = accionPlanAmbientalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccionPlanAmbiental> listarTodas() {
        return accionPlanAmbientalRepository.findAll(Sort.by(Sort.Direction.ASC, "codigoAccion"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccionPlanAmbiental> buscarPorId(Long id) {
        return accionPlanAmbientalRepository.findById(id);
    }
}
