package com.devforce.devForce.service.impl;

import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.entity.Solicitud;
import com.devforce.devForce.model.entity.Usuario;
import com.devforce.devForce.repository.SolicitudRepository;
import com.devforce.devForce.repository.UsuarioRepository;
import com.devforce.devForce.security.services.UserDetailsImpl;
import com.devforce.devForce.service.MentorService;
import com.devforce.devForce.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MentorServiceImpl implements MentorService {

    @Autowired
    SolicitudRepository solicitudRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioService usuarioService;

    public RespuestaDTO verificarEstado(Solicitud solicitud) {
        RespuestaDTO respuestaDTO =  new RespuestaDTO();
        respuestaDTO.setOk(false);

        UserDetailsImpl userDetails = usuarioService.obtenerUsuario();
        Usuario mentor = usuarioRepository.findByUsername(userDetails.getUsername()).orElse(null);

        Solicitud solicitudUsuario = solicitudRepository.findById(solicitud.getId()).orElse(null);

        if (solicitudUsuario == null){
            respuestaDTO.setMensaje("Las solicitud no existe");
            respuestaDTO.setContenido(solicitud);
            return respuestaDTO;
        }

        if (!(solicitudUsuario.getEstado().equals("PENDIENTE-MENTOR"))){
            respuestaDTO.setMensaje("Las solicitud no se encuentra en PENDIENTE-MENTOR");
            respuestaDTO.setContenido(solicitud);
            return respuestaDTO;
        }

        assert mentor != null;
        if (!solicitudUsuario.getArea().equals(mentor.getMentorArea())){
            respuestaDTO.setMensaje("La solicitud no pertenece al area " + mentor.getMentorArea());
            respuestaDTO.setContenido(solicitud);
            return respuestaDTO;
        }

        if (solicitudUsuario.getUsuario().getId() == mentor.getId()) {
            respuestaDTO.setMensaje("La solicitud no puede estar dirigida al mentor " + mentor.getUsername() + "debido a que fue emitida por el mismo mentor.");
            respuestaDTO.setContenido(solicitud);
            return respuestaDTO;
        }

        solicitudUsuario.setApruebaMentorID((int) mentor.getId());
        solicitudRepository.save(solicitudUsuario);
        respuestaDTO.setOk(true);
        respuestaDTO.setMensaje("La solicitud se encuentra en estado: " + solicitudUsuario.getEstado());
        respuestaDTO.setContenido(solicitudUsuario);
        return respuestaDTO;
    }

    @Override
    public ResponseEntity<RespuestaDTO> aceptarSolicitud(Solicitud solicitud, Integer dias) {

        RespuestaDTO respuestaDTO = verificarEstado(solicitud);

        Solicitud solicitudUsuario = solicitudRepository.findById(solicitud.getId()).orElse(null);

        if((solicitud.getTipo().equals("UDEMY") || solicitud.getTipo().equals("OTRA PLATAFORMA")) && dias < 1){
            respuestaDTO.setOk(false);
            respuestaDTO.setMensaje("Cantidad de dias no permitida");
            respuestaDTO.setContenido(null);
            return new ResponseEntity<>(respuestaDTO, HttpStatus.FORBIDDEN);
        }

        if (!respuestaDTO.isOk()){
            return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
        } else {
            if (solicitudUsuario.getTipo().equals("OTROS") || solicitudUsuario.getTipo().equals("ASESORAMIENTO")){
                solicitudUsuario.setEstado("ACEPTADO");
            }
            if (solicitudUsuario.getTipo().equals("UDEMY") || solicitudUsuario.getTipo().equals("OTRA PLATAFORMA")){
                solicitudUsuario.setEstado("PENDIENTE-ADMIN");
                solicitudUsuario.setTiempoSolicitado(dias);
            }
            solicitudRepository.save(solicitudUsuario);
            respuestaDTO.setMensaje("La solicitud se encuentra en estado: " + solicitudUsuario.getEstado());
            return new ResponseEntity<>(respuestaDTO, HttpStatus.ACCEPTED);
        }
    }

    @Override
    public ResponseEntity<RespuestaDTO> rechazarSolicitud(Solicitud solicitud) {

        RespuestaDTO respuestaDTO = verificarEstado(solicitud);

        Solicitud solicitudUsuario = solicitudRepository.findById(solicitud.getId()).orElse(null);

        if (!respuestaDTO.isOk()){
            return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
        } else {
            solicitudUsuario.setEstado("DENEGADO");
            solicitudRepository.save(solicitudUsuario);
            respuestaDTO.setMensaje("La solicitud se encuentra en estado: " + solicitudUsuario.getEstado());
            return new ResponseEntity<>(respuestaDTO, HttpStatus.ACCEPTED);
        }
    }

    @Override
    public ResponseEntity<RespuestaDTO> devolverSolicitud(Solicitud solicitud) {

        RespuestaDTO respuestaDTO = verificarEstado(solicitud);

        Solicitud solicitudUsuario = solicitudRepository.findById(solicitud.getId()).orElse(null);

        if (!respuestaDTO.isOk()){
            return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
        } else {
            solicitudUsuario.setEstado("DEVUELTA-USUARIO");
            solicitudRepository.save(solicitudUsuario);
            respuestaDTO.setMensaje("La solicitud se encuentra en estado: " + solicitudUsuario.getEstado());
            return new ResponseEntity<>(respuestaDTO, HttpStatus.ACCEPTED);
        }
    }
}
