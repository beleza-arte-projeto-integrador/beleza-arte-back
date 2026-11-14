package com.belezaearte.salaobeleza.application.port.out.AppointmentsItemsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;

import java.math.BigDecimal;
import java.util.List;

public interface SearchPricePort {
    List<AppointmentsItems>  searchPrice (BigDecimal price);
}
