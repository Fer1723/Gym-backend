package gym.system.com.mx.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteCajaGlobalDTO {
	private Double totalGlobalEfectivo;
	private Double totalGlobalTarjeta; 
	private Double totalGlobalTransferencia;
	private Double granTotalCaja;
	
	private List<Map<String, Object>> desglosePorRecepcionista;
}
