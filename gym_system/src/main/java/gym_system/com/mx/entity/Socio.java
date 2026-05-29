package gym_system.com.mx.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "socios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Socio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_socio")
	private Integer idSocio;
	
	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;
	
	@Column(name = "apellido", nullable = false, length = 100)
	private String apellido;
	
	@Pattern(regexp = "^\\d{10}$", message = "El telefono debe tener exactamente 10 numeros")
	@Column(name = "telefono", length = 20)
	private String telefono;
	
	@Column(name = "foto_base64", columnDefinition = "TEXT")
	private String fotoBase64;
	
	@Column(name = "huella_template", columnDefinition = "TEXT")
	private String huellaTemplate;
	
	@Column(name = "estado", columnDefinition = "TINYINT(1)")
	private Boolean estado;
	
	@Column(name = "fecha_registro", updatable = false)
	private LocalDateTime fechaRegistro;
	
	
	@PrePersist
	protected void OnCreate() {
		if(this.fechaRegistro == null) {
			this.fechaRegistro = LocalDateTime.now();
		}
		if(this.estado == null) {
			this.estado = true;
		}
	}
	

}
