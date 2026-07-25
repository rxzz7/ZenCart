package com.zencart.user_service.service.serviceImpl;

import com.zencart.user_service.dto.UserDTO;
import com.zencart.user_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public List<UserDTO> findAll() {
        return List.of();
    }

    @Override
    public UserDTO findByID(Integer userId) {
        return null;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO update(Integer userId, UserDTO userDTO) {
        return null;
    }

    @Override
    public Boolean deleteById(Integer userId) {
        return null;
    }

    @Override
    public UserDTO findByUsername(String username) {
        return null;
    }

    @Override
    public void activateAccount(String email) {

    }

    @Override
    public void forgotPassword(String email) {

    }

    @Override
    public void resetPassword(String token, String newPassword) {

    }
}
