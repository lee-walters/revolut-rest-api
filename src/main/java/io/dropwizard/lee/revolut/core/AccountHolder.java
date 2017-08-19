package io.dropwizard.lee.revolut.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "account_holder")
@NamedQueries(
    {
        @NamedQuery(
            name = "io.dropwizard.lee.revolut.core.AccountHolder.findAll",
            query = "SELECT ah FROM AccountHolder ah"
        )
    }
)
public class AccountHolder
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @NotEmpty
  @Column(name = "full_name", nullable = false)
  private String fullName;
  
  @Email
  @NotEmpty
  @Column(name = "email", nullable = false)
  private String email;

  public AccountHolder()
  {
    
  }
  
  public AccountHolder(long id, String fullName, String email)
  {
    super();
    this.id = id;
    this.fullName = fullName;
    this.email = email;
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public String getFullName()
  {
    return fullName;
  }

  public void setFullName(String fullName)
  {
    this.fullName = fullName;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
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
    AccountHolder other = (AccountHolder) obj;
    if (email == null)
    {
      if (other.email != null)
        return false;
    }
    else if (!email.equals(other.email))
      return false;
    if (fullName == null)
    {
      if (other.fullName != null)
        return false;
    }
    else if (!fullName.equals(other.fullName))
      return false;
    if (id != other.id)
      return false;
    return true;
  }
}
