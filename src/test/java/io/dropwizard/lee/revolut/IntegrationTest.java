package io.dropwizard.lee.revolut;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;

import io.dropwizard.lee.revolut.api.AccountInfoDTO;
import io.dropwizard.lee.revolut.api.AccountTransferInfoDTO;
import io.dropwizard.lee.revolut.api.TransferDTO;
import io.dropwizard.lee.revolut.core.AccountHolder;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTest
{
  private static final String TMP_FILE = createTempFile();
  private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-revolut.yml");

  @ClassRule
  public static final DropwizardAppRule<RevolutConfiguration> RULE = new DropwizardAppRule<>(RevolutApplication.class, CONFIG_PATH, ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

  @BeforeClass
  public static void migrateDb() throws Exception
  {
    RULE.getApplication().run("db", "migrate", CONFIG_PATH);
  }

  private static String createTempFile()
  {
    try
    {
      return File.createTempFile("test-revolut", null).getAbsolutePath();
    }
    catch (IOException e)
    {
      throw new IllegalStateException(e);
    }
  }

  @Test
  public void a_shouldCreateNewAccountHolder() throws Exception
  {
    final AccountHolder accountHolder = new AccountHolder("Lee Walters", "l.walters@gmail.com");
    final AccountInfoDTO newAccountHolder = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/create-new").request().put(Entity.entity(accountHolder, MediaType.APPLICATION_JSON_TYPE)).readEntity(AccountInfoDTO.class);

    assertThat(newAccountHolder.getFullName()).isEqualTo(accountHolder.getFullName());
    assertThat(newAccountHolder.getEmail()).isEqualTo(accountHolder.getEmail());
    assertThat(newAccountHolder.getAccountNumber()).isNotNull();
    assertThat(newAccountHolder.getBalance().intValue()).isEqualTo(1000);
  }

  @Test
  public void a_shouldNotCreateNewAccountHolderIfEmailInvalid() throws Exception
  {
    final AccountHolder accountHolder = new AccountHolder("Lee Walters", "sdfsdjrjk3kl4jkldjfklds");
    final String response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/create-new").request().put(Entity.entity(accountHolder, MediaType.APPLICATION_JSON_TYPE)).readEntity(String.class);

    assertThat(response).contains("email not a well-formed email address");
  }

  @Test
  public void a_shouldNotCreateNewAccountHolderIfFullNameInvalid() throws Exception
  {
    final AccountHolder accountHolder = new AccountHolder("", "l.walters@gmail.com");
    final String response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/create-new").request().put(Entity.entity(accountHolder, MediaType.APPLICATION_JSON_TYPE)).readEntity(String.class);

    assertThat(response).contains("fullName may not be empty");
  }

  @Test
  public void b_shouldGetAllAccountHoldersWithAccountDetails() throws Exception
  {
    final List<AccountInfoDTO> accountHolders = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management").request().get(new GenericType<List<AccountInfoDTO>>()
    {
    });

    assertThat(accountHolders.size()).isEqualTo(1);
  }

  @Test
  public void c_shouldGetAccountHolderByAccountNumber() throws Exception
  {
    final List<AccountInfoDTO> allAccountHolders = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management").request().get(new GenericType<List<AccountInfoDTO>>()
    {
    });

    AccountInfoDTO existingAccount = allAccountHolders.get(0);

    // Example account number
    int accountNumber = existingAccount.getAccountNumber();

    final AccountInfoDTO accountHolder = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/" + accountNumber).request().get(AccountInfoDTO.class);

    assertThat(accountHolder.getFullName()).isEqualTo(existingAccount.getFullName());
    assertThat(accountHolder.getEmail()).isEqualTo(existingAccount.getEmail());
    assertThat(accountHolder.getAccountNumber()).isEqualTo(existingAccount.getAccountNumber());
    assertThat(accountHolder.getBalance().intValue()).isEqualTo(existingAccount.getBalance().intValue());
  }

  @Test
  public void d_shouldNotGetAccountHolderIfAccountNumberParamInvalid1() throws Exception
  {
    final Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/2347").request().get();

    assertThat(response.getStatus()).isEqualTo(400);
  }

  @Test
  public void d_shouldNotGetAccountHolderIfAccountNumberParamInvalid2() throws Exception
  {
    final Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/sdfhjk28947*(&£$%(*").request().get();

    assertThat(response.getStatus()).isEqualTo(400);
  }

  @Test
  public void e_shouldTransferFundsFromOneAccountToAnother() throws Exception
  {
    // Firstly we create another account holder to be able to transfer with
    final AccountHolder accountHolder = new AccountHolder("Ana Gomes", "a.gomes@gmail.com");
    RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/create-new").request().put(Entity.entity(accountHolder, MediaType.APPLICATION_JSON_TYPE)).readEntity(AccountInfoDTO.class);

    final List<AccountInfoDTO> allAccountHolders = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management").request().get(new GenericType<List<AccountInfoDTO>>()
    {
    });

    int fromAccountNumber = allAccountHolders.get(0).getAccountNumber();
    BigDecimal fromAccountOriginalBalance = allAccountHolders.get(0).getBalance();

    int toAccountNumber = allAccountHolders.get(1).getAccountNumber();
    BigDecimal toAccountOriginalBalance = allAccountHolders.get(1).getBalance();

    TransferDTO transferDTO = new TransferDTO(fromAccountNumber, toAccountNumber, new BigDecimal("200"));

    final AccountTransferInfoDTO accountTransfer = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/transfer").request().post(Entity.entity(transferDTO, MediaType.APPLICATION_JSON_TYPE)).readEntity(AccountTransferInfoDTO.class);

    assertThat(accountTransfer.getFrom()).isEqualTo(allAccountHolders.get(0).getFullName());
    assertThat(accountTransfer.getTo()).isEqualTo(allAccountHolders.get(1).getFullName());
    assertThat(accountTransfer.getAmount()).isEqualTo(new BigDecimal("200"));

    int fromAccountNewBalance = fromAccountOriginalBalance.intValue() - 200;
    final AccountInfoDTO fromAccountHolderReloaded = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/" + fromAccountNumber).request().get(AccountInfoDTO.class);
    assertThat(fromAccountHolderReloaded.getBalance().intValue()).isEqualTo(new BigDecimal(fromAccountNewBalance).intValue());

    int toAccountNewBalance = toAccountOriginalBalance.intValue() + 200;
    final AccountInfoDTO toAccountHolderReloaded = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account-management/" + toAccountNumber).request().get(AccountInfoDTO.class);
    assertThat(toAccountHolderReloaded.getBalance().intValue()).isEqualTo(new BigDecimal(toAccountNewBalance).intValue());
  }

  @Test
  public void e_shouldNotTransferFundsFromOneAccountToAnotherIfInvalidAmount() throws Exception
  {
    TransferDTO transferDTO = new TransferDTO(12345678, 87654321, new BigDecimal("0"));

    final String response = RULE.client().target("http://localhost:" + RULE.getLocalPort() 
      + "/account-management/transfer").request().post(Entity.entity(transferDTO, MediaType.APPLICATION_JSON_TYPE)).readEntity(String.class);
    
    assertThat(response).contains("amount must be greater than or equal to 1");
  }
  
  @Test
  public void e_shouldNotTransferFundsFromOneAccountToAnotherIfAccountNumberInvalid() throws Exception
  {
    TransferDTO transferDTO = new TransferDTO(12345678, 3455, new BigDecimal("200"));

    final String response = RULE.client().target("http://localhost:" + RULE.getLocalPort() 
      + "/account-management/transfer").request().post(Entity.entity(transferDTO, MediaType.APPLICATION_JSON_TYPE)).readEntity(String.class);
    
    assertThat(response).contains("to must be greater than or equal to 10000000");
  }

  @Test
  public void z_shouldLogFileWritten() throws IOException
  {
    // The log file is using a size and time based policy, which used to
    // silently
    // fail (and not write to a log file). This test ensures not only that the
    // log file exists, but also contains the log line that jetty prints on
    // startup
    final Path log = Paths.get("./logs/application.log");
    assertThat(log).exists();
    final String actual = new String(Files.readAllBytes(log), UTF_8);
    assertThat(actual).contains("0.0.0.0:" + RULE.getLocalPort());
  }
}
