package gym_system.com.mx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gym_system.com.mx.entity.CatalogoMembresia;

@Repository
public interface MembresiaRepository extends JpaRepository<CatalogoMembresia, Integer>{
	
}
