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

public class PDFExistenciasMes_ant2 {
	private DriverManagerDataSource datasource;
	private Connection conexion;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private PdfFont fontContenido;
	private PdfWriter pdfWriter = null;
	private PdfDocument pdfDocument;
	private Document document;
	private Integer pagina;
	private Integer linea;
	private Table table;
	private Cell cell;

	private Integer periodo;
	private String tipkar;
	private Integer mes;
	private String sql;
	private double entrada;
	private double salida;

	static final Logger logger = LoggerFactory.getLogger(PDFExistenciasMes_ant2.class);

	public PDFExistenciasMes_ant2(Map<String, Object> parametros) {
		datasource = (DriverManagerDataSource) parametros.get("datasource");
		periodo = (Integer) parametros.get("periodo");
		mes = (Integer) parametros.get("mes");
		tipkar = (String) parametros.get("tipkar");
//        lEmpresa = (List<Empresa>)model.get("lEmpresa");
	}

	public byte[] creaReporte() throws SQLException, IOException {
		sql = "SELECT k.periodo, k.tipkar, k.id_articulo, a.codart, a.nomart, k.correl, k.coraux, k.fecha, k.tipdoc, "
				+ "       k.numdoc, k.item, k.cantidad, k.unidad, k.fraccion, k.totcan, IF(k.tipmov='+',k.totcan,0) entrada, "
				+ "       IF(k.tipmov='-',k.totcan,0) salida, k.preuni, k.valuni "
				+ "  FROM kardex k LEFT JOIN articulo a ON k.id_articulo=a.id_articulo " + " WHERE YEAR(fecha)="
				+ periodo + " AND MONTH(fecha)=" + mes;
		sql += " and estado<>'99'";
//      sql+=" ORDER BY periodo, tipkar, codart, correl, coraux ";
//        logger.info("PDFKardex...sql.-> " + sql);
//		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//		String fondo = classloader.getResource("fondo.png").getFile();
//		Image imagen = new Image(ImageDataFactory.create(fondo));  

		ByteArrayOutputStream docBytes = new ByteArrayOutputStream();
		pdfWriter = new PdfWriter(docBytes);

		pdfDocument = new PdfDocument(pdfWriter);
		document = new Document(pdfDocument, PageSize.A4.rotate()); // , new PageSize(envelope)
//    
		conexion = datasource.getConnection();
		logger.info("PDFKardex...statement.-> ");
		pstmt = conexion.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = pstmt.executeQuery(sql);

		pagina = 0;
		plantilla();
		table = new Table(new float[] { 60, 20, 20, 200, 60, 30, 30, 30, 50, 40, 40, 40, 40, 40 }, true);
//		cabecera();

		// *****//
		cell = new Cell(1, 12)
				.add(new Paragraph("SOCIEDAD DE BENEFICENCIA DE PIURA").setTextAlignment(TextAlignment.LEFT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		table.addHeaderCell(cell);

		String dato = "Pag:" + (pagina++);
		cell = new Cell(1,2).add(new Paragraph(dato).setTextAlignment(TextAlignment.RIGHT)).setFont(fontContenido)
				.setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		table.addHeaderCell(cell);
//**//		
		cell = new Cell(1,14).add(new Paragraph("Resumen de Existencias por mes " + mes).setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.BLACK, ColorConstants.WHITE, ColorConstants.WHITE);
		table.addHeaderCell(cell);
//**//		
		cell = new Cell(1,3)
				.add(new Paragraph("Codigo - corr - aux").setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido)
				.setFontSize(8);
		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
		table.addHeaderCell(cell);
		
//		cell = new Cell()
//				.add(new Paragraph("Correl").setTextAlignment(TextAlignment.RIGHT))
//				.setFont(fontContenido)
//				.setFontSize(8);
//		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
//		table.addHeaderCell(cell);
//		
//		cell = new Cell()
//				.add(new Paragraph("Aux").setTextAlignment(TextAlignment.RIGHT))
//				.setFont(fontContenido)
//				.setFontSize(8);
//		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
//		table.addHeaderCell(cell);
		
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
		
//		cell = new Cell()
//				.add(new Paragraph("Numero").setTextAlignment(TextAlignment.RIGHT))
//				.setFont(fontContenido)
//				.setFontSize(8);
//		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
//		table.addHeaderCell(cell);
		
//		cell = new Cell()
//				.add(new Paragraph("Item").setTextAlignment(TextAlignment.RIGHT))
//				.setFont(fontContenido)
//				.setFontSize(8);
//		bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
//		table.addHeaderCell(cell);
		
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
		
		
//		table.addFooterCell("Table footer 3");
//		table.addFooterCell("Table footer 1");
		document.add(table);
		int i = 0;
        while (rs.next()) {
//		for (int i = 0; i < 1000; i++) {
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
	        		.add(new Paragraph(String.valueOf(rs.getString("coraux"))).setTextAlignment(TextAlignment.CENTER))
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
			
			if (i % 50 == 0) {
				table.flush();
			}
		}
		table.complete();
		document.close();

		return docBytes.toByteArray();
	}

	private void plantilla() throws IOException {
		fontContenido = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
	}

	private void bordes(Cell cell, Color arriba, Color abajo, Color izquierda, Color derecha) {
		cell.setBorderTop(new SolidBorder(arriba, 1));
		cell.setBorderBottom(new SolidBorder(abajo, 1));
		cell.setBorderLeft(new SolidBorder(izquierda, 1));
		cell.setBorderRight(new SolidBorder(derecha, 1));
	}

}
