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
  
  @JoinColumn(name = "from_account_holder_id", referencedColumnName = "id")
  private AccountHolder fromAccountHolder;
  
  @JoinColumn(name = "to_account_holder_id", referencedColumnName = "id")
  private AccountHolder toAccountHolder;
  
  @Column(name = "amount", nullable = false)
  private BigDecimal amount;
  
  public Transaction()
  {
    
  }

  public Transaction(long id, AccountHolder fromAccountHolder, AccountHolder toAccountHolder, BigDecimal amount)
  {
    super();
    this.id = id;
    this.fromAccountHolder = fromAccountHolder;
    this.toAccountHolder = toAccountHolder;
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

  public AccountHolder getFromAccountHolder()
  {
    return fromAccountHolder;
  }

  public void setFromAccountHolder(AccountHolder fromAccountHolder)
  {
    this.fromAccountHolder = fromAccountHolder;
  }

  public AccountHolder getToAccountHolder()
  {
    return toAccountHolder;
  }

  public void setToAccountHolder(AccountHolder toAccountHolder)
  {
    this.toAccountHolder = toAccountHolder;
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
    result = prime * result + ((fromAccountHolder == null) ? 0 : fromAccountHolder.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((toAccountHolder == null) ? 0 : toAccountHolder.hashCode());
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
    if (fromAccountHolder == null)
    {
      if (other.fromAccountHolder != null)
        return false;
    }
    else if (!fromAccountHolder.equals(other.fromAccountHolder))
      return false;
    if (id != other.id)
      return false;
    if (toAccountHolder == null)
    {
      if (other.toAccountHolder != null)
        return false;
    }
    else if (!toAccountHolder.equals(other.toAccountHolder))
      return false;
    return true;
  }
}
