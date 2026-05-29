package gym_system.com.mx.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gym_system.com.mx.entity.Licencia;

public interface LicenciaRepository extends JpaRepository<Licencia, Long> {
	Optional<Licencia> findByHardwareIdAndActivaTrue(String hardwareId);

}
