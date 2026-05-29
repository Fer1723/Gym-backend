package gym_system.com.mx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gym_system.com.mx.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer>{

}
