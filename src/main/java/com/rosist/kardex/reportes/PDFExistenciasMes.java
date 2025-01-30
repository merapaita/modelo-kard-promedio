package com.rosist.kardex.reportes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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

public class PDFExistenciasMes {
	private DriverManagerDataSource datasource;
	private Connection conexion;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
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

	static final Logger log = LoggerFactory.getLogger(PDFExistenciasMes.class);
	
	public PDFExistenciasMes(Map<String, Object> parametros) {
		datasource = (DriverManagerDataSource) parametros.get("datasource");
		periodo = (Integer) parametros.get("periodo");
		mes = (Integer) parametros.get("mes");
		tipkar = (String) parametros.get("tipkar");
//        lEmpresa = (List<Empresa>)model.get("lEmpresa");
	}
	
	public byte[] creaReporte()  throws SQLException, IOException {
		sql = "SELECT k.periodo, k.tipkar, k.id_articulo, a.codart, a.nomart, k.correl, k.coraux, k.fecha, k.tipdoc, "
				+ "       k.numdoc, k.item, k.cantidad, a.unidad, k.fraccion, k.totcan, IF(k.tipmov='+',k.totcan,0) entrada, "
				+ "       IF(k.tipmov='-',k.totcan,0) salida, k.preuni, k.valuni "
				+ "  FROM kardex k LEFT JOIN articulo a ON k.id_articulo=a.id_articulo "
				+ " WHERE YEAR(fecha) = ? and MONTH(fecha)=? and tipkar=?"
		        + " and estado<>'99'";
//		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//		String fondo = classloader.getResource("fondo.png").getFile();
//		Image imagen = new Image(ImageDataFactory.create(fondo));  
		
        ByteArrayOutputStream docBytes = new ByteArrayOutputStream();
        pdfWriter = new PdfWriter(docBytes);

		pdfDocument = new PdfDocument(pdfWriter);
		document = new Document(pdfDocument, PageSize.A4.rotate()); // , new PageSize(envelope)
		
        EventoPagina evento = new EventoPagina(document);
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, evento);
        
        document.setMargins(75, 36, 75, 36);

		conexion = datasource.getConnection();
		pstmt = conexion.prepareStatement(sql);		// , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE
		pstmt.setInt(1, periodo);
		pstmt.setInt(2, mes);
		pstmt.setString(3, tipkar);
		
		rs = pstmt.executeQuery();
		log.info("PDFKardex...hasta aqui bien.-> ");
        
		table = new Table(new float[] { 60, 20, 20, 200, 60, 30, 30, 30, 50, 40, 40, 40, 40, 40 }, true);
		log.info("PDFKardex...por aqui bien.-> ");
		
		fontContenido = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
		
		String MES[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
		String TIPO[] = {"Existencias", "Donaciones", "Sobrantes"};
		cell = new Cell(1,14).add(new Paragraph("Resumen de Existencias por mes " + MES[mes-1] + " - " + TIPO[Integer.valueOf(tipkar)-1]).setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.BLACK, ColorConstants.WHITE, ColorConstants.WHITE);
		table.addHeaderCell(cell);
////**//		
		cell = new Cell(1,3)
				.add(new Paragraph("Codigo - corr - aux").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		cell = new Cell()
				.add(new Paragraph("Descripcion").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		cell = new Cell()
				.add(new Paragraph("Fecha").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		cell = new Cell(1,3)
				.add(new Paragraph("Documento - Item").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
		.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		cell = new Cell()
				.add(new Paragraph("Unidad").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		cell = new Cell()
				.add(new Paragraph("Cantidad").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		cell = new Cell()
				.add(new Paragraph("Entrada").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		cell = new Cell()
				.add(new Paragraph("Salida").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		cell = new Cell()
				.add(new Paragraph("P.U").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		cell = new Cell()
				.add(new Paragraph("V.U").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
		document.add(table);
		int i = 0;
        while (rs.next()) {
        	i++;
	        cell = new Cell()
	        		.add(new Paragraph(rs.getString("codart")).setTextAlignment(TextAlignment.CENTER))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	        
	        cell = new Cell()
	        		.add(new Paragraph(String.valueOf(rs.getString("correl"))).setTextAlignment(TextAlignment.CENTER))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	        
	        cell = new Cell()
	        		.add(new Paragraph("").setTextAlignment(TextAlignment.CENTER))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	        
	        cell = new Cell()
	        		.add(new Paragraph(rs.getString("nomart")).setTextAlignment(TextAlignment.LEFT))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);

	        cell = new Cell()
	        		.add(new Paragraph(rs.getString("fecha")).setTextAlignment(TextAlignment.CENTER))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	        
	        cell = new Cell()
	        		.add(new Paragraph(rs.getString("tipdoc")).setTextAlignment(TextAlignment.CENTER))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	        
	        cell = new Cell()
	        		.add(new Paragraph(rs.getString("numdoc")).setTextAlignment(TextAlignment.CENTER))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	        
	        cell = new Cell()
	        		.add(new Paragraph(String.valueOf(rs.getInt("item"))).setTextAlignment(TextAlignment.CENTER))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	    	
	        cell = new Cell()
	        		.add(new Paragraph(rs.getString("unidad")).setTextAlignment(TextAlignment.CENTER))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	    	
	        cell = new Cell()
	        		.add(new Paragraph(String.valueOf(rs.getDouble("cantidad"))).setTextAlignment(TextAlignment.RIGHT))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	    	
	        cell = new Cell()
	        		.add(new Paragraph(String.valueOf(rs.getDouble("entrada"))).setTextAlignment(TextAlignment.RIGHT))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	    	
	        cell = new Cell()
	        		.add(new Paragraph(String.valueOf(rs.getDouble("salida"))).setTextAlignment(TextAlignment.RIGHT))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	    	
	        cell = new Cell()
	        		.add(new Paragraph(String.valueOf(rs.getDouble("preuni"))).setTextAlignment(TextAlignment.RIGHT))
	        		.setFont(fontContenido)
	        		.setFontSize(8);
	        table.addCell(cell);
	    	
	        cell = new Cell()
	        		.add(new Paragraph(String.valueOf(rs.getDouble("valuni"))).setTextAlignment(TextAlignment.RIGHT))
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
