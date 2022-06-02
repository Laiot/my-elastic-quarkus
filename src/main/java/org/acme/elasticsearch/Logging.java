package org.acme.elasticsearch;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jboss.logging.Logger;

@Path("/gelf-logging")
@ApplicationScoped
public class Logging {
    private static final Logger LOG = Logger.getLogger(Logging.class);

    @GET
    public void log() {
        LOG.info("Some useful log message");
    }
}
