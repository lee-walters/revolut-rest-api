package io.dropwizard.lee.revolut.db;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.lee.revolut.core.Account;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class AccountDAO extends AbstractDAO<Account>
{
  public AccountDAO(SessionFactory factory)
  {
    super(factory);
  }

  public Optional<Account> findById(Long id)
  {
    return Optional.ofNullable(get(id));
  }
  
  public Account findByAccountNumber(int accountNumber)
  {   
    Query<Account> query = query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber");
    query.setParameter("accountNumber", accountNumber);
    
    return query.uniqueResult();
  }

  public Account save(Account account)
  {
    return persist(account);
  }

  public List<Account> findAll()
  {
    return list(namedQuery("io.dropwizard.lee.revolut.core.Account.findAll"));
  }
}
