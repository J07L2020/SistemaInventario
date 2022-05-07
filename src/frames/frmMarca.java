
package frames;

import Clases.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmMarca extends javax.swing.JFrame {

    //Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
        
        private boolean swap = false;

    public frmMarca() {
        initComponents();
        this.setLocationRelativeTo(null);
        MostrarMarcas();
    }

    private void limpiar(){
        txtId.setText("");
        txtMarca.setText("");
    }
    
    private void habilitar(boolean t){
        txtMarca.setEnabled(t);
        
        //Botones
        btnGrabarMarca.setEnabled(t);
        btnCancelarMarca.setEnabled(t);
        btnAgregarMarca.setEnabled(!t);
        btnEliminarMarca.setEnabled(t);
    }
    
    //MÉTODO PARA INSERTAR MARCA
    private void insertBrands(){
        try{
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into tbl_marca (marc_desc) value (?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, txtMarca.getText().toUpperCase());
            
            pst.execute();//ejecutar las instrucciones          
            JOptionPane.showMessageDialog(null, "Registro incertado con éxito");         
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: "+e.getMessage());
        }
    }
    
    //MÉTODO PARA ACTUALIZAR MARCA
    private void updateBrands(){
        try{
            //Prepara sentencia de sql; instrucciòn para actualizar en la bbdd
            String comandSQL = "update tbl_marca set marc_desc=? where marca_iden=?";
            
            int SelectionFile = tblMarcas.getSelectedRow();
            String idal = (String) tblMarcas.getValueAt(SelectionFile, 0);//casting
            
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
            
            pst.setString(1, txtMarca.getText().toUpperCase());
            pst.setString(2, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Registro actualizado correctamente");          
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado la actualizaciòn: "+e.getMessage());
        }
    }
    
    //MÉTODO PARA MOSTRAR INFORMACIÓN DE LAS MARCAS EN TABLA
    public void MostrarMarcas(){
        String[] head = {"ID","MARCA"};
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL="SELECT * FROM tbl_marca"; //Realiza bùsqueda en la tabla
        try{
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("marca_iden");
                records[1]=rs.getString("marc_desc");
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblMarcas.setModel(model);
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de bùsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblMarcas.getColumnModel().getColumn(0).setPreferredWidth(15);
        tblMarcas.getColumnModel().getColumn(0).setResizable(true);
        tblMarcas.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblMarcas.getColumnModel().getColumn(1).setResizable(true);
        
    }
    
    //MÉTODO IMPRIMIR DESDE LA TABLA A LOS CAMPOS
    private void printTable(){
        PreparedStatement ps = null;
        ResultSet rs =null;
        try{
            
            int Fila = tblMarcas.getSelectedRow();
            String codigo = tblMarcas.getValueAt(Fila, 0).toString();
            
            ps = c.prepareStatement("SELECT * FROM tbl_marca WHERE marca_iden=?");
            ps.setString(1, codigo);
            rs = ps.executeQuery();
            
            while (rs.next()){
                txtId.setText(rs.getString("marca_iden"));
                txtMarca.setText(rs.getString("marc_desc"));
            }
            
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
    
    //MÉTODO PARA ELIMINAR MARCAS
    public void DeleteBrands() {
        try {
            //Campos a actualizar
            String comandSQL = "delete from marc_desc where marca_iden=?";
            int SelectionFile = tblMarcas.getSelectedRow();
            String idal = (String) tblMarcas.getValueAt(SelectionFile, 0);//casting
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
            pst.setString(1, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Se ha eliminado el registro con èxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha eliminado el registro: " + e.getMessage());
        }
    }    
    
    //MÉTODO PARA BUSCAR REGISTROS DE LAS MARCAS
    private void BuscarMarcas(){
        String Buscar = txtBuscar.getText();
        String[] head = {"ID","MARCA"};
        String[] records = new String[2];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL="SELECT * FROM tbl_marca WHERE marc_desc LIKE '%"+Buscar+"%'"; //Realiza bùsqueda en la tabla
        try{
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("marca_iden");
                records[1]=rs.getString("marc_desc");
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblMarcas.setModel(model);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblMarcas.getColumnModel().getColumn(0).setPreferredWidth(15);
        tblMarcas.getColumnModel().getColumn(0).setResizable(true);
        tblMarcas.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblMarcas.getColumnModel().getColumn(1).setResizable(true);
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblMarcas = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        btnAgregarMarca = new javax.swing.JButton();
        btnGrabarMarca = new javax.swing.JButton();
        btnCancelarMarca = new javax.swing.JButton();
        btnEliminarMarca = new javax.swing.JButton();
        btnSalirMarca = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REGISTRO DE MARCAS O MODELOS DE AUTOPARTES");
        setPreferredSize(new java.awt.Dimension(855, 415));
        setSize(new java.awt.Dimension(855, 415));

        tblMarcas.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        tblMarcas.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMarcas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMarcasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMarcas);

        jLabel1.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel1.setText("BUSCAR:");

        txtBuscar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 204));
        jLabel2.setText("GESTIONAR INFORMACIÓN DE MARCAS");

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel3.setText("ID:");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel4.setText("MARCA:");

        txtId.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtId.setEnabled(false);

        txtMarca.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtMarca.setEnabled(false);
        txtMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMarcaActionPerformed(evt);
            }
        });

        btnAgregarMarca.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnAgregarMarca.setText("AGREGAR");
        btnAgregarMarca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarMarcaActionPerformed(evt);
            }
        });

        btnGrabarMarca.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnGrabarMarca.setText("GRABAR");
        btnGrabarMarca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGrabarMarca.setEnabled(false);
        btnGrabarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarMarcaActionPerformed(evt);
            }
        });

        btnCancelarMarca.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnCancelarMarca.setText("CANCELAR");
        btnCancelarMarca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelarMarca.setEnabled(false);
        btnCancelarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarMarcaActionPerformed(evt);
            }
        });

        btnEliminarMarca.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnEliminarMarca.setText("ELIMINAR");
        btnEliminarMarca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarMarca.setEnabled(false);
        btnEliminarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarMarcaActionPerformed(evt);
            }
        });

        btnSalirMarca.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnSalirMarca.setText("SALIR");
        btnSalirMarca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalirMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirMarcaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnSalirMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnAgregarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCancelarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEliminarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                            .addComponent(btnGrabarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(33, 33, 33)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgregarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGrabarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancelarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(btnSalirMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMarcaActionPerformed
        // TODO add your handling code here:
        habilitar(true);
        swap = true;
        limpiar();
        txtMarca.requestFocusInWindow();
    }//GEN-LAST:event_btnAgregarMarcaActionPerformed

    private void btnCancelarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarMarcaActionPerformed
        // TODO add your handling code here:
        habilitar(false);
    }//GEN-LAST:event_btnCancelarMarcaActionPerformed

    private void tblMarcasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMarcasMouseClicked
        // TODO add your handling code here:
        printTable();
        habilitar(true);
        swap = false;
    }//GEN-LAST:event_tblMarcasMouseClicked

    private void btnSalirMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirMarcaActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnSalirMarcaActionPerformed

    private void btnGrabarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarMarcaActionPerformed
        // TODO add your handling code here:
        //Mensaje previo de anticipo para que se ejecute cada decision del crud
        if(txtMarca.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
        }else{
            int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un nuevo registro?");
            if(m==0){
                if(swap){
                    insertBrands();
                }else{
                    updateBrands();
                }
            }
            habilitar(false);//cuando quiero deshabilitar los textos
            MostrarMarcas();
        }
    }//GEN-LAST:event_btnGrabarMarcaActionPerformed

    private void btnEliminarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarMarcaActionPerformed
        // TODO add your handling code here:
        int fsel = tblMarcas.getSelectedRow();
            if(fsel == -1){
                JOptionPane.showMessageDialog(null, "Debe seleccionar una fila");
            }else{
                int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar la marca?");
                if(m==0){
                    DeleteBrands();
                }
                limpiar();
            }
            habilitar(false);
            MostrarMarcas();
        
    }//GEN-LAST:event_btnEliminarMarcaActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        // TODO add your handling code here:
        BuscarMarcas();
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void txtMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMarcaActionPerformed
        if(txtMarca.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
        }else{
            int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un nuevo registro?");
            if(m==0){
                if(swap){
                    insertBrands();
                }else{
                    updateBrands();
                }
            }
            habilitar(false);//cuando quiero deshabilitar los textos
            MostrarMarcas();
        }
    }//GEN-LAST:event_txtMarcaActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
    }//GEN-LAST:event_txtBuscarActionPerformed

    
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
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMarca().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarMarca;
    private javax.swing.JButton btnCancelarMarca;
    private javax.swing.JButton btnEliminarMarca;
    private javax.swing.JButton btnGrabarMarca;
    private javax.swing.JButton btnSalirMarca;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMarcas;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtMarca;
    // End of variables declaration//GEN-END:variables
}
