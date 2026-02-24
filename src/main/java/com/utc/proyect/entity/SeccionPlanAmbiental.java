package com.utc.proyect.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "seccion_plan_ambiental")
public class SeccionPlanAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_seccion")
    private Long codigoSeccion;

    @Column(name = "numero_seccion")
    @NotNull(message = "El numero de seccion es obligatorio.")
    @Min(value = 1, message = "El numero de seccion debe ser mayor o igual a 1.")
    @Max(value = 9999, message = "El numero de seccion no puede superar 9999.")
    private Integer numeroSeccion;

    @Column(name = "titulo_seccion", length = 255, nullable = false)
    @NotBlank(message = "El titulo de la seccion es obligatorio.")
    @Size(min = 3, max = 255, message = "El titulo debe tener entre 3 y 255 caracteres.")
    private String tituloSeccion;

    @Lob
    @Column(name = "objetivo_seccion", columnDefinition = "LONGTEXT")
    @Size(max = 4000, message = "El objetivo no puede superar 4000 caracteres.")
    private String objetivoSeccion;

    @Column(name = "color_seccion", length = 25)
    @Size(max = 25, message = "El color no puede superar 25 caracteres.")
    private String colorSeccion;

    @Column(name = "fk_cod_etapa")
    private Long fkCodEtapa;

    @Column(name = "fecha_creado_seccion", updatable = false)
    private LocalDateTime fechaCreadoSeccion;

    @Column(name = "fecha_editado_seccion")
    private LocalDateTime fechaEditadoSeccion;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (fechaCreadoSeccion == null) {
            fechaCreadoSeccion = now;
        }
        fechaEditadoSeccion = now;
    }

    @PreUpdate
    public void preUpdate() {
        fechaEditadoSeccion = LocalDateTime.now();
    }

    public Long getCodigoSeccion() {
        return codigoSeccion;
    }

    public void setCodigoSeccion(Long codigoSeccion) {
        this.codigoSeccion = codigoSeccion;
    }

    public Integer getNumeroSeccion() {
        return numeroSeccion;
    }

    public void setNumeroSeccion(Integer numeroSeccion) {
        this.numeroSeccion = numeroSeccion;
    }

    public String getTituloSeccion() {
        return tituloSeccion;
    }

    public void setTituloSeccion(String tituloSeccion) {
        this.tituloSeccion = tituloSeccion;
    }

    public String getObjetivoSeccion() {
        return objetivoSeccion;
    }

    public void setObjetivoSeccion(String objetivoSeccion) {
        this.objetivoSeccion = objetivoSeccion;
    }

    public String getColorSeccion() {
        return colorSeccion;
    }

    public void setColorSeccion(String colorSeccion) {
        this.colorSeccion = colorSeccion;
    }

    public Long getFkCodEtapa() {
        return fkCodEtapa;
    }

    public void setFkCodEtapa(Long fkCodEtapa) {
        this.fkCodEtapa = fkCodEtapa;
    }

    public LocalDateTime getFechaCreadoSeccion() {
        return fechaCreadoSeccion;
    }

    public void setFechaCreadoSeccion(LocalDateTime fechaCreadoSeccion) {
        this.fechaCreadoSeccion = fechaCreadoSeccion;
    }

    public LocalDateTime getFechaEditadoSeccion() {
        return fechaEditadoSeccion;
    }

    public void setFechaEditadoSeccion(LocalDateTime fechaEditadoSeccion) {
        this.fechaEditadoSeccion = fechaEditadoSeccion;
    }
}
