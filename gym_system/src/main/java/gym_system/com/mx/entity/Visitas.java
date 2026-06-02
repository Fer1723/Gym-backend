package gym_system.com.mx.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "visitas")
@Data
public class Visitas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, length = 15)
	private String pin;
	
	private Double monto;
	private LocalDateTime fechaCreacion;
	private Boolean usado = false;
	@Column(name = "metodo_pago", length = 30)
	private String metodoPago;
	
	@PrePersist
	protected void onCreate() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "turno_caja_id")
	private TurnoCaja turnoCaja;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pago", nullable = false)
	private FormaPago formaPago;

	
	
}
