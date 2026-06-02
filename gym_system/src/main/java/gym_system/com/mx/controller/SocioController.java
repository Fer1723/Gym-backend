package gym_system.com.mx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gym_system.com.mx.entity.Socio;
import gym_system.com.mx.service.SocioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

	// 🧹 Limpiamos el llamado directo al Repository. El Controller solo debe hablar con el Service.
	@Autowired
	private SocioService socioService;
	
	@GetMapping
	public ResponseEntity<List<Socio>> obtenerTodos() {
		List<Socio> listaCompleta = socioService.obtenerTodos();
		
		// 🚨 EL CHIVATO: Va a imprimir en la consola negra de Spring Boot (Eclipse/IntelliJ)
		System.out.println("🚨 ALERTA DETECTIVE: Java extrajo " + listaCompleta.size() + " socios de la base de datos.");
		
		return ResponseEntity.ok(listaCompleta);
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<Socio>> obtenerSoloActivos() {
		return ResponseEntity.ok(socioService.obtenerSoloActivos());
	}
	
	@PostMapping
	public ResponseEntity<Socio> registrarSocio(@Valid @RequestBody Socio socio) {
		return ResponseEntity.ok(socioService.guardarSocio(socio));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Socio> actualizarSocio(@PathVariable Integer id, @RequestBody Socio socio){
		return ResponseEntity.ok(socioService.actualizarSocio(id, socio));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Socio> obtenerSocioPorId(@PathVariable Integer id){
		Optional<Socio> socio = socioService.obtenerPorId(id);
		
		if(socio.isPresent()) {
			return ResponseEntity.ok(socio.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// 🛡️ EL ÚNICO PUNTO DE ACCESO PARA BAJAS Y REINCORPORACIONES
	@PatchMapping("/{id}/estado")
	public ResponseEntity<Map<String, String>> actualizarEstado(@PathVariable Integer id, @RequestParam boolean estado) {
		Map<String, String> respuesta = new HashMap<>();
		try {
			// 🔐 Aquí está la magia: Llamamos al SERVICE, el cual ejecutará tu lógica de auditoría
			socioService.actualizarEstado(id, estado);
			
			respuesta.put("mensaje", "Estado actualizado correctamente");
			return ResponseEntity.ok(respuesta);
			
		} catch (Exception e) {
			respuesta.put("error", "No se pudo actualizar: " + e.getMessage());
			return ResponseEntity.badRequest().body(respuesta);
		}
	}
}