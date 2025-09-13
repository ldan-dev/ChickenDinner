package com.example.chikendinner.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chikendinner.AdminSQLiteOpenHelper;
import com.example.chikendinner.R;
import com.example.chikendinner.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView tvInvTotal,tvIngresosTot,tvGanNeta;
    private Button btnGuia;
    private TableLayout tbGuia;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Obtener la actividad que contiene este fragmento
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        // Establecer el color de la Action Bar
        if (activity != null) {
            // Obtener el color de tu archivo colors.xml
            int colorActionBar = getResources().getColor(R.color.blue_600);

            // Establecer el color de la ActionBar
            Objects.requireNonNull(activity.getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(colorActionBar));
        }

        tbGuia=root.findViewById(R.id.h_tb_guia_usuario);

        tbGuia.setVisibility(View.GONE);

        btnGuia=root.findViewById(R.id.h_btn_guia);

        btnGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tbGuia.getVisibility()!=View.VISIBLE)
                    tbGuia.setVisibility(View.VISIBLE);
                else
                    tbGuia.setVisibility(View.GONE);
            }
        });

        tvIngresosTot = root.findViewById(R.id.h_tv_ingresos_totales);
        tvInvTotal = root.findViewById(R.id.h_tv_inversion_total);
        tvGanNeta = root.findViewById(R.id.h_tv_ganancia_neta);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(requireContext(), "chicken_dinner", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        tvInvTotal.setText(admin.getInversionTotal() +"$" );
        tvIngresosTot.setText(admin.getIngresosTotales(getContext()) +"$" );
        tvGanNeta.setText(admin.getGananciaNeta(getContext() ) +"$" );

        if (admin.getGananciaNeta(getContext() )<1.0)
            tvGanNeta.setTextColor(Color.RED);

        if (admin.getIngresosTotales(getContext() )<1.0)
            tvIngresosTot.setTextColor(Color.RED);

        if (admin.getInversionTotal( )<admin.getIngresosTotales(getContext() ))
            tvInvTotal.setTextColor(Color.GREEN);
        else
            tvInvTotal.setTextColor(Color.RED);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}