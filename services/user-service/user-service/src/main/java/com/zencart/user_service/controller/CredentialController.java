package com.zencart.user_service.controller;

import com.zencart.user_service.dto.CredentialDTO;
import com.zencart.user_service.response.ResponseCollectionDTO;
import com.zencart.user_service.service.CredentialService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/credentials")
public class CredentialController {

    private final CredentialService credentialService;

    @GetMapping
    public ResponseEntity<ResponseCollectionDTO<CredentialDTO>> findAll(){
    log.info("**CredentialController; Fetching all Credentials");
    return ResponseEntity.ok(new ResponseCollectionDTO<>(credentialService.findAll()));
    }

    @GetMapping("/{credentialId}")
    public ResponseEntity<CredentialDTO> findById(@PathVariable("credentialId")
                                                      @NotBlank(message = "Field must not be blank")
                                                      @Valid final String credentialId){
        log.info("**CredentialController; Fetch credential by id");
        return ResponseEntity.ok(credentialService.findById(Integer.parseInt(credentialId.strip())));
    }

    @PostMapping
    public ResponseEntity<CredentialDTO> save(@RequestBody @NotNull(message = "Fields must not be empty")
                                                  @Valid final CredentialDTO credentialDTO){
        log.info("Saving credential");
        return ResponseEntity.ok(credentialService.save(credentialDTO));
    }

    @PutMapping("/{credentialId}")
    public ResponseEntity<CredentialDTO> update(@RequestBody @NotNull(message = "Fields must not be blank")
                                                @Valid CredentialDTO credentialDTO,
                                                @PathVariable("credentialId") @NotBlank(message = "Fields must not be blank")
                                                @Valid final String credentialId){
        log.info("**CredentialDTO; update credential");
        return ResponseEntity.ok(credentialService.update(Integer.parseInt(credentialId.strip()), credentialDTO));
    }

    @DeleteMapping("/{credentialId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("credentialId") @NotBlank(message = "Fields must not be blank")
                                                  @Valid final String credentialId){
        log.info("**Delete Credential");
        credentialService.deleteById(Integer.parseInt(credentialId.strip()));
        return ResponseEntity.ok(true);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<CredentialDTO> findByUsername(@PathVariable("username") @NotBlank(message = "Fields must not be blank")
                                                            @Valid final String username){
        log.info("**CredentialDTO; find by username");
        return ResponseEntity.ok(credentialService.findByUsername(username));
    }

}
