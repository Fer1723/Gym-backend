package gym_system.com.mx.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import gym.system.com.mx.dto.CorteCajaDTO;
import gym_system.com.mx.entity.Pago;
import gym_system.com.mx.entity.TurnoCaja;
import gym_system.com.mx.entity.Usuario;
import gym_system.com.mx.entity.VentaTienda;
import gym_system.com.mx.entity.Visitas;
import gym_system.com.mx.repository.PagoRepository;
import gym_system.com.mx.repository.TurnoCajaRepository;
import gym_system.com.mx.repository.UsuarioRepository;
import gym_system.com.mx.repository.VentaTiendaRepository;
import gym_system.com.mx.repository.VisitaRepository;

@Service
public class CajaService {
	@Autowired
	private VentaTiendaRepository ventaTiendaRepository;
	
	@Autowired
	private PagoRepository pagoRepository;
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	@Autowired
	private TurnoCajaRepository turnoCajaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TurnoCajaService turnoCajaService;
	
	// 🟢 MÉTODO PÚBLICO: El "Cadenero" que revisa roles
	public CorteCajaDTO obtenerCorteInteligente() throws Exception {
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    Usuario usuario = usuarioRepository.findByUsername(username)
	            .orElseThrow(() -> new Exception("Usuario no encontrado en el sistema"));
	    
	    // Obtenemos tu turno activo (todos necesitan uno para ver la caja)
	    TurnoCaja turnoActual = turnoCajaService.getTurnoActivoDelUsuarioLogueado();
	    
	    if (usuario.getRol().toUpperCase().contains("ADMIN")) {
	        System.out.println("👁️ MODO DIOS ACTIVADO: Calculando TODO el dinero del día para " + username);
	        // Le pasamos el ID de tu turno actual para saber cuánto efectivo tienes físicamente
	        return generarCorteDelDia(turnoActual.getId().intValue()); 
	    } else {
	        System.out.println("💼 MODO RECEPCIÓN: Calculando solo la caja de " + username);
	        return generarCortePorTurno(turnoActual.getId().intValue()); 
	    }
	}

	// 🔴 MÉTODO PRIVADO: Modo Dios Híbrido (Nadie puede llamarlo desde afuera)
	private CorteCajaDTO generarCorteDelDia(Integer turnoActualId) throws Exception {
		CorteCajaDTO corte = new CorteCajaDTO();
		
		LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
		LocalDateTime finDia = LocalDate.now().atTime(LocalTime.MAX);
		
		List<VentaTienda> ventasTienda = ventaTiendaRepository.findByFechaVentaBetween(inicioDia, finDia);
		List<Pago> pagosMembresias = pagoRepository.findByFechaPagoBetween(inicioDia, finDia);
		List<Visitas> visitas = visitaRepository.findByFechaCreacionBetween(inicioDia, finDia);
		
		BigDecimal totalTienda = BigDecimal.ZERO;
		BigDecimal totalMembresias = BigDecimal.ZERO;
		BigDecimal totalVisitas = BigDecimal.ZERO;
				
		BigDecimal totalTarjeta = BigDecimal.ZERO;
		BigDecimal totalTransferencia = BigDecimal.ZERO;
		
		// SUMAMOS GLOBALES DE VENTAS
		for(VentaTienda v : ventasTienda) {
			BigDecimal monto = v.getTotal();
			totalTienda = totalTienda.add(monto);
			if("Tarjeta".equalsIgnoreCase(v.getMetodoPago())) totalTarjeta = totalTarjeta.add(monto);
			else if("Transferencia".equalsIgnoreCase(v.getMetodoPago())) totalTransferencia = totalTransferencia.add(monto);
		}
		
		for(Pago p : pagosMembresias) {
			BigDecimal monto = p.getMonto();
			totalMembresias = totalMembresias.add(monto);
			if("Tarjeta".equalsIgnoreCase(p.getMetodoPago())) totalTarjeta = totalTarjeta.add(monto);
			else if("Transferencia".equalsIgnoreCase(p.getMetodoPago())) totalTransferencia = totalTransferencia.add(monto);
		}
		
		for(Visitas vis : visitas) {
			BigDecimal monto = BigDecimal.valueOf(vis.getMonto());
			totalVisitas = totalVisitas.add(monto);
			if("Tarjeta".equalsIgnoreCase(vis.getMetodoPago())) totalTarjeta = totalTarjeta.add(monto);
			else if("Transferencia".equalsIgnoreCase(vis.getMetodoPago())) totalTransferencia = totalTransferencia.add(monto);
		}
		
		// 🛡️ EL BLINDAJE CONTRA RETIROS: Le pedimos el cálculo del efectivo al método del turno
		CorteCajaDTO corteLocal = generarCortePorTurno(turnoActualId);
		
		corte.setTotalTienda(totalTienda);
		corte.setTotalMembresias(totalMembresias);
		corte.setTotalVisitas(totalVisitas);
		
		corte.setTotalTarjeta(totalTarjeta);
		corte.setTotalTransferencia(totalTransferencia);
		
		// Extraemos solo el efectivo real del cajón (que ya contempla tus retiros)
		corte.setTotalEfectivo(corteLocal.getTotalEfectivo());
		
		corte.setGranTotal(totalTienda.add(totalMembresias).add(totalVisitas));
		
		return corte;
	}
	
