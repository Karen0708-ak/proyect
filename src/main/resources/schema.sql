CREATE TABLE IF NOT EXISTS actividad_accion (
    codigo_actividad BIGINT NOT NULL AUTO_INCREMENT,
    numero_actividad INT NULL,
    fecha_inicial_actividad DATE NULL,
    fecha_max_actividad DATE NULL,
    estado_actividad VARCHAR(255) NULL,
    fk_cod_accion BIGINT NULL,
    fecha_creado_actividad DATETIME NULL,
    fecha_editado_actividad DATETIME NULL,
    PRIMARY KEY (codigo_actividad)
);

ALTER TABLE actividad_accion
    ADD COLUMN IF NOT EXISTS numero_actividad INT NULL,
    ADD COLUMN IF NOT EXISTS fecha_inicial_actividad DATE NULL,
    ADD COLUMN IF NOT EXISTS fecha_max_actividad DATE NULL,
    ADD COLUMN IF NOT EXISTS estado_actividad VARCHAR(255) NULL,
    ADD COLUMN IF NOT EXISTS fk_cod_accion BIGINT NULL,
    ADD COLUMN IF NOT EXISTS fecha_creado_actividad DATETIME NULL,
    ADD COLUMN IF NOT EXISTS fecha_editado_actividad DATETIME NULL;
