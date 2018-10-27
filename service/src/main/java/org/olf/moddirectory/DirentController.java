package org.olf.moddirectory;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

@Controller("/dirent") 
public class DirentController {

    @Get(produces = MediaType.TEXT_PLAIN) 
    public String index() {
        return "Hello World"; 
    }
}
