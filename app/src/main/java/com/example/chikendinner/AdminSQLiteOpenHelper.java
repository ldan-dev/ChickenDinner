package com.example.chikendinner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.chikendinner.clases.Producto;
import com.example.chikendinner.clases.Venta;
import com.example.chikendinner.clases.Venta_Producto;

/**
 * AUTOR: LEONARDO DANIEL AVIÑA NERI
 * GRUPO: 6PRAM
 * TODOS LOS DERECHOS RESERVADOS
 * clase de la BD
 */
public class AdminSQLiteOpenHelper  extends SQLiteOpenHelper {
    public SQLiteDatabase bd=this.getWritableDatabase();
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.bd=db;

        bd.execSQL("CREATE TABLE IF NOT EXISTS Productos (\n" +
                "    id INTEGER PRIMARY KEY ,\n" +
                "    nombre VARCHAR,\n" +
                "    costo FLOAT,\n" +
                "    cantidad INT,\n" +
                "    n_compra INT,\n" +
                "    precio_unitario FLOAT,\n" +
                "    precio_venta FLOAT,\n" +
                "    ingresos FLOAT,\n" +
                "    ganancia_neta FLOAT,\n" +
                "    ganancia_unitaria FLOAT,\n" +
                "    porcentaje_ganancia FLOAT\n" +
                ");");

        bd.execSQL("CREATE TABLE IF NOT EXISTS Ventas (\n" +
                "    id INTEGER PRIMARY KEY ,\n" +
                "    dia VARCHAR,\n" +
                "    ingresos FLOAT\n" +
                ");");

