package io.dropwizard.lee.revolut;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.lee.revolut.core.Account;
import io.dropwizard.lee.revolut.core.AccountHolder;
import io.dropwizard.lee.revolut.core.Transaction;
import io.dropwizard.lee.revolut.db.AccountDAO;
import io.dropwizard.lee.revolut.db.AccountHolderDAO;
import io.dropwizard.lee.revolut.db.TransactionDAO;
import io.dropwizard.lee.revolut.resources.AccountHolderResource;
import io.dropwizard.lee.revolut.resources.AccountManagementResource;
import io.dropwizard.lee.revolut.resources.AccountResource;
import io.dropwizard.lee.revolut.resources.SyncResource;
import io.dropwizard.lee.revolut.resources.TransactionResource;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RevolutApplication extends Application<RevolutConfiguration>
{

  public static void main(final String[] args) throws Exception
  {
    new RevolutApplication().run(args);
  }

  private final HibernateBundle<RevolutConfiguration> hibernateBundle = new HibernateBundle<RevolutConfiguration>(Account.class, AccountHolder.class, Transaction.class)
  {
    @Override
    public DataSourceFactory getDataSourceFactory(RevolutConfiguration configuration)
    {
      return configuration.getDataSourceFactory();
    }
  };

  @Override
  public String getName()
  {
    return "revolut";
  }

  @Override
  public void initialize(final Bootstrap<RevolutConfiguration> bootstrap)
  {
    // Enable variable substitution with environment variables
    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
    bootstrap.addBundle(new MigrationsBundle<RevolutConfiguration>()
    {
      @Override
      public DataSourceFactory getDataSourceFactory(RevolutConfiguration configuration)
      {
        return configuration.getDataSourceFactory();
      }
    });
    bootstrap.addBundle(hibernateBundle);
  }

  @Override
  public void run(final RevolutConfiguration configuration, final Environment environment)
  {
    final AccountDAO accountDAO = new AccountDAO(hibernateBundle.getSessionFactory());
    final AccountHolderDAO accountHolderDAO = new AccountHolderDAO(hibernateBundle.getSessionFactory());
    final TransactionDAO transactionDAO = new TransactionDAO(hibernateBundle.getSessionFactory());
    final SyncResource sync = new SyncResource();

    environment.jersey().register(new AccountResource(accountDAO));
    environment.jersey().register(new AccountHolderResource(accountHolderDAO));
    environment.jersey().register(new TransactionResource(transactionDAO));
    environment.jersey().register(new AccountManagementResource(accountDAO, accountHolderDAO, transactionDAO, sync));
  }

}
