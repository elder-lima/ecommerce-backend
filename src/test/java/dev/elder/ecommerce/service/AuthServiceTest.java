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
import dev.elder.ecommerce.service.exceptions.UnauthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    AuthUserRepository authUserRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    JwtEncoder jwtEncoder;

    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("Deve Registrar usuario com sucesso")
    void deveRegistrarUusarioComSucesso() {

        CreateUser createUser = new CreateUser(
                "elder@gmail.com",
                "elder",
                "Elder",
                "99999",
                "56484253846"
        );

        Role basicRole = new Role();
        basicRole.setNome(RoleName.ROLE_BASIC);

        when(authUserRepository.existsByEmail(createUser.email())).thenReturn(false);
        when(usuarioRepository.existsByCpf(createUser.cpf())).thenReturn(false);
        when(roleRepository.findByNome(RoleName.ROLE_BASIC)).thenReturn(Optional.of(basicRole));
        when(passwordEncoder.encode(createUser.senha())).thenReturn("elder");

        authService.register(createUser);

        verify(authUserRepository).save(any(AuthUser.class));
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Email Duplicado deve lançar Exceção")
    void emailDuplicadoAoRegistrarUsuarioDeveLancarExcecao() {

        CreateUser createUser = new CreateUser(
                "elder@gmail.com",
                "elder",
                "Elder",
                "9999",
                "56484253846"
        );

        when(authUserRepository.existsByEmail(createUser.email())).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(createUser));

        verify(authUserRepository).existsByEmail(createUser.email());
        verify(authUserRepository, never()).save(any());
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("CPF Duplicado deve lançar Exceção")
    void cpfDuplicadoAoRegistrarUsuarioDeveLancarExcecao() {

        CreateUser createUser = new CreateUser(
                "elder@gmail.com",
                "elder",
                "Elder",
                "9999",
                "56484253846"
        );

        when(authUserRepository.existsByEmail(createUser.email())).thenReturn(false);
        when(usuarioRepository.existsByCpf(createUser.cpf())).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(createUser));

        verify(authUserRepository, never()).save(any());
        verify(usuarioRepository).existsByCpf(createUser.cpf());
        verifyNoInteractions(roleRepository);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve logar usuario com Sucesso")
    void deveLogarUsuarioComSucesso() {

        LoginRequest login = new LoginRequest(
                "elder@gmail.com",
                "elder"
        );

        Role role = new Role();
        role.setNome(RoleName.ROLE_BASIC);

        AuthUser authUser = new AuthUser();
        authUser.setAuth_user_id(UUID.randomUUID());
        authUser.setEmail(login.email());
        authUser.setSenha(passwordEncoder.encode(login.senha()));
        authUser.getRoles().add(role);

        when(authUserRepository.findByEmailWithRoles(login.email())).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches(login.senha(), authUser.getSenha())).thenReturn(true);

        Jwt jwtMock = mock(Jwt.class);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwtMock);
        when(jwtMock.getTokenValue()).thenReturn("token-fake");

        LoginResponse response = authService.login(login);

        assertNotNull(response);
        assertEquals("token-fake", response.accessToken());
        verify(authUserRepository).findByEmailWithRoles(authUser.getEmail());
    }

    @Test
    @DisplayName("Deve Lançar Exceção quando senha é Inválida")
    void deveLancarExcecaoQuandoSenhaInvalida() {

        LoginRequest login = new LoginRequest(
                "elder@gmail.com",
                "elder"
        );

        AuthUser authUser = new AuthUser();
        authUser.setSenha("senhaErrada");

        when(authUserRepository.findByEmailWithRoles(login.email())).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches(login.senha(), authUser.getSenha())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(login));
    }
}