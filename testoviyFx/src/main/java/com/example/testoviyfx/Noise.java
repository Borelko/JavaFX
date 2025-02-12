package com.example.testoviyfx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Noise {
    protected static void addNoise(BufferedImage img, int percent) {

        int r, g, b, p, color, r_znak, g_znak, b_znak;
        for (int i = 0; i < img.getWidth() - 10; i += 10) {
            for (int j = 0; j < img.getHeight() - 50; j += 10) {


                color = percent;
                color *= Math.round(Math.random());
                r_znak = (int) Math.round(Math.random());
                g_znak = (int) Math.round(Math.random());
                b_znak = (int) Math.round(Math.random());

                for (int plusi = 0; plusi < 10; plusi++) {
                    for (int plusj = 0; plusj < 10; plusj++) {
                        p = img.getRGB(i + plusi, j + plusj);
                        r = (p >> 16) & 0xff;
                        g = (p >> 8) & 0xff;
                        b = p & 0xff;

                        if (r_znak == 1)
                            r += color;
                        else
                            r -= color;
                        if (g_znak == 1)
                            g += color;
                        else
                            g -= color;
                        if (b_znak == 1)
                            b += color;
                        else
                            g -= color;

                        if (r > 255) {
                            r -= 2 * color;
                        }

                        if (g > 255) {
                            g -= 2 * color;
                        }

                        if (b > 255) {
                            b -= 2 * color;
                        }
                        if (r < 0) {
                            r += 2 * color;
                        }
                        if (g < 0) {
                            g += 2 * color;
                        }
                        if (b < 0) {
                            b += 2 * color;
                        }
                        p = (r << 16) | (g << 8) | b;
                        img.setRGB((i + plusi), (j + plusj), p);

                    }
                }
            }
        }
    }

    protected static void writeObjBoxes (BufferedImage img, File txt_fie) throws ParserConfigurationException, IOException, SAXException {
        NodeList list = GET_NODELIST_FROM_XML(txt_fie);
        System.out.println("Writing File: " + txt_fie);
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node.getChildNodes();
                String clas = element.getElementsByTagName("name").item(0).getTextContent();
                String xmin = element.getElementsByTagName("xmin").item(0).getTextContent();
                String ymin = element.getElementsByTagName("ymin").item(0).getTextContent();
                String xmax = element.getElementsByTagName("xmax").item(0).getTextContent();
                String ymax = element.getElementsByTagName("ymax").item(0).getTextContent();

                writeBox(img, clas, Integer.parseInt(xmin),Integer.parseInt(ymin),Integer.parseInt(xmax),Integer.parseInt(ymax));
            }
        }
        System.out.println("Ended");
    }

    protected static void writeBox(BufferedImage img, String clas, int x1, int y1, int x2, int y2) {
        int p;
        Font font = new Font("Consolas", Font.TRUETYPE_FONT, 10);

        Graphics g = img.getGraphics();
        g.setFont(font);
        if (((x2 - x1) < 10) || ((y2 - y1) < 10)) {
            g.setColor(Color.RED);
            p = (255 << 16);
        } else {
            g.setColor(Color.GREEN);
            p = (255 << 8);
        }

        g.drawString(clas, x1, y1);
        //System.out.println(clas);
        if (x1 < 0) x1 = 0;
        if (y1 < 0) y1 = 0;
        if (x2 >= img.getWidth()) x2 = img.getWidth() - 1;
        if (y2 >= img.getHeight()) y2 = img.getHeight() - 1;

        for (int i = 0; i < (x2 - x1) + 1; i++) {

            img.setRGB(x1 + i, y1, p);

            //System.out.println(" x1+i = " + (x1 + i) + " y2 = " + y2);
            img.setRGB(x1 + i, y2, p);
        }
        for (int i = 0; i < (y2 - y1) + 1; i++) {

            img.setRGB(x1, y1 + i, p);
            img.setRGB(x2, y1 + i, p);
        }
    }

    public static NodeList GET_NODELIST_FROM_XML(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(String.valueOf(file)));
        doc.getDocumentElement().normalize();
        return doc.getElementsByTagName("object");
    }
}
