/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internalFrames;

import Clases.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class infrmAjustes extends javax.swing.JInternalFrame {

    //Declaraciòn de objeto conexion
    ConexionBD con = new ConexionBD();
    Connection c = con.getConexionBD();
    private int idAjuste;
    public infrmAjustes() {
        initComponents();
        mostrarAjustes();
    }

    private void habilitar(boolean t){
        a_txtPGanancia.setEnabled(t);
        a_txtIGV.setEnabled(t);
        a_txtModificar.setEnabled(!t);
        a_txtGrabar.setEnabled(t);
        a_btnCancelar.setEnabled(t);
    }
    
    //REGISTRA LA SERIA Y NÚMERO DE LA BOLETA
    private void actualizarAjustes() {
        try {
            //Prepara sentencia de sql; instrucciòn para alamcenar en la bbdd
            String comandSQL = "update tbl_ajuste set ajus_pGanancia=?, ajus_igv=? WHERE ajuste_iden= '"+idAjuste+"'";
            PreparedStatement pst = c.prepareStatement(comandSQL); //Determina la ecuciòn de la instrucciòn

            pst.setFloat(1, Float.parseFloat(a_txtPGanancia.getText()));
            pst.setDouble(2, Double.parseDouble(a_txtIGV.getText()));

            pst.execute();//ejecutar las instrucciones          
            JOptionPane.showMessageDialog(null, "Registro actualizado correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error, no se ha realizado el registro: " + e.getMessage());
        }
    }
    private void mostrarAjustes(){
        //MÉTODO PARA ALMACENAR LOS AJUSTES GENERALES
        String[] records = new String[3];
        //Modelo para establecer en la tabla
        try {
            String login = "SELECT * FROM tbl_ajuste WHERE ajuste_iden = (SELECT MAX(ajuste_iden) FROM tbl_ajuste)";
            //crea cuando la devuelve resultados o registros
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(login);
            while (rs.next()) { //mientras halla registro y puedo ir al siguiente
                records[0] = rs.getString("ajus_pGanancia");
                records[1] = rs.getString("ajus_igv");
                records[2] = rs.getString("ajuste_iden");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        a_txtPGanancia.setText(records[0]);
        a_txtIGV.setText(records[1]);
        idAjuste = Integer.parseInt(records[2]);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        a_txtPGanancia = new javax.swing.JTextField();
        a_txtModificar = new javax.swing.JButton();
        a_txtGrabar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        a_txtIGV = new javax.swing.JTextField();
        a_btnCancelar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("AJUSTES DE ADMINISTRACIÓN");

        jLabel1.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel1.setText("Porcentaje de Ganancia:");

        a_txtPGanancia.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        a_txtPGanancia.setEnabled(false);
        a_txtPGanancia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                a_txtPGananciaKeyTyped(evt);
            }
        });

        a_txtModificar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        a_txtModificar.setText("Modificar");
        a_txtModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a_txtModificarActionPerformed(evt);
            }
        });

        a_txtGrabar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        a_txtGrabar.setText("Grabar");
        a_txtGrabar.setEnabled(false);
        a_txtGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a_txtGrabarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel2.setText("IGV:");

        a_txtIGV.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        a_txtIGV.setEnabled(false);
        a_txtIGV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                a_txtIGVKeyTyped(evt);
            }
        });

        a_btnCancelar.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        a_btnCancelar.setText("Cancelar");
        a_btnCancelar.setEnabled(false);
        a_btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a_btnCancelarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel3.setText("%");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel4.setText("%");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(a_txtPGanancia)
                    .addComponent(a_txtIGV, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(a_txtModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(a_txtGrabar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(a_btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(a_txtPGanancia, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a_txtModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a_txtIGV, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(a_txtGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(a_btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void a_txtModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a_txtModificarActionPerformed
        habilitar(true);
//        a_btnCancelar.setEnabled(true);
    }//GEN-LAST:event_a_txtModificarActionPerformed

    private void a_txtGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a_txtGrabarActionPerformed
        if(a_txtPGanancia.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Hay campos vacíos");
        }else{
            actualizarAjustes();
            mostrarAjustes();
            a_btnCancelar.setEnabled(true);
            habilitar(false);
        }
    }//GEN-LAST:event_a_txtGrabarActionPerformed

    private void a_txtPGananciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_a_txtPGananciaKeyTyped
        //VALIDACIÓN DE PORCENTAJE DE GANACIA
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >= 48 && key6 <= 57;
        if (!numeros) {
            evt.consume();
        }
    }//GEN-LAST:event_a_txtPGananciaKeyTyped

    private void a_txtIGVKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_a_txtIGVKeyTyped
        int key6 = evt.getKeyChar();
        boolean numeros = key6 >= 48 && key6 <= 57;
        if (!numeros) {
            evt.consume();
        }
    }//GEN-LAST:event_a_txtIGVKeyTyped

    private void a_btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a_btnCancelarActionPerformed
        habilitar(false);
    }//GEN-LAST:event_a_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton a_btnCancelar;
    private javax.swing.JButton a_txtGrabar;
    private javax.swing.JTextField a_txtIGV;
    private javax.swing.JButton a_txtModificar;
    private javax.swing.JTextField a_txtPGanancia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