	// 🔴 MÉTODO PRIVADO: Modo Recepcionista (Nadie puede llamarlo desde afuera)
	private CorteCajaDTO generarCortePorTurno(Integer turnoId) throws Exception{
		CorteCajaDTO corte = new CorteCajaDTO();
		
		TurnoCaja turno = turnoCajaRepository.findById(turnoId.longValue())
						.orElseThrow(() -> new Exception("Turno no encontrado"));
		
		List<VentaTienda> ventasTienda = ventaTiendaRepository.findByTurnoCaja_Id(turnoId.longValue());
		List<Pago> pagosMembresias = pagoRepository.findByTurnoCaja_Id(turnoId.longValue());
		List<Visitas> visitas = visitaRepository.findByTurnoCaja_Id(turnoId.longValue());
				
		BigDecimal totalTienda = BigDecimal.ZERO;
		BigDecimal totalMembresias = BigDecimal.ZERO;
		BigDecimal totalVisitas = BigDecimal.ZERO;
				
		BigDecimal totalEfectivo = BigDecimal.ZERO;
		BigDecimal totalTarjeta = BigDecimal.ZERO;
		BigDecimal totalTransferencia = BigDecimal.ZERO;
		
		for(VentaTienda v : ventasTienda) {
			BigDecimal monto = v.getTotal();
			totalTienda = totalTienda.add(monto);
			if("Efectivo".equalsIgnoreCase(v.getMetodoPago())) totalEfectivo = totalEfectivo.add(monto);
			else if("Tarjeta".equalsIgnoreCase(v.getMetodoPago())) totalTarjeta = totalTarjeta.add(monto);
			else if("Transferencia".equalsIgnoreCase(v.getMetodoPago())) totalTransferencia = totalTransferencia.add(monto);
		}
		
		for(Pago p : pagosMembresias) {
			BigDecimal monto = p.getMonto();
			totalMembresias = totalMembresias.add(monto);
			if("Efectivo".equalsIgnoreCase(p.getMetodoPago())) totalEfectivo = totalEfectivo.add(monto);
			else if("Tarjeta".equalsIgnoreCase(p.getMetodoPago())) totalTarjeta = totalTarjeta.add(monto);
			else if("Transferencia".equalsIgnoreCase(p.getMetodoPago())) totalTransferencia = totalTransferencia.add(monto);
		}
		
		for(Visitas vis : visitas) {
			BigDecimal monto = BigDecimal.valueOf(vis.getMonto());
			totalVisitas = totalVisitas.add(monto);
			if("Efectivo".equalsIgnoreCase(vis.getMetodoPago())) totalEfectivo = totalEfectivo.add(monto);
			else if("Tarjeta".equalsIgnoreCase(vis.getMetodoPago())) totalTarjeta = totalTarjeta.add(monto);
			else if("Transferencia".equalsIgnoreCase(vis.getMetodoPago())) totalTransferencia = totalTransferencia.add(monto);
		}
		
		BigDecimal fondoInicial = BigDecimal.valueOf(turno.getEfectivoInicial());
		totalEfectivo = totalEfectivo.add(fondoInicial);
		
		corte.setTotalTienda(totalTienda);
		corte.setTotalMembresias(totalMembresias);
		corte.setTotalVisitas(totalVisitas);
		
		corte.setTotalEfectivo(totalEfectivo);
		corte.setTotalTarjeta(totalTarjeta);
		corte.setTotalTransferencia(totalTransferencia);
		
		corte.setGranTotal(totalTienda.add(totalMembresias).add(totalVisitas).add(fondoInicial));
		
		return corte;
	}
	
	// 🟢 MÉTODO PÚBLICO: Cerrar el turno sí es una acción que viene de Angular
	public void cerrarTurno(Long turnoId, BigDecimal efectivoFisico) {
		TurnoCaja turno = turnoCajaRepository.findById(turnoId)
				.orElseThrow(() -> new RuntimeException("Turno no encontrado"));
		
		turno.setFechaCierre(LocalDateTime.now());
		turno.setEfectivoFinal(efectivoFisico.doubleValue());
		turno.setEstado("CERRADO");
		
		turnoCajaRepository.save(turno);
	}
}