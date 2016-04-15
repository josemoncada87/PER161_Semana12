import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
			salida = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
			salida.writeObject(new String("Saludo desde el servidor"));
			salida.flush();
			entrada = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				if(guardarImagen(recibirImagen())){
					System.out.println("imagen recibida y guardada");
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
	}

	public byte[] recibirImagen() throws IOException {
		int tam = entrada.readInt();
		byte[] buf = new byte[tam];
		/** Forma 1 - lectura secuencial */
		// int i = 0;
		// while (i < tam) {
		// buf[i] = (byte) entrada.read();
		// i++;
		// }
		/** Forma 2 - lectura completa */
		entrada.readFully(buf);
		return buf;
	}
	
	public boolean guardarImagen(byte[] buf){			
		try {
			File file = new File(app.sketchPath("") + "../data/KoalaA.jpg");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(buf);
			fos.flush();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		return false;		
	}
}
