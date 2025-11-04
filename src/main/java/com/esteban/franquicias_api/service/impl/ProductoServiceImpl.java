package com.esteban.franquicias_api.service.impl;

import com.esteban.franquicias_api.dto.ProductoDTO;
import com.esteban.franquicias_api.model.Franquicia;
import com.esteban.franquicias_api.model.Producto;
import com.esteban.franquicias_api.model.Sucursal;
import com.esteban.franquicias_api.repository.FranquiciaRepository;
import com.esteban.franquicias_api.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final FranquiciaRepository franquiciaRepository;

    @Override
    public ProductoDTO agregarProducto(String franquiciaId, String sucursalId, ProductoDTO productoDTO) {
        return franquiciaRepository.findById(franquiciaId)
                .map(franquicia -> {
                    Sucursal sucursal = franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

                    Producto producto = Producto.builder()
                            .id(UUID.randomUUID().toString())
                            .nombre(productoDTO.getNombre())
                            .stock(productoDTO.getStock())
                            .build();

                    sucursal.getProductos().add(producto);
                    franquiciaRepository.save(franquicia);

                    return ProductoDTO.builder()
                            .id(producto.getId())
                            .nombre(producto.getNombre())
                            .stock(producto.getStock())
                            .sucursalId(sucursalId)
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));
    }

    @Override
    public ProductoDTO actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
                .map(franquicia -> {
                    Sucursal sucursal = franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

                    Producto producto = sucursal.getProductos().stream()
                            .filter(p -> p.getId().equals(productoId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                    producto.setNombre(nuevoNombre);
                    franquiciaRepository.save(franquicia);

                    return ProductoDTO.builder()
                            .id(producto.getId())
                            .nombre(producto.getNombre())
                            .stock(producto.getStock())
                            .sucursalId(sucursalId)
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));
    }

    @Override
    public ProductoDTO actualizarStock(String franquiciaId, String sucursalId, String productoId, int nuevoStock) {
        return franquiciaRepository.findById(franquiciaId)
                .map(franquicia -> {
                    Sucursal sucursal = franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

                    Producto producto = sucursal.getProductos().stream()
                            .filter(p -> p.getId().equals(productoId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                    producto.setStock(nuevoStock);
                    franquiciaRepository.save(franquicia);

                    return ProductoDTO.builder()
                            .id(producto.getId())
                            .nombre(producto.getNombre())
                            .stock(producto.getStock())
                            .sucursalId(sucursalId)
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));
    }

    @Override
    public boolean eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
        return franquiciaRepository.findById(franquiciaId)
                .map(franquicia -> {
                    Sucursal sucursal = franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

                    boolean eliminado = sucursal.getProductos().removeIf(p -> p.getId().equals(productoId));

                    if (eliminado) franquiciaRepository.save(franquicia);
                    return eliminado;
                })
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));
    }
}
