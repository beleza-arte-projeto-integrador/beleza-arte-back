package com.belezaearte.salaobeleza.application.port.in.Clients;

import com.belezaearte.salaobeleza.application.domain.dto.ClientsDataExportDto;

// LGPD (art. 18, II e V): o cliente tem direito de receber uma cópia dos dados dele.
public interface ExportClientsDataUseCase {
    ClientsDataExportDto exportClientsData (Integer id);
}
