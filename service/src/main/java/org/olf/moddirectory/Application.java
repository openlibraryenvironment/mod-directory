package org.olf.moddirectory;

import io.micronaut.runtime.Micronaut;
import groovy.transform.CompileStatic;
import javax.inject.Singleton;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class Application implements ApplicationEventListener<ServerStartupEvent> {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  @Override
  public void onApplicationEvent(ServerStartupEvent event) {
  }

  public static void main(String[] args) {
    LOG.debug("Application::main("+args+")");
    Micronaut.run(Application.class);
  }
}
