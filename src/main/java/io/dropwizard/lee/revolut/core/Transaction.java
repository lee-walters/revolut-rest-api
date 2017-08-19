package io.dropwizard.lee.revolut.core;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
@NamedQueries(
    {
        @NamedQuery(
            name = "io.dropwizard.lee.revolut.core.Transaction.findAll",
            query = "SELECT t FROM Transaction t"
        )
    }
)
public class Transaction
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "from_account_id", referencedColumnName = "id")
  private Account fromAccount;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "to_account_id", referencedColumnName = "id")
  private Account toAccount;
  
  @Column(name = "amount", nullable = false)
  private BigDecimal amount;
  
  public Transaction()
  {
    
  }

  public Transaction(Account fromAccount, Account toAccount, BigDecimal amount)
  {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public Account getFromAccountHolder()
  {
    return fromAccount;
  }

  public void setFromAccountHolder(Account fromAccount)
  {
    this.fromAccount = fromAccount;
  }

  public Account getToAccountHolder()
  {
    return toAccount;
  }

  public void setToAccountHolder(Account toAccount)
  {
    this.toAccount = toAccount;
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((amount == null) ? 0 : amount.hashCode());
    result = prime * result + ((fromAccount == null) ? 0 : fromAccount.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((toAccount == null) ? 0 : toAccount.hashCode());
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
    Transaction other = (Transaction) obj;
    if (amount == null)
    {
      if (other.amount != null)
        return false;
    }
    else if (!amount.equals(other.amount))
      return false;
    if (fromAccount == null)
    {
      if (other.fromAccount != null)
        return false;
    }
    else if (!fromAccount.equals(other.fromAccount))
      return false;
    if (id != other.id)
      return false;
    if (toAccount == null)
    {
      if (other.toAccount != null)
        return false;
    }
    else if (!toAccount.equals(other.toAccount))
      return false;
    return true;
  }
}
