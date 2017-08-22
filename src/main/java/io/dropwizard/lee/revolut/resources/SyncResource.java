package io.dropwizard.lee.revolut.resources;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SyncResource
{
  private ReentrantLock lock = new ReentrantLock();

  public SyncResource()
  {
  }

  public Lock getLock()
  {
    return lock;
  }
}
