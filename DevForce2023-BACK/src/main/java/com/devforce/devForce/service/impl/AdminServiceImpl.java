package com.devforce.devForce.service.impl;

import com.devforce.devForce.model.dto.LicenciaDTO;
import com.devforce.devForce.model.dto.RespuestaDTO;
import com.devforce.devForce.model.dto.authRequestDTO.RegistroDTO;
import com.devforce.devForce.model.entity.Licencia;
import com.devforce.devForce.model.entity.Role;
import com.devforce.devForce.model.entity.Solicitud;
import com.devforce.devForce.model.entity.Usuario;
import com.devforce.devForce.model.enums.ERole;
import com.devforce.devForce.repository.LicenciaRepository;
import com.devforce.devForce.repository.RoleRepository;
import com.devforce.devForce.repository.SolicitudRepository;
import com.devforce.devForce.repository.UsuarioRepository;
import com.devforce.devForce.security.jwtUtils.JwtUtils;
import com.devforce.devForce.security.services.UserDetailsImpl;
import com.devforce.devForce.service.AdminService;
import com.devforce.devForce.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    LicenciaRepository licenciaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    SolicitudRepository solicitudRepository;

    @Autowired
    SolicitudServiceImpl solicitudService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UsuarioService usuarioService;

    @Override
    public List<Licencia> getLicencias() {
        return licenciaRepository.findAll();
    }

    @Override
    public List<LicenciaDTO> getLicenciasDTO() {
        return licenciaRepository.findAll().stream().map(licencia -> createLicenciaDTO(licencia)).collect(Collectors.toList());
    }

    @Override
    public List<LicenciaDTO> getLicenciasDTOasignadas(){
     return  licenciaRepository.findByEstado("ASIGNADA").stream().map(licencia -> createLicenciaDTO(licencia)).collect(Collectors.toList());
    }

    @Override
    public List<LicenciaDTO> getLicenciasDTOdisponibles(){
        return  licenciaRepository.findByEstado("DISPONIBLE").stream().map(licencia -> createLicenciaDTO(licencia)).collect(Collectors.toList());
    }


    private LicenciaDTO createLicenciaDTO(Licencia licencia) {
        LicenciaDTO licenciaDTO = new LicenciaDTO(licencia.getId(), licencia.getSerie(), licencia.getEstado(),
                licencia.getVencimiento(), licencia.getPlataforma(), licencia.getSolicitudes().stream()
                .map(solicitud -> solicitudService.crearSolicitudDTO(solicitud)).collect(Collectors.toList()));
        return licenciaDTO;
    }

    @Override
    public ResponseEntity<RespuestaDTO> crearUsuario(RegistroDTO registroDTO) {

        RespuestaDTO respuestaDTO;
        if (usuarioRepository.findByUsername(registroDTO.getUsername()).orElse(null) != null)
        {
            respuestaDTO = new RespuestaDTO(false, "El usuario ya existe", null);
            return new ResponseEntity<>(respuestaDTO, HttpStatus.FORBIDDEN);
        }

        if (usuarioRepository.findByEmail(registroDTO.getEmail()) != null)
        {
            respuestaDTO = new RespuestaDTO(false, "El Mail ya esta en uso", null);
            return new ResponseEntity<>(respuestaDTO, HttpStatus.FORBIDDEN);
        }

        Usuario usuario = new Usuario(registroDTO.getNombre(), registroDTO.getApellido(), registroDTO.getUsername(),
                registroDTO.getEmail(),passwordEncoder.encode(registroDTO.getPassword()), registroDTO.getPhone(),
                registroDTO.getHasTeams(),registroDTO.getMentorArea());

        Set<String> rolesUsuarioString = registroDTO.getRole();
        Set<Role> rolesUsuario = new HashSet<>();

        if(rolesUsuarioString.isEmpty()){
            Role userRoleMentor = roleRepository.findByName(ERole.ROLE_USUARIO).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            rolesUsuario.add(userRoleMentor);
        } else {

            if(rolesUsuarioString.contains("mentor")) {
                if (registroDTO.getMentorArea() == null) {
                    respuestaDTO = new RespuestaDTO(false, "Falta completar el area del mentor", null);
                    return new ResponseEntity<>(respuestaDTO, HttpStatus.FORBIDDEN);
                }
            }

            rolesUsuarioString.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        rolesUsuario.add(adminRole);
                        break;
                    case "mentor":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MENTOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        rolesUsuario.add(modRole);
                        Role userRoleMentor = roleRepository.findByName(ERole.ROLE_USUARIO).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        rolesUsuario.add(userRoleMentor);
                        break;
                    case "usuario":
                        Role defaultRole = roleRepository.findByName(ERole.ROLE_USUARIO).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        rolesUsuario.add(defaultRole);
                }
            });

            if(rolesUsuario.isEmpty()){
                respuestaDTO = new RespuestaDTO(false, "No se ingreso ningun rol valido.", null);
                return new ResponseEntity<>(respuestaDTO, HttpStatus.FORBIDDEN);
            }
        }
        usuario.setRoles(rolesUsuario);
        usuarioRepository.save(usuario);
        respuestaDTO = new RespuestaDTO(true, "El usuario ha sido creado correctamente", usuario);
        return new ResponseEntity<>(respuestaDTO, HttpStatus.CREATED);
    }

    //Prueba
    @Override
    public ResponseEntity<RespuestaDTO> asignarLicencia(Solicitud solicitud) {
        RespuestaDTO respuestaDTO;
        Solicitud solicitudRecibida = solicitudRepository.findById(solicitud.getId()).orElse(null);

        if(solicitudRecibida == null){
            respuestaDTO = new RespuestaDTO(false, "la Solicitud Recibida no existe", null);
            return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.FORBIDDEN);
        }

        if (solicitudRecibida.getEstado().equals("PENDIENTE-ADMIN") == false) {
            respuestaDTO = new RespuestaDTO(false, "LICENCIA DENEGADA. No fue aprobada", null);
            return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.FORBIDDEN);
        }

        // Esta verificacion se podra utilizar cuando se hayan asignado los ID de los mentores a las solicitudes:
        /*
        if (solicitudRecibida.getArea().equals(usuarioRepository.findById(solicitudRecibida.getApruebaMentorID()).getMentorArea()) == false) {
            respuestaDTO = new RespuestaDTO(false, "lICENCIA DENEGADA. El mentor no pertenece al area de la licencia", null);
            return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.FORBIDDEN);
        }
         */
        int solicitudesSimilares = solicitudRepository.findByUsuarioAndTipoAndEstado(solicitudRecibida.getUsuario(), solicitudRecibida.getTipo(), "ACEPTADO").size();
        int flagLicencias = 0;
        if ( solicitudesSimilares == 0) {
            return asignarNuevaLicencia(solicitudRecibida);
        } else {
            List<Solicitud> solicitudesSimilaresAceptadas = solicitudRepository.findByUsuarioAndTipoAndEstado(solicitudRecibida.getUsuario(),
                    solicitudRecibida.getTipo(), "ACEPTADO");

            for(int i = 0; i < solicitudesSimilares; i++){
            if(solicitudesSimilaresAceptadas.get(i).getLicencia().getEstado().equals("ASIGNADA")){
            flagLicencias = i;
            break;
            }
            if(i == solicitudesSimilares-1){
            flagLicencias = -1;
            }
            }
            if(flagLicencias == -1){
                return asignarNuevaLicencia(solicitudRecibida);
            }else{
            return renovarLicencia(solicitudRecibida, solicitudesSimilaresAceptadas.get(flagLicencias));
            }
        }
    }


    private  ResponseEntity<RespuestaDTO> asignarNuevaLicencia(Solicitud solicitud){
        UserDetailsImpl adminAutenticado = usuarioService.obtenerUsuario();

        RespuestaDTO respuestaDTO;

        if (licenciaRepository.findByPlataformaAndEstado(solicitud.getTipo(),"DISPONIBLE").size() == 0) {
            respuestaDTO = new RespuestaDTO(false, "LICENCIA DENEGADA. No hay licencias disponibles por el momento.", null);
            return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.FORBIDDEN);
        } else {

            Licencia licencia = licenciaRepository.findByPlataformaAndEstado(solicitud.getTipo(), "DISPONIBLE").get(0);
            licencia.setVencimiento(LocalDate.now().plusDays(solicitud.getTiempoSolicitado()));
            solicitud.setLicencia(licencia);
            licencia.setEstado("ASIGNADA");
            solicitud.setApruebaAdminID(adminAutenticado.getId().intValue());
            solicitud.setEstado("ACEPTADO");
            solicitudRepository.save(solicitud);
            licenciaRepository.save(licencia);

            respuestaDTO = new RespuestaDTO(true, "LICENCIA ASIGNADA", solicitud.getLicencia());
            return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.ACCEPTED);
        }
    }

    private  ResponseEntity<RespuestaDTO> renovarLicencia(Solicitud solicitud, Solicitud solicitudAnterior){

        RespuestaDTO respuestaDTO;
        if (solicitudAnterior.getLicencia().getVencimiento().isBefore(LocalDate.now())) {
            return asignarNuevaLicencia(solicitud);
        } else {

            UserDetailsImpl adminAutenticado = usuarioService.obtenerUsuario();
            if(solicitud.getTiempoSolicitado() == 0){
                solicitudAnterior.getLicencia().setVencimiento(solicitudAnterior.getLicencia().getVencimiento().
                        plusDays(25));
            } else {
                solicitudAnterior.getLicencia().setVencimiento(solicitudAnterior.getLicencia().getVencimiento().
                        plusDays(solicitud.getTiempoSolicitado()));
            }

            solicitud.setLicencia(solicitudAnterior.getLicencia());
            solicitud.setApruebaAdminID(adminAutenticado.getId().intValue());
            solicitud.setEstado("ACEPTADO");
            solicitudRepository.save(solicitud);
            licenciaRepository.save(solicitudAnterior.getLicencia());

            respuestaDTO = new RespuestaDTO(true, "LICENCIA RENOVADA", solicitud.getLicencia());
            return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.ACCEPTED);
        }
    }
    @Override
    public ResponseEntity<RespuestaDTO> rechazarSolicitudes(Solicitud solicitud){

        Solicitud solicitudRecibida = solicitudRepository.findById(solicitud.getId()).orElse(null);
        UserDetailsImpl adminAutenticado = usuarioService.obtenerUsuario();
        RespuestaDTO respuestaDTO;

        if(solicitudRecibida == null){
            respuestaDTO = new RespuestaDTO(false, "la Solicitud Recibida no existe", null);
            return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.FORBIDDEN);
        }

        if(!solicitudRecibida.getEstado().equals("PENDIENTE-ADMIN") || solicitudRecibida.getApruebaMentorID() == 0){
            respuestaDTO = new RespuestaDTO(false, "La solicitud todavia no fue aprobada por el mentor", null);
            return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.FORBIDDEN);
        }

        solicitudRecibida.setEstado("DENEGADA");
        solicitudRecibida.setApruebaAdminID(adminAutenticado.getId().intValue());
        solicitudRepository.save(solicitudRecibida);
        respuestaDTO = new RespuestaDTO(true, "La Solicitud se rechazo correctamente", solicitudRecibida);
        return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RespuestaDTO> reservarLicencia(Licencia licencia){
        RespuestaDTO respuestaDTO;

        Licencia licenciaRecibida = licenciaRepository.findBySerie(licencia.getSerie());

    if(!licenciaRecibida.getEstado().equals("DISPONIBLE")){
        respuestaDTO = new RespuestaDTO(false, "La licencia no se encuentra disponible para reservar", null);
        return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.FORBIDDEN);
    }

    licenciaRecibida.setEstado("RESERVADA");
    licenciaRepository.save(licenciaRecibida);
    respuestaDTO = new RespuestaDTO(true, "La licencia se reservo correctamente",  licenciaRecibida);
    return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<RespuestaDTO> revocarLicencia(Licencia licencia){
        RespuestaDTO respuestaDTO;

        Licencia licenciaRecibida = licenciaRepository.findBySerie(licencia.getSerie());

        if(licenciaRecibida.getEstado().equals("DISPONIBLE")){
            respuestaDTO = new RespuestaDTO(false, "La licencia no esta asignada", null);
            return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.FORBIDDEN);
        }

        licenciaRecibida.setEstado("DISPONIBLE");
        licenciaRecibida.setVencimiento(null);
        licenciaRepository.save(licenciaRecibida);
        respuestaDTO = new RespuestaDTO(true, "La licencia " + licenciaRecibida.getSerie() + " fue revocada perfectamente", licenciaRecibida);
        return new ResponseEntity<RespuestaDTO>(respuestaDTO, HttpStatus.OK);
    }
}




