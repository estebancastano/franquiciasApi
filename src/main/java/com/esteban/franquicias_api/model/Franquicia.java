package com.esteban.franquicias_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "franquicias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Franquicia {

    @Id
    private String id;
    private String nombre;

    // Sucursales embebidas dentro de la franquicia
    @Builder.Default
    private List<Sucursal> sucursales = new ArrayList<>();
}
