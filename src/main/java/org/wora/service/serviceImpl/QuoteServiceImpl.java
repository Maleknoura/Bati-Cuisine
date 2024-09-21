package org.wora.service.serviceImpl;

import org.wora.entity.Quote;
import org.wora.repository.QuoteRepository;
import org.wora.service.QuoteService;

import java.time.LocalDate;

public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override

    public void saveQuote(double estimatedAmount, LocalDate issueDate, LocalDate validityDate, boolean isAccepted, int projectId) {
        Quote quote = new Quote();
        quote.setEstimatedAmount(estimatedAmount);
        quote.setIssueDate(issueDate);
        quote.setValidityDate(validityDate);
        quote.setAccepted(isAccepted);

        quoteRepository.addQuote(quote, projectId);
    }


}
