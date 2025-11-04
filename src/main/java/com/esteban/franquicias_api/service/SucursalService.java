package com.esteban.franquicias_api.service;

import com.esteban.franquicias_api.dto.SucursalDTO;
import com.esteban.franquicias_api.model.Sucursal;

import java.util.List;

public interface SucursalService {
    SucursalDTO agregarSucursal(String franquiciaId, SucursalDTO sucursal);
    SucursalDTO actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre);
}
