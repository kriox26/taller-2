package modelo.niceland;

public class Roto extends Estado {

	protected MedioRoto arreglar(){
		return new MedioRoto();
	}
    
    public String to_string(){
        return "roto";
    }
}
