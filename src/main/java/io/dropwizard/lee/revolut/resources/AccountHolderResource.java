package io.dropwizard.lee.revolut.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.lee.revolut.core.AccountHolder;
import io.dropwizard.lee.revolut.db.AccountHolderDAO;

@Path("/account-holders")
@Produces(MediaType.APPLICATION_JSON)
public class AccountHolderResource
{

  private final AccountHolderDAO accountHolderDAO;

  public AccountHolderResource(AccountHolderDAO accountHolderDAO)
  {
    this.accountHolderDAO = accountHolderDAO;
  }

  @POST
  @UnitOfWork
  public AccountHolder createAccountHolder(AccountHolder accountHolder)
  {
    return accountHolderDAO.create(accountHolder);
  }

  @GET
  @UnitOfWork
  public List<AccountHolder> listAccountHolders()
  {
    return accountHolderDAO.findAll();
  }

}
