package io.dropwizard.lee.revolut.api;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class AccountTransferInfoDTO
{
  private String from;

  private String to;

  private BigDecimal amount;

  public AccountTransferInfoDTO()
  {
    // Jackson deserialization
  }

  public AccountTransferInfoDTO(String from, String to, BigDecimal amount)
  {
    this.from = from;
    this.to = to;
    this.amount = amount;
  }

  @JsonProperty
  public String getFrom()
  {
    return from;
  }

  public void setFrom(String from)
  {
    this.from = from;
  }

  @JsonProperty
  public String getTo()
  {
    return to;
  }

  public void setTo(String to)
  {
    this.to = to;
  }

  @JsonProperty
  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

  @Override
  public String toString()
  {
    return MoreObjects.toStringHelper(this).add("from", from).add("to", to).add("amount", amount).toString();
  }
}
