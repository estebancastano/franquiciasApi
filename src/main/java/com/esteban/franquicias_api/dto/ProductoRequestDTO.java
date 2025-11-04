package com.esteban.franquicias_api.dto;

import lombok.Data;

@Data
public class ProductoRequestDTO {
    private String nombre;
    private int stock;
}
