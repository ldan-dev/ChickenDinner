package com.example.chikendinner.clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.chikendinner.AdminSQLiteOpenHelper;

/**
 * AUTOR: LEONARDO DANIEL AVIÑA NERI
 * GRUPO: 6PRAM
 * TODOS LOS DERECHOS RESERVADOS
 * clase de la tabla intermedia (n:m) de Venta_Producto
 */
public class Venta_Producto{

    private int id;
    private int producto_id;
    private int venta_id;
    private int cantidad;//cantidad del producto q se vendio
    private int n_productos;
    private Producto[] productos;
    private int[] cantidades;
    public float ingreso;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase bd;

    public Venta_Producto(@Nullable Context context, int id, int venta_id, int producto_id, int cantidad) {
        this.id = id;
        this.venta_id = venta_id;
        this.producto_id = producto_id;
        this.cantidad = cantidad;

        admin = new AdminSQLiteOpenHelper(context,"chicken_dinner",null,1);
        bd= admin.getWritableDatabase();

        this.ingreso= admin.getProductoById(producto_id).getP_venta()*cantidad;
    }

    /*
    public int getN_productos() {
        return n_productos;
    }

    public void setN_productos(int n_productos) {
        this.n_productos = n_productos;
    }
     */

    public int getProducto_id() {
        return producto_id;
    }

    public float getIngreso() {
        return ingreso;
    }
    public void setIngreso() {
        this.ingreso= admin.getProductoById(producto_id).getP_venta()*cantidad;
    }

    public void setProductos(Producto[] productos) {
        this.productos = productos;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int c) {
        this.cantidad=c;
        this.ingreso= admin.getProductoById(producto_id).getP_venta()*cantidad;
    }

    public void setCantidades(int[] cantidades) {
        this.cantidades = cantidades;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVenta_id() {
        return venta_id;
    }

    public void setVenta_id(int venta_id) {
        this.venta_id = venta_id;
    }
}
