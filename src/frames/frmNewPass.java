package frames;

import Clases.ConexionBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class frmNewPass extends javax.swing.JFrame {

 //Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
        
    public frmNewPass() {
        initComponents();
        this.lblVer1.setVisible(false);
        this.lblVer2.setVisible(false);
        this.setLocationRelativeTo(null);
    }
    //MÉTODO PARA VERIFICAR EL CÓDIGO DEL ADMINISTRADOR
    public void VerificarAdmin(String clave){
        
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        String login="SELECT * FROM tbl_usuario WHERE user_cl_sol = '"+clave+"'"; 
                //Realiza bùsqueda en la tabla trabajadores
        try{
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("user_cl_sol");
                records[1]=rs.getString("user_pass");
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de Consulta del código de verificación "+e.getMessage());
        }
        
         if(this.txtCodigoVerificacion.getText().equals(records[0])){
                    JOptionPane.showMessageDialog(null, "Verificación de código exitoso");
                    btnRestablecer.setEnabled(true);
                    txtCodigoVerificacion.setEnabled(false);
                    btnVerificarCodigo.setEnabled(false);
                    btnCan.setEnabled(false);
                    txtNewPass.setText(records[1]);
                }else if(this.txtCodigoVerificacion.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Campo Vacío");
                }else{
                    txtCodigoVerificacion.requestFocusInWindow();
                    JOptionPane.showMessageDialog(null, "Código de verificación incorrecta");
                }
    }
    
     //MÉTODO PARA VERIFICAR EL CÓDIGO DEL VENDEDOR
    public void VerificarVendedor(String dni){
        
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        String login="SELECT * FROM tbl_usuario WHERE user_dni = '"+dni+"'"; 
                //Realiza bùsqueda en la tabla trabajadores
        try{
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement(); 
            ResultSet rs = st.executeQuery(login);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("user_dni");
                records[1]=rs.getString("user_pass");
            } 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de Consulta del código de verificación "+e.getMessage());
        }
        
         if(this.txtCodigoVerificacion.getText().equals(records[0])){
                    JOptionPane.showMessageDialog(null, "Verificación de código exitoso");
                    btnRestablecer.setEnabled(true);
                    txtCodigoVerificacion.setEnabled(false);
                    btnVerificarCodigo.setEnabled(false);
                    btnCan.setEnabled(false);
                    txtNewPass.setText(records[1]);
                }else if(this.txtCodigoVerificacion.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Campo Vacío");
                }else{
                    txtCodigoVerificacion.requestFocusInWindow();
                    JOptionPane.showMessageDialog(null, "Código de verificación incorrecta");
                }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtCodigoVerificacion = new javax.swing.JPasswordField();
        btnVerificarCodigo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtNewPass = new javax.swing.JPasswordField();
        btnRestablecer = new javax.swing.JButton();
        lblVer1 = new javax.swing.JLabel();
        lblOcultar1 = new javax.swing.JLabel();
        lblVer2 = new javax.swing.JLabel();
        lblOcultar2 = new javax.swing.JLabel();
        btnCan = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbxUser = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REESTABLECER CONTRASEÑA");

        jLabel1.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel1.setText("Ingrese Código de verificación:");

        txtCodigoVerificacion.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N

        btnVerificarCodigo.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnVerificarCodigo.setText("Verificar");
        btnVerificarCodigo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerificarCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarCodigoActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel2.setText("Esta es tu contraseña:");

        txtNewPass.setEditable(false);
        txtNewPass.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N

        btnRestablecer.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnRestablecer.setText("Volver interfaz de Inicio de Sesión");
        btnRestablecer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRestablecer.setEnabled(false);
        btnRestablecer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestablecerActionPerformed(evt);
            }
        });

        lblVer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ojoA.png"))); // NOI18N
        lblVer1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblVer1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblVer1MouseClicked(evt);
            }
        });

        lblOcultar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ojoC.png"))); // NOI18N
        lblOcultar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblOcultar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOcultar1MouseClicked(evt);
            }
        });

        lblVer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ojoA.png"))); // NOI18N
        lblVer2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblVer2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblVer2MouseClicked(evt);
            }
        });

        lblOcultar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ojoC.png"))); // NOI18N
        lblOcultar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblOcultar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOcultar2MouseClicked(evt);
            }
        });

        btnCan.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Copia la contraseña para volver e Inciar Sesión");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel4.setText("Tipo de Usuario:");

        cbxUser.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        cbxUser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione el usuario", "Administrador", "Vendedor" }));
        cbxUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnVerificarCodigo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCan)
                        .addGap(24, 24, 24))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(btnRestablecer, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbxUser, 0, 221, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1)
                                    .addComponent(txtCodigoVerificacion)
                                    .addComponent(txtNewPass, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblVer1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblOcultar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblVer2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblOcultar2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(24, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbxUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVer1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigoVerificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOcultar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVerificarCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addGap(33, 33, 33)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOcultar2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNewPass, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVer2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(26, 26, 26)
                .addComponent(btnRestablecer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCanActionPerformed

    private void lblVer1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblVer1MouseClicked
        // TODO add your handling code here:
        lblVer1.setVisible(false);
        lblOcultar1.setVisible(true);
        txtCodigoVerificacion.setEchoChar('*');
    }//GEN-LAST:event_lblVer1MouseClicked

    private void lblOcultar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOcultar1MouseClicked
        // TODO add your handling code here:
        lblVer1.setVisible(true);
        lblOcultar1.setVisible(false);
        txtCodigoVerificacion.setEchoChar((char) 0);
    }//GEN-LAST:event_lblOcultar1MouseClicked

    private void btnVerificarCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarCodigoActionPerformed
        // TODO add your handling code here:
        String user = txtCodigoVerificacion.getText();
        if(cbxUser.getSelectedItem().equals("Administrador")){
            VerificarAdmin(user);
        }else if(cbxUser.getSelectedItem().equals("Vendedor")){
            VerificarVendedor(user);
        }else{
            JOptionPane.showMessageDialog(null, "No seleccionó el usuario");
        }
    }//GEN-LAST:event_btnVerificarCodigoActionPerformed

    private void btnRestablecerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestablecerActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnRestablecerActionPerformed

    private void lblVer2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblVer2MouseClicked
        // TODO add your handling code here:
        lblVer2.setVisible(false);
        lblOcultar2.setVisible(true);
        txtNewPass.setEchoChar('*');
    }//GEN-LAST:event_lblVer2MouseClicked

    private void lblOcultar2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOcultar2MouseClicked
        // TODO add your handling code here:
        lblVer2.setVisible(true);
        lblOcultar2.setVisible(false);
        txtNewPass.setEchoChar((char) 0);
    }//GEN-LAST:event_lblOcultar2MouseClicked

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
            java.util.logging.Logger.getLogger(frmNewPass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmNewPass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmNewPass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmNewPass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmNewPass().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCan;
    private javax.swing.JButton btnRestablecer;
    private javax.swing.JButton btnVerificarCodigo;
    private javax.swing.JComboBox<String> cbxUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblOcultar1;
    private javax.swing.JLabel lblOcultar2;
    private javax.swing.JLabel lblVer1;
    private javax.swing.JLabel lblVer2;
    private javax.swing.JPasswordField txtCodigoVerificacion;
    private javax.swing.JPasswordField txtNewPass;
    // End of variables declaration//GEN-END:variables
}
