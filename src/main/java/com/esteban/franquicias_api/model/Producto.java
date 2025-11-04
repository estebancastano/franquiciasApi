package com.esteban.franquicias_api.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    private String id;
    private String nombre;
    private int stock;
}
