package dev.elder.ecommerce.service;

import dev.elder.ecommerce.dto.request.UsuarioUpdateRequest;
import dev.elder.ecommerce.dto.response.UsuarioDetailResponse;
import dev.elder.ecommerce.entity.AuthUser;
import dev.elder.ecommerce.entity.Usuario;
import dev.elder.ecommerce.repository.UsuarioRepository;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UsuarioService usuarioService;

    @Test
    @DisplayName("Deve retornar Usuario pelo Id com Sucesso")
    void deveRetornarUsuarioPeloIdComSucesso() {

        UUID id = UUID.randomUUID();

        AuthUser authUser = new AuthUser();
        authUser.setAuth_user_id(id);
        authUser.setEmail("elder@gmail.com");

        Usuario usuario = new Usuario();
        usuario.setUserId(id);
        usuario.setNome("Elder");
        usuario.setTelefone("99999");
        usuario.setCpf("414141");
        usuario.setAuthUser(authUser);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        UsuarioDetailResponse usuarioDetailResponse = usuarioService.findById(id);

        assertNotNull(usuarioDetailResponse);
        assertEquals(id, usuarioDetailResponse.id());
        verify(usuarioRepository).findById(id);
        assertEquals("Elder", usuarioDetailResponse.nome());
        assertEquals("elder@gmail.com", usuarioDetailResponse.email());
    }

    @Test
    @DisplayName("Deve lançar exceção quando Id é do usuario é inválido")
    void deveLancarExcecaoQuandoIdInvalido() {

        UUID id = UUID.randomUUID();

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.findById(id));

        verify(usuarioRepository).findById(id);
    }

    @Test
    @DisplayName("Deve Atualizar Usuario com Sucesso.")
    void deveAtualizarUsuarioComSucesso() {

        UUID id = UUID.randomUUID();

        AuthUser authUser = new AuthUser();
        authUser.setAuth_user_id(id);
        authUser.setEmail("antigoemail@gmail.com");

        Usuario usuario = new Usuario();
        usuario.setUserId(authUser.getAuth_user_id());
        usuario.setNome("AntigoNome");
        usuario.setAuthUser(authUser);

        UsuarioUpdateRequest dto = new UsuarioUpdateRequest(
            "Elder",
                null,
                null,
                "elder@gmail.com",
                null
        );

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        UsuarioDetailResponse result = usuarioService.update(id, dto);

        assertNotNull(result);
        assertEquals("Elder", result.nome());
        assertEquals("elder@gmail.com", result.email());
        verify(usuarioRepository).save(any());

    }
}