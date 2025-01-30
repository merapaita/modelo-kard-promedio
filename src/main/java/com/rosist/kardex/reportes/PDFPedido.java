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
import com.rosist.kardex.model.Pedido;
import com.rosist.kardex.model.Iteii;
import com.rosist.kardex.model.Itepedido;

public class PDFPedido {

	private Document documento;
	private PdfDocument pdfDoc;
	private PdfWriter pdfWriter = null;
	private Table tCabecera;
	private Table tDetalle;
	private Table tTitulo;
	private Table tPie;
	private Cell cell;
	private PdfFont fontContenido;
	private int linea;
	private int pagina;
	private double tTotal;

	static final Logger logger = LoggerFactory.getLogger(PDFPedido.class);

	Pedido pedido;

	public PDFPedido(Map<String, Object> parametros) {
		pedido = (Pedido) parametros.get("pedido");
	}

	public byte[] creaPedido() throws IOException {
//		logger.info("creaPecosa");
		try {
			fontContenido = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//		String fondo = classloader.getResource("fondo.png").getFile();

//		Image imagen = new Image(ImageDataFactory.create(fondo));  
//		Map<String, Object> parametros = new HashMap<String, Object>();

		ByteArrayOutputStream docBytes = new ByteArrayOutputStream();
		pdfWriter = new PdfWriter(docBytes);

		pdfDoc = new PdfDocument(pdfWriter);
		documento = new Document(pdfDoc, PageSize.A4); // , new PageSize(envelope)

		if (pedido != null) {
			pagina = 0;
			linea = 0;
			tTotal = 0;
//			plantilla(); // writer
//			logger.info("cabecera" + lIteii);
			cabecera();
//			logger.info("cabecera" + lItepec);
			if (pedido.getDetpedido() != null) { // pedido.getDetii()
				for (Itepedido itepedido : pedido.getDetpedido()) { // pedido.getDetii()
//					logger.info("items");
					detalle(itepedido);
				}
			}
			pie();
		} else {
			Paragraph mensaje = new Paragraph("No existe informacion para procesar");
			mensaje.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));
			mensaje.setFontSize(18f);
			mensaje.setItalic();
			documento.add(mensaje);
		}

		documento.close();

		return docBytes.toByteArray();
	}

	private void pie() {
		logger.info("imprimiendo pie.");
		tPie = new Table(new float[] { 35, 55, 170, 40, 40, 45, 45, 45, 45 });
		tPie.setWidth(PageSize.A4.getWidth() - 60);
		
		cell = new Cell(1, 8)
				.add(new Paragraph("TOTAL").setTextAlignment(TextAlignment.LEFT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.BLACK, ColorConstants.BLACK, ColorConstants.BLACK, ColorConstants.BLACK);
		tPie.addCell(cell);
		
		cell = new Cell()
				.add(new Paragraph(String.format("%.4f", tTotal)).setTextAlignment(TextAlignment.RIGHT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.BLACK, ColorConstants.BLACK, ColorConstants.BLACK, ColorConstants.BLACK);
		tPie.addCell(cell);
		documento.add(tPie);
	}

	private void cabecera() {
		cell = null;
		tCabecera = new Table(new float[] { 200, 200, 20 });
		tCabecera.setWidth(PageSize.A4.getWidth() - 60);

		/**/
		cell = new Cell(1, 2)
				.add(new Paragraph("SOCIEDAD DE BENEFICENCIA PUBLICA DE PIURA").setTextAlignment(TextAlignment.LEFT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);

		String cPagina = "Pag.: " + String.format("%1$010d", ++pagina);
		cell = new Cell().add(new Paragraph(cPagina).setTextAlignment(TextAlignment.LEFT)).setFont(fontContenido)
				.setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);
		/**/
		cell = new Cell(1, 2).add(new Paragraph("PEDIDO Nro. " + pedido.getNumped()) // pedido.getIdPecosa()
				.setTextAlignment(TextAlignment.LEFT)).setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);

		cell = new Cell().add(new Paragraph("Fecha :" + pedido.getFecha()).setTextAlignment(TextAlignment.LEFT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);
		/**/
		cell = new Cell(1, 2).add(new Paragraph("Documento :" + pedido.getDocref() + " - " + pedido.getNumref())
				.setTextAlignment(TextAlignment.LEFT)).setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);

		cell = new Cell()
				.add(new Paragraph("Fecha Referencia :" + pedido.getFecref()).setTextAlignment(TextAlignment.LEFT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);
/**/
		cell = new Cell(1, 3).add(new Paragraph("Oficina :" + " - ")
				.setTextAlignment(TextAlignment.LEFT)).setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);
		/**/
		cell = new Cell(1,3)
				.add(new Paragraph("Empleado :" + pedido.getEmpleado().getIdEmpleado() +  " - " + pedido.getEmpleado().getNombre()).setTextAlignment(TextAlignment.LEFT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);
		/**/
		cell = new Cell(1,3)
				.add(new Paragraph("Secuencia Funcional :" + " - ").setTextAlignment(TextAlignment.LEFT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);
/**/
		cell = new Cell(1,3)
				.add(new Paragraph("Fuente Financiamiento :").setTextAlignment(TextAlignment.LEFT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);
/**/
		cell = new Cell(1,3)
				.add(new Paragraph("Observaciones :" + pedido.getObserv() ).setTextAlignment(TextAlignment.LEFT))
				.setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);
/**/
		cell = new Cell(1, 3).add(new Paragraph(" :" ) // + " Valorizacion: " +
																				// pedido.getTotval()
				.setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido).setFontSize(8);
		bordes(cell, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE);
		tCabecera.addCell(cell);

/////////
		tTitulo = new Table(new float[] { 35, 55, 170, 40, 40, 45, 45, 45, 45 });
		tTitulo.setWidth(PageSize.A4.getWidth() - 60);
		
//        tTitulo.setLockedWidth(true);

		cell = new Cell().add(new Paragraph("Item").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setWidth(8).setFontSize(8);
		tTitulo.addCell(cell);

		cell = new Cell().add(new Paragraph("id Art.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
//        		.setWidth(40)
				.setFontSize(8);
		tTitulo.addCell(cell);

		cell = new Cell().add(new Paragraph("Articulo").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		tTitulo.addCell(cell);

		cell = new Cell().add(new Paragraph("Men.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		tTitulo.addCell(cell);

		cell = new Cell().add(new Paragraph("Unid.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		tTitulo.addCell(cell);

		cell = new Cell().add(new Paragraph("Cantidad.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		tTitulo.addCell(cell);

		cell = new Cell().add(new Paragraph("P.U.").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		tTitulo.addCell(cell);

		cell = new Cell().add(new Paragraph("V.U").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		tTitulo.addCell(cell);

		cell = new Cell().add(new Paragraph("Valor").setTextAlignment(TextAlignment.CENTER)).setFont(fontContenido)
				.setFontSize(8);
		tTitulo.addCell(cell);

		documento.add(tCabecera);
		documento.add(tTitulo);
		linea = 8;
	}

	private void detalle(Itepedido itepedido) {
		tDetalle = new Table(new float[] { 35, 55, 170, 40, 40, 45, 45, 45, 45 });
//    	tDetalle = new Table(new float[] { 35, 35, 75, 40, 40, 40, 40, 65, 45, 45, 30, 30});
		tDetalle.setWidth(PageSize.A4.getWidth() - 60);

		cell = new Cell()
				.add(new Paragraph(String.valueOf(itepedido.getIdItepedido())).setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido).setFontSize(8);
		tDetalle.addCell(cell);

		cell = new Cell().add(new Paragraph(itepedido.getArticulo().getCodart()).setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido).setFontSize(8);
		tDetalle.addCell(cell);

		cell = new Cell().add(new Paragraph(itepedido.getArticulo().getNomart()).setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido).setFontSize(8);
		tDetalle.addCell(cell);

		cell = new Cell().add(
				new Paragraph(String.valueOf(itepedido.getArticulo().getMenudeo())).setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido).setFontSize(8);
		tDetalle.addCell(cell);

//		cell = new Cell()
//				.add(new Paragraph(String.valueOf(itepedido.getCantidad())).setTextAlignment(TextAlignment.CENTER))
//				.setFont(fontContenido).setFontSize(8);
//		tDetalle.addCell(cell);

		cell = new Cell().add(new Paragraph(itepedido.getUnidad()).setTextAlignment(TextAlignment.CENTER))
				.setFont(fontContenido).setFontSize(8);
		tDetalle.addCell(cell);
// 		                                    String.format("%1$03d", nSerie)
//		String dato = String.format("%.4f", itepedido.getMonto());
		cell = new Cell().add(new Paragraph(String.format("%.4f", itepedido.getTotcan())).setTextAlignment(TextAlignment.RIGHT))
				.setFont(fontContenido).setFontSize(8);
		tDetalle.addCell(cell);

		cell = new Cell().add(new Paragraph(String.format("%.4f", itepedido.getPreuni())).setTextAlignment(TextAlignment.RIGHT))
				.setFont(fontContenido).setFontSize(8);
		tDetalle.addCell(cell);

		cell = new Cell().add(new Paragraph(String.format("%.4f", itepedido.getValuni())).setTextAlignment(TextAlignment.RIGHT))
				.setFont(fontContenido).setFontSize(8);
		tDetalle.addCell(cell);

		double _valor = itepedido.getMonto() - itepedido.getIgvmto();
		cell = new Cell().add(new Paragraph(String.format("%.4f", _valor)).setTextAlignment(TextAlignment.RIGHT))
				.setFont(fontContenido).setFontSize(8);
		tDetalle.addCell(cell);
		
		tTotal += _valor;
//		tTotal += itepedido.getValor();
		documento.add(tDetalle);
	}

	private void bordes(Cell cell, Color arriba, Color abajo, Color izquierda, Color derecha) {
		cell.setBorderTop(new SolidBorder(arriba, 1));
		cell.setBorderBottom(new SolidBorder(abajo, 1));
		cell.setBorderLeft(new SolidBorder(izquierda, 1));
		cell.setBorderRight(new SolidBorder(derecha, 1));
	}

}
