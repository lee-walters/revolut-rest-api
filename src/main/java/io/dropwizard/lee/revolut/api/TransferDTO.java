package io.dropwizard.lee.revolut.api;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class TransferDTO
{

  @Min(10000000)
  @Max(99999999)
  private int from;

  @Min(10000000)
  @Max(99999999)
  private int to;

  @Min(1)
  @Max(99999999)
  private BigDecimal amount;

  public TransferDTO()
  {
    // Jackson deserialization
  }

  public TransferDTO(int from, int to, BigDecimal amount)
  {
    this.from = from;
    this.to = to;
    this.amount = amount;
  }

  @JsonProperty
  public int getFrom()
  {
    return from;
  }

  public void setFrom(int from)
  {
    this.from = from;
  }

  @JsonProperty
  public int getTo()
  {
    return to;
  }

  public void setTo(int to)
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
