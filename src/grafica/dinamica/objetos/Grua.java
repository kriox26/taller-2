package grafica.dinamica.objetos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import modelo.direcciones.Direccion;
import grafica.dinamica.GGO;

public class Grua extends GGO {
    private BufferedImage gruaHaciaI, gruaHaciaD; // I: izquierda D: derecha
    private Direccion direccion;



    private void setDireccion (Direccion dir) {
      this.direccion = dir;
    }

    private Direccion getDireccion () {
      return this.direccion;
    }

    public Grua(int hParam, Direccion direccion) { // hParam: parametro de ubicacion horizontal
        this.initGrua(hParam, direccion)
    }

    private void initGrua(int hParam, Direccion direccion) {
      this.setIP("src/grafica/imagenes/grua/");
      this.uploadImages();
      this.setY(0); // La  grua siempre en el piso
      this.setX(hParam);
      this.setDireccion(direccion);
    }

    private void uploadImages () {
        try {
            if (gruaHaciaI == null) {
                gruaHaciaI = this.up("¡!")); // Falta convertir a .png la imagen de la grua
            }
            if (gruaHaciaD == null) {
                gruaHaciaD = this.up("¡!")); // Falta convertir a .png la imagen de la grua
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }


}
