package com.esteban.franquicias_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    private String id;
    private String nombre;
    private int stock;
}
