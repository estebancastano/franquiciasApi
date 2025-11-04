package com.esteban.franquicias_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sucursal {

    private String id;
    private String nombre;

    // Productos embebidos dentro de la sucursal
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
}
