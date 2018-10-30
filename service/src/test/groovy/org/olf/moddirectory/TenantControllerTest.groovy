package org.olf.moddirectory;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TenantControllerTest {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeClass
    public static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class); 
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());  
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void testTenantCreate() throws Exception {
        HttpRequest request = HttpRequest.POST("/_/tenant", MediaType.APPLICATION_JSON)
              .header("X-Okapi-Tenant", "diku"); 
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        println(body)
    }

    @Test
    public void testTenantDestroy() throws Exception {
        HttpRequest request = HttpRequest.DELETE("/_/tenant")
              .header("X-Okapi-Tenant", "diku"); 
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        println(body)
    }
}
