package io.dropwizard.lee.revolut.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.lee.revolut.core.AccountHolder;

public class AccountHolderDAO extends AbstractDAO<AccountHolder>
{
  public AccountHolderDAO(SessionFactory factory)
  {
    super(factory);
  }

  public Optional<AccountHolder> findById(Long id)
  {
    return Optional.ofNullable(get(id));
  }

  public AccountHolder create(AccountHolder accountHolder)
  {
    return persist(accountHolder);
  }

  public List<AccountHolder> findAll()
  {
    return list(namedQuery("io.dropwizard.lee.revolut.core.AccountHolder.findAll"));
  }
}
