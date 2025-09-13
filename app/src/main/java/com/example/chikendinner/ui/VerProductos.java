package com.example.chikendinner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chikendinner.AdminSQLiteOpenHelper;
import com.example.chikendinner.R;
import com.example.chikendinner.clases.Producto;
import com.example.chikendinner.clases.Venta_Producto;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerProductos#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class VerProductos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerProductos.
     */
    // TODO: Rename and change types and number of parameters
    public static VerProductos newInstance(String param1, String param2) {
        VerProductos fragment = new VerProductos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public VerProductos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Producto[] productos;
    private TableLayout tbProductos;
    private LinearLayout lyVacio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_ver_productos2, container, false);

        View root = inflater.inflate(R.layout.fragment_ver_productos, container, false);
        Context applicationContext = requireContext().getApplicationContext();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            int colorActionBar = getResources().getColor(R.color.blue_600);
            Objects.requireNonNull(activity.getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(colorActionBar));
        }

        tbProductos = root.findViewById(R.id.vp_tb_productos);
        lyVacio = root.findViewById(R.id.table_empty);

        tbProductos.setVisibility(View.GONE);
        lyVacio.setVisibility(View.GONE);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(requireContext(), "chicken_dinner", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        productos = admin.getProductos();

        if (productos.length == 0) {
            lyVacio.setVisibility(View.VISIBLE);
        } else {
            tbProductos.setVisibility(View.VISIBLE);

            for (int i = 0; i < productos.length; i++) {
                TableRow tableRow = new TableRow(getContext());
                tableRow.setPadding(30, 46, 15, 15); //padding superior de 20dp, laterales de 5dp

                // alterna el color de fondo
                if (i % 2 == 0) {
                    tableRow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tableRow.setBackgroundColor(getResources().getColor(R.color.blue_800));
                }

                ArrayList<TextView> textViews = new ArrayList<>();//almacenar tv's para darles el mismo estilo y guardarlos en tableRow

                CheckBox chkEditar = new CheckBox(getContext());
                chkEditar.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                CheckBox chkBorrar = new CheckBox(getContext());
                chkBorrar.setButtonTintList(ColorStateList.valueOf(Color.WHITE));

                int finalI = i;

                chkEditar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        boolean isUserInteraction = true; // Variable de control para determinar la interacción del usuario
                        if (isChecked) {
                            editar(productos[finalI].getId(),admin);

                            //Toast.makeText(getContext(), "id=" + productos[finalI].getId(), Toast.LENGTH_SHORT).show();
                            isUserInteraction = false; // Cambia la variable de control para evitar el ciclo infinito
                            chkEditar.setChecked(false);
                            isUserInteraction = true; // Restaura la variable de control para permitir futuras interacciones del usuari


                        } else {
                            Toast.makeText(getContext(), "edit desmarcado", Toast.LENGTH_SHORT).show();
                            chkEditar.setChecked(false);
                        }
                    }
                });

                chkBorrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            borrar(productos[finalI].getId(),admin);
                            chkBorrar.setChecked(false);
                        } else {
                            Toast.makeText(getContext(), "chkBorrar está desmarcado", Toast.LENGTH_SHORT).show();
                            chkBorrar.setChecked(false);
                        }
                    }
                });

                tableRow.addView(chkEditar);
                tableRow.addView(chkBorrar);

                addTextViewToRow(tableRow, String.valueOf(productos[i].getId()));
                addTextViewToRow(tableRow, productos[i].getNombre());
                addTextViewToRow(tableRow, String.valueOf(productos[i].getCosto()) );
                addTextViewToRow(tableRow, String.valueOf(productos[i].getCantidad()) );
                addTextViewToRow(tableRow, String.valueOf(productos[i].getP_unitario()));
                addTextViewToRow(tableRow, String.valueOf(productos[i].getP_venta()));
                addTextViewToRow(tableRow, String.valueOf(productos[i].getIngresos()));
                addTextViewToRow(tableRow, String.valueOf(productos[i].getGanancia_neta()));
                addTextViewToRow(tableRow, String.valueOf(productos[i].getGanancia_unitaria()));
                addTextViewToRow(tableRow, String.valueOf(productos[i].getPorcentaje_ganancia()));
                addTextViewToRow(tableRow, String.valueOf(productos[i].getN_compra()) );

                // Añade el TableRow al TableLayout
                tbProductos.addView(tableRow);
            }
        }

        return root;
    }

    private void addTextViewToRow(TableRow tableRow, String texto) {
        TextView textView = new TextView(getContext());
        textView.setText(texto);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); //tamaño del texto en sp
        textView.setTextColor(Color.WHITE);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.coiny);
        textView.setTypeface(typeface);
        textView.setGravity(Gravity.CENTER);

        tableRow.addView(textView);
    }

    public void editar(int ele, AdminSQLiteOpenHelper admin){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View editView = inflater.inflate(R.layout.edit_producto, null);

        EditText etNombre, etCosto, etCantidad, etPrecioVenta,etNumCompra, etPrecioRecomendado;
        EditText etPrecioUnitario, etIngresos, etGNeta,etGUnitaria,etPorcGanancia;
        Button btnCostear;
        EditText[] campos;
        final Producto[] prodAux = new Producto[1];

        etNombre=editView.findViewById(R.id.edit_et_nombre);
        etCosto=editView.findViewById(R.id.edit_et_costo);
        etCantidad=editView.findViewById(R.id.edit_et_cantidad);
        etPrecioUnitario=editView.findViewById(R.id.edit_et_precio_unitario);
        etPrecioRecomendado=editView.findViewById(R.id.edit_et_precio_recomendado);
        etPrecioVenta=editView.findViewById(R.id.edit_et_precio_venta);
        etIngresos=editView.findViewById(R.id.edit_et_ingresos);
        etGNeta=editView.findViewById(R.id.edit_et_ganancia_neta);
        etGUnitaria=editView.findViewById(R.id.edit_et_ganancia_unitaria);
        etPorcGanancia=editView.findViewById(R.id.edit_et_porcentaje_ganancia);
        etNumCompra=editView.findViewById(R.id.edit_et_numero_compra);

        btnCostear=editView.findViewById(R.id.edit_btn_costear);

        prodAux[0]= admin.getProductoById(ele);

        etNombre.setText(prodAux[0].getNombre());
        etCosto.setText( String.valueOf(prodAux[0].getCosto()) );
        etCantidad.setText( String.valueOf(prodAux[0].getCantidad()) );
        etNumCompra.setText( String.valueOf(prodAux[0].getN_compra()) );
        etPrecioVenta.setText( String.valueOf(prodAux[0].getP_venta()) );

        etPrecioRecomendado.setText( String.valueOf( prodAux[0].getPrecioRecomendado() ) );
        etPrecioUnitario.setText( String.valueOf( prodAux[0].getP_unitario() ) );
        etPrecioVenta.setText( String.valueOf( prodAux[0].getP_venta() ) );
        etIngresos.setText( String.valueOf( prodAux[0].getIngresos() ) );
        etGNeta.setText( String.valueOf( prodAux[0].getGanancia_neta() ) );
        etGUnitaria.setText( String.valueOf( prodAux[0].getGanancia_unitaria() ) );
        etPorcGanancia.setText(String.format("%s%%", prodAux[0].getPorcentaje_ganancia()));

        campos= new EditText[]{etNombre, etCosto, etCantidad, etPrecioRecomendado, etPrecioUnitario, etPrecioVenta, etIngresos, etGNeta, etGUnitaria, etPorcGanancia, etNumCompra};

        btnCostear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNombre.getText().toString().isEmpty() || etCosto.getText().toString().isEmpty() || etCantidad.getText().toString().isEmpty() || etNumCompra.getText().toString().isEmpty() || etPrecioVenta.getText().toString().isEmpty() ){
                    Toast.makeText(getContext(),"llena todos los campos",Toast.LENGTH_SHORT).show();
                }else{

                    int id = ele;
                    float costo= Float.parseFloat(etCosto.getText().toString());
                    int cantidad= Integer.parseInt(etCantidad.getText().toString());
                    int n_compra= Integer.parseInt(etNumCompra.getText().toString());
                    float pventa= Float.parseFloat(etPrecioVenta.getText().toString());

                    prodAux[0] = new Producto(id,etNombre.getText().toString(),costo,cantidad,n_compra,pventa);
                   // prodAux[0]= admin.getProductoById(id);
                    etNumCompra.setText( String.valueOf(prodAux[0].getN_compra()) );

                    etPrecioRecomendado.setText( String.valueOf( prodAux[0].getPrecioRecomendado() ) );
                    etPrecioUnitario.setText( String.valueOf( prodAux[0].getP_unitario() ) );
                    etPrecioVenta.setText( String.valueOf( prodAux[0].getP_venta() ) );
                    etIngresos.setText( String.valueOf( prodAux[0].getIngresos() ) );
                    etGNeta.setText( String.valueOf( prodAux[0].getGanancia_neta() ) );
                    etGUnitaria.setText( String.valueOf( prodAux[0].getGanancia_unitaria() ) );
                    etPorcGanancia.setText(String.format("%s%%", prodAux[0].getPorcentaje_ganancia()));
                }

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(editView)
                .setTitle("")
                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText[] campos2= {etNombre, etCosto, etCantidad, etPrecioRecomendado, etPrecioUnitario, etPrecioVenta, etIngresos, etGNeta, etGUnitaria, etPorcGanancia, etNumCompra};
                        boolean vacio[]= {false};
                            for (EditText campo : campos2) {
                                if (campo.getText().toString().isEmpty()){
                                    vacio[0] =true;
                                    break;
                                }
                            }
                            if (vacio[0]){
                                Toast.makeText(getContext(),"Llena todos los campos",Toast.LENGTH_SHORT).show();
                            }else{
                                admin.editProducto(ele,prodAux[0]);

                                Toast.makeText(getContext(),"Producto actualizado",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                getActivity().recreate();

                            }
                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Mostrar el cuadro de diálogo
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
    }

    public void borrar(int ele, AdminSQLiteOpenHelper admin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Deseas eliminar el producto? esta acción no se puede deshacer")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Llama al método para eliminar el insumo

                        admin.deleteProducto(ele);
                        Toast.makeText(getContext(), "producto eliminado", Toast.LENGTH_SHORT).show();
                        getActivity().recreate();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


}
