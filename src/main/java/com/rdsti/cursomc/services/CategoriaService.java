package com.rdsti.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rdsti.cursomc.domain.Categoria;
import com.rdsti.cursomc.dto.CategoriaDTO;
import com.rdsti.cursomc.repositories.CategoriaRepository;
import com.rdsti.cursomc.services.exception.DataIntegrityException;
import com.rdsti.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria find(Integer id) {
		
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		
		
		return categoria.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
	}
	
	public Categoria insert(Categoria obj) {
		
		obj.setId(null);
		
		return categoriaRepository.save(obj);
		
	}
	
	public Categoria update(Categoria obj) {

		find(obj.getId());
		
		return categoriaRepository.save(obj);
		
	}
	
	public void delete(Integer id) {
		
		find(id);
		
		try {
			categoriaRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma Categoria que possuiu Produtos.");
		}
		
	}
	
	public List<Categoria> findAll() {
		
		return categoriaRepository.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return categoriaRepository.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDto) {
		
		
		return new Categoria(objDto.getId(), objDto.getNome());
	}

}
