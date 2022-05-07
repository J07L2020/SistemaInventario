package Clases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class perfilUsuario {
    
    //Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
    
    private String id;
    private String dni;
    private String ruc;
    private String claveSol;
    private String correo;
    private String celular;
    private String contrasenia;
    private String tipoUsuario;
    private String nombres;
    private String apellidos;


    public perfilUsuario() {
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getClaveSol() {
        return claveSol;
    }

    public void setClaveSol(String claveSol) {
        this.claveSol = claveSol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void mostrarPerfil(String pass){
        //Modelo para establecer en la tabla
        String login="SELECT * FROM tbl_usuario INNER JOIN tbl_tipo_usuario ON tbl_usuario.tipo_usuario_iden = tbl_tipo_usuario.tipo_usuario_iden WHERE user_pass = '"+pass+"'"; 
                //Realiza bùsqueda en la tabla trabajadores
        try{
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                id=rs.getString("usuario_iden");
                dni =rs.getString("user_dni");
                ruc=rs.getString("user_ruc");
                claveSol=rs.getString("user_cl_sol");
                correo=rs.getString("user_email");
                celular=rs.getString("user_celu");
                contrasenia=rs.getString("user_pass");
                tipoUsuario = rs.getString("tipo_usuario_desc");
                nombres=rs.getString("user_nom");
                apellidos=rs.getString("user_ape");
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
    } 
}
