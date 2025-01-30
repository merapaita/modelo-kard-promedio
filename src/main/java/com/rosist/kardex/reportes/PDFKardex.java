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

public class PDFKardex {

    private DriverManagerDataSource datasource;
    private Connection conexion;
    private PreparedStatement pstmt;
    private ResultSet rs;

	private PdfFont fontContenido;
	private PdfWriter pdfWriter = null;
	private PdfDocument pdfDoc;
	private Document documento;
	private int pagina;
	private int linea;
	private Table tCabecera;
	private Table tCabArt;
	private Table tDetalle;
	private Table tPieArt;
	private Cell cell;
	
	private Integer periodo;
	private String tipkar;
	private Integer id_articulo;
	private Integer correl;
	private String nomart;
    private String fecini;
    private String fecfin;
	private String sql;
	private double entrada;
	private double salida;

	static final Logger logger = LoggerFactory.getLogger(PDFKardex.class);
	
	public PDFKardex(Map<String, Object> parametros) {
        datasource = (DriverManagerDataSource) parametros.get("datasource");
        periodo = (Integer) parametros.get("periodo");
        tipkar = (String) parametros.get("tipkar");
        id_articulo  = (Integer)  parametros.get("id_articulo");
        nomart  = (String)  parametros.get("descri");
        fecini  = (String)  parametros.get("fecini");
        fecfin  = (String)  parametros.get("fecfin");
	}

	public byte[] creaKardex() throws SQLException, IOException {
        sql = "SELECT k.id_kardex, k.periodo, k.tipkar, k.id_articulo, a.codart, a.nomart, a.menudeo menart, "
        		+ "       a.fraccion fracart, a.unidad uniart, a.unimen unimenart, k.correl,  k.coraux, "
        		+ "       k.estado, k.fecha, k.tipdoc, k.menudeo, k.numdoc, k.item, "
        		+ "       k.cantidad, k.fraccion, k.totcan, k.saldo_cantidad, k.tipmov, IF(k.tipmov='+',k.totcan,0) entrada, "
        		+ "       IF(k.tipmov='-',k.totcan,0) salida, k.preuni, k.igv, k.valuni, k.preunifr, "
        		+ "       k.igvfr, k.valunifr, k.valor_promedio, k.valor_promedio_fraccion, k.dusercr, k.duserup, k.usercr, k.userup, k.unidad, k.unimen "
        		+ "  FROM kardex k     LEFT JOIN articulo a  ON a.id_articulo=k.id_articulo "
            	+ " WHERE 1=1 ";
        sql+=" and periodo=" + periodo + " and tipkar='" + tipkar + "'"  ;
        if (id_articulo!=null) sql+=" and k.id_articulo="+id_articulo;
        if (correl!=null) sql+=" and correl="+correl;
        if (nomart!=null) sql+=" and nomart like'%"+nomart+"%'";
        if (fecini!=null) sql+=" and fecha>='"+fecini+"'";
        if (fecfin!=null) sql+=" and fecha<='"+fecfin+"'";
        sql+=" and estado<>'99'";
        sql+=" ORDER BY periodo, tipkar, codart, correl ";
        
//        logger.info("PDFKardex...sql.-> " + sql);

//		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//		String fondo = classloader.getResource("fondo.png").getFile();

//		Image imagen = new Image(ImageDataFactory.create(fondo));  
//		Map<String, Object> parametros = new HashMap<String, Object>();
        ByteArrayOutputStream docBytes = new ByteArrayOutputStream();
        pdfWriter = new PdfWriter(docBytes);

		pdfDoc = new PdfDocument(pdfWriter);
		documento = new Document(pdfDoc, PageSize.A4); // , new PageSize(envelope)
    
        conexion = datasource.getConnection();
        pstmt = conexion.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
        		  ResultSet.CONCUR_UPDATABLE);
        rs = pstmt.executeQuery(sql);
        
