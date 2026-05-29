package gym_system.com.mx.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gym_system.com.mx.entity.TurnoCaja;
import gym_system.com.mx.entity.Usuario;
import gym_system.com.mx.repository.TurnoCajaRepository;
import gym_system.com.mx.repository.UsuarioRepository;

import org.springframework.security.core.context.SecurityContextHolder;
@Service
public class TurnoCajaService {
	@Autowired
	private TurnoCajaRepository turnoCajaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Optional<TurnoCaja> getTurnoActivo(Integer usuarioId){
		return turnoCajaRepository.findByUsuarioIdAndEstado(usuarioId, "ABIERTO");
	}
	
	public TurnoCaja abrirTurno(Integer usuarioId, Double efectivoInicial, String observaciones) throws Exception{
		Optional<TurnoCaja> turnoActivo = getTurnoActivo(usuarioId);
		if(turnoActivo.isPresent()) {
			throw new Exception("El usuario ya tiene un turno abierto. Debe cerrar antes de abrir uno nuevo.");
		}
		
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new Exception("Usuario no encontrado en el sistema"));
		
		TurnoCaja nuevoTurno = new TurnoCaja();
        nuevoTurno.setUsuario(usuario);
        nuevoTurno.setFechaApertura(LocalDateTime.now());
        nuevoTurno.setEfectivoInicial(efectivoInicial);
        nuevoTurno.setEstado("ABIERTO");
        
        nuevoTurno.setObservaciones(observaciones);

        return turnoCajaRepository.save(nuevoTurno);
	}
	
	public TurnoCaja cerrarTurno(Long turnoId, Double efectivoFinal) throws Exception{
		TurnoCaja turno = turnoCajaRepository.findById(turnoId)
                .orElseThrow(() -> new Exception("El turno no existe."));

        if ("CERRADO".equals(turno.getEstado())) {
            throw new Exception("Este turno ya fue cerrado previamente.");
        }

        turno.setFechaCierre(LocalDateTime.now());
        turno.setEfectivoFinal(efectivoFinal);
        turno.setEstado("CERRADO");

        return turnoCajaRepository.save(turno);
	}
	
	public TurnoCaja getTurnoActivoDelUsuarioLogueado() throws Exception{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Usuario usuario = usuarioRepository.findByUsername(username)
				.orElseThrow(()-> new Exception("Usuario no encontrado:" + username));
		return getTurnoActivo(usuario.getId())
				.orElseThrow(() -> new Exception("Accion denegada: no tienes un turno de caja abierto para registrar ingresos"));
	}
	
	public Double obtenerUltimoEfectivo() {
		return turnoCajaRepository.findTopByOrderByFechaAperturaDesc()
				.map(TurnoCaja::getEfectivoFinal)
				.orElse(0.0);
	}
	
	public List<TurnoCaja> obtenerAlertasAuditoria(){
		List<TurnoCaja> alertas = turnoCajaRepository.findByObservacionesContaining("ALERTA");
		
		return alertas.stream()
						.filter(t -> t.getObservaciones() != null && !t.getObservaciones().contains("[RESUELTO]"))
						.toList();
	}
	
public TurnoCaja resolverDiscrepancia(Long turnoId, Double montoRepuesto, String notaResolucion) throws Exception {
		
		// 1. Buscamos el turno que tiene el problema (el turno viejo)
		TurnoCaja turnoViejo = turnoCajaRepository.findById(turnoId)
				.orElseThrow(() -> new Exception("Turno no encontrado"));
		
		// 2. Le ponemos el sello de resuelto para limpiar el historial 
        // (Pero NO le movemos el dinero, el pasado se queda como evidencia)
		String nuevaObservacion = turnoViejo.getObservaciones() + " | [RESUELTO]: " + notaResolucion;
		turnoViejo.setObservaciones(nuevaObservacion);
		turnoCajaRepository.save(turnoViejo);
		
		// 3. Si se entregó dinero físico, se lo inyectamos al turno ACTIVO (El del dueño en este momento)
		if (montoRepuesto > 0) {
			TurnoCaja turnoActivo = getTurnoActivoDelUsuarioLogueado();
			turnoActivo.setEfectivoInicial(turnoActivo.getEfectivoInicial() + montoRepuesto);
			turnoCajaRepository.save(turnoActivo);
		}
		
		return turnoViejo;
	}
}
