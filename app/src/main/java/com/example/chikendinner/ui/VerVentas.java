package com.example.chikendinner.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chikendinner.AdminSQLiteOpenHelper;
import com.example.chikendinner.R;
import com.example.chikendinner.clases.Producto;
import com.example.chikendinner.clases.Venta;
import com.example.chikendinner.clases.Venta_Producto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * AUTOR: LEONARDO DANIEL AVIÑA NERI
 * GRUPO: 6PRAM
 * TODOS LOS DERECHOS RESERVADOS
 */
public class VerVentas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VerVentas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerVentas.
     */
    // TODO: Rename and change types and number of parameters
    public static VerVentas newInstance(String param1, String param2) {
        VerVentas fragment = new VerVentas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Venta[] ventas;
    private TableLayout tbVentas;
    private LinearLayout lyVacio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_ver_ventas, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            int colorActionBar = getResources().getColor(R.color.blue_600);
            Objects.requireNonNull(activity.getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(colorActionBar));
        }

        tbVentas = root.findViewById(R.id.sv_tb_ventas);
        lyVacio = root.findViewById(R.id.table_empty2);

        tbVentas.setVisibility(View.GONE);
        lyVacio.setVisibility(View.GONE);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(requireContext(), "chicken_dinner", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ventas = admin.getVentas(getContext());

        if (ventas.length == 0) {
            lyVacio.setVisibility(View.VISIBLE);
        } else {
            tbVentas.setVisibility(View.VISIBLE);

            for (int i = 0; i < ventas.length; i++) {
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
                        boolean isUserInteraction = true;
                        if (isChecked) {
                            editar(ventas[finalI].getId(),admin);
                            Toast.makeText(getContext(), "id=" + ventas[finalI].getId(), Toast.LENGTH_SHORT).show();
                            isUserInteraction = false;
                            chkEditar.setChecked(false);
                            isUserInteraction = true;


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
                            borrar(ventas[finalI].getId(),admin);
                            chkBorrar.setChecked(false);
                        } else {
                            Toast.makeText(getContext(), "chkBorrar está desmarcado", Toast.LENGTH_SHORT).show();
                            chkBorrar.setChecked(false);
                        }
                    }
                });

                tableRow.addView(chkEditar);
                tableRow.addView(chkBorrar);

                addTextViewToRow(tableRow, String.valueOf(ventas[i].getId()));
                addTextViewToRow(tableRow, ventas[i].getDia());
                addTextViewToRow(tableRow, String.valueOf(ventas[i].getIngresos()) );

                tbVentas.addView(tableRow);
            }

        }
        verProductosVentas(root);

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

    public void borrar(int ele, AdminSQLiteOpenHelper admin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Deseas eliminar la venta? esta acción no se puede deshacer")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Llama al método para eliminar el insumo

                        admin.deleteVenta(ele);
                        Toast.makeText(getContext(), "Venta eliminada", Toast.LENGTH_SHORT).show();
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

    public void editar(int ele, AdminSQLiteOpenHelper admin) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View editView = inflater.inflate(R.layout.edit_venta, null);

        Button btnDia;
        TableLayout tbEditVenta;
        ArrayList<EditText> cantidades = new ArrayList<>();

        btnDia = editView.findViewById(R.id.edit_btn_dia);
        btnDia.setText( admin.getVentaById(getContext(),ele).getDia() );

        tbEditVenta = editView.findViewById(R.id.edit_tb_add_venta);

        Producto[] productos = admin.getProductos();

        ArrayList<Venta_Producto> ventaProductos = new ArrayList<>();

        int i=0;
        for (Producto prod : productos) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setPadding(4, 15, 4, 5); //padding superior de 20dp, laterales de 5dp

            estilizarTV(tableRow,prod.getNombre());
            cantidades.add( estilizarEditText(tableRow,"0") );

            int id_vp= ventaProductos.size()+1;

            Venta_Producto vp= new Venta_Producto(getContext(),id_vp, ele,prod.getId(),0);
            ventaProductos.add(vp);

            tbEditVenta.addView(tableRow);
            i++;
        }

        Venta_Producto[] pv = admin.getProductoVentaByVentaId(getContext(),ele);
        i=0;
        for (Venta_Producto ventaProducto : pv) {
            if (ventaProducto.getProducto_id() == productos[i].getId() )
                cantidades.get(i).setText( String.valueOf(ventaProducto.getCantidad() ) );
            i++;
        }

        Calendar actual = Calendar.getInstance();//configurar el timePicker y datePicker con la hora actual del dispositivo
        Calendar calendar = Calendar.getInstance();//seleccionar fecha
        btnDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ano = actual.get(Calendar.YEAR);
                int mes = actual.get(Calendar.MONTH);
                int dia = actual.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //dar formato a la fecha indicada
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = format.format(calendar.getTime());
                        btnDia.setText(strDate);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(editView)
                .setTitle("")
                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        admin.deleteVentaProductoByVentaId(ele);
                        int i=0;
                        for (Venta_Producto vp : ventaProductos) {
                            int c= Integer.parseInt(cantidades.get(i).getText().toString());

                            if (c!=0){
                                vp.setCantidad( c );
                                //admin.editProductoVenta(vp,vp.getId());
                                admin.addProductoVenta(vp);
                            }
                            i++;
                        }

                        int id_venta= admin.getVentas(getContext()).length+1;
                        Venta venta=new Venta(getContext(),ele,btnDia.getText().toString());
                        admin.editVenta(id_venta,venta);

                        Toast.makeText(getContext(), "ventas: "+admin.getVentas(getContext()).length, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "ventas_productos: "+admin.getProductoVenta(getContext()).length, Toast.LENGTH_SHORT).show();

                        getActivity().recreate();
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

    public void estilizarTV(TableRow tableRow, String texto){
        TextView tv = new TextView(getContext());
        tv.setText(texto);

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); //tamaño del texto en sp
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.coiny);
        tv.setTypeface(typeface);
        // Crear parámetros de diseño para la fila
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
        params.weight = 1; // Establecer el peso

        // Aplicar los parámetros de diseño al TextView
        tv.setLayoutParams(params);

        tableRow.addView(tv);
    }
    public EditText estilizarEditText(TableRow tableRow, String texto){
        EditText et = new EditText(getContext());
        et.setHint("cantidad");

        et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); //tamaño del texto en sp
        et.setText(texto);

        et.setInputType(InputType.TYPE_CLASS_NUMBER);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.coiny);
        et.setTypeface(typeface);
        // Crear parámetros de diseño para la fila
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
        params.weight = 1; // Establecer el peso

        // Aplicar los parámetros de diseño al TextView
        et.setLayoutParams(params);

        tableRow.addView(et);
        return et;
    }

    public void verProductosVentas(View root){
        TableLayout tbVentProd;
        Venta_Producto[] ventas;
        TextView tvAutor;

        tbVentProd=root.findViewById(R.id.tb_ventas_prod2);
        tvAutor=root.findViewById(R.id.tv_autor);

        tbVentProd.setVisibility(View.GONE);

        tvAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tbVentProd.getVisibility()!=View.VISIBLE)
                    tbVentProd.setVisibility(View.VISIBLE);
                else
                    tbVentProd.setVisibility(View.GONE);
            }
        });


        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(requireContext(), "chicken_dinner", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ventas = admin.getProductoVenta(getContext());

        int i=0;
        for (Venta_Producto venta : ventas) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setPadding(30, 46, 15, 15); //padding superior de 20dp, laterales de 5dp

            // alterna el color de fondo
            if (i % 2 == 0) {
                tableRow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                tableRow.setBackgroundColor(getResources().getColor(R.color.blue_800));
            }
            /*
            CheckBox chkEditar = new CheckBox(getContext());
            chkEditar.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            CheckBox chkBorrar = new CheckBox(getContext());
            chkBorrar.setButtonTintList(ColorStateList.valueOf(Color.WHITE));

            int finalI = i;
            chkEditar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    boolean isUserInteraction = true;
                    if (isChecked) {
                        //editar(ventas[finalI].getId(),admin);
                        Toast.makeText(getContext(), "id=" + ventas[finalI].getId(), Toast.LENGTH_SHORT).show();
                        isUserInteraction = false;
                        chkEditar.setChecked(false);
                        isUserInteraction = true;
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

                        chkBorrar.setChecked(false);
                    } else {
                        Toast.makeText(getContext(), "chkBorrar está desmarcado", Toast.LENGTH_SHORT).show();
                        chkBorrar.setChecked(false);
                    }
                }
            });
            tableRow.addView(chkEditar);
            tableRow.addView(chkBorrar);
             */

            addTextViewToRow(tableRow, String.valueOf(ventas[i].getId()));
            addTextViewToRow(tableRow, String.valueOf(ventas[i].getVenta_id()) );
            addTextViewToRow(tableRow, String.valueOf(ventas[i].getProducto_id()) );
            addTextViewToRow(tableRow, String.valueOf(ventas[i].getCantidad()) );
            addTextViewToRow(tableRow, String.valueOf(ventas[i].getIngreso()) );

            tbVentProd.addView(tableRow);

            i++;
        }
    }

}