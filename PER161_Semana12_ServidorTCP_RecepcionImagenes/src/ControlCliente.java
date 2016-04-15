import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;

import processing.core.PApplet;
import processing.core.PImage;

public class ControlCliente extends Observable implements Runnable {

	private Socket s;
	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private PApplet app;

	public ControlCliente(Socket s, PApplet app) {
		this.s = s;
		this.app = app;
		try {
			System.out.println("antes");
			salida = new ObjectOutputStream(new BufferedOutputStream(
					s.getOutputStream()));
			salida.writeObject(new String("hola amiwito"));
			salida.flush();
			System.out.println("despues");
			entrada = new ObjectInputStream(new BufferedInputStream(
					s.getInputStream()));
			System.out.println("final");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				recibir();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void recibir() {
		try {
			int tam = entrada.readInt();
			System.out.println("tamaño a leer: " + tam);
			byte[] buf = new byte[tam];
			int i = 0;
			while (i < tam) {
				buf[i] = (byte) entrada.read();
				i++;
			}
			File file = new File(app.sketchPath("") + "../data/Koala8.jpg");
			FileOutputStream fos = new FileOutputStream(file);
			/*i = 0;
			while (i < tam) {
				fos.write(buf[i]);
				i++;
			}*/
			fos.write(buf);
			fos.flush();
			fos.close();
			System.out.println("Fin Control Cliente");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
