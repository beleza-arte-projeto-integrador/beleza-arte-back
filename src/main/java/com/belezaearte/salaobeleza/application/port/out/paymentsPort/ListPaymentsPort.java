package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.PaymentsListView;

import java.util.List;

public interface ListPaymentsPort {
    List<PaymentsListView> listPayments (String status);
}
