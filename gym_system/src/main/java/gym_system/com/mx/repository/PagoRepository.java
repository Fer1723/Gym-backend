package gym_system.com.mx.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gym_system.com.mx.entity.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
	List<Pago> findByFechaPagoBetween(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);
	List<Pago> findByTurnoCaja_Id(Long turnoId);
}
