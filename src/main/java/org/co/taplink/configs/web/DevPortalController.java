package org.co.taplink.configs.web;

import lombok.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.co.taplink.utils.TapLinkAppMessages.DEV_PORTAL.HTML_CONTENT;

@Controller
@ConditionalOnProperty(name = "taplink.mode", havingValue = "local-dev")
public class DevPortalController {

    @GetMapping("/**")
    @ResponseBody
    public ResponseEntity<@NonNull String> handleRootAccess() {
        return ResponseEntity.ok().body(HTML_CONTENT);
    }
}
