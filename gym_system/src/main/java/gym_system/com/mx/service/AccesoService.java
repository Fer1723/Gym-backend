package gym_system.com.mx.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gym_system.com.mx.config.WebSocketConfig;
import gym_system.com.mx.entity.Suscripcion;
import gym_system.com.mx.repository.SocioRepository;
import gym_system.com.mx.repository.SuscripcionRepository;

@Service
public class AccesoService {
	
	@Autowired
	private SocioRepository socioRepository;
	
	@Autowired
	private SuscripcionRepository suscripcionRepository;
	
	public void simularAcceso(Integer idSocio) {
		socioRepository.findById(idSocio).ifPresentOrElse(socio ->{
			List<Suscripcion> suscripciones = suscripcionRepository.findBySocioAndEstado(socio, "ACTIVA");
			LocalDate hoy = LocalDate.now();
			
			boolean tieneAcceso = suscripciones.stream().anyMatch(s ->
					(hoy.isEqual(s.getFechaInicio()) || hoy.isAfter(s.getFechaInicio())) &&
					(hoy.isEqual(s.getFechaFin()) || hoy.isBefore(s.getFechaFin()))
					);
			
			String labelEstado = tieneAcceso ? "Activo" : "Vencido/Inactivo";
			String mensaje = tieneAcceso ? "¡Acceso Autorizado!" : "Acceso Denegado";
			String nombrePlan = tieneAcceso ? suscripciones.get(0).getMembresia().getNombre() : "Sin plan vigente";
			
			String socioJson = String.format(
					"{\"idSocio\": %d, \"nombre\": \"%s %s\", \"labelEstado\": \"%s\", \"mensaje\": \"%s\", \"nombrePlan\": \"%s\"}",
					socio.getIdSocio(), socio.getNombre(), socio.getApellido(), labelEstado, mensaje, nombrePlan
					);
			WebSocketConfig.getHandler().mandarSeñaleAcceso(socioJson);
		}, () ->{
			System.out.println("No se encontro al socio con ID: " + idSocio);
		});
	}
}
