package br.nawan.socket.pratica;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Cliente extends Thread {

	private static boolean done = false;
	private Socket conexao;

	public Cliente(Socket s) {
		conexao = s;
	}

	public static void main(String[] args) throws Exception {
		Socket conexao = new Socket("127.0.0.1", 2001);
		PrintStream saida = new PrintStream(conexao.getOutputStream());

		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("Qual o seu nome? ");
		String nome = teclado.readLine();
		saida.println(nome);

		Thread thread = new Cliente(conexao);
		thread.start();
		String linha;
		while (true) {
			if (done)
				break;
			System.out.print("> ");
			linha = teclado.readLine();
			saida.println(linha);
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			String linha;
			while (true) {
				linha = entrada.readLine();
				if (linha.trim().isEmpty()) {
					System.out.println("\nConexão encerrada!!!");
					break;
				}
				System.out.println(linha);
				System.out.print(">> ");
			}
			done = true;
		} catch (Exception e) {
		}
	}
}