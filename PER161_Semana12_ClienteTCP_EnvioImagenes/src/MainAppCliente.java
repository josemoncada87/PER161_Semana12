import processing.core.PApplet;


public class MainAppCliente extends PApplet{
	
	ComunicacionCliente comCliente;
	
	@Override
	public void setup() {
		comCliente = new ComunicacionCliente(this);
		comCliente.start();
	}
	
	@Override
	public void draw() {
		background(255);
		fill(0);
		ellipse(mouseX, mouseY, 20, 20);
	}

}
