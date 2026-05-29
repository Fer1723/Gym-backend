package gym_system.com.mx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gym_system.com.mx.service.AccesoService;

@RestController
@RequestMapping("/api/accesos")
@CrossOrigin(originPatterns = "*")
public class AccesoController {
	@Autowired
	private AccesoService accesoService;
	
	@PostMapping("/simular/{idSocio}")
	public ResponseEntity<?> simularHuella(@PathVariable Integer idSocio){
		accesoService.simularAcceso(idSocio);
		return ResponseEntity.ok("{\\\"status\\\":\\\"Huella simulada procesada y enviada a Electron\\\"}");
	}

}
