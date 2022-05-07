
package internalFrames;

import Clases.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class infrmModeloPorMarca extends javax.swing.JInternalFrame {

    //Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
        
        private boolean swap = false;
    
    public infrmModeloPorMarca() {
        initComponents();
        
        MostrarModelosMarcas();
        mostrarMarcas();
    }
    
    private void limpiar(){
        cbxMarcaM.addItem("Seleccione la marca");
        txtModelo.setText("");
    }
    
    private void habilitar(boolean t){
        cbxMarcaM.setEnabled(t);
        txtModelo.setEnabled(t);
        
        //Botones
        btnGrabarM.setEnabled(t);
        btnCancelarM.setEnabled(t);
        btnAgregarM.setEnabled(!t);
        btnEliminarM.setEnabled(t);
    }
    
    //MÉTODO PARA MOSTRAR INFORMACIÓN DE LAS MARCAS EN TABLA
    public void MostrarModelosMarcas(){
        String[] head = {"ID", "MODELO", "MARCA"};
        String[] records = new String[3];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL="SELECT * FROM tbl_modelo INNER JOIN tbl_marca ON tbl_modelo.marca_iden = tbl_marca.marca_iden"; //Realiza bùsqueda en la tabla
        try{
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("modelo_iden");
                records[1]=rs.getString("mod_desc");
                records[2]=rs.getString("marc_desc");
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblModeloPorM.setModel(model);
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblModeloPorM.getColumnModel().getColumn(0).setPreferredWidth(5);
        tblModeloPorM.getColumnModel().getColumn(0).setResizable(true);
        tblModeloPorM.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblModeloPorM.getColumnModel().getColumn(1).setResizable(true);
        tblModeloPorM.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblModeloPorM.getColumnModel().getColumn(2).setResizable(true); 
    }
    
    //MÉTODO PARA MOSTRAR LAS MARCAS EN EL COMBO BOX
    private void mostrarMarcas(){    
        try{
            String sql="SELECT * FROM tbl_marca";
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(sql);
            cbxMarcaM.addItem("Seleccione la marca");
            while(rs.next()){
                cbxMarcaM.addItem(rs.getString("marc_desc"));
            }
            rs.close();
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
    
    //MÉTODO PARA INSERTAR MODELOS
    private void insertModels(){
        try{
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "Insert Into tbl_modelo (mod_desc, marca_iden) value (?,?)";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setString(1, txtModelo.getText().toUpperCase());
            int itemSel = cbxMarcaM.getSelectedIndex(); //identificar el elemento seleccionado
            String marca = cbxMarcaM.getItemAt(itemSel).toUpperCase();
            
            String codsql = "SELECT * FROM tbl_marca WHERE marc_desc = '"+marca+"'";
            Statement st = c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(codsql);
            int idm = 0;
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                idm = rs.getInt("marca_iden");
            }
            
           pst.setInt(2, idm);//Obtener el nombre del item seleccionado 
            pst.execute();//ejecutar las instrucciones          
            JOptionPane.showMessageDialog(null, "Registro incertado con éxito");         
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: "+e.getMessage());
        }
    }
    
    //MÉTODO PARA ACTUALIZAR MARCA
    private void updateModels(){
        try{
            //Prepara sentencia de sql; instrucciòn para actualizar en la bbdd
            String comandSQL = "update tbl_modelo set mod_desc=?, marca_iden=? where modelo_iden=?";
            
            int SelectionFile = tblModeloPorM.getSelectedRow();
            String idal = (String) tblModeloPorM.getValueAt(SelectionFile, 0);//casting  
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
            
            pst.setString(1, txtModelo.getText().toUpperCase());
            int itemSel = cbxMarcaM.getSelectedIndex(); //identificar el elemento seleccionado
            String marca = cbxMarcaM.getItemAt(itemSel).toUpperCase();
            
            String codsql = "SELECT * FROM tbl_marca WHERE marc_desc = '"+marca+"'";
            Statement st = c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(codsql);
            int idm = 0;
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                idm = rs.getInt("marca_iden");
            }   
           pst.setInt(2, idm);//Obtener el nombre del item seleccionado 
           
            pst.setString(3, idal);
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
            
            int Fila = tblModeloPorM.getSelectedRow();
            String codigo = tblModeloPorM.getValueAt(Fila, 0).toString();
            
            ps = c.prepareStatement("SELECT * FROM tbl_modelo INNER JOIN tbl_marca ON tbl_modelo.marca_iden = tbl_marca.marca_iden WHERE tbl_modelo.modelo_iden=?");
            ps.setString(1, codigo);
            rs = ps.executeQuery();
            
            while (rs.next()){
                txtId.setText(rs.getString("tbl_modelo.modelo_iden"));
                txtModelo.setText(rs.getString("mod_desc"));
                cbxMarcaM.setSelectedItem(rs.getString("marc_desc"));
            }
            
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
    
    //MÉTODO PARA ELIMINAR MARCAS
    public void DeleteBrands() {
        try {
            //Campos a actualizar
            String comandSQL = "delete from tbl_modelo where modelo_iden=?";
            int SelectionFile = tblModeloPorM.getSelectedRow();
            String idal = (String) tblModeloPorM.getValueAt(SelectionFile, 0);//casting
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn
            pst.setString(1, idal);
            pst.execute();//ejecutar las instrucciones
            JOptionPane.showMessageDialog(null, "Se ha eliminado el registro con èxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha eliminado el registro: " + e.getMessage());
        }
    }    
    
    //MÉTODO PARA BUSCAR REGISTROS DE LAS MARCAS
    private void BuscarModelos(){
        String Buscar = txtBuscarM.getText();
        String[] head = {"ID", "MODELO", "MARCA"};
        String[] records = new String[3];
        //Modelo para establecer en la tabla
        DefaultTableModel model = new DefaultTableModel(null, head);
        //Instruccion utilizando concat_ws para realizar la busqueda
        String comandSQL="SELECT * FROM tbl_modelo INNER JOIN tbl_marca ON tbl_modelo.marca_iden = tbl_marca.marca_iden WHERE mod_desc LIKE '%"+Buscar+"%' OR marc_desc LIKE '%"+Buscar+"%'"; //Realiza bùsqueda en la tabla
        try{
            Statement st = (Statement) c.createStatement(); //crea cuando la devuelve resultados o registros
            ResultSet rs = st.executeQuery(comandSQL);
            
            while (rs.next()){ //mientras halla registro y puedo ir al siguiente
                records[0]=rs.getString("tbl_modelo.modelo_iden");
                records[1]=rs.getString("mod_desc");
                records[2]=rs.getString("marc_desc");
                //el orden no necesariamente de como este en la bbbdd
                //Agrega a las tablas
                model.addRow(records);
            }
            //Se obtiene de la tabla y se va modificando
            tblModeloPorM.setModel(model);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de búsqueda "+e.getMessage());
        }
        
        //REDIMENSIONAR LAS COLUMNAS
        tblModeloPorM.getColumnModel().getColumn(0).setPreferredWidth(5);
        tblModeloPorM.getColumnModel().getColumn(0).setResizable(true);
        tblModeloPorM.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblModeloPorM.getColumnModel().getColumn(1).setResizable(true);
        tblModeloPorM.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblModeloPorM.getColumnModel().getColumn(2).setResizable(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtBuscarM = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblModeloPorM = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        cbxMarcaM = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        btnAgregarM = new javax.swing.JButton();
        btnGrabarM = new javax.swing.JButton();
        btnCancelarM = new javax.swing.JButton();
        btnEliminarM = new javax.swing.JButton();
        btnSalirM = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("REGISTRO DE MODELOS POR MARCA");
        setPreferredSize(new java.awt.Dimension(917, 506));

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("GESTIONAR INFORMACIÓN DE MODELOS POR MARCA");

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel2.setText("BUSCAR:");

        txtBuscarM.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtBuscarM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarMActionPerformed(evt);
            }
        });
        txtBuscarM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarMKeyReleased(evt);
            }
        });

        tblModeloPorM.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        tblModeloPorM.setModel(new javax.swing.table.DefaultTableModel(
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
        tblModeloPorM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblModeloPorMMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblModeloPorM);

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel3.setText("MARCA:");

        cbxMarcaM.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        cbxMarcaM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbxMarcaM.setEnabled(false);
        cbxMarcaM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMarcaMActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel4.setText("MODELO O PRESENTACIÓN:");

        txtModelo.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtModelo.setEnabled(false);
        txtModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModeloActionPerformed(evt);
            }
        });

        btnAgregarM.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnAgregarM.setText("AGREGAR");
        btnAgregarM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarMActionPerformed(evt);
            }
        });

        btnGrabarM.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnGrabarM.setText("GRABAR");
        btnGrabarM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGrabarM.setEnabled(false);
        btnGrabarM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarMActionPerformed(evt);
            }
        });

        btnCancelarM.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnCancelarM.setText("CANCELAR");
        btnCancelarM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelarM.setEnabled(false);
        btnCancelarM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarMActionPerformed(evt);
            }
        });

        btnEliminarM.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnEliminarM.setText("ELIMINAR");
        btnEliminarM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarM.setEnabled(false);
        btnEliminarM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarMActionPerformed(evt);
            }
        });

        btnSalirM.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnSalirM.setText("SALIR");
        btnSalirM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalirM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirMActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel5.setText("ID:");

        txtId.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtId.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4)
                        .addComponent(txtModelo)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnCancelarM, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                .addComponent(btnAgregarM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnGrabarM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEliminarM, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)))
                        .addComponent(btnSalirM, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cbxMarcaM, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE)))))
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarM, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBuscarM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cbxMarcaM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgregarM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGrabarM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancelarM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminarM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addComponent(btnSalirM, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirMActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnSalirMActionPerformed

    private void btnGrabarMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarMActionPerformed
        // TODO add your handling code here:
        //Mensaje previo de anticipo para que se ejecute cada decision del crud
        if(txtModelo.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
        }else{
            int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un nuevo registro?");
            if(m==0){
                if(swap){
                    insertModels();
                }else{
                    updateModels();
                }
                habilitar(false);//cuando quiero deshabilitar los textos
                limpiar();
                MostrarModelosMarcas();
            }
        }
    }//GEN-LAST:event_btnGrabarMActionPerformed

    private void tblModeloPorMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblModeloPorMMouseClicked
        // TODO add your handling code here:
        printTable();
        habilitar(true);
        swap = false;
    }//GEN-LAST:event_tblModeloPorMMouseClicked

    private void btnEliminarMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarMActionPerformed
        // TODO add your handling code here:
        int fsel = tblModeloPorM.getSelectedRow();
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
                MostrarModelosMarcas();
    }//GEN-LAST:event_btnEliminarMActionPerformed

    private void btnCancelarMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarMActionPerformed
        // TODO add your handling code here:
        habilitar(false);
    }//GEN-LAST:event_btnCancelarMActionPerformed

    private void btnAgregarMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMActionPerformed
        // TODO add your handling code here:
        habilitar(true);
        swap = true;
        txtModelo.requestFocusInWindow();
    }//GEN-LAST:event_btnAgregarMActionPerformed

    private void txtBuscarMKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarMKeyReleased
        // TODO add your handling code here:
        if(txtBuscarM.getText().equals("")){
            MostrarModelosMarcas();
        }else{
            BuscarModelos();
        }
    }//GEN-LAST:event_txtBuscarMKeyReleased

    private void txtModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModeloActionPerformed
        if(txtModelo.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
        }else{
            int m = JOptionPane.showConfirmDialog(this, "¿Estas seguro de grabar un nuevo registro?");
            if(m==0){
                if(swap){
                    insertModels();
                }else{
                    updateModels();
                }
                habilitar(false);//cuando quiero deshabilitar los textos
                limpiar();
                MostrarModelosMarcas();
            }
        }
    }//GEN-LAST:event_txtModeloActionPerformed

    private void txtBuscarMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarMActionPerformed
        txtModelo.requestFocusInWindow();
    }//GEN-LAST:event_txtBuscarMActionPerformed

    private void cbxMarcaMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMarcaMActionPerformed
        txtModelo.requestFocusInWindow();
    }//GEN-LAST:event_cbxMarcaMActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarM;
    private javax.swing.JButton btnCancelarM;
    private javax.swing.JButton btnEliminarM;
    private javax.swing.JButton btnGrabarM;
    private javax.swing.JButton btnSalirM;
    private javax.swing.JComboBox<String> cbxMarcaM;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblModeloPorM;
    private javax.swing.JTextField txtBuscarM;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtModelo;
    // End of variables declaration//GEN-END:variables
}
