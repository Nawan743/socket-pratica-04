package br.nawan.socket.pratica;
  
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class Servidor extends Thread {

	private static Vector<PrintStream> clientes;
	private Socket conexao;
	private String nome;

	public Servidor(Socket s) {
		conexao = s;
	}

	public static void main(String[] args) throws Exception {
		clientes = new Vector<PrintStream>();

		try (ServerSocket s = new ServerSocket(2001)) {
			while (true) {
				System.out.println("Esperando conexão .................");
				Socket conexao = s.accept();
				System.out.println("Conectou!");
				Thread t = new Servidor(conexao);
				t.start();
			}
		} catch (Exception e) {
			System.out.println("Não foi possível se conectar ao servidor!s");
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			PrintStream saida = new PrintStream(conexao.getOutputStream());
			nome = entrada.readLine();
			if (nome == null)
				return;
			clientes.add(saida);
			String linha = entrada.readLine();

			while (linha != null && !linha.trim().isEmpty()) {
				sendToAll(saida, ": ", linha);
				linha = entrada.readLine();
			}
			sendToAll(saida, " se ", "desconectou!");
			clientes.remove(saida);
			conexao.close();
		} catch (Exception e) {
		}
	}

	public void sendToAll(PrintStream saida, String acao, String linha) {
		Enumeration<PrintStream> e = clientes.elements();
		while (e.hasMoreElements()) {
			PrintStream chat = (PrintStream) e.nextElement();

			if (chat != saida)
				chat.println(nome + acao + linha);
			if (acao.equalsIgnoreCase(" sair ")) {
				if (chat == saida)
					chat.println();
			}
		}
	}
}
