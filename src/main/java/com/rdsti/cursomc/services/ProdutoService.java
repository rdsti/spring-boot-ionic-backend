package com.rdsti.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rdsti.cursomc.domain.Categoria;
import com.rdsti.cursomc.domain.Produto;
import com.rdsti.cursomc.repositories.CategoriaRepository;
import com.rdsti.cursomc.repositories.ProdutoRepository;
import com.rdsti.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto find(Integer id) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		
		
		return produto.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
	}
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		
		//return produtoRepository.search(nome, categorias, pageRequest);
		
		return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
		
	}
	
}
