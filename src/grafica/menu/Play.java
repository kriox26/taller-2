package grafica.menu;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.swing.JPanel;

import controlador.Controlador;
import grafica.dinamica.objetos.LadrilloView;
import grafica.dinamica.personajes.FelixView;
import grafica.dinamica.personajes.RalphView;
import grafica.niceland.VentanaView;

public class Play extends Grafica {
	private static final long serialVersionUID = 1L;
	private static final int horEdificio= 250;
    private static final int verEdificio= 120;
    private JPanel panel = new PanelJuego();
    private BufferedImage seccionActual;
    private Map<String, BufferedImage> imagenes;
   
    
    /*
    * Se crean todas las imagenes de Niceland, ventanas con obstaculos
    * todo.
    */
    public Play(Controlador ctrl, VentanaView[][] building, Map<String, BufferedImage> imagenes, FelixView felix, RalphView rView){
    	//panel.setVisible(true);
    	this.setResizable(false);
    	this.setVisible(true);
    	this.seccionActual = imagenes.get("piso1.png");
    	this.imagenes = imagenes;
    	this.add(panel);
        setSize(800, 600);
        //this.getGraphics().drawImage(imagenes.get("grass_background.jpg"), 0,0,null);
    	Timer timer = new Timer("Turnos");
    	timer.schedule(ctrl, 0, 100);
    }

    public void graficarLadrillos(List<LadrilloView> ladrillos){
        for(LadrilloView ladrillo : ladrillos){
        	if(!ladrillo.getOculto()){
        		System.out.println("Ladrillo esta en: " + (verEdificio + (230 - 4 * ladrillo.getOffsetY() - 20)));
            	panel.getGraphics().drawImage(ladrillo.getImagenActual(), (-10) + horEdificio + (54 * (ladrillo.getOffsetX() + 1)), verEdificio + (230 - 4*ladrillo.getOffsetY()), null);
        	}
        }
    }
    
    public void addKeyboardEvents(KeyAdapter keyadapter){
        this.addKeyListener(keyadapter);
    }

    public void setSeccionActual(BufferedImage img){
    	this.seccionActual = img;
    }

    public void cargarNiceland(VentanaView[][] building, FelixView felix, RalphView rView, int desde, int hasta){
        panel.getGraphics().drawImage(this.seccionActual, horEdificio, verEdificio, null);
//        this.getGraphics().drawImage(imagenes.get("piso2.png"), horEdificio, verEdificio - 330, null);
        // Despues dibujamos ventanas sobre las secciones
        int alturaActual = 0;
        int actualX = 0;
        for (int i = desde; i < hasta; i++) {
            for (int j = 0; j < 5; j++) {
            	VentanaView act = building[i][j];
            	int venX = 10 + horEdificio + actualX + act.getXinicial();
            	int venY = verEdificio + (230 - (alturaActual + act.getYinicial()));
                panel.getGraphics().drawImage(act.getImagenActual(), venX, venY, null);
                if(act.tieneObstaculo()){
                	panel.getGraphics().drawImage(act.getObstaculoView().getImagenActual(), venX + act.getObstaculoView().getOffsetX(), venY + act.getObstaculoView().getOffsetY(), null);
                }
            	if(felix.getOffsetX() == j && felix.getOffsetY() == i){
            		panel.getGraphics().drawImage(felix.getImagenActual(), venX + act.getAjusteX(), venY + act.getAjusteY(), null);
            	}
                actualX += 54;
            }
            if (i == 2 || i == 5 || i == 8) {
            	alturaActual += 90;
            }else{
                alturaActual += 70;
            }
            actualX = 0;
        }
        panel.getGraphics().drawImage(imagenes.get("u_standing_fury_2.png"), horEdificio + (54 * rView.getOffsetX() + 1), verEdificio + (230 - alturaActual - 20), null);
    }
        
   // public void paintComponent(Graphics g){
   // 	this.paintComponent(g);
   //   }
    
}