		if(rs.first()){//recorre el resultset al siguiente registro si es que existen
	        logger.info("PDFKardex...encontre.-> ");
            rs.beforeFirst();//regresa el puntero al primer registro
			pagina=0;
			linea=0;
			entrada=0;
			salida=0;
			plantilla();	// writer
			cabecera();
			int xPeriodo = 0; String xCodArt=""; int xCorrel = 0;
            while (rs.next()) {
                if (linea==0){
                    cabecera();
                }
                if (!(xPeriodo==rs.getInt("periodo") && xCodArt.contentEquals(rs.getString("codart")))) {
                	cabArt(rs);
                }
//                if (!(xPeriodo==rs.getInt("periodo") && xCodArt.contentEquals(rs.getString("codart")) && xCorrel==rs.getInt("correl"))) {
//                	cabPreArt(rs);
//                }
                detArt(rs);
            	xPeriodo = rs.getInt("periodo");
            	xCodArt  = rs.getString("codart");
//            	xCorrel  = rs.getInt("correl");
                rs.next();
                if (rs.isAfterLast()){
                    rs.last();
                }else{
//                    if (!(xPeriodo==rs.getInt("periodo") && xCodArt.equals(rs.getString("codart")) && xCorrel==rs.getInt("correl"))) {
//                        rs.previous();
//                        pieArtCor();
//                    } else rs.previous();
                	rs.previous();
                }
                rs.next();
                if (rs.isAfterLast()){
                    rs.last();
                } else {
                    if (!(xPeriodo==rs.getInt("periodo") && xCodArt.equals(rs.getString("codart")))){
                        rs.previous();
                        pieArt();
//                        doc.newPage();
                    } else rs.previous();
                }
                if (linea>=62){
                    linea = 0;
//                    doc.newPage();
                }
                
                if (rs.isLast()){
                    pieArtCor();
//                    doc.newPage();
                }
            }
        } else {
			plantilla();	// writer
			cabecera();
        }
		documento.close();
        return docBytes.toByteArray();
	}
	
	private void pieArt() {
    	tPieArt = new Table(new float[]{45, 45, 75, 40, 40, 40, 40, 45, 45, 65, 40});
        tPieArt.setWidth(PageSize.A4.getWidth()-60);
    	
        cell = new Cell(1,9)
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE);
        tPieArt.addCell(cell);
        documento.add(tPieArt);
        
        cell = new Cell(1,2)
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE);
        tPieArt.addCell(cell);
        documento.add(tPieArt);
        
        linea +=1;
	}

	private void pieArtCor() {
logger.info("pieArtCor");
		tPieArt = new Table(new float[]{45, 45, 75, 40, 40, 40, 40, 45, 45, 65, 40});
        tPieArt.setWidth(PageSize.A4.getWidth()-60);
    	
        cell = new Cell(1,2)
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.WHITE);
        tPieArt.addCell(cell);
        
        cell = new Cell(1,5)
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.WHITE,ColorConstants.BLACK);
        tPieArt.addCell(cell);
        
        double xEntrada = (entrada-salida>salida-entrada?entrada-salida:0);
        double xSalida  = (entrada-salida<salida-entrada?salida-entrada:0);

        cell = new Cell()
        		.add(new Paragraph((xEntrada!=0.0?String.valueOf(xEntrada):"-")).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tPieArt.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph((xSalida!=0.0?String.valueOf(xSalida):"-")).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tPieArt.addCell(cell);
        
        cell = new Cell(1,2)
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        tPieArt.addCell(cell);
        documento.add(tPieArt);
        
        entrada = 0;
        salida  = 0;
        linea +=1;
	}

	private void detArt(ResultSet rs2) throws SQLException {
		tDetalle = new Table(new float[]{45, 45, 75, 40, 40, 40, 40, 45, 45, 65, 40});
		tDetalle.setWidth(PageSize.A4.getWidth()-60);
		
        cell = new Cell()
        		.add(new Paragraph(String.valueOf(rs2.getInt("correl"))).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph(rs2.getString("fecha")).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);
        
        String dato = rs2.getString("tipdoc") + " - " + rs2.getInt("numdoc");
        cell = new Cell()
        		.add(new Paragraph(dato).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph(String.valueOf(rs2.getInt("item"))).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);

        cell = new Cell()
        		.add(new Paragraph(String.valueOf(rs2.getDouble("cantidad"))).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph(String.valueOf(rs2.getDouble("saldo_cantidad"))).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph(rs2.getString("tipmov")).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph(String.valueOf(rs2.getDouble("entrada"))).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);
	
        cell = new Cell()
        		.add(new Paragraph(String.valueOf(rs2.getDouble("salida"))).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph(String.valueOf(rs2.getDouble("valuni"))).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph(String.valueOf(rs2.getDouble("valor_promedio"))).setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tDetalle	.addCell(cell);
        
//        cell = new Cell()
//        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
//        		.setFont(fontContenido)
//        		.setFontSize(8);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
//        tDetalle.addCell(cell);
        
        documento.add(tDetalle);
        entrada += rs2.getDouble("entrada");
        salida  += rs2.getDouble("salida");
        linea+=1;
	}

	private void cabPreArt(ResultSet rs2) throws SQLException {
    	tCabArt = new Table(new float[]{45, 45, 75, 40, 40, 40, 40, 45, 45, 65, 40});
        tCabArt.setWidth(PageSize.A4.getWidth()-60);
/**/
        cell = new Cell(1,2)
        		.add(new Paragraph("Precio:").setTextAlignment(TextAlignment.RIGHT))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.WHITE);
        tCabArt.addCell(cell);
        
        String dato = String.valueOf(rs2.getDouble("preuni"));
        cell = new Cell(1,7)
        		.add(new Paragraph(dato).setTextAlignment(TextAlignment.LEFT))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.BLACK,ColorConstants.BLACK);
        tCabArt.addCell(cell);
        
        cell = new Cell(1,2)
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
/**/
        cell = new Cell()
        		.add(new Paragraph("Cor").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph("Fecha").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph("Doc.").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph("Item").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);

        cell = new Cell()
        		.add(new Paragraph("Cantidad").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph("Fraccion").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph("Tipo").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph("Entrada").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
	
        cell = new Cell()
        		.add(new Paragraph("Salida").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
   		tCabArt.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        tCabArt.addCell(cell);
        
        cell = new Cell()
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        tCabArt.addCell(cell);
        
        documento.add(tCabArt);
        linea+=2;
	}

	private void cabArt(ResultSet rs2) throws SQLException {
		tCabArt = new Table(new float[]{45, 45, 75, 40, 40, 40, 40, 45, 45, 65, 40});
		tCabArt.setWidth(PageSize.A4.getWidth()-60);
/**/
		cell = new Cell(1,2)
				.add(new Paragraph("Periodo:").setTextAlignment(TextAlignment.RIGHT))
				.setFont(fontContenido)
				.setFontSize(8);
//		bordes(cell,ColorConstants.BLACK,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
		tCabArt.addCell(cell);
        
        cell = new Cell(1,7)
        		.add(new Paragraph(rs2.getString("periodo")).setTextAlignment(TextAlignment.LEFT))
        		.setFont(fontContenido)
        		.setFontSize(8);
//        bordes(cell,ColorConstants.BLACK,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.BLACK);
        tCabArt.addCell(cell);
        cell = new Cell()
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.BLACK,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK);
        cell = new Cell()
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
/**/
        cell = new Cell(1,2)
        		.add(new Paragraph("Articulo:").setTextAlignment(TextAlignment.RIGHT))
        		.setFont(fontContenido)
        		.setFontSize(8);
//        bordes(cell,ColorConstants.BLACK,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        tCabArt.addCell(cell);
        
        String dato = rs2.getString("codart") + "-"+ rs2.getInt("correl") + " "+ rs2.getString("nomart");
        cell = new Cell(1,7)
        		.add(new Paragraph(dato).setTextAlignment(TextAlignment.LEFT))
        		.setFont(fontContenido)
        		.setFontSize(8);
//        bordes(cell,ColorConstants.BLACK,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.BLACK);
        tCabArt.addCell(cell);
        cell = new Cell()
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE,ColorConstants.BLACK);
        cell = new Cell()
        		.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Id").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Fecha").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Documento").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Item").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Cantidad").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Acumulado").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("+ / -").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Entrada").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Salida").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Valor").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        cell = new Cell()
        		.add(new Paragraph("Promedio").setTextAlignment(TextAlignment.CENTER))
        		.setFont(fontContenido)
        		.setFontSize(8);
        tCabArt.addCell(cell);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.BLACK,ColorConstants.WHITE);
        
        documento.add(tCabArt);
        linea+=1;
	}
	
	private void cabecera() {
		pagina+=1;
    	tCabecera = new Table(new float[]{165, 190, 165});
        tCabecera.setWidth(PageSize.A4.getWidth()-60);
/**/
//        cell = new Cell(1,2)
//        		.add(new Paragraph(lEmpresa.get(0).getRazsoc()).setTextAlignment(TextAlignment.LEFT))
//        		.setFont(fontContenido)
//        		.setFontSize(8);
//        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE);
//        tCabecera.addCell(cell);

        String dato = "";
        cell = new Cell()
        		.add(new Paragraph(dato).setTextAlignment(TextAlignment.RIGHT))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE);
        tCabecera.addCell(cell);
/**/
        dato = "Pag:" + pagina;
//        if (fecini!=null && fecfin!=null)
//        	dato = "Kardex de " + SistemaUtil.fechaBeanVista(fecini) + " a " + SistemaUtil.fechaBeanVista(fecfin);
//        else 
//        	dato = "Kardex";
        cell = new Cell(1,2)
        		.add(new Paragraph(dato).setTextAlignment(TextAlignment.RIGHT))
        		.setFont(fontContenido)
        		.setFontSize(8);
        bordes(cell,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE,ColorConstants.WHITE);
        tCabecera.addCell(cell);
        documento.add(tCabecera);
        
        linea = 3;
	}
	
	private void plantilla() throws IOException {
        fontContenido = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
	}
	
    private void bordes(Cell cell, Color arriba, Color abajo, Color izquierda, Color derecha) {
//      cell.setUseVariableBorders(true);
      cell.setBorderTop(new SolidBorder(arriba , 1));
      cell.setBorderBottom(new SolidBorder(abajo , 1));
      cell.setBorderLeft(new SolidBorder(izquierda , 1));
      cell.setBorderRight(new SolidBorder(derecha , 1));
  }
	
}
