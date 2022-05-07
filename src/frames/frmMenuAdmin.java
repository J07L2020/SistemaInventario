package frames;

import Clases.ConexionBD;
import internalFrames.ifrmCompra;
import internalFrames.infrmAjustes;
//import internalFrames.ifrmReport_usuarios;
import internalFrames.infrmModeloPorMarca;
import internalFrames.infrmPerfilAdmin;
import java.sql.Connection;


public class frmMenuAdmin extends javax.swing.JFrame {

    //Declaraciòn de objeto conexion
        ConexionBD con = new ConexionBD();
        Connection c = con.getConexionBD();
    
    public String pass;
    public frmMenuAdmin() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
    }

    public void setPass(String pass){
        this.pass = pass;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpPrincipal = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        mnAdmin = new javax.swing.JMenu();
        jMenu18 = new javax.swing.JMenu();
        mnSalir = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        invGeneral = new javax.swing.JMenu();
        mnProveedor = new javax.swing.JMenu();
        jMenu13 = new javax.swing.JMenu();
        mnModPorMarc = new javax.swing.JMenu();
        mnRepuesto = new javax.swing.JMenu();
        jMenu20 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        mnVentas = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu17 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenu16 = new javax.swing.JMenu();
        jMenu15 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu19 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SISTEMA DE CONTROL DE INVENTARIO Y FACTURACIÓN ELECTRONICA");
        setPreferredSize(new java.awt.Dimension(1250, 770));

        jpPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jpPrincipalLayout = new javax.swing.GroupLayout(jpPrincipal);
        jpPrincipal.setLayout(jpPrincipalLayout);
        jpPrincipalLayout.setHorizontalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1250, Short.MAX_VALUE)
        );
        jpPrincipalLayout.setVerticalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 676, Short.MAX_VALUE)
        );

        jMenu3.setText("SISTEMA");
        jMenu3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        mnAdmin.setText("MiPerfil");
        mnAdmin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnAdminMouseClicked(evt);
            }
        });
        mnAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAdminActionPerformed(evt);
            }
        });
        jMenu3.add(mnAdmin);

        jMenu18.setText("Ajustes");
        jMenu18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu18MouseClicked(evt);
            }
        });
        jMenu3.add(jMenu18);

        mnSalir.setText("Salir");
        mnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnSalirMouseClicked(evt);
            }
        });
        mnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalirActionPerformed(evt);
            }
        });
        jMenu3.add(mnSalir);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("GESTION ADMINISTRATIVA");
        jMenu2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        invGeneral.setText("Inventario General");
        invGeneral.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        mnProveedor.setText("Proveedor");
        mnProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnProveedorMouseClicked(evt);
            }
        });
        mnProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnProveedorActionPerformed(evt);
            }
        });
        invGeneral.add(mnProveedor);

        jMenu13.setText("Marca");
        jMenu13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu13MouseClicked(evt);
            }
        });
        invGeneral.add(jMenu13);

        mnModPorMarc.setText("ModelosPorMarca");
        mnModPorMarc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnModPorMarc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnModPorMarcMouseClicked(evt);
            }
        });
        invGeneral.add(mnModPorMarc);

        mnRepuesto.setText("Repuesto");
        mnRepuesto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnRepuesto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnRepuestoMouseClicked(evt);
            }
        });
        mnRepuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRepuestoActionPerformed(evt);
            }
        });
        invGeneral.add(mnRepuesto);

        jMenu2.add(invGeneral);

        jMenu20.setText("Inventario de Compra");
        jMenu20.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu20MouseClicked(evt);
            }
        });
        jMenu2.add(jMenu20);

        jMenuBar1.add(jMenu2);

        jMenu10.setText("GESTION DE VENTAS");
        jMenu10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        mnVentas.setText("Venta");
        mnVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnVentasMouseClicked(evt);
            }
        });

        jMenu5.setText("Boleta");
        jMenu5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu5MouseClicked(evt);
            }
        });
        mnVentas.add(jMenu5);

        jMenu17.setText("Factura");
        jMenu17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu17MouseClicked(evt);
            }
        });
        mnVentas.add(jMenu17);

        jMenu10.add(mnVentas);

        jMenu7.setText("Cliente");
        jMenu7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu7MouseClicked(evt);
            }
        });
        jMenu10.add(jMenu7);

        jMenuBar1.add(jMenu10);

        jMenu16.setText("GESTION DE REPORTES");
        jMenu16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenu15.setText("R. Administrativo");
        jMenu15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenu1.setText("Clientes");
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu15.add(jMenu1);

        jMenu4.setText("Proveedores");
        jMenu4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu15.add(jMenu4);

        jMenu6.setText("Productos");
        jMenu6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu15.add(jMenu6);

        jMenu16.add(jMenu15);

        jMenu19.setText("Reporte de Compras");
        jMenu19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu16.add(jMenu19);

        jMenu11.setText("Reporte de Ventas");
        jMenu11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu16.add(jMenu11);

        jMenuBar1.add(jMenu16);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpPrincipal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalirActionPerformed
    }//GEN-LAST:event_mnSalirActionPerformed

    private void mnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAdminActionPerformed
    }//GEN-LAST:event_mnAdminActionPerformed

    private void mnSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnSalirMouseClicked
        login nl = new login();
        nl.setVisible(true);
        dispose();
    }//GEN-LAST:event_mnSalirMouseClicked

    private void mnAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnAdminMouseClicked
        jpPrincipal.removeAll();
        infrmPerfilAdmin perAdmin = new infrmPerfilAdmin();
        perAdmin.mostrarPerfilAdmin(pass);
        this.jpPrincipal.add(perAdmin);
        int x = (jpPrincipal.getWidth()/2) - (perAdmin.getWidth()/2);
        int y = (jpPrincipal.getHeight()/2) - (perAdmin.getHeight()/2);
        perAdmin.setLocation(x, y);
        perAdmin.show();
    }//GEN-LAST:event_mnAdminMouseClicked

    private void mnRepuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRepuestoActionPerformed
    }//GEN-LAST:event_mnRepuestoActionPerformed

    private void mnProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnProveedorActionPerformed
    }//GEN-LAST:event_mnProveedorActionPerformed

    private void jMenu13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu13MouseClicked
        frmMarca nmarc = new frmMarca();
        nmarc.setVisible(true);
    }//GEN-LAST:event_jMenu13MouseClicked

    private void mnRepuestoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnRepuestoMouseClicked
        frmRepuestos nrep = new frmRepuestos();
        nrep.setVisible(true);
    }//GEN-LAST:event_mnRepuestoMouseClicked

    private void mnProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnProveedorMouseClicked
        frmProveedores nprov = new frmProveedores();
        nprov.setVisible(true);
    }//GEN-LAST:event_mnProveedorMouseClicked

    private void mnModPorMarcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnModPorMarcMouseClicked
        jpPrincipal.removeAll();
        infrmModeloPorMarca nmpm = new infrmModeloPorMarca();
        this.jpPrincipal.add(nmpm);
        int x = (jpPrincipal.getWidth()/2) - (nmpm.getWidth()/2);
        int y = (jpPrincipal.getHeight()/2) - (nmpm.getHeight()/2);
        nmpm.setLocation(x, y);
        nmpm.show();
    }//GEN-LAST:event_mnModPorMarcMouseClicked

    private void jMenu7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu7MouseClicked
        frmClientes ncli = new frmClientes();
        ncli.setVisible(true);
    }//GEN-LAST:event_jMenu7MouseClicked

    private void mnVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnVentasMouseClicked
    }//GEN-LAST:event_mnVentasMouseClicked

    private void jMenu17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu17MouseClicked
        frmFacturaVentas nvf = new frmFacturaVentas();
        nvf.getInfoUser(pass);
        nvf.setVisible(true);
    }//GEN-LAST:event_jMenu17MouseClicked

    private void jMenu5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseClicked
        frmBoletaVentas nvb = new frmBoletaVentas();
        nvb.getInfoUser(pass);
        nvb.setVisible(true);
    }//GEN-LAST:event_jMenu5MouseClicked

    private void jMenu20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu20MouseClicked
        jpPrincipal.removeAll();
        ifrmCompra ifrmC = new ifrmCompra();
        ifrmC.getInfoUser(pass);
        this.jpPrincipal.add(ifrmC);
        int x = (jpPrincipal.getWidth()/2) - (ifrmC.getWidth()/2);
        int y = (jpPrincipal.getHeight()/2) - (ifrmC.getHeight()/2);
        ifrmC.setLocation(x, y);
        ifrmC.show();
    }//GEN-LAST:event_jMenu20MouseClicked

    private void jMenu18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu18MouseClicked
        jpPrincipal.removeAll();
        infrmAjustes ifrAj = new infrmAjustes();
        this.jpPrincipal.add(ifrAj);
        int x = (jpPrincipal.getWidth()/2) - (ifrAj.getWidth()/2);
        int y = (jpPrincipal.getHeight()/2) - (ifrAj.getHeight()/2);
        ifrAj.setLocation(x, y);
        ifrAj.show();
    }//GEN-LAST:event_jMenu18MouseClicked

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
            java.util.logging.Logger.getLogger(frmMenuAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMenuAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMenuAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMenuAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMenuAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu invGeneral;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu18;
    private javax.swing.JMenu jMenu19;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu20;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jpPrincipal;
    private javax.swing.JMenu mnAdmin;
    private javax.swing.JMenu mnModPorMarc;
    private javax.swing.JMenu mnProveedor;
    private javax.swing.JMenu mnRepuesto;
    private javax.swing.JMenu mnSalir;
    private javax.swing.JMenu mnVentas;
    // End of variables declaration//GEN-END:variables
}