        bd.execSQL("CREATE TABLE IF NOT EXISTS Ventas_Productos (\n" +
                "    id INTEGER PRIMARY KEY ,\n" +
                "    venta_id INTEGER,\n" +
                "    producto_id INTEGER,\n" +
                "    cantidad INTEGER,\n" +
                "    ingresos FLOAT\n," +
                "    FOREIGN KEY (venta_id) REFERENCES Ventas(id),\n" +
                "    FOREIGN KEY (producto_id) REFERENCES Productos(id)\n" +
                ");");
    }

    //PRODUCTOS
    public  void addProducto(Producto producto){
        ContentValues registro = new ContentValues();
        registro.put("nombre",producto.getNombre());
        registro.put("costo",producto.getCosto());
        registro.put("cantidad",producto.getCantidad());
        registro.put("precio_unitario",producto.getP_unitario() );
        registro.put("precio_venta",producto.getP_venta());
        registro.put("ingresos",producto.getIngresos() );
        registro.put("ganancia_neta",producto.getGanancia_neta());
        registro.put("ganancia_unitaria",producto.getGanancia_unitaria());
        registro.put("porcentaje_ganancia",producto.getPorcentaje_ganancia());
        registro.put("n_compra",producto.getN_compra());

        bd.insert("Productos",null,registro);
    }
    public  Producto[] getProductos(){
        Cursor fila = bd.rawQuery("SELECT id, nombre, costo, cantidad, n_compra, precio_venta FROM Productos;",null);

        Producto[] productos = new Producto[fila.getCount()];
        int i = 0;

        if (fila != null && fila.moveToFirst()) {
            do {
                int id=fila.getInt(0);
                String nombre= fila.getString(1);
                float costo= fila.getFloat(2);
                int cantidad=fila.getInt(3);
                int n_compra=fila.getInt(4);
                float p_venta= fila.getFloat(5);

                Producto prod = new Producto(id,nombre,costo,cantidad,n_compra,p_venta);
                productos[i] = prod;
                i++;
            } while (fila.moveToNext());
        }
        fila.close();
        Log.i("Aviso", "uso de getProducto");
        return productos;
    }

    public  Producto getProductoById(int index){
        Cursor fila = bd.rawQuery("SELECT id, nombre, costo, cantidad, n_compra, precio_venta FROM Productos WHERE id="+index,null);

        int id=0,cantidad=0,n_compra=0;
        float costo=0.0f,p_venta=0.0f;
        String nombre="";

        if (fila != null && fila.moveToFirst()) {
            id=fila.getInt(0);
            nombre= fila.getString(1);
            costo= fila.getFloat(2);
            cantidad=fila.getInt(3);
            n_compra=fila.getInt(4);
            p_venta= fila.getFloat(5);
        }
        fila.close();
        Producto prod = new Producto(id,nombre,costo,cantidad,n_compra,p_venta);
        Log.i("Aviso", "uso de getProducto");
        return prod;
    }

    public  int editProducto(int id, Producto producto){
        ContentValues registro = new ContentValues();
        registro.put("nombre",producto.getNombre());
        registro.put("costo",producto.getCosto());
        registro.put("cantidad",producto.getCantidad());
        registro.put("precio_unitario",producto.getP_unitario() );
        registro.put("precio_venta",producto.getP_venta());
        registro.put("ingresos",producto.getIngresos() );
        registro.put("ganancia_neta",producto.getGanancia_neta());
        registro.put("ganancia_unitaria",producto.getGanancia_unitaria());
        registro.put("porcentaje_ganancia",producto.getPorcentaje_ganancia());
        registro.put("n_compra",producto.getN_compra());

        int n= bd.update("Productos",registro,"id="+id,null);
        return n;//si no es igual a 1 algo mal paso
    }

    public  int deleteProducto(int id){
        int n= bd.delete("Productos","id="+id,null);
        return n;
    }

    //PRODUCTO_VENTA
    public void addProductoVenta( Venta_Producto venta_pro ){
        //float ingreso= getProductoById(venta_pro.getProducto_id()).getCosto()*venta_pro.getCantidad();
        ContentValues registro = new ContentValues();
        registro.put("venta_id",venta_pro.getVenta_id());
        registro.put("producto_id",venta_pro.getProducto_id());
        registro.put("cantidad",venta_pro.getCantidad());
        registro.put("ingresos",venta_pro.getIngreso());

        bd.insert("Ventas_Productos",null,registro);
    }

    public  int editProductoVenta(Venta_Producto venta_pro, int id){

        ContentValues registro = new ContentValues();
        registro.put("venta_id",venta_pro.getVenta_id());
        registro.put("producto_id",venta_pro.getProducto_id());
        registro.put("cantidad",venta_pro.getCantidad());
        registro.put("ingresos",venta_pro.getIngreso());

        int n= bd.update("Ventas_Productos",registro,"id="+id,null);
        return n;//si no es igual a 1 algo mal paso
    }
    public  Venta_Producto[] getProductoVentaById(@Nullable Context context, int index){
        Cursor fila = bd.rawQuery("SELECT id, venta_id , producto_id , cantidad, ingresos FROM Ventas_Productos  WHERE venta_id="+index,null);

        Venta_Producto[] venta_pro = new Venta_Producto[fila.getCount()];
        int i = 0;

        if (fila != null && fila.moveToFirst()) {
            do {
                int id=fila.getInt(0);
                int venta_id= fila.getInt(1);
                int producto_id= fila.getInt(2);
                int cantidad=fila.getInt(3);
                //float ingresos=fila.getFloat(4);

                Venta_Producto vp = new Venta_Producto(context,id,venta_id,producto_id,cantidad);
                venta_pro[i] = vp;
                i++;
            } while (fila.moveToNext());
        }
        fila.close();
        Log.i("Aviso", "uso de getProductoVentaById");
        return venta_pro;
    }

    public  Venta_Producto[] getProductoVenta(@Nullable Context context){
        Cursor fila = bd.rawQuery("SELECT id, venta_id , producto_id , cantidad, ingresos FROM Ventas_Productos",null);

        Venta_Producto[] venta_pro = new Venta_Producto[fila.getCount()];
        int i = 0;

        if (fila != null && fila.moveToFirst()) {
            do {
                int id=fila.getInt(0);
                int venta_id= fila.getInt(1);
                int producto_id= fila.getInt(2);
                int cantidad=fila.getInt(3);
                float ingresos=fila.getFloat(4);

                Venta_Producto vp = new Venta_Producto(context,id,venta_id,producto_id,cantidad);
                venta_pro[i] = vp;
                i++;
            } while (fila.moveToNext());
        }
        fila.close();
        Log.i("Aviso", "uso de getProductoVentaById");
        return venta_pro;
    }
    public  Venta_Producto[] getProductoVentaByVentaId(@Nullable Context context, int venta_id){
        Cursor fila = bd.rawQuery("SELECT id, venta_id , producto_id , cantidad, ingresos FROM Ventas_Productos  WHERE venta_id="+venta_id,null);

        Venta_Producto[] venta_pro = new Venta_Producto[fila.getCount()];
        int i = 0;

        if (fila != null && fila.moveToFirst()) {
            do {
                int id=fila.getInt(0);
                int producto_id= fila.getInt(2);
                int cantidad=fila.getInt(3);
                //float ingresos=fila.getFloat(4);

                Venta_Producto vp = new Venta_Producto(context,id,venta_id,producto_id,cantidad);
                venta_pro[i] = vp;
                i++;
            } while (fila.moveToNext());
        }
        fila.close();
        Log.i("Aviso", "uso de getProductoVentaByVentaId");
        return venta_pro;
    }
    public  int deleteProductoVenta(int id){
        int n= bd.delete("Ventas_Productos","id="+id,null);
        return n;
    }

    //VENTA
    public  void addVenta(Venta venta){
        ContentValues registro = new ContentValues();
        registro.put("dia",venta.getDia());
        registro.put("ingresos",venta.getIngresos());

        bd.insert("Ventas",null,registro);
    }

    public  int editVenta(int id, Venta venta){
        ContentValues registro = new ContentValues();
        registro.put("dia",venta.getDia());
        registro.put("ingresos",venta.getIngresos());

        int n= bd.update("Ventas",registro,"id="+id,null);
        return n;//si no es igual a 1 algo mal paso
    }

    public Venta[] getVentas(Context context){
        Cursor fila = bd.rawQuery("SELECT id, dia , ingresos  FROM Ventas;",null);

        Venta[] ventas = new Venta[fila.getCount()];
        int i = 0;

        if (fila != null && fila.moveToFirst()) {
            do {
                int id=fila.getInt(0);
                String dia=fila.getString(1);

                Venta venta= new Venta(context, id, dia);
                ventas[i] = venta;
                i++;
            } while (fila.moveToNext());
        }
        fila.close();
        Log.i("Aviso", "uso de getVentas");
        return ventas;
    }
    public Venta getVentaById(Context context, int index){
        Cursor fila = bd.rawQuery("SELECT id, dia , ingresos  FROM Ventas WHERE id="+index,null);

        int id = 0;
        String dia="";

        if (fila != null && fila.moveToFirst()) {
            id=fila.getInt(0);
            dia=fila.getString(1);
        }
        Venta venta= new Venta(context, id, dia);
        fila.close();
        Log.i("Aviso", "uso de getVentaById");
        return venta;
    }

    public  int deleteVenta(int id){
        int n= bd.delete("Ventas","id="+id,null);
        deleteVentaProductoByVentaId(id);
        return n;
    }

    public  int deleteVentaProductoByVentaId(int id){
        int n= bd.delete("Ventas_Productos","venta_id="+id,null);
        return n;
    }

    public float getInversionTotal() {
        float inversionTotal = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT costo, n_compra FROM Productos", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                float costo = cursor.getFloat(0) ;
                int nCompra = cursor.getInt(1);
                inversionTotal += costo * nCompra;
            }
            cursor.close();
        }
        return inversionTotal;
    }

    public float getIngresosTotales(Context context) {
        float ingresos=0;

        Venta[] ventas=getVentas(context);
        for (Venta venta : ventas) {
            ingresos+=venta.getIngresos();
        }

        return ingresos;
    }

    public float getGananciaNeta(Context context){
        return getIngresosTotales(context)-getInversionTotal();
    }

    /**
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
