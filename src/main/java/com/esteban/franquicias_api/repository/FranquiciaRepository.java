package com.esteban.franquicias_api.repository;

import com.esteban.franquicias_api.model.Franquicia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FranquiciaRepository extends MongoRepository<Franquicia, String> {

    // Buscar una franquicia por nombre
    Franquicia findByNombre(String nombre);

    // Buscar todas las sucursales de una franquicia específica
    @Query(value = "{ '_id': ?0 }", fields = "{ 'sucursales': 1, '_id': 0 }")
    Optional<Franquicia> findSucursalesByFranquiciaId(String franquiciaId);

    // Buscar una sucursal específica dentro de una franquicia
    @Query(value = "{ '_id': ?0, 'sucursales.id': ?1 }", fields = "{ 'sucursales.$': 1 }")
    Optional<Franquicia> findSucursalByFranquiciaIdAndSucursalId(String franquiciaId, String sucursalId);

    // Buscar todos los productos de una sucursal específica
    @Query(value = "{ '_id': ?0, 'sucursales.id': ?1 }", fields = "{ 'sucursales.$.productos': 1 }")
    Optional<Franquicia> findProductosBySucursalId(String franquiciaId, String sucursalId);

    // Buscar un producto específico en una sucursal específica
    @Query(value = "{ '_id': ?0, 'sucursales.id': ?1, 'sucursales.productos.id': ?2 }",
            fields = "{ 'sucursales.$.productos.$': 1 }")
    Optional<Franquicia> findProductoBySucursalIdAndProductoId(String franquiciaId, String sucursalId, String productoId);
}
