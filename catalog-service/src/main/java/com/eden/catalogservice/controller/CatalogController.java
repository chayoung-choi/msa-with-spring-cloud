package com.eden.catalogservice.controller;

import com.eden.catalogservice.jpa.CatalogEntity;
import com.eden.catalogservice.service.CatalogService;
import com.eden.catalogservice.vo.ResponseCatalog;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
  Environment env;
  CatalogService catalogService;

  public CatalogController(CatalogService catalogService, Environment env) {
    this.catalogService = catalogService;
    this.env = env;
  }

  @GetMapping("/health_check")
  public String status(HttpServletRequest request) {return String.format("It's Working in Catalog Service on Port %s", request.getServerPort());}

  @GetMapping("/catalogs")
  public ResponseEntity<List<ResponseCatalog>> getUsers() {
    Iterable<CatalogEntity> userList = catalogService.getAllCatalogs();

    List<ResponseCatalog> result = new ArrayList<>();
    userList.forEach(v -> {
      result.add(new ModelMapper().map(v, ResponseCatalog.class));
    });

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

}
