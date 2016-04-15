import processing.core.PApplet;
import processing.core.PImage;

public class MainAppServidor extends PApplet {

	ComunicacionServidor com;
	PImage img;

	@Override
	public void setup() {
		System.out.print("[INICIANDO ... ");
		com = new ComunicacionServidor(this);
		com.start();
		System.out.println("OK]");
	}

	@Override
	public void draw() {
		/*background(255);
		fill(0);
		ellipse(mouseX, mouseY, 20, 20);*/
	}

}
