package gym_system.com.mx.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "licencias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Licencia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "hardware_id", unique = true, nullable = false)
	private String hardwareId;
	
	private String descripcionEquipo;
	private boolean activa;
	private LocalDateTime fechaRegistro = LocalDateTime.now(); 
	 
}
