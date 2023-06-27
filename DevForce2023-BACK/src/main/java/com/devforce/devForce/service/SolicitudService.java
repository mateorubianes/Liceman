package com.devforce.devForce.service;

import com.devforce.devForce.model.dto.SolicitudDTO;
import com.devforce.devForce.model.entity.Solicitud;
import com.devforce.devForce.model.entity.Usuario;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SolicitudService {
    //TODO: REEMPLAZAR EL Usuario usuario POR AUTHENTICATION CUANDO LE INTEGREMOS EL SECURITY
    public ResponseEntity<?> crearSolicitud(Solicitud solicitud);
    public SolicitudDTO crearSolicitudDTO(Solicitud solicitud);
    public List<SolicitudDTO> getSolicitudesUsuario();
    public List<SolicitudDTO> getSolicitudesMentor();
    public List<SolicitudDTO> getSolicitudesAdmin();
    public ResponseEntity<?> actualizarSolicitud(Solicitud solicitud);

    public List<SolicitudDTO> testSolicitudesDTO();
}