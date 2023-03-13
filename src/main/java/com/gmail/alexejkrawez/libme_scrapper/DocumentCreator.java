package com.gmail.alexejkrawez.libme_scrapper;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Parts;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.FontTablePart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.Br;
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
import org.docx4j.wml.STBrType;
import org.docx4j.wml.Text;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.slf4j.LoggerFactory.getLogger;

public class DocumentCreator {

    private static final Logger log = getLogger(DocumentCreator.class);
    private static WordprocessingMLPackage word;
    private static Parts parts;
    private static MainDocumentPart document;
    private static ObjectFactory factory = Context.getWmlObjectFactory();
    private static FontTablePart fonts;
    private static StyleDefinitionsPart styles;
    private static P paragraph;
    private static PPr paragraphProperties;
    private static PPr paragraphImageProperties;
    private static PPr chapterProperties;
    private static PPrBase.PStyle paragraphStyle;
    private static PPrBase.PStyle chapterStyle;
    private static R wrapper;
    private static Text text;
    private static RPr propertiesText;
    private static Drawing drawing;
    private static Color color;
    private static int id1 = 1;
    private static int id2 = 2;
    private static int count = 0;

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
        // mainDocumentPart > paragraph + paragraphProperties > wrapper > text + drawing + (propertiesText > color + B + I + U + caps)

        paragraphProperties = factory.createPPr();
        paragraphImageProperties = factory.createPPr();
//        styles.getStyleById("Title").setPPr(paragraphProperties);
//        document.getPropertyResolver().activateStyle("Normal");
        paragraphStyle = factory.createPPrBasePStyle();
        paragraphStyle.setVal("Normal");
        paragraphProperties.setPStyle(paragraphStyle);
        Jc justificationLeft = factory.createJc();
        justificationLeft.setVal(JcEnumeration.LEFT);
        paragraphProperties.setJc(justificationLeft);
        Jc justificationCenter = factory.createJc();
        justificationCenter.setVal(JcEnumeration.CENTER);
        paragraphImageProperties.setJc(justificationCenter);

        chapterProperties = factory.createPPr();;
        chapterStyle = factory.createPPrBasePStyle();
        chapterStyle.setVal("Heading1");
        chapterProperties.setPStyle(chapterStyle);
//        paragraph.setPPr(paragraphProperties);

//        wrapper = factory.createR();
//        text = factory.createText();
//        drawing = factory.createDrawing();
//        propertiesText = factory.createRPr();
//        color = factory.createColor();
    }

    protected boolean saveDocument(String pathToSave, String name) {
        File file = new File(pathToSave + File.separator + name + ".docx");
        try {
            word.save(file);
            log.info("Saved successfully");
            return true;
        } catch (Docx4JException e) {
            log.error(e.getLocalizedMessage());
        }
        return false;
    }

    protected void addTextParagraph(String source) {
        P paragraph = factory.createP();
        paragraph.setPPr(paragraphProperties);
        R wrapper = factory.createR();
        Text text = factory.createText();

        text.setValue(source);
        wrapper.getContent().add(text);
        paragraph.getContent().add(wrapper);
        document.getContent().add(paragraph);
        log.info("paragraphs count " + ++count);
    }

    protected void addImgParagraph(String source) {
        Inline image;

        try {
            image = createImg(source);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return;
        }

        P paragraph = factory.createP();
        paragraph.setPPr(paragraphImageProperties);
        R wrapper = factory.createR();
        Drawing drawing = factory.createDrawing();

        drawing.getAnchorOrInline().add(image); //TODO: все списки трим-ту-сайзнуть
        wrapper.getContent().add(drawing);
        paragraph.getContent().add(wrapper);
        document.getContent().add(paragraph);
        log.info("paragraphs count " + ++count);
    }

    private Inline createImg(String source) throws Exception {
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
        Inline image = imagePart
                .createImageInline(String.valueOf(id1), url.toString(), id1, id2, false, 10000);
        id1 += 2;
        id2 += 2;

        return image;
    }

    protected void addChapterName(String name) {
        P paragraph = factory.createP();
        paragraph.setPPr(chapterProperties);
        R wrapper = factory.createR();
        Text text = factory.createText();

        text.setValue(name);
        wrapper.getContent().add(text);
        paragraph.getContent().add(wrapper);
        document.getContent().add(paragraph);
    }

    protected void addPageBreak() {
        P paragraph = factory.createP();
        paragraph.setPPr(paragraphProperties);
        R wrapper = factory.createR();
        Br br = factory.createBr();
        br.setType(STBrType.PAGE);

        wrapper.getContent().add(br);
        paragraph.getContent().add(wrapper);
        document.getContent().add(paragraph);
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
