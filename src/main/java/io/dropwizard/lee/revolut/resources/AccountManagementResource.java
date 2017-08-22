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
import io.dropwizard.lee.revolut.api.AccountInfoDTO;
import io.dropwizard.lee.revolut.api.AccountTransferInfoDTO;
import io.dropwizard.lee.revolut.api.TransferDTO;
import io.dropwizard.lee.revolut.core.Account;
import io.dropwizard.lee.revolut.core.AccountHolder;
import io.dropwizard.lee.revolut.core.Transaction;
import io.dropwizard.lee.revolut.db.AccountDAO;
import io.dropwizard.lee.revolut.db.AccountHolderDAO;
import io.dropwizard.lee.revolut.db.TransactionDAO;

@Path("/account-management")
@Produces(MediaType.APPLICATION_JSON)
public class AccountManagementResource
{
  private final AccountDAO accountDAO;
  private final AccountHolderDAO accountHolderDAO;
  private final TransactionDAO transactionDAO;
  private SyncResource sync;

  public AccountManagementResource(AccountDAO accountDAO, AccountHolderDAO accountHolderDAO, TransactionDAO transactionDAO, SyncResource sync)
  {
    this.accountDAO = accountDAO;
    this.accountHolderDAO = accountHolderDAO;
    this.transactionDAO = transactionDAO;
    this.sync = sync;
  }

  @PUT
  @UnitOfWork
  @Path("/create-new")
  public AccountInfoDTO createAccountHolder(@Valid AccountHolder accountHolder)
  {
    AccountHolder newAccountHolder = accountHolderDAO.create(accountHolder);
    Account newAccount = null;
    AccountInfoDTO info = new AccountInfoDTO();

    if (newAccountHolder != null)
    {
      // Generate random account number
      Random rnd = new Random();
      int accountNumber = 10000000 + rnd.nextInt(99999999);

      // Default balance for opening an account
      BigDecimal balance = new BigDecimal(1000);

      newAccount = accountDAO.save(new Account(accountNumber, balance, newAccountHolder));
      info = new AccountInfoDTO(newAccountHolder.getFullName(), newAccountHolder.getEmail(), newAccount.getAccountNumber(), newAccount.getBalance());
    }

    return info;
  }

  @GET
  @UnitOfWork
  @Path("/{accountNumber}")
  public AccountInfoDTO getAccountHolderByAccountNumber(@Min(10000000) @Max(99999999) @PathParam("accountNumber") IntParam accountNumber)
  {
    AccountInfoDTO info = new AccountInfoDTO();
    Account account = accountDAO.findByAccountNumber(accountNumber.get().intValue());

    if (account != null)
    {
      info = new AccountInfoDTO(account.getAccountHolder().getFullName(), account.getAccountHolder().getEmail(), account.getAccountNumber(), account.getBalance());
    }

    return info;
  }

  @GET
  @UnitOfWork
  public List<AccountInfoDTO> listAllAccountHoldersWithAccounts()
  {
    List<AccountInfoDTO> infos = new ArrayList<>();
    List<Account> accounts = accountDAO.findAll();

    if (accounts != null && !accounts.isEmpty())
    {
      for (Account account : accounts)
      {
        infos.add(new AccountInfoDTO(account.getAccountHolder().getFullName(), account.getAccountHolder().getEmail(), account.getAccountNumber(), account.getBalance()));
      }
    }

    return infos;
  }

  @POST
  @UnitOfWork
  @Path("/transfer")
  public AccountTransferInfoDTO transferBetweenAccounts(@Valid TransferDTO transferDTO)
  {
    sync.getLock().lock();

    try
    {
      AccountTransferInfoDTO transferInfo = new AccountTransferInfoDTO();
      Account fromAcc = accountDAO.findByAccountNumber(transferDTO.getFrom());
      Account toAcc = accountDAO.findByAccountNumber(transferDTO.getTo());

      if (fromAcc != null && toAcc != null && fromAcc.getBalance().intValue() >= transferDTO.getAmount().intValue())
      {
        int fromBalance = fromAcc.getBalance().intValue();
        fromAcc.setBalance(new BigDecimal(fromBalance - transferDTO.getAmount().intValue()));
        int toBalance = toAcc.getBalance().intValue();
        toAcc.setBalance(new BigDecimal(toBalance + transferDTO.getAmount().intValue()));

        accountDAO.save(fromAcc);
        accountDAO.save(toAcc);

        transferInfo = new AccountTransferInfoDTO(fromAcc.getAccountHolder().getFullName(), toAcc.getAccountHolder().getFullName(), transferDTO.getAmount());
        transactionDAO.create(new Transaction(fromAcc, toAcc, transferDTO.getAmount()));
      }

      return transferInfo;
    }
    finally
    {
      sync.getLock().unlock();
    }
  }
}
