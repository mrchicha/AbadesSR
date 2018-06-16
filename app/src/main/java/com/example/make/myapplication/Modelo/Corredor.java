package com.example.make.myapplication.Modelo;

public class Corredor {

    private String dorsal;
    private String modalidad;
    private String nombre;
    private String apellidos;
    private String email;
    private String fechanacimiento;
    private String tipodocumento;
    private String dni;
    private String codigopostal;
    private String localidad;
    private String provincia;
    private String genero;
    private String telefono;
    private String federado;
    private String club;
    private String kmvertical;
    private String enmovimiento;
    private String longitud;
    private String latitud;
    private String clasificacion;


    public Corredor(){}

    public Corredor(String dorsal, String modalidad, String nombre, String apellidos, String email, String fechanacimiento, String tipodocumento, String dni, String codigopostal, String localidad, String provincia, String genero, String telefono, String federado, String club, String kmvertical, String enmovimiento, String longitud, String latitud, String clasificacion) {
        this.dorsal = dorsal;
        this.modalidad = modalidad;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.fechanacimiento = fechanacimiento;
        this.tipodocumento = tipodocumento;
        this.dni = dni;
        this.codigopostal = codigopostal;
        this.localidad = localidad;
        this.provincia = provincia;
        this.genero = genero;
        this.telefono = telefono;
        this.federado = federado;
        this.club = club;
        this.kmvertical = kmvertical;
        this.enmovimiento = enmovimiento;
        this.longitud = longitud;
        this.latitud = latitud;
        this.clasificacion = clasificacion;
    }

    // Getter

    public String getDorsal() {
        return dorsal;
    }

    public String getModalidad() {
        return modalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public String getDni() {
        return dni;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getGenero() {
        return genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFederado() {
        return federado;
    }

    public String getClub() {
        return club;
    }

    public String getKmvertical() {
        return kmvertical;
    }

    // Setter

    public void setDorsal(String dorsal) {
        this.dorsal = dorsal;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFederado(String federado) {
        this.federado = federado;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setKmvertical(String kmvertical) {
        this.kmvertical = kmvertical;
    }

    public String getEnmovimiento() {
        return enmovimiento;
    }

    public void setEnmovimiento(String enmovimiento) {
        this.enmovimiento = enmovimiento;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud(String latitud) {
        return this.latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getLatitud() {
        return latitud;
    }

    @Override
    public String toString() {
        return "Corredor{" +
                "dorsal='" + dorsal + '\'' +
                ", modalidad='" + modalidad + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", fechanacimiento='" + fechanacimiento + '\'' +
                ", tipodocumento='" + tipodocumento + '\'' +
                ", dni='" + dni + '\'' +
                ", codigopostal='" + codigopostal + '\'' +
                ", localidad='" + localidad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", genero='" + genero + '\'' +
                ", telefono='" + telefono + '\'' +
                ", federado='" + federado + '\'' +
                ", club='" + club + '\'' +
                ", kmvertical='" + kmvertical + '\'' +
                ", enmovimiento='" + enmovimiento + '\'' +
                ", longitud='" + longitud + '\'' +
                ", latitud='" + latitud + '\'' +
                ", clasificacion='" + clasificacion + '\'' +
                '}';
    }


}
