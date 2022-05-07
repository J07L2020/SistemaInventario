package frames;

import Clases.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmProveedores extends javax.swing.JFrame {

    //Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
        
        private boolean swap = false;
    
    public frmProveedores() {
        initComponents();
        this.setLocationRelativeTo(null);
        MostrarProveedores();
    }
    
     private void limpiar(){
        p_txtId.setText("");
        p_txtRuc.setText("");
        p_txtEmpresa.setText("");
        p_txtDireccion.setText("");
        p_txtCelular.setText("");
        p_txtCorreo.setText("");
    }
    
    private void habilitar(boolean t){
        p_txtRuc.setEnabled(t);
        p_txtEmpresa.setEnabled(t);
        p_txtDireccion.setEnabled(t);
        p_txtCelular.setEnabled(t);
        p_txtCorreo.setEnabled(t);
        
        //Botones
        p_btnGrabar.setEnabled(t);
        p_btnCancelar.setEnabled(t);
        p_btnAgregar.setEnabled(!t);
        p_btnEliminar.setEnabled(t);
    }

    //MÉTODO PARA MOSTRAR INFORMACIÓN DEL PROVEEDOR EN TABLA
    public void MostrarProveedores(){
        String[] head = {"ID","RUC", "NOMBRE","DIRECCIÓN", "CELULAR","CORREO"};
        String[] records = new String[7];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL="SELECT * FROM tbl_proveedor"; //Realiza bùsqueda en la tabla
        try{
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("proveedor_iden");
                records[1]=rs.getString("prov_ruc");
                records[2]=rs.getString("prov_nom");
                records[3]=rs.getString("prov_dire");         
                records[4]=rs.getString("prov_celu");
                records[5]=rs.getString("prov_email"); 
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblProveedores.setModel(model);
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de bùsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblProveedores.getColumnModel().getColumn(0).setPreferredWidth(3);
        tblProveedores.getColumnModel().getColumn(0).setResizable(true);
        tblProveedores.getColumnModel().getColumn(1).setPreferredWidth(10);
        tblProveedores.getColumnModel().getColumn(1).setResizable(true);
        tblProveedores.getColumnModel().getColumn(2).setPreferredWidth(160);
        tblProveedores.getColumnModel().getColumn(2).setResizable(true);
        tblProveedores.getColumnModel().getColumn(3).setPreferredWidth(160);
        tblProveedores.getColumnModel().getColumn(3).setResizable(true);
        tblProveedores.getColumnModel().getColumn(4).setPreferredWidth(10);
        tblProveedores.getColumnModel().getColumn(4).setResizable(true);
        tblProveedores.getColumnModel().getColumn(5).setPreferredWidth(120);
        tblProveedores.getColumnModel().getColumn(5).setResizable(true);
    }
    
    //MÉTODO PARA INSERTAR MODELOS
    private void insertarProveedores(){
        try{
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into tbl_proveedor (prov_ruc, prov_nom, prov_dire, prov_celu, prov_email) value (?,?,?,?,?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, p_txtRuc.getText());
            pst.setString(2, p_txtEmpresa.getText().toUpperCase()); 
            pst.setString(3, p_txtDireccion.getText().toUpperCase());
            pst.setString(4, p_txtCelular.getText());
            pst.setString(5, p_txtCorreo.getText().toUpperCase());
            
            pst.execute();//ejecutar las instrucciones          
            JOptionPane.showMessageDialog(null, "Registro insertado con èxito");         
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: "+e.getMessage());
        }
    }
    
    //MÉTODO PARA ACTUALIZAR LA INFORMACIÓN DEL PROVEEDOR
    private void actualizarProveedores(){
        try{
            //Prepara sentencia de sql; instrucciòn para actualizar en la bbdd
            String comandSQL = "update tbl_proveedor set prov_ruc=?, prov_nom=?, prov_dire=?, prov_celu=?, prov_email=? where proveedor_iden=?";
            
            int SelectionFile = tblProveedores.getSelectedRow();
            String idal = (String) tblProveedores.getValueAt(SelectionFile, 0);//casting
            
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, p_txtRuc.getText());
            pst.setString(2, p_txtEmpresa.getText().toUpperCase());
            pst.setString(3, p_txtDireccion.getText().toUpperCase());
            pst.setString(4, p_txtCelular.getText());
            pst.setString(5, p_txtCorreo.getText().toUpperCase());
            
            pst.setString(6, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Registro actualizado correctamente");          
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado la actualizaciòn: "+e.getMessage());
        }
    }
    
    //MÉTODO IMPRIMIR DESDE LA TABLA A LOS CAMPOS
    private void printTable(){
        PreparedStatement ps = null;
        ResultSet rs =null;
        try{
            
            int Fila = tblProveedores.getSelectedRow();
            String codigo = tblProveedores.getValueAt(Fila, 0).toString();
            
            ps = c.prepareStatement("SELECT * FROM tbl_proveedor WHERE proveedor_iden=?");
            ps.setString(1, codigo);
            rs = ps.executeQuery();
            
            while (rs.next()){
                p_txtId.setText(rs.getString("proveedor_iden"));
                p_txtRuc.setText(rs.getString("prov_ruc"));
                p_txtEmpresa.setText(rs.getString("prov_nom"));
                p_txtDireccion.setText(rs.getString("prov_dire"));
                p_txtCelular.setText(rs.getString("prov_celu"));
                p_txtCorreo.setText(rs.getString("prov_email"));
            }
            
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
    
    //MÉTODO PARA ELIMINAR PROVEEDORES
    public void eliminarProveedor() {
        try {
            //Campos a actualizar
            String comandSQL = "delete from tbl_proveedor where proveedor_iden=?";
            int SelectionFile = tblProveedores.getSelectedRow();
            String idal = (String) tblProveedores.getValueAt(SelectionFile, 0);//casting
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
            pst.setString(1, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Se ha eliminado el registro con èxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha eliminado el registro: " + e.getMessage());
        }
    } 
    
    //MÉTODO PARA BUSCAR REGISTROS DE LOS PROVEEDORES
    private void BuscarProveedores(){
        String Buscar = p_txtBuscar.getText();
        String[] head = {"ID","RUC", "NOMBRE","DIRECCIÓN", "CELULAR","CORREO"};
        String[] records = new String[6];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL="SELECT * FROM tbl_proveedor WHERE prov_ruc LIKE '%"+Buscar+"%' OR prov_nom LIKE '%"+Buscar+"%'  "; //Realiza bùsqueda en la tabla
        try{
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("proveedor_iden");
                records[1]=rs.getString("prov_ruc");
                records[2]=rs.getString("prov_nom");
                records[3]=rs.getString("prov_dire");         
                records[4]=rs.getString("prov_celu");
                records[5]=rs.getString("prov_email"); 
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblProveedores.setModel(model);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblProveedores.getColumnModel().getColumn(0).setPreferredWidth(3);
        tblProveedores.getColumnModel().getColumn(0).setResizable(true);
        tblProveedores.getColumnModel().getColumn(1).setPreferredWidth(10);
        tblProveedores.getColumnModel().getColumn(1).setResizable(true);
        tblProveedores.getColumnModel().getColumn(2).setPreferredWidth(160);
        tblProveedores.getColumnModel().getColumn(2).setResizable(true);
        tblProveedores.getColumnModel().getColumn(3).setPreferredWidth(160);
        tblProveedores.getColumnModel().getColumn(3).setResizable(true);
        tblProveedores.getColumnModel().getColumn(4).setPreferredWidth(10);
        tblProveedores.getColumnModel().getColumn(4).setResizable(true);
        tblProveedores.getColumnModel().getColumn(5).setPreferredWidth(120);
        tblProveedores.getColumnModel().getColumn(5).setResizable(true);
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProveedores = new javax.swing.JTable();
        p_txtBuscar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        p_txtRuc = new javax.swing.JTextField();
        p_txtEmpresa = new javax.swing.JTextField();
        p_txtDireccion = new javax.swing.JTextField();
        p_btnAgregar = new javax.swing.JButton();
        p_btnGrabar = new javax.swing.JButton();
        p_btnCancelar = new javax.swing.JButton();
        p_btnEliminar = new javax.swing.JButton();
        p_btnSalir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        p_txtId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        p_txtCelular = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        p_txtCorreo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REGISTRO DE PROVEEDORES");

        jLabel1.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel1.setText("RUC:");

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel2.setText("NOMBRE O EMPRESA:");

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel3.setText("DIRECCIÓN:");

        tblProveedores.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        tblProveedores.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProveedoresMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProveedores);

        p_txtBuscar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        p_txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txtBuscarActionPerformed(evt);
            }
        });
        p_txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                p_txtBuscarKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel4.setText("BUSCAR:");

        p_txtRuc.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        p_txtRuc.setEnabled(false);
        p_txtRuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txtRucActionPerformed(evt);
            }
        });
        p_txtRuc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                p_txtRucKeyTyped(evt);
            }
        });

        p_txtEmpresa.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        p_txtEmpresa.setEnabled(false);
        p_txtEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txtEmpresaActionPerformed(evt);
            }
        });

        p_txtDireccion.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        p_txtDireccion.setEnabled(false);
        p_txtDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txtDireccionActionPerformed(evt);
            }
        });

        p_btnAgregar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        p_btnAgregar.setText("AGREGAR");
        p_btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_btnAgregarActionPerformed(evt);
            }
        });

        p_btnGrabar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        p_btnGrabar.setText("GRABAR");
        p_btnGrabar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_btnGrabar.setEnabled(false);
        p_btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_btnGrabarActionPerformed(evt);
            }
        });

        p_btnCancelar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        p_btnCancelar.setText("CANCELAR");
        p_btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_btnCancelar.setEnabled(false);
        p_btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_btnCancelarActionPerformed(evt);
            }
        });

        p_btnEliminar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        p_btnEliminar.setText("ELIMINAR");
        p_btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_btnEliminar.setEnabled(false);
        p_btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_btnEliminarActionPerformed(evt);
            }
        });

        p_btnSalir.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        p_btnSalir.setText("SALIR");
        p_btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_btnSalirActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 204));
        jLabel5.setText("GESTIONAR LA INFORMACIÓN DEL PROVEEDOR");

        jLabel6.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel6.setText("ID:");

        p_txtId.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        p_txtId.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel7.setText("CELULAR:");

        p_txtCelular.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        p_txtCelular.setEnabled(false);
        p_txtCelular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txtCelularActionPerformed(evt);
            }
        });
        p_txtCelular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                p_txtCelularKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel8.setText("CORREO:");

        p_txtCorreo.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        p_txtCorreo.setEnabled(false);
        p_txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txtCorreoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel5)
                        .addGap(158, 158, 158)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(p_txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(p_btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(p_btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(p_btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(54, 54, 54)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(p_btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(p_btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel7))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(p_txtCelular, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                                        .addComponent(p_txtDireccion)
                                        .addComponent(p_txtCorreo)))
                                .addComponent(jLabel8)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(p_txtId))
                                        .addComponent(jLabel2))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(p_txtEmpresa))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addGap(69, 69, 69)
                                            .addComponent(jLabel1)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(p_txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(54, 54, 54)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p_txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(p_txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(p_txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(p_txtEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(p_txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(p_txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(p_txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(p_btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(p_btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(p_btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(p_btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addComponent(p_btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void p_btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_btnSalirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_p_btnSalirActionPerformed

    private void p_btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_btnGrabarActionPerformed
        // TODO add your handling code here:
        if(p_txtRuc.getText().equals("") || p_txtEmpresa.getText().equals("") || p_txtDireccion.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
        }else{
            int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un nuevo registro?");
            if(m==0){
                if(swap){
                    insertarProveedores();
                }else{
                    actualizarProveedores();
                }
                limpiar();
            }
            habilitar(false);//cuando quiero deshabilitar los textos
            MostrarProveedores();
        }
    }//GEN-LAST:event_p_btnGrabarActionPerformed

    private void p_btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_btnCancelarActionPerformed
        // TODO add your handling code here:
        habilitar(false);
    }//GEN-LAST:event_p_btnCancelarActionPerformed

    private void p_btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_btnEliminarActionPerformed
        
        int fsel = tblProveedores.getSelectedRow();
            if(fsel == -1){
                JOptionPane.showMessageDialog(null, "Debe seleccionar una fila");
            }else{
                int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar la marca?");
                if(m==0){
                    eliminarProveedor();
                }
                limpiar();
            }
            habilitar(false);
            MostrarProveedores();
        
    }//GEN-LAST:event_p_btnEliminarActionPerformed

    private void tblProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProveedoresMouseClicked
        // TODO add your handling code here:
        printTable();
        habilitar(true);
        swap = false;
    }//GEN-LAST:event_tblProveedoresMouseClicked

    private void p_btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_btnAgregarActionPerformed
        // TODO add your handling code here:
        limpiar();
        habilitar(true);
        swap = true;
        p_txtRuc.requestFocusInWindow();
    }//GEN-LAST:event_p_btnAgregarActionPerformed

    private void p_txtRucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p_txtRucKeyTyped
        // TODO add your handling code here:
        //VALIDACIÓN DE CAMPO RUC
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >=48 && key6 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(p_txtRuc.getText().length()>=11){
            evt.consume();
        }
    }//GEN-LAST:event_p_txtRucKeyTyped

    private void p_txtCelularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p_txtCelularKeyTyped
        //VALIDACIÓN DE CAMPO CELULAR
        int key5 = evt.getKeyChar();
        boolean numeros = key5 >=48 && key5 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(p_txtCelular.getText().length()>=9){
            evt.consume();
        }
    }//GEN-LAST:event_p_txtCelularKeyTyped

    private void p_txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p_txtBuscarKeyReleased
        // TODO add your handling code here:
        BuscarProveedores();
    }//GEN-LAST:event_p_txtBuscarKeyReleased

    private void p_txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_p_txtBuscarActionPerformed

    private void p_txtRucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txtRucActionPerformed
    p_txtEmpresa.requestFocusInWindow();
    }//GEN-LAST:event_p_txtRucActionPerformed

    private void p_txtEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txtEmpresaActionPerformed
        p_txtDireccion.requestFocusInWindow();
    }//GEN-LAST:event_p_txtEmpresaActionPerformed

    private void p_txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txtDireccionActionPerformed
        p_txtCelular.requestFocusInWindow();
    }//GEN-LAST:event_p_txtDireccionActionPerformed

    private void p_txtCelularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txtCelularActionPerformed
        p_txtCorreo.requestFocusInWindow();
    }//GEN-LAST:event_p_txtCelularActionPerformed

    private void p_txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txtCorreoActionPerformed
        if(p_txtRuc.getText().equals("") || p_txtEmpresa.getText().equals("") || p_txtDireccion.getText().equals("") || p_txtCelular.getText().equals("") || p_txtCorreo.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Hay campos vacíos");
            }else{
                int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un nuevo registro?");
                if(m==0){
                    if(swap){
                        insertarProveedores();
                    }else{
                        actualizarProveedores();
                    }
                    habilitar(false);//cuando quiero deshabilitar los textos
                    limpiar();
                    MostrarProveedores();
                }
            }
    }//GEN-LAST:event_p_txtCorreoActionPerformed

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
            java.util.logging.Logger.getLogger(frmProveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmProveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmProveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmProveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmProveedores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton p_btnAgregar;
    private javax.swing.JButton p_btnCancelar;
    private javax.swing.JButton p_btnEliminar;
    private javax.swing.JButton p_btnGrabar;
    private javax.swing.JButton p_btnSalir;
    private javax.swing.JTextField p_txtBuscar;
    private javax.swing.JTextField p_txtCelular;
    private javax.swing.JTextField p_txtCorreo;
    private javax.swing.JTextField p_txtDireccion;
    private javax.swing.JTextField p_txtEmpresa;
    private javax.swing.JTextField p_txtId;
    private javax.swing.JTextField p_txtRuc;
    private javax.swing.JTable tblProveedores;
    // End of variables declaration//GEN-END:variables
}
