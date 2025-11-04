package com.esteban.franquicias_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class FranquiciaDTO {
    private String id;
    private String nombre;
    private List<SucursalDTO> sucursales;
}
