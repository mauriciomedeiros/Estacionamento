package br.com.cleancode.core.test;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import br.com.cleancode.core.Carro;
import br.com.cleancode.core.Estacionamento;
import br.com.cleancode.exception.CarroJaEntrouException;
import br.com.cleancode.exception.CarroNaoEncontradoException;
import br.com.cleancode.exception.EntradaForaDoHorarioException;
import br.com.cleancode.exception.EstacionamentoLotadoException;
import br.com.cleancode.exception.PlacaInvalidaException;
import br.com.cleancode.exception.SaidaAntesDeEntradaException;
import br.com.cleancode.exception.SaidaForaDoHorarioException;


public class EstacionamentoTest {

	Estacionamento estacionamento;
	Carro carro;
	private final int LIMITE_VAGAS = 500;

	@Before
	public void before(){
		estacionamento = new Estacionamento(LIMITE_VAGAS, 8,22);
		carro = new Carro("AAA-1234");
	}
	
	@Test
	public void deveTer500VagasDisponiveis(){
		int vagasDisponiveis = estacionamento.getVagasDisponiveis();
		assertEquals(500, vagasDisponiveis);
	}
	
	@Test
	public void deveEntrarUmCarro(){
		estacionamento.entrarUmCarro(carro, getData(9));
		assertEquals(499, estacionamento.getVagasDisponiveis() );
	}
	
	@Test
	public void deveSairUmCarro(){
		estacionamento.entrarUmCarro(carro, getData(21));
		estacionamento.sairUmCarro(carro, getData(21));
		assertEquals(500, estacionamento.getVagasDisponiveis());
	}
	
	@Test(expected=EstacionamentoLotadoException.class)
	public void estacionamentoLotado(){
		for (int i = 1; i <= 501; i++) {
			estacionamento.entrarUmCarro(new Carro("AAA-" + String.format("%04d", i)), getData(12));
		} 
	}
	
	@Test(expected=CarroNaoEncontradoException.class)
	public void carroNaoEncontrado(){
		estacionamento.sairUmCarro(new Carro("aaa"), getData(21));
	}
	
	@Test(expected=CarroJaEntrouException.class)
	public void naoPodeEntrarCarroDuplicado(){
		estacionamento.entrarUmCarro(carro, getData(11));
		estacionamento.entrarUmCarro(carro, getData(14));
	}
	
	@Test(expected=PlacaInvalidaException.class)
	public void naoPodeEntrarCarroComPlacaInvalida(){
		Carro carro = new Carro("asdfg");
		estacionamento.entrarUmCarro(carro, getData(11));
	}
	
	@Test(expected=EntradaForaDoHorarioException.class)
	public void tentouEntrarUmCarroForaDoHorario(){
		estacionamento.entrarUmCarro(carro, getData(23));
	}

	@Test(expected=SaidaForaDoHorarioException.class)
	public void tentouSairUmCarroForaDoHorario(){
		estacionamento.entrarUmCarro(carro, getData(20));
		estacionamento.sairUmCarro(carro, getData(23));
	}
	
	@Test(expected=SaidaAntesDeEntradaException.class)
	public void umCarroNaoPodeSairAntesDeTerEntrado(){
		estacionamento.entrarUmCarro(carro, getData(20));
		estacionamento.sairUmCarro(carro, getData(18));
	}
	
	private Date getData(int hora){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hora);
		return calendar.getTime();
	}
	
}
