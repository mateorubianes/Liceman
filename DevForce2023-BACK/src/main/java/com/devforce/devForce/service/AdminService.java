package com.devforce.devForce.service;

import com.devforce.devForce.model.dto.LicenciaDTO;
import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.dto.authRequestDTO.RegistroDTO;
import com.devforce.devForce.model.entity.Licencia;
import com.devforce.devForce.model.entity.Solicitud;
import com.devforce.devForce.model.entity.Usuario;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface AdminService {
    public List<LicenciaDTO> getLicenciasDTO();
    public List<LicenciaDTO> getLicenciasDTOasignadas();
    public List<LicenciaDTO> getLicenciasDTOdisponibles();
    public List<Licencia> getLicencias();
    public ResponseEntity<RespuestaDTO> reservarLicencia(Licencia licencia);
    public ResponseEntity<RespuestaDTO> rechazarSolicitudes(Solicitud solicitud);
    public ResponseEntity<RespuestaDTO> revocarLicencia(Licencia licencia);
    public ResponseEntity<RespuestaDTO> crearUsuario(RegistroDTO registroDTO);
    public ResponseEntity<RespuestaDTO> asignarLicencia(Solicitud solicitud);
}
