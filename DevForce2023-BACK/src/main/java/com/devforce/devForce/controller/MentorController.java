package com.devforce.devForce.controller;
import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.entity.Solicitud;
import com.devforce.devForce.service.impl.MentorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mentor")
public class MentorController {
    @Autowired
    MentorServiceImpl mentorServiceImpl;
    @PutMapping("/aceptarSolicitud")
    @PreAuthorize("hasRole('MENTOR')")
    ResponseEntity<RespuestaDTO> aceptarSolicitud (@RequestBody Solicitud solicitud, @RequestParam(required = false) Integer dias) throws Exception{
        return mentorServiceImpl.aceptarSolicitud(solicitud, dias);
    }

    @PutMapping("/rechazarSolicitudPlataforma")
    @PreAuthorize("hasRole('MENTOR')")
    ResponseEntity<RespuestaDTO> rechazarSolicitud (@RequestBody Solicitud solicitud){
        return mentorServiceImpl.rechazarSolicitud(solicitud);
    }

   @PutMapping("/devolverSolicitudPlataforma")
   @PreAuthorize("hasRole('MENTOR')")
    ResponseEntity<RespuestaDTO> devolverSolicitud (@RequestBody Solicitud solicitud){
        return mentorServiceImpl.devolverSolicitud(solicitud);
    }

}