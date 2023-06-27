package com.devforce.devForce.controller;
import com.devforce.devForce.model.dto.LicenciaDTO;
import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.dto.SolicitudDTO;
import com.devforce.devForce.model.dto.authRequestDTO.RegistroDTO;
import com.devforce.devForce.model.entity.Licencia;
import com.devforce.devForce.model.entity.Solicitud;
import com.devforce.devForce.model.entity.Usuario;
import com.devforce.devForce.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminServiceImpl adminServiceImpl;
    @PostMapping("/registrarUsuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespuestaDTO> crearUsuario(@RequestBody RegistroDTO registroDTO) {
        return adminServiceImpl.crearUsuario(registroDTO);
    }

    @PostMapping("/asignarLicencia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespuestaDTO> asignarLicencia(@RequestBody Solicitud solicitud){
        return adminServiceImpl.asignarLicencia(solicitud);
    }

    @GetMapping("/licencias")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LicenciaDTO> obtenerLicencias (){
        return adminServiceImpl.getLicenciasDTO();
    }

    @PutMapping("/revocarLicencia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespuestaDTO> revocarLic(@RequestBody Licencia licencia) {
        return adminServiceImpl.revocarLicencia(licencia);
    }

    @PutMapping("/reservarLicencia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespuestaDTO> reservarLic(@RequestBody Licencia licencia) {
        return adminServiceImpl.reservarLicencia(licencia);
    }

    @PostMapping("/rechazarSolicitudAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespuestaDTO> rechaSoliAdmin(@RequestBody Solicitud solicitud) {
        return adminServiceImpl.rechazarSolicitudes(solicitud);
    }
}
