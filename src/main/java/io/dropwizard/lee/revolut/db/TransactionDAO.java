package io.dropwizard.lee.revolut.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.lee.revolut.core.Transaction;

public class TransactionDAO extends AbstractDAO<Transaction>
{
  public TransactionDAO(SessionFactory factory)
  {
    super(factory);
  }

  public Optional<Transaction> findById(Long id)
  {
    return Optional.ofNullable(get(id));
  }

  public Transaction create(Transaction transaction)
  {
    return persist(transaction);
  }

  public List<Transaction> findAll()
  {
    return list(namedQuery("io.dropwizard.lee.revolut.core.Transaction.findAll"));
  }
}
