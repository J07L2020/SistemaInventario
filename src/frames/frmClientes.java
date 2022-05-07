package frames;

import Clases.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;

public class frmClientes extends javax.swing.JFrame {

    //Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
        
    private boolean swap = false;

    public frmClientes() {
        initComponents();
        this.setLocationRelativeTo(null);
        MostrarClientesTotales();
    }
    
         private void limpiar(){
        cli_txtId.setText("");
        cli_txtRucParticular.setText("");
        cli_txtCelularParticular.setText("");
        cli_txtNombres.setText("");
        cli_txtRucEmpresa.setText("");
        cli_txtCelularEmpresa.setText("");
        cli_cbxCliente.setSelectedIndex(0);
        cli_txtNombreEmpresa.setText("");
        cli_txtDireccionEmpresa.setText("");
        cli_txtDireccionParticular.setText("");
    }
    
    private void habilitarPart(boolean t){
        cli_txtRucParticular.setEnabled(t);
        cli_txtCelularParticular.setEnabled(t);
        cli_txtNombres.setEnabled(t);
        cli_txtDireccionParticular.setEnabled(t);
    }
    private void habilitarEmp(boolean t){
        cli_txtRucEmpresa.setEnabled(t);
        cli_txtCelularEmpresa.setEnabled(t);
        cli_txtNombreEmpresa.setEnabled(t);
        cli_txtDireccionEmpresa.setEnabled(t);
    }
    private void habilitarBotones(boolean t){
        //Botones
        cli_btnGrabar.setEnabled(t);
        cli_btnEliminar.setEnabled(t);
        cli_btnAgregar.setEnabled(!t);
        cli_btnCancelar.setEnabled(t);
    }
    
    //MOSTRAR INFORMACIÓN DE CLIENTES TOTALES EN LA TABLA
    public void MostrarClientesTotales(){
        String[] head = {"ID","RUC O DNI", "CLIENTE","CELULAR", "TIPO"};
        String[] records = new String[6];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda

         try{
            String clientes="SELECT * FROM tbl_cliente"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(clientes);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("cliente_iden");
                records[1]=rs.getString("cli_ruc");
                records[2]=rs.getString("cli_desc");
                records[3]=rs.getString("cli_celu");
                records[4]=rs.getString("cli_tipc");
                model.addRow(records);
            } 
            cli_tblCliente.setModel(model);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
        
         //REDIMENSIONAR LAS COLUMNAS
        cli_tblCliente.getColumnModel().getColumn(0).setPreferredWidth(5);
        cli_tblCliente.getColumnModel().getColumn(0).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(1).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(1).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(2).setPreferredWidth(150);
        cli_tblCliente.getColumnModel().getColumn(2).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(3).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(3).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(4).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(4).setResizable(true);
         
    }
    
