package internalFrames;

import Clases.ConexionBD;
import frames.frmProveedores;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public final class ifrmCompra extends javax.swing.JInternalFrame {

//Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
    private int usuario_iden;
    private int proveedor_iden;
    private int producto_iden;
    private boolean swap = false;
    private Double subTotal=0.0;
    private Double importT=0.0;
    private Double importTmasIgv=0.0;
    private Double igvST=0.0;
    private Double igvT=0.0;
    
    private Object []infoDcompraId_productos;
    private Object []infoDcompraCantidadCompra;
    private Object []infoDcompraPrecioCostoCompra;
    private Object []infoDcompraSubTotalCompra;
    
    private ArrayList<String> id_productos = new ArrayList<String>();
    private ArrayList<String> cantidadCompra = new ArrayList<String>();
    private ArrayList<String> precioCostoCompra = new ArrayList<String>();
    private ArrayList<String> subTotalCompra = new ArrayList<String>();
    
    private DefaultTableModel m, m1;
    private int item=0;

    public ifrmCompra() {
        initComponents();
        MostrarProductos();
        Calendar c2 = new GregorianCalendar();
        c_dcrFechaCompra.setCalendar(c2);
    }
    
    private void reiniciarVariables(){
        item=0;
        subTotal=0.0;
        importT=0.0;
        importTmasIgv=0.0;
        igvST=0.0;
        igvT=0.0;
    }

    //MÉTODO PARA OBTENER DATOS DEL USUARIO
    public void getInfoUser(String pass){
        String[] records = new String[1];
        //Modelo para establecer en la tabla
        try{
            String login="SELECT usuario_iden FROM tbl_usuario WHERE user_pass = '"+pass+"'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("usuario_iden");
            } 
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        this.usuario_iden = Integer.parseInt(records[0]);
    }
    
    //MÉTODO PARA OBTENER DATOS DEL PROVEEDOR
    public void getInfoProveedor(String ruc){
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        try{
            String login="SELECT proveedor_iden FROM tbl_proveedor WHERE prov_ruc = '"+ruc+"'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("proveedor_iden");
            } 
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        this.proveedor_iden = Integer.parseInt(records[0]);
    }
    
    //MÉTODO PARA OBTENER DATOS DEL PRODUCTO
    public void getInfoProducto(String codigo){
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        try{
            String login="SELECT producto_iden FROM tbl_producto WHERE prod_cod = '"+codigo+"'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("producto_iden");
            } 
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        this.producto_iden = Integer.parseInt(records[0]);
    }
    
    
    //MÉTODO PARA HABLILITAR CAMPOS
    private void habilitar(boolean t){
        //CABECERA
        c_txtRucProv.setEnabled(t);
        c_txtNomProveedor.setEnabled(t);
        c_txtDireccion.setEnabled(t);
        c_txtNumFactura.setEnabled(t);
        c_dcrFechaCompra.setEnabled(t);
        c_btnBuscarProv.setEnabled(t);
        c_btnNuevoRegistro.setEnabled(!t);
        c_btnCancelarRegistro.setEnabled(t);
        c_btnAgregarProveedor.setEnabled(t);
        
        //DETALLE
        c_txtCodigo.setEnabled(t);
        c_txtDescripcion.setEnabled(t);
        c_txtCantidad.setEnabled(t);
        c_txtRucProv.setEnabled(t);
        c_txtPrecioCosto.setEnabled(t);
        c_txtImpuesto.setEnabled(t);
        c_txtSubTotal.setEnabled(t);
        
        c_btnagregarProducto.setEnabled(t);
        c_btnAgregarRegistro.setEnabled(t);
        c_btnQuitarRegistro.setEnabled(t);
        
        c_btnRegistrarCompra.setEnabled(t);
        
    }
    
    private void limpiar() {
        //Campos de info del prov
        c_txtNumFactura.setText("");
        c_txtRucProv.setText("");
        c_txtId.setText(""); 
        c_txtNomProveedor.setText("");
        c_txtDireccion.setText("");
        limpiarDetalle();
        c_txtImporte.setText("0.0");
        c_txtIgv.setText("0.0");
        c_txtImporteTotal.setText("0.0");
    }
    
    private void limpiarDetalle(){
        c_txtCodigo.setText("");
        c_txtDescripcion.setText("");
        c_txtCantidad.setText("0");
        c_txtPrecioCosto.setText("0.0");
        c_txtImpuesto.setText("0.0");
        c_txtSubTotal.setText("0.0");
    }
    
    public static String fechaActual(){
//        Date fecha = new Date();//instancias la clase
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");//formato de fecha
        return formatoFecha.format(LocalDateTime.now()); 
    }
    
    private void insertCompraCabecera(){
        
        String date = fechaActual();
        try {
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into tbl_compra_cabecera (usuario_iden, proveedor_iden, cc_codf, cc_impt, cc_fechac, cc_fechar) value (?,?,?,?,?,?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
        
            pst.setInt(1, usuario_iden);
            pst.setInt(2, proveedor_iden);
            pst.setString(3, c_txtNumFactura.getText());
            pst.setDouble(4, Double.parseDouble(c_txtImporteTotal.getText()));
            pst.setString(5, String.valueOf(c_dcrFechaCompra.getDate()));
            pst.setString(6, date);            
            pst.execute();//ejecutar las instrucciones 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: " + e.getMessage());
        }
        
        //SELECCIONAR ID DE CABECERA DE COMPRA
        String[] selcDetalle = new String[1];
        //Modelo para establecer en la tabla
        try{
            String login="SELECT compra_cabecera_iden FROM tbl_compra_cabecera WHERE cc_fechar = '"+date+"'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                selcDetalle[0]=rs.getString("compra_cabecera_iden");
            } 
            JOptionPane.showMessageDialog(null, "Registro exitoso de la compra"); 
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        insertCompraDetalle(Integer.parseInt(selcDetalle[0]));
    }
    
    //MÉTODO PARA INSERTAR LAS COMPRAS
    private void insertCompraDetalle(int compra_cabecera_iden) {
        for(int i=0; i<infoDcompraId_productos.length; i++){
            try {
                //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
                String comandSQL = "Insert Into  tbl_compra_detalle (compra_cabecera_iden, producto_iden, cd_cant, cd_cunit, cd_subt, cd_fechar) value (?,?,?,?,?,?)";
                PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn


                    pst.setInt(1, compra_cabecera_iden);
                    pst.setInt(2, Integer.parseInt(String.valueOf(infoDcompraId_productos[i])));
                    pst.setInt(3, Integer.parseInt(String.valueOf(infoDcompraCantidadCompra[i])));
                    pst.setDouble(4, Double.parseDouble(String.valueOf(infoDcompraPrecioCostoCompra[i])));
                    pst.setDouble(5, Double.parseDouble(String.valueOf(infoDcompraSubTotalCompra[i])));
                    pst.setString(6, fechaActual());

                pst.execute();//ejecutar las instrucciones          
            } catch (HeadlessException | NumberFormatException | SQLException e) {
                JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: " + e.getMessage());
            }
            
            //CONSULTANDO EL ID EN LA TABLA STOCK
            String[] records = new String[2];
            //Modelo para establecer en la tabla
            try{
                String login="SELECT producto_iden, sock_cant FROM tbl_stock WHERE producto_iden = '"+Integer.parseInt(String.valueOf(infoDcompraId_productos[i]))+"'"; 
                //crea cuando la devuelve resultados o registros
                Statement st = c.createStatement(); 
                ResultSet rs = st.executeQuery(login);

                while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                    records[0]=rs.getString("producto_iden");
                    records[1]=rs.getString("sock_cant");
                } 
            }catch (NumberFormatException | SQLException e){
                JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
            }
            
            //ACTUALIZANDO EL STOVK DELPRODUCTO CORRESPONDIENTE
                int stock = Integer.parseInt(records[1])+Integer.parseInt(String.valueOf(infoDcompraCantidadCompra[i]));
                try{
                    //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
                    String comandSQL = "UPDATE tbl_stock SET sock_cant = '"+stock+"' WHERE producto_iden = '"+records[0]+"'";
                    PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

                    pst.execute();//ejecutar las instrucciones          
                       
                }catch (HeadlessException | SQLException e){
                    JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha actualizado: "+e.getMessage());
                }       
        }
    }

    //CONSULTAR A TRAVÉS DE RUC
    private void consultarRUC(String ruc){
        try {
            String sql = "SELECT * FROM tbl_proveedor WHERE prov_ruc= '"+ruc+"'";
            PreparedStatement st =c.prepareStatement(sql); //crea cuando la devuelve resultados o registros
            try (ResultSet rs = st.executeQuery()) {
                while(rs.next()){
                    c_txtId.setText(rs.getString("proveedor_iden"));
                    c_txtNomProveedor.setText(rs.getString("prov_nom"));
                    c_txtDireccion.setText(rs.getString("prov_dire"));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }
    
    //MÉTODO PARA MOSTRAR INFORMACIÓN DE LOS REPUESTOS EN TABLA
    public void MostrarProductos() {
        String[] head = {"CÓDIGO", "DESCRIPCIÓN", "STOCK"};
        String[] records = new String[3];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL = "SELECT * FROM tbl_producto INNER JOIN tbl_stock ON tbl_producto.producto_iden = tbl_stock.producto_iden "; //Realiza bùsqueda en la tabla
        try {
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("prod_cod");
                records[1] = rs.getString("prod_desc")+ rs.getString("prod_caract");
                records[2] = rs.getString("sock_cant");

                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblSelectProductos.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de bùsqueda " + e.getMessage());
        }
        //REDIMENSIONAR LAS COLUMNAS
        tblSelectProductos.getColumnModel().getColumn(0).setPreferredWidth(10);
        tblSelectProductos.getColumnModel().getColumn(0).setResizable(true);
        tblSelectProductos.getColumnModel().getColumn(1).setPreferredWidth(300);
        tblSelectProductos.getColumnModel().getColumn(1).setResizable(true);
        tblSelectProductos.getColumnModel().getColumn(2).setPreferredWidth(5);
        tblSelectProductos.getColumnModel().getColumn(2).setResizable(true);
    }
    
    public void BuscarRepuestos(){
        String Buscar = s_txtBusca.getText();
        String[] head = {"CÓDIGO", "DESCRIPCIÓN", "STOCK"};
        String[] records = new String[3];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL = "SELECT * FROM tbl_producto INNER JOIN tbl_stock ON tbl_producto.producto_iden = tbl_stock.producto_iden  WHERE prod_cod LIKE '%"+Buscar+"%' OR prod_desc LIKE '%"+Buscar+"%' OR prod_caract LIKE '%"+Buscar+"%'"; //Realiza bùsqueda en la tabla
        try {
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("prod_cod");
                records[1] = rs.getString("prod_desc")+ rs.getString("prod_caract");
                records[2] = rs.getString("sock_cant");

                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblSelectProductos.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de bùsqueda " + e.getMessage());
        }
    }
    
    public void calc(){
        Double monto = Integer.parseInt(String.valueOf(c_txtCantidad.getText())) * Double.parseDouble(String.valueOf(c_txtPrecioCosto.getText()));
        c_txtSubTotal.setText(String.valueOf(monto));
        Double igv =Double.parseDouble(c_txtSubTotal.getText())*0.0;
        c_txtImpuesto.setText(String.valueOf(igv));
    }
    
    private void calcImportTotal(){
        subTotal = Double.parseDouble(c_txtSubTotal.getText());
        igvST = Double.parseDouble(c_txtImpuesto.getText());
        importT = importT + subTotal;
        igvT = igvT + igvST;
        c_txtImporte.setText(String.valueOf(importT));
        importTmasIgv = importT+igvT;
        c_txtImporteTotal.setText(String.valueOf(importTmasIgv));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ifrmSeleccionarProducto = new javax.swing.JDialog();
        jLabel19 = new javax.swing.JLabel();
        s_txtBusca = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSelectProductos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        c_txtNumFactura = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        c_dcrFechaCompra = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        c_txtId = new javax.swing.JTextField();
        c_btnBuscarProv = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        c_txtRucProv = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        c_txtNomProveedor = new javax.swing.JTextField();
        c_btnAgregarProveedor = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        c_txtDireccion = new javax.swing.JTextField();
        c_btnNuevoRegistro = new javax.swing.JButton();
        c_btnCancelarRegistro = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        c_txtCodigo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        c_txtCantidad = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        c_txtPrecioCosto = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        c_txtImpuesto = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        c_txtSubTotal = new javax.swing.JTextField();
        c_txtDescripcion = new javax.swing.JTextField();
        c_btnagregarProducto = new javax.swing.JButton();
        c_btnAgregarRegistro = new javax.swing.JButton();
        c_btnQuitarRegistro = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCompras = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        c_txtImporte = new javax.swing.JTextField();
        c_txtIgv = new javax.swing.JTextField();
        c_txtImporteTotal = new javax.swing.JTextField();
        c_btnRegistrarCompra = new javax.swing.JButton();

        ifrmSeleccionarProducto.setTitle("BUSCAR PRODUCTOS");

        jLabel19.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel19.setText("Buscar:");

        s_txtBusca.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        s_txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                s_txtBuscaKeyReleased(evt);
            }
        });

        tblSelectProductos.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        tblSelectProductos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblSelectProductos);

        jButton1.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jButton1.setText("SELECCIONAR PRODUCTO");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jButton2.setText("CANCELAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ifrmSeleccionarProductoLayout = new javax.swing.GroupLayout(ifrmSeleccionarProducto.getContentPane());
        ifrmSeleccionarProducto.getContentPane().setLayout(ifrmSeleccionarProductoLayout);
        ifrmSeleccionarProductoLayout.setHorizontalGroup(
            ifrmSeleccionarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ifrmSeleccionarProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ifrmSeleccionarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                    .addGroup(ifrmSeleccionarProductoLayout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(s_txtBusca))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ifrmSeleccionarProductoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(38, 38, 38)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        ifrmSeleccionarProductoLayout.setVerticalGroup(
            ifrmSeleccionarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ifrmSeleccionarProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ifrmSeleccionarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(s_txtBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(ifrmSeleccionarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        setClosable(true);
        setMaximizable(true);
        setTitle("GESTIÓN DE COMPRAS DE REPUESTOS");
        setPreferredSize(new java.awt.Dimension(1195, 700));

        jLabel1.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel1.setText("FACTURA N°:");

        c_txtNumFactura.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        c_txtNumFactura.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel2.setText("Fecha de compra:");

        c_dcrFechaCompra.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Cabecera:");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel4.setText("Id:");

        c_txtId.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        c_txtId.setEnabled(false);

        c_btnBuscarProv.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        c_btnBuscarProv.setText("+");
        c_btnBuscarProv.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        c_btnBuscarProv.setEnabled(false);
        c_btnBuscarProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_btnBuscarProvActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel5.setText("RUC:");

        c_txtRucProv.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        c_txtRucProv.setEnabled(false);
        c_txtRucProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c_txtRucProvKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel6.setText("Proveedor:");

        c_txtNomProveedor.setEditable(false);
        c_txtNomProveedor.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        c_btnAgregarProveedor.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        c_btnAgregarProveedor.setText("AGREGAR PROVEEDOR");
        c_btnAgregarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        c_btnAgregarProveedor.setEnabled(false);
        c_btnAgregarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_btnAgregarProveedorActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel7.setText("Dirección:");

        c_txtDireccion.setEditable(false);
        c_txtDireccion.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        c_btnNuevoRegistro.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        c_btnNuevoRegistro.setText("NUEVO REGISTRO");
        c_btnNuevoRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        c_btnNuevoRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_btnNuevoRegistroActionPerformed(evt);
            }
        });

        c_btnCancelarRegistro.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        c_btnCancelarRegistro.setText("CANCELAR");
        c_btnCancelarRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        c_btnCancelarRegistro.setEnabled(false);
        c_btnCancelarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_btnCancelarRegistroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(c_btnNuevoRegistro)
                        .addGap(18, 18, 18)
                        .addComponent(c_btnCancelarRegistro))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c_txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(84, 84, 84)
                                .addComponent(c_btnBuscarProv))
                            .addComponent(c_btnAgregarProveedor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(c_txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c_txtRucProv, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(c_txtNomProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(c_btnNuevoRegistro)
                        .addComponent(c_btnCancelarRegistro)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(c_txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_btnBuscarProv)
                    .addComponent(jLabel5)
                    .addComponent(c_txtRucProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(c_txtNomProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(c_btnAgregarProveedor)
                    .addComponent(jLabel7)
                    .addComponent(c_txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("Detalle de Compra:");

        jLabel9.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel9.setText("Código:");

        c_txtCodigo.setEditable(false);
        c_txtCodigo.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel10.setText("Cantidad:");

        c_txtCantidad.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        c_txtCantidad.setEnabled(false);
        c_txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                c_txtCantidadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c_txtCantidadKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel11.setText("Descripción:");

        jLabel12.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel12.setText("Precio de compra:");

        c_txtPrecioCosto.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        c_txtPrecioCosto.setEnabled(false);
        c_txtPrecioCosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                c_txtPrecioCostoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c_txtPrecioCostoKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel13.setText("Impuesto:");

        c_txtImpuesto.setEditable(false);
        c_txtImpuesto.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel14.setText("SubTotal:");

        c_txtSubTotal.setEditable(false);
        c_txtSubTotal.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        c_txtDescripcion.setEditable(false);
        c_txtDescripcion.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        c_btnagregarProducto.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        c_btnagregarProducto.setText("+");
        c_btnagregarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        c_btnagregarProducto.setEnabled(false);
        c_btnagregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_btnagregarProductoActionPerformed(evt);
            }
        });

        c_btnAgregarRegistro.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        c_btnAgregarRegistro.setText("AGREGAR");
        c_btnAgregarRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        c_btnAgregarRegistro.setEnabled(false);
        c_btnAgregarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_btnAgregarRegistroActionPerformed(evt);
            }
        });

        c_btnQuitarRegistro.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        c_btnQuitarRegistro.setText("QUITAR");
        c_btnQuitarRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        c_btnQuitarRegistro.setEnabled(false);
        c_btnQuitarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_btnQuitarRegistroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(c_txtCodigo)
                            .addComponent(c_txtCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(c_txtPrecioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c_txtImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c_txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(c_txtDescripcion))
                        .addGap(18, 18, 18)
                        .addComponent(c_btnagregarProducto)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(c_btnQuitarRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(c_btnAgregarRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(c_txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(c_txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_btnagregarProducto)
                    .addComponent(c_btnAgregarRegistro))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(c_txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(c_txtPrecioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(c_txtImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(c_txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_btnQuitarRegistro))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        tblCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Código", "Descripción", "Cantidad", "P. Compra", "Impuesto", "SubTotal"
            }
        ));
        tblCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblComprasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCompras);

        jLabel15.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel15.setText("Importe:");

        jLabel16.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel16.setText("IGV:");

        jLabel17.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel17.setText("Importe Total:");

        c_txtImporte.setEditable(false);
        c_txtImporte.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        c_txtIgv.setEditable(false);
        c_txtIgv.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        c_txtImporteTotal.setEditable(false);
        c_txtImporteTotal.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        c_btnRegistrarCompra.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        c_btnRegistrarCompra.setText("REGISTRAR");
        c_btnRegistrarCompra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        c_btnRegistrarCompra.setEnabled(false);
        c_btnRegistrarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_btnRegistrarCompraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(c_txtNumFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(c_dcrFechaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel15)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(c_btnRegistrarCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(c_txtImporte, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                            .addComponent(c_txtIgv)
                            .addComponent(c_txtImporteTotal))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(c_txtNumFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(c_dcrFechaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(c_txtImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(c_txtIgv, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(c_txtImporteTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_btnRegistrarCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void c_btnBuscarProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_btnBuscarProvActionPerformed
        if(c_txtRucProv.getText().equals("")){
            JOptionPane.showMessageDialog(null, "No escribió el RUC");
        }else {
            consultarRUC(c_txtRucProv.getText());
            getInfoProveedor(c_txtRucProv.getText());
        }
    }//GEN-LAST:event_c_btnBuscarProvActionPerformed

    private void c_txtRucProvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_txtRucProvKeyTyped
        //VALIDACIÓN DE CAMPO RUC
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >=48 && key6 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(c_txtRucProv.getText().length()>=11){
            evt.consume();
        }
    }//GEN-LAST:event_c_txtRucProvKeyTyped

    private void c_btnNuevoRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_btnNuevoRegistroActionPerformed
        limpiar();
        habilitar(true);
    }//GEN-LAST:event_c_btnNuevoRegistroActionPerformed

    private void c_btnCancelarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_btnCancelarRegistroActionPerformed
        limpiar();
        habilitar(false);
    }//GEN-LAST:event_c_btnCancelarRegistroActionPerformed

    private void c_btnAgregarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_btnAgregarProveedorActionPerformed
        frmProveedores frmnp = new frmProveedores();
        frmnp.setVisible(true);
    }//GEN-LAST:event_c_btnAgregarProveedorActionPerformed

    private void c_btnagregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_btnagregarProductoActionPerformed
        ifrmSeleccionarProducto.setSize(584, 654);
        ifrmSeleccionarProducto.setLocationRelativeTo(null);
        ifrmSeleccionarProducto.setModal(true);
        ifrmSeleccionarProducto.setVisible(true);
    }//GEN-LAST:event_c_btnagregarProductoActionPerformed

    private void c_btnAgregarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_btnAgregarRegistroActionPerformed
        if(c_txtCodigo.getText().equals("") || c_txtDescripcion.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Hay campos vacíos");
            }else{
                getInfoProducto(c_txtCodigo.getText());
                    
                id_productos.add(String.valueOf(producto_iden));
                cantidadCompra.add(c_txtCantidad.getText());
                precioCostoCompra.add(c_txtPrecioCosto.getText());
                subTotalCompra.add(c_txtSubTotal.getText());

                item = item + 1;

                String []infoTabla = new String[7];

                infoTabla[0]=String.valueOf(item);
                infoTabla[1]=c_txtCodigo.getText();
                infoTabla[2]=c_txtDescripcion.getText();
                infoTabla[3]=c_txtCantidad.getText();
                infoTabla[4]=c_txtPrecioCosto.getText();
                infoTabla[5]=c_txtImpuesto.getText();
                infoTabla[6]=c_txtSubTotal.getText();

                m1 = (DefaultTableModel) tblCompras.getModel();
                m1.addRow(infoTabla);

                calcImportTotal();
                limpiarDetalle();
            }
    }//GEN-LAST:event_c_btnAgregarRegistroActionPerformed

    private void c_btnQuitarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_btnQuitarRegistroActionPerformed
        int Qfila = tblCompras.getSelectedRow();
        if(Qfila >=0){
            
            Double Qsubtotal, Qtributo;
            
            id_productos.remove(Qfila);
            cantidadCompra.remove(Qfila);
            precioCostoCompra.remove(Qfila);
            subTotalCompra.remove(Qfila);
            
            Qtributo = Double.parseDouble(tblCompras.getValueAt(Qfila, 5).toString());
            Qsubtotal = Double.parseDouble(tblCompras.getValueAt(Qfila, 6).toString());
            
            importT = importT - Qsubtotal;
            igvT = igvT - Qtributo;
            c_txtImporte.setText(String.valueOf(importT));
            c_txtIgv.setText(String.valueOf(igvT));
            c_txtImporteTotal.setText(String.valueOf(importT+igvT));
     
            m1 = (DefaultTableModel) tblCompras.getModel();    
            m1.removeRow(Qfila );
        }else{
            JOptionPane.showMessageDialog(null, "Seleccionar Fila");
        }
    }//GEN-LAST:event_c_btnQuitarRegistroActionPerformed

    private void tblComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblComprasMouseClicked
        habilitar(true);
        c_btnQuitarRegistro.setEnabled(true);
        swap = false;
    }//GEN-LAST:event_tblComprasMouseClicked

    private void c_btnRegistrarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_btnRegistrarCompraActionPerformed
        if(c_txtNumFactura.getText().equals("") || c_txtRucProv.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
        }else{
            infoDcompraId_productos=id_productos.toArray();
            infoDcompraCantidadCompra=cantidadCompra.toArray();
            infoDcompraPrecioCostoCompra=precioCostoCompra.toArray();
            infoDcompraSubTotalCompra=subTotalCompra.toArray();
            insertCompraCabecera();
            //DEJAR EN BLANCO TODO
            int Efila = tblCompras.getRowCount();
            for(int i=Efila-1; i>=0;i--){
                id_productos.remove(i);
                cantidadCompra.remove(i);
                precioCostoCompra.remove(i);
                subTotalCompra.remove(i);
                m1.removeRow(i);
            }
            reiniciarVariables();
            MostrarProductos();
            limpiar();
            habilitar(false);
        }
    }//GEN-LAST:event_c_btnRegistrarCompraActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int fsel = tblSelectProductos.getSelectedRow();
        try{
            String codigo, descripcion;
            
            if(fsel == -1){
                JOptionPane.showMessageDialog(null, "Debe seleccionar un producto");
            }else{
                m = (DefaultTableModel) tblSelectProductos.getModel();
                codigo = tblSelectProductos.getValueAt(fsel, 0).toString();
                descripcion = tblSelectProductos.getValueAt(fsel, 1).toString();
                
                c_txtCodigo.setText(codigo);
                c_txtDescripcion.setText(descripcion);
            }
            
        }catch(HeadlessException e){
            
        }
        ifrmSeleccionarProducto.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ifrmSeleccionarProducto.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void c_txtPrecioCostoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_txtPrecioCostoKeyReleased
        calc();
    }//GEN-LAST:event_c_txtPrecioCostoKeyReleased

    private void c_txtCantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_txtCantidadKeyReleased
        calc();
    }//GEN-LAST:event_c_txtCantidadKeyReleased

    private void s_txtBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_s_txtBuscaKeyReleased
        BuscarRepuestos();
    }//GEN-LAST:event_s_txtBuscaKeyReleased

    private void c_txtPrecioCostoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_txtPrecioCostoKeyTyped
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >=48 && key6 <= 57 || key6==46;
        if(!numeros){
            evt.consume();
        }
    }//GEN-LAST:event_c_txtPrecioCostoKeyTyped

    private void c_txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_txtCantidadKeyTyped
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >=48 && key6 <= 57;
        if(!numeros){
            evt.consume();
        }
    }//GEN-LAST:event_c_txtCantidadKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton c_btnAgregarProveedor;
    private javax.swing.JButton c_btnAgregarRegistro;
    private javax.swing.JButton c_btnBuscarProv;
    private javax.swing.JButton c_btnCancelarRegistro;
    private javax.swing.JButton c_btnNuevoRegistro;
    private javax.swing.JButton c_btnQuitarRegistro;
    private javax.swing.JButton c_btnRegistrarCompra;
    private javax.swing.JButton c_btnagregarProducto;
    private com.toedter.calendar.JDateChooser c_dcrFechaCompra;
    private javax.swing.JTextField c_txtCantidad;
    private javax.swing.JTextField c_txtCodigo;
    private javax.swing.JTextField c_txtDescripcion;
    private javax.swing.JTextField c_txtDireccion;
    private javax.swing.JTextField c_txtId;
    private javax.swing.JTextField c_txtIgv;
    private javax.swing.JTextField c_txtImporte;
    private javax.swing.JTextField c_txtImporteTotal;
    private javax.swing.JTextField c_txtImpuesto;
    private javax.swing.JTextField c_txtNomProveedor;
    private javax.swing.JTextField c_txtNumFactura;
    private javax.swing.JTextField c_txtPrecioCosto;
    private javax.swing.JTextField c_txtRucProv;
    private javax.swing.JTextField c_txtSubTotal;
    private javax.swing.JDialog ifrmSeleccionarProducto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField s_txtBusca;
    private javax.swing.JTable tblCompras;
    private javax.swing.JTable tblSelectProductos;
    // End of variables declaration//GEN-END:variables
}
