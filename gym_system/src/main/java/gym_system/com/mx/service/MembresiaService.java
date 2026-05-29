package gym_system.com.mx.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gym_system.com.mx.controller.MembresiaController;
import gym_system.com.mx.entity.CatalogoMembresia;
import gym_system.com.mx.repository.MembresiaRepository;

@Service
public class MembresiaService {
	
	@Autowired
	private MembresiaRepository membresiaRepository;
	
	public List<CatalogoMembresia> obtenerTodas(){
		return membresiaRepository.findAll();
	}
	public Optional<CatalogoMembresia> obetenerPorId(Integer id){
		return membresiaRepository.findById(id);
	}
	public CatalogoMembresia guardarMembresia(CatalogoMembresia membresia) {
		return membresiaRepository.save(membresia);
	}
	
	public CatalogoMembresia actualizarMembresia(Integer id, CatalogoMembresia membresiaActualizada) {
		return membresiaRepository.findById(id).map(membresia ->{
			membresia.setNombre(membresiaActualizada.getNombre());
			membresia.setPrecio(membresiaActualizada.getPrecio());
			membresia.setDuracionDias(membresiaActualizada.getDuracionDias());
			membresia.setDescripcion(membresiaActualizada.getDescripcion());
			return membresiaRepository.save(membresia);
		}).orElseThrow(() -> new RuntimeException("Membresia no encontrada"));
	}
	
	public void eliminarMembresia(Integer id) {
		membresiaRepository.deleteById(id);
	}

}
