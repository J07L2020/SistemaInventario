
package Clases;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConexionBD {
    private final String base = "tigrebd";
    private final String user = "root";
    private final String password = "";
    private final String url= "jdbc:mysql://localhost:3306/tigrebd?user=root&password=Pass&useUnicode=true&characterEncoding=UTF-8";
    private Connection con=null;
    
    public Connection getConexionBD(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(this.url, this.user,this.password);
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,"A ocurrido un error, conexión no establecida: " + e.getMessage());
        }catch (ClassNotFoundException ex){
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    
}

