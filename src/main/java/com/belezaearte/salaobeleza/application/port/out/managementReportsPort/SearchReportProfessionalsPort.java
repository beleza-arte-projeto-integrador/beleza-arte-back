package com.belezaearte.salaobeleza.application.port.out.managementReportsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;

import java.util.List;

public interface SearchReportProfessionalsPort {
    // Todos os profissionais (para mostrar o nome no relatorio).
    List<Professionals> searchAllProfessionals ();
}
