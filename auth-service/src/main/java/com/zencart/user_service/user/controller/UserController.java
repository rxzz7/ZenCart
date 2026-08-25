package com.zencart.user_service.user.controller;

import com.zencart.user_service.dto.UserDTO;
import com.zencart.user_service.response.ResponseCollectionDTO;
import com.zencart.user_service.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j

@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseCollectionDTO<UserDTO>> findAll(){
        log.info("**User List, controller; fetch all users**");
        return ResponseEntity.ok(new ResponseCollectionDTO<>(userService.findAll()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findById(@PathVariable("userId")
                                                @NotBlank(message = "Input must not be blank")
                                                @Valid final String userId){
        log.info("**UserDTO resource ; find user by id**");
        return ResponseEntity.ok(userService.findByID(Integer.parseInt(userId.strip()))); //String to int
    }

//    @PostMapping
//    public ResponseEntity<UserDTO> save(@RequestBody
//                                            @NotNull(message = "Input must not be null")
//                                            @Valid final UserDTO userDTO){
//
//        log.info("**USerDto, saving the user**");
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userDTO));
//    }

//    @PutMapping
//    public ResponseEntity<UserDTO> update(@RequestBody
//                                              @NotNull(message = "Fields cannot be null")
//                                              @Valid UserDTO userDTO){
//
//        log.info("**UserDto , resource , Update user**");
//        return ResponseEntity.ok(userService.update(userDTO));
//    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> update(@PathVariable("userId")
                                              @NotBlank(message = "Input must not be blank") final String userId,
                                              @RequestBody @NotNull(message = "Fields cannot be null")
                                              @Valid UserDTO userDTO){

        log.info("**UserDto, resource.. update user with userId**");
        return ResponseEntity.ok(userService.update(Integer.parseInt(userId.strip()), userDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable
                                                  @NotBlank(message = "Input must not be blank") final String userId ){
        log.info("**Boolean, resource; delete user by Id**");

        if (userService.deleteById(Integer.parseInt(userId.strip()))){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/promote")
    public ResponseEntity<String> promoteRole(@PathVariable Integer userId) {

        userService.promoteRole(userId);
        return ResponseEntity.ok("User is promoted to admin");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/demote")
    public ResponseEntity<String> demoteRole(@PathVariable Integer userId) {

        userService.demoteRole(userId);

        return ResponseEntity.ok("User is demoted to user");
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> findUserByUsername(@PathVariable
                                                    @NotBlank(message = "Input must not be blank")
                                                    final String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }


}
