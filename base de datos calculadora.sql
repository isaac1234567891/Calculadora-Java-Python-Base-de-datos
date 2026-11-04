CREATE DATABASE IF NOT EXISTS calculadora_db;
USE calculadora_db;

-- 1. Tabla Usuarios (Independiente)
CREATE TABLE Usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL,
    Contraseña VARCHAR(255) NOT NULL
);

-- 2. Tabla Catalogo_funciones (Independiente)
CREATE TABLE Catalogo_funciones (
    id_funcion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    sintaxis VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    Tipo_operacion VARCHAR(100)
);

-- 3. Tabla Registros_Usuarios (Depende de Usuarios)
CREATE TABLE Registros_Usuarios (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado_cuenta VARCHAR(100),
    token_activado VARCHAR(255) UNIQUE,
    CONSTRAINT fk_registros_usuario FOREIGN KEY (id_usuario) 
        REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
);

-- 4. Tabla Recuperacion_Contraseña (Depende de Usuarios)
CREATE TABLE Recuperacion_Contraseña (
    id_recuperacion INT AUTO_INCREMENT PRIMARY KEY,
    Usuario_id INT NOT NULL,
    Token VARCHAR(255) UNIQUE NOT NULL,
    Expiracion TIMESTAMP NOT NULL,
    usado BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_recuperacion_usuario FOREIGN KEY (Usuario_id) 
        REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
);

-- 5. Tabla Operaciones (Depende de Usuarios y Catalogo_funciones)
CREATE TABLE Operaciones (
    id_operaciones INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_funcion INT NOT NULL,
    operando_a NUMERIC,
    operando_b NUMERIC,
    resultado NUMERIC,
    estado_error VARCHAR(255),
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_operaciones_usuario FOREIGN KEY (id_usuario) 
        REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_operaciones_funcion FOREIGN KEY (id_funcion) 
        REFERENCES Catalogo_funciones(id_funcion) ON DELETE CASCADE
);

-- 6. Tabla Graficos_generados (Depende de Operaciones)
CREATE TABLE Graficos_generados (
    id_graficos INT AUTO_INCREMENT PRIMARY KEY,
    id_operacion INT NOT NULL UNIQUE,
    ruta_archivo VARCHAR(255) UNIQUE NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_graficos_operacion FOREIGN KEY (id_operacion) 
        REFERENCES Operaciones(id_operaciones) ON DELETE CASCADE
);