package org.wora.service;

import java.time.LocalDate;

public interface QuoteService {
    void saveQuote(double estimatedAmount, LocalDate issueDate, LocalDate validityDate, boolean isAccepted,int projectId);

}
