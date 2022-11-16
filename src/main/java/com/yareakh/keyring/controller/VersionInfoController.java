package com.yareakh.keyring.controller;

import com.yareakh.keyring.dto.VersionInfoResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.security.RolesAllowed;

/**
 * <p>For security reasons, this controller must handle signup requests only.</p>
 */
@RestController
@RequestMapping("version-info")
@CrossOrigin(origins = "*", maxAge=3600, methods={
        RequestMethod.GET
})
@RolesAllowed({"USER", "ADMIN"})
public class VersionInfoController {
    @GetMapping
    public VersionInfoResponse get() {
        return VersionInfoResponse
                .builder()
                .version("1")
                .commitMessage("lorem ipsum")
                .buildNumber("881445")
                .build();
    }
}
