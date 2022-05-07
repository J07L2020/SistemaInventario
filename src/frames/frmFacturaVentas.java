package frames;

import Clases.ConexionBD;
import Clases.Numero_a_Letra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import restAPI.Post;

public class frmFacturaVentas extends javax.swing.JFrame {

    //Declaraciòn de objeto conexion
    ConexionBD con = new ConexionBD();
    Connection c = con.getConexionBD();

    private int usuario_iden;
    private int producto_iden;
    private int cliente_iden;
    private int tipo_doc_iden;
    private double valorIgv;
    private float porcentajeGanancia;

    String[][] cantList = new String[100][2];

    DefaultTableModel m, m1;
    int item = 0;
    Double subTotal;
    private Double importT = 0.0;
    private Double importTmasIgv = 0.0;
    private Double igvST = 0.0;
    private Double igvT = 0.0;
    private Double montoCuota;

    //TRABAJRCON ARRAYS
    ArrayList<String> id_productos = new ArrayList<String>();
    ArrayList<String> cantidadVenta = new ArrayList<String>();
    ArrayList<String> precioVenta = new ArrayList<String>();
    ArrayList<String> subTotaligv = new ArrayList<String>();
    ArrayList<String> subTotalVenta = new ArrayList<String>();

    //ARREGLOS DE OBJETOS
    Object[] infoDventaId_productos;
    Object[] infoDventaCantidadVenta;
    Object[] infoDventaPrecioVenta;
    Object[] infoDventaIgv;
    Object[] infoDventaSubTotalVenta;

    //VARIABLE PARA EVALUAR EL STOCK
    private int checkStock;

    //VARIABLE NUM DE BOLETA
    private String cont_cod;
    private String cont_num;
    private int synfactura;

    //VARIABLES PARA EL REST API ====================================================================================
    private String hora_de_emision;
    private String numero_documento;
    private String codigo_tipo_documento_identidad = "6";
//    private String correo_electronico="";
    private String apellidos_y_nombres_o_razon_social;
//    private String ubigeo="";
    private String direccion;
    private String numero_documento_cliente;
//    private String codigo_pais="PE";
//    private String telefono="";
    private String codigo_tipo_moneda;
    private String serie_documento;
//    private String informacion_adicional="";
    private String fecha_de_vencimiento;
    private String fecha_de_emision;
//    private String numero_orden_de_compra="";
    private String codigo_tipo_documento="01";
    ArrayList<String> item_descripcion = new ArrayList<String>();
    ArrayList<String>  item_total_igv = new ArrayList<String>();
    ArrayList<String>  item_total_item = new ArrayList<String>();
//    private String item_codigo_producto_sunat="";
    ArrayList<String>  item_total_valor_item = new ArrayList<String>();
//    private String item_unidad_de_medida="NIU";
//    private String item_codigo_tipo_precio="01";
    ArrayList<String>  item_precio_unitario = new ArrayList<String>();
    ArrayList<String>  item_porcentaje_igv = new ArrayList<String>();
//    private String item_codigo_interno="";
    ArrayList<String>  item_total_base_igv = new ArrayList<String>();
    ArrayList<String>  item_cantidad = new ArrayList<String>();
//    private String item_codigo_tipo_afectacion_igv="10";
    ArrayList<String>  item_valor_unitario = new ArrayList<String>();
    ArrayList<String>  item_total_impuestos = new ArrayList<String>();
//    private String codigo_tipo_operacion="0101";
//    private double total_operaciones_exoneradas=0.00;
    private double total_igv;
    private double total_operaciones_gravadas;
    private double total_venta;
//    private double total_operaciones_gratuitas = 0.00;
//    private double total_operaciones_inafectas = 0.00;
//    private double total_exportacion = 0.00;
    private double total_valor;
    private double total_impuestos;
    
    //ARREGLOS DE OBJETOS
    Object[] info_item_descripcion;
    Object[] info_item_total_igv;
    Object[] info_item_total_item;
    Object[] info_item_total_valor_item;
    Object[] info_item_precio_unitario;
    Object[] info_item_porcentaje_igv;
    Object[] info_item_total_base_igv;
    Object[] info_item_cantidad;
    Object[] info_item_valor_unitario;
    Object[] info_item_total_impuestos;
    //===============================================================================================================
    
    public frmFacturaVentas() {
        initComponents();
        txtFecha.setText(fechaActual()+" "+HoraActual());
        this.setExtendedState(MAXIMIZED_BOTH);
        MostrarProductos();
        getInfoTipoDoc();
        getInfoAjustes();
        //REDIMENSIONAR LAS COLUMNAS
        f_tblDetalleRepuesto.getColumnModel().getColumn(0).setPreferredWidth(5);
        f_tblDetalleRepuesto.getColumnModel().getColumn(0).setResizable(true);
        f_tblDetalleRepuesto.getColumnModel().getColumn(1).setPreferredWidth(15);
        f_tblDetalleRepuesto.getColumnModel().getColumn(1).setResizable(true);
        f_tblDetalleRepuesto.getColumnModel().getColumn(2).setPreferredWidth(500);
        f_tblDetalleRepuesto.getColumnModel().getColumn(2).setResizable(true);
        f_tblDetalleRepuesto.getColumnModel().getColumn(3).setPreferredWidth(5);
        f_tblDetalleRepuesto.getColumnModel().getColumn(3).setResizable(true);
        f_tblDetalleRepuesto.getColumnModel().getColumn(4).setPreferredWidth(15);
        f_tblDetalleRepuesto.getColumnModel().getColumn(4).setResizable(true);
        f_tblDetalleRepuesto.getColumnModel().getColumn(5).setPreferredWidth(10);
        f_tblDetalleRepuesto.getColumnModel().getColumn(5).setResizable(true);
        f_tblDetalleRepuesto.getColumnModel().getColumn(6).setPreferredWidth(15);
        f_tblDetalleRepuesto.getColumnModel().getColumn(6).setResizable(true);
    }

    private void reiniciarVariables() {
        valorIgv = 0.0;
        for (int l = 0; l < cantList.length; l++) {
            for (int j = 0; j < 2; j++) {
                cantList[l][j] = null;
            }
        }
        item = 0;
        subTotal = 0.0;
        importT = 0.0;
        importTmasIgv = 0.0;
        igvST = 0.0;
        igvT = 0.0;
        montoCuota = 0.0;
        checkStock = 0;
    }

