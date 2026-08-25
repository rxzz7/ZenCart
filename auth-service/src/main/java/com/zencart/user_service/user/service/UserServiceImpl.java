package com.zencart.user_service.user.service;

import com.zencart.user_service.dto.UserDTO;
import com.zencart.user_service.entity.Credential;
import com.zencart.user_service.entity.RoleBasedAuthority;
import com.zencart.user_service.entity.User;
import com.zencart.user_service.exception.CredentialNotFoundException;
import com.zencart.user_service.exception.UserObjectNotFoundException;
import com.zencart.user_service.mapper.AddressMapping;
import com.zencart.user_service.mapper.CredentialMapping;
import com.zencart.user_service.mapper.UserMapper;
import com.zencart.user_service.repo.CredentialRepo;
import com.zencart.user_service.repo.UserRepo;
import com.zencart.user_service.auth.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final CredentialRepo credentialRepo;





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
//
//    @Override
//    public UserDTO save(UserDTO userDTO) {
//        log.info("** UserDTO, service; save user **");
//
//        User user = userMapper.toEntity(userDTO);
//        User savedUser = userRepo.save(user);
//        return userMapper.toDTO(savedUser);
//    }

//    @Override
//    public UserDTO update(UserDTO userDTO) {
//        log.info("** UserDTO, service; update user **");
//
//        User user = userMapper.toEntity(userDTO);
//        User savedUser = userRepo.save(user);
//        return userMapper.toDTO(savedUser);
//
////      return userMapper.toDTO(this.userRepository.save(userMapper.toEntity(userDTO)));
//    }

    @Override
    public UserDTO update(Integer userId, UserDTO userDTO) {
        log.info("***update user with user id***");

//        User existingUser = userRepo.findById(userId).orElseThrow(() ->
//                new UserObjectNotFoundException("User not found"));
//
//        BeanUtils.copyProperties(userDTO, existingUser, "id", "credential", "addresses");
//        User updatedUser = userRepo.save(existingUser);
//        return userMapper.toDTO(updatedUser);
        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new UserObjectNotFoundException("User not found"));

        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setImageUrl(userDTO.getImageUrl());
        existingUser.setAddresses(userDTO.getAddressDTOs().stream().map(AddressMapping::toEntity).collect(Collectors.toSet()));
        existingUser.setCredential(CredentialMapping.toEntity(userDTO.getCredentialDTO()));

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
//
        if(!userRepo.existsById(userId)){
            return false;
        }
        userRepo.deleteById(userId);
        return true;
    }

    @Transactional
    public void promoteRole(Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->new UserObjectNotFoundException("User not found"));
        user.getCredential()
                .setRoleBasedAuthority(RoleBasedAuthority.ROLE_ADMIN);
    }

    @Transactional
    public void demoteRole(Integer userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserObjectNotFoundException("User not found"));
        user.getCredential().setRoleBasedAuthority(RoleBasedAuthority.ROLE_USER);
    }

    @Override
    public UserDTO findByUsername(String username) {
        User user = userRepo.findByCredentialUsername(username)
                .orElseThrow(()-> new UserObjectNotFoundException(String.format("User with name :%s not found", username)));
        return userMapper.toDTO(user);
    }


}
