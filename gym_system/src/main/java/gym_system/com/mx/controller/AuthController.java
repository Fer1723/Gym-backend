package gym_system.com.mx.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gym.system.com.mx.dto.AuthResponseDTO;
import gym.system.com.mx.dto.LoginRequestDTO;
import gym.system.com.mx.dto.RegistroRequestDTO;
import gym_system.com.mx.config.CorsConfig;
import gym_system.com.mx.config.JwtUtil;
import gym_system.com.mx.entity.Usuario;
import gym_system.com.mx.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CorsConfig corsConfig;
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private final String LLAVE_MAESTRA_SUPREMA = "FactoryGymSuperAdminSecretKey2026";
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    AuthController(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }
	
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequestDTO request) {
        try {
            // 1. Validar si el usuario ya existe
            if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre de usuario ya está registrado."));
            }

            // 2. NORMALIZAMOS EL ROL: Nos aseguramos de que siempre empiece con "ROLE_"
            String rolSolicitado = request.getRol().toUpperCase();
            if (!rolSolicitado.startsWith("ROLE_")) {
                rolSolicitado = "ROLE_" + rolSolicitado;
            }

            // 3. FILTRO DE SEGURIDAD CRÍTICO: Ahora sí evaluamos el rol normalizado
            if ("ROLE_ADMIN".equals(rolSolicitado)) {
                if (request.getLlaveMaestra() == null || !request.getLlaveMaestra().equals(LLAVE_MAESTRA_SUPREMA)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Llave maestra inválida. No tienes permisos para crear un Administrador."));
                }
            }

            // 4. Crear el nuevo usuario con la contraseña encriptada
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(request.getUsername());
            
            String passwordEncriptada = passwordEncoder.encode(request.getPassword());
            nuevoUsuario.setPassword(passwordEncriptada);
            
            // Guardamos con el rol ya normalizado
            nuevoUsuario.setRol(rolSolicitado);

            usuarioRepository.save(nuevoUsuario);

            return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado exitosamente con el rol: " + rolSolicitado));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en el servidor: " + e.getMessage()));
        }
    }
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO request){
		try {
			Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());
			
			if(usuarioOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Usuario no encontrado"));
			}
			
			Usuario usuario = usuarioOpt.get();
			
			if(!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Contraseña incorrecta"));
			}
			
			String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRol());
			
			return ResponseEntity.ok(new AuthResponseDTO(token, usuario.getRol(), usuario.getId()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Error en el servidor"));

		}
	}
}
