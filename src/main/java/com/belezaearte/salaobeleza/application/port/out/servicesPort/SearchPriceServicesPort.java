package com.belezaearte.salaobeleza.application.port.out.servicesPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Services;

import java.math.BigDecimal;
import java.util.List;

public interface SearchPriceServicesPort {
    List<Services> searchPrice (BigDecimal price);
}
