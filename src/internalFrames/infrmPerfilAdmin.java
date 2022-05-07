
package internalFrames;

import Clases.ConexionBD;
import Clases.perfilUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class infrmPerfilAdmin extends javax.swing.JInternalFrame {

    //Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
    
    public infrmPerfilAdmin() {
        initComponents();
        this.lblVer.setVisible(false);
        MostrarAdminTotales();
    }
    //MÉTODO PARA MOSTRAR LA INFORMACIÓN DEL ADMINISTRADOR
    public void mostrarPerfilAdmin(String pass){//Recibe el código a travez del frm Menu admin
        perfilUsuario info = new perfilUsuario();//Clase usuario
        info.mostrarPerfil(pass);
        txtEmail.setText(info.getCorreo());
        txtApeNom.setText(info.getNombres()+", "+info.getApellidos());
        txtId.setText(info.getId());
        txtDni.setText(info.getDni());
        txtRuc.setText(info.getRuc());
        txtSol.setText(info.getClaveSol());
        txtCelular.setText(info.getCelular());
    }

    //MÉTODO PARA HABILITAR LOS CAMPOS DEL PERFIL
    public void habilitar(boolean t){
        btnGrabarPerfil.setEnabled(t);
        txtEmail.setEnabled(t);
        txtApeNom.setEnabled(t);
        txtDni.setEnabled(t);
        txtRuc.setEnabled(t);
        txtSol.setEnabled(t);
        txtCelular.setEnabled(t);
        btnGrabarPerfil.setEnabled(t);
        btnCancelarMod.setEnabled(t);
        btnModatos.setEnabled(!t);
    }
    
    //MÉTODO PARA VERIFICAR LA CONTRASEÑA QUE ESTA ESCRIBIENDO EN LA CAJA DE TEXTO
    public void Verificar(String pass, String dni){
        
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        String login="SELECT * FROM tbl_usuario WHERE user_pass = '"+pass+"' AND user_dni = '"+dni+"'"; 
                //Realiza bùsqueda en la tabla trabajadores
        try{
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("user_pass");
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de Consulta del cóntraseña "+e.getMessage());
        }
        
         if(this.txtPassOld.getText().equals(records[0])){
                    JOptionPane.showMessageDialog(null, "Verificación de contraseña exitosa");
                    txtNewPass.setEnabled(true);
                    btnRegistrar.setEnabled(true);
                    btnCancelar.setEnabled(true);
                    txtPassOld.setEnabled(false);
                    btnVerificarPass.setEnabled(false);
                }else if(this.txtPassOld.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Campo Vacío");
                }else{
                    txtPassOld.requestFocusInWindow();
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                }
    }
    
    //MOSTRAR INFORMACIÓN DE ADMINISTRADORES EN LA TABLA
    public void MostrarAdminTotales(){
        String[] head = {"DNI","ADMINISTRADOR", "CELULAR","CORREO"};
        String[] records = new String[4];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL="SELECT user_dni, CONCAT(user_nom, ' ' ,user_ape) AS administrador, user_celu, user_email FROM tbl_usuario INNER JOIN tbl_tipo_usuario ON tbl_usuario.tipo_usuario_iden = tbl_tipo_usuario.tipo_usuario_iden WHERE tipo_usuario_desc = 'ADMINISTRADOR'"; //Realiza bùsqueda en la tabla
        try{
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("user_dni");
                records[1]=rs.getString("administrador");
                records[2]=rs.getString("user_celu");
                records[3]=rs.getString("user_email");                
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblAdmin.setModel(model);
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de bùsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblAdmin.getColumnModel().getColumn(0).setPreferredWidth(15);
        tblAdmin.getColumnModel().getColumn(0).setResizable(true);
        tblAdmin.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblAdmin.getColumnModel().getColumn(1).setResizable(true);
        tblAdmin.getColumnModel().getColumn(2).setPreferredWidth(15);
        tblAdmin.getColumnModel().getColumn(2).setResizable(true);
        tblAdmin.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblAdmin.getColumnModel().getColumn(3).setResizable(true);
        
    }
    
   //CÓDIGO PARA ACTUALIZAR CONTRASEÑA DEL USUARIO ADMINISTRADOR
    public void insertNewPass(String newpass, String pass){
        try{
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "UPDATE tbl_usuario SET user_pass = '"+newpass+"' WHERE user_pass = '"+pass+"'";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
            
            pst.execute();//ejecutar las instrucciones          
            JOptionPane.showMessageDialog(null, "Se ha actualizado tu contraseña exitosamente");     
            txtPassOld.setText("");
            txtNewPass.setText("");
            txtNewPass.setEnabled(false);
            btnRegistrar.setEnabled(false);
            btnCancelar.setEnabled(false);
            txtPassOld.setEnabled(true);
            btnVerificarPass.setEnabled(true);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha actualizado: "+e.getMessage());
        }
    }
    
    //CÓDIGO PARA ACTUALIZAR INFORMACIÓNDEL USUARIO ADMINISTRADOR
    public void updateNewInfo(String id, String correo, String nombres, String apellidos, String dni, String ruc, String sol, String cel){
        try{
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "UPDATE tbl_usuario SET user_email = '"+correo+"', user_nom = '"+nombres+"', user_ape = '"+apellidos+"', user_dni = '"+dni+"', user_ruc = '"+ruc+"', user_cl_sol = '"+sol+"', user_celu = '"+cel+"' WHERE usuario_iden = '"+id+"' AND user_dni = '"+dni+"'";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
            
            pst.execute();//ejecutar las instrucciones          
            JOptionPane.showMessageDialog(null, "Se ha actualizado tu información exitosamente");     
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha actualizado: "+e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtDni = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtApeNom = new javax.swing.JTextField();
        txtRuc = new javax.swing.JTextField();
        txtSol = new javax.swing.JTextField();
        txtCelular = new javax.swing.JTextField();
        btnModatos = new javax.swing.JButton();
        btnGrabarPerfil = new javax.swing.JButton();
        btnCancelarMod = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAdmin = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtPassOld = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNewPass = new javax.swing.JPasswordField();
        btnVerificarPass = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblVer = new javax.swing.JLabel();
        lblOcultar = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("PERFIL DEL ADMINISTRADOR");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel1.setText("Mi Perfil");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/perfil1.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel3.setText("RUC:");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel4.setText("DNI:");

        jLabel5.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel5.setText("CLAVE SOL:");

        jLabel6.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel6.setText("ID:");

        jLabel7.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel7.setText("CELULAR:");

        jLabel10.setBackground(new java.awt.Color(102, 102, 102));
        jLabel10.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N

        txtId.setBackground(new java.awt.Color(255, 255, 255));
        txtId.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtId.setForeground(new java.awt.Color(0, 0, 102));
        txtId.setBorder(null);
        txtId.setEnabled(false);

        txtDni.setBackground(new java.awt.Color(255, 255, 255));
        txtDni.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtDni.setForeground(new java.awt.Color(0, 0, 102));
        txtDni.setBorder(null);
        txtDni.setEnabled(false);
        txtDni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniKeyTyped(evt);
            }
        });

        txtEmail.setBackground(new java.awt.Color(255, 255, 255));
        txtEmail.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(0, 0, 102));
        txtEmail.setBorder(null);
        txtEmail.setEnabled(false);

        txtApeNom.setBackground(new java.awt.Color(255, 255, 255));
        txtApeNom.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtApeNom.setForeground(new java.awt.Color(0, 0, 102));
        txtApeNom.setBorder(null);
        txtApeNom.setEnabled(false);
        txtApeNom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApeNomKeyTyped(evt);
            }
        });

        txtRuc.setBackground(new java.awt.Color(255, 255, 255));
        txtRuc.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtRuc.setForeground(new java.awt.Color(0, 0, 102));
        txtRuc.setBorder(null);
        txtRuc.setEnabled(false);
        txtRuc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRucKeyTyped(evt);
            }
        });

        txtSol.setBackground(new java.awt.Color(255, 255, 255));
        txtSol.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtSol.setForeground(new java.awt.Color(0, 0, 102));
        txtSol.setBorder(null);
        txtSol.setEnabled(false);

        txtCelular.setBackground(new java.awt.Color(255, 255, 255));
        txtCelular.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtCelular.setForeground(new java.awt.Color(0, 0, 102));
        txtCelular.setBorder(null);
        txtCelular.setEnabled(false);
        txtCelular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCelularKeyTyped(evt);
            }
        });

        btnModatos.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnModatos.setText("Modificar Datos");
        btnModatos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModatosMouseClicked(evt);
            }
        });
        btnModatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModatosActionPerformed(evt);
            }
        });

        btnGrabarPerfil.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnGrabarPerfil.setText("Grabar");
        btnGrabarPerfil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGrabarPerfil.setEnabled(false);
        btnGrabarPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarPerfilActionPerformed(evt);
            }
        });

        btnCancelarMod.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnCancelarMod.setText("Cancelar");
        btnCancelarMod.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelarMod.setEnabled(false);
        btnCancelarMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarModActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtEmail)
                            .addComponent(txtApeNom)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(59, 59, 59)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtRuc)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDni, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnCancelarMod, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                    .addComponent(btnModatos, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                                .addGap(57, 57, 57)
                                .addComponent(btnGrabarPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7))
                                .addGap(5, 5, 5)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCelular)
                                    .addComponent(txtSol))))))
                .addGap(34, 34, 34))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtApeNom, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSol, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(btnModatos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelarMod)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGrabarPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32))))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/perfil1.png"))); // NOI18N

        tblAdmin.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblAdmin);

        jLabel13.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel13.setText("Administradores");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel13)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel12.setText("Configurar Contraseña");

        txtPassOld.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtPassOld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassOldActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel8.setText("Contraseña Actual:");

        jLabel9.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel9.setText("Nueva Contraseña");

        txtNewPass.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtNewPass.setEnabled(false);
        txtNewPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNewPassActionPerformed(evt);
            }
        });

        btnVerificarPass.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnVerificarPass.setText("Verificar");
        btnVerificarPass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerificarPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarPassActionPerformed(evt);
            }
        });

        btnRegistrar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.setEnabled(false);
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.setEnabled(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        lblVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ojoA.png"))); // NOI18N
        lblVer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblVer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblVerMouseClicked(evt);
            }
        });

        lblOcultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ojoC.png"))); // NOI18N
        lblOcultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblOcultar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOcultarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(23, 334, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPassOld))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(txtNewPass)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnVerificarPass, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(btnCancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblVer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblOcultar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnRegistrar))))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassOld, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(btnVerificarPass))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblVer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(txtNewPass, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblOcultar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnRegistrar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGrabarPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarPerfilActionPerformed
        // TODO add your handling code here:
        String correo = txtEmail.getText().toUpperCase();
        String[] nc = txtApeNom.getText().toUpperCase().split(",");
        String dni = txtDni.getText();
        String ruc = txtRuc.getText();
        String sol = txtSol.getText();
        String cel = txtCelular.getText();
        String id = txtId.getText();
        updateNewInfo(id, correo,  nc[0], nc[1], dni, ruc, sol, cel);
        MostrarAdminTotales();
        habilitar(false);
    }//GEN-LAST:event_btnGrabarPerfilActionPerformed

    private void btnModatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModatosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnModatosActionPerformed

    private void btnModatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModatosMouseClicked
        // TODO add your handling code here:
        habilitar(true);
    }//GEN-LAST:event_btnModatosMouseClicked

    private void btnCancelarModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarModActionPerformed
        // TODO add your handling code here:
        btnModatos.setEnabled(true);
        habilitar(false);
    }//GEN-LAST:event_btnCancelarModActionPerformed

    private void btnVerificarPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarPassActionPerformed
        // TODO add your handling code here:
        String user = txtPassOld.getText();
        String dni = txtDni.getText();
        Verificar(user, dni);
    }//GEN-LAST:event_btnVerificarPassActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        String npass = txtNewPass.getText();
        String opass = txtPassOld.getText();
        if(npass.equals("")){
                 JOptionPane.showMessageDialog(null, "Campo Vacío");
        }else{
                 insertNewPass(npass, opass);
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        txtNewPass.setEnabled(false);
        btnRegistrar.setEnabled(false);
        btnCancelar.setEnabled(false);
        txtPassOld.setEnabled(true);
        btnVerificarPass.setEnabled(true);
        
        txtPassOld.setText("");
        txtNewPass.setText("");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtDniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyTyped
        // TODO add your handling code here:
        //VALIDACIÓN DE CAMPO DNI
        int key4 = evt.getKeyChar();
        boolean numeros = key4 >=48 && key4 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(txtDni.getText().length()>=8){
            evt.consume();
        }
    }//GEN-LAST:event_txtDniKeyTyped

    private void txtCelularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCelularKeyTyped
        // TODO add your handling code here:
        //VALIDACIÓN DE CAMPO CELULAR
        int key5 = evt.getKeyChar();
        boolean numeros = key5 >=48 && key5 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(txtCelular.getText().length()>=9){
            evt.consume();
        }
    }//GEN-LAST:event_txtCelularKeyTyped

    private void txtApeNomKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApeNomKeyTyped
        // TODO add your handling code here:
        //VALIDAR CAMPO  PARA SOLOLETRAS
        char letras = evt.getKeyChar();
        boolean caracteres = letras >=65 && letras <= 90 || letras >=97 && letras <= 122;
        if(!caracteres ){
            evt.consume();
        }
    }//GEN-LAST:event_txtApeNomKeyTyped

    private void txtRucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRucKeyTyped
        // TODO add your handling code here:
        //VALIDACIÓN DE CAMPO RUC
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >=48 && key6 <= 57;
        if(!numeros){
            evt.consume();
        }
        if(txtRuc.getText().length()>=11){
            evt.consume();
        }
    }//GEN-LAST:event_txtRucKeyTyped

    private void lblVerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblVerMouseClicked
        // TODO add your handling code here:
        lblVer.setVisible(false);
        lblOcultar.setVisible(true);
        txtNewPass.setEchoChar('*');
    }//GEN-LAST:event_lblVerMouseClicked

    private void lblOcultarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOcultarMouseClicked
        // TODO add your handling code here:
        lblVer.setVisible(true);
        lblOcultar.setVisible(false);
        txtNewPass.setEchoChar((char) 0);
    }//GEN-LAST:event_lblOcultarMouseClicked

    private void txtPassOldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPassOldActionPerformed
        // TODO add your handling code here:
        String user = txtPassOld.getText();
        String dni = txtDni.getText();
        Verificar(user, dni);
        txtNewPass.requestFocusInWindow();
    }//GEN-LAST:event_txtPassOldActionPerformed

    private void txtNewPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNewPassActionPerformed
        // TODO add your handling code here:
        String npass = txtNewPass.getText();
        String opass = txtPassOld.getText();
        if(npass.equals("")){
                 JOptionPane.showMessageDialog(null, "Campo Vacío");
        }else{
                 insertNewPass(npass, opass);
        }
    }//GEN-LAST:event_txtNewPassActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelarMod;
    private javax.swing.JButton btnGrabarPerfil;
    private javax.swing.JButton btnModatos;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnVerificarPass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblOcultar;
    private javax.swing.JLabel lblVer;
    private javax.swing.JTable tblAdmin;
    protected javax.swing.JTextField txtApeNom;
    protected javax.swing.JTextField txtCelular;
    protected javax.swing.JTextField txtDni;
    protected javax.swing.JTextField txtEmail;
    protected javax.swing.JTextField txtId;
    private javax.swing.JPasswordField txtNewPass;
    private javax.swing.JPasswordField txtPassOld;
    protected javax.swing.JTextField txtRuc;
    protected javax.swing.JTextField txtSol;
    // End of variables declaration//GEN-END:variables
}
