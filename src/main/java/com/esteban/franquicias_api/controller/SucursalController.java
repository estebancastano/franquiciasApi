package com.esteban.franquicias_api.controller;

import com.esteban.franquicias_api.dto.SucursalDTO;
import com.esteban.franquicias_api.model.Sucursal;
import com.esteban.franquicias_api.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/franquicias/{franquiciaId}/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @PostMapping
    public SucursalDTO crearSucursal(@PathVariable String franquiciaId, @RequestBody SucursalDTO sucursal) {
        return sucursalService.agregarSucursal(franquiciaId, sucursal);
    }

    @PutMapping("/{sucursalId}")
    public SucursalDTO actualizarNombre(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @RequestBody Sucursal sucursalActualizada) {
        return sucursalService.actualizarNombreSucursal(franquiciaId, sucursalId, sucursalActualizada.getNombre());
    }
}
