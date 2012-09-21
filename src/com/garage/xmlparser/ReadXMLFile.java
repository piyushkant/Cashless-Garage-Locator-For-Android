package com.garage.xmlparser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class ReadXMLFile {

	public void parseXml(ArrayList<String> name, ArrayList<String> cashless,
			ArrayList<String> manufacturer, ArrayList<String> street,
			ArrayList<String> city, ArrayList<String> pincode,
			ArrayList<String> state, ArrayList<String> contact,
			ArrayList<String> landline, ArrayList<String> mobile,
			ArrayList<String> email, Document doc) {
		try {

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			NodeList garageList = doc.getElementsByTagName("Garage");
			NodeList addressList = doc.getElementsByTagName("Address");
			NodeList contactList = doc.getElementsByTagName("ContactDetails");

			System.out.println("-----------------------");

			for (int temp = 0; temp < garageList.getLength(); temp++) {

				Node garageNode = garageList.item(temp);
				Element garageElement = (Element) garageNode;
				name.add(garageElement.getAttribute("Name"));
				cashless.add(garageElement.getAttribute("Cashless"));
				manufacturer.add(garageElement.getAttribute("Manufacturer"));

				Node addressNode = addressList.item(temp);
				Element addressElement = (Element) addressNode;
				street.add(addressElement.getAttribute("Street"));
				city.add(addressElement.getAttribute("City"));
				pincode.add(addressElement.getAttribute("PinCode"));
				state.add(addressElement.getAttribute("State"));

				Node contactNode = contactList.item(temp);
				Element contactElement = (Element) contactNode;
				contact.add(contactElement.getAttribute("ContactPerson"));
				landline.add(contactElement.getAttribute("Landline"));
				mobile.add(contactElement.getAttribute("Mobile"));
				email.add(contactElement.getAttribute("EMail"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final static Document XMLfromString(String xml) {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}

		return doc;

	}

	public static String getXML(String url) {
		String line = null;

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			line = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (MalformedURLException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (IOException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		}

		return line;
	}
}