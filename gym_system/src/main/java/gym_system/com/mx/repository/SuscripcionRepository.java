package gym_system.com.mx.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gym_system.com.mx.entity.Socio;
import gym_system.com.mx.entity.Suscripcion;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Integer> {
	List<Suscripcion> findByEstado(String estado);
	List<Suscripcion> findBySocioAndEstado(Socio socio, String estado);
	List<Suscripcion> findBySocioOrderByFechaFinDesc(Socio socio);
	List<Suscripcion> findByEstadoIgnoreCaseAndFechaFinBefore(String estado, LocalDate fecha);
	List<Suscripcion> findBySocioIdSocio(Integer idSocio);
}
