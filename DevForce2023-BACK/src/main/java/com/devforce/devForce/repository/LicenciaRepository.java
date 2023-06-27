package com.devforce.devForce.repository;

import com.devforce.devForce.model.entity.Licencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenciaRepository extends JpaRepository<Licencia, Long> {

    Licencia findById(long id);
    List<Licencia> findByPlataformaAndEstado(String plataforma, String estado);
    List<Licencia> findByEstado(String estado);
    Licencia findBySerie(String serie);
}
