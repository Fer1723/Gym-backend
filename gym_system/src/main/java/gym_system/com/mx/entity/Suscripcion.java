package gym_system.com.mx.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suscripciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suscripcion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_suscripcion")
	private Integer idSuscripcion;
	
	@ManyToOne
	@JoinColumn(name = "id_socio", nullable = false)
	private Socio socio;
	
	@ManyToOne
	@JoinColumn(name = "id_membresia", nullable = false)
	private CatalogoMembresia membresia;
	
	@Column(name = "fecha_inicio", nullable = false)
	private LocalDate fechaInicio;
	
	@Column(name = "fecha_fin", nullable = false)
	private LocalDate fechaFin;
	
	@Column(name = "estado", length = 20)
	private String estado;
	
	@Column(name = "monto_cobrado")
	private BigDecimal montoCobrado;

}
