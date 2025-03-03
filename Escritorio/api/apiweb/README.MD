package com.api.app.controller;

import com.api.app.model.Cita;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@Tag(name = "Citas API", description = "API para gestionar citas médicas y autenticación de usuarios")
public class Controller {

    private List<Cita> citas = new ArrayList<>();
    private Long contadorId = 1L;

    /**
     * Sirve el archivo HTML de la página de inicio.
     * @return HTML de la página de inicio.
     * @throws IOException Si hay un error al leer el archivo.
     */
    @GetMapping
    @Operation(summary = "Obtener la página de inicio", description = "Devuelve el HTML de la página de inicio.")
    public ResponseEntity<String> home() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/index.html");
        String html = new String(StreamUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
        return new ResponseEntity<>(html, HttpStatus.OK);
    }

    /**
     * Crea una nueva cita médica.
     * @param cita Objeto de tipo Cita.
     * @return La cita creada.
     */
    @PostMapping("/citas")
    @Operation(summary = "Crear cita médica", description = "Registra una nueva cita médica.")
    public Cita crearCita(@RequestBody Cita cita) {
        cita.setId(contadorId++);
        citas.add(cita);
        return cita;
    }

    /**
     * Obtiene todas las citas médicas registradas.
     * @return Lista de citas.
     */
    @GetMapping("/citas")
    @Operation(summary = "Obtener todas las citas", description = "Devuelve una lista de todas las citas médicas registradas.")
    public List<Cita> obtenerCitas() {
        return citas;
    }

    /**
     * Obtiene una cita médica por su ID.
     * @param id Identificador de la cita.
     * @return Objeto de tipo Cita o null si no se encuentra.
     */
    @GetMapping("/citas/{id}")
    @Operation(summary = "Obtener una cita por ID", description = "Devuelve una cita médica específica mediante su ID.")
    public Cita obtenerCita(@PathVariable Long id) {
        return citas.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Actualiza los datos de una cita médica.
     * @param id ID de la cita a actualizar.
     * @param nuevaCita Nuevos datos de la cita.
     * @return Cita actualizada o null si no se encuentra.
     */
    @PutMapping("/citas/{id}")
    @Operation(summary = "Actualizar cita médica", description = "Modifica los datos de una cita médica existente.")
    public Cita actualizarCita(@PathVariable Long id, @RequestBody Cita nuevaCita) {
        Optional<Cita> citaExistente = citas.stream().filter(c -> c.getId().equals(id)).findFirst();
        if (citaExistente.isPresent()) {
            Cita cita = citaExistente.get();
            cita.setPaciente(nuevaCita.getPaciente());
            cita.setFechaHora(nuevaCita.getFechaHora());
            cita.setMotivo(nuevaCita.getMotivo());
            return cita;
        }
        return null;
    }

    /**
     * Elimina una cita médica por su ID.
     * @param id ID de la cita a eliminar.
     * @return Mensaje de confirmación.
     */
    @DeleteMapping("/citas/{id}")
    @Operation(summary = "Eliminar cita médica", description = "Borra una cita médica según su ID.")
    public String eliminarCita(@PathVariable Long id) {
        if (citas.removeIf(c -> c.getId().equals(id))) {
            return "Cita eliminada";
        }
        return "Cita no encontrada";
    }

    private static final String USUARIO_VALIDO = "MASTER";
    private static final String CONTRASEÑA_VALIDA = "0000";

    /**
     * Autenticación de usuario.
     * @param usuario Nombre de usuario.
     * @param contraseña Contraseña del usuario.
     * @return Mensaje de autenticación exitosa o error.
     */
    @PostMapping("/auth/login")
    @Operation(summary = "Autenticación de usuario", description = "Permite iniciar sesión con credenciales válidas.")
    public ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String contraseña) {
        if (USUARIO_VALIDO.equals(usuario) && CONTRASEÑA_VALIDA.equals(contraseña)) {
            return ResponseEntity.ok("Autenticado");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Usuario o contraseña incorrectos");
        }
    }
}




# Proyecto: API de Gestión de Citas Médicas

## Descripción
Esta API permite gestionar citas médicas a través de operaciones REST. Incluye funcionalidades para autenticación de usuarios, creación, consulta, actualización y eliminación de citas médicas.

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3**
- **Maven**


## Instalación y Ejecución
### 1. Clonar el repositorio
```bash
git clone <https://github.com/programandounsue/APIWEB.git>
cd APIWEB
```

### 2. Construir y ejecutar el proyecto
```bash
mvn spring-boot:run
```

El servidor se ejecutará en `http://localhost:8080`

## Documentación de la API
Swagger está disponible en:
## `http://localhost:8080/swagger-ui.html`


## Endpoints Principales
## Método | Endpoint | Descripción 

## POST    `/auth/login`  Iniciar sesión 


## Versionamiento
Este proyecto está versionado utilizando **Git**. Enlace al repositorio:  
🔗 **[Repositorio en GitHub](<https://github.com/programandounsue/APIWEB.git>)**

## Autor
- **ARLEY TORRES APRENDIS A INGENIERO**

---



