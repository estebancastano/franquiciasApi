package com.esteban.franquicias_api.controller;

import com.esteban.franquicias_api.dto.FranquiciaDTO;
import com.esteban.franquicias_api.dto.ProductoDTO;
import com.esteban.franquicias_api.model.*;
import com.esteban.franquicias_api.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    @PostMapping
    public FranquiciaDTO crearFranquicia(@RequestBody FranquiciaDTO franquicia) {
        return franquiciaService.crearFranquicia(franquicia);
    }

    @PutMapping("/{id}")
    public FranquiciaDTO actualizarNombre(@PathVariable String id, @RequestBody Map<String, String> body) {
        String nuevoNombre = body.get("nuevoNombre");
        return franquiciaService.actualizarNombreFranquicia(id, nuevoNombre);
    }

    @GetMapping("/{id}/productos/top-stock")
    public List<ProductoDTO> obtenerProductosConMasStock(@PathVariable String id) {
        return franquiciaService.obtenerProductosConMasStock(id);
    }
}

