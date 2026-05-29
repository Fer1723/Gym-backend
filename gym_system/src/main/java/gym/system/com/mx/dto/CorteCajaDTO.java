package gym.system.com.mx.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorteCajaDTO {
	
	private BigDecimal totalTienda = BigDecimal.ZERO;
	private BigDecimal totalMembresias = BigDecimal.ZERO;
	private BigDecimal totalVisitas = BigDecimal.ZERO;
	
	private BigDecimal totalEfectivo = BigDecimal.ZERO;
	private BigDecimal totalTarjeta = BigDecimal.ZERO;
	private BigDecimal totalTransferencia = BigDecimal.ZERO;
	
	private BigDecimal granTotal = BigDecimal.ZERO;
}
