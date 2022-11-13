package com.yareakh.keyring.controller;
import com.yareakh.keyring.dto.SignupRequest;
import com.yareakh.keyring.dto.SignupResponse;
import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.service.KeyPairService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>For security reasons, this controller must handle signup requests only.</p>
 */
@Controller
@RequestMapping("signup")
@CrossOrigin(origins = "*", maxAge=3600, methods={
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS,
        RequestMethod.HEAD
})
public class SignUpController {
    /**
     * Key pair service used to create new key pairs.
     */
    KeyPairService keyPairService;

    /**
     *
     * @param keyPairService Key pair service used to create new key pairs.
     */
    public SignUpController(KeyPairService keyPairService) {
        this.keyPairService = keyPairService;
    }

    /**
     * <p>Creates a new key pair</p>
     * @param request This DTO packages data required to create a new key pair
     * @return The response object containing the unique id (integer) for the new key pair.
     */
    @PostMapping
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        Long id = keyPairService.create(
                KeyPair.builder()
                        .name(request.name)
                        .password(request.password)
                        .build()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(SignupResponse.builder().id(id).build());
    }
}
