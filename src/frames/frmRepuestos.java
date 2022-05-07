package frames;

import Clases.ConexionBD;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmRepuestos extends javax.swing.JFrame {

    //Declaraciòn de objeto conexion
    ConexionBD con = new ConexionBD();
    Connection c = con.getConexionBD();

    private boolean swap = false;
    private int modelo_iden;
    private int producto_iden;
    
    public frmRepuestos() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        MostrarRepuestos();
        mostrarMarcas();
    }
    
    //MÉTODO PARA OBTENER DATOS DE LOS MODELOS
    public void getInfoModel(String modelo, String marca){
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        try{
            String login="SELECT modelo_iden FROM tbl_marca INNER JOIN tbl_modelo ON tbl_marca.marca_iden = tbl_modelo.marca_iden WHERE mod_desc = '"+modelo+"' AND marc_desc = '"+marca+"'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("modelo_iden");
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        this.modelo_iden = Integer.parseInt(records[0]);
    }
    
    //MÉTODO PARA OBTENER DATOS DEL PRODUCTO LUEGO DEINSERTAR
    public void getInfoProduct(String cod){
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        try{
            String login="SELECT producto_iden FROM tbl_producto where prod_cod = '"+cod+"'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("tbl_producto.producto_iden");
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        this.producto_iden = Integer.parseInt(records[0]);
    }

    //MÉTODO PARA HABLITAR CAMPOS
    private void habilitar(boolean t) {

        //Campos de info del rep
        r_txtCod.setEnabled(t);
        r_txtRepuesto.setEnabled(t);
        r_cbxMarca.setEnabled(t);
        r_cbxTipoMarca.setEnabled(t);
        r_cbxTam.setEnabled(t);
        r_cbxMat.setEnabled(t);

        //Botones de CRUD
        r_btnGrabar.setEnabled(t);
        r_btnAgregar.setEnabled(!t);
        r_btnCancelar.setEnabled(t);
        r_btnEliminar.setEnabled(t);
    }

    private void limpiar() {
        r_txtId.setText("");
        r_txtCod.setText("");
        r_txtRepuesto.setText("");
        r_cbxMarca.setSelectedIndex(0);
        r_cbxTam.setSelectedIndex(0);
        r_cbxMat.setSelectedIndex(0);
    }

    //MÉTODO PARA MOSTRAR INFORMACIÓN DE LOS REPUESTOS EN TABLA
    public void MostrarRepuestos() {
        String[] head = {"ID", "CÓDIGO", "REPUESTO", "STOCK", "MODELO", "MARCA"};
        String[] records = new String[6];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL = "SELECT * FROM tbl_producto INNER JOIN tbl_modelo ON tbl_producto.modelo_iden = tbl_modelo.modelo_iden INNER JOIN tbl_marca ON tbl_modelo.marca_iden = tbl_marca.marca_iden INNER JOIN tbl_stock ON tbl_producto.producto_iden = tbl_stock.producto_iden"; //Realiza bùsqueda en la tabla
        try {
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("producto_iden");
                records[1] = rs.getString("prod_cod");
                if(rs.getString("prod_caract").equals("") || rs.getString("prod_caract")==null){
                    records[2] = rs.getString("prod_desc");
                }else{
                    records[2] = rs.getString("prod_desc")+", "+rs.getString("prod_caract");
                }
                records[3] = rs.getString("sock_cant");
                records[4] = rs.getString("mod_desc");
                records[5] = rs.getString("marc_desc");
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblRepuestos.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de bùsqueda " + e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblRepuestos.getColumnModel().getColumn(0).setPreferredWidth(5);
        tblRepuestos.getColumnModel().getColumn(0).setResizable(true);
        tblRepuestos.getColumnModel().getColumn(1).setPreferredWidth(15);
        tblRepuestos.getColumnModel().getColumn(1).setResizable(true);
        tblRepuestos.getColumnModel().getColumn(2).setPreferredWidth(300);
        tblRepuestos.getColumnModel().getColumn(2).setResizable(true);
        tblRepuestos.getColumnModel().getColumn(3).setPreferredWidth(5);
        tblRepuestos.getColumnModel().getColumn(3).setResizable(true); 
        tblRepuestos.getColumnModel().getColumn(4).setPreferredWidth(15);
        tblRepuestos.getColumnModel().getColumn(4).setResizable(true);
        tblRepuestos.getColumnModel().getColumn(5).setPreferredWidth(10);
        tblRepuestos.getColumnModel().getColumn(5).setResizable(true); 
    }

    private void mostrarMarcas() {
        try {
            String sql = "SELECT * FROM tbl_marca";
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(sql);
            r_cbxMarca.addItem("Seleccione la marca");
            while (rs.next()) {
                r_cbxMarca.addItem(rs.getString("marc_desc"));
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private void mostrarTiposMarca(String marca) {
        try {
            String sql = "SELECT * FROM tbl_marca INNER JOIN tbl_modelo ON tbl_marca.marca_iden = tbl_modelo.marca_iden WHERE marc_desc = '" + marca + "'";
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(sql);
            //Limpio el combobox antes de llenarlos de nueva información
            r_cbxTipoMarca.removeAllItems();
            while (rs.next()) {
                r_cbxTipoMarca.addItem(rs.getString("mod_desc"));
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    //MÉTODO PARA INSERTAR MODELOS
    private void insertRepuestos() {
        try {
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into tbl_producto (prod_cod, modelo_iden, prod_desc, prod_caract) value (?,?,?,?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, r_txtCod.getText());
            String marca = String.valueOf(r_cbxMarca.getSelectedItem()).toUpperCase();
            String tipoMarca = String.valueOf(r_cbxTipoMarca.getSelectedItem()).toUpperCase();
            getInfoModel(tipoMarca,marca);
            pst.setInt(2, modelo_iden);
            pst.setString(3, r_txtRepuesto.getText().toUpperCase());

            int itemTam = r_cbxTam.getSelectedIndex(); //identificar el elemento seleccionado
            int itemTipoMat = r_cbxMat.getSelectedIndex(); //identificar el elemento seleccionado
            String tam = r_cbxTam.getItemAt(itemTam);
            String mat = r_cbxMat.getItemAt(itemTipoMat);
            
            if(tam.equals("Seleccione tamaño") && mat.equals("Seleccione material")){
                pst.setString(4, "");
            }else if(mat.equals("Seleccione material")){
                pst.setString(4, (tam).toUpperCase());
            }else if(tam.equals("Seleccione tamaño")){
                pst.setString(4, (mat).toUpperCase());
            }else{
                pst.setString(4, (tam+" "+mat).toUpperCase());
            }

            pst.execute();//ejecutar las instrucciones          
            JOptionPane.showMessageDialog(null, "Registro incertado con éxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: " + e.getMessage());
        }
        getInfoProduct(r_txtCod.getText());
        stock();
    }
    private void stock(){
        try {
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into tbl_stock (producto_iden, sock_cant) value (?,?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setInt(1, producto_iden);
            pst.setInt(2, 0);

            pst.execute();//ejecutar las instrucciones          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro del stock: " + e.getMessage());
        }
    }

    //MÉTODO PARA ACTUALIZAR MARCA
    private void updateRepuestos() {
        try {
            //Prepara sentencia de sql; instrucciòn para actualizar en la bbdd
            String comandSQL = "update tbl_producto set prod_cod=?,  modelo_iden=?, prod_desc=?, prod_caract=? where producto_iden=?";

            int SelectionFile = tblRepuestos.getSelectedRow();
            String idal = (String) tblRepuestos.getValueAt(SelectionFile, 0);//casting

            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, r_txtCod.getText());
            String marca = String.valueOf(r_cbxMarca.getSelectedItem()).toUpperCase();
            String tipoMarca = String.valueOf(r_cbxTipoMarca.getSelectedItem()).toUpperCase();
            getInfoModel(tipoMarca,marca);
            pst.setInt(2, modelo_iden);
            pst.setString(3, r_txtRepuesto.getText().toUpperCase());

            int itemTam = r_cbxTam.getSelectedIndex(); //identificar el elemento seleccionado
            int itemTipoMat = r_cbxMat.getSelectedIndex(); //identificar el elemento seleccionado
            String tam = r_cbxTam.getItemAt(itemTam);
            String mat = r_cbxMat.getItemAt(itemTipoMat);
            
            if(tam.equals("Seleccione tamaño") && mat.equals("Seleccione material")){
                pst.setString(4, "");
            }else if(mat.equals("Seleccione material")){
                pst.setString(4, (tam).toUpperCase());
            }else if(tam.equals("Seleccione tamaño")){
                pst.setString(4, (mat).toUpperCase());
            }else{
                pst.setString(4, (tam+" "+mat).toUpperCase());
            }

            pst.setString(5, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Registro actualizado correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado la actualización: " + e.getMessage());
        }
    }

    //MÉTODO IMPRIMIR DESDE LA TABLA A LOS CAMPOS
    private void printTable() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            int Fila = tblRepuestos.getSelectedRow();
            String codigo = tblRepuestos.getValueAt(Fila, 0).toString();

            ps = c.prepareStatement("SELECT * FROM tbl_producto INNER JOIN tbl_stock ON tbl_producto.producto_iden = tbl_stock.producto_iden INNER JOIN tbl_marca INNER JOIN tbl_modelo ON tbl_marca.marca_iden = tbl_modelo.marca_iden AND tbl_producto.modelo_iden = tbl_modelo.modelo_iden WHERE tbl_producto.producto_iden=?");
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            while (rs.next()) {
                r_txtId.setText(rs.getString("producto_iden"));
                r_txtCod.setText(rs.getString("prod_cod"));
                r_txtRepuesto.setText(rs.getString("prod_desc"));
                r_txtStock.setValue(rs.getInt("sock_cant"));
                r_cbxMarca.setSelectedItem(rs.getString("marc_desc"));
                r_cbxTipoMarca.setSelectedItem(rs.getString("mod_desc"));
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    //MÉTODO PARA ELIMINAR REPUESTOS
    public void DeleteRepuestos() {
        try {
            //Campos a actualizar
            String comandSQL = "delete from tbl_producto where producto_iden=?";
            int SelectionFile = tblRepuestos.getSelectedRow();
            String idal = (String) tblRepuestos.getValueAt(SelectionFile, 0);//casting
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
            pst.setString(1, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Se ha eliminado el registro con èxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha eliminado el registro: " + e.getMessage());
        }
    }
    
    //MÉTODO PARA BUSCAR REGISTROS DELOS PRODUCTOS
    public void BuscarRepuestos(){
        String Buscar = r_txtBuscar.getText();
        String[] head = {"ID", "CÓDIGO", "PRODUCTO", "STOCK", "MODELO", "MARCA"};
        String[] records = new String[8];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL="SELECT * FROM tbl_producto INNER JOIN tbl_stock ON tbl_producto.producto_iden = tbl_stock.producto_iden INNER JOIN tbl_marca INNER JOIN tbl_modelo ON tbl_marca.marca_iden = tbl_modelo.marca_iden AND tbl_producto.modelo_iden = tbl_modelo.modelo_iden WHERE prod_cod LIKE '%"+Buscar+"%' OR prod_desc LIKE '%"+Buscar+"%' OR mod_desc   LIKE '%"+Buscar+"%' OR marc_desc LIKE '%"+Buscar+"%'"; //Realiza bùsqueda en la tabla
        try {
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);

            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("producto_iden");
                records[1] = rs.getString("prod_cod");
                records[2] = rs.getString("prod_desc")+", "+rs.getString("prod_caract");
                records[3] = rs.getString("sock_cant");
                records[4] = rs.getString("marc_desc");
                records[5] = rs.getString("mod_desc");
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblRepuestos.setModel(model);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblRepuestos.getColumnModel().getColumn(0).setPreferredWidth(5);
        tblRepuestos.getColumnModel().getColumn(0).setResizable(true);
        tblRepuestos.getColumnModel().getColumn(1).setPreferredWidth(15);
        tblRepuestos.getColumnModel().getColumn(1).setResizable(true);
        tblRepuestos.getColumnModel().getColumn(2).setPreferredWidth(300);
        tblRepuestos.getColumnModel().getColumn(2).setResizable(true);
        tblRepuestos.getColumnModel().getColumn(3).setPreferredWidth(5);
        tblRepuestos.getColumnModel().getColumn(3).setResizable(true); 
        tblRepuestos.getColumnModel().getColumn(4).setPreferredWidth(15);
        tblRepuestos.getColumnModel().getColumn(4).setResizable(true);
        tblRepuestos.getColumnModel().getColumn(5).setPreferredWidth(10);
        tblRepuestos.getColumnModel().getColumn(5).setResizable(true); 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        r_txtId = new javax.swing.JTextField();
        r_txtCod = new javax.swing.JTextField();
        r_txtRepuesto = new javax.swing.JTextField();
        r_txtStock = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        r_cbxMarca = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        r_cbxTipoMarca = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        r_cbxTam = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        r_cbxMat = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        r_txtBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRepuestos = new javax.swing.JTable();
        r_btnAgregar = new javax.swing.JButton();
        r_btnGrabar = new javax.swing.JButton();
        r_btnCancelar = new javax.swing.JButton();
        r_btnEliminar = new javax.swing.JButton();
        r_btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REGISTRO DE REPUESTOS O AUTOPARTES");
        setPreferredSize(new java.awt.Dimension(1250, 796));

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GESTIONAR LA INFORMACIÓN DEL REPUESTO O AUTOPARTE DE LA EMPRESA");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel2.setText("ID:");

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel3.setText("CÓDIGO:");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel4.setText("NOMBRE DE PRODUCTO:");

        jLabel6.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel6.setText("STOCK:");

        r_txtId.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        r_txtId.setEnabled(false);

        r_txtCod.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        r_txtCod.setEnabled(false);
        r_txtCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_txtCodActionPerformed(evt);
            }
        });

        r_txtRepuesto.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        r_txtRepuesto.setEnabled(false);
        r_txtRepuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_txtRepuestoActionPerformed(evt);
            }
        });

        r_txtStock.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        r_txtStock.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("CARACTERÍSTICAS ADICIONALES DEL PRODUCTO");

        jLabel9.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel9.setText("MARCA:");

        r_cbxMarca.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        r_cbxMarca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        r_cbxMarca.setEnabled(false);
        r_cbxMarca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                r_cbxMarcaItemStateChanged(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel10.setText("TIPO DE MARCA:");

        r_cbxTipoMarca.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        r_cbxTipoMarca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        r_cbxTipoMarca.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel11.setText("TAMAÑO:");

        r_cbxTam.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        r_cbxTam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione tamaño", "Grande", "Mediano", "Chico" }));
        r_cbxTam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        r_cbxTam.setEnabled(false);

        jLabel12.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel12.setText("MATERIAL:");

        r_cbxMat.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        r_cbxMat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione material", "Fierro", "Acero", "Plástico" }));
        r_cbxMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        r_cbxMat.setEnabled(false);
        r_cbxMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_cbxMatActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel13.setText("BUSCAR:");

        r_txtBuscar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        r_txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_txtBuscarActionPerformed(evt);
            }
        });
        r_txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                r_txtBuscarKeyReleased(evt);
            }
        });

        tblRepuestos.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        tblRepuestos.setModel(new javax.swing.table.DefaultTableModel(
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
        tblRepuestos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRepuestosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblRepuestos);

        r_btnAgregar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        r_btnAgregar.setText("AGREGAR");
        r_btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        r_btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_btnAgregarActionPerformed(evt);
            }
        });

        r_btnGrabar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        r_btnGrabar.setText("GRABAR");
        r_btnGrabar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        r_btnGrabar.setEnabled(false);
        r_btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_btnGrabarActionPerformed(evt);
            }
        });

        r_btnCancelar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        r_btnCancelar.setText("Cancelar");
        r_btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        r_btnCancelar.setEnabled(false);
        r_btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_btnCancelarActionPerformed(evt);
            }
        });

        r_btnEliminar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        r_btnEliminar.setText("ELIMINAR");
        r_btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        r_btnEliminar.setEnabled(false);
        r_btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_btnEliminarActionPerformed(evt);
            }
        });

        r_btnSalir.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        r_btnSalir.setText("SALIR");
        r_btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        r_btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(r_btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                                    .addComponent(r_btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(r_btnGrabar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(r_btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                                        .addGap(1, 1, 1))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(r_txtId)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(21, 21, 21)
                                        .addComponent(r_txtCod, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
                                    .addComponent(r_txtRepuesto)))
                            .addComponent(r_btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(r_cbxTipoMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(r_cbxMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addGap(19, 19, 19)
                                .addComponent(r_txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(14, 14, 14)
                                .addComponent(r_cbxTam, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(r_cbxMat, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(jLabel13)
                                .addGap(41, 41, 41)
                                .addComponent(r_txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 51, Short.MAX_VALUE))
                            .addComponent(jScrollPane1)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(r_txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r_txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(r_txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(r_txtRepuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(r_txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(r_cbxMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(r_cbxTipoMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addComponent(jLabel8)
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(r_cbxTam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(r_cbxMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(r_btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(r_btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(r_btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(r_btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(r_btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void r_cbxMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_cbxMatActionPerformed
    }//GEN-LAST:event_r_cbxMatActionPerformed

    private void r_btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_r_btnSalirActionPerformed

    private void r_btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_btnAgregarActionPerformed
        habilitar(true);
        limpiar();
        swap = true;
        r_txtCod.requestFocusInWindow();
    }//GEN-LAST:event_r_btnAgregarActionPerformed

    private void r_btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_btnCancelarActionPerformed
        habilitar(false);
        limpiar();
    }//GEN-LAST:event_r_btnCancelarActionPerformed

    private void r_cbxMarcaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_r_cbxMarcaItemStateChanged
        //estado de cambio del item
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String marca = (String) r_cbxMarca.getSelectedItem();//Selecciono el item del combo Box y lo almaceno en la variable categoria
            mostrarTiposMarca(marca); //ejecuto el metodo de mostrar el pedido dependiendo de la categoria seleccionada
        }
    }//GEN-LAST:event_r_cbxMarcaItemStateChanged

    private void r_btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_btnGrabarActionPerformed
        
        String[] cod = new String[2];
        //Modelo para establecer en la tabla
        try{
            String login="SELECT * FROM tbl_producto where prod_cod = '"+r_txtCod.getText()+"'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                cod[0]=rs.getString("producto_iden");
                cod[1]=rs.getString("prod_cod");
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }    
        
        //Mensaje previo de anticipo para que se ejecute cada decision del crud
        if(r_txtCod.getText().equals("") || r_txtRepuesto.getText().equals("") ){
            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
        }else if(r_cbxMarca.getSelectedItem().equals("Seleccione la marca") || r_cbxTipoMarca.getSelectedItem().equals("Seleccione la marca")){
            JOptionPane.showMessageDialog(null, "No a elegido la marca o el modelo");  
        }else{
            int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un nuevo registro?");
            if (m == 0) {
                if (swap) {
                    if(r_txtCod.getText().equals(cod[1])){
                        JOptionPane.showMessageDialog(null, "El producto se está repitiendo");
                    }else{
                        insertRepuestos();
                    }
                } else {
                    updateRepuestos();
                }
                habilitar(false);//cuando quiero deshabilitar los textos
                limpiar();
                MostrarRepuestos();
            }
        }
    }//GEN-LAST:event_r_btnGrabarActionPerformed

    private void r_btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_btnEliminarActionPerformed
        int fsel = tblRepuestos.getSelectedRow();
            if(fsel == -1){
                JOptionPane.showMessageDialog(null, "Debe seleccionar una fila");
            }else{
                int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar la marca?");
                    if (m == 0) {
                        DeleteRepuestos();
                    }
                    limpiar();
            }
        habilitar(false);
        MostrarRepuestos();
    }//GEN-LAST:event_r_btnEliminarActionPerformed

    private void tblRepuestosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRepuestosMouseClicked
        printTable();
        habilitar(true);
        swap = false;
    }//GEN-LAST:event_tblRepuestosMouseClicked

    private void r_txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_r_txtBuscarKeyReleased
        BuscarRepuestos();
    }//GEN-LAST:event_r_txtBuscarKeyReleased

    private void r_txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_txtCodActionPerformed
       r_txtRepuesto.requestFocusInWindow();
    }//GEN-LAST:event_r_txtCodActionPerformed

    private void r_txtRepuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_txtRepuestoActionPerformed
    }//GEN-LAST:event_r_txtRepuestoActionPerformed

    private void r_txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_txtBuscarActionPerformed
        r_txtCod.requestFocusInWindow();
    }//GEN-LAST:event_r_txtBuscarActionPerformed

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
            java.util.logging.Logger.getLogger(frmRepuestos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmRepuestos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmRepuestos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmRepuestos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmRepuestos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton r_btnAgregar;
    private javax.swing.JButton r_btnCancelar;
    private javax.swing.JButton r_btnEliminar;
    private javax.swing.JButton r_btnGrabar;
    private javax.swing.JButton r_btnSalir;
    private javax.swing.JComboBox<String> r_cbxMarca;
    private javax.swing.JComboBox<String> r_cbxMat;
    private javax.swing.JComboBox<String> r_cbxTam;
    private javax.swing.JComboBox<String> r_cbxTipoMarca;
    private javax.swing.JTextField r_txtBuscar;
    private javax.swing.JTextField r_txtCod;
    private javax.swing.JTextField r_txtId;
    private javax.swing.JTextField r_txtRepuesto;
    private javax.swing.JSpinner r_txtStock;
    private javax.swing.JTable tblRepuestos;
    // End of variables declaration//GEN-END:variables
}
