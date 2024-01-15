package com.bolsadeideas.springboot.app.view.pdf;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.Map;

//es la misma ruta que devuelve le metodo handler ver de FacturaController
@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document,
                                    PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        // creamos una factura y se la asignamos casteada al model
        // se tiene que llamar igual que como le asignamos en el controlador
        Factura factura = (Factura) model.get("factura");

        // creamos una tabla con una columna pera los datos del cliente
        PdfPTable tabla = new PdfPTable(1);
        tabla.setSpacingAfter(20);
        tabla.addCell("Datos del Cliente");
        tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla.addCell(factura.getCliente().getEmail());

        // creamos otra tabla para los datos de la factura
        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);
        tabla2.addCell("Datos de la Factura");
        tabla2.addCell("Folio: " + factura.getId());
        tabla2.addCell("Descripción: " + factura.getDescripcion());
        tabla2.addCell("Fecha: " + factura.getCreateAt());

        // agragamos las tablas al document
        document.add(tabla);
        document.add(tabla2);

    }
}
