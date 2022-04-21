package com.eden.userservice.controller;

import com.eden.userservice.dto.UserDto;
import com.eden.userservice.jpa.UserEntity;
import com.eden.userservice.service.UserService;
import com.eden.userservice.vo.Greeting;
import com.eden.userservice.vo.RequestUser;
import com.eden.userservice.vo.ResponseUser;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
  private final Environment env;
  private UserService userService;

  @Autowired
  private Greeting greeting;

  @Autowired
  public UserController(Environment env, UserService userService) {
    this.env = env;
    this.userService = userService;
  }

  @GetMapping("/health_check")
  public String status(HttpServletRequest request) {return String.format("It's Working in User Service on Port %s", request.getServerPort());}

  @GetMapping("/welcome")
  public String welcome() {
//    return env.getProperty("greeting.message");
    return greeting.getMessage();
  }

  @PostMapping("/users")
  public ResponseEntity createUser(@RequestBody RequestUser user) {
    ModelMapper mapper= new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    UserDto userDto = mapper.map(user, UserDto.class);
    userDto = userService.createUser(userDto);

    ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
  }

  @GetMapping("/users")
  public ResponseEntity<List<ResponseUser>> getUsers() {
    Iterable<UserEntity> userList = userService.getUserByAll();
    List<ResponseUser> result = new ArrayList<>();

    userList.forEach(v -> {
      result.add(new ModelMapper().map(v, ResponseUser.class));
    });

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
    UserDto userDto = userService.getUserByUserId(userId);
    ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);
    return ResponseEntity.status(HttpStatus.OK).body(returnValue);
  }
}
