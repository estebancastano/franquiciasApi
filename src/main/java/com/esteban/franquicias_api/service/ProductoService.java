package com.esteban.franquicias_api.service;

import com.esteban.franquicias_api.dto.ProductoDTO;
import java.util.List;

public interface ProductoService {
    ProductoDTO agregarProducto(String franquiciaId, String sucursalId, ProductoDTO productoDTO);
    ProductoDTO actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nuevoNombre);
    ProductoDTO actualizarStock(String franquiciaId, String sucursalId, String productoId, int nuevoStock);
    boolean eliminarProducto(String franquiciaId, String sucursalId, String productoId);
}