    //MOSTRAR INFORMACIÓN DE CLIENTES PARTICULARES EN LA TABLA
    public void MostrarClientesPart(){
        String[] head = {"ID","RUC O DNI", "NOMBRE COMPLETO","CELULAR","DIRECCIÓN"};
        String[] records = new String[6];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda

        try{
            String clientes="SELECT * FROM tbl_cliente WHERE cli_tipc= 'PARTICULAR'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(clientes);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("cliente_iden");
                records[1]=rs.getString("cli_ruc");
                records[2]=rs.getString("cli_desc");
                records[3]=rs.getString("cli_celu");
                records[4]=rs.getString("cli_dire");
                model.addRow(records);
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
            //Se obtiene de la tabla y se va modificando
            cli_tblCliente.setModel(model);
            
            //REDIMENSIONAR LAS COLUMNAS
        cli_tblCliente.getColumnModel().getColumn(0).setPreferredWidth(5);
        cli_tblCliente.getColumnModel().getColumn(0).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(1).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(1).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(2).setPreferredWidth(150);
        cli_tblCliente.getColumnModel().getColumn(2).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(3).setPreferredWidth(10);
        cli_tblCliente.getColumnModel().getColumn(3).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(4).setPreferredWidth(100);
        cli_tblCliente.getColumnModel().getColumn(4).setResizable(true);
    }
    
    //MOSTRAR INFORMACIÓN DE CLIENTES TOTALES EN LA TABLA
    public void MostrarClientesEmp(){
        String[] head = {"ID","RUC", "EMPRESA","CELULAR","DIRECCIÓN"};
        String[] records = new String[6];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
         
        try{
            String clientes="SELECT * FROM tbl_cliente WHERE cli_tipc = 'EMPRESA'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(clientes);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("cliente_iden");
                records[1]=rs.getString("cli_ruc");
                records[2]=rs.getString("cli_desc");
                records[3]=rs.getString("cli_celu");
                records[4]=rs.getString("cli_dire");
                model.addRow(records);
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
            //Se obtiene de la tabla y se va modificando
            cli_tblCliente.setModel(model);
            
            //REDIMENSIONAR LAS COLUMNAS
        cli_tblCliente.getColumnModel().getColumn(0).setPreferredWidth(5);
        cli_tblCliente.getColumnModel().getColumn(0).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(1).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(1).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(2).setPreferredWidth(150);
        cli_tblCliente.getColumnModel().getColumn(2).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(3).setPreferredWidth(10);
        cli_tblCliente.getColumnModel().getColumn(3).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(4).setPreferredWidth(100);
        cli_tblCliente.getColumnModel().getColumn(4).setResizable(true);
            
    }
    
    //MÉTODO PARA INSERTAR CLIENTES EMPRESAS
    private void insertarClientesEmp(){
        try{
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into tbl_cliente (cli_ruc, cli_celu, cli_desc, cli_tipc, cli_dire) value (?,?,?,?,?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, cli_txtRucEmpresa.getText());
            pst.setString(2, cli_txtCelularEmpresa.getText()); 
            pst.setString(3, cli_txtNombreEmpresa.getText().toUpperCase());
            pst.setString(4, String.valueOf(cli_cbxCliente.getSelectedItem()).toUpperCase());
            pst.setString(5, cli_txtDireccionEmpresa.getText().toUpperCase());
            
            pst.execute();//ejecutar las instrucciones          
            JOptionPane.showMessageDialog(null, "Registro insertado con èxito");         
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: "+e.getMessage());
        }
    }
    
    //MÉTODO PARA INSERTAR CLIENTES PARTICULARES
    private void insertarClientesPart(){
        try{
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into tbl_cliente (cli_ruc, cli_celu, cli_desc, cli_tipc, cli_dire) value (?,?,?,?,?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, cli_txtRucParticular.getText()); 
            pst.setString(2, cli_txtCelularParticular.getText());
            pst.setString(3, cli_txtNombres.getText().toUpperCase());
            pst.setString(4, String.valueOf(cli_cbxCliente.getSelectedItem()).toUpperCase());
            pst.setString(5, cli_txtDireccionParticular.getText().toUpperCase());
            
            pst.execute();//ejecutar las instrucciones          
            JOptionPane.showMessageDialog(null, "Registro insertado con éxito");         
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: "+e.getMessage());
        }
    }
    
    //MÉTODO PARA ACTUALIZAR LA INFORMACIÓN DEL CLIENTE EMPRESA
    private void actualizarClienteEmp(){
        try{
            //Prepara sentencia de sql; instrucciòn para actualizar en la bbdd
            String comandSQL = "update tbl_cliente set cli_ruc=?, cli_celu=?, cli_desc=?, cli_tipc=?, cli_dire=? where cliente_iden=?";
            
            int SelectionFile = cli_tblCliente.getSelectedRow();
            String idal = (String) cli_tblCliente.getValueAt(SelectionFile, 0);//casting
            
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, cli_txtRucEmpresa.getText());
            pst.setString(2, cli_txtCelularEmpresa.getText()); 
            pst.setString(3, cli_txtNombreEmpresa.getText().toUpperCase());
            pst.setString(4, String.valueOf(cli_cbxCliente.getSelectedItem()).toUpperCase());
            pst.setString(5, cli_txtDireccionEmpresa.getText().toUpperCase());
            
            pst.setString(6, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Registro actualizado correctamente");          
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado la actualizaciòn: "+e.getMessage());
        }
    }
    
    //MÉTODO PARA ACTUALIZAR LA INFORMACIÓN DEL CLIENTE PARTICULAR
    private void actualizarClientePart(){
        try{
            //Prepara sentencia de sql; instrucciòn para actualizar en la bbdd
            String comandSQL = "update tbl_cliente set cli_ruc=?, cli_celu=?, cli_desc=?, cli_tipc=?, cli_dire=? WHERE cliente_iden=?";
            
            int SelectionFile = cli_tblCliente.getSelectedRow();
            String idal = (String) cli_tblCliente.getValueAt(SelectionFile, 0);//casting
            
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, cli_txtRucParticular.getText()); 
            pst.setString(2, cli_txtCelularParticular.getText());
            pst.setString(3, cli_txtNombres.getText().toUpperCase());
            pst.setString(4, String.valueOf(cli_cbxCliente.getSelectedItem()).toUpperCase());
            pst.setString(5, cli_txtDireccionParticular.getText().toUpperCase());
            
            pst.setString(6, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Registro actualizado correctamente");          
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado la actualizaciòn: "+e.getMessage());
        }
    }
    
    //MÉTODO IMPRIMIR DESDE LA TABLA A LOS CAMPOS
    private void printTablePart() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            int Fila = cli_tblCliente.getSelectedRow();
            String codigo = cli_tblCliente.getValueAt(Fila, 0).toString();

            ps = c.prepareStatement("SELECT * FROM tbl_cliente WHERE cliente_iden=? ");
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            while (rs.next()) {
                cli_txtId.setText(rs.getString("cliente_iden"));
                cli_txtRucParticular.setText(rs.getString("cli_ruc"));
                cli_txtCelularParticular.setText(rs.getString("cli_celu"));
                cli_txtNombres.setText(rs.getString("cli_desc"));
                cli_txtDireccionParticular.setText(rs.getString("cli_dire"));
                cli_cbxCliente.setSelectedItem(rs.getString("cli_tipc"));
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    //MÉTODO IMPRIMIR DESDE LA TABLA A LOS CAMPOS
    private void printTableEmp() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            int Fila = cli_tblCliente.getSelectedRow();
            String codigo = cli_tblCliente.getValueAt(Fila, 0).toString();

            ps = c.prepareStatement("SELECT * FROM tbl_cliente WHERE cliente_iden=? ");
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            while (rs.next()) {
                cli_txtId.setText(rs.getString("cliente_iden"));
                cli_txtRucEmpresa.setText(rs.getString("cli_ruc"));
                cli_txtCelularEmpresa.setText(rs.getString("cli_celu"));
                cli_txtNombreEmpresa.setText(rs.getString("cli_desc"));
                cli_cbxCliente.setSelectedItem(rs.getString("cli_tipc"));
                cli_txtDireccionEmpresa.setText(rs.getString("cli_dire"));
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    //MÉTODO PARA ELIMINAR PROVEEDORES
    public void eliminarClientes() {
        try {
            //Campos a actualizar
            String comandSQL = "delete from tbl_cliente where cliente_iden=?";
            int SelectionFile = cli_tblCliente.getSelectedRow();
            String idal = (String) cli_tblCliente.getValueAt(SelectionFile, 0);//casting
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
            pst.setString(1, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Se ha eliminado el registro con èxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha eliminado el registro: " + e.getMessage());
        }
    } 
    
    //MÉTODO PARA BUSCAR REGISTROS DELOS CLIENTES
    public void BuscarClientesTotales(){
        String Buscar = cli_txtBuscar.getText();
        String[] head = {"ID","RUC O DNI", "CLIENTE","CELULAR", "TIPO"};
        String[] records = new String[6];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        try{
            String clientes="SELECT * FROM tbl_cliente WHERE cli_ruc LIKE '%"+Buscar+"%' OR cli_desc LIKE '%"+Buscar+"%' OR cli_tipc LIKE '%"+Buscar+"%'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(clientes);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("cliente_iden");
                records[1]=rs.getString("cli_ruc");
                records[2]=rs.getString("cli_desc");
                records[3]=rs.getString("cli_celu");
                records[4]=rs.getString("cli_tipc");
                model.addRow(records);
            } 
            cli_tblCliente.setModel(model);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        cli_tblCliente.getColumnModel().getColumn(0).setPreferredWidth(5);
        cli_tblCliente.getColumnModel().getColumn(0).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(1).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(1).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(2).setPreferredWidth(150);
        cli_tblCliente.getColumnModel().getColumn(2).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(3).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(3).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(4).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(4).setResizable(true); 
    }
    
    //MÉTODO PARA BUSCAR REGISTROS DELOS CLIENTES
    public void BuscarClientesParticular(){
        String Buscar = cli_txtBuscar.getText();
        String[] head = {"ID","RUC O DNI", "NOMBRE COMPLETO","CELULAR","DIRECCIÓN"};
        String[] records = new String[6];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda

        try{
            String clientes="SELECT * FROM tbl_cliente WHERE cli_tipc= 'PARTICULAR' AND  cli_desc LIKE '%"+Buscar+"%' OR cli_ruc LIKE '%"+Buscar+"%'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(clientes);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("cliente_iden");
                records[1]=rs.getString("cli_ruc");
                records[2]=rs.getString("cli_desc");
                records[3]=rs.getString("cli_celu");
                records[4]=rs.getString("cli_dire");
                model.addRow(records);
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
            //Se obtiene de la tabla y se va modificando
            cli_tblCliente.setModel(model);
            
            //REDIMENSIONAR LAS COLUMNAS
        cli_tblCliente.getColumnModel().getColumn(0).setPreferredWidth(5);
        cli_tblCliente.getColumnModel().getColumn(0).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(1).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(1).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(2).setPreferredWidth(150);
        cli_tblCliente.getColumnModel().getColumn(2).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(3).setPreferredWidth(10);
        cli_tblCliente.getColumnModel().getColumn(3).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(4).setPreferredWidth(100);
        cli_tblCliente.getColumnModel().getColumn(4).setResizable(true);
    }
    
    //MÉTODO PARA BUSCAR REGISTROS DELOS CLIENTES
    public void BuscarClientesEmpresa(){
        String Buscar = cli_txtBuscar.getText();
        String[] head = {"ID","RUC", "EMPRESA","CELULAR","DIRECCIÓN"};
        String[] records = new String[6];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
         
        try{
            String clientes="SELECT * FROM tbl_cliente WHERE cli_ruc LIKE '%"+Buscar+"%' OR cli_desc LIKE '%"+Buscar+"%'"; 
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(clientes);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("cliente_iden");
                records[1]=rs.getString("cli_ruc");
                records[2]=rs.getString("cli_desc");
                records[3]=rs.getString("cli_celu");
                records[4]=rs.getString("cli_dire");
                model.addRow(records);
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
            //Se obtiene de la tabla y se va modificando
            cli_tblCliente.setModel(model);
            
            //REDIMENSIONAR LAS COLUMNAS
        cli_tblCliente.getColumnModel().getColumn(0).setPreferredWidth(5);
        cli_tblCliente.getColumnModel().getColumn(0).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(1).setPreferredWidth(15);
        cli_tblCliente.getColumnModel().getColumn(1).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(2).setPreferredWidth(150);
        cli_tblCliente.getColumnModel().getColumn(2).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(3).setPreferredWidth(10);
        cli_tblCliente.getColumnModel().getColumn(3).setResizable(true);
        cli_tblCliente.getColumnModel().getColumn(4).setPreferredWidth(100);
        cli_tblCliente.getColumnModel().getColumn(4).setResizable(true);
            
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cli_txtBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        cli_tblCliente = new javax.swing.JTable();
        cli_btnAgregar = new javax.swing.JButton();
        cli_btnGrabar = new javax.swing.JButton();
        cli_btnEliminar = new javax.swing.JButton();
        cli_btnCancelar = new javax.swing.JButton();
        cli_btnSalir = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cli_txtId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cli_cbxCliente = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cli_txtRucParticular = new javax.swing.JTextField();
        cli_txtCelularParticular = new javax.swing.JTextField();
        cli_txtNombres = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cli_txtRucEmpresa = new javax.swing.JTextField();
        cli_txtCelularEmpresa = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cli_txtNombreEmpresa = new javax.swing.JTextField();
        cli_cbxBuscar = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        cli_txtDireccionEmpresa = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        cli_txtDireccionParticular = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REGISTRO DE CLIENTES");

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("GESTIONAR LA INFORMACIÓN DEL CLIENTE");

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel2.setText("BUSCAR:");

        cli_txtBuscar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cli_txtBuscarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cli_txtBuscarKeyReleased(evt);
            }
        });

        cli_tblCliente.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_tblCliente.setModel(new javax.swing.table.DefaultTableModel(
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
        cli_tblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cli_tblClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(cli_tblCliente);

        cli_btnAgregar.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        cli_btnAgregar.setText("AGREGAR");
        cli_btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cli_btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_btnAgregarActionPerformed(evt);
            }
        });

        cli_btnGrabar.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        cli_btnGrabar.setText("GRABAR");
        cli_btnGrabar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cli_btnGrabar.setEnabled(false);
        cli_btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_btnGrabarActionPerformed(evt);
            }
        });

        cli_btnEliminar.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        cli_btnEliminar.setText("ELIMINAR");
        cli_btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cli_btnEliminar.setEnabled(false);
        cli_btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_btnEliminarActionPerformed(evt);
            }
        });

        cli_btnCancelar.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        cli_btnCancelar.setText("CANCELAR");
        cli_btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cli_btnCancelar.setEnabled(false);
        cli_btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_btnCancelarActionPerformed(evt);
            }
        });

        cli_btnSalir.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        cli_btnSalir.setText("SALIR");
        cli_btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cli_btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_btnSalirActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel3.setText("ID:");

        cli_txtId.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtId.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel4.setText("DNI O RUC:");

        jLabel5.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel5.setText("TIPO DE CLIENTE:");

        cli_cbxCliente.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_cbxCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione al cliente", "Particular", "Empresa" }));
        cli_cbxCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cli_cbxCliente.setEnabled(false);
        cli_cbxCliente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cli_cbxClienteItemStateChanged(evt);
            }
        });
        cli_cbxCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cli_cbxClienteMouseClicked(evt);
            }
        });
        cli_cbxCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_cbxClienteActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 51));
        jLabel6.setText("CLIENTE PARTICULAR");

        jLabel7.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 51));
        jLabel7.setText("EMPRESA");

        jLabel9.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel9.setText("CELULAR:");

        jLabel10.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel10.setText("NOMBRES:");

        cli_txtRucParticular.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtRucParticular.setEnabled(false);
        cli_txtRucParticular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_txtRucParticularActionPerformed(evt);
            }
        });
        cli_txtRucParticular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cli_txtRucParticularKeyTyped(evt);
            }
        });

        cli_txtCelularParticular.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtCelularParticular.setEnabled(false);
        cli_txtCelularParticular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_txtCelularParticularActionPerformed(evt);
            }
        });
        cli_txtCelularParticular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cli_txtCelularParticularKeyTyped(evt);
            }
        });

        cli_txtNombres.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtNombres.setEnabled(false);
        cli_txtNombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_txtNombresActionPerformed(evt);
            }
        });
        cli_txtNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cli_txtNombresKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel12.setText("RUC:");

        jLabel13.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel13.setText("CELULAR:");

        cli_txtRucEmpresa.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtRucEmpresa.setEnabled(false);
        cli_txtRucEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_txtRucEmpresaActionPerformed(evt);
            }
        });
        cli_txtRucEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cli_txtRucEmpresaKeyTyped(evt);
            }
        });

        cli_txtCelularEmpresa.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtCelularEmpresa.setEnabled(false);
        cli_txtCelularEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_txtCelularEmpresaActionPerformed(evt);
            }
        });
        cli_txtCelularEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cli_txtCelularEmpresaKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel14.setText("EMPRESA:");

        cli_txtNombreEmpresa.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtNombreEmpresa.setEnabled(false);
        cli_txtNombreEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_txtNombreEmpresaActionPerformed(evt);
            }
        });

        cli_cbxBuscar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_cbxBuscar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione al cliente", "Particular", "Empresa" }));
        cli_cbxBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cli_cbxBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_cbxBuscarActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel15.setText("DIRECCIÓN:");

        cli_txtDireccionEmpresa.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtDireccionEmpresa.setEnabled(false);
        cli_txtDireccionEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_txtDireccionEmpresaActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel16.setText("DIRECCION:");

        cli_txtDireccionParticular.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cli_txtDireccionParticular.setEnabled(false);
        cli_txtDireccionParticular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_txtDireccionParticularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(114, 114, 114)
                                        .addComponent(jLabel6))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel12)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel10)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cli_txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel15)
                                            .addComponent(jLabel16))
                                        .addGap(11, 11, 11)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(cli_cbxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(cli_txtNombres, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                            .addComponent(cli_txtCelularParticular, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                            .addComponent(cli_txtRucParticular, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                            .addComponent(cli_txtRucEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                            .addComponent(cli_txtCelularEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                            .addComponent(cli_txtNombreEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                            .addComponent(cli_txtDireccionEmpresa)
                                            .addComponent(cli_txtDireccionParticular)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(170, 170, 170)
                                .addComponent(jLabel7)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cli_btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cli_btnGrabar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cli_btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cli_btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cli_btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cli_cbxBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cli_txtBuscar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cli_txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(cli_cbxBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cli_txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(cli_cbxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cli_btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(cli_btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(cli_txtRucParticular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(cli_txtCelularParticular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(cli_txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cli_btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(cli_txtDireccionParticular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(41, 41, 41)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cli_txtRucEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(27, 27, 27)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(cli_txtCelularEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel13)))
                                            .addComponent(jLabel12)))
                                    .addComponent(cli_btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(cli_txtNombreEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(cli_txtDireccionEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cli_btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(91, 91, 91))))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cli_btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_btnSalirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_cli_btnSalirActionPerformed

    private void cli_cbxClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cli_cbxClienteMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cli_cbxClienteMouseClicked

    private void cli_btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_btnAgregarActionPerformed
        // TODO add your handling code here:
        limpiar();
        habilitarBotones(true);
        cli_cbxCliente.setEnabled(true);
        swap = true;
    }//GEN-LAST:event_cli_btnAgregarActionPerformed

    private void cli_cbxClienteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cli_cbxClienteItemStateChanged

    }//GEN-LAST:event_cli_cbxClienteItemStateChanged

    private void cli_cbxClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_cbxClienteActionPerformed
        // TODO add your handling code here:
            if(cli_cbxCliente.getItemAt(cli_cbxCliente.getSelectedIndex()).equals("Particular")){
                habilitarPart(true);
                habilitarEmp(false);
            }else if(cli_cbxCliente.getItemAt(cli_cbxCliente.getSelectedIndex()).equals("Empresa")){
                habilitarPart(false);
                habilitarEmp(true);
            }else{
                habilitarPart(false);
                habilitarEmp(false);
                limpiar();
            }
    }//GEN-LAST:event_cli_cbxClienteActionPerformed

    private void cli_btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_btnCancelarActionPerformed
        // TODO add your handling code here:
        limpiar();
        habilitarPart(false);
        habilitarEmp(false);
        habilitarBotones(false);
        cli_cbxCliente.setEnabled(false);
    }//GEN-LAST:event_cli_btnCancelarActionPerformed

    private void cli_cbxBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_cbxBuscarActionPerformed
        // TODO add your handling code here:
         if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Particular")){
                MostrarClientesPart();
            }else if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Empresa")){
                MostrarClientesEmp();
            }else{
                MostrarClientesTotales();
                limpiar();
                habilitarBotones(swap);
            }
    }//GEN-LAST:event_cli_cbxBuscarActionPerformed

    private void cli_tblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cli_tblClienteMouseClicked
        // TODO add your handling code here:
        cli_cbxCliente.setEnabled(true);
        if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Particular")){
                limpiar();
                printTablePart();
                habilitarPart(true);
                habilitarEmp(false);
                habilitarBotones(!swap);
                cli_cbxCliente.setSelectedIndex(1);
            }else if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Empresa")){
                limpiar();
                printTableEmp();
                habilitarEmp(true);
                habilitarPart(false);
                habilitarBotones(!swap);
                cli_cbxCliente.setSelectedIndex(2);
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione al cliente");
            }
        swap = false;
    }//GEN-LAST:event_cli_tblClienteMouseClicked

    private void cli_btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_btnGrabarActionPerformed
        // TODO add your handling code here:
        int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un"
                + " nuevo registro?");
        if(m==0){
            if(swap){
                    if(cli_cbxCliente.getItemAt(cli_cbxCliente.getSelectedIndex()).equals("Particular")){
                        if(cli_txtRucParticular.getText().equals("")|| cli_txtCelularParticular.getText().equals("")|| cli_txtNombres.getText().equals("")|| cli_txtDireccionParticular.getText().equals("")){
                            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
                        }else{
                            insertarClientesPart();
                            habilitarPart(false);
                            limpiar();
                        }
                    }else if(cli_cbxCliente.getItemAt(cli_cbxCliente.getSelectedIndex()).equals("Empresa")){
                        
                        if(cli_txtRucEmpresa.getText().equals("") || cli_txtCelularEmpresa.getText().equals("")|| cli_txtNombreEmpresa.getText().equals("")|| cli_txtDireccionEmpresa.getText().equals("")){
                            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
                        }else{
                            insertarClientesEmp();
                            habilitarEmp(false);
                            limpiar();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Seleccione al cliente");
                    }
            }else{
                    if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Particular")){
                        actualizarClientePart();
                        habilitarPart(false);
                        limpiar();
                    }else if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Empresa")){
                        actualizarClienteEmp();
                        habilitarEmp(false);
                        limpiar();
                    }else{
                        JOptionPane.showMessageDialog(null, "Seleccione al cliente");
                    }
            }
            MostrarClientesTotales();
        }
    }//GEN-LAST:event_cli_btnGrabarActionPerformed

    private void cli_btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_btnEliminarActionPerformed
        // TODO add your handling code here:
        int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar la marca?");
        if(m==0){
            eliminarClientes();
        }
        habilitarPart(false);
        habilitarEmp(false);
        habilitarBotones(false);
        limpiar();
