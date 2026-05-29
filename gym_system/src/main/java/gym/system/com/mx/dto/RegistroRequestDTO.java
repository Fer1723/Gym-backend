package gym.system.com.mx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroRequestDTO {
	private String username;
	private String password;
	private String rol;
	private String llaveMaestra;

}
