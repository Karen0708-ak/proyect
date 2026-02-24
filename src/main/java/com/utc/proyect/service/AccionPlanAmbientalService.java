package com.utc.proyect.service;

import java.util.List;
import java.util.Optional;

import com.utc.proyect.entity.AccionPlanAmbiental;

public interface AccionPlanAmbientalService {

    List<AccionPlanAmbiental> listarTodas();

    Optional<AccionPlanAmbiental> buscarPorId(Long id);
}
