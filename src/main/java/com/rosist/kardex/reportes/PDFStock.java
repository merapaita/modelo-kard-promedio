package com.rosist.kardex.reportes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.rosist.kardex.model.Stock;

public class PDFStock {
	
	private Document document;
	private PdfDocument pdfDocument;
	private PdfWriter pdfWriter = null;
	
	private PdfFont fontContenido;
	private Table table;
	private Cell cell;

	private Integer periodo;
	private String tipkar;
	private Integer mes;
	private String sql;
	private double entrada;
	private double salida;

	List<Stock> lStock;
	
	static final Logger logger = LoggerFactory.getLogger(PDFStock.class);
	
	public PDFStock(Map<String, Object> parametros) {
		lStock = (List<Stock>) parametros.get("lStock");
		periodo = (Integer) parametros.get("periodo");
		mes = (Integer) parametros.get("mes");
		tipkar = (String) parametros.get("tipkar");
//        lEmpresa = (List<Empresa>)model.get("lEmpresa");
	}
	
	public byte[] creaReporte() throws IOException {
		
		try {
			fontContenido = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
//			fontContenido = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        ByteArrayOutputStream docBytes = new ByteArrayOutputStream();
        pdfWriter = new PdfWriter(docBytes);

		pdfDocument = new PdfDocument(pdfWriter);
		document = new Document(pdfDocument, PageSize.A4.rotate()); // , new PageSize(envelope)
		
        EventoPagina evento = new EventoPagina(document);
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, evento);
        
        document.setMargins(75, 36, 75, 36);
        
		table = new Table(new float[] { 60, 20, 20, 200, 50, 50, 50, 50, 50, 50, 50, 50 }, true);
		fontContenido = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
//		fontContenido = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);

		
		if (lStock != null) {}
		logger.info("PDFKardex...por aqui bien.->xx " + mes);
		
	////**//		
		String MES[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
		String TIPO[] = {"Existencias", "Donaciones", "Sobrantes"};
		cell = new Cell(1,12)
				.add(new Paragraph("Stock de Existencias por mes " + (mes!=0?MES[mes-1]:"") + " - " + TIPO[Integer.valueOf(tipkar)-1])   
						.setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		
		bordes(cell, ColorConstants.WHITE, ColorConstants.BLACK, ColorConstants.WHITE, ColorConstants.WHITE);
		table.addHeaderCell(cell);
	////**//		
		cell = new Cell(1,4)
				.add(new Paragraph("Articulo:").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setWidth(8).setFontSize(8);
		table.addHeaderCell(cell);
		logger.info("PDFKardex...por aqui bien.-> ");

		cell = new Cell()
				.add(new Paragraph("Entrada.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
//        		.setWidth(40)
				.setFontSize(8);
		table.addHeaderCell(cell);
		
		cell = new Cell()
				.add(new Paragraph("Salida.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
//        		.setWidth(40)
				.setFontSize(8);
		table.addHeaderCell(cell);
		cell = new Cell()
				.add(new Paragraph("Saldo.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
//        		.setWidth(40)
				.setFontSize(8);
		table.addHeaderCell(cell);
		logger.info("PDFKardex...por aqui bien.-> 2");

		cell = new Cell().add(new Paragraph("Fracc.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		table.addHeaderCell(cell);

		cell = new Cell().add(new Paragraph("Total").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		table.addHeaderCell(cell);

		cell = new Cell().add(new Paragraph("P.U.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		table.addHeaderCell(cell);

		cell = new Cell().add(new Paragraph("IGV.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		table.addHeaderCell(cell);

		cell = new Cell().add(new Paragraph("V.U").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		table.addHeaderCell(cell);
		logger.info("PDFKardex...por aqui bien.-> 3" );
		
		document.add(table);
		
		int i = 0;
        for (Stock registro : lStock) {
        	i++;
            String dato = registro.getArticulo().getCodart() + "-"+ registro.getCorrel() + " "+ registro.getArticulo().getNomart();
            cell = new Cell(1,4)
            		.add(new Paragraph(dato).setTextAlignment(TextAlignment.LEFT))
            		.setFont(fontContenido)
            		.setFontSize(8);
            bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
            table.addCell(cell);
            
            cell = new Cell()
            		.add(new Paragraph(String.valueOf(registro.getEntrada())).setTextAlignment(TextAlignment.CENTER))
            		.setFont(fontContenido)
            		.setFontSize(8);
            table.addCell(cell);
            cell = new Cell()
            		.add(new Paragraph(String.valueOf(registro.getSalida())).setTextAlignment(TextAlignment.CENTER))
            		.setFont(fontContenido)
            		.setFontSize(8);
            table.addCell(cell);
            
            cell = new Cell()
            		.add(new Paragraph(String.valueOf(registro.getSaldoCantidad())).setTextAlignment(TextAlignment.CENTER))
            		.setFont(fontContenido)
            		.setFontSize(8);
            table.addCell(cell);
            
            cell = new Cell()
            		.add(new Paragraph(String.valueOf(registro.getFraccion())).setTextAlignment(TextAlignment.CENTER))
            		.setFont(fontContenido)
            		.setFontSize(8);
            table.addCell(cell);
            
            cell = new Cell()
            		.add(new Paragraph(String.valueOf(registro.getSaldoTotalCantidad())).setTextAlignment(TextAlignment.CENTER))
            		.setFont(fontContenido)
            		.setFontSize(8);
            table.addCell(cell);
            
            cell = new Cell()
            		.add(new Paragraph(String.valueOf(registro.getPrecioPromedio())).setTextAlignment(TextAlignment.CENTER))
            		.setFont(fontContenido)
            		.setFontSize(8);
            table.addCell(cell);
            
            cell = new Cell()
            		.add(new Paragraph(String.valueOf(registro.getIgvPromedio())).setTextAlignment(TextAlignment.CENTER))
            		.setFont(fontContenido)
            		.setFontSize(8);
            table.addCell(cell);
            
            cell = new Cell()
            		.add(new Paragraph(String.valueOf(registro.getValorPromedio())).setTextAlignment(TextAlignment.CENTER))
            		.setFont(fontContenido)
            		.setFontSize(8);
            table.addCell(cell);
        	
			if (i % 30 == 0) {
				table.flush();
			}
        }

		table.complete();
		document.close();
        
        return docBytes.toByteArray();
	}
	
	private void bordes(Cell cell, Color arriba, Color abajo, Color izquierda, Color derecha) {
		cell.setBorderTop(new SolidBorder(arriba, 1));
		cell.setBorderBottom(new SolidBorder(abajo, 1));
		cell.setBorderLeft(new SolidBorder(izquierda, 1));
		cell.setBorderRight(new SolidBorder(derecha, 1));
	}
	
}
