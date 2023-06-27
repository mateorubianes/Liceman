package com.devforce.devForce.service.impl;

import com.devforce.devForce.model.dto.LicenciaDTO;
import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.dto.SolicitudDTO;
import com.devforce.devForce.model.dto.UsuarioDTO;
import com.devforce.devForce.model.entity.Solicitud;
import com.devforce.devForce.model.entity.Usuario;
import com.devforce.devForce.repository.SolicitudRepository;
import com.devforce.devForce.repository.UsuarioRepository;
import com.devforce.devForce.security.services.UserDetailsImpl;
import com.devforce.devForce.service.SolicitudService;
import com.devforce.devForce.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    @Autowired
    SolicitudRepository solicitudRepository;
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public ResponseEntity<?> crearSolicitud(Solicitud solicitud) {

        UserDetailsImpl usuarioAutenticado = usuarioService.obtenerUsuario();
        Usuario usuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail());

        if(solicitud.getTipo().isEmpty() || solicitud.getArea().isEmpty())
        {
            RespuestaDTO respuestaDTO = new RespuestaDTO();
            respuestaDTO.setOk(false);
            respuestaDTO.setMensaje("Faltan datos");
            respuestaDTO.setContenido(null);

            return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
        }
        if(solicitud.getTipo().equals("UDEMY") || solicitud.getTipo().equals("OTRA PLATAFORMA"))
        {
            if(solicitud.getLink() == null)
            {
                RespuestaDTO respuestaDTO = new RespuestaDTO();
                respuestaDTO.setOk(false);
                respuestaDTO.setMensaje("Para este tipo de solicitud es obligatorio adjuntar un link");
                respuestaDTO.setContenido(null);

                return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
            }
        }

        Solicitud solicitudNueva = new Solicitud();
        solicitudNueva.setTipo(solicitud.getTipo());
        solicitudNueva.setDescripcion(solicitud.getDescripcion());
        solicitudNueva.setArea(solicitud.getArea());
        solicitudNueva.setLink(solicitud.getLink());
        solicitudNueva.setEstado("PENDIENTE-MENTOR");
        solicitudNueva.setUsuario(usuario);

        solicitudRepository.save(solicitudNueva);

        RespuestaDTO respuestaDTO = new RespuestaDTO();
        respuestaDTO.setOk(true);
        respuestaDTO.setMensaje("Solicitud Creada correctamente");
        respuestaDTO.setContenido(solicitudNueva);

        return new ResponseEntity<>(respuestaDTO, HttpStatus.CREATED);
    }

    @Override
    public SolicitudDTO crearSolicitudDTO(Solicitud solicitud) {

        SolicitudDTO solicitudDTO = new SolicitudDTO();
        solicitudDTO.setId(solicitud.getId());
        solicitudDTO.setTipo(solicitud.getTipo());
        solicitudDTO.setDescripcion(solicitud.getDescripcion());
        solicitudDTO.setApruebaMentorID(solicitud.getApruebaMentorID());
        solicitudDTO.setApruebaAdminID(solicitud.getApruebaAdminID());
        solicitudDTO.setEstado(solicitud.getEstado());
        solicitudDTO.setArea(solicitud.getArea());
        solicitudDTO.setLink(solicitud.getLink());
        solicitudDTO.setTiempoSolicitado(solicitud.getTiempoSolicitado());
        solicitudDTO.setUsuario(usuarioService.crearUsuarioDTO(solicitud.getUsuario()));

        return solicitudDTO;
    }

    @Override
    public List<SolicitudDTO> getSolicitudesUsuario() {
        UserDetailsImpl usuarioAutenticado = usuarioService.obtenerUsuario();
        Usuario usuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail());
        List<SolicitudDTO> solicitudes = usuario.getSolicitudes().stream().map(solicitud -> crearSolicitudDTO(solicitud)).collect(Collectors.toList());
        return solicitudes;
    }

    @Override
    public List<SolicitudDTO> getSolicitudesMentor() {
        UserDetailsImpl usuarioAutenticado = usuarioService.obtenerUsuario();
        Usuario usuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail());
        List<SolicitudDTO> solicitudes = this.solicitudRepository.findByArea(usuario.getMentorArea()).stream().filter(solicitud -> !solicitud.getUsuario().equals(usuario))
                .map(solicitud -> crearSolicitudDTO(solicitud)).collect(Collectors.toList());
        return solicitudes;
    }
    @Override
    public List<SolicitudDTO> getSolicitudesAdmin() {
        UserDetailsImpl usuarioAutenticado = usuarioService.obtenerUsuario();
        Usuario usuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail());

        List<SolicitudDTO> solicitudes = this.solicitudRepository.findAll().stream().filter(solicitud -> !solicitud.getUsuario().equals(usuario))
                .map(solicitud -> crearSolicitudDTO(solicitud)).collect(Collectors.toList());
        return solicitudes;
    }

    @Override
    public ResponseEntity<?> actualizarSolicitud(Solicitud solicitud) {

        UserDetailsImpl usuarioAutenticado = usuarioService.obtenerUsuario();
        Usuario usuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail());

        if(solicitud.getEstado().equals("PENDIENTE-MENTOR") || solicitud.getEstado().equals("DEVUELTO-USER"))
        {
            if(solicitud.getTipo().isEmpty() || solicitud.getDescripcion().isEmpty() || solicitud.getArea().isEmpty())
            {
                RespuestaDTO respuestaDTO = new RespuestaDTO();
                respuestaDTO.setOk(false);
                respuestaDTO.setMensaje("Faltan datos");
                respuestaDTO.setContenido(null);

                return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
            }
            if(solicitud.getTipo().equals("UDEMY") || solicitud.getTipo().equals("OTRA PLATAFORMA"))
            {
                if(solicitud.getLink().isEmpty())
                {
                    RespuestaDTO respuestaDTO = new RespuestaDTO();
                    respuestaDTO.setOk(false);
                    respuestaDTO.setMensaje("Para este tipo de solicitud es obligatorio adjuntar un link");
                    respuestaDTO.setContenido(null);

                    return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
                }
            }

            Solicitud solicitudAnterior = this.solicitudRepository.findById(solicitud.getId()).orElse(null);

            if(!usuario.getSolicitudes().contains(solicitudAnterior))
            {
                RespuestaDTO respuestaDTO = new RespuestaDTO();
                respuestaDTO.setOk(false);
                respuestaDTO.setMensaje("La solicitud elegida no pertenece a esta cuenta");
                respuestaDTO.setContenido(null);

                return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
            }

            solicitudAnterior.setTipo(solicitud.getTipo());
            solicitudAnterior.setDescripcion(solicitud.getDescripcion());
            solicitudAnterior.setArea(solicitud.getArea());
            solicitudAnterior.setLink(solicitud.getLink());
            solicitudAnterior.setEstado("PENDIENTE-MENTOR");

            solicitudRepository.save(solicitudAnterior);
        }
        else
        {
            RespuestaDTO respuestaDTO = new RespuestaDTO();
            respuestaDTO.setOk(false);
            respuestaDTO.setMensaje("La solicitud no se encuentra en un estado permitido para actualizar");
            respuestaDTO.setContenido(null);

            return new ResponseEntity<>(respuestaDTO, HttpStatus.BAD_REQUEST);
        }

        RespuestaDTO respuestaDTO = new RespuestaDTO();
        respuestaDTO.setOk(true);
        respuestaDTO.setMensaje("Solicitud Actualizada");
        respuestaDTO.setContenido(null);

        return new ResponseEntity<>(respuestaDTO, HttpStatus.OK);
    }

    @Override
    public List<SolicitudDTO> testSolicitudesDTO() {
        return this.solicitudRepository.findAll().stream().map(solicitud -> crearSolicitudDTO(solicitud)).collect(Collectors.toList());
    }
}
