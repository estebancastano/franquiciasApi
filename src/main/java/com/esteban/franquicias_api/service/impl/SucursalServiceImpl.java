package com.esteban.franquicias_api.service.impl;

import com.esteban.franquicias_api.dto.ProductoDTO;
import com.esteban.franquicias_api.dto.SucursalDTO;
import com.esteban.franquicias_api.model.Franquicia;
import com.esteban.franquicias_api.model.Producto;
import com.esteban.franquicias_api.model.Sucursal;
import com.esteban.franquicias_api.repository.FranquiciaRepository;
import com.esteban.franquicias_api.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {

    private final FranquiciaRepository franquiciaRepository;

    @Override
    public SucursalDTO agregarSucursal(String franquiciaId, SucursalDTO sucursalDTO) {
        return franquiciaRepository.findById(franquiciaId)
                .map(franquicia -> {
                    Sucursal nueva = Sucursal.builder()
                            .id(UUID.randomUUID().toString())
                            .nombre(sucursalDTO.getNombre())
                            .productos(Optional.ofNullable(sucursalDTO.getProductos())
                                    .map(lista -> lista.stream()
                                            .map(p -> new Producto(UUID.randomUUID().toString(), p.getNombre(), p.getStock()))
                                            .collect(Collectors.toList()))
                                    .orElse(new ArrayList<>()))
                            .build();

                    franquicia.getSucursales().add(nueva);
                    Franquicia actualizada = franquiciaRepository.save(franquicia);

                    // Retornar DTO de la sucursal recién agregada
                    return SucursalDTO.builder()
                            .id(nueva.getId())
                            .nombre(nueva.getNombre())
                            .franquiciaId(actualizada.getId())
                            .productos(
                                    nueva.getProductos().stream()
                                            .map(p -> new ProductoDTO(p.getId(), p.getNombre(), p.getStock(), nueva.getId()))
                                            .collect(Collectors.toList())
                            )
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));
    }

    @Override
    public SucursalDTO actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
                .map(franquicia -> {
                    franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .findFirst()
                            .ifPresentOrElse(
                                    s -> s.setNombre(nuevoNombre),
                                    () -> { throw new RuntimeException("Sucursal no encontrada"); }
                            );
                    Franquicia actualizada = franquiciaRepository.save(franquicia);

                    // Convertir la sucursal actualizada en DTO
                    Sucursal sucursal = actualizada.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Error actualizando sucursal"));

                    return SucursalDTO.builder()
                            .id(sucursal.getId())
                            .nombre(sucursal.getNombre())
                            .franquiciaId(actualizada.getId())
                            .productos(
                                    sucursal.getProductos().stream()
                                            .map(p -> new ProductoDTO(p.getId(), p.getNombre(), p.getStock(), sucursal.getId()))
                                            .collect(Collectors.toList())
                            )
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));
    }



}
