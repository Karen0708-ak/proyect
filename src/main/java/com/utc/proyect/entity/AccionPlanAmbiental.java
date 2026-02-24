package com.utc.proyect.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "accion_plan_ambiental")
public class AccionPlanAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_accion")
    private Long codigoAccion;

    @Lob
    @Column(name = "aspecto_ambiental_accion", columnDefinition = "LONGTEXT")
    private String aspectoAmbientalAccion;

    @Lob
    @Column(name = "impacto_ambiental_accion", columnDefinition = "LONGTEXT")
    private String impactoAmbientalAccion;

    @Lob
    @Column(name = "medidas_propuestas_accion", columnDefinition = "LONGTEXT")
    private String medidasPropuestasAccion;

    @Column(name = "numerador_valor_accion")
    private Integer numeradorValorAccion;

    @Column(name = "denominador_valor_accion")
    private Integer denominadorValorAccion;

    @Column(name = "estado_aplica")
    private Integer estadoAplica;

    @Column(name = "color_accion", length = 25)
    private String colorAccion;

    @Column(name = "valor_accion", length = 25)
    private String valorAccion;

    @Column(name = "frecuencia_accion")
    private Integer frecuenciaAccion;

    @Column(name = "periodo_accion", length = 255)
    private String periodoAccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_cod_seccion", nullable = false)
    private SeccionPlanAmbiental seccion;

    @Column(name = "fecha_creado_accion", updatable = false)
    private LocalDateTime fechaCreadoAccion;

    @Column(name = "fecha_editado_accion")
    private LocalDateTime fechaEditadoAccion;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (fechaCreadoAccion == null) {
            fechaCreadoAccion = now;
        }
        fechaEditadoAccion = now;
    }

    @PreUpdate
    public void preUpdate() {
        fechaEditadoAccion = LocalDateTime.now();
    }

    public Long getCodigoAccion() {
        return codigoAccion;
    }

    public void setCodigoAccion(Long codigoAccion) {
        this.codigoAccion = codigoAccion;
    }

    public String getAspectoAmbientalAccion() {
        return aspectoAmbientalAccion;
    }

    public void setAspectoAmbientalAccion(String aspectoAmbientalAccion) {
        this.aspectoAmbientalAccion = aspectoAmbientalAccion;
    }

    public String getImpactoAmbientalAccion() {
        return impactoAmbientalAccion;
    }

    public void setImpactoAmbientalAccion(String impactoAmbientalAccion) {
        this.impactoAmbientalAccion = impactoAmbientalAccion;
    }

    public String getMedidasPropuestasAccion() {
        return medidasPropuestasAccion;
    }

    public void setMedidasPropuestasAccion(String medidasPropuestasAccion) {
        this.medidasPropuestasAccion = medidasPropuestasAccion;
    }

    public Integer getNumeradorValorAccion() {
        return numeradorValorAccion;
    }

    public void setNumeradorValorAccion(Integer numeradorValorAccion) {
        this.numeradorValorAccion = numeradorValorAccion;
    }

    public Integer getDenominadorValorAccion() {
        return denominadorValorAccion;
    }

    public void setDenominadorValorAccion(Integer denominadorValorAccion) {
        this.denominadorValorAccion = denominadorValorAccion;
    }

    public Integer getEstadoAplica() {
        return estadoAplica;
    }

    public void setEstadoAplica(Integer estadoAplica) {
        this.estadoAplica = estadoAplica;
    }

    public String getColorAccion() {
        return colorAccion;
    }

    public void setColorAccion(String colorAccion) {
        this.colorAccion = colorAccion;
    }

    public String getValorAccion() {
        return valorAccion;
    }

    public void setValorAccion(String valorAccion) {
        this.valorAccion = valorAccion;
    }

    public Integer getFrecuenciaAccion() {
        return frecuenciaAccion;
    }

    public void setFrecuenciaAccion(Integer frecuenciaAccion) {
        this.frecuenciaAccion = frecuenciaAccion;
    }

    public String getPeriodoAccion() {
        return periodoAccion;
    }

    public void setPeriodoAccion(String periodoAccion) {
        this.periodoAccion = periodoAccion;
    }

    public SeccionPlanAmbiental getSeccion() {
        return seccion;
    }

    public void setSeccion(SeccionPlanAmbiental seccion) {
        this.seccion = seccion;
    }

    public LocalDateTime getFechaCreadoAccion() {
        return fechaCreadoAccion;
    }

    public void setFechaCreadoAccion(LocalDateTime fechaCreadoAccion) {
        this.fechaCreadoAccion = fechaCreadoAccion;
    }

    public LocalDateTime getFechaEditadoAccion() {
        return fechaEditadoAccion;
    }

    public void setFechaEditadoAccion(LocalDateTime fechaEditadoAccion) {
        this.fechaEditadoAccion = fechaEditadoAccion;
    }
}
