package io.dropwizard.lee.revolut.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class AccountTransferInfo {

    private String from;
    private String to;
    private int amount;
    
    public AccountTransferInfo() {
        // Jackson deserialization
    }

    public AccountTransferInfo(String from, String to, int amount)
    {
      super();
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
    public int getAmount()
    {
      return amount;
    }

    public void setAmount(int amount)
    {
      this.amount = amount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("from", from)
                .add("to", to)
                .add("amount", amount)
                .toString();
    }    
}
