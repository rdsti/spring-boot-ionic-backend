package com.rdsti.cursomc.services;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rdsti.cursomc.domain.Categoria;
import com.rdsti.cursomc.domain.Cidade;
import com.rdsti.cursomc.domain.Cliente;
import com.rdsti.cursomc.domain.Endereco;
import com.rdsti.cursomc.domain.Estado;
import com.rdsti.cursomc.domain.ItemPedido;
import com.rdsti.cursomc.domain.Pagamento;
import com.rdsti.cursomc.domain.PagamentoComBoleto;
import com.rdsti.cursomc.domain.PagamentoComCartao;
import com.rdsti.cursomc.domain.Pedido;
import com.rdsti.cursomc.domain.Produto;
import com.rdsti.cursomc.domain.enums.EstadoPagamento;
import com.rdsti.cursomc.domain.enums.Perfil;
import com.rdsti.cursomc.domain.enums.TipoCliente;
import com.rdsti.cursomc.repositories.CategoriaRepository;
import com.rdsti.cursomc.repositories.CidadeRepository;
import com.rdsti.cursomc.repositories.ClienteRepository;
import com.rdsti.cursomc.repositories.EnderecoRepository;
import com.rdsti.cursomc.repositories.EstadoRepository;
import com.rdsti.cursomc.repositories.ItemPedidoRepository;
import com.rdsti.cursomc.repositories.PagamentoRepository;
import com.rdsti.cursomc.repositories.PedidoRepository;
import com.rdsti.cursomc.repositories.ProdutoRepository;

@Service
public class DBConfigService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	public void instantiateTesteDataBase() throws Exception {
		
		Categoria categoria1 = new Categoria(null,"Informática");
		Categoria categoria2 = new Categoria(null,"Escritório");
		Categoria categoria3 = new Categoria(null,"Eletrônicos");
		Categoria categoria4 = new Categoria(null,"Cama Mesa e Banho");
		Categoria categoria5 = new Categoria(null,"Jardinagem");
		Categoria categoria6 = new Categoria(null,"Decoração");
		Categoria categoria7 = new Categoria(null,"Perfumaria");
		
		categoriaRepository.saveAll(Arrays.asList(categoria1, categoria2, categoria3, categoria4, categoria5, categoria6, categoria7));
		
		Produto p1 = new Produto(null,"Computador",2000.00);
		Produto p2 = new Produto(null,"Impressora",800.00);
		Produto p3 = new Produto(null,"Mouse",80.00);
		Produto p4 = new Produto(null,"Mouse",300.00);
		Produto p5 = new Produto(null,"Mouse",50.00);
		Produto p6 = new Produto(null,"Mouse",200.00);
		Produto p7 = new Produto(null,"Mouse",1200.00);
		Produto p8 = new Produto(null,"Mouse",800.00);
		Produto p9 = new Produto(null,"Mouse",100.00);
		Produto p10 = new Produto(null,"Mouse",180.00);
		Produto p11 = new Produto(null,"Mouse",90.00);
		
		categoria1.getProdutos().addAll(Arrays.asList(p1, p2 ,p3));
		categoria2.getProdutos().addAll(Arrays.asList(p2,p4));
		categoria4.getProdutos().addAll(Arrays.asList(p5,p6));
		categoria4.getProdutos().addAll(Arrays.asList(p1,p2,p3,p7));
		categoria5.getProdutos().addAll(Arrays.asList(p8));
		categoria6.getProdutos().addAll(Arrays.asList(p9,p10));
		categoria7.getProdutos().addAll(Arrays.asList(p11));
		
		p1.getCategorias().addAll(Arrays.asList(categoria1,categoria4));
		p2.getCategorias().addAll(Arrays.asList(categoria1,categoria2,categoria4));
		p3.getCategorias().addAll(Arrays.asList(categoria1,categoria4));
		p4.getCategorias().addAll(Arrays.asList(categoria2));
		p5.getCategorias().addAll(Arrays.asList(categoria3));
		p6.getCategorias().addAll(Arrays.asList(categoria3));
		p7.getCategorias().addAll(Arrays.asList(categoria4));
		p8.getCategorias().addAll(Arrays.asList(categoria5));
		p9.getCategorias().addAll(Arrays.asList(categoria6));
		p10.getCategorias().addAll(Arrays.asList(categoria6));
		p11.getCategorias().addAll(Arrays.asList(categoria7));
				
		produtoRepository.saveAll(Arrays.asList(p1, p2 ,p3));
		
		Estado est1 = new Estado(null,"Minas Gerais");
		Estado est2 = new Estado(null,"São Paulo");
		
		Cidade c1 = new Cidade(null,"Uberlândia", est1);
		Cidade c2 = new Cidade(null,"São Paulo", est2);
		Cidade c3 = new Cidade(null,"Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "rodrigo.siqueira@rdsti.com.br", "78945612307", TipoCliente.PESSOAFISICA, bcryptPasswordEncoder.encode("123"));
		cli1.getTelefones().addAll(Arrays.asList("11993217878","11997894561"));
		cli1.addPerfil(Perfil.CLIENTE);
		
		Cliente cli2 = new Cliente(null, "Maria Costa", "rodrigo.donizete.siqueira@gmail.com", "09555535043", TipoCliente.PESSOAFISICA, bcryptPasswordEncoder.encode("1234"));
		cli2.getTelefones().addAll(Arrays.asList("11993214444","11997895555"));
		cli2.addPerfil(Perfil.ADMIN);
		cli2.addPerfil(Perfil.CLIENTE);
				
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38224578", cli1, c2);
		Endereco e3 = new Endereco(null, "Avenida Floriano", "2106", null, "Centro", "01314000", cli2, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		cli2.getEnderecos().addAll(Arrays.asList(e3));
		
		clienteRepository.saveAll(Arrays.asList(cli1,cli2));
		enderecoRepository.saveAll(Arrays.asList(e1,e2,e3));
		
		SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2020 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2020 10:32"), cli1, e2);
		
		Pagamento pgto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pgto1);
		
		Pagamento pgto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("10/10/2020 00:00"), null);
		ped2.setPagamento(pgto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pgto1, pgto2));
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
		
	}

}
