package com.rdsti.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rdsti.cursomc.domain.Cliente;
import com.rdsti.cursomc.repositories.ClienteRepository;
import com.rdsti.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository categoriaRepository;

	public Cliente buscar(Integer id) {
		
		Optional<Cliente> categoria = categoriaRepository.findById(id);
		
		
		return categoria.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
	}
	
}
