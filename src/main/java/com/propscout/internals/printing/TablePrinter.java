package com.propscout.internals.printing;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TablePrinter extends BasePrinter {

    public TablePrinter(String destination) throws FileNotFoundException {
        super(destination);
    }

    public void addTitle(String title) throws IOException {

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        addBlockElement(new Paragraph(title).setFontSize(24.0F).setFont(font));

    }

    @Override
    public void initDocument(int margins, PageSize pageSize) {

        document = new Document(pdfDocument, pageSize);

    }
}
