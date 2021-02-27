package com.rdsti.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rdsti.cursomc.domain.Cidade;
import com.rdsti.cursomc.domain.Cliente;
import com.rdsti.cursomc.domain.Endereco;
import com.rdsti.cursomc.domain.enums.Perfil;
import com.rdsti.cursomc.domain.enums.TipoCliente;
import com.rdsti.cursomc.dto.ClienteDTO;
import com.rdsti.cursomc.dto.ClienteNewDTO;
import com.rdsti.cursomc.repositories.ClienteRepository;
import com.rdsti.cursomc.repositories.EnderecoRepository;
import com.rdsti.cursomc.security.UserSS;
import com.rdsti.cursomc.services.exception.AuthorizationException;
import com.rdsti.cursomc.services.exception.DataIntegrityException;
import com.rdsti.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		Optional<Cliente> categoria = clienteRepository.findById(id);
		
		
		return categoria.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		
		obj.setId(null);
		
		obj = clienteRepository.save(obj);
		
		enderecoRepository.saveAll(obj.getEnderecos());
		
		return obj;
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
		
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), bcryptPasswordEncoder.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2()!=null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3()!=null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}

	
	private void updateData(Cliente newObj, Cliente obj) {
		
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
