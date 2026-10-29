package com.belezaearte.salaobeleza.application.port.out.professionalsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchCreatedProfessionalsPort {
    List<Professionals> professionalCreated (LocalDateTime created);
}
