package modelo.niceland;

public class Panel {
	private Estado estado;

    public Panel(int estado){
        switch (estado) {
            case 0: this.estado = new Sano();
                    break;
            case 1: this.estado = new MedioRoto();
                    break;
            case 2: this.estado = new Roto();
                    break;
        }
    }

	public Estado getEstado(){
		return this.estado;
	}

	public void setEstado(Estado estado){
		this.estado = estado;
	}

    /*
     * Si el panel que estamos queriendo arreglar esta sano, entonces
     * devolvemos false, ya que ya no se puede arreglar. Caso contrario 
     * se devuelve true.
     * @return boolean
     *
     */
	protected boolean arreglar(){
        if (!this.estaSano()) {
            this.setEstado(this.getEstado().arreglar()); //este arreglar() es de la clase Estado
            return true;
        }else{
            return false;
        }
    }

	/*
	 * Si this.estado es una instancia de Roto, se devuelve true
	 * caso contrario se devuelve false
	 * @return boolean
	 */
	protected boolean estaRoto(){
		if(this.getEstado() instanceof Roto){
			return true;
		} else {
			return false;
		}
	}
    
    protected boolean estaSano(){
        System.out.println("El panel esta: " + this.estado.to_string());
        return (this.estado instanceof Sano);
    }
}