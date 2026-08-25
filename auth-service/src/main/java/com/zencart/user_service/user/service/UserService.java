package com.zencart.user_service.user.service;

import com.zencart.user_service.dto.UserDTO;
import com.zencart.user_service.entity.RoleBasedAuthority;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();
    UserDTO findByID(final Integer userId);
//    UserDTO save(final UserDTO userDTO);
//    UserDTO update(final UserDTO userDTO);
    UserDTO update(final Integer userId, final UserDTO userDTO);
    Boolean deleteById(final Integer userId);
    UserDTO findByUsername(final String username);

    void promoteRole(Integer userId);
    void demoteRole(Integer userId);
}
