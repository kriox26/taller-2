package grafica.menu;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import modelo.main.Main;

public class Play extends Grafica {
	private String imgPath = "src/grafica/niceland/";
	private String backgroundImage = imgPath + "FondoNiceland.jpg";
	private JLabel fondo;
	private Map<String, BufferedImage> imgs = new TreeMap<String, BufferedImage>();

	/*
	 * Se crean todas las imagenes de Niceland, ventanas con obstaculos
	 * todo.
	 */
	public Play(Main main){
		cargarImagenes();
		File imgFondo = new File(backgroundImage);
		JLabel fondo = new JLabel(new ImageIcon(imgFondo.getAbsolutePath()));
		add(goBack);
		setContentPane(fondo);
		setSize(919, 720);
		cargarNiceland();
		setVisible(true);
	}


	public void addKeyboardEvents(KeyAdapter keyadapter){
		this.addKeyListener(keyadapter);
	}

	public void cargarNiceland(){
		BufferedImage img = imgs.get("edificio_150_seccion1.png");
		System.out.println("Imagen: " + img);
		this.getGraphics().drawImage(imgs.get("edificio_150_seccion1.png"), 20, 0, null);
	}

	public void paintComponents(Graphics g){
		super.paintComponents(g);
	}

	private void cargarImagenes(){
		File folder = new File("src/grafica/imagenes/edificio/");
		File[] filesList = folder.listFiles();

		for(File img : filesList){
			try {
				BufferedImage imagen = ImageIO.read(img);
				System.out.println("El nombre es:" + img.getName());
				this.imgs.put(img.getName(), imagen);
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

}
