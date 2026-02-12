package dev.elder.ecommerce.service;

import dev.elder.ecommerce.dto.request.UsuarioUpdateRequest;
import dev.elder.ecommerce.dto.response.UsuarioDetailResponse;
import dev.elder.ecommerce.entity.Usuario;
import dev.elder.ecommerce.repository.UsuarioRepository;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UsuarioDetailResponse findById(UUID id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDetailResponse> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(usuario -> toResponse(usuario)).collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDetailResponse update(UUID id, UsuarioUpdateRequest dto) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        applyUpdates(usuario, dto);
        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    public void applyUpdates(Usuario usuario, UsuarioUpdateRequest dto) {
        if (dto.nome() != null) {
            usuario.setNome(dto.nome());
        }
        if (dto.telefone() != null) {
            usuario.setTelefone(dto.telefone());
        }
        if (dto.cpf() != null) {
            usuario.setCpf(dto.cpf());
        }
        if (dto.email() != null) {
            usuario.getAuthUser().setEmail(dto.email());
        }
        if (dto.senha() != null) {
            usuario.getAuthUser().setSenha(passwordEncoder.encode(dto.senha()));
        }
    }

    public UsuarioDetailResponse toResponse(Usuario usuario) {
        return new UsuarioDetailResponse(
                usuario.getUserId(),
                usuario.getAuthUser().getEmail(),
                usuario.getNome(),
                usuario.getTelefone(),
                usuario.getCpf()
        );
    }

}
