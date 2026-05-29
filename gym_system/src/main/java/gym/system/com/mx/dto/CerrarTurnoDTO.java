package gym.system.com.mx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CerrarTurnoDTO {
	private Long turnoId;
	private Double efectivoFinal;
}
