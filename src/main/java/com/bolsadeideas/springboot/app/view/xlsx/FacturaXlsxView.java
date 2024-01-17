package com.bolsadeideas.springboot.app.view.xlsx;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import java.util.Locale;
import java.util.Map;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        Locale locale = localeResolver.resolveLocale(request);

        response.setHeader("Content-Disposition", "attachment; filename=\"factura_spring.xlsx\"");

        // creamos una factura y le mapeamos con el nombre del controlador
        Factura factura = (Factura) model.get("factura");

        // creamos una hoja excel llamada factura
        Sheet sheet = workbook.createSheet("Factura " + factura.getCliente().getNombre());

        // creacion de las celdas para el cliente
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);

        // creamos un estilo para dar color
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // tabla de datos del cliente con 2 filas y color
        cell.setCellValue(messageSource.getMessage("text.factura.ver.datosCliente", null, locale));
        cell.setCellStyle(style);
        sheet.createRow(1).createCell(0).setCellValue(messageSource.getMessage("text.cliente.nombre", null, locale) + ": " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        sheet.createRow(2).createCell(0).setCellValue(messageSource.getMessage("text.cliente.email", null, locale) + ": " + factura.getCliente().getEmail());

        // creamos una fila para darle estilo al titulo de la tabla datos de la factura
        Row rowFactura = sheet.createRow(4);
        rowFactura.createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.datosFactura", null, locale));
        rowFactura.getCell(0).setCellStyle(style);
        sheet.createRow(5).createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.folio", null, locale) + ": " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue(messageSource.getMessage("text.factura.descripcion", null, locale) + ": " + factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue(messageSource.getMessage("text.factura.fecha", null, locale) + ": " + factura.getCreateAt());

        // estilos para el header de la tabla
        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader.setBorderBottom(BorderStyle.MEDIUM);
        styleHeader.setBorderLeft(BorderStyle.MEDIUM);
        styleHeader.setBorderTop(BorderStyle.MEDIUM);
        styleHeader.setBorderRight(BorderStyle.MEDIUM);

        // estilos para el cuerpo de la tabla
        CellStyle styleBody = workbook.createCellStyle();
        styleBody.setBorderBottom(BorderStyle.THIN);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBorderTop(BorderStyle.THIN);
        styleBody.setBorderRight(BorderStyle.THIN);

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue(messageSource.getMessage("text.factura.nombreProducto", null, locale));
        header.createCell(1).setCellValue(messageSource.getMessage("text.factura.precio", null, locale));
        header.createCell(2).setCellValue(messageSource.getMessage("text.factura.cantidad", null, locale));
        header.createCell(3).setCellValue(messageSource.getMessage("text.factura.total", null, locale));

        header.getCell(0).setCellStyle(styleHeader);
        header.getCell(1).setCellStyle(styleHeader);
        header.getCell(2).setCellStyle(styleHeader);
        header.getCell(3).setCellStyle(styleHeader);

        // for para ir creando las filas de la factura
        int contadorFila = 10;
        for (ItemFactura items : factura.getItems()){
            Row fila = sheet.createRow(contadorFila++);

            cell = fila.createCell(0);
            cell.setCellValue(items.getProducto().getNombre());
            cell.setCellStyle(styleBody);

            cell = fila.createCell(1);
            cell.setCellValue(items.getProducto().getPrecio());
            cell.setCellStyle(styleBody);

            cell = fila.createCell(2);
            cell.setCellValue(items.getCantidad());
            cell.setCellStyle(styleBody);

            cell = fila.createCell(3);
            cell.setCellValue(items.calcularImporte());
            cell.setCellStyle(styleBody);
        }

        // creamos fila para darle estilo al total de la factura
        Row filaTotal = sheet.createRow(contadorFila);
        filaTotal.createCell(2).setCellValue(messageSource.getMessage("text.fectura.totalFactura", null, locale));
        filaTotal.createCell(3).setCellValue(factura.getTotal());
        filaTotal.getCell(2).setCellStyle(styleBody);
        filaTotal.getCell(3).setCellStyle(styleBody);


    }
}