//        MostrarClientesTotales();
    }//GEN-LAST:event_cli_btnEliminarActionPerformed

    private void cli_txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cli_txtBuscarKeyReleased
       if(cli_txtBuscar.getText().equals("")){
           cli_cbxBuscar.setSelectedIndex(0);
           cli_cbxCliente.setSelectedIndex(0);
            cli_cbxCliente.setEnabled(false);
       }else{
           if(String.valueOf(cli_cbxBuscar.getSelectedItem()).equals("Empresa")){
                BuscarClientesEmpresa();
            }else if(String.valueOf(cli_cbxBuscar.getSelectedItem()).equals("Particular")){
                BuscarClientesParticular();
            }else{
                BuscarClientesTotales();
            }
            cli_cbxCliente.setSelectedIndex(0);
            cli_cbxCliente.setEnabled(false);
       }
    }//GEN-LAST:event_cli_txtBuscarKeyReleased

    private void cli_txtRucEmpresaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cli_txtRucEmpresaKeyTyped
        // TODO add your handling code here:
        //VALIDACIÓN DE CAMPO RUC
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >=48 && key6 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(cli_txtRucEmpresa.getText().length()>=11){
            evt.consume();
        }
    }//GEN-LAST:event_cli_txtRucEmpresaKeyTyped

    private void cli_txtRucParticularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cli_txtRucParticularKeyTyped
        //VALIDACIÓN DE CAMPO RUC
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >=48 && key6 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(cli_txtRucParticular.getText().length()>=11){
            evt.consume();
        }
    }//GEN-LAST:event_cli_txtRucParticularKeyTyped

    private void cli_txtCelularParticularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cli_txtCelularParticularKeyTyped
        //VALIDACIÓN DE CAMPO CELULAR
        int key5 = evt.getKeyChar();
        boolean numeros = key5 >=48 && key5 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(cli_txtCelularParticular.getText().length()>=9){
            evt.consume();
        }
    }//GEN-LAST:event_cli_txtCelularParticularKeyTyped

    private void cli_txtCelularEmpresaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cli_txtCelularEmpresaKeyTyped
        //VALIDACIÓN DE CAMPO CELULAR
        int key5 = evt.getKeyChar();
        boolean numeros = key5 >=48 && key5 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(cli_txtCelularEmpresa.getText().length()>=9){
            evt.consume();
        }
    }//GEN-LAST:event_cli_txtCelularEmpresaKeyTyped

    private void cli_txtNombresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cli_txtNombresKeyTyped
        //VALIDAR CAMPO  PARA SOLOLETRAS
        char letras = evt.getKeyChar();
        boolean caracteres = letras >=65 && letras <= 90 || letras >=97 && letras <= 122 || letras==32;
        if(!caracteres ){
            evt.consume();
        }
    }//GEN-LAST:event_cli_txtNombresKeyTyped

    private void cli_txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cli_txtBuscarKeyPressed
    }//GEN-LAST:event_cli_txtBuscarKeyPressed

    private void cli_txtRucParticularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_txtRucParticularActionPerformed
        cli_txtCelularParticular.requestFocusInWindow();
    }//GEN-LAST:event_cli_txtRucParticularActionPerformed

    private void cli_txtCelularParticularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_txtCelularParticularActionPerformed
        cli_txtNombres.requestFocusInWindow();
    }//GEN-LAST:event_cli_txtCelularParticularActionPerformed

    private void cli_txtNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_txtNombresActionPerformed
        cli_txtDireccionParticular.requestFocusInWindow();
    }//GEN-LAST:event_cli_txtNombresActionPerformed

    private void cli_txtRucEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_txtRucEmpresaActionPerformed
        cli_txtCelularEmpresa.requestFocusInWindow();
    }//GEN-LAST:event_cli_txtRucEmpresaActionPerformed

    private void cli_txtCelularEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_txtCelularEmpresaActionPerformed
        cli_txtNombreEmpresa.requestFocusInWindow();
    }//GEN-LAST:event_cli_txtCelularEmpresaActionPerformed

    private void cli_txtNombreEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_txtNombreEmpresaActionPerformed
        cli_txtDireccionEmpresa.requestFocusInWindow();
    }//GEN-LAST:event_cli_txtNombreEmpresaActionPerformed

    private void cli_txtDireccionParticularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_txtDireccionParticularActionPerformed
        int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un"
                + " nuevo registro?");
        if(m==0){
            if(swap){
                    if(cli_cbxCliente.getItemAt(cli_cbxCliente.getSelectedIndex()).equals("Particular")){
                        if(cli_txtRucParticular.getText().equals("")|| cli_txtCelularParticular.getText().equals("")|| cli_txtNombres.getText().equals("")|| cli_txtDireccionParticular.getText().equals("")){
                            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
                        }else{
                            insertarClientesPart();
                            habilitarPart(false);
                            limpiar();
                        }
                    }else if(cli_cbxCliente.getItemAt(cli_cbxCliente.getSelectedIndex()).equals("Empresa")){
                        
                        if(cli_txtRucEmpresa.getText().equals("") || cli_txtCelularEmpresa.getText().equals("")|| cli_txtNombreEmpresa.getText().equals("")|| cli_txtDireccionEmpresa.getText().equals("")){
                            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
                        }else{
                            insertarClientesEmp();
                            habilitarEmp(false);
                            limpiar();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Seleccione al cliente");
                    }
            }else{
                    if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Particular")){
                        actualizarClientePart();
                        habilitarPart(false);
                        limpiar();
                    }else if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Empresa")){
                        actualizarClienteEmp();
                        habilitarEmp(false);
                        limpiar();
                    }else{
                        JOptionPane.showMessageDialog(null, "Seleccione al cliente");
                    }
            }
            MostrarClientesTotales();
        }
    }//GEN-LAST:event_cli_txtDireccionParticularActionPerformed

    private void cli_txtDireccionEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_txtDireccionEmpresaActionPerformed
        int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un"
                + " nuevo registro?");
        if(m==0){
            if(swap){
                    if(cli_cbxCliente.getItemAt(cli_cbxCliente.getSelectedIndex()).equals("Particular")){
                        if(cli_txtRucParticular.getText().equals("")|| cli_txtCelularParticular.getText().equals("")|| cli_txtNombres.getText().equals("")|| cli_txtDireccionParticular.getText().equals("")){
                            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
                        }else{
                            insertarClientesPart();
                            habilitarPart(false);
                            limpiar();
                        }
                    }else if(cli_cbxCliente.getItemAt(cli_cbxCliente.getSelectedIndex()).equals("Empresa")){
                        
                        if(cli_txtRucEmpresa.getText().equals("") || cli_txtCelularEmpresa.getText().equals("")|| cli_txtNombreEmpresa.getText().equals("")|| cli_txtDireccionEmpresa.getText().equals("")){
                            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
                        }else{
                            insertarClientesEmp();
                            habilitarEmp(false);
                            limpiar();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Seleccione al cliente");
                    }
            }else{
                    if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Particular")){
                        actualizarClientePart();
                        habilitarPart(false);
                        limpiar();
                    }else if(cli_cbxBuscar.getItemAt(cli_cbxBuscar.getSelectedIndex()).equals("Empresa")){
                        actualizarClienteEmp();
                        habilitarEmp(false);
                        limpiar();
                    }else{
                        JOptionPane.showMessageDialog(null, "Seleccione al cliente");
                    }
            }
            MostrarClientesTotales();
        }
    }//GEN-LAST:event_cli_txtDireccionEmpresaActionPerformed

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
            java.util.logging.Logger.getLogger(frmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cli_btnAgregar;
    private javax.swing.JButton cli_btnCancelar;
    private javax.swing.JButton cli_btnEliminar;
    private javax.swing.JButton cli_btnGrabar;
    private javax.swing.JButton cli_btnSalir;
    private javax.swing.JComboBox<String> cli_cbxBuscar;
    private javax.swing.JComboBox<String> cli_cbxCliente;
    private javax.swing.JTable cli_tblCliente;
    private javax.swing.JTextField cli_txtBuscar;
    private javax.swing.JTextField cli_txtCelularEmpresa;
    private javax.swing.JTextField cli_txtCelularParticular;
    private javax.swing.JTextField cli_txtDireccionEmpresa;
    private javax.swing.JTextField cli_txtDireccionParticular;
    private javax.swing.JTextField cli_txtId;
    private javax.swing.JTextField cli_txtNombreEmpresa;
    private javax.swing.JTextField cli_txtNombres;
    private javax.swing.JTextField cli_txtRucEmpresa;
    private javax.swing.JTextField cli_txtRucParticular;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
