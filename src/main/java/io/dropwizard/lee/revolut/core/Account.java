package io.dropwizard.lee.revolut.core;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "account")
@NamedQueries({ @NamedQuery(name = "io.dropwizard.lee.revolut.core.Account.findAll", query = "SELECT a FROM Account a") })
public class Account
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "accountNumber", nullable = false)
  private long accountNumber;

  @Column(name = "balance", nullable = false)
  private BigDecimal balance;

  @JoinColumn(name = "account_holder_id", referencedColumnName = "id")
  private AccountHolder accountHolder;
  
  public Account()
  {
  }

  public Account(long accountNumber, BigDecimal balance, AccountHolder accountHolder)
  {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.setAccountHolder(accountHolder);
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public long getAccountNumber()
  {
    return accountNumber;
  }

  public void setAccountNumber(long accountNumber)
  {
    this.accountNumber = accountNumber;
  }

  public BigDecimal getBalance()
  {
    return balance;
  }

  public void setBalance(BigDecimal balance)
  {
    this.balance = balance;
  }

  public AccountHolder getAccountHolder()
  {
    return accountHolder;
  }

  public void setAccountHolder(AccountHolder accountHolder)
  {
    this.accountHolder = accountHolder;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (accountNumber ^ (accountNumber >>> 32));
    result = prime * result + ((balance == null) ? 0 : balance.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Account other = (Account) obj;
    if (accountNumber != other.accountNumber)
      return false;
    if (balance == null)
    {
      if (other.balance != null)
        return false;
    }
    else if (!balance.equals(other.balance))
      return false;
    if (id != other.id)
      return false;
    return true;
  }
}
