package io.dropwizard.lee.revolut.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.lee.revolut.core.Account;
import io.dropwizard.lee.revolut.db.AccountDAO;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource
{

  private final AccountDAO accountDAO;

  public AccountResource(AccountDAO accountDAO)
  {
    this.accountDAO = accountDAO;
  }

  @POST
  @UnitOfWork
  public Account createAccount(Account account)
  {
    return accountDAO.create(account);
  }

  @GET
  @UnitOfWork
  public List<Account> listAccounts()
  {
    return accountDAO.findAll();
  }

}
