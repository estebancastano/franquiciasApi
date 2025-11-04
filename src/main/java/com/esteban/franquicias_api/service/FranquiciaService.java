package com.esteban.franquicias_api.service;

import com.esteban.franquicias_api.dto.FranquiciaDTO;
import com.esteban.franquicias_api.dto.ProductoDTO;

import java.util.List;

public interface FranquiciaService {
    FranquiciaDTO crearFranquicia(FranquiciaDTO franquiciaDTO);
    FranquiciaDTO actualizarNombreFranquicia(String id, String nuevoNombre);
    List<ProductoDTO> obtenerProductosConMasStock(String franquiciaId);
}
