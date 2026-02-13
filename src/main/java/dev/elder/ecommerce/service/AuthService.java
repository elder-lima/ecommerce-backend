package dev.elder.ecommerce.service;

import dev.elder.ecommerce.dto.request.CreateUser;
import dev.elder.ecommerce.dto.request.LoginRequest;
import dev.elder.ecommerce.dto.response.LoginResponse;
import dev.elder.ecommerce.entity.AuthUser;
import dev.elder.ecommerce.entity.Role;
import dev.elder.ecommerce.entity.Usuario;
import dev.elder.ecommerce.entity.enums.RoleName;
import dev.elder.ecommerce.repository.AuthUserRepository;
import dev.elder.ecommerce.repository.RoleRepository;
import dev.elder.ecommerce.repository.UsuarioRepository;
import dev.elder.ecommerce.service.exceptions.ConflictException;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;
import dev.elder.ecommerce.service.exceptions.UnauthorizedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(AuthUserRepository authUserRepository, UsuarioRepository usuarioRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.authUserRepository = authUserRepository;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional
    public void register(CreateUser user) {

        verificarEmail(user.email());
        verificarCpf(user.cpf());

        Role basicRole = roleRepository.findByNome(RoleName.ROLE_BASIC).orElseThrow(() -> new ResourceNotFoundException("ROLE_BASIC not found"));

        AuthUser authUser = new AuthUser();
        authUser.setEmail(user.email());
        authUser.setSenha(passwordEncoder.encode(user.senha()));
        authUser.getRoles().add(basicRole);
        authUserRepository.save(authUser);

        Usuario usuario = new Usuario();
        usuario.setNome(user.nome());
        usuario.setTelefone(user.telefone());
        usuario.setCpf(user.cpf());
        usuario.setAuthUser(authUser);
        usuarioRepository.save(usuario);

    }

    private void verificarEmail(String email) {
        if (authUserRepository.existsByEmail(email)) {
            throw new ConflictException("Email já cadastrado.");
        }
    }

    private void verificarCpf(String cpf) {
        if (usuarioRepository.existsByCpf(cpf)) {
            throw new ConflictException("CPF já cadastrado.");
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {

        AuthUser user = authUserRepository.findByEmailWithRoles(loginRequest.email()).orElseThrow(() -> new UnauthorizedException("Email ou Senha inválidos!"));

        if (!passwordEncoder.matches(loginRequest.senha(), user.getSenha())) {
            throw new UnauthorizedException("Email ou senha Inválidos!");
        }

        Instant now = Instant.now();
        var expiresIn = 300L; // token válido por 5 minutos

        // Extração das Roles do Usuario: ROLE_ADMIN ou ROLE_BASIC
        String scope = user.getRoles().stream().map(role -> role.getNome().name()).collect(Collectors.joining(" ")); // Collerctors.joining(" "): junta vários valores em uma única String, separados por espaço.

        // Criação das claims do JWT, montando o conteúdo do token.
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("ecommmerce-api")  // iss. Quem emitiu o token
                .issuedAt(now) // // iat, Quando foi criado
                .expiresAt(now.plusSeconds(expiresIn)) //// exp, quando o token expira.
                .subject(user.getAuth_user_id().toString()) // sub. Identidade do usuário, geralmente ID do usuario ou username unico.
                .claim("scope", scope)
                .build();

        // Geração do JWT, claims são assinadas com a chave privada, Gera um JWT no formato: HEADER.PAYLOAD.SIGNATURE
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(token, expiresIn);
    }

}
