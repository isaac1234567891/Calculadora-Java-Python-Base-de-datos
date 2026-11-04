-- 1. Elimina la base de datos previa (si existe) y crea una versión limpia
DROP DATABASE IF EXISTS calculadora_db;
CREATE DATABASE calculadora_db;
USE calculadora_db;

-- 2. Crear tabla: Usuarios
CREATE TABLE Usuarios (
    Id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL,
    Contraseña VARCHAR(255) NOT NULL
);

-- 3. Crear tabla: Registros_Usuarios
CREATE TABLE Registros_Usuarios (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL UNIQUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado_cuenta VARCHAR(50),
    token_activado VARCHAR(255) UNIQUE NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(Id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 4. Crear tabla: Recuperacion_Contraseña
CREATE TABLE Recuperacion_Contraseña (
    Id_recuperacion INT AUTO_INCREMENT PRIMARY KEY,
    Usuario_id INT NOT NULL,
    Token VARCHAR(255) UNIQUE NOT NULL,
    Expiracion TIMESTAMP NOT NULL,
    usado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (Usuario_id) REFERENCES Usuarios(Id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 5. Crear tabla: Catalogo_funciones
CREATE TABLE Catalogo_funciones (
    id_funcion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    sintaxis VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    Tipo_operacion VARCHAR(100)
);

-- 6. Crear tabla: Operaciones
CREATE TABLE Operaciones (
    id_operaciones INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_funcion INT NOT NULL,
    operando_a DECIMAL(15,4),
    operando_b DECIMAL(15,4),
    resultado DECIMAL(15,4),
    estado_error VARCHAR(255),
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(Id_usuario) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_funcion) REFERENCES Catalogo_funciones(id_funcion) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT check_estado_error CHECK (estado_error IN ('OK', 'DIVISION_POR_CERO', 'ERROR_SINTAXIS', 'INDEFINIDO') OR estado_error IS NOT NULL)
);

-- 7. Crear tabla: Graficos_generados
CREATE TABLE Graficos_generados (
    id_graficos INT AUTO_INCREMENT PRIMARY KEY,
    id_operacion INT NOT NULL UNIQUE,
    ruta_archivo VARCHAR(500) UNIQUE NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_operacion) REFERENCES Operaciones(id_operaciones) ON DELETE CASCADE ON UPDATE CASCADE
);