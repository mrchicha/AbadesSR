package com.example.make.myapplication.ImpCorredor;

import com.example.make.myapplication.ICorredor.ICorredor;
import com.example.make.myapplication.Modelo.Corredor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CorredorImpl implements ICorredor{

    private Statement st = null;
    private PreparedStatement pt = null;
    private ResultSet rs = null;

    private String bd = "abadesstonerace";
    private String login = "mrchicha";
    private String password = "o9zvtf5c";
    private String url = "jdbc:mysql://db4free.net/"+bd;


    /**
     *  Método que permite establecer una conexión a la base de datos
     * @return Retorna una conexión a la base de datos
     */
    private Connection conexion (){

        Connection con=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection(url,login,password);
            System.out.println("Conexión establecida.");
        } catch (SQLException ex) {
            System.out.println("Excepción en la conexión");
        } catch (ClassNotFoundException ex) {
            System.out.println("No se encuentra la clase");
        }
        return con;
    }

    @Override
    public ArrayList<Corredor> SelectPosiciones(String consulta) {

        ArrayList<Corredor> corredores=new ArrayList<Corredor>();

        try {
            Connection conexion = conexion();

            st=conexion.createStatement();
            rs=st.executeQuery(consulta);

                while (rs.next()){
                   corredores.add(new Corredor(rs.getInt("dorsal"),rs.getString("nombre"),
                           rs.getString("apellidos")));
                }


        } catch (SQLException ex) {
            System.out.println("excepcion");
        }

        return corredores;
    }

    public Boolean delete(String sSQL) {
        Boolean resultado = false;

        return resultado;

    }

    @Override
    public Corredor SeleccionarPorID(int dorsal)
    {
        Corredor corredor = new Corredor();

        String sSQL = "select * from " + Corredor.TABLA
                + " where " + Corredor.CLAVE + " = "
                + dorsal;
        try
        {
            Connection conexion = conexion();

            st=conexion.createStatement();
            rs=st.executeQuery(sSQL);
            corredor.setDorsal(rs.getInt("dorsal"));
            corredor.setNombre(rs.getString("nombre"));
            corredor.setApellidos(rs.getString("apellidos"));

        } catch (SQLException ex)
        {
            System.out.println("excepcion");
        }

        return corredor;
    }

    @Override
    public ResultSet Select(String sSQL) {
        return null;
    }

    @Override
    public ArrayList<Double> Posicion(int dorsal) {
        return null;
    }

    @Override
    public Boolean insert(Corredor corredor) {
        Boolean resultado = false;
        return resultado;
    }

    @Override
    public Boolean Update(Corredor corredor) {
        Boolean resultado = false;

        return resultado;
    }

    @Override
    public Boolean Delete(int dorsal) {

        Boolean resultado=false;
        String sSQL="delete from " + Corredor.TABLA + " where " + Corredor.CLAVE +
                " = " + dorsal;
        try {

            Connection conexion = conexion();
            PreparedStatement stmt = null;
            stmt = conexion.prepareStatement(sSQL);

            stmt.executeUpdate();
            stmt.close();
            resultado=true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }
}
