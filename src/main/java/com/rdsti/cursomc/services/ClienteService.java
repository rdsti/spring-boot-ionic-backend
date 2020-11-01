package com.rdsti.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rdsti.cursomc.domain.Cliente;
import com.rdsti.cursomc.dto.ClienteDTO;
import com.rdsti.cursomc.repositories.ClienteRepository;
import com.rdsti.cursomc.services.exception.DataIntegrityException;
import com.rdsti.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente find(Integer id) {
		
		Optional<Cliente> categoria = clienteRepository.findById(id);
		
		
		return categoria.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
	}
	
	
	public Cliente insert(Cliente obj) {
		
		obj.setId(null);
		
		return clienteRepository.save(obj);
		
	}
	
	public Cliente update(Cliente obj) {

		Cliente objNew = find(obj.getId());
		
		updateData(objNew, obj);
		
		return clienteRepository.save(objNew);
		
	}
	
	public void delete(Integer id) {
		
		find(id);
		
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um Cliente que possuiu Enttidades relacionadas.");
		}
		
	}
	
	public List<Cliente> findAll() {
		
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}

	
	private void updateData(Cliente newObj, Cliente obj) {
		
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
