package com.esteban.franquicias_api.service.impl;

import com.esteban.franquicias_api.dto.FranquiciaDTO;
import com.esteban.franquicias_api.dto.ProductoDTO;
import com.esteban.franquicias_api.dto.SucursalDTO;
import com.esteban.franquicias_api.model.*;
import com.esteban.franquicias_api.repository.FranquiciaRepository;
import com.esteban.franquicias_api.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FranquiciaServiceImpl implements FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;

    @Override
    public FranquiciaDTO crearFranquicia(FranquiciaDTO dto) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la franquicia es obligatorio");
        }

        Franquicia franquicia = new Franquicia();
        franquicia.setNombre(dto.getNombre());
        franquicia.setSucursales(
                Optional.ofNullable(dto.getSucursales())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(s -> new Sucursal(UUID.randomUUID().toString(), s.getNombre(), new ArrayList<>()))
                        .collect(Collectors.toList())
        );

        Franquicia guardada = franquiciaRepository.save(franquicia);

        FranquiciaDTO respuesta = new FranquiciaDTO();
        respuesta.setId(guardada.getId());
        respuesta.setNombre(guardada.getNombre());
        respuesta.setSucursales(
                guardada.getSucursales().stream()
                        .map(s -> new SucursalDTO(s.getId(), s.getNombre(), guardada.getId()))
                        .collect(Collectors.toList())
        );

        return respuesta;
    }

    @Override
    public FranquiciaDTO actualizarNombreFranquicia(String id, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");
        }

        Franquicia actualizada = franquiciaRepository.findById(id)
                .map(f -> {
                    f.setNombre(nuevoNombre);
                    return franquiciaRepository.save(f);
                })
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));

        FranquiciaDTO dto = new FranquiciaDTO();
        dto.setId(actualizada.getId());
        dto.setNombre(actualizada.getNombre());
        dto.setSucursales(
                actualizada.getSucursales().stream()
                        .map(s -> new SucursalDTO(s.getId(), s.getNombre(), actualizada.getId()))
                        .collect(Collectors.toList())
        );

        return dto;
    }

    @Override
    public List<ProductoDTO> obtenerProductosConMasStock(String franquiciaId) {
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));

        return franquicia.getSucursales().stream()
                .filter(sucursal -> sucursal.getProductos() != null && !sucursal.getProductos().isEmpty())
                .map(sucursal -> sucursal.getProductos().stream()
                        .max(Comparator.comparingInt(Producto::getStock))
                        .map(producto -> ProductoDTO.builder()
                                .id(producto.getId())
                                .nombre(producto.getNombre() + " (Sucursal: " + sucursal.getNombre() + ")")
                                .stock(producto.getStock())
                                .sucursalId(sucursal.getId())
                                .build()
                        )
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
