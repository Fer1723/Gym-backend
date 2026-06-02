package gym_system.com.mx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gym_system.com.mx.entity.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
	List<Pago> findByFechaPagoBetween(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);
	List<Pago> findByTurnoCaja_Id(Long turnoId);
}
