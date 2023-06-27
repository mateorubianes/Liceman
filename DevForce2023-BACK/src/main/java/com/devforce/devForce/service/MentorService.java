package com.devforce.devForce.service;

import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.entity.Solicitud;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

public interface MentorService {

    ResponseEntity<RespuestaDTO>  aceptarSolicitud (Solicitud solicitud, Integer dias);
    ResponseEntity<RespuestaDTO>  rechazarSolicitud (Solicitud solicitud);
    ResponseEntity<RespuestaDTO> devolverSolicitud (Solicitud solicitud);

}