    public static String fechaActual() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");//formato de fecha
        return formatoFecha.format(LocalDateTime.now());
    }
    public static String HoraActual() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("HH:mm:ss");//formato de fecha
        return formatoFecha.format(LocalDateTime.now());
    }

    private void imprimirFactura() {
        JasperReport reporte = null;
        String path = "E:\\DOC\\programa\\SisInventarioFacturacion\\src\\Reportes\\FacturaVentaCabecera.jasper";
        Map parametro = new HashMap();
        parametro.put("venta_cabecera_iden", synfactura);//venta_cabecera_iden
        try {
            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametro, c);
            JasperViewer view = new JasperViewer(jprint, false);
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(frmBoletaVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getInfoAjustes() {
        //MÉTODO PARA ALMACENAR LOS AJUSTES GENERALES
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        try {
            String login = "SELECT * FROM tbl_ajuste WHERE ajuste_iden = (SELECT MAX(ajuste_iden) FROM tbl_ajuste)";
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(login);
            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("ajus_pGanancia");
                records[1] = rs.getString("ajus_igv");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        valorIgv = Double.parseDouble(records[1]);
        porcentajeGanancia = Float.parseFloat(records[0]);
    }

    //MÉTODO PARA OBTENER ID DE LA BOLETA
    public void getInfoFactura(String synboleta) {
        String[] records = new String[1];
        //Modelo para establecer en la tabla
        try {
            String login = "SELECT * FROM tbl_venta_cabecera WHERE vc_codf = '" + synboleta + "'";
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(login);
            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("venta_cabecera_iden");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        this.synfactura = Integer.parseInt(records[0]);
    }

    //MÉTODO PARA OBTENER LOS VALORES DE LA TABLA CONTADOR
    private void getInfoContFactura() {
        String[] numFactura = new String[2];
        try {
            String login = "SELECT *FROM tbl_contador_fact WHERE contador_fact_iden= (SELECT MAX(contador_fact_iden) FROM tbl_contador_fact)";
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(login);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                numFactura[0] = rs.getString("cont_cod");
                numFactura[1] = rs.getString("cont_num");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        this.cont_cod = numFactura[0];
        this.cont_num = numFactura[1];
    }

    //MÉTODO PARA OBTENER DATOS DEL CLIENTE
    public void getInfoTipoDoc() {
        String[] records = new String[1];
        //Modelo para establecer en la tabla
        try {
            String login = "SELECT * FROM tbl_tipo_doc WHERE tipo_doc_desc= 'FACTURA'";
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(login);
            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("tipo_doc_iden");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        this.tipo_doc_iden = Integer.parseInt(records[0]);
    }

    //MÉTODO PARA OBTENER DATOS DEL USUARIO
    public void getInfoUser(String pass) {
        String[] records = new String[1];
        //Modelo para establecer en la tabla
        try {
            String login = "SELECT usuario_iden FROM tbl_usuario WHERE user_pass = '" + pass + "'";
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(login);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("usuario_iden");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        this.usuario_iden = Integer.parseInt(records[0]);
    }

    //MÉTODO PARA OBTENER DATOS DEL CLIENTE
    public void getInfoCliente(String info, String doc) {
        String[] records = new String[1];
        //Modelo para establecer en la tabla
        try {
            if (doc.equals("RUC")) {
                String login = "SELECT cliente_iden FROM tbl_cliente WHERE cli_ruc= '" + info + "'";
                //crea cuando la devuelve resultados o registros
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery(login);
                while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                    records[0] = rs.getString("cliente_iden");
                }
                this.cliente_iden = Integer.parseInt(records[0]);
            } else if (doc.equals("DNI")) {
                String login = "SELECT cliente_iden FROM tbl_cliente WHERE cli_ruc = '" + info + "'";
                //crea cuando la devuelve resultados o registros
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery(login);
                while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                    records[0] = rs.getString("cliente_iden");
                }
                this.cliente_iden = Integer.parseInt(records[0]);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: Cliente no registrado, agregue nuevo cliente " + e.getMessage());
            f_txtRuc.setText("");
        }
    }

    //MÉTODO PARA OBTENER DATOS DEL PRODUCTO
    public void getInfoProducto(String codigo) {
        String[] records = new String[1];
        //Modelo para establecer en la tabla
        try {
            String login = "SELECT producto_iden FROM tbl_producto WHERE prod_cod = '" + codigo + "'";
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(login);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("producto_iden");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        this.producto_iden = Integer.parseInt(records[0]);
    }

    private void limpiarCabecera() {
        f_txtRuc.setText("");
        f_txtCliente.setText("");
        f_txtDireccion.setText("");
    }

    private void limpiarDetalle() {
        d_txtCantidad.setText("0");
        f_txtCodigo.setText("");
        f_txtDescripcion.setText("");
        f_txtImpuesto.setText(String.valueOf(valorIgv / 100));
        f_txtPrecio.setText("0.0");
        f_txtSubTotal.setText("0.0");
    }

    private void limpiarImporte() {
        f_txtPendientePago.setText("0.0");
        f_txtImporte.setText("");
        f_txtIGVtotal.setText("");
        f_txtImporteTotal.setText("");
    }

    private void habilitar(boolean t) {
//        f_cbxTipoDoc.setEnabled(t);
        f_txtRuc.setEnabled(t);
        f_btnBuscar.setEnabled(t);
        f_btnAgregarProducto.setEnabled(t);
        d_btnAgregar.setEnabled(t);
        f_btnQuitar.setEnabled(t);
        f_btnRegistrar.setEnabled(t);
        f_btnNuevaCabecera.setEnabled(!t);
        f_btnCancelar.setEnabled(t);
        f_btnAgregarNuevoCliente.setEnabled(t);
    }

    //GENERAR EL CÓDIGO  O NÚMERO DE LA FACTURA   ====  F001-00000001
    private void generarNumFactura() {
        long num = Long.parseLong(cont_num);
        int serie = Integer.parseInt(cont_cod);

        if (serie == 999 && num == 99999999) {
            JOptionPane.showMessageDialog(null, "YA NOPUEDE EMITIR FACTURA ELECTRÓNICA");
        } else {
            if (num == 99999999) {
                serie = serie + 1;
                String cero = "";
                for (int i = 0; i < 3 - String.valueOf(serie).length(); i++) {
                    cero = cero + "0";
                }
                txtNumFactura.setText("F" + cero + serie + "-00000001");
            } else {
                num = num + 1;
                String cero = "";
                for (int i = 0; i < 8 - String.valueOf(num).length(); i++) {
                    cero = cero + "0";
                }
                txtNumFactura.setText("F" + cont_cod + "-" + cero + num);
            }
        }
    }

    //REGISTRA LA SERIA Y NÚMERO DE LA FACTURA
    private void registrarNumFactura() {
        String codBoleta = txtNumFactura.getText();
        String[] digBoleta = codBoleta.split("");
        try {
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into  tbl_contador_fact (cont_cod, cont_num) value (?,?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, digBoleta[1] + digBoleta[2] + digBoleta[3]);
            pst.setString(2, digBoleta[5] + digBoleta[6] + digBoleta[7] + digBoleta[8] + digBoleta[9] + digBoleta[10] + digBoleta[11] + digBoleta[12]);

            pst.execute();//ejecutar las instrucciones          

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: " + e.getMessage());
        }
    }

    //INSERTAR LOS DETALLES E LA VENTA
    private void insertVentaCabecera() {
        String[] bol = txtNumFactura.getText().split("");
        String serie = bol[0] + bol[1] + bol[2] + bol[3];
        String num = bol[5] + bol[6] + bol[7] + bol[8] + bol[9] + bol[10] + bol[11] + bol[12];
        
        hora_de_emision=HoraActual();
        numero_documento = num;
        apellidos_y_nombres_o_razon_social = f_txtCliente.getText();
        direccion=f_txtDireccion.getText();
        numero_documento_cliente = f_txtRuc.getText();
        codigo_tipo_moneda = (String) f_txtMoneda.getSelectedItem();
        serie_documento = serie;
        fecha_de_vencimiento = fechaActual();
        fecha_de_emision = fechaActual();
        total_igv = Double.parseDouble(f_txtIGVtotal.getText());
        total_operaciones_gravadas = Double.parseDouble(f_txtImporte.getText());
        total_venta = Double.parseDouble(f_txtImporteTotal.getText());
        total_valor = Double.parseDouble(f_txtImporte.getText());
        total_impuestos = Double.parseDouble(f_txtIGVtotal.getText());
        
        Post cp = new Post();
        cp.apiConsume(serie_documento,numero_documento,fecha_de_emision,hora_de_emision,codigo_tipo_documento,codigo_tipo_moneda,fecha_de_vencimiento,codigo_tipo_documento_identidad,numero_documento_cliente,apellidos_y_nombres_o_razon_social,direccion,total_operaciones_gravadas,total_igv,total_impuestos,total_valor,total_venta,info_item_descripcion,info_item_cantidad,info_item_valor_unitario,info_item_precio_unitario,info_item_total_base_igv,info_item_porcentaje_igv,info_item_total_igv,info_item_total_impuestos,info_item_total_valor_item,info_item_total_item);
        
        String dateFV = fechaActual();
        try {
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into  tbl_venta_cabecera (usuario_iden, cliente_iden, tipo_doc_iden, vc_codf, vc_impt, vc_igv, vc_fecha,vc_mone, vc_cond_pago, vc_imp, vc_letras_impt,vc_serie,vc_num) value (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setInt(1, usuario_iden);
            pst.setInt(2, cliente_iden);
            pst.setInt(3, tipo_doc_iden);
            pst.setString(4, txtNumFactura.getText());
            pst.setDouble(5, Double.parseDouble(f_txtImporteTotal.getText()));
            pst.setDouble(6, Double.parseDouble(f_txtIGVtotal.getText()));
            pst.setString(7, dateFV);
            if (f_txtMoneda.getSelectedItem().equals("PEN")) {
                pst.setString(8, "SOLES");
            }
            if (f_cbxFormaPago.getSelectedItem().equals("CONTADO")) {
                pst.setString(9, "CONTADO");
            }
            pst.setDouble(10, Double.parseDouble(f_txtImporte.getText()));
            pst.setString(11, f_lbl_letrasImporteTotal.getText());
            pst.setString(12, serie);
            pst.setString(13, num);

            pst.execute();//ejecutar las instrucciones          

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: " + e.getMessage());
        }

        //SELECCIONAR ID DE CABECERA DE COMPRA
        String[] records = new String[1];
        //Modelo para establecer en la tabla
        try {
            String login = "SELECT venta_cabecera_iden FROM tbl_venta_cabecera WHERE vc_fecha = '" + dateFV + "'";
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(login);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("venta_cabecera_iden");
            }
            JOptionPane.showMessageDialog(null, "Se ha registrado con éxito la venta");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        getInfoFactura(txtNumFactura.getText());
        insertVentaDetalle(Integer.parseInt(records[0]));
    }

    //MÉTODO PARA INSERTAR LAS COMPRAS
    private void insertVentaDetalle(int venta_cabecera_iden) {
        for (int i = 0; i < infoDventaId_productos.length; i++) {
            try {
                //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
                String comandSQL = "Insert Into  tbl_venta_detalle (venta_cabecera_iden, producto_iden, vd_cant, vd_punit, vd_igv, vd_subt, vd_fecha) value (?,?,?,?,?,?,?)";
                PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

                pst.setInt(1, venta_cabecera_iden);
                pst.setInt(2, Integer.parseInt(String.valueOf(infoDventaId_productos[i])));
                pst.setInt(3, Integer.parseInt(String.valueOf(infoDventaCantidadVenta[i])));
                pst.setDouble(4, Double.parseDouble(String.valueOf(infoDventaPrecioVenta[i])));
                pst.setDouble(5, Double.parseDouble(String.valueOf(infoDventaIgv[i])));
                pst.setDouble(6, Double.parseDouble(String.valueOf(infoDventaSubTotalVenta[i])));
                pst.setString(7, fechaActual());

                pst.execute();//ejecutar las instrucciones          

            } catch (NumberFormatException | SQLException e) {
                JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: " + e.getMessage());
            }

            //CONSULTANDO EL ID EN LA TABLA STOCK
            String[] records = new String[2];
            //Modelo para establecer en la tabla
            try {
                String login = "SELECT producto_iden, sock_cant FROM tbl_stock WHERE producto_iden = '" + Integer.parseInt(String.valueOf(infoDventaId_productos[i])) + "'";
                //crea cuando la devuelve resultados o registros
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery(login);

                while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                    records[0] = rs.getString("producto_iden");
                    records[1] = rs.getString("sock_cant");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

            //ACTUALIZANDO EL STOVK DELPRODUCTO CORRESPONDIENTE
            int stock = Integer.parseInt(records[1]) - Integer.parseInt(String.valueOf(infoDventaCantidadVenta[i]));
            try {
                //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
                String comandSQL = "UPDATE tbl_stock SET sock_cant = '" + stock + "' WHERE producto_iden = '" + records[0] + "'";
                PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

                pst.execute();//ejecutar las instrucciones          

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha actualizado: " + e.getMessage());
            }
        }
    }

    //CONSULTAR A TRAVÉS DE RUC
    private void consultarRUC(String ruc) {
        try {
            String sql = "SELECT * FROM tbl_cliente WHERE cli_ruc= '" + ruc + "'";
            PreparedStatement st = c.prepareStatement(sql); //crea cuando la devuelve resultados o registros

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String tipo_cliente = rs.getString("cli_tipc");
                if (tipo_cliente.equals("EMPRESA")) {
                    f_txtCliente.setText(rs.getString("cli_desc"));
                    f_txtDireccion.setText(rs.getString("cli_dire"));
                } else if (tipo_cliente.equals("PARTICULAR")) {
                    f_txtCliente.setText(rs.getString("cli_desc"));
                    f_txtDireccion.setText(rs.getString("cli_dire"));
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente mal especificado");
                }
            }

            rs.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    //CONSULTAR A TRAVÉS DE DNI
    private void consultarDNI(String dni) {
        try {
            String sql = "SELECT * FROM tbl_cliente WHERE cli_ruc= '" + dni + "'";
            PreparedStatement st = c.prepareStatement(sql); //crea cuando la devuelve resultados o registros

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                f_txtCliente.setText(rs.getString("cli_desc"));
                f_txtDireccion.setText(rs.getString("cli_dire"));
            }

            rs.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    //MÉTODO PARA MOSTRAR INFORMACIÓN DE LOS REPUESTOS EN TABLA
    public void MostrarProductos() {
        String[] head = {"N°", "CÓDIGO", "DESCRIPCIÓN", "STOCK"};
        String[] records = new String[4];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL = "SELECT * FROM tbl_producto INNER JOIN tbl_modelo ON tbl_producto.modelo_iden = tbl_modelo.modelo_iden INNER JOIN tbl_marca ON tbl_modelo.marca_iden = tbl_marca.marca_iden INNER JOIN tbl_stock ON tbl_producto.producto_iden = tbl_stock.producto_iden WHERE sock_cant != '0' "; //Realiza bùsqueda en la tabla
        try {
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("producto_iden");
                records[1] = rs.getString("prod_cod");
                if (rs.getString("prod_caract").equals("") || rs.getString("prod_caract") == null) {
                    records[2] = rs.getString("prod_desc")+ ", "+ rs.getString("mod_desc")+ rs.getString("marc_desc");
                } else {
                    records[2] = rs.getString("prod_desc") + ", " + rs.getString("prod_caract")+ ", "+ rs.getString("mod_desc")+ rs.getString("marc_desc");
                }
                records[3] = rs.getString("sock_cant");
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            d_tblRepuestos.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de búsqueda " + e.getMessage());
        }

        //REDIMENSIONAR LAS COLUMNAS
        d_tblRepuestos.getColumnModel().getColumn(0).setPreferredWidth(5);
        d_tblRepuestos.getColumnModel().getColumn(0).setResizable(true);
        d_tblRepuestos.getColumnModel().getColumn(1).setPreferredWidth(15);
        d_tblRepuestos.getColumnModel().getColumn(1).setResizable(true);
        d_tblRepuestos.getColumnModel().getColumn(2).setPreferredWidth(400);
        d_tblRepuestos.getColumnModel().getColumn(2).setResizable(true);
        d_tblRepuestos.getColumnModel().getColumn(3).setPreferredWidth(5);
        d_tblRepuestos.getColumnModel().getColumn(3).setResizable(true);
    }

    public void BuscarRepuestos() {
        String Buscar = d_txtBuscar.getText();
        String[] head = {"N°", "CÓDIGO", "DESCRIPCIÓN", "STOCK"};
        String[] records = new String[4];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL = "SELECT * FROM tbl_producto INNER JOIN tbl_modelo ON tbl_producto.modelo_iden = tbl_modelo.modelo_iden INNER JOIN tbl_marca ON tbl_modelo.marca_iden = tbl_marca.marca_iden INNER JOIN tbl_stock ON tbl_producto.producto_iden = tbl_stock.producto_iden WHERE sock_cant != '0' AND (prod_cod LIKE '%" + Buscar + "%' OR prod_desc LIKE '%" + Buscar + "%')"; //Realiza bùsqueda en la tabla
        try {
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("producto_iden");
                records[1] = rs.getString("prod_cod");
                if (rs.getString("prod_caract").equals("") || rs.getString("prod_caract") == null) {
                    records[2] = rs.getString("prod_desc")+ ", "+ rs.getString("mod_desc")+ rs.getString("marc_desc");
                } else {
                    records[2] = rs.getString("prod_desc") + ", " + rs.getString("prod_caract")+ ", "+ rs.getString("mod_desc")+ rs.getString("marc_desc");
                }
                records[3] = rs.getString("sock_cant");
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            d_tblRepuestos.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de búsqueda " + e.getMessage());
        }

        //REDIMENSIONAR LAS COLUMNAS
        d_tblRepuestos.getColumnModel().getColumn(0).setPreferredWidth(5);
        d_tblRepuestos.getColumnModel().getColumn(0).setResizable(true);
        d_tblRepuestos.getColumnModel().getColumn(1).setPreferredWidth(15);
        d_tblRepuestos.getColumnModel().getColumn(1).setResizable(true);
        d_tblRepuestos.getColumnModel().getColumn(2).setPreferredWidth(400);
        d_tblRepuestos.getColumnModel().getColumn(2).setResizable(true);
        d_tblRepuestos.getColumnModel().getColumn(3).setPreferredWidth(5);
        d_tblRepuestos.getColumnModel().getColumn(3).setResizable(true);
    }

    public void calc() {
        Double monto = Integer.parseInt(String.valueOf(d_txtCantidad.getText())) * Double.parseDouble(String.valueOf(f_txtPrecio.getText()));
        f_txtSubTotal.setText(String.valueOf(Math.round(monto * 100.0) / 100.0));
        Double ImpuestoIgv = Double.parseDouble(f_txtSubTotal.getText()) / 1.18 * valorIgv / 100;
        f_txtImpuesto.setText(String.valueOf(Math.round(ImpuestoIgv * 100.0) / 100.0));
    }

    private void calcImportTotal() {
        subTotal = Double.parseDouble(f_txtSubTotal.getText());
        igvST = Double.parseDouble(f_txtImpuesto.getText());
        importT = importT + subTotal;
        igvT = igvT + igvST;
        f_txtImporte.setText(String.valueOf(Math.round(importT / 1.18 * 100.0) / 100.0));
        f_txtIGVtotal.setText(String.valueOf(Math.round(igvT * 100.0) / 100.0));
        importTmasIgv = importT / 1.18 + igvT;
        f_txtImporteTotal.setText(String.valueOf(Math.round(importTmasIgv * 100.0) / 100.0));
        f_txtMontoCuota.setText(String.valueOf(Math.round(importTmasIgv * 100.0) / 100.0));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        detalleVenta = new javax.swing.JDialog();
        d_btnSeleccionar = new javax.swing.JButton();
        d_btnCancelar = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        d_txtBuscar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        d_tblRepuestos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtNumFactura = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        f_cbxTipoDoc = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        f_txtRuc = new javax.swing.JTextField();
        f_btnBuscar = new javax.swing.JButton();
        f_txtCliente = new javax.swing.JTextField();
        f_txtDireccion = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        f_cbxFormaPago = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        f_cbxCuotas = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        f_txtMontoCuota = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        f_txtMoneda = new javax.swing.JComboBox<>();
        f_btnAgregarNuevoCliente = new javax.swing.JButton();
        f_btnNuevaCabecera = new javax.swing.JButton();
        f_btnCancelar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        d_txtCantidad = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        f_txtCodigo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        f_txtDescripcion = new javax.swing.JTextField();
        f_btnAgregarProducto = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        f_txtPrecio = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        d_btnAgregar = new javax.swing.JButton();
        f_btnQuitar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        f_tblDetalleRepuesto = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        f_txtSubTotal = new javax.swing.JTextField();
        f_txtImpuesto = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        f_txtPendientePago = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        f_txtImporte = new javax.swing.JTextField();
        f_txtIGVtotal = new javax.swing.JTextField();
        f_txtImporteTotal = new javax.swing.JTextField();
        f_btnRegistrar = new javax.swing.JButton();
        f_btnImprimir = new javax.swing.JButton();
        txtFecha = new javax.swing.JTextField();
        f_lbl_letrasImporteTotal = new javax.swing.JLabel();

        detalleVenta.setTitle("BUSCAR PRODUCTOS");

        d_btnSeleccionar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        d_btnSeleccionar.setText("SELECCIONAR");
        d_btnSeleccionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d_btnSeleccionarActionPerformed(evt);
            }
        });

        d_btnCancelar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        d_btnCancelar.setText("CANCELAR");
        d_btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d_btnCancelarActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel25.setText("Buscar:");

        d_txtBuscar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        d_txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                d_txtBuscarKeyReleased(evt);
            }
        });

        d_tblRepuestos.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        d_tblRepuestos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(d_tblRepuestos);

        javax.swing.GroupLayout detalleVentaLayout = new javax.swing.GroupLayout(detalleVenta.getContentPane());
        detalleVenta.getContentPane().setLayout(detalleVentaLayout);
        detalleVentaLayout.setHorizontalGroup(
            detalleVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detalleVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detalleVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detalleVentaLayout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(d_txtBuscar))
                    .addGroup(detalleVentaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(d_btnSeleccionar)
                        .addGap(35, 35, 35)
                        .addComponent(d_btnCancelar)))
                .addContainerGap())
        );
        detalleVentaLayout.setVerticalGroup(
            detalleVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detalleVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detalleVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(d_txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(detalleVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(d_btnSeleccionar)
                    .addComponent(d_btnCancelar))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REGISTRO DE VENTAS");
        setPreferredSize(new java.awt.Dimension(1290, 770));

        jLabel1.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel1.setText("FACTURA N°:");

        txtNumFactura.setEditable(false);
        txtNumFactura.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 51));
        jLabel2.setText("Cabecera:");

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel3.setText("Tipo DOC:");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel4.setText("Cliente:");

        jLabel5.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel5.setText("Dirección:");

        f_cbxTipoDoc.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_cbxTipoDoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RUC", "DNI" }));
        f_cbxTipoDoc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_cbxTipoDoc.setEnabled(false);
        f_cbxTipoDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_cbxTipoDocActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel7.setText("N° DOC:");

        f_txtRuc.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_txtRuc.setEnabled(false);
        f_txtRuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_txtRucActionPerformed(evt);
            }
        });
        f_txtRuc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                f_txtRucKeyTyped(evt);
            }
        });

        f_btnBuscar.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        f_btnBuscar.setText("BUSCAR");
        f_btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_btnBuscar.setEnabled(false);
        f_btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_btnBuscarActionPerformed(evt);
            }
        });

        f_txtCliente.setEditable(false);
        f_txtCliente.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N

        f_txtDireccion.setEditable(false);
        f_txtDireccion.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel6.setText("Forma Pago:");

        f_cbxFormaPago.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_cbxFormaPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CONTADO", "CRÉDITO" }));
        f_cbxFormaPago.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_cbxFormaPago.setEnabled(false);
        f_cbxFormaPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_cbxFormaPagoActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel9.setText("Cuotas:");

        f_cbxCuotas.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_cbxCuotas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        f_cbxCuotas.setEnabled(false);
        f_cbxCuotas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_cbxCuotasActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel10.setText("Monto cuota:");

        f_txtMontoCuota.setEditable(false);
        f_txtMontoCuota.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_txtMontoCuota.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel11.setText("Moneda:");

        f_txtMoneda.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_txtMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PEN" }));
        f_txtMoneda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_txtMoneda.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(41, 41, 41)
                        .addComponent(f_cbxCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(f_txtMontoCuota))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(f_cbxFormaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(f_txtMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(f_cbxFormaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(f_cbxCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f_txtMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(f_txtMontoCuota, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        f_btnAgregarNuevoCliente.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        f_btnAgregarNuevoCliente.setText("AGREGAR CLIENTE");
        f_btnAgregarNuevoCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_btnAgregarNuevoCliente.setEnabled(false);
        f_btnAgregarNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_btnAgregarNuevoClienteActionPerformed(evt);
            }
        });

        f_btnNuevaCabecera.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        f_btnNuevaCabecera.setText("NUEVO");
        f_btnNuevaCabecera.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_btnNuevaCabecera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_btnNuevaCabeceraActionPerformed(evt);
            }
        });

        f_btnCancelar.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        f_btnCancelar.setText("CANCELAR");
        f_btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_btnCancelar.setEnabled(false);
        f_btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(36, 36, 36)
                            .addComponent(f_cbxTipoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(35, 35, 35)
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(f_txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                            .addComponent(f_btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(47, 47, 47)
                            .addComponent(f_btnAgregarNuevoCliente))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(f_txtCliente)
                                .addComponent(f_txtDireccion)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(f_btnNuevaCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(f_btnCancelar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(f_btnNuevaCabecera)
                    .addComponent(f_btnCancelar))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(f_cbxTipoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(f_txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(f_btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(f_btnAgregarNuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(f_txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(f_txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(19, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jLabel8.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel8.setText("Fecha de emisión:");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 51));
        jLabel12.setText("Detalle:");

        jLabel13.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel13.setText("Cantidad:");

        d_txtCantidad.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        d_txtCantidad.setText("0");
        d_txtCantidad.setEnabled(false);
        d_txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d_txtCantidadActionPerformed(evt);
            }
        });
        d_txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                d_txtCantidadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                d_txtCantidadKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel14.setText("Código:");

        f_txtCodigo.setEditable(false);
        f_txtCodigo.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel15.setText("Descripción:");

        f_txtDescripcion.setEditable(false);
        f_txtDescripcion.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N

        f_btnAgregarProducto.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        f_btnAgregarProducto.setText("+");
        f_btnAgregarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_btnAgregarProducto.setEnabled(false);
        f_btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_btnAgregarProductoActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel16.setText("P. Unitario:");

        f_txtPrecio.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_txtPrecio.setText("0.0");
        f_txtPrecio.setEnabled(false);
        f_txtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_txtPrecioActionPerformed(evt);
            }
        });
        f_txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                f_txtPrecioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                f_txtPrecioKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel17.setText("Tributo:");

        d_btnAgregar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        d_btnAgregar.setText("AGREGAR");
        d_btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_btnAgregar.setEnabled(false);
        d_btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d_btnAgregarActionPerformed(evt);
            }
        });

        f_btnQuitar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        f_btnQuitar.setText("QUITAR");
        f_btnQuitar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_btnQuitar.setEnabled(false);
        f_btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_btnQuitarActionPerformed(evt);
            }
        });

        f_tblDetalleRepuesto.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_tblDetalleRepuesto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Código", "Descripción", "Cantidad", "P. Venta", "Monto tributo", "Subtotal"
            }
        ));
        jScrollPane1.setViewportView(f_tblDetalleRepuesto);

        jLabel26.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel26.setText("SubTotal:");

        f_txtSubTotal.setEditable(false);
        f_txtSubTotal.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_txtSubTotal.setText("0.0");

        f_txtImpuesto.setEditable(false);
        f_txtImpuesto.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        f_txtImpuesto.setText("0.0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(f_txtCodigo)
                                    .addComponent(d_txtCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(f_txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(36, 36, 36)
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(f_txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(f_txtImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(f_txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(30, 30, 30)
                                .addComponent(f_btnAgregarProducto)
                                .addGap(63, 63, 63))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(d_btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(f_btnQuitar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(1, 1, 1)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(d_txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(f_txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(f_btnAgregarProducto))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17)
                                .addComponent(f_txtImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(f_txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel14)
                                .addComponent(f_txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16)
                                .addComponent(f_txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(d_btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(f_btnQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel18.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel18.setText("SON:");

        jLabel19.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel20.setText("Importe Neto pendiente de Pago:");

        f_txtPendientePago.setEditable(false);
        f_txtPendientePago.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        jLabel21.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel21.setText("* Solo para forma de pago a crédito");

        jLabel22.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel22.setText("Importe:");

        jLabel23.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel23.setText("IGV:");

        jLabel24.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel24.setText("Importe Total:");

        f_txtImporte.setEditable(false);
        f_txtImporte.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        f_txtIGVtotal.setEditable(false);
        f_txtIGVtotal.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        f_txtImporteTotal.setEditable(false);
        f_txtImporteTotal.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        f_txtImporteTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                f_txtImporteTotalKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(f_txtIGVtotal)
                    .addComponent(f_txtImporteTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(f_txtImporte))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(f_txtImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(f_txtIGVtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(f_txtImporteTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        f_btnRegistrar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        f_btnRegistrar.setText("REGISTRAR");
        f_btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_btnRegistrar.setEnabled(false);
        f_btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_btnRegistrarActionPerformed(evt);
            }
        });

        f_btnImprimir.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        f_btnImprimir.setText("IMPRIMIR");
        f_btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_btnImprimir.setEnabled(false);
        f_btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_btnImprimirActionPerformed(evt);
            }
        });

        txtFecha.setEditable(false);
        txtFecha.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N

        f_lbl_letrasImporteTotal.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 657, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(472, 472, 472)
                                .addComponent(jLabel21))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(f_txtPendientePago, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(f_btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(58, 58, 58)
                                        .addComponent(f_btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(f_lbl_letrasImporteTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNumFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(f_lbl_letrasImporteTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel18)
                                .addComponent(jLabel19)))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(f_txtPendientePago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(f_btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(f_btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void f_btnAgregarNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_btnAgregarNuevoClienteActionPerformed
        frmClientes nc = new frmClientes();
        nc.setVisible(true);
    }//GEN-LAST:event_f_btnAgregarNuevoClienteActionPerformed

    private void f_cbxTipoDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_cbxTipoDocActionPerformed
    }//GEN-LAST:event_f_cbxTipoDocActionPerformed

    private void f_btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_btnBuscarActionPerformed
        if (f_txtRuc.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "No escribió el " + f_cbxTipoDoc.getSelectedItem());
        } else if (f_cbxTipoDoc.getSelectedItem().equals("DNI")) {
            consultarDNI(f_txtRuc.getText());
            getInfoCliente(f_txtRuc.getText(), String.valueOf(f_cbxTipoDoc.getSelectedItem()));
        } else if (f_cbxTipoDoc.getSelectedItem().equals("RUC")) {
            consultarRUC(f_txtRuc.getText());
            getInfoCliente(f_txtRuc.getText(), String.valueOf(f_cbxTipoDoc.getSelectedItem()));
        }
    }//GEN-LAST:event_f_btnBuscarActionPerformed

    private void f_btnNuevaCabeceraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_btnNuevaCabeceraActionPerformed
        limpiarCabecera();
        limpiarDetalle();
        limpiarImporte();
        habilitar(true);
        getInfoContFactura();
        generarNumFactura();
        f_btnImprimir.setEnabled(false);
    }//GEN-LAST:event_f_btnNuevaCabeceraActionPerformed

    private void f_cbxFormaPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_cbxFormaPagoActionPerformed
        if (f_cbxFormaPago.getItemAt(f_cbxFormaPago.getSelectedIndex()).equals("CONTADO")) {
            f_cbxCuotas.setEnabled(false);
            f_txtMontoCuota.setEnabled(false);
            f_cbxCuotas.setSelectedIndex(0);

        } else if (f_cbxFormaPago.getItemAt(f_cbxFormaPago.getSelectedIndex()).equals("CRÉDITO")) {
            f_cbxCuotas.setEnabled(true);
            f_txtMontoCuota.setEnabled(true);
        } else {

        }
    }//GEN-LAST:event_f_cbxFormaPagoActionPerformed

    private void d_btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d_btnSeleccionarActionPerformed
        // TODO add your handling code here:
        int fsel = d_tblRepuestos.getSelectedRow();
        try {
            String codigo, descripcion;
            int id;
            float precioVenta;

            if (fsel == -1) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un producto");
            } else {
                m = (DefaultTableModel) d_tblRepuestos.getModel();
                codigo = d_tblRepuestos.getValueAt(fsel, 1).toString();
                descripcion = d_tblRepuestos.getValueAt(fsel, 2).toString();

                //PRECIO DE VENTA PROMEDIADO CON LOS 2 ÚLTIMOS PRECIOS INGRESADOS
                id = Integer.parseInt(String.valueOf(d_tblRepuestos.getValueAt(fsel, 0)));
                String[] records = new String[2];
                //Modelo para establecer en la tabla
                try {
                    String login = "SELECT max(cd_cunit) AS pmin, min(cd_cunit) AS pmax FROM tbl_compra_detalle WHERE producto_iden = '" + id + "'";
                    //crea cuando la devuelve resultados o registros
                    Statement st = c.createStatement();
                    ResultSet rs = st.executeQuery(login);

                    while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                        records[0] = rs.getString("pmin");
                        records[1] = rs.getString("pmax");
                    }

                    if(records[0]==null && records[1]==null){
                        JOptionPane.showMessageDialog(null, "Digite el precio de venta");
                        f_txtCodigo.setText(codigo);
                        f_txtDescripcion.setText(descripcion);
                        f_txtPrecio.setEnabled(true);
                    }else{
                        float promedioPcompra = (Float.parseFloat(records[0]) + Float.parseFloat(records[1])) / 2;
                        //FÓRMULA: P= C*(100/100-R) Referencia:(https://www.zendesk.com.mx/blog/costo-de-venta-formula/)
                        float calculoPorcentaje = 100 / (100 - porcentajeGanancia);
                        precioVenta = promedioPcompra * calculoPorcentaje;
                        f_txtPrecio.setEnabled(false);
                        f_txtCodigo.setText(codigo);
                        f_txtDescripcion.setText(descripcion);
                        f_txtPrecio.setText(String.valueOf(Math.round(precioVenta * 100.0) / 100.0));
                    }
                    detalleVenta.setVisible(false);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
                d_txtCantidad.setEnabled(true);
            }

        } catch (Exception e) {

        }
    }//GEN-LAST:event_d_btnSeleccionarActionPerformed

    private void f_btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_btnAgregarProductoActionPerformed
        detalleVenta.setSize(584, 654);
        detalleVenta.setLocationRelativeTo(null);
        detalleVenta.setModal(true);
        detalleVenta.setVisible(true);
    }//GEN-LAST:event_f_btnAgregarProductoActionPerformed

    private void d_btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d_btnCancelarActionPerformed
        detalleVenta.setVisible(false);
    }//GEN-LAST:event_d_btnCancelarActionPerformed

    private void d_btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d_btnAgregarActionPerformed
       if (f_txtDescripcion.getText().equals("") || f_txtCodigo.getText().equals("") || d_txtCantidad.getText().equals("0")) {
            JOptionPane.showMessageDialog(null, "Hay campos vacìos");
        } else {
            //CONSULTAR STOCK
            String[] records = new String[2];
            //Modelo para establecer en la tabla
            try {
                String login = "SELECT * FROM tbl_stock INNER JOIN tbl_producto ON tbl_stock.producto_iden = tbl_producto.producto_iden WHERE prod_cod = '" + f_txtCodigo.getText() + "'";
                //crea cuando la devuelve resultados o registros
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery(login);

                while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                    records[0] = rs.getString("prod_cod");
                    records[1] = rs.getString("sock_cant");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

            for (int i = 0; i < cantList.length; i++) {
                if (cantList[i][0] != null && records[0].equals(cantList[i][0])) {//0,0

                    checkStock = Integer.parseInt(cantList[i][1]) - Integer.parseInt(d_txtCantidad.getText()); //2 -2

                    if (d_txtCantidad.getText().equals("") || f_txtDescripcion.getText().equals("") || f_txtCodigo.getText().equals("") || f_txtPrecio.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Hay campos vacíos");
                    } else if (checkStock < 0) {
                        JOptionPane.showMessageDialog(null, "No hay suficiente cantidad del producto en el almacén");
                    } else if (checkStock == 0) {
                        JOptionPane.showMessageDialog(null, "El producto " + f_txtDescripcion.getText() + " va tener stock 0, esta es su última venta");
                        getInfoProducto(f_txtCodigo.getText());
                        
//===================================AGREGAMOS PARA EL REST ==========================================
                        item_descripcion.add(f_txtDescripcion.getText());
                        item_total_igv.add(f_txtImpuesto.getText());
                        item_total_item.add(f_txtSubTotal.getText());
                        item_total_valor_item.add(String.valueOf(Math.round(Double.parseDouble(f_txtSubTotal.getText())/1.18 * 100.0) / 100.0));
                        item_precio_unitario.add(f_txtPrecio.getText());//ok
                        item_porcentaje_igv.add(String.valueOf(valorIgv));//ok
                        item_total_base_igv.add(String.valueOf(Math.round(Double.parseDouble(f_txtSubTotal.getText())/1.18 * 100.0) / 100.0));//ok
                        item_cantidad.add(d_txtCantidad.getText());
                        item_valor_unitario.add(String.valueOf(Math.round(Double.parseDouble(f_txtPrecio.getText())/1.18 * 100.0) / 100.0));//ok
                        item_total_impuestos.add(f_txtImpuesto.getText());
//====================================================================================================
                        
                        
                        id_productos.add(String.valueOf(producto_iden));
                        cantidadVenta.add(d_txtCantidad.getText());
                        precioVenta.add(f_txtPrecio.getText());
                        subTotaligv.add(f_txtImpuesto.getText());
                        subTotalVenta.add(f_txtSubTotal.getText());

                        item = item + 1;

                        String[] infoTabla = new String[7];

                        infoTabla[0] = String.valueOf(item);
                        infoTabla[1] = f_txtCodigo.getText();
                        infoTabla[2] = f_txtDescripcion.getText();
                        infoTabla[3] = d_txtCantidad.getText();
                        infoTabla[4] = f_txtPrecio.getText();
                        infoTabla[5] = f_txtImpuesto.getText();
                        infoTabla[6] = f_txtSubTotal.getText();

                        m1 = (DefaultTableModel) f_tblDetalleRepuesto.getModel();
                        m1.addRow(infoTabla);

                        //ASIGNA EL NUEVO STOCK DEL PRODUCTO
                        cantList[i][1] = String.valueOf(checkStock);

                        calcImportTotal();
                        limpiarDetalle();
                        f_txtPrecio.setEnabled(false);
                        d_txtCantidad.setEnabled(false);
                        
                        Numero_a_Letra d= new Numero_a_Letra();
                        double numero= 0.0;
                        numero= Double.parseDouble(f_txtImporteTotal.getText());
                        String letras= d.Convertir(String.valueOf(numero), true);
                        f_lbl_letrasImporteTotal.setText(letras);
                        
                    } else if (checkStock > 0) {
                        JOptionPane.showMessageDialog(null, "Le quedan " + checkStock + " unidades del producto " + f_txtDescripcion.getText() + " en el almacen");
                        getInfoProducto(f_txtCodigo.getText());
                        
//===================================AGREGAMOS PARA EL REST ==========================================
                        item_descripcion.add(f_txtDescripcion.getText());
                        item_total_igv.add(f_txtImpuesto.getText());
                        item_total_item.add(f_txtSubTotal.getText());
                        item_total_valor_item.add(String.valueOf(Math.round(Double.parseDouble(f_txtSubTotal.getText())/1.18 * 100.0) / 100.0));
                        item_precio_unitario.add(f_txtPrecio.getText());//ok
                        item_porcentaje_igv.add(String.valueOf(valorIgv));//ok
                        item_total_base_igv.add(String.valueOf(Math.round(Double.parseDouble(f_txtSubTotal.getText())/1.18 * 100.0) / 100.0));//ok
                        item_cantidad.add(d_txtCantidad.getText());
                        item_valor_unitario.add(String.valueOf(Math.round(Double.parseDouble(f_txtPrecio.getText())/1.18 * 100.0) / 100.0));//ok
                        item_total_impuestos.add(f_txtImpuesto.getText());
//====================================================================================================
                        
                        id_productos.add(String.valueOf(producto_iden));
                        cantidadVenta.add(d_txtCantidad.getText());
                        precioVenta.add(f_txtPrecio.getText());
                        subTotaligv.add(f_txtImpuesto.getText());
                        subTotalVenta.add(f_txtSubTotal.getText());

                        item = item + 1;

                        String[] infoTabla = new String[7];

                        infoTabla[0] = String.valueOf(item);
                        infoTabla[1] = f_txtCodigo.getText();
                        infoTabla[2] = f_txtDescripcion.getText();
                        infoTabla[3] = d_txtCantidad.getText();
                        infoTabla[4] = f_txtPrecio.getText();
                        infoTabla[5] = f_txtImpuesto.getText();
                        infoTabla[6] = f_txtSubTotal.getText();

                        m1 = (DefaultTableModel) f_tblDetalleRepuesto.getModel();
                        m1.addRow(infoTabla);

                        //ASIGNA EL NUEVO STOCK DEL PRODUCTO
                        cantList[i][1] = String.valueOf(checkStock);

                        calcImportTotal();
                        limpiarDetalle();
                        f_txtPrecio.setEnabled(false);
                        d_txtCantidad.setEnabled(false);
                        
                        Numero_a_Letra d= new Numero_a_Letra();
                        double numero= 0.0;
                        numero= Double.parseDouble(f_txtImporteTotal.getText());
                        String letras= d.Convertir(String.valueOf(numero), true);
                        f_lbl_letrasImporteTotal.setText(letras);
                    }
                    break;
                } else if (cantList[i][0] == null || cantList[i][1] == null) {

                    //ASIGNA EL NUEVO STOCK DEL PRODUCTO
                    cantList[i][0] = records[0];
                    cantList[i][1] = records[1];

                    checkStock = Integer.parseInt(cantList[i][1]) - Integer.parseInt(d_txtCantidad.getText()); //5 -3

                    if (d_txtCantidad.getText().equals("") || f_txtDescripcion.getText().equals("") || f_txtCodigo.getText().equals("") || f_txtPrecio.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Hay campos vacíos");
                    } else if (checkStock < 0) {
                        JOptionPane.showMessageDialog(null, "No hay suficiente cantidad del producto en el almacén");
                    } else if (checkStock == 0) {
                        JOptionPane.showMessageDialog(null, "El producto " + f_txtDescripcion.getText() + " va tener stock 0, esta es su última venta");
                        getInfoProducto(f_txtCodigo.getText());

//===================================AGREGAMOS PARA EL REST ==========================================
                        item_descripcion.add(f_txtDescripcion.getText());
                        item_total_igv.add(f_txtImpuesto.getText());
                        item_total_item.add(f_txtSubTotal.getText());
                        item_total_valor_item.add(String.valueOf(Math.round(Double.parseDouble(f_txtSubTotal.getText())/1.18 * 100.0) / 100.0));
                        item_precio_unitario.add(f_txtPrecio.getText());//ok
                        item_porcentaje_igv.add(String.valueOf(valorIgv));//ok
                        item_total_base_igv.add(String.valueOf(Math.round(Double.parseDouble(f_txtSubTotal.getText())/1.18 * 100.0) / 100.0));//ok
                        item_cantidad.add(d_txtCantidad.getText());
                        item_valor_unitario.add(String.valueOf(Math.round(Double.parseDouble(f_txtPrecio.getText())/1.18 * 100.0) / 100.0));//ok
                        item_total_impuestos.add(f_txtImpuesto.getText());
//====================================================================================================                        
                        
                        id_productos.add(String.valueOf(producto_iden));
                        cantidadVenta.add(d_txtCantidad.getText());
                        precioVenta.add(f_txtPrecio.getText());
                        subTotaligv.add(f_txtImpuesto.getText());
                        subTotalVenta.add(f_txtSubTotal.getText());

                        item = item + 1;

                        String[] infoTabla = new String[7];

                        infoTabla[0] = String.valueOf(item);
                        infoTabla[1] = f_txtCodigo.getText();
                        infoTabla[2] = f_txtDescripcion.getText();
                        infoTabla[3] = d_txtCantidad.getText();
                        infoTabla[4] = f_txtPrecio.getText();
                        infoTabla[5] = f_txtImpuesto.getText();
                        infoTabla[6] = f_txtSubTotal.getText();

                        m1 = (DefaultTableModel) f_tblDetalleRepuesto.getModel();
                        m1.addRow(infoTabla);

                        cantList[i][1] = String.valueOf(checkStock);

                        calcImportTotal();
                        limpiarDetalle();
                        f_txtPrecio.setEnabled(false);
                        d_txtCantidad.setEnabled(false);
                        
                        Numero_a_Letra d= new Numero_a_Letra();
                        double numero= 0.0;
                        numero= Double.parseDouble(f_txtImporteTotal.getText());
                        String letras= d.Convertir(String.valueOf(numero), true);
                        f_lbl_letrasImporteTotal.setText(letras);
                        
                    } else if (checkStock > 0) {
                        JOptionPane.showMessageDialog(null, "Le quedan " + checkStock + " unidades del producto " + f_txtDescripcion.getText() + " en el almacen");
                        getInfoProducto(f_txtCodigo.getText());

//===================================AGREGAMOS PARA EL REST ==========================================
                        item_descripcion.add(f_txtDescripcion.getText());
                        item_total_igv.add(f_txtImpuesto.getText());
                        item_total_item.add(f_txtSubTotal.getText());
                        item_total_valor_item.add(String.valueOf(Math.round(Double.parseDouble(f_txtSubTotal.getText())/1.18 * 100.0) / 100.0));
                        item_precio_unitario.add(f_txtPrecio.getText());//ok
                        item_porcentaje_igv.add(String.valueOf(valorIgv));//ok
                        item_total_base_igv.add(String.valueOf(Math.round(Double.parseDouble(f_txtSubTotal.getText())/1.18 * 100.0) / 100.0));//ok
                        item_cantidad.add(d_txtCantidad.getText());
                        item_valor_unitario.add(String.valueOf(Math.round(Double.parseDouble(f_txtPrecio.getText())/1.18 * 100.0) / 100.0));//ok
                        item_total_impuestos.add(f_txtImpuesto.getText());
//====================================================================================================                        
                        
                        id_productos.add(String.valueOf(producto_iden));
                        cantidadVenta.add(d_txtCantidad.getText());
                        precioVenta.add(f_txtPrecio.getText());
                        subTotaligv.add(f_txtImpuesto.getText());
                        subTotalVenta.add(f_txtSubTotal.getText());

                        item = item + 1;

                        String[] infoTabla = new String[7];

                        infoTabla[0] = String.valueOf(item);
                        infoTabla[1] = f_txtCodigo.getText();
                        infoTabla[2] = f_txtDescripcion.getText();
                        infoTabla[3] = d_txtCantidad.getText();
                        infoTabla[4] = f_txtPrecio.getText();
                        infoTabla[5] = f_txtImpuesto.getText();
                        infoTabla[6] = f_txtSubTotal.getText();

                        m1 = (DefaultTableModel) f_tblDetalleRepuesto.getModel();
                        m1.addRow(infoTabla);

                        cantList[i][1] = String.valueOf(checkStock);

                        calcImportTotal();
                        limpiarDetalle();
                        f_txtPrecio.setEnabled(false);
                        d_txtCantidad.setEnabled(false);
                        
                        Numero_a_Letra d= new Numero_a_Letra();
                        double numero= 0.0;
                        numero= Double.parseDouble(f_txtImporteTotal.getText());
                        String letras= d.Convertir(String.valueOf(numero), true);
                        f_lbl_letrasImporteTotal.setText(letras);
                    }
                    break;
                }
            }          
        }
    }//GEN-LAST:event_d_btnAgregarActionPerformed

    private void f_btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_btnQuitarActionPerformed
        int Qfila = f_tblDetalleRepuesto.getSelectedRow();
        if (Qfila >= 0) {

            Double Qsubtotal, Qtributo;
            String Qcod;
            int Qcant;

//===================================QUITAMOS LOS DETALLES PARA EL REST ==========================================
                        item_descripcion.remove(Qfila);
                        item_total_igv.remove(Qfila);
                        item_total_item.remove(Qfila);
                        item_total_valor_item.remove(Qfila);
                        item_precio_unitario.remove(Qfila);
                        item_porcentaje_igv.remove(Qfila);
                        item_total_base_igv.remove(Qfila);
                        item_cantidad.remove(Qfila);
                        item_valor_unitario.remove(Qfila);
                        item_total_impuestos.remove(Qfila);
//====================================================================================================                        
          
            
            id_productos.remove(Qfila);
            cantidadVenta.remove(Qfila);
            precioVenta.remove(Qfila);
            subTotaligv.remove(Qfila);
            subTotalVenta.remove(Qfila);

            Qcod = f_tblDetalleRepuesto.getValueAt(Qfila, 1).toString();
            Qcant = Integer.parseInt(f_tblDetalleRepuesto.getValueAt(Qfila, 3).toString());
            Qtributo = Double.parseDouble(f_tblDetalleRepuesto.getValueAt(Qfila, 5).toString());
            Qsubtotal = Double.parseDouble(f_tblDetalleRepuesto.getValueAt(Qfila, 6).toString());

            //CADA VEZ QUE QUITA UN PRODUCTO, LO QUE OCURRE ES QUE SE SELECCIONA EL ARRAY, SE COMPARA EL CODIGO Y SE RESTA A LA CANTIDAD ALMACENADA
            for (int i = 0; i < cantList.length; i++) {
                if (cantList[i][0] != null && Qcod.equals(cantList[i][0])) {//0,0
                    cantList[i][1] = String.valueOf(Integer.parseInt(cantList[i][1]) + Qcant);
                    JOptionPane.showMessageDialog(null, "Ahora tienes " + cantList[i][1] + " en stock");
                    break;
                }
            }

            if(importT==0){
                f_txtImporte.setText(String.valueOf(0));
                f_txtIGVtotal.setText(String.valueOf(0));
                f_txtImporteTotal.setText(String.valueOf(0));
                f_txtMontoCuota.setText(String.valueOf(0));
            }else{
                importT = importT - Qsubtotal;
                igvT = igvT - Qtributo;
                f_txtImporte.setText(String.valueOf(Math.round(importT/1.18 * 100.0) / 100.0));
                f_txtIGVtotal.setText(String.valueOf(Math.round((igvT) * 100.0) / 100.0));
                f_txtImporteTotal.setText(String.valueOf(Math.round((importT/1.18 + igvT) * 100.0) / 100.0));
                f_txtMontoCuota.setText(String.valueOf(Math.round((importT/1.18 + igvT) * 100.0) / 100.0));
            }
            m1 = (DefaultTableModel) f_tblDetalleRepuesto.getModel();
            m1.removeRow(Qfila);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccionar Fila");
        }
        Numero_a_Letra d= new Numero_a_Letra();
        double numero= 0.0;
        numero= Double.parseDouble(f_txtImporteTotal.getText());
        String letras= d.Convertir(String.valueOf(numero), true);
        f_lbl_letrasImporteTotal.setText(letras);
    }//GEN-LAST:event_f_btnQuitarActionPerformed

    private void f_cbxCuotasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_cbxCuotasActionPerformed
        // TODO add your handling code here:
        int nroCuotas = Integer.parseInt(String.valueOf(f_cbxCuotas.getSelectedItem()));
        Double montCuota = Double.parseDouble(f_txtImporteTotal.getText());
        montCuota = Math.round((montCuota / nroCuotas) * 100.0) / 100.0;
        f_txtMontoCuota.setText(montCuota.toString());
        double pendiente = Double.parseDouble(f_txtImporteTotal.getText()) - montCuota;
        f_txtPendientePago.setText(String.valueOf(pendiente));
    }//GEN-LAST:event_f_cbxCuotasActionPerformed

    private void f_txtRucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_f_txtRucKeyTyped
        //VALIDACIÓN DE CAMPO RUC
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >= 48 && key6 <= 57;
        if (!numeros) {
            evt.consume();
        }
        if (f_txtRuc.getText().length() >= 11) {
            evt.consume();
        }
    }//GEN-LAST:event_f_txtRucKeyTyped

    private void d_txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_d_txtCantidadKeyTyped
        //VALIDACIÓN DE CAMPO RUC
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >= 48 && key6 <= 57;
        if (!numeros) {
            evt.consume();
        }
    }//GEN-LAST:event_d_txtCantidadKeyTyped

    private void f_txtRucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_txtRucActionPerformed
        if (f_txtRuc.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "No escribió el " + f_cbxTipoDoc.getSelectedItem());
        } else if (f_cbxTipoDoc.getSelectedItem().equals("DNI")) {
            consultarDNI(f_txtRuc.getText());
        } else if (f_cbxTipoDoc.getSelectedItem().equals("RUC")) {
            consultarRUC(f_txtRuc.getText());
        }
    }//GEN-LAST:event_f_txtRucActionPerformed

    private void f_btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_btnRegistrarActionPerformed
        if (txtNumFactura.getText().equals("") || f_txtRuc.getText().equals("") || txtFecha.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
        } else {
            infoDventaId_productos = id_productos.toArray();
            infoDventaCantidadVenta = cantidadVenta.toArray();
            infoDventaPrecioVenta = precioVenta.toArray();
            infoDventaIgv = subTotaligv.toArray();
            infoDventaSubTotalVenta = subTotalVenta.toArray();
            
//===================================CONVERTIMOS A ARREGLOS PARA EL REST ==========================================
                        info_item_descripcion = item_descripcion.toArray();
                        info_item_total_igv = item_total_igv.toArray();
                        info_item_total_item = item_total_item.toArray();
                        info_item_total_valor_item = item_total_valor_item.toArray();
                        info_item_precio_unitario = item_precio_unitario.toArray();
                        info_item_porcentaje_igv = item_porcentaje_igv.toArray();
                        info_item_total_base_igv = item_total_base_igv.toArray();
                        info_item_cantidad = item_cantidad.toArray();
                        info_item_valor_unitario = item_valor_unitario.toArray();
                        info_item_total_impuestos = item_total_impuestos.toArray();
//==================================================================================================== 
            
            
            insertVentaCabecera();
            //DEJAR EN BLANCO TODO
            int Efila = f_tblDetalleRepuesto.getRowCount();
            for (int i = Efila - 1; i >= 0; i--) {
                
                
//===================================QUITAMOS LOS DETALLES PARA EL REST ==========================================
                        item_descripcion.remove(i);
                        item_total_igv.remove(i);
                        item_total_item.remove(i);
                        item_total_valor_item.remove(i);
                        item_precio_unitario.remove(i);
                        item_porcentaje_igv.remove(i);
                        item_total_base_igv.remove(i);
                        item_cantidad.remove(i);
                        item_valor_unitario.remove(i);
                        item_total_impuestos.remove(i);
//====================================================================================================                        
               
                id_productos.remove(i);
                cantidadVenta.remove(i);
                precioVenta.remove(i);
                subTotalVenta.remove(i);
                m1.removeRow(i);
            }
            reiniciarVariables();
            registrarNumFactura();
            MostrarProductos();
            limpiarCabecera();
            limpiarDetalle();
            limpiarImporte();
            habilitar(false);
            f_btnImprimir.setEnabled(true);
            d_txtCantidad.setEnabled(false);
            imprimirFactura();
        }
    }//GEN-LAST:event_f_btnRegistrarActionPerformed

    private void f_btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_btnImprimirActionPerformed
        imprimirFactura();
    }//GEN-LAST:event_f_btnImprimirActionPerformed

    private void f_btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_btnCancelarActionPerformed
        habilitar(false);
        limpiarDetalle();
    }//GEN-LAST:event_f_btnCancelarActionPerformed

    private void d_txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d_txtCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_d_txtCantidadActionPerformed

    private void d_txtCantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_d_txtCantidadKeyReleased
        calc();
    }//GEN-LAST:event_d_txtCantidadKeyReleased

    private void d_txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_d_txtBuscarKeyReleased
        BuscarRepuestos();
    }//GEN-LAST:event_d_txtBuscarKeyReleased

    private void f_txtPrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_f_txtPrecioKeyReleased
        calc();
    }//GEN-LAST:event_f_txtPrecioKeyReleased

    private void f_txtPrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_f_txtPrecioKeyTyped
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >= 48 && key6 <= 57 && key6 == 46;
        if (numeros) {
            evt.consume();
        }
    }//GEN-LAST:event_f_txtPrecioKeyTyped

    private void f_txtImporteTotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_f_txtImporteTotalKeyReleased
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_f_txtImporteTotalKeyReleased

    private void f_txtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_txtPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_f_txtPrecioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmFacturaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmFacturaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmFacturaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmFacturaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmFacturaVentas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton d_btnAgregar;
    private javax.swing.JButton d_btnCancelar;
    private javax.swing.JButton d_btnSeleccionar;
    private javax.swing.JTable d_tblRepuestos;
    private javax.swing.JTextField d_txtBuscar;
    private javax.swing.JTextField d_txtCantidad;
    private javax.swing.JDialog detalleVenta;
    private javax.swing.JButton f_btnAgregarNuevoCliente;
    private javax.swing.JButton f_btnAgregarProducto;
    private javax.swing.JButton f_btnBuscar;
    private javax.swing.JButton f_btnCancelar;
    private javax.swing.JButton f_btnImprimir;
    private javax.swing.JButton f_btnNuevaCabecera;
    private javax.swing.JButton f_btnQuitar;
    private javax.swing.JButton f_btnRegistrar;
    private javax.swing.JComboBox<String> f_cbxCuotas;
    private javax.swing.JComboBox<String> f_cbxFormaPago;
    private javax.swing.JComboBox<String> f_cbxTipoDoc;
    private javax.swing.JLabel f_lbl_letrasImporteTotal;
    private javax.swing.JTable f_tblDetalleRepuesto;
    private javax.swing.JTextField f_txtCliente;
    private javax.swing.JTextField f_txtCodigo;
    private javax.swing.JTextField f_txtDescripcion;
    private javax.swing.JTextField f_txtDireccion;
    private javax.swing.JTextField f_txtIGVtotal;
    private javax.swing.JTextField f_txtImporte;
    private javax.swing.JTextField f_txtImporteTotal;
    private javax.swing.JTextField f_txtImpuesto;
    private javax.swing.JComboBox<String> f_txtMoneda;
    private javax.swing.JTextField f_txtMontoCuota;
    private javax.swing.JTextField f_txtPendientePago;
    private javax.swing.JTextField f_txtPrecio;
    private javax.swing.JTextField f_txtRuc;
    private javax.swing.JTextField f_txtSubTotal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNumFactura;
    // End of variables declaration//GEN-END:variables
}
