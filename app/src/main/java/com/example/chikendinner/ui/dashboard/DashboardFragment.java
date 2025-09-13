package com.example.chikendinner.ui.dashboard;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chikendinner.AdminSQLiteOpenHelper;
import com.example.chikendinner.R;
import com.example.chikendinner.clases.Producto;
import com.example.chikendinner.clases.Venta;
import com.example.chikendinner.clases.Venta_Producto;
import com.example.chikendinner.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * AUTOR: LEONARDO DANIEL AVIÑA NERI
 * GRUPO: 6PRAM
 * TODOS LOS DERECHOS RESERVADOS
 * clase de las ventas que tengamos
 */
public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;

    private Button btnAgregar, btnCancelar, btnDia;
    private LinearLayout lyVacio, lyBtns;
    private TableLayout tbAddVenta;
    private ArrayList<Venta_Producto> ventaProductos = new ArrayList<>();
    private ArrayList<EditText> cantidades = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            int colorActionBar = getResources().getColor(R.color.blue_600);
            Objects.requireNonNull(activity.getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(colorActionBar));
        }

        btnAgregar = root.findViewById(R.id.d_btn_agregar);
        btnCancelar = root.findViewById(R.id.d_btn_cancelar);
        btnDia = root.findViewById(R.id.d_btn_dia);
        btnDia.setText("Día");

        tbAddVenta = root.findViewById(R.id.d_tb_add_venta);
        lyVacio = root.findViewById(R.id.ly_no_products);
        lyBtns = root.findViewById(R.id.ly_btns);

        lyVacio.setVisibility(View.GONE);
        tbAddVenta.setVisibility(View.GONE);
        lyBtns.setVisibility(View.GONE);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(requireContext(), "chicken_dinner", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Producto[] productos = admin.getProductos();

        if (productos.length == 0) {
            lyVacio.setVisibility(View.VISIBLE);
        } else {
            lyBtns.setVisibility(View.VISIBLE);
            tbAddVenta.setVisibility(View.VISIBLE);

            for (Producto prod : productos) {
                TableRow tableRow = new TableRow(getContext());
                tableRow.setPadding(4, 15, 4, 5); //padding superior de 20dp, laterales de 5dp

                estilizarTV(tableRow,prod.getNombre());

                cantidades.add( estilizarEditText(tableRow,"cantidad") );

                int id_vp= ventaProductos.size()+1;
                int id_venta= admin.getVentas(getContext()).length+1;

                Venta_Producto vp= new Venta_Producto(getContext(),id_vp, id_venta,prod.getId(),0);
                ventaProductos.add(vp);

                tbAddVenta.addView(tableRow);
            }

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

            btnAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnDia.getText().equals("Día")) {
                        Toast.makeText(v.getContext(), "seleciona un día válido", Toast.LENGTH_SHORT).show();
                    } else {

                        final String[] cant = {""};
                        int i=0;
                        for (Venta_Producto vp : ventaProductos) {
                            int c= Integer.parseInt(cantidades.get(i).getText().toString());

                            if (c!=0){
                                vp.setCantidad( c );
                                admin.addProductoVenta(vp);
                                //cant[0] +=vp.getIngreso()+", ";
                            }
                            i++;
                        }

                        int id_venta= admin.getVentas(getContext()).length+1;
                        Venta venta=new Venta(getContext(),1,btnDia.getText().toString());
                        admin.addVenta(venta);

                        Toast.makeText(v.getContext(), "ventas: "+admin.getVentas(getContext()).length, Toast.LENGTH_SHORT).show();
                        Toast.makeText(v.getContext(), "ventas_productos: "+admin.getProductoVenta(getContext()).length, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(v.getContext(), "ingresos: "+admin.getVentas(getContext())[0].getIngresos(), Toast.LENGTH_SHORT).show();

                        getActivity().recreate();
                    }

                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().recreate();
                }
            });

            return root;
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
            et.setHint(texto);

            et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); //tamaño del texto en sp
            et.setText("0");

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

        @Override
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
        }
}
