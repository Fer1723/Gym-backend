package gym_system.com.mx.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gym_system.com.mx.entity.Suscripcion;
import gym_system.com.mx.repository.SuscripcionRepository;
import jakarta.transaction.Transactional;

@Component
public class TareaProgramadaService {
	
	@Autowired
	private SuscripcionRepository suscripcionRepository;
	
	@Scheduled(cron = "0 30 22 * * ?")
	@Transactional
	public void actualizarVencimientosNocturnos() {
		System.out.println("🦉 Velador Nocturno despertando (10:30 PM): Buscando planes vencidos...");
		ejecutarLimpieza();
	}
	
	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void actualizarVencimientosAlEncender() {
		System.out.println("🌅 Sistema Iniciado: Verificando membresías que vencieron mientras el sistema estaba apagado...");
		ejecutarLimpieza();
	}
	public void ejecutarLimpieza() {
		LocalDate hoy = LocalDate.now();
		
		// 🚨 Así es más rápido: La base de datos hace el filtro, no la memoria RAM
		List<Suscripcion> vencidas = suscripcionRepository.findByEstadoIgnoreCaseAndFechaFinBefore("ACTIVA", hoy);
		
		if(!vencidas.isEmpty()) {
			for(Suscripcion s : vencidas) {
				s.setEstado("VENCIDA");
			}
			suscripcionRepository.saveAll(vencidas);
			System.out.println("✅ Se han vencido " + vencidas.size() + " suscripciones automáticamente.");
		} else {
			System.out.println("💤 Todo en orden. Nadie venció hoy. Volviendo a dormir.");
		}
	}

}
