import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import processing.core.PImage;

public class ComunicacionServidor extends Thread implements Observer {

	private ArrayList<ControlCliente> clientes;
	private ServerSocket ss;
	private PApplet app;

	public ComunicacionServidor(PApplet app) {
		this.app = app;
		clientes = new ArrayList<>();
		try {
			ss = new ServerSocket(6001);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				recibirClientes();
				sleep(100);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metodo encargado de recibir a los clientes, agregarlos a la lista de clientes e
	 * iniciar el hilo del nuevo, es llamado desde el metodo run
	 * 
	 * @exception IOException en caso de no poder aceptar al cliente en el {@link Socket} 
	 * */
	private void recibirClientes() throws IOException {
		Socket s = ss.accept();
		ControlCliente nuevo = new ControlCliente(s, app);
		Thread controlCliente = new Thread(nuevo);
		controlCliente.start();
		clientes.add(nuevo);
	}

	@Override
	public void update(Observable arg0, Object arg1) {

	}

}
