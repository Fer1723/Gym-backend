package gym_system.com.mx.controller;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gym_system.com.mx.entity.Visitas;
import gym_system.com.mx.repository.VisitaRepository;
import gym_system.com.mx.service.TurnoCajaService;

@RestController
@RequestMapping("/api/visitas")
public class VisitaController {
	@Autowired
	private VisitaRepository visitaRepository;
	
	@Autowired
	private TurnoCajaService turnoCajaService;
	
	public static final String Pool_ALFANUMERICO = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	public static final int LONGITUD_PIN = 5;
	public final SecureRandom secureRandom = new SecureRandom();
	
	@PostMapping("/generar")
	public ResponseEntity<?> generarVisita(@RequestParam Double monto, @RequestParam String metodoPago){
		try {
			gym_system.com.mx.entity.TurnoCaja turnoActual = turnoCajaService.getTurnoActivoDelUsuarioLogueado();
			
			Visitas v = new Visitas();
			v.setMonto(monto);
			v.setMetodoPago(metodoPago);
			v.setTurnoCaja(turnoActual);
			
			v.setFechaCreacion(LocalDateTime.now());
			v.setUsado(false);
			// Generamos el PIN Blindado (Ejemplo: V-K7R9X)
			v.setPin(generarPinAlfanumerico());
			return ResponseEntity.ok(visitaRepository.save(v));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Error en caja: " + e.getMessage()));
		}	
	}
	
	@GetMapping("/validar/{pin}")
	public ResponseEntity<?> validadrAcceso(@PathVariable String pin){
		return visitaRepository.findByPinAndUsadoFalse(pin.toUpperCase())
				.map(v ->{
					v.setUsado(true);
					visitaRepository.save(v);
					return ResponseEntity.ok("{\"acceso\": true, \"mensaje\": \"¡Visita Express Válida! Bienvenido\"}");
				})
				.orElse(ResponseEntity.status(403).body("{\"acceso\": false, \"mensaje\": \"Código de visita inválido o ya utilizado\"}"));
	}
	
	@GetMapping("/stats/mes-actual")
    public ResponseEntity<Map<String, Object>> getStatsMesActual() {
        // 1. Calculamos el inicio y fin del mes actual
        YearMonth mesActual = YearMonth.now();
        LocalDateTime inicioMes = mesActual.atDay(1).atStartOfDay();
        LocalDateTime finMes = mesActual.atEndOfMonth().atTime(23, 59, 59);

        // 2. Ejecutamos las consultas de la bóveda
        Double ingresosVisitas = visitaRepository.sumarIngresosPorFecha(inicioMes, finMes);
        Long totalVisitas = visitaRepository.contarVisitasPorFecha(inicioMes, finMes);

        // 3. Empaquetamos la respuesta
        Map<String, Object> stats = new HashMap<>();
        stats.put("ingresosVisitas", ingresosVisitas);
        stats.put("totalVisitas", totalVisitas);

        return ResponseEntity.ok(stats);
    }
	
	private String generarPinAlfanumerico() {
		StringBuilder sb = new StringBuilder("V-");
		for(int i = 0; i < LONGITUD_PIN; i++) {
			int index = secureRandom.nextInt(Pool_ALFANUMERICO.length());
			sb.append(Pool_ALFANUMERICO.charAt(index));
		}
		return sb.toString();
	}
}
