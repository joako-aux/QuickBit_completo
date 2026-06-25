package com.proyecto.menu.service;

import com.proyecto.menu.dto.ProductoDto;
import com.proyecto.menu.entity.Categoria;
import com.proyecto.menu.entity.Producto;
import com.proyecto.menu.exception.ResourceNotFoundException;
import com.proyecto.menu.repository.CategoriaRepository;
import com.proyecto.menu.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ProductoDto> obtenerTodos() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public ProductoDto guardar(ProductoDto dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("La categoria con el ID " + dto.getCategoriaId() + " no existe."));

        Producto productoEntidad = convertirAEntidad(dto, categoria);
        Producto productoGuardado = productoRepository.save(productoEntidad);

        return convertirADto(productoGuardado);
    }

    public ProductoDto obtenerPorId(UUID id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el producto con el ID: " + id));

        return convertirADto(producto);
    }

    private ProductoDto convertirADto(Producto producto) {
        ProductoDto dto = new ProductoDto();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setImagen(producto.getImagen());
        dto.setDisponible(producto.getDisponible());
        dto.setCategoriaId(producto.getCategoria().getId());

        return dto;
    }

    private Producto convertirAEntidad(ProductoDto dto, Categoria categoria) {
        Producto entidad = new Producto();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setPrecio(dto.getPrecio());
        entidad.setImagen(dto.getImagen());
        entidad.setDisponible(dto.getDisponible());
        entidad.setCategoria(categoria);

        return entidad;
    }

}
