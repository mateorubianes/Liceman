package com.devforce.devForce.model.dto;

import com.devforce.devForce.model.entity.Solicitud;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LicenciaDTO {

    private long id;
    private String serie;
    private String estado;
    private LocalDate vencimiento;
    private String plataforma;
    private List<SolicitudDTO> solicitudes;

}
