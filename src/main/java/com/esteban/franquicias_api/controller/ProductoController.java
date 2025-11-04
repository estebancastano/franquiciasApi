package com.esteban.franquicias_api.controller;

import com.esteban.franquicias_api.dto.ProductoDTO;
import com.esteban.franquicias_api.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ProductoDTO crearProducto(@PathVariable String franquiciaId,
                                     @PathVariable String sucursalId,
                                     @RequestBody ProductoDTO productoDTO) {
        return productoService.agregarProducto(franquiciaId, sucursalId, productoDTO);
    }

    @PutMapping("/{productoId}")
    public ProductoDTO actualizarNombre(@PathVariable String franquiciaId,
                                        @PathVariable String sucursalId,
                                        @PathVariable String productoId,
                                        @RequestBody Map<String, String> body) {
        String nuevoNombre = body.get("nuevoNombre");
        return productoService.actualizarNombreProducto(franquiciaId, sucursalId, productoId, nuevoNombre);
    }

    @PutMapping("/{productoId}/stock")
    public ProductoDTO actualizarStock(@PathVariable String franquiciaId,
                                       @PathVariable String sucursalId,
                                       @PathVariable String productoId,
                                       @RequestBody Map<String, Integer> body) {
        int nuevoStock = body.getOrDefault("nuevoStock", 0);
        return productoService.actualizarStock(franquiciaId, sucursalId, productoId, nuevoStock);
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String franquiciaId,
                                              @PathVariable String sucursalId,
                                              @PathVariable String productoId) {
        boolean eliminado = productoService.eliminarProducto(franquiciaId, sucursalId, productoId);
        return eliminado
                ? ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"))
                : ResponseEntity.status(404).body(Map.of("error", "Producto no encontrado"));
    }
}
