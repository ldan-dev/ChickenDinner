package com.example.chikendinner.ui.notifications;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chikendinner.AdminSQLiteOpenHelper;
import com.example.chikendinner.R;
import com.example.chikendinner.clases.Producto;
import com.example.chikendinner.databinding.FragmentNotificationsBinding;

/**
 * AUTOR: LEONARDO DANIEL AVIÑA NERI
 * GRUPO: 6PRAM
 * TODOS LOS DERECHOS RESERVADOS
 * clase de las ventas que tengamos
 */
public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private EditText etNombre, etCosto, etCantidad, etPrecioVenta,etNumCompra, etPrecioRecomendado;
    private EditText etPrecioUnitario, etIngresos, etGNeta,etGUnitaria,etPorcGanancia;
    private Button btnAgregar, btnCancelar, btnCostear;

    private EditText[] campos;
    private Producto prodAux;
    //bd:

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(requireContext(),"chicken_dinner",null,1);
        SQLiteDatabase bd=admin.getWritableDatabase();

        etNombre=root.findViewById(R.id.n_et_nombre);
        etCosto=root.findViewById(R.id.n_et_costo);
        etCantidad=root.findViewById(R.id.n_et_cantidad);
        etPrecioUnitario=root.findViewById(R.id.n_et_precio_unitario);
        etPrecioRecomendado=root.findViewById(R.id.n_et_precio_recomendado);
        etPrecioVenta=root.findViewById(R.id.n_et_precio_venta);
        etIngresos=root.findViewById(R.id.n_et_ingresos);
        etGNeta=root.findViewById(R.id.n_et_ganancia_neta);
        etGUnitaria=root.findViewById(R.id.n_et_ganancia_unitaria);
        etPorcGanancia=root.findViewById(R.id.n_et_porcentaje_ganancia);
        etNumCompra=root.findViewById(R.id.n_et_numero_compra);

        btnAgregar=root.findViewById(R.id.n_btn_agregar);
        btnCostear=root.findViewById(R.id.n_btn_costear);
        btnCancelar=root.findViewById(R.id.n_btn_cancelar);

        campos= new EditText[]{etNombre, etCosto, etCantidad, etPrecioRecomendado, etPrecioUnitario, etPrecioVenta, etIngresos, etGNeta, etGUnitaria, etPorcGanancia, etNumCompra};


        btnCostear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNombre.getText().toString().isEmpty() || etCosto.getText().toString().isEmpty() || etCantidad.getText().toString().isEmpty() || etNumCompra.getText().toString().isEmpty() || etPrecioVenta.getText().toString().isEmpty() ){
                    Toast.makeText(getContext(),"llena todos los campos",Toast.LENGTH_SHORT).show();
                }else{

                    int id = admin.getProductos().length+1;//por ejemplo si esta vacio es 0+1
                    float costo= Float.parseFloat(etCosto.getText().toString());
                    int cantidad= Integer.parseInt(etCantidad.getText().toString());
                    int n_compra= Integer.parseInt(etNumCompra.getText().toString());
                    float pventa= Float.parseFloat(etPrecioVenta.getText().toString());

                    prodAux= new Producto(id,etNombre.getText().toString(),costo,cantidad,n_compra,pventa);

                    etPrecioRecomendado.setText( String.valueOf( prodAux.getPrecioRecomendado() ) );
                    etPrecioUnitario.setText( String.valueOf( prodAux.getP_unitario() ) );
                    etPrecioVenta.setText( String.valueOf( prodAux.getP_venta() ) );
                    etIngresos.setText( String.valueOf( prodAux.getIngresos() ) );
                    etGNeta.setText( String.valueOf( prodAux.getGanancia_neta() ) );
                    etGUnitaria.setText( String.valueOf( prodAux.getGanancia_unitaria() ) );
                    etPorcGanancia.setText(String.format("%s%%", prodAux.getPorcentaje_ganancia()));
                    etNumCompra.setText( String.valueOf( prodAux.getN_compra() ) );
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaciar();
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            private EditText[] campos2= {etNombre, etCosto, etCantidad, etPrecioRecomendado, etPrecioUnitario, etPrecioVenta, etIngresos, etGNeta, etGUnitaria, etPorcGanancia, etNumCompra};
            boolean vacio[]= {false};
            @Override
                public void onClick(View v) {
                    for (EditText campo : campos2) {
                        if (campo.getText().toString().isEmpty()){
                            vacio[0] =true;
                            break;
                        }
                    }
                    if (vacio[0]){
                        Toast.makeText(getContext(),"Llena todos los campos",Toast.LENGTH_SHORT).show();
                    }else{
                        admin.addProducto(prodAux);

                        Toast.makeText(getContext(),"Producto agregado",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(),"Productos: "+admin.getProductos().length,Toast.LENGTH_LONG).show();

                        vaciar();
                    }
                }
            });


        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void vaciar(){
        for (EditText campo : campos) {
            campo.setText("");
        }
    }
}