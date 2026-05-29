package gym_system.com.mx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gym_system.com.mx.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	@Query("SELECT p FROM  Producto p WHERE p.stockActual <= p.stockMinimo AND p.estado = true")
	List<Producto> buscarProductosCriticos();

}
