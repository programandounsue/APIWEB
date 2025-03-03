package com.api.app.controller;

import com.api.app.model.Cita;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class controller {

    // Servir el HTML al acceder a la raíz
    @GetMapping
    public ResponseEntity<String> home() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/index.html");
        String html = new String(StreamUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
        return new ResponseEntity<>(html, HttpStatus.OK);
    }

    // Lista de citas (simulación de base de datos en memoria)
    private List<Cita> citas = new ArrayList<>();
    private Long contadorId = 1L;

    // Crear cita
    @PostMapping("/citas")
    public Cita crearCita(@RequestBody Cita cita) {
        cita.setId(contadorId++);
        citas.add(cita);
        return cita;
    }

    // Obtener todas las citas
    @GetMapping("/citas")
    public List<Cita> obtenerCitas() {
        return citas;
    }

    // Obtener una cita por ID
    @GetMapping("/citas/{id}")
    public Cita obtenerCita(@PathVariable Long id) {
        return citas.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    // Actualizar una cita
    @PutMapping("/citas/{id}")
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

    // Eliminar una cita
    @DeleteMapping("/citas/{id}")
    public String eliminarCita(@PathVariable Long id) {
        if (citas.removeIf(c -> c.getId().equals(id))) {
            return "Cita eliminada";
        }
        return "Cita no encontrada";
    }

    // Autenticación (login)
    private static final String USUARIO_VALIDO = "MASTER";
    private static final String CONTRASEÑA_VALIDA = "0000";

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String contraseña) {
        if (USUARIO_VALIDO.equals(usuario) && CONTRASEÑA_VALIDA.equals(contraseña)) {
            return ResponseEntity.ok("Autenticado");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Usuario o contraseña incorrectos");
        }
    }
}
