package com.belezaearte.salaobeleza.application.port.out.managementReportsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Services;

import java.util.List;

public interface SearchReportServicesPort {
    // Todos os servicos (para mostrar o nome no relatorio).
    List<Services> searchAllServices ();
}
