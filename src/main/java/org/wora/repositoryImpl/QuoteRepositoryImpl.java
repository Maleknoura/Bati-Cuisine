package org.wora.repositoryImpl;

import org.wora.entity.Quote;
import org.wora.repository.QuoteRepository;

import java.sql.Connection;
import java.util.List;

public class QuoteRepositoryImpl implements QuoteRepository {
    private Connection connection;
    public QuoteRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Quote> findAll() {
        return List.of();
    }
}
