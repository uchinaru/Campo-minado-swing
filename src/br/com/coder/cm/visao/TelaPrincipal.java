package br.com.coder.cm.visao;

import javax.swing.JFrame;

import br.com.coder.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame {

	public TelaPrincipal() {
		
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 15);
		add(new PainelTabuleiro(tabuleiro));
		
		setTitle(" CAMPO MINADO ");
		setVisible(true);
		setSize(500,300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		
		new TelaPrincipal();
		
	}
}
