package com.proyecto.menu.service;

import com.proyecto.menu.dto.CategoriaDto;
import com.proyecto.menu.entity.Categoria;
import com.proyecto.menu.exception.ResourceNotFoundException;
import com.proyecto.menu.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaDto> obtenerTodas() {
        List<Categoria> categorias = categoriaRepository.findAll();

        return categorias.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public CategoriaDto guardar(CategoriaDto dto) {
        Categoria entidad = convertirAEntidad(dto);
        Categoria guardada = categoriaRepository.save(entidad);
        return convertirADto(guardada);
    }

    public CategoriaDto buscarPorId(UUID id) {
        Categoria entidad = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La categoria con ID: " + id + " no existe."));

        return convertirADto(entidad);
    }

    public void eliminar(UUID id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se pudo eliminar, la categoria con el ID " + id + " no existe.");
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaDto convertirADto(Categoria categoria) {
        CategoriaDto dto = new CategoriaDto();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());

        return dto;
    }

    private Categoria convertirAEntidad(CategoriaDto dto) {
        Categoria entidad = new Categoria();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());

        return entidad;
    }

}