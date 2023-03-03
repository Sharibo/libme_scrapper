package com.gmail.alexejkrawez.site_scrapper;

import jakarta.xml.bind.JAXBException;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Parts;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.DocumentSettingsPart;
import org.docx4j.openpackaging.parts.WordprocessingML.EndnotesPart;
import org.docx4j.openpackaging.parts.WordprocessingML.FontTablePart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Color;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Styles;
import org.docx4j.wml.Text;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class DocumentCreator {

    private static final Logger log = LoggerFactory.getLogger(DocumentCreator.class);
    private static WordprocessingMLPackage word;
    private static Parts parts;
    private static MainDocumentPart document;
    private static FontTablePart fonts;
    private static StyleDefinitionsPart styles;
    private static P paragraph;
    private static PPr paragraphProperties;
    private static PPr paragraphImageProperties;
    private static PPrBase.PStyle paragraphStyle;
    private static R wrapper;
    private static Text text;
    private static RPr propertiesText;
    private static Drawing drawing;
    private static Color color;
    private int id1 = 1;
    private int id2 = 2;

//    public FontTablePart getFontTablePart();
//    public StyleDefinitionsPart getStyleDefinitionsPart();
//    public EndnotesPart getEndNotesPart();
//    public DocumentSettingsPart getDocumentSettingsPart();
//    RelationshipsPart rp = part.getRelationshipsPart();
//    factory.createBr() //Page break


    // Populate it with default styles
//    stylesPart.unmarshalDefaultStyles();

    // Add the styles part to the main document part relationships
//    wordDocumentPart.addTargetPart(stylesPart);

    protected void createDocument(String title) {
        try {
            word = WordprocessingMLPackage.createPackage();
//            fonts = new FontTablePart();
//            fonts.unmarshalDefaultFonts();
//            styles = new StyleDefinitionsPart();
//            Object o = styles.unmarshalDefaultStyles();
        } catch (InvalidFormatException e) {
            log.error(e.getLocalizedMessage());
        }

        parts = word.getParts();
//        parts.put(fonts);
//        parts.put(styles);
        document = word.getMainDocumentPart();
        document.addStyledParagraphOfText("Title", title);

        ObjectFactory factory = Context.getWmlObjectFactory();
        // mainDocumentPart > paragraph + paragraphProperties > wrapper > text + drawing + (propertiesText > color + B + I + U + caps)
        paragraph = factory.createP();
        paragraphProperties = factory.createPPr();
        paragraphImageProperties = factory.createPPr();
//        styles.getStyleById("Title").setPPr(paragraphProperties);
//        document.getPropertyResolver().activateStyle("Normal");
        paragraphStyle = factory.createPPrBasePStyle();
        paragraphStyle.setVal("Normal");
        paragraphProperties.setPStyle(paragraphStyle);
        Jc justification = factory.createJc();
        justification.setVal(JcEnumeration.LEFT);
        paragraphProperties.setJc(justification);
        justification.setVal(JcEnumeration.CENTER);
        paragraphImageProperties.setJc(justification);
//        paragraph.setPPr(paragraphProperties);

//        wrapper = factory.createR();
//        text = factory.createText();
//        drawing = factory.createDrawing();
        propertiesText = factory.createRPr();
        color = factory.createColor();
    }

    protected void saveDocument(String name) {
        String path = "E:" + File.separator;
        File file = new File(path + name + ".docx");
        try {
            word.save(file);
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addTextParagraph(String source) {
        ObjectFactory factory = Context.getWmlObjectFactory();
        P paragraph = factory.createP();
        paragraph.setPPr(paragraphProperties);
        R wrapper = factory.createR();
        Text text = factory.createText();

        text.setValue(source);
        wrapper.getContent().add(text);
        paragraph.getContent().add(wrapper);
        document.getContent().add(paragraph);
    }

    protected void createImg(String source) {

        try {
            URL url = new URL(source);
            InputStream is = url.openStream();
            ByteArrayOutputStream bus = new ByteArrayOutputStream();
            int readed;
            byte[] data = new byte[33554432]; // 32mb

            while ((readed = is.read(data, 0, data.length)) != -1) {
                bus.write(data, 0, readed);
            }

            data = bus.toByteArray();

            BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(word, data);
            log.info("url.getFile() " + url.getFile());
            log.info("url.getQuery() " + url.getQuery());
            log.info("url.getPath() " + url.getPath());
            Inline image = imagePart
                    .createImageInline(url.getFile(), url.getFile(), id1, id2, false, 10000); //TODO всё уникальное
            id1 += 2;
            id2 += 2;

            P Imageparagraph = addImgToParagraph(image);
            document.getContent().add(Imageparagraph);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }

//
////        Connection.Response resultImageResponse = Jsoup.connect(image).cookies(cookies)
////                .ignoreContentType(true).execute();
////        byte[] fileContent = new byte[0];
//        try {
//            fileContent = Files.readAllBytes(image.toPath());
//            BinaryPartAbstractImage imagePart = BinaryPartAbstractImage
//                    .createImagePart(word, fileContent);
//            Inline inline = imagePart.createImageInline(
//                    "Baeldung Image (filename hint)", "Alt Text", 1, 2, false); //TODO всё уникальное
//
//            P Imageparagraph = addImgToParagraph(inline);
//            document.getContent().add(Imageparagraph);
//        } catch (Exception e) {
//            log.error(e.getLocalizedMessage());
//        }
    }

    private P addImgToParagraph(Inline image) {
        ObjectFactory factory = Context.getWmlObjectFactory();
        P paragraph = factory.createP();
        paragraph.setPPr(paragraphImageProperties);
        R wrapper = factory.createR();
        Drawing drawing = factory.createDrawing();

        paragraph.getContent().add(wrapper);
        wrapper.getContent().add(drawing);
        drawing.getAnchorOrInline().add(image);
        return paragraph;
    }

//    private void style() {
//        text.setValue("Welcome To Baeldung");
//        wrapper.getContent().add(text);
//        paragraph.getContent().add(wrapper);
//
//        BooleanDefaultTrue b = new BooleanDefaultTrue(); //TODO ???
//        propertiesText.setB(b);
//        propertiesText.setI(b);
//        propertiesText.setCaps(b);
//        color.setVal("green");
//        propertiesText.setColor(color);
//
//        wrapper.setRPr(propertiesText);
//        document.getContent().add(paragraph); //TODO: разнести здесь всё определение и наполнение
//    }


}
