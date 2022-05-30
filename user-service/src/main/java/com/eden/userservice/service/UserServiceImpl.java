package com.eden.userservice.service;

import com.eden.userservice.client.OrderServiceClient;
import com.eden.userservice.dto.UserDto;
import com.eden.userservice.jpa.UserEntity;
import com.eden.userservice.jpa.UserRepository;
import com.eden.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  UserRepository userRepository;

  BCryptPasswordEncoder passwordEncoder;

  Environment env;
//  RestTemplate restTemplate;
  OrderServiceClient orderServiceClient;

  @Autowired
  public UserServiceImpl(UserRepository userRepository,
                         BCryptPasswordEncoder passwordEncoder,
                         Environment env,
                         OrderServiceClient orderServiceClient) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.env = env;
    this.orderServiceClient = orderServiceClient;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(username);
    if (userEntity == null) {
      throw new UsernameNotFoundException(username);
    }

    return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
            true, true, true, true,
            new ArrayList<>());
  }

  @Override
  public UserDto createUser(UserDto userDto) {
    userDto.setUserId(UUID.randomUUID().toString());

    UserEntity checkUser = userRepository.findByEmail(userDto.getEmail());
    if (checkUser != null) {
      throw new EntityExistsException(userDto.getEmail());
    }
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = mapper.map(userDto, UserEntity.class);
    userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

    userRepository.save(userEntity);

    UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
    return returnUserDto;
  }

  @Override
  public UserDto getUserByUserId(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId);
    if (userEntity == null) {
      throw new UsernameNotFoundException("User not found");
    }
    UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//    List<ResponseOrder> orders = new ArrayList<>();
    /* Using as rest template */
//    String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//    ResponseEntity<List<ResponseOrder>> orderListResponse =  restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//            new ParameterizedTypeReference<List<ResponseOrder>>() {
//    });
//    List<ResponseOrder> ordersList = orderListResponse.getBody();

    /* Using as feign client */
    List<ResponseOrder> ordersList = orderServiceClient.getOrders(userId);
    userDto.setOrders(ordersList);

    return userDto;
  }

  @Override
  public Iterable<UserEntity> getUserByAll() {
    return userRepository.findAll();
  }

  @Override
  public UserDto getUserDetailsByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email);

    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }

    UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
    return userDto;
  }
}
