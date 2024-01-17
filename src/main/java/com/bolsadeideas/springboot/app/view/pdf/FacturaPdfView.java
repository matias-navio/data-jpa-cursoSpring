package com.bolsadeideas.springboot.app.view.pdf;


import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.awt.*;
import java.util.Locale;
import java.util.Map;

// este componente se anota igual a lo que devuelve el motodo ver handler del controlador
@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;


    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document,
                                    PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        Locale locale = localeResolver.resolveLocale(request);

        // se crea un objeto factura y tmb se anota como el model del controlador
        Factura factura = (Factura) model.get("factura");

        // creamos una celda que va a ser la cabecera de la tabla clientes y factura para poder darle estilo
        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datosCliente", null, locale)));
        cell.setBackgroundColor(new Color(184, 218, 255)); // color de fondo
        cell.setPadding(8f); // padding

        // se crea una tabla de una sola columna
        PdfPTable tabla1 = new PdfPTable(1);
        tabla1.setSpacingAfter(20); // espacio entre tablas
        tabla1.addCell(cell);
        tabla1.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla1.addCell(factura.getCliente().getEmail());

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datosFactura", null, locale)));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);

        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);
        tabla2.addCell(cell);
        tabla2.addCell(messageSource.getMessage("text.factura.ver.folio", null, locale) + ": " + factura.getId());
        tabla2.addCell(messageSource.getMessage("text.factura.descripcion", null, locale) + ": " + factura.getDescripcion());
        tabla2.addCell(messageSource.getMessage("text.factura.fecha", null, locale) + ": " + factura.getCreateAt());

        // se agregan las tablas al documento pdf
        document.add(tabla1);
        document.add(tabla2);

        // creamos una tercer tabla para los detalles de la factura, como el precio, cantidad, etc
        PdfPTable tabla3 = new PdfPTable(4);
        tabla3.setWidths(new float[]{2.5f, 1, 1, 1}); // setea el tamaño de cada columna
        tabla3.addCell(new Phrase(messageSource.getMessage("text.factura.nombreProducto", null, locale), new Font(Font.BOLD, 12, Font.BOLD)));
        tabla3.addCell(new Phrase(messageSource.getMessage("text.factura.precio", null, locale), new Font(Font.BOLD, 12, Font.BOLD)));
        tabla3.addCell(new Phrase(messageSource.getMessage("text.factura.cantidad", null, locale), new Font(Font.BOLD, 12, Font.BOLD)));
        tabla3.addCell(new Phrase(messageSource.getMessage("text.factura.total", null, locale), new Font(Font.BOLD, 12, Font.BOLD)));

        // recorremos la tabla con un for y vamos agregando las lineas
        for(ItemFactura item : factura.getItems()){
            tabla3.addCell(item.getProducto().getNombre());
            tabla3.addCell(item.getProducto().getPrecio().toString());
            tabla3.addCell(item.getCantidad().toString());
            tabla3.addCell(item.calcularImporte().toString());
        }

        // creamos celda aparte para el gran total
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.granTotal", null, locale)));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(factura.getTotal().toString());

        document.add(tabla3);
    }
}
