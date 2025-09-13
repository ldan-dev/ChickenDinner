package com.example.chikendinner.clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.chikendinner.AdminSQLiteOpenHelper;

/**
 * AUTOR: LEONARDO DANIEL AVIÑA NERI
 * GRUPO: 6PRAM
 * TODOS LOS DERECHOS RESERVADOS
 * clase de las ventas que tengamos
 */
public class Venta {

    private int id;
    private String dia;
    private float ingresos;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase bd;

    /**
     * @param id id de venta
     * @param dia fecha
     */
    public Venta(Context context,int id, String dia){
        this.id=id;
        this.dia=dia;

        admin = new AdminSQLiteOpenHelper(context,"chicken_dinner",null,1);
        bd= admin.getWritableDatabase();

        setIngresos(context);
    }

    /**
     * calcular los ingresos segun los productos  q tenga
     */
    public void setIngresos(Context context ){
        float in=0;
        Venta_Producto[] venta_pro= admin.getProductoVentaByVentaId(context,this.id);
        for (Venta_Producto v : venta_pro) {
            in+=v.ingreso;
        }
        ingresos=in;
        /*
        float in=0;
        Venta_Producto ventaProducto[]= new Venta_Producto[n_productos];
        for (int i = 0; i < n_productos; i++) {
            float costo= admin.getProductoById( ventaProducto[i].getProducto_id() ).getCosto();
           in+= ventaProducto[i].getCantidad()*costo;
        }
        this.ingresos=in;
         */
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public float getIngresos() {
        return ingresos;
    }

}
