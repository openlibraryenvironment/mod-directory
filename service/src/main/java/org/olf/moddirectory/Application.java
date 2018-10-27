package org.olf.moddirectory;

import io.micronaut.runtime.Micronaut;
import groovy.transform.CompileStatic;
import javax.inject.Singleton;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;

@Singleton
public class Application implements ApplicationEventListener<ServerStartupEvent> {

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
    }

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}
