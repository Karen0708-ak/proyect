package com.utc.proyect.service;

import java.util.List;
import java.util.Optional;

import com.utc.proyect.entity.ActividadAccion;

public interface ActividadAccionService {

    List<ActividadAccion> listarTodas();

    Optional<ActividadAccion> buscarPorId(Long id);

    ActividadAccion guardar(ActividadAccion actividadAccion);

    ActividadAccion actualizar(Long id, ActividadAccion actividadAccion);

    void eliminar(Long id);
}
