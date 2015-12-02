package controlador;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import excepciones.InvalidMoveException;
import grafica.dinamica.objetos.LadrilloView;
import grafica.dinamica.personajes.FelixView;
import grafica.dinamica.personajes.RalphView;
import grafica.menu.Configuracion;
import grafica.menu.Grafica;
import grafica.menu.Instrucciones;
import grafica.menu.MainMenu;
import grafica.menu.Play;
import grafica.menu.TopScores;
import grafica.niceland.IrrompibleView;
import grafica.niceland.PuertaView;
import grafica.niceland.SemiCircularView;
import grafica.niceland.SimpleView;
import grafica.niceland.VentanaView;
import grafica.obstaculos.MacetaView;
import grafica.obstaculos.MolduraView;
import modelo.dinamica.Posicion;
import modelo.direcciones.Direccion;
import modelo.main.Main;
import modelo.niceland.Irrompible;
import modelo.niceland.Niceland;
import modelo.niceland.Puerta;
import modelo.niceland.Semicircular;
import modelo.niceland.Simple;
import modelo.niceland.Ventana;

public class Controlador extends TimerTask{
    private Grafica view;
    private FelixView fView;
    private RalphView rView;
    private List<LadrilloView> ladrillos = new ArrayList<LadrilloView>();
    private Main model;
    private static MainMenu MENU = new MainMenu();
    private Map<String, BufferedImage> imagenes = new TreeMap<String, BufferedImage>();
    private VentanaView[][] edificio;
    private int cont = 0;
    
    public void run(){
    	if(!this.model.gameOver()){
        	this.model.jugarUnTurno();
        	if (cont % 25 == 0){
        		ladrillos.add(new LadrilloView(imagenes.get("ladrillo_der.png"), this.model.getDvp().getRalph().getPosicion().getColumna(), 30));
        	}
        	cont++;
            actualizarLadrillos();
        	actualizarPersonajes();
        	this.view.cargarNiceland(edificio, imagenes, fView, rView);
            this.view.graficarLadrillos(ladrillos);
    	}else{
    		this.cancel();
    	}
    }
    
    private void actualizarLadrillos(){
        for(LadrilloView ladrillo : ladrillos){
            ladrillo.actualizar();
            if(ladrillo.getOffsetY() < 0)
            	ladrillo = null;
        }
//        for(Objeto obj : this.model.getColeccionDeObjetos()){
//            
//        }
    }

    private void actualizarPersonajes(){
    	rView.setOffsetX(this.model.getDvp().getRalph().getPosicion().getColumna());
    }
    
    
    public Controlador(){
    }
    
    public Controlador(Main model, int nivel){
    	this.rView = new RalphView(imagenes.get("u_standing_fury_2.png"), 0, 0);
        this.model= model;
        cargarImagenes();
        edificio = new VentanaView[(nivel * 3) * 3][5];
        crearEdificio(nivel);
        MENU.addMouseEvents(new ManejaPlayAdapter(), new ManejaTopScoresAdapter(), 
        new ManejaInstrucciones(), new ManejaConfiguracion());
    }
    
    private void cargarImagenes(){
        File carpeta = new File("src/grafica/imagenes/");
        File[] lista = carpeta.listFiles();
        cargaRecursiva(lista);
    }
    
