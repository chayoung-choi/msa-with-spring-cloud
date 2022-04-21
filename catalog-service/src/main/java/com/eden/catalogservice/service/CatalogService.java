package com.eden.catalogservice.service;

import com.eden.catalogservice.jpa.CatalogEntity;

public interface CatalogService {
  Iterable<CatalogEntity> getAllCatalogs();
}
