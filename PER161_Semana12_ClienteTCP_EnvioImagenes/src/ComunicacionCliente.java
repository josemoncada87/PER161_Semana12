import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import processing.core.PApplet;

public class ComunicacionCliente extends Thread {

	private Socket s;
	private ObjectInputStream entrada;
	private ObjectOutputStream salida;
	private PApplet app;

	public ComunicacionCliente(PApplet app) {
		System.out.println("inciando cliente com");
		this.app = app;
		try {
			System.out.print("Conexión con el servidor ...");
			s = new Socket(InetAddress.getByName("127.0.0.1"), 6001);
			System.out.println("OK");
			
			System.out.print("Creación flujo de salida ...");
			salida = new ObjectOutputStream(new BufferedOutputStream(
					s.getOutputStream()));
			System.out.println("OK");

			System.out.print("Creación flujo de entrada ...");
			entrada = new ObjectInputStream(new BufferedInputStream(
					s.getInputStream()));
			System.out.println("OK");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			recibirSaludo();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				enviarImagen(app.sketchPath("") + "../data/Koala.jpg");
				break;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void recibirSaludo() throws IOException {
		byte[] buf = new byte[512];
		String recibido = "Nan";
		try {
			recibido = (String) entrada.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("recibí: " + recibido);
	}

	private void enviarImagen(String path) throws FileNotFoundException {
		try {
			File file = new File(path);
			//System.out.println(file.getAbsolutePath());
			FileInputStream fis = new FileInputStream(file);
			int tam = fis.available();
			salida.writeInt(tam);
			byte[] buf = new byte[tam];
			System.out.println(fis.read(buf) == tam ? "carga completa"
					: "no se cargó la imagen");
			fis.close();
			// Forma 1 - envio secuencial
			// int i = 0;
			// while (i < tam) {
			//		salida.write((byte)buf[i]);
			//		i++;
			// }
			// Forma 2 - envío único
			salida.write(buf);			
			salida.flush();
			System.out.println("Imagen Enviada");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
