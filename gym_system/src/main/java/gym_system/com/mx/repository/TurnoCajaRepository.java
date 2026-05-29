package gym_system.com.mx.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gym_system.com.mx.entity.TurnoCaja;

@Repository
public interface TurnoCajaRepository extends JpaRepository<TurnoCaja, Long>{
	Optional<TurnoCaja> findByUsuarioIdAndEstado(Integer usuarioId, String estado);
	Optional<TurnoCaja> findTopByOrderByFechaAperturaDesc();
	List<TurnoCaja> findByObservacionesContaining(String palabraClave);

}
