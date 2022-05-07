
package clases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
//Clase sin metodo main
public class fechaActual {
    //Crear metodo para que muestre la fecha actual
    public static String fechaActual(){
        Date fecha = new Date();//instancias la clase
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//formato de fecha
        return formatoFecha.format(LocalDateTime.now()); 
    }
}