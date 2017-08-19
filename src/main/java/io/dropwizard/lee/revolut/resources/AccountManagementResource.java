package io.dropwizard.lee.revolut.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.lee.revolut.api.AccountInfo;
import io.dropwizard.lee.revolut.api.AccountTransferInfo;
import io.dropwizard.lee.revolut.core.Account;
import io.dropwizard.lee.revolut.core.AccountHolder;
import io.dropwizard.lee.revolut.db.AccountDAO;
import io.dropwizard.lee.revolut.db.AccountHolderDAO;

@Path("/account-management")
@Produces(MediaType.APPLICATION_JSON)
public class AccountManagementResource
{
  private final AccountDAO accountDAO;
  private final AccountHolderDAO accountHolderDAO;
  
  public AccountManagementResource(AccountDAO accountDAO, AccountHolderDAO accountHolderDAO)
  {
    this.accountDAO = accountDAO;
    this.accountHolderDAO = accountHolderDAO;
  }
  
  @PUT
  @UnitOfWork
  @Path("/create-new")
  public AccountInfo createAccountHolder(@Valid AccountHolder accountHolder)
  {
    AccountHolder newAccountHolder = accountHolderDAO.create(accountHolder);
    Account newAccount = null;
    AccountInfo info = new AccountInfo();
    
    if (newAccountHolder != null)
    {
      // Generate random account number
      Random rnd = new Random();
      int accountNumber = 10000000 + rnd.nextInt(99999999);

      // Default balance for opening an account
      BigDecimal balance = new BigDecimal(1000);
      
      newAccount = accountDAO.create(new Account(accountNumber, balance, newAccountHolder));
      info = new AccountInfo(newAccountHolder.getFullName(), newAccountHolder.getEmail(), newAccount.getAccountNumber(), newAccount.getBalance());
    }
    
    return info;
  }

  @GET
  @UnitOfWork
  @Path("/{accountNumber}")
  public AccountInfo getAccountHolderByAccountNumber(@Min(10000000) @Max(99999999) @PathParam("accountNumber") IntParam accountNumber)
  {
    AccountInfo info = new AccountInfo();
    Account account = accountDAO.findByAccountNumber(accountNumber.get().intValue());
    
    if (account != null)
    {
      info = new AccountInfo(account.getAccountHolder().getFullName(), account.getAccountHolder().getEmail(), account.getAccountNumber(), account.getBalance());
    }
    
    return info;
  }
  
  @GET
  @UnitOfWork
  public List<AccountInfo> listAllAccountHoldersWithAccounts()
  {
    List<AccountInfo> infos = new ArrayList<>();
    List<Account> accounts = accountDAO.findAll();
    
    if (accounts != null && !accounts.isEmpty())
    {
      for (Account account : accounts)
      {
        infos.add(new AccountInfo(account.getAccountHolder().getFullName(), account.getAccountHolder().getEmail(), account.getAccountNumber(), account.getBalance()));
      }
    }
    
    return infos;
  }
  
  @POST
  @UnitOfWork
  @Path("/transfer/{from}/{to}/{amount}")
  public AccountTransferInfo transferBetweenAccounts(@Min(10000000) @Max(99999999) @PathParam("from") IntParam from, @Min(10000000) @Max(99999999) @PathParam("to") IntParam to, @Min(1) @Max(99999999) @PathParam("amount") IntParam amount)
  {
    AccountTransferInfo transferInfo = new AccountTransferInfo();
    Account fromAcc = accountDAO.findByAccountNumber(from.get().intValue());
    Account toAcc = accountDAO.findByAccountNumber(to.get().intValue());
    
    if (fromAcc != null && toAcc != null && fromAcc.getBalance().intValue() >= amount.get().intValue())
    {
      int fromBalance = fromAcc.getBalance().intValue();
      fromAcc.setBalance(new BigDecimal(fromBalance - amount.get().intValue()));
      int toBalance = toAcc.getBalance().intValue();
      toAcc.setBalance(new BigDecimal(toBalance + amount.get().intValue()));
      
      transferInfo = new AccountTransferInfo(fromAcc.getAccountHolder().getFullName(), toAcc.getAccountHolder().getFullName(), amount.get().intValue());
    }

    return transferInfo;
  }
  
  
}
