package org.acme.elasticsearch;

import javax.enterprise.context.control.ActivateRequestContext;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main implements QuarkusApplication{
    
    @ActivateRequestContext
    @Override
    public int run(String... args) throws Exception{
        return 0;
    }
}
