package com.belezaearte.salaobeleza.application.port.out.servicesPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Services;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface SearchCreatedServicesPort {
    List<Services> searchCreated(OffsetDateTime searchCreation);
}
