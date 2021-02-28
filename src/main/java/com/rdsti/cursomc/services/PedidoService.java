package com.rdsti.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rdsti.cursomc.domain.Cliente;
import com.rdsti.cursomc.domain.ItemPedido;
import com.rdsti.cursomc.domain.PagamentoComBoleto;
import com.rdsti.cursomc.domain.Pedido;
import com.rdsti.cursomc.domain.enums.EstadoPagamento;
import com.rdsti.cursomc.repositories.ItemPedidoRepository;
import com.rdsti.cursomc.repositories.PagamentoRepository;
import com.rdsti.cursomc.repositories.PedidoRepository;
import com.rdsti.cursomc.security.UserSS;
import com.rdsti.cursomc.services.exception.AuthorizationException;
import com.rdsti.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	

	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	
	public Pedido buscar(Integer id) {
		
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		
		
		return pedido.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		
		//System.out.println(obj);
		
		//emailService.sendOrderConfirmationEmail(obj);
		
		emailService.sendOrderConfirmationHtmlEmail(obj);
		
		return obj;
	
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.find(user.getId());
		
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}

	
}
