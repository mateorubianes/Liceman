package com.devforce.devForce.controller;
import com.devforce.devForce.model.dto.SolicitudDTO;
import com.devforce.devForce.model.dto.UsuarioDTO;
import com.devforce.devForce.model.entity.Solicitud;
import com.devforce.devForce.model.entity.Usuario;
import com.devforce.devForce.repository.SolicitudRepository;
import com.devforce.devForce.repository.UsuarioRepository;
import com.devforce.devForce.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SolicitudController {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    SolicitudRepository solicitudRepository;
    @Autowired
    SolicitudService solicitudService;

    @GetMapping("/solicitudesusuario")
    @PreAuthorize("hasRole('USUARIO')")
    public List<SolicitudDTO> solicitudesUsuario() {
        return solicitudService.getSolicitudesUsuario();
    }

    @GetMapping("/solicitudesmentor")
    @PreAuthorize("hasRole('MENTOR')")
    public List<SolicitudDTO> solicitudesMentor() {
        return solicitudService.getSolicitudesMentor();
    }

    @GetMapping("/solicitudesadmin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SolicitudDTO> solicitudesAdmin() {
        return solicitudService.getSolicitudesAdmin();
    }

    @PostMapping("/nuevaSolicitud")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<?> crearSolicitud(@Valid @RequestBody Solicitud solicitud) {
        return solicitudService.crearSolicitud(solicitud);
    }

    @PutMapping("/updateSolicitud")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<?> actualizarSolicitud(@Valid @RequestBody Solicitud solicitud) {
        return solicitudService.actualizarSolicitud(solicitud);
    }

    @GetMapping("/test/solicitudes")
    public List<Solicitud> testSolicitudes() {
        return solicitudRepository.findAll();
    }

    @GetMapping("/test/solicitudesDTO")
    public List<SolicitudDTO> testSolicitudesDTO() {
        return solicitudService.testSolicitudesDTO();
    }
}
