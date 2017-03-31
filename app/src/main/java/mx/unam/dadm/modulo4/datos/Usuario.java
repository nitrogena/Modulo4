package mx.unam.dadm.modulo4.datos;

/**
 * Created by USUARIO on 29/03/2017.
 */

public class Usuario {
    private int id;
    private String correo;
    private String password;
    private String genero;
    private int foto;
    private String nombre;
    private String telefono;
    private String descripcion;

    public Usuario(){};

    public Usuario(String correo, String password, String genero, int foto, String nombre,
                   String telefono, String descripcion)
    {
        this.correo = correo;
        this.password = password;
        this.genero = genero;
        this.foto = foto;
        this.nombre = nombre;
        this.telefono = telefono;
        this.descripcion = descripcion;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }



}
