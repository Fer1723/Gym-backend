package gym_system.com.mx.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gym_system.com.mx.entity.CatalogoMembresia;
import gym_system.com.mx.service.MembresiaService;

@RestController
@RequestMapping("/api/membresias")
public class MembresiaController {
	@Autowired
	private MembresiaService membresiaService;
	
	@GetMapping
	public List<CatalogoMembresia> listarMembresias(){
		return membresiaService.obtenerTodas();
	}
	
	@PostMapping
	public CatalogoMembresia registrarMembresia(@RequestBody CatalogoMembresia membresia) {
		return membresiaService.guardarMembresia(membresia);
	}
	
	@PutMapping("/{id}")
	public CatalogoMembresia actualizarMembresia(@PathVariable Integer id, @RequestBody CatalogoMembresia membresia) {
		return membresiaService.actualizarMembresia(id, membresia);
	}
	
	@DeleteMapping("/{id}")
	public void eliminarMembresia(@PathVariable Integer id) {
		membresiaService.eliminarMembresia(id);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CatalogoMembresia> obtenerMembresiaPorId(@PathVariable Integer id){
		Optional<CatalogoMembresia> membresia = membresiaService.obetenerPorId(id);
		
		if(membresia.isPresent()) {
			return ResponseEntity.ok(membresia.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
