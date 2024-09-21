package org.wora.repository.repositoryImpl;

import org.wora.entity.Quote;
import org.wora.repository.QuoteRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    @Override
    public void addQuote(Quote quote,int projectId) {
        String query = "INSERT INTO quote (estimatedamount, issuedate, validitydate, isaccepted, projectid) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, quote.getEstimatedAmount());
            stmt.setDate(2, java.sql.Date.valueOf(quote.getIssueDate()));
            stmt.setDate(3, java.sql.Date.valueOf(quote.getValidityDate()));
            stmt.setBoolean(4, quote.isAccepted());
            stmt.setInt(5, projectId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
