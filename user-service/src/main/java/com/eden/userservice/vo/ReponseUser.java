package com.eden.userservice.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReponseUser {
  private String email;
  private String name;
  private String userId;

  private List<ResponseOrder> orders;
}
