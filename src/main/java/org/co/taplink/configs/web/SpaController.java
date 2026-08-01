package org.co.taplink.configs.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    /**
     * Forwards any unmapped route that does not contain a file extension (like .js, .css, .png)
     * and does not start with /api back to Angular's index.html.
     */
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirectSingle() {
        return "forward:/index.html";
    }

    @RequestMapping(value = "/**/{path:[^\\.]*}")
    public String redirectMultiple() {
        return "forward:/index.html";
    }
}
