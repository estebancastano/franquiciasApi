package com.esteban.franquicias_api.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SucursalDTO {
    private String id;
    private String nombre;
    // Solo se manda el ID de la franquicia
    private String franquiciaId;
    private List<ProductoDTO> productos;

    public SucursalDTO(String id, String nombre, String franquiciaId) {
        this.id = id;
        this.nombre = nombre;
        this.franquiciaId = franquiciaId;
    }
}
