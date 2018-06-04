package com.example.make.myapplication.ICorredor;

import com.example.make.myapplication.Modelo.Corredor;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface ICorredor {

    public Corredor SeleccionarPorID(int dorsal);
    public ResultSet Select(String sSQL);
    public ArrayList<Double> Posicion(int dorsal);
    public ArrayList<Corredor> SelectPosiciones(String consulta);
    public Boolean insert(Corredor corredor);
    public Boolean Update(Corredor corredor);
    public Boolean Delete(int dorsal);

}
