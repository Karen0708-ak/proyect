package com.utc.proyect.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "actividad_accion")
public class ActividadAccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_actividad")
    private Long codigoActividad;

    @NotNull(message = "El numero de actividad es obligatorio.")
    @Min(value = 1, message = "El numero de actividad debe ser mayor o igual a 1.")
    @Column(name = "numero_actividad")
    private Integer numeroActividad;

    @NotNull(message = "La fecha inicial es obligatoria.")
    @Column(name = "fecha_inicial_actividad")
    private LocalDate fechaInicialActividad;

    @NotNull(message = "La fecha maxima es obligatoria.")
    @Column(name = "fecha_max_actividad")
    private LocalDate fechaMaxActividad;

    @NotBlank(message = "El estado es obligatorio.")
    @Pattern(
            regexp = "PENDIENTE|EN_PROGRESO|COMPLETADO|CANCELADO",
            message = "El estado debe ser PENDIENTE, EN_PROGRESO, COMPLETADO o CANCELADO.")
    @Column(name = "estado_actividad", length = 255)
    private String estadoActividad;

    @NotNull(message = "Debe seleccionar una accion del plan ambiental.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_cod_accion", nullable = false)
    private AccionPlanAmbiental accionPlanAmbiental;

    @Column(name = "fecha_creado_actividad", updatable = false)
    private LocalDateTime fechaCreadoActividad;

    @Column(name = "fecha_editado_actividad")
    private LocalDateTime fechaEditadoActividad;

    public ActividadAccion() {
    }

    public ActividadAccion(
            Long codigoActividad,
            Integer numeroActividad,
            LocalDate fechaInicialActividad,
            LocalDate fechaMaxActividad,
            String estadoActividad,
            AccionPlanAmbiental accionPlanAmbiental,
            LocalDateTime fechaCreadoActividad,
            LocalDateTime fechaEditadoActividad) {
        this.codigoActividad = codigoActividad;
        this.numeroActividad = numeroActividad;
        this.fechaInicialActividad = fechaInicialActividad;
        this.fechaMaxActividad = fechaMaxActividad;
        this.estadoActividad = estadoActividad;
        this.accionPlanAmbiental = accionPlanAmbiental;
        this.fechaCreadoActividad = fechaCreadoActividad;
        this.fechaEditadoActividad = fechaEditadoActividad;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (fechaCreadoActividad == null) {
            fechaCreadoActividad = now;
        }
        fechaEditadoActividad = now;
    }

    @PreUpdate
    public void preUpdate() {
        fechaEditadoActividad = LocalDateTime.now();
    }

    @AssertTrue(message = "La fecha maxima debe ser posterior o igual a la fecha inicial.")
    public boolean isRangoFechasValido() {
        if (fechaInicialActividad == null || fechaMaxActividad == null) {
            return true;
        }
        return !fechaMaxActividad.isBefore(fechaInicialActividad);
    }

    public Long getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(Long codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public Integer getNumeroActividad() {
        return numeroActividad;
    }

    public void setNumeroActividad(Integer numeroActividad) {
        this.numeroActividad = numeroActividad;
    }

    public LocalDate getFechaInicialActividad() {
        return fechaInicialActividad;
    }

    public void setFechaInicialActividad(LocalDate fechaInicialActividad) {
        this.fechaInicialActividad = fechaInicialActividad;
    }

    public LocalDate getFechaMaxActividad() {
        return fechaMaxActividad;
    }

    public void setFechaMaxActividad(LocalDate fechaMaxActividad) {
        this.fechaMaxActividad = fechaMaxActividad;
    }

    public String getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(String estadoActividad) {
        this.estadoActividad = estadoActividad;
    }

    public AccionPlanAmbiental getAccionPlanAmbiental() {
        return accionPlanAmbiental;
    }

    public void setAccionPlanAmbiental(AccionPlanAmbiental accionPlanAmbiental) {
        this.accionPlanAmbiental = accionPlanAmbiental;
    }

    public LocalDateTime getFechaCreadoActividad() {
        return fechaCreadoActividad;
    }

    public void setFechaCreadoActividad(LocalDateTime fechaCreadoActividad) {
        this.fechaCreadoActividad = fechaCreadoActividad;
    }

    public LocalDateTime getFechaEditadoActividad() {
        return fechaEditadoActividad;
    }

    public void setFechaEditadoActividad(LocalDateTime fechaEditadoActividad) {
        this.fechaEditadoActividad = fechaEditadoActividad;
    }
}
