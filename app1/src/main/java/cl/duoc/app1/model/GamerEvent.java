package cl.duoc.app1.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "eventos")
public class GamerEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private LocalDate fecha;

    private LocalTime hora;

    @Column(nullable = false)
    private String ubicacion;

    private String direccion;

    private String organizadores;

    private boolean estacionamiento;

    private boolean locomocion;

    private boolean comidas;

    private boolean alojamiento;

    private String expositores;

    private boolean destacado;

    public GamerEvent() {
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getOrganizadores() {
        return organizadores;
    }

    public void setOrganizadores(String organizadores) {
        this.organizadores = organizadores;
    }

    public boolean isEstacionamiento() {
        return estacionamiento;
    }

    public void setEstacionamiento(boolean estacionamiento) {
        this.estacionamiento = estacionamiento;
    }

    public boolean isLocomocion() {
        return locomocion;
    }

    public void setLocomocion(boolean locomocion) {
        this.locomocion = locomocion;
    }

    public boolean isComidas() {
        return comidas;
    }

    public void setComidas(boolean comidas) {
        this.comidas = comidas;
    }

    public boolean isAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(boolean alojamiento) {
        this.alojamiento = alojamiento;
    }

    public String getExpositores() {
        return expositores;
    }

    public void setExpositores(String expositores) {
        this.expositores = expositores;
    }

    public boolean isDestacado() {
        return destacado;
    }

    public void setDestacado(boolean destacado) {
        this.destacado = destacado;
    }
}