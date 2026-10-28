package com.belezaearte.salaobeleza.application.port.out.professionalsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchCreatedProfessionalsPort {
    List<Clients> professionalExperience (LocalDateTime created);
}
