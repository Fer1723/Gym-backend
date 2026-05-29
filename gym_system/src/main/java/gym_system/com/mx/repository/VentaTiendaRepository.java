package gym_system.com.mx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gym_system.com.mx.entity.VentaTienda;

@Repository
public interface VentaTiendaRepository extends JpaRepository<VentaTienda, Integer>{
	List<VentaTienda> findByFechaVentaBetween(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);
	List<VentaTienda> findByTurnoCaja_Id(Long turnoId);
}
