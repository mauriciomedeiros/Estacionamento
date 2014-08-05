package br.com.cleancode.core;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.cleancode.exception.CarroInvalidoException;
import br.com.cleancode.exception.CarroJaEntrouException;
import br.com.cleancode.exception.CarroNaoEncontradoException;
import br.com.cleancode.exception.EntradaForaDoHorarioException;
import br.com.cleancode.exception.EstacionamentoLotadoException;
import br.com.cleancode.exception.PlacaInvalidaException;
import br.com.cleancode.exception.SaidaAntesDeEntradaException;
import br.com.cleancode.exception.SaidaForaDoHorarioException;
import br.com.cleancode.util.CalendarUtil;

public class Estacionamento {
	private Carro[] carros;
	private final String PADRAO_PLACA = "[A-Z]{3}-[\\d]{4}";
	private int quantidadeDeCarros;
	private int horarioAbertura;
	private int horarioFechamento;
	private Map<String, Date> registroCarro;

	public Estacionamento(int numeroVagas, int horarioAbertura, int horarioFechamento) {
		this.carros = new Carro[numeroVagas];
		this.quantidadeDeCarros = 0;
		this.horarioAbertura = horarioAbertura;
		this.horarioFechamento = horarioFechamento;
		this.registroCarro = new HashMap<String, Date>();
	}

	public int getVagasDisponiveis() {
		return this.carros.length - quantidadeDeCarros;
	}

	public void entrarUmCarro(Carro carro, Date horaEntrada) throws EstacionamentoLotadoException, CarroInvalidoException, PlacaInvalidaException {
		podeEntrarCarro(carro, horaEntrada);
		carros[getNumeroVagaLivre()] = carro;
		registroCarro.put(carro.getPlaca(), horaEntrada);
		quantidadeDeCarros++;
	}

	private void podeEntrarCarro(Carro carro, Date horaEntrada) throws CarroInvalidoException, PlacaInvalidaException {
		validaHorarioEntrada(horaEntrada);
		validaPlaca(carro.getPlaca());
		carroJaEntrou(carro);
	}

	private void validaHorarioEntrada(Date horaEntrada) {
		if(!isHorarioValidio(horaEntrada))
			throw new EntradaForaDoHorarioException();
	}

	private boolean isHorarioValidio(Date horaEntrada) {
		return CalendarUtil.getInstance().getHora(horaEntrada) >= this.horarioAbertura && CalendarUtil.getInstance().getHora(horaEntrada) <= this.horarioFechamento;
	}

	private void validaPlaca(String placa) {
		if(!placa.matches(PADRAO_PLACA))
			throw new PlacaInvalidaException();
	}

	private void carroJaEntrou(Carro carro) throws CarroJaEntrouException{
		for (int i = 0; i < this.carros.length; i++)
			if(carro.equals(carros[i]))
				throw new CarroJaEntrouException();
	}

	public void sairUmCarro(Carro carro, Date horaSaida) throws CarroNaoEncontradoException {
		carros[getNumeroVaga(carro)] = null;
		podeSairCarro(carro, horaSaida);
		registroCarro.remove(carro);
		quantidadeDeCarros--;
	}

	private void podeSairCarro(Carro carro, Date horaSaida){
		validaHorarioSaida(carro.getPlaca(), horaSaida);
	}
	
	private void validaHorarioSaida(String placa, Date horaSaida) {
		if(!isHorarioValidio(horaSaida))
			throw new SaidaForaDoHorarioException();
		if(CalendarUtil.getInstance().isHoraAntes(horaSaida, registroCarro.get(placa)))
			throw new SaidaAntesDeEntradaException();
	}

	private int getNumeroVaga(Carro carro) throws CarroNaoEncontradoException{
		for (int numeroVaga = 0; numeroVaga < carros.length; numeroVaga++)
			if(carro.equals(carros[numeroVaga]))
				return numeroVaga;
		
		throw new CarroNaoEncontradoException();
	}
	
	private int getNumeroVagaLivre() throws EstacionamentoLotadoException{
		for (int numeroVaga = 0; numeroVaga < carros.length; numeroVaga++)
			if(carros[numeroVaga] == null)
				return numeroVaga;
		
		throw new EstacionamentoLotadoException();
	}
}
