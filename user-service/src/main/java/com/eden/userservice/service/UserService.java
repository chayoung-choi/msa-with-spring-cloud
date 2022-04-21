package com.eden.userservice.service;

import com.eden.userservice.dto.UserDto;
import com.eden.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
  UserDto createUser(UserDto userDto);

  UserDto getUserByUserId(String userId);
  Iterable<UserEntity> getUserByAll();

  UserDto getUserDetailsByEmail(String userName);
}
