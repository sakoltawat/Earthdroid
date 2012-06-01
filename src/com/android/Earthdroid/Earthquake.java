package com.android.Earthdroid;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import android.app.Activity;


import android.location.Location;
import android.os.Bundle;



import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;

public class Earthquake extends Activity {
	ListView earthquakeListView;
    ArrayAdapter<Quake> aa;
    ArrayList<Quake> earthquakes = new ArrayList<Quake>();
	@Override
	public void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.earthquake);
		
		earthquakeListView = (ListView)this.findViewById(R.id.earthquakeListView);
		
		int layoutID = android.R.layout.simple_list_item_1;
		aa = new ArrayAdapter<Quake>(this, layoutID , earthquakes);
		earthquakeListView.setAdapter(aa);
		
		refreshEarthquakes();
	}
 private void refreshEarthquakes() {
	 URL url;
	 try {
		 String quakeFeed = getString(R.string.quake_feed);
		 url = new URL(quakeFeed);
		 
		 URLConnection connection;
		 connection = url.openConnection();
		 
		 HttpURLConnection httpConnection = (HttpURLConnection)connection;
		 int responseCode = httpConnection.getResponseCode();
		 
		 if (responseCode == HttpURLConnection.HTTP_OK) {
			 InputStream in = httpConnection.getInputStream();
			 
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			 DocumentBuilder db = dbf.newDocumentBuilder();
			 
			 Document dom = db.parse(in);
			 Element docEle = dom.getDocumentElement();
			 
			 earthquakes.clear();
			 
			 NodeList nl = docEle.getElementsByTagName("entry");
			 if (nl != null && nl.getLength() > 0) {
				 for (int i = 0 ; i < nl.getLength(); i++) {
					 Element entry = (Element)nl.item(i);
					 Element title = (Element)entry.getElementsByTagName("title").item(0);
					 Element g = (Element)entry.getElementsByTagName("georss:point").item(0);
					 Element when = (Element)entry.getElementsByTagName("updated").item(0);
					 Element link = (Element)entry.getElementsByTagName("link").item(0);
					 
					 String details = title.getFirstChild().getNodeValue();
					 String hostname = "http://earthquake.usgs.gov";
					 String linkString = hostname + link.getAttribute("href");
					 
					 String point = g.getFirstChild().getNodeValue();
					 String dt = when.getFirstChild().getNodeValue();
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
					 Date qdate = new GregorianCalendar(0,0,0).getTime();
					 try {
						 qdate = sdf.parse(dt);
					 }catch (ParseException e) {
						 e.printStackTrace();
					 }
					 
					 String[] location = point.split(" ");
					 Location l = new Location("dummyGPS");
					 l.setLatitude(Double.parseDouble(location[0]));
					 l.setLongitude(Double.parseDouble(location[1]));
					 
					 String magnitudeString = details.split(" ")[1];
					 int end = magnitudeString.length()-1;
					 double magnitude = Double.parseDouble(magnitudeString.substring(0,end));
					 
					 details = details.split(",")[1].trim();
					 
					 Quake quake = new Quake(qdate, details, l , magnitude, linkString);
					 
					 addNewQuake(quake);
				 }
			 }
		 }
	 }catch(MalformedURLException e) {
		 e.printStackTrace();
	 }catch(IOException e) {
		 e.printStackTrace();
	 }catch(ParserConfigurationException e){
		 e.printStackTrace();
	 }catch(SAXException e){
		 e.printStackTrace();
	 }
	 finally{}
 }
 private void addNewQuake(Quake _quake) {
	 earthquakes.add(_quake);
	 aa.notifyDataSetChanged();
 }
 
}

