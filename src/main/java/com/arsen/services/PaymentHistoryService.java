package com.arsen.services;

import com.arsen.exceptions.PaymentNotFoundException;
import com.arsen.models.PaymentHistory;
import com.arsen.repositories.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentHistoryService {
    private final PaymentHistoryRepository historyRepository;

    @Autowired
    public PaymentHistoryService(PaymentHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<PaymentHistory> findAll() {
        return historyRepository.findAll();
    }

    public PaymentHistory findById(Long id) {
        return historyRepository.findById(id).orElseThrow(PaymentNotFoundException::new);
    }

    public void save(PaymentHistory paymentHistory) {
        historyRepository.save(paymentHistory);
    }
}
