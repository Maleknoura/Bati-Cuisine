package org.wora.repository;

import org.wora.entity.Quote;

import java.util.List;

public interface QuoteRepository {
    List<Quote> findAll();
}
