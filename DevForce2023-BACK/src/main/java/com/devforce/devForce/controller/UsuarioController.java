package com.devforce.devForce.controller;
import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.dto.UsuarioDTO;
import com.devforce.devForce.model.entity.Usuario;
import com.devforce.devForce.repository.UsuarioRepository;
import com.devforce.devForce.service.UsuarioService;
import com.devforce.devForce.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PutMapping("/usuario/updatedatos")
    public ResponseEntity<RespuestaDTO> updateDatos(@RequestBody Usuario usuario) {
        return usuarioService.actualizarDatos(usuario);
    }

    @GetMapping("/test/usuariosDTO")
    public List<UsuarioDTO> testUsuariosDTO() {
        return usuarioService.getUsuariosDTOs();
    }

    @GetMapping("/test/usuario")
    public Usuario testUsuario() {
        return usuarioRepository.findAll().stream().findAny().get();
    }

    @GetMapping("/test/usuarios")
    public List<Usuario> testUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/usuario/obtenerUsuario")
    public UsuarioDTO obtenerUsuario(Authentication authentication){
        return usuarioService.obtenerUsuarioLogueado(authentication);
    }
}
