package com.example.chikendinner.ui;

import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chikendinner.AdminSQLiteOpenHelper;
import com.example.chikendinner.R;
import com.example.chikendinner.clases.Venta_Producto;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ver_venta_producto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ver_venta_producto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ver_venta_producto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ver_venta_producto.
     */
    // TODO: Rename and change types and number of parameters
    public static ver_venta_producto newInstance(String param1, String param2) {
        ver_venta_producto fragment = new ver_venta_producto();
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

    private TableLayout tbVentProd;
    private Venta_Producto[] ventas;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_ver_venta_producto, container, false);

        tbVentProd=root.findViewById(R.id.tb_ventas_prod);

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

            addTextViewToRow(tableRow, String.valueOf(ventas[i].getId()));
            addTextViewToRow(tableRow, String.valueOf(ventas[i].getVenta_id()) );
            addTextViewToRow(tableRow, String.valueOf(ventas[i].getProducto_id()) );
            addTextViewToRow(tableRow, String.valueOf(ventas[i].getCantidad()) );
            addTextViewToRow(tableRow, String.valueOf(ventas[i].getIngreso()) );

            tbVentProd.addView(tableRow);

            i++;
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
}