package gym.system.com.mx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
	@JsonProperty("username")
	private String username;
	@JsonProperty("password")
	private String password;
}
