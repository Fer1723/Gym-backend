package gym_system.com.mx.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gym_system.com.mx.entity.Socio;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Integer>{
	List<Socio> findByEstadoTrue();
	Optional<Socio> findByHuellaTemplate(String huellaTemplate);
}