    private void cargaRecursiva(File[] lista){
        for(File act : lista){
            if(act.isDirectory()){
                cargaRecursiva(act.listFiles());
            }else if(act.getName().endsWith(".png") || act.getName().endsWith(".jpg")){
                try{
                    BufferedImage imagen = ImageIO.read(act);
                    imagenes.put(act.getName(), imagen);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void crearEdificio(int nivel){
        Niceland building = this.model.getNiceland();
        int n = 0;
        for (int i = 0; i < (nivel * 3) ; i++) {
            Ventana[][] ventanas = building.getSecciones()[i].getVentanas();
            for (int k = 0; k < 3; k++) {
                for (int j = 0; j < 5; j++) {
                    Ventana act = ventanas[k][j];
                    if(act instanceof Irrompible){
                    	edificio[n][j] = generarViewIrrompible(act, j + 1, k + 1);
                    } else if(act instanceof Simple){
                    	edificio[n][j] = generarViewSimple(act, j + 1, k + 1);
                    } else if(act instanceof Puerta){
                    	edificio[n][j] = generarViewPuerta(act, j + 1, k + 1);
                    } else if(act instanceof Semicircular){
                    	edificio[n][j] = generarViewSemiCircular(act, j + 1, k + 1);
                    }
                }
                n++;
            }
        }
    }
    
    public IrrompibleView generarViewIrrompible(Ventana ven, int x, int y){
        switch (ven.getObstaculo().getDireccion()) {
            case DERECHA: return new IrrompibleView(imagenes.get("irrompible_derecha.png"));
            
            case IZQUIERDA: return new IrrompibleView(imagenes.get("irrompible_izquierda.png"));
            
            default: return new IrrompibleView(imagenes.get("irrompible_cerrada.png"));
        }
    }
    
    public SimpleView generarViewSimple(Ventana ven, int x, int y){
        SimpleView act = new SimpleView();
        switch (ven.estadoTotal()) {
            case 0: act = new SimpleView(imagenes.get("simple.png"));// Esta arreglada
            break;
            case 1: act = new SimpleView(imagenes.get("media_rota_abajo.png")); // La ventana esta media rota abajo
            break;
            case 2: act = new SimpleView(imagenes.get("media_rota_ambos.png")); // La ventana esta media rota en ambos
            break;
            case 3: act = new SimpleView(imagenes.get("media_rota_arriba.png"));// La ventana media rota arriba
            break;
            case 4: act = new SimpleView(imagenes.get("rota_arriba.png"));// La ventana esta rota arriba nada mas
            break;
            case 5: act = new SimpleView(imagenes.get("rota_abajo.png"));// La ventana esta rota abajo nada mas
            break;
            case 6: act = new SimpleView(imagenes.get("rota_ambos.png"));// La ventana esta completamente rota
            break;
            default: act = new SimpleView(imagenes.get("simple.png"));
            break;
        }
        switch (ven.getTipoObstaculo()) {
            case "moldura": act.setObstaculoView(new MolduraView(imagenes.get("moldura.png"))); // Falta calcular el offset x e Y
            break;
            case "maceta": act.setObstaculoView(new MacetaView(imagenes.get("macetero.png"))); // Falta calcular el offset x e y
            break;
            default: break; // nada
        }
        return act;
    }
    
    public PuertaView generarViewPuerta(Ventana ven, int x, int y){
        PuertaView act = new PuertaView();
        switch (ven.estadoTotal()) {
            case 0: act = new PuertaView(imagenes.get("puerta-sana.png"));
            break;
            case 1: act = new PuertaView(imagenes.get("puerta-abierta.png"));
            break;
            case 2: act = new PuertaView(imagenes.get("puerta-rota-abajo.png"));
            break;
            case 3: act = new PuertaView(imagenes.get("puerta-rota-derecha.png"));
            break;
            case 4: act = new PuertaView(imagenes.get("puerta-rota-salvoUI"));
            break;
            case 5: act = new PuertaView(imagenes.get("puerta-rota-salvoUD.png"));
            break;
            case 6: act = new PuertaView(imagenes.get("puerta-rota-diagD.png"));
            break;
            case 7: act = new PuertaView(imagenes.get("puerta-rota-UI.png"));
            break;
            case 8: act = new PuertaView(imagenes.get("puerta-rota-UD.png"));
            break;
            case 9: act = new PuertaView(imagenes.get("puerta-rota-DI.png"));
            break;
            case 10: act = new PuertaView(imagenes.get("puerta-rota-DD.png"));
            break;
            case 11: act = new PuertaView(imagenes.get("puerta-rota-salvoDI.png"));
            break;
            case 12: act = new PuertaView(imagenes.get("puerta-rota-salvoDD.png"));
            break;
            default: act = new PuertaView(imagenes.get("puerta-sana.png"));
            break;
        }
        return act;
    }
    
    public SemiCircularView generarViewSemiCircular(Ventana ven, int x, int y){
        return new SemiCircularView(imagenes.get("semi-circular.png")); // Falta implementar
    }
    
    public Grafica getView(){
        return this.view;
    }
    
    public void setView(Grafica view){
        this.view = view;
    }
    
    public Main getModel(){
        return this.model;
    }
    
    /*Creo que de esta forma se deberia manejar con el Controlador. Cualquier cosa si ven
    * que se puede cambiar lo que sea para hacerlo mejor, mporque no se si les va a parecer
    * un lio de clases, pero bueno jaj. Lo que va a relacionar el modelo con la grafica
    * va a ser el Boton Play, que ahi cree la clase, pero no mas que eso por cuestiones
    * que se puede relacionar de muchas formas y no se como empezar.
    */
    public static void main (String[] args){
        @SuppressWarnings("unused")
		Controlador ctrl = new Controlador(new Main(false, 1), 1);
    }
    
    class ManejaEventosTeclado extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            int movek = e.getKeyCode();
            Posicion pos = model.getDvp().getFelix().getPosicion();
            try{
                switch(movek){
                    case KeyEvent.VK_UP:		//Arriba
                    	model.getDvp().getFelix().mover(Direccion.ARRIBA, model.getNiceland().getVentana(pos));
                    	fView.setImagenActual(imagenes.get("a_standing_basic.png"));
                    	break;
                    case KeyEvent.VK_DOWN:		//Abajo
                    	model.getDvp().getFelix().mover(Direccion.ABAJO, model.getNiceland().getVentana(pos));
                    	fView.setImagenActual(imagenes.get("a_standing_basic.png"));
                    	break;
                    case KeyEvent.VK_LEFT: //Izquierda
                    	model.getDvp().getFelix().mover(Direccion.IZQUIERDA, model.getNiceland().getVentana(pos));
                    	fView.setImagenActual(imagenes.get("a_runNoAxe_left_1.png"));
                    	break;
                    case KeyEvent.VK_RIGHT: //Derecha
                    	model.getDvp().getFelix().mover(Direccion.DERECHA, model.getNiceland().getVentana(pos));
                    	fView.setImagenActual(imagenes.get("a_runNoAxe_right_1.png"));
                    	break;
                    case KeyEvent.VK_SPACE: //Barra espaciadora
                    	model.getDvp().getFelix().martillar(model.getNiceland().getVentana(pos));
                    	model.getDvp().getFelix().martillar(model.getNiceland().getVentana(pos));
                    	model.getDvp().getFelix().martillar(model.getNiceland().getVentana(pos));
                    	model.getDvp().getFelix().martillar(model.getNiceland().getVentana(pos));
                        edificio[pos.getSeccion() + pos.getFila()][pos.getColumna()] = actualizarVentana(model.getNiceland().getVentana(pos), pos);
                    	fView.setImagenActual(imagenes.get("felix_martillar_izquierda.png"));
                    default: 
                    	break;
                }
            }catch(InvalidMoveException exc){
            	System.out.println(exc.getMessage());
            }
            fView.setOffsetX(pos.getColumna());
            fView.setOffsetY(pos.getFila() + pos.getSeccion());
        }
    }
    
    public VentanaView actualizarVentana(Ventana ven, Posicion pos){
        if (ven instanceof Simple) {
            return generarViewSimple(ven, 0, 0);
        }
        if(ven instanceof Puerta){
        	return generarViewPuerta(ven, 0,0);
        }
        if(ven instanceof Semicircular){
        	return generarViewSemiCircular(ven, 0, 0);
        }
       	return edificio[pos.getSeccion()+pos.getFila()][pos.getColumna()];
    }
    
    class ManejaPlayAdapter extends MouseAdapter{
        public void mouseClicked(MouseEvent e){
            MENU.turnOff();
            fView = new FelixView(imagenes.get("a_standing_basic.png"), 2, 0);
//            ctrlLadrillos = new ControladorLadrillo(imagenes);
            cargarView();
            view.addKeyboardEvents(new ManejaEventosTeclado());
//            view.addBackMenu(new VolverAMenu());
//            ejecutarTimer();
        }
    }

    public void ejecutarTimer(){
    	System.out.println("Adentro de ejecutartimer");
    	Timer timer = new Timer("Turnos");
    	timer.schedule(this, 0, 1);
    }

    public void cargarView(){
    	view = new Play(this, edificio, imagenes, fView, rView);
//    	this.ctrlLadrillos.setGameWindow(view);
    }
    
    
    class VolverAMenu extends MouseAdapter{
        public void mouseClicked(MouseEvent e){
            view.turnOff();
            MENU.turnOn();
        }
    }
    class ManejaTopScoresAdapter extends MouseAdapter{
        public void mouseClicked(MouseEvent e){
            MENU.turnOff();
            view = new TopScores();
            view.addBackMenu(new VolverAMenu());
        }
    }
    class ManejaInstrucciones extends MouseAdapter{
        public void mouseClicked(MouseEvent e){
            MENU.turnOff();
            view = new Instrucciones();
            view.addBackMenu(new VolverAMenu());
        }
    }
    class ManejaConfiguracion extends MouseAdapter{
        public void mouseClicked(MouseEvent e){
            view = new Configuracion();
            view.addBackMenu(new VolverAMenu());
        }
    }
    
}
