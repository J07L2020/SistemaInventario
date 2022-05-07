package restAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.swing.JOptionPane;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Post {

    private boolean responseToF;
    
//  RUTA para enviar documentos
    private String RUTA = "https://gsoft.factmype.com/api/documents";
    
//  TOKEN para enviar documentos    
    private String TOKEN = "0hM7vKBOnMLNzsidFkxpSHzOqbae7CLcVVuF8HKHzEGt8kvLW2";

    public boolean isResponseToF() {
        return responseToF;
    }

    public void setResponseToF(boolean responseToF) {
        this.responseToF = responseToF;
    }
    
    public void apiConsume(String c1,String c2,String c3,String c4,String c5,String c6,String c7,String cl1,String cl2,String cl3,String cl4,double ct1,double ct2,double ct3,double ct4,double ct5,Object[] itm1,Object[] itm2,Object[] itm3,Object[] itm4,Object[] itm5,Object[] itm6,Object[] itm7,Object[] itm8,Object[] itm9,Object[] itm10) {
        
        HttpClient cliente = new DefaultHttpClient();
        HttpPost post = new HttpPost(RUTA);
        post.addHeader("Authorization", "Token token=" + TOKEN); // Cabecera del token
        post.addHeader("Content-Type","application/json"); // Cabecera del Content-Type
        
        try {
            JSONObject objetoCabecera = new JSONObject(); // Instancear el  segundario
            objetoCabecera.put("serie_documento", c1);
            objetoCabecera.put("numero_documento", c2);
            objetoCabecera.put("fecha_de_emision", c3);
            objetoCabecera.put("hora_de_emision", c4);
            objetoCabecera.put("codigo_tipo_operacion", "0101");
            objetoCabecera.put("codigo_tipo_documento", c5);
            objetoCabecera.put("codigo_tipo_moneda", c6);
            objetoCabecera.put("fecha_de_vencimiento", c7);
            objetoCabecera.put("numero_orden_de_compra", "");

            JSONObject objetoCabeceraCliente = new JSONObject();

            objetoCabeceraCliente.put("codigo_tipo_documento_identidad", cl1);
            objetoCabeceraCliente.put("numero_documento", cl2);
            objetoCabeceraCliente.put("apellidos_y_nombres_o_razon_social", cl3);
            objetoCabeceraCliente.put("codigo_pais", "PE");
            objetoCabeceraCliente.put("ubigeo", "");
            objetoCabeceraCliente.put("direccion", cl4);
            objetoCabeceraCliente.put("correo_electronico", "");
            objetoCabeceraCliente.put("telefono", "");

            JSONObject objetoCabeceraVenta = new JSONObject();

            objetoCabeceraVenta.put("total_exportacion", 0.00);
            objetoCabeceraVenta.put("total_operaciones_gravadas", ct1);
            objetoCabeceraVenta.put("total_operaciones_inafectas", 0.00);
            objetoCabeceraVenta.put("total_operaciones_exoneradas", 0.00);
            objetoCabeceraVenta.put("total_operaciones_gratuitas", 0.00);
            objetoCabeceraVenta.put("total_igv", ct2);
            objetoCabeceraVenta.put("total_impuestos", ct3);
            objetoCabeceraVenta.put("total_valor", ct4);
            objetoCabeceraVenta.put("total_venta", ct5);

            JSONArray lista = new JSONArray();
            for (int i = 0; i < itm1.length; i++) {

                JSONObject detalle_linea_1 = new JSONObject();

                detalle_linea_1.put("codigo_interno", "");
                detalle_linea_1.put("descripcion", String.valueOf(itm1[i]));
                detalle_linea_1.put("codigo_producto_sunat", "");
                detalle_linea_1.put("unidad_de_medida", "NIU");
                detalle_linea_1.put("cantidad", Integer.parseInt(String.valueOf(itm2[i])));
                detalle_linea_1.put("valor_unitario", Double.parseDouble(String.valueOf(itm3[i])));
                detalle_linea_1.put("codigo_tipo_precio", "01");
                detalle_linea_1.put("precio_unitario", Double.parseDouble(String.valueOf(itm4[i])));
                detalle_linea_1.put("codigo_tipo_afectacion_igv", "10");
                detalle_linea_1.put("total_base_igv", Double.parseDouble(String.valueOf(itm5[i])));
                detalle_linea_1.put("porcentaje_igv", Double.parseDouble(String.valueOf(itm6[i])));
                detalle_linea_1.put("total_igv", Double.parseDouble(String.valueOf(itm7[i])));
                detalle_linea_1.put("total_impuestos", Double.parseDouble(String.valueOf(itm8[i])));
                detalle_linea_1.put("total_valor_item", Double.parseDouble(String.valueOf(itm9[i])));
                detalle_linea_1.put("total_item", Double.parseDouble(String.valueOf(itm10[i])));

                lista.add(detalle_linea_1);
            }

            objetoCabecera.put("datos_del_cliente_o_receptor", objetoCabeceraCliente);
            objetoCabecera.put("totales", objetoCabeceraVenta);
            objetoCabecera.put("items", lista);
            objetoCabecera.put("informacion_adicional", "");

            StringEntity parametros = new StringEntity(objetoCabecera.toString() ,"UTF-8");
            System.out.println(objetoCabecera.toString());
            post.setEntity(parametros);
            HttpResponse response = cliente.execute(post);     
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));           
            String linea = "";
            if((linea = rd.readLine()) != null) {
                
                JSONParser parsearRsptaJson = new JSONParser();
                JSONObject json_rspta = (JSONObject) parsearRsptaJson.parse(linea);
              
                if(json_rspta.get("errors") != null ){
                    responseToF = false;
                    System.out.println("Error => " + json_rspta.get("errors"));
                }else{
                    
                    responseToF = true;
                    
                    JSONParser parsearRsptaDetalleOK = new JSONParser();
                    JSONObject json_rspta_ok = (JSONObject) parsearRsptaDetalleOK.parse(json_rspta.get("invoice").toString());
                    
                    System.out.println(json_rspta_ok.get("codigo_tipo_documento"));
                    System.out.println(json_rspta_ok.get("serie_documento"));
                    System.out.println(json_rspta_ok.get("numero_documento"));
                    System.out.println(json_rspta_ok.get("enlace"));
                    System.out.println(json_rspta_ok.get("aceptada_por_sunat"));
                    System.out.println(json_rspta_ok.get("sunat_description"));
                    System.out.println(json_rspta_ok.get("sunat_note"));
                    System.out.println(json_rspta_ok.get("sunat_responsecode"));
                    System.out.println(json_rspta_ok.get("sunat_soap_error"));
                    System.out.println(json_rspta_ok.get("pdf_zip_base64"));
                    System.out.println(json_rspta_ok.get("xml_zip_base64"));
                    System.out.println(json_rspta_ok.get("cdr_zip_base64"));
                    System.out.println(json_rspta_ok.get("cadena_para_codigo_qr"));
                    System.out.println(json_rspta_ok.get("codigo_hash"));
                    
//                    objetoCabecera.put("datos_del_cliente_o_receptor",objetoCabeceraCliente);
                    
                } 
            }  

        } catch (Exception e) {
            System.err.println("Exepction: " + e.getMessage());
        }
    }
}
