package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ServicesRepository;
import com.belezaearte.salaobeleza.application.port.out.managementReportsPort.SearchReportProfessionalsPort;
import com.belezaearte.salaobeleza.application.port.out.managementReportsPort.SearchReportServicesPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// Dados extras usados pelo relatorio gerencial (so leitura).
// Os agendamentos do periodo vem do SearchDateAppointmentsPort, que ja existe no modulo Appointments.
@Component
@AllArgsConstructor
public class ManagementReportsAdapter implements SearchReportServicesPort, SearchReportProfessionalsPort {

    private final ServicesRepository servicesRepository;
    private final ProfessionalsRepository professionalsRepository;

    @Override
    public List<Services> searchAllServices() {
        return servicesRepository.findAll();
    }

    @Override
    public List<Professionals> searchAllProfessionals() {
        return professionalsRepository.findAll();
    }
}
