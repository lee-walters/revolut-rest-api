package io.dropwizard.lee.revolut.api;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class AccountInfoDTO {

    private String fullName;
    private String email;
    private int accountNumber;
    private BigDecimal balance;
    
    public AccountInfoDTO() {
        // Jackson deserialization
    }

    public AccountInfoDTO(String fullName, String email, int accountNumber, BigDecimal balance)
    {
      this.fullName = fullName;
      this.email = email;
      this.accountNumber = accountNumber;
      this.balance = balance;
    }

    @JsonProperty
    public String getFullName()
    {
      return fullName;
    }

    public void setFullName(String fullName)
    {
      this.fullName = fullName;
    }

    @JsonProperty
    public String getEmail()
    {
      return email;
    }

    public void setEmail(String email)
    {
      this.email = email;
    }

    @JsonProperty
    public int getAccountNumber()
    {
      return accountNumber;
    }

    public void setAccountNumber(int accountNumber)
    {
      this.accountNumber = accountNumber;
    }

    @JsonProperty
    public BigDecimal getBalance()
    {
      return balance;
    }

    public void setBalance(BigDecimal balance)
    {
      this.balance = balance;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fullName", fullName)
                .add("email", email)
                .add("accountNumber", accountNumber)
                .add("balance", balance)
                .toString();
    }    
}
