
package internalFrames;

import Clases.ConexionBD;
import Clases.perfilUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class infrmPerfilVendedor extends javax.swing.JInternalFrame {

    //Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
    
    public infrmPerfilVendedor() {
        initComponents();
        MostrarVendTotales();
    }

    //MÉTODO PARA MOSTRAR LA INFORMACIÓN DEL VENDEDOR
    public void mostrarPerfilVend(String pass){
        perfilUsuario info = new perfilUsuario();//Clase usuario
        info.mostrarPerfil(pass);
        txtEmail.setText(info.getCorreo());
        txtNombre.setText(info.getNombres());
        txtApellido.setText(info.getApellidos());
        txtId.setText(info.getId());
        txtDni.setText(info.getDni());
        txtCelular.setText(info.getCelular());
    }
    
    //MÉTODO PARA HABILITAR LOS CAMPOS DEL PERFIL
    public void habilitar(boolean t){
        btnGrabar.setEnabled(t);
        txtEmail.setEnabled(t);
        txtNombre.setEnabled(t);
        txtApellido.setEnabled(t);
        txtDni.setEnabled(t);
        txtCelular.setEnabled(t);
        btnCancelarInfo.setEnabled(t);
        btnModatos.setEnabled(!t);
    }
    
    //MÉTODO PARA VERIFICAR LA CONTRASEÑA QUE ESTA ESCRIBIENDO EN LA CAJA DE TEXTO
    public void Verificar(String pass){
        
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        String login="SELECT * FROM tbl_usuario WHERE user_pass = '"+pass+"'"; 
                //Realiza bùsqueda en la tabla trabajadores
        try{
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("user_pass");
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de Consulta del código de verificación "+e.getMessage());
        }
        
         if(this.txtPassOld.getText().equals(records[0])){
                    JOptionPane.showMessageDialog(null, "Verificación de contraseña exitosa");
                    txtNewPass.setEnabled(true);
                    btnRegistrar.setEnabled(true);
                    btnCancelarPass.setEnabled(true);
                    txtPassOld.setEnabled(false);
                    btnVerificarPass.setEnabled(false);
                }else if(this.txtPassOld.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Campo Vacío");
                }else{
                    JOptionPane.showMessageDialog(null, "Código de verificación incorrecta");
                }
    }
    
    //MOSTRAR INFORMACIÓN DE ADMINISTRADORES EN LA TABLA
    public void MostrarVendTotales(){
        String[] head = {"DNI","VENDEDOR", "CELULAR","CORREO"};
        String[] records = new String[4];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL="SELECT user_dni, CONCAT(user_nom, ' ' ,user_ape) AS vendedor, user_celu, user_email FROM tbl_usuario INNER JOIN tbl_tipo_usuario ON tbl_usuario.tipo_usuario_iden = tbl_tipo_usuario.tipo_usuario_iden WHERE tipo_usuario_desc = 'VENDEDOR'"; //Realiza bùsqueda en la tabla
        try{
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("user_dni");
                records[1]=rs.getString("vendedor");
                records[2]=rs.getString("user_celu");
                records[3]=rs.getString("user_email");                
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblVendedores.setModel(model);
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de bùsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblVendedores.getColumnModel().getColumn(0).setPreferredWidth(15);
        tblVendedores.getColumnModel().getColumn(0).setResizable(true);
        tblVendedores.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblVendedores.getColumnModel().getColumn(1).setResizable(true);
        tblVendedores.getColumnModel().getColumn(2).setPreferredWidth(15);
        tblVendedores.getColumnModel().getColumn(2).setResizable(true);
        tblVendedores.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblVendedores.getColumnModel().getColumn(3).setResizable(true);
        
    }
    
    //CÓDIGO PARA ACTUALIZAR CONTRASEÑA DEL USUARIO VENDEDOR
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
            btnCancelarInfo.setEnabled(false);
            txtPassOld.setEnabled(true);
            btnVerificarPass.setEnabled(true);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha actualizado: "+e.getMessage());
        }
    }
    
    //CÓDIGO PARA ACTUALIZAR INFORMACIÓNDEL USUARIO ADMINISTRADOR
    public void updateNewInfo(String id, String correo, String nombres, String apellidos, String dni, String cel){
        try{
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "UPDATE tbl_usuario SET user_email = '"+correo+"', user_nom = '"+nombres+"', user_ape = '"+apellidos+"', user_dni = '"+dni+"', user_celu = '"+cel+"' WHERE usuario_iden = '"+id+"' AND user_dni = '"+dni+"'";
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
        txtEmail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnModatos = new javax.swing.JButton();
        btnCancelarInfo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        txtDni = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtCelular = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVendedores = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnVerificarPass = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        btnCancelarPass = new javax.swing.JButton();
        lblVer = new javax.swing.JLabel();
        lblOcultar = new javax.swing.JLabel();
        txtPassOld = new javax.swing.JPasswordField();
        txtNewPass = new javax.swing.JPasswordField();

        setClosable(true);
        setMaximizable(true);
        setTitle("PERFIL DEL VENDEDOR");
        setPreferredSize(new java.awt.Dimension(1000, 525));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/perfil1.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel2.setText("Mi Perfil");

        txtEmail.setBackground(new java.awt.Color(255, 255, 255));
        txtEmail.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(0, 0, 102));
        txtEmail.setBorder(null);
        txtEmail.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel5.setText("ID:");

        jLabel6.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel6.setText("DNI:");

        jLabel7.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel7.setText("NOMBRES:");

        jLabel8.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel8.setText("CELULAR:");

        jLabel9.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel9.setText("APELLIDOS:");

        btnModatos.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnModatos.setText("Modificar Datos");
        btnModatos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModatosActionPerformed(evt);
            }
        });

        btnCancelarInfo.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnCancelarInfo.setText("Cancelar");
        btnCancelarInfo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelarInfo.setEnabled(false);
        btnCancelarInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarInfoActionPerformed(evt);
            }
        });

        btnGrabar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnGrabar.setText("Grabar");
        btnGrabar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGrabar.setEnabled(false);
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

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

        txtApellido.setBackground(new java.awt.Color(255, 255, 255));
        txtApellido.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtApellido.setForeground(new java.awt.Color(0, 0, 102));
        txtApellido.setBorder(null);
        txtApellido.setEnabled(false);

        txtNombre.setBackground(new java.awt.Color(255, 255, 255));
        txtNombre.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(0, 0, 102));
        txtNombre.setBorder(null);
        txtNombre.setEnabled(false);

        txtCelular.setBackground(new java.awt.Color(255, 255, 255));
        txtCelular.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtCelular.setForeground(new java.awt.Color(0, 0, 102));
        txtCelular.setBorder(null);
        txtCelular.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnModatos, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelarInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                                    .addComponent(txtNombre)
                                    .addComponent(txtCelular))))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(btnModatos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelarInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/perfil1.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel4.setText("Vendedores");

        tblVendedores.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblVendedores);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 51, 255));
        jLabel10.setText("Configurar Contraseña");

        jLabel11.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel11.setText("Contraseña Actual:");

        jLabel12.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel12.setText("Nueva Contraseña:");

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

        btnCancelarPass.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnCancelarPass.setText("Cancelar");
        btnCancelarPass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelarPass.setEnabled(false);
        btnCancelarPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarPassActionPerformed(evt);
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
                    .addComponent(jLabel10)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPassOld, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(txtNewPass)))
                    .addComponent(btnCancelarPass, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVerificarPass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblVer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOcultar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(btnVerificarPass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassOld, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOcultar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNewPass, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarPass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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

    private void btnCancelarInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarInfoActionPerformed
        // TODO add your handling code here:
        btnModatos.setEnabled(true);
        habilitar(false);
    }//GEN-LAST:event_btnCancelarInfoActionPerformed

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

    private void btnVerificarPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarPassActionPerformed
        // TODO add your handling code here:
        String user = txtPassOld.getText();
        Verificar(user);
    }//GEN-LAST:event_btnVerificarPassActionPerformed

    private void btnModatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModatosActionPerformed
        // TODO add your handling code here:
        habilitar(true);
    }//GEN-LAST:event_btnModatosActionPerformed

    private void btnCancelarPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarPassActionPerformed
        // TODO add your handling code here:
        txtNewPass.setEnabled(false);
        btnRegistrar.setEnabled(false);
        btnCancelarInfo.setEnabled(false);
        txtPassOld.setEnabled(true);
        btnVerificarPass.setEnabled(true);
        
        txtPassOld.setText("");
    }//GEN-LAST:event_btnCancelarPassActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        // TODO add your handling code here:
        String correo = txtEmail.getText().toUpperCase();
        String ape = txtApellido.getText().toUpperCase();
        String nom = txtNombre.getText().toUpperCase();
        String dni = txtDni.getText();
        String cel = txtCelular.getText();
        String id = txtId.getText();
        updateNewInfo(id, correo, nom, ape, dni, cel);
        MostrarVendTotales();
        habilitar(false);
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void lblVerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblVerMouseClicked
        lblVer.setVisible(false);
        lblOcultar.setVisible(true);
        txtNewPass.setEchoChar('*');
    }//GEN-LAST:event_lblVerMouseClicked

    private void lblOcultarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOcultarMouseClicked
        lblVer.setVisible(true);
        lblOcultar.setVisible(false);
        txtNewPass.setEchoChar((char) 0);
    }//GEN-LAST:event_lblOcultarMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelarInfo;
    private javax.swing.JButton btnCancelarPass;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnModatos;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnVerificarPass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JTable tblVendedores;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCelular;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtId;
    private javax.swing.JPasswordField txtNewPass;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JPasswordField txtPassOld;
    // End of variables declaration//GEN-END:variables
}
