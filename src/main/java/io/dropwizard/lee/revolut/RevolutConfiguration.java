package io.dropwizard.lee.revolut;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RevolutConfiguration extends Configuration
{
  @Valid
  @NotNull
  private DataSourceFactory database = new DataSourceFactory();
  
  @JsonProperty("database")
  public DataSourceFactory getDataSourceFactory() {
      return database;
  }

  @JsonProperty("database")
  public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
      this.database = dataSourceFactory;
  }
}
