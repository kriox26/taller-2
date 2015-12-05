package modelo.dinamica.objetos;

import modelo.direcciones.Direccion;
import modelo.dinamica.Posicion;

/**
 * La clase Ladrillo, más allá de la diferenciación gráfica, solo
 * se distinge de las demás de tipo Objeto porque su único
 * movimiento posible es en sentido vertical hacia abajo.
 *
 * @author  Santiago Marrone
 * @see modelo.dinamica.direcciones
 */
public class Ladrillo extends Objeto {
    private int velocidad = 1;
    private int vaCayendo = 30;
    private boolean golpeado = false;

	public Ladrillo(){
		Posicion posicion = new Posicion();
		super.setPosicion(posicion);
		this.setGolpeado(false);
	}

	public Ladrillo(Posicion posicion){
		posicion.setSeccion(0);
		posicion.setFila(2);
		super.setPosicion(posicion);
	}

	public Ladrillo(int s, int f, int c){
		Posicion p = new Posicion(2,c,s - 1);
		super.setPosicion(p);
	}

	/**
	 * El método caerDesde identifica la caída de un ladrillo: movimiento
	 * desde una posición dada en sentido vertical, siempre hacia abajo
	 *
	 * @param     position	desde dónde es lanzado el ladrillo (provisto por Ralph)
	 */
	public void caerDesde (Posicion position) {
		this.unidireccionar(Direccion.ABAJO); // Unidireccionarlo hacia abajo (en niveles Pro podr�a alterarse la gravedad)
	}
    
    public void mover(Direccion direccion){
        if (this.getPosicion().getSeccion() == 0) {
            if (this.getPosicion().getFila() > 0) {
                super.mover(direccion);
            }
        }
    }
    
    private void caer(){
        if (this.vaCayendo > 0) {
            vaCayendo -= velocidad;
            if (vaCayendo / 10 > 0) {
                System.out.println("Antes de setFila en caer ladrillo");
                this.getPosicion().setFila(vaCayendo / 10);
            }
        }else{
            this.getPosicion().setFila(0);
        }
    }
    
    /*
     * Metodo utilizado para actualizar la posicion del ladrillo. Lo tiene heredado
     * de la clase Objeto.
     */
    public void actualizar(){
        caer();
    }

	public boolean isGolpeado() {
		return golpeado;
	}

	public void setGolpeado(boolean golpeado) {
		this.golpeado = golpeado;
	}
}
