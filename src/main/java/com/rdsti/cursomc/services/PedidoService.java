package com.rdsti.cursomc.services;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rdsti.cursomc.domain.Categoria;
import com.rdsti.cursomc.domain.ItemPedido;
import com.rdsti.cursomc.domain.Pagamento;
import com.rdsti.cursomc.domain.PagamentoComBoleto;
import com.rdsti.cursomc.domain.Pedido;
import com.rdsti.cursomc.domain.enums.EstadoPagamento;
import com.rdsti.cursomc.dto.CategoriaDTO;
import com.rdsti.cursomc.repositories.ItemPedidoRepository;
import com.rdsti.cursomc.repositories.PagamentoRepository;
import com.rdsti.cursomc.repositories.PedidoRepository;
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
	
}
