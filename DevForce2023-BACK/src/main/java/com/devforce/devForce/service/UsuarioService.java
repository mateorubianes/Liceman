package com.devforce.devForce.service;

import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.dto.UsuarioDTO;
import com.devforce.devForce.model.dto.authRequestDTO.LoginRequest;
import com.devforce.devForce.model.entity.Usuario;
import com.devforce.devForce.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface UsuarioService {

    public ResponseEntity<RespuestaDTO> actualizarDatos(Usuario usuario);
    public UsuarioDTO crearUsuarioDTO (Usuario usuario);
    public UserDetailsImpl obtenerUsuario();
    public ResponseEntity<RespuestaDTO> login(@Valid @RequestBody LoginRequest loginRequest);
    public List<UsuarioDTO> getUsuariosDTOs();

    public UsuarioDTO obtenerUsuarioLogueado(Authentication authentication);

}
