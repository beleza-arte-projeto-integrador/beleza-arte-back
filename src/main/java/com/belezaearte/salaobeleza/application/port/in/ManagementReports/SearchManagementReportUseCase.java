package com.belezaearte.salaobeleza.application.port.in.ManagementReports;

import com.belezaearte.salaobeleza.application.domain.dto.ManagementReportDto;

import java.time.LocalDate;

public interface SearchManagementReportUseCase {
    // idProfessional e opcional: null = todos os profissionais.
    ManagementReportDto searchManagementReport (LocalDate startDate, LocalDate endDate, Integer idProfessional);
}
