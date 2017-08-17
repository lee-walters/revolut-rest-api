package io.dropwizard.lee.revolut.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.lee.revolut.core.Transaction;
import io.dropwizard.lee.revolut.db.TransactionDAO;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource
{

  private final TransactionDAO transactionDAO;

  public TransactionResource(TransactionDAO transactionDAO)
  {
    this.transactionDAO = transactionDAO;
  }

  @POST
  @UnitOfWork
  public Transaction createAccount(Transaction transaction)
  {
    return transactionDAO.create(transaction);
  }

  @GET
  @UnitOfWork
  public List<Transaction> listTransactions()
  {
    return transactionDAO.findAll();
  }

}
