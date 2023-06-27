package com.devforce.devForce.service.impl;

import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.dto.UsuarioDTO;
import com.devforce.devForce.model.dto.authRequestDTO.LoginRequest;
import com.devforce.devForce.model.entity.Usuario;
import com.devforce.devForce.repository.UsuarioRepository;
import com.devforce.devForce.security.jwtUtils.JwtUtils;
import com.devforce.devForce.security.services.UserDetailsImpl;
import com.devforce.devForce.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ResponseEntity<RespuestaDTO> actualizarDatos(Usuario usuario) {

        UserDetailsImpl usuarioAutenticado = obtenerUsuario();
        Usuario usuarioLogueado = usuarioRepository.findByEmail(usuarioAutenticado.getEmail());


        usuarioLogueado.setPassword(encoder.encode(usuario.getPassword()));
        usuarioLogueado.setPhone(usuario.getPhone());
        usuarioLogueado.setHasTeams(usuario.getHasTeams());

        if(usuarioRepository.existsByEmail(usuario.getEmail()))
        {
            if(!usuarioLogueado.getEmail().equals(usuario.getEmail()))
            {RespuestaDTO respuestaDTO = new RespuestaDTO();
                respuestaDTO.setOk(false);
                respuestaDTO.setMensaje("Ese email ya se encuentra registrado");
                respuestaDTO.setContenido(null);

                return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
            }
        }

        usuarioLogueado.setEmail(usuario.getEmail());
        usuarioRepository.save(usuarioLogueado);

        RespuestaDTO respuestaDTO = new RespuestaDTO(true,"Datos actualizados",usuarioLogueado);

        return new ResponseEntity<>(respuestaDTO, HttpStatus.OK);
    }

    @Override
    public UsuarioDTO crearUsuarioDTO (Usuario usuario) {

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setPhone(usuario.getPhone());
        usuarioDTO.setHasTeams(usuario.getHasTeams());
        usuarioDTO.setMentorArea(usuario.getMentorArea());
        usuarioDTO.setRoles(usuario.getRoles());
        return usuarioDTO;
    }

    public List<UsuarioDTO> getUsuariosDTOs(){
        List<UsuarioDTO> usuarios = usuarioRepository.findAll().stream().map(usuario -> crearUsuarioDTO(usuario)).collect(Collectors.toList());
        return usuarios;
    }

    @Override
    public UserDetailsImpl obtenerUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetail = (UserDetailsImpl) auth.getPrincipal();
        return userDetail;
    }

    @Override
    public ResponseEntity<RespuestaDTO> login(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new RespuestaDTO(true, "logueado", userDetails));
    }

    @Override
    public UsuarioDTO obtenerUsuarioLogueado(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findById(userDetails.getId()).get();

        return crearUsuarioDTO(usuario);
    }

}
