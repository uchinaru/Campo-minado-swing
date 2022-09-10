package br.com.coder.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {

	private final int linhas;
	private final int colunas;
	private final int minas;

	private final List<Campo> campos = new ArrayList<>();
	private List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;

		gerarCampos();
		associarOsVizinhos();
		sortearAsMinas();
	}
	
	public void paraCada(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}

	public void registarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}

	private void notificarObservadores(boolean resultado) {

		observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}

	public void abrir(int linha, int coluna) {

		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.abrir());

	}

	private void mostrarMinas() {
		campos.stream()
		.filter(c -> c.IsMinado())
		.filter(c -> !c.IsMarcado())
		.forEach(c -> c.setAberto(true));
	}

	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.alternarMarcacao());

	}

	private void sortearAsMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.IsMinado();

		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < minas);

	}

	private void associarOsVizinhos() {
		for (Campo campo : campos) {
			for (Campo campo2 : campos) {
				campo.adicionarVizinho(campo2);
			}
		}
	}

	private void gerarCampos() {

		for (int linha = 0; linha < linhas; linha++) {
			for (int coluna = 0; coluna < colunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registrarObservador(this);
				campos.add(campo);
			}
		}
	}

	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}

	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearAsMinas();

	}
	
	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if (evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			notificarObservadores(true);
		}
	}

}
