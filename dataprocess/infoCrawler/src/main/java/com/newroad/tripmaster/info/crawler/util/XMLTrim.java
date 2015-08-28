package com.newroad.tripmaster.info.crawler.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLTrim {
  DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
  public List<Float> latList = new ArrayList<Float>();
  public List<Float> lngList = new ArrayList<Float>();

  // Load and parse XML file into DOM
  public Document parse(String filePath) {
    Document document = null;
    try {
      // DOM parser instance
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      // parse an XML file into a DOM tree
      document = builder.parse(new File(filePath));
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return document;
  }


  public void doFilter() {
    Document document = parse("/Users/niuniu422/Downloads/content.xml");
    // get root element
    Element rootElement = document.getDocumentElement();


    NodeList nodeList = rootElement.getElementsByTagName("point");
    if (nodeList != null) {
      for (int i = 0; i < nodeList.getLength(); i++) {
        Element element = (Element) nodeList.item(i);
        String x = element.getAttribute("x");
        String y = element.getAttribute("y");


        System.out.println("x is:" + x + "    y is:" + y);
        System.out.println("title: " + element.getElementsByTagName("title").item(0).getFirstChild().getNodeValue());
        System.out.println("latitude: " + element.getElementsByTagName("latitude").item(0).getFirstChild().getNodeValue());
        System.out.println("longitude: " + element.getElementsByTagName("longitude").item(0).getFirstChild().getNodeValue());
        System.out.println();

        latList.add(Float.parseFloat(element.getElementsByTagName("latitude").item(0).getFirstChild().getNodeValue()));
        lngList.add(Float.parseFloat(element.getElementsByTagName("longitude").item(0).getFirstChild().getNodeValue()));
      }
    }
  }

  public static void main(String[] args) {

  }
}
