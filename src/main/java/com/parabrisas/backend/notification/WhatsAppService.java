package com.parabrisas.backend.notification;

import com.parabrisas.backend.producto.Producto;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsAppService {

    private static final Logger logger = LoggerFactory.getLogger(WhatsAppService.class);

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Value("${twilio.destination.number:whatsapp:+51000000000}")
    private String destinationNumber;

    /**
     * Envía alerta de stock bajo para un producto específico
     */
    public void enviarAlertaStockBajo(Producto producto) {
        try {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("⚠️ *ALERTA DE STOCK BAJO* ⚠️\n\n");
            mensaje.append("El siguiente producto ha llegado a stock crítico:\n\n");
            mensaje.append("📦 *Producto:*\n");
            mensaje.append("- Marca: ").append(producto.getMarcaVehiculo()).append("\n");
            mensaje.append("- Modelo: ").append(producto.getModeloVehiculo()).append("\n");
            mensaje.append("- Año: ").append(producto.getAnioVehiculo()).append("\n");
            mensaje.append("- Tipo: ").append(producto.getTipoVidrio()).append("\n");
            mensaje.append("- Calidad: ").append(producto.getCalidadVidrio()).append("\n");
            mensaje.append("- Proveedor: ").append(producto.getProveedor().getNombreProveedor()).append("\n");
            mensaje.append("\n📊 *Stock Actual: ").append(producto.getStockActual()).append("*\n");
            mensaje.append("🏪 Ubicación: ").append(producto.getUbicacionAlmacen() != null ? producto.getUbicacionAlmacen() : "No especificada").append("\n");
            mensaje.append("\n⚡ Es necesario reabastecer este producto.");

            enviarMensaje(mensaje.toString());
            logger.info("Alerta de stock bajo enviada para producto ID: {}", producto.getIdProducto());
        } catch (Exception e) {
            logger.error("Error al enviar alerta de stock bajo: {}", e.getMessage());
        }
    }

    /**
     * Envía alerta cuando la suma de productos similares llega a 1 o 0
     */
    public void enviarAlertaStockCriticoGrupo(List<Producto> productosSimilares, int stockTotal) {
        try {
            if (productosSimilares.isEmpty()) {
                return;
            }

            Producto primerProducto = productosSimilares.get(0);
            
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("🚨 *ALERTA CRÍTICA DE STOCK GRUPAL* 🚨\n\n");
            mensaje.append("Los productos con las siguientes características han llegado a stock crítico:\n\n");
            mensaje.append("🔍 *Características Comunes:*\n");
            mensaje.append("- Marca: ").append(primerProducto.getMarcaVehiculo()).append("\n");
            mensaje.append("- Modelo: ").append(primerProducto.getModeloVehiculo()).append("\n");
            mensaje.append("- Año: ").append(primerProducto.getAnioVehiculo()).append("\n");
            mensaje.append("- Tipo: ").append(primerProducto.getTipoVidrio()).append("\n");
            mensaje.append("- Calidad: ").append(primerProducto.getCalidadVidrio()).append("\n\n");
            
            mensaje.append("📦 *Desglose por Producto:*\n");
            for (Producto p : productosSimilares) {
                mensaje.append("  • ID: ").append(p.getIdProducto());
                mensaje.append(" - Proveedor: ").append(p.getProveedor().getNombreProveedor());
                mensaje.append(" - Stock: ").append(p.getStockActual());
                if (p.getUbicacionAlmacen() != null) {
                    mensaje.append(" - Ubicación: ").append(p.getUbicacionAlmacen());
                }
                mensaje.append("\n");
            }
            
            mensaje.append("\n📊 *STOCK TOTAL: ").append(stockTotal).append("*\n");
            mensaje.append("\n🔴 *ACCIÓN URGENTE REQUERIDA*\n");
            mensaje.append("Es crítico reabastecer estos productos inmediatamente.");

            enviarMensaje(mensaje.toString());
            logger.info("Alerta de stock crítico grupal enviada. Total productos: {}, Stock total: {}", 
                       productosSimilares.size(), stockTotal);
        } catch (Exception e) {
            logger.error("Error al enviar alerta de stock crítico grupal: {}", e.getMessage());
        }
    }

    /**
     * Método genérico para enviar mensajes de WhatsApp
     */
    private void enviarMensaje(String contenido) {
        Message message = Message.creator(
                new PhoneNumber(destinationNumber),
                new PhoneNumber(twilioPhoneNumber),
                contenido
        ).create();

        logger.info("Mensaje enviado con SID: {}", message.getSid());
    }
}
