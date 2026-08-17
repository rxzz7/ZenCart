package com.zencart.user_service.service.serviceImpl;

import com.zencart.user_service.dto.UserDTO;
import com.zencart.user_service.entity.User;
import com.zencart.user_service.exception.UserObjectNotFoundException;
import com.zencart.user_service.mapper.UserMapper;
import com.zencart.user_service.repo.UserRepo;
import com.zencart.user_service.service.EmailService;
import com.zencart.user_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final EmailService emailService;




    @Override
    public List<UserDTO> findAll() {
        log.info("*** find all user ***");
        return userRepo.findAll().stream().map(userMapper::toDTO).toList();
    }

    @Override
    public UserDTO findByID(Integer userId) {
        log.info("*** find by id ***");
        User user = userRepo.findById(userId).orElseThrow(()->
                new UserObjectNotFoundException(String.format("User not found with this id : %d", userId)));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        log.info("** UserDTO, service; save user **");

        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepo.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        log.info("** UserDTO, service; update user **");

        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepo.save(user);
        return userMapper.toDTO(savedUser);

//      return userMapper.toDTO(this.userRepository.save(userMapper.toEntity(userDTO)));
    }

    @Override
    public UserDTO update(Integer userId, UserDTO userDTO) {
        log.info("***update user with user id***");
//        BeanUtils.copyProperties(userDTO, this.findByID(userId), "id", "credential", "addresses");
//        User updatedUser = userRepo.save(userMapper.toEntity(userDTO));
//        return userMapper.toDTO(updatedUser);

        User existingUser = userRepo.findById(userId).orElseThrow(() ->
                new UserObjectNotFoundException("User not found"));

        BeanUtils.copyProperties(userDTO, existingUser, "id", "credential", "addresses");
        User updatedUser = userRepo.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public Boolean deleteById(Integer userId) {
        log.info("***delete user with user id***");
//        Optional<User> user = userRepo.findById(userId);
//        if(user.isPresent()){
//            userRepo.deleteById(userId);
//            return true;
//        }
//        return false;
        if(!userRepo.existsById(userId)){
            return false;
        }
        userRepo.deleteById(userId);
        return true;

    }

    @Override
    public UserDTO findByUsername(String username) {
        User user = userRepo.findByCredentialUsername(username)
                .orElseThrow(()-> new UserObjectNotFoundException(String.format("User with name :%s not found", username)));
        return userMapper.toDTO(user);
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
