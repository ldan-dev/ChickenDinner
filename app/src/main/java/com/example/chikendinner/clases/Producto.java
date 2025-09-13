package com.example.chikendinner.clases;
/**
 * AUTOR: LEONARDO DANIEL AVIÑA NERI
 * GRUPO: 6PRAM
 * TODOS LOS DERECHOS RESERVADOS
 * clase de los productos que tengamos en inventario
 */
public class Producto {

    private int id;
    private String nombre;
    private float costo;
    private int cantidad;
    private int n_compra;
    private float p_unitario;
    private float p_venta;
    private float ingresos;
    private float ganancia_neta;
    private float ganancia_unitaria;
    private float porcentaje_ganancia;

    /**
     * constructor
     * @param id id del producto
     * @param nombre nombre del producto
     * @param costo costo o inversión de la bolsa del producto
     * @param cantidad cantidad de paquetes 
     * @param n_compra cuantas veces ha sido comprado
     * @param p_venta a cuanto lo voy a vender
     */
    public Producto(int id, String nombre, float costo, int cantidad, int n_compra, float p_venta){
        this.id=id;
        this.nombre=nombre;
        this.costo=costo;
        this.cantidad=cantidad;
        this.n_compra=n_compra;
        this.p_venta=p_venta;
        
        calcular();
    }
    public Producto(){
    }

    /**
     * hace los calculos necesarios para la venta del producto
     */
    public void calcular(){
        p_unitario=costo/cantidad;
        ingresos=(p_venta*cantidad)*n_compra;
        ganancia_neta=ingresos-(costo*n_compra);
        ganancia_unitaria=p_venta-p_unitario;
        porcentaje_ganancia=ingresos*100/costo;
    }

    /**
     * @return retorna el precio de venta recomendado al ganarle 70% por "paquete" (multiplicar por 1.7)
     */
    public float getPrecioRecomendado(){
        return p_unitario*1.7f;
    }

    /**
     * actualiza el precio unitario
     * @param p_unitario
     */
    public void setP_unitario(float p_unitario) {
        this.p_unitario = p_unitario;
        calcular();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCosto() {
        return costo;
    }

    public int getCantidad() {
        return cantidad;
    }
    public float getP_unitario() {
        return this.p_unitario;
    }

    public float getP_venta() {
        return p_venta;
    }

    public float getIngresos() {
        return ingresos;
    }

    public float getGanancia_neta() {
        return ganancia_neta;
    }

    public float getGanancia_unitaria() {
        return ganancia_unitaria;
    }

    public float getPorcentaje_ganancia() {
        return porcentaje_ganancia;
    }

    public int getN_compra() {
        return n_compra;
    }

}
