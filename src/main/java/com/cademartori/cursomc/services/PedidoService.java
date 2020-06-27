package com.cademartori.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cademartori.cursomc.domain.ItemPedido;
import com.cademartori.cursomc.domain.PagamentoComBoleto;
import com.cademartori.cursomc.domain.Pedido;
import com.cademartori.cursomc.domain.enums.EstadoPagamento;
import com.cademartori.cursomc.repositories.ItemPedidoRepository;
import com.cademartori.cursomc.repositories.PagamentoRepository;
import com.cademartori.cursomc.repositories.PedidoRepository;
import com.cademartori.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	@Autowired
	private ProdutoService ProdServ;
	@Autowired
	private BoletoService boletoService;
	@Autowired
	private PagamentoRepository pagRepo;
	@Autowired
	private ItemPedidoRepository ipRepo;
	
	public Pedido find(Integer id) {
			Optional<Pedido> obj = repo.findById(id);
			return obj.orElseThrow(() -> new ObjectNotFoundException(
					 "Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagRepo.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(ProdServ.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		ipRepo.saveAll(obj.getItens());
		return obj;
	}

}
