package cr.ic.jmoraroditcr.firebase_android;

/**
 * Created by josem on 24/5/2018.
 */

public class Item {
    private String Nombre;
    private String Precio;
    private String Foto;
    private String Descripcion;
    private String ID_Usuario;
    public Item(String Descripcion,String Foto, String ID_Usuario, String Nombre, String Precio) {
        this.Nombre = Nombre;
        this.Precio = Precio;
        this.Foto = Foto;
        this.Descripcion = Descripcion;
        this.ID_Usuario = ID_Usuario;
    }

    public Item(){}


    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        this.Precio = precio;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        this.Foto = foto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    public String getID_Usuario() {
        return ID_Usuario;
    }

    public void setID_Usuario(String ID_Usuario) {
        this.ID_Usuario = ID_Usuario;
    }
}
