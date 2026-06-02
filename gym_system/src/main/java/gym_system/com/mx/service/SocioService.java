package gym_system.com.mx.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import gym_system.com.mx.GymSystemApplication;
import gym_system.com.mx.entity.Socio;
import gym_system.com.mx.repository.SocioRepository;

@Service
public class SocioService {

	@Autowired
	private SocioRepository socioRepository;

	public List<Socio> obtenerTodos(){
		return socioRepository.findAll();
	}
	
	public List<Socio> obtenerSoloActivos(){
		return socioRepository.findByEstadoTrue();
	}
	
	public Optional<Socio> obtenerPorId(Integer id){
		return socioRepository.findById(id);
	}
	
	public Socio guardarSocio(Socio socio) {
		return socioRepository.save(socio);
	}
	
	public Socio actualizarSocio(Integer id, Socio socioActualizado) {
		return socioRepository.findById(id).map(socioExistente ->{
			socioExistente.setNombre(socioActualizado.getNombre());
			socioExistente.setApellido(socioActualizado.getApellido());
			socioExistente.setTelefono(socioActualizado.getTelefono());
			socioExistente.setFotoBase64(socioActualizado.getFotoBase64());
			socioExistente.setHuellaTemplate(socioActualizado.getHuellaTemplate());
			return socioRepository.save(socioExistente);
		}).orElseThrow(() -> new RuntimeException("Socio no encontrado"));
	}
	
	public Socio actualizarEstado(Integer id, boolean nuevoEstado) {
		Socio socio = socioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Socio no encontrado"));
		
		socio.setEstado(nuevoEstado);
		
		// 🕵️‍♂️ EL RASTREADOR DE AUDITORÍA
		if (!nuevoEstado) {
			// Si el estado es 'false' (Dar de baja), registramos quién apretó el botón
			String usuarioLogueado = SecurityContextHolder.getContext().getAuthentication().getName();
			socio.setDadoDeBajaPor(usuarioLogueado);
		} else {
			// Si el estado es 'true' (Reincorporación), limpiamos su expediente
			socio.setDadoDeBajaPor(null);
		}
		
		return socioRepository.save(socio);
	}
}
