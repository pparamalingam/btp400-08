package Provider.GoogleMapsStatic;

import Task.Support.CoreSupport.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

import java.io.*;
import java.net.URLEncoder;

/**
 * MapLookup
 * <p/>
 * http://code.google.com/apis/maps/documentation/staticmaps/index.html
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Apr 16, 2008, 10:55:50 PM
 */
/**
 * @author Preshoth
 * @version 2.0
 * @since March 15 2012
 */

/**
 * @author Preshoth
 * @description
 * Added XML uris to parse data for paths and latitude/longitude
 */

public class MapLookup {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constants
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static final String GmapStaticURI = "http://maps.google.com/staticmap";
public static final String GmapStatApiUri = "http://maps.google.com/maps/api/staticmap";
public static final String GmapStatXmlUri = "http://maps.googleapis.com/maps/api/geocode/xml?address=";
public static final String GmapDirecXmlUri = "http://maps.googleapis.com/maps/api/directions/xml?";
public static final String GmapLicenseKey = "key";

public static final String CenterKey = "center";

public static final String ZoomKey = "zoom";
public static final int ZoomMax = 19;
public static final int ZoomMin = 0;
public static final int ZoomDefault = 10;

public static final String SizeKey = "size";
public static final String SizeSeparator = "x";
public static final int SizeMin = 10;
public static final int SizeMax = 512;
public static final int SizeDefault = SizeMax;

public static final String MarkerSeparator = "%7C"; //%7C is the URL-Safe encoding for "|" pipe character
public static final String MarkersKey = "markers";


//******Path declaration vars go here *********
//&path=rgba:0xff000099,weight:6|37.39561,-122.08952|37.39125,-122.07064&zoom=13 (format)


//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static final MapLookup _map = new MapLookup();
public static String GmapLicense = "";

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// set the license key
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static void setLicenseKey(String lic) {
  GmapLicense = lic;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static String getMap(double lat, double lon) {
  return getMap(lat, lon, SizeMax, SizeMax);
}

public static String getMap(double lat, double lon, int sizeW, int sizeH) {
  return getMap(lat, lon, sizeW, sizeH, ZoomDefault);
}

public static String getMap(double lat, double lon, int sizeW, int sizeH, int zoom) {
  return _map.getURI(lat, lon, sizeW, sizeH, zoom);
}

/**
 * New method that allows for paths and marker to interact with each other by getting XML data from SampleApp.java
 * @param lat
 * @param lon
 * @param sizeW
 * @param sizeH
 * @param zoom
 * @param path
 * @param markers
 * @return mapURi
 * @throws UnsupportedEncodingException
 */
public static String getMap(double lat, double lon, int sizeW, int sizeH, int zoom, String path, MapMarker... markers) throws UnsupportedEncodingException {
	  return _map.getURI(lat, lon, sizeW, sizeH, zoom, path, markers);
	}

/**
 * New Method that allows for map to be generated from address string instead of lat/lon values
 * @param address
 * @param city
 * @param state
 * @param sizeW
 * @param sizeH
 * @param zoom
 * @param markers
 * @return mapURI
 */
public static String getMap(String address, String city, String state, int sizeW, int sizeH, int zoom, MapMarker... markers) {
	  return _map.getURI(address, city,state, sizeW, sizeH, zoom, markers);
	}

public static String getMap(double lat, double lon, int sizeW, int sizeH, MapMarker... markers) {
  return _map.getURI(lat, lon, sizeW, sizeH, markers);
}

public static String getMap(String address){
	return _map.getURI(address);
}
/**
 * XML uri for Google Direction API, used to parse for encoded path
 * @param latA
 * @param lonA
 * @param latB
 * @param lonB
 * @return
 */
public static String getMap(double latA, double lonA, double latB, double lonB){
	return _map.getURI(latA,lonA,  latB,  lonB);
}

/**
 * XML uri for Google Geocode API, used to parse for lon/lat value of closest match
 * @param lat
 * @param lon
 * @param markers
 * @return
 */
public static String getMap(double lat, double lon, MapMarker... markers) {
  return getMap(lat, lon, SizeMax, SizeMax, markers);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// param handling and uri generation
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public String getURI(double lat, double lon, int sizeW, int sizeH, MapMarker... markers) {
  _validateParams(sizeW, sizeH, ZoomDefault);

  // generate the URI
  StringBuilder sb = new StringBuilder();
  sb.append(GmapStaticURI);

  // size key
  sb.
      append("?").
      append(SizeKey).append("=").append(sizeW).append(SizeSeparator).append(sizeH);

  // markers key
  sb.
      append("&").
      append(MarkerUtils.toString(markers));

  // maps key
  sb.
      append("&").
      append(GmapLicenseKey).append("=").append(GmapLicense);


  return sb.toString();
}

public String getURI(double lat, double lon, int sizeW, int sizeH, int zoom) {
  _validateParams(sizeW, sizeH, zoom);

  // generate the URI
  StringBuilder sb = new StringBuilder();
  sb.append(GmapStaticURI);

  // center key
  sb.
      append("?").
      append(CenterKey).append("=").append(lat).append(",").append(lon);

  // zoom key
  sb.
      append("&").
      append(ZoomKey).append("=").append(zoom);

  // size key
  sb.
      append("&").
      append(SizeKey).append("=").append(sizeW).append(SizeSeparator).append(sizeH);

  // markers key
  sb.
      append("&").
      append(MarkerUtils.toString(new MapMarker(lat, lon)));

  // maps key
  sb.
      append("&").
      append(GmapLicenseKey).append("=").append(GmapLicense);

  return sb.toString();
}

public String getURI(double lat, double lon, int sizeW, int sizeH, int zoom, String path, MapMarker... markers) throws UnsupportedEncodingException {
	  _validateParams(sizeW, sizeH, zoom);
	  
	  String encodedPath = URLEncoder.encode(path, "UTF-8");
		  	
	  // generate the URI
	  StringBuilder sb = new StringBuilder();
	  sb.append(GmapStatApiUri);

	  // center key
	  sb.
	      append("?").
	      append(CenterKey).append("=").append(lat).append(",").append(lon);

	  // zoom key
	  sb.
	      append("&").
	      append(ZoomKey).append("=").append(zoom);

	  // size key
	  sb.
	      append("&").
	      append(SizeKey).append("=").append(sizeW).append(SizeSeparator).append(sizeH);

	  // markers key
	  sb.
	      append("&").
	      append(MarkerUtils.toString(markers));

	  //encoded path
	  sb.
	  	append("&path=weight:4%7Ccolor:blue%7Cenc:").
	  	append(encodedPath); 
	  
	  // sensor
	  sb.
	      append("&sensor=false");

	  return sb.toString();
	}

public String getURI(String address, String city, String state, int sizeW, int sizeH, int zoom, MapMarker... markers) {
	  _validateParams(sizeW, sizeH, zoom);
	  
	   city= city.replaceAll("\\s+", "+");
	   state= state.replaceAll("\\s+", "+");
	   address= address.replaceAll("\\s+", "+");
	   
	  // generate the URI
	  StringBuilder sb = new StringBuilder();
	  sb.append(GmapStatApiUri);

	  // center key
	  sb.
	      append("?").
	      append(CenterKey).append("=").append(address).append(",").append(city).append(",").append(state);

	  // zoom key
	  sb.
	      append("&").
	      append(ZoomKey).append("=").append(zoom);

	  // size key
	  sb.
	      append("&").
	      append(SizeKey).append("=").append(sizeW).append(SizeSeparator).append(sizeH);

	  // markers key
	  sb.
	      append("&").
	      append(MarkerUtils.toString(markers));

	  // maps key
	  sb.
	      append("&").
	      append(GmapLicenseKey).append("=").append(GmapLicense);

	  return sb.toString();
	}

//XML URI Handle
public String getURI(String address) {
	  //_validateParams(sizeW, sizeH, zoom);
	  
	   address= address.replaceAll("\\s+", "+");
	   
	  // generate the URI
	  StringBuilder sb = new StringBuilder();
	  sb.append(GmapStatXmlUri);

	  // Address Line
	  sb. 
	      append(address);

	  // Sensor Line
	  sb.
	      append("&sensor=false");

	  return sb.toString();
	}

public String getURI(double latA, double lonA, double latB, double lonB) {
	  

	  // generate the URI
	  StringBuilder sb = new StringBuilder();
	  sb.append(GmapDirecXmlUri);

	  // origin key
	  sb.
	      append("origin").
	     append("=").append(latA).append(",").append(lonA);

	  // destination key
	  sb.
	      append("&destination").
	      append("=").append(latB).append(",").append(lonB);
	  
	  //Sensor Line
	  sb.
	  	append("&sensor=false");
	  return sb.toString();
	}
private void _validateParams(int sizeW, int sizeH, int zoom) {
  if (zoom < ZoomMin || zoom > ZoomMax)
    throw new IllegalArgumentException("zoom value is out of range [" + ZoomMin + "-" + ZoomMax + "]");

  if (sizeW < SizeMin || sizeW > SizeMax)
    throw new IllegalArgumentException("width is out of range [" + SizeMin + "-" + SizeMax + "]");

  if (sizeH < SizeMin || sizeH > SizeMax)
    throw new IllegalArgumentException("height is out of range [" + SizeMin + "-" + SizeMax + "]");
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// actually get the map from Google
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/** use httpclient to get the data */
public static ByteBuffer getDataFromURI(String uri) throws IOException {

  GetMethod get = new GetMethod(uri);

  try {
    new HttpClient().executeMethod(get);
    return new ByteBuffer(get.getResponseBodyAsStream());
  }
  finally {
    get.releaseConnection();
  }

}


/** markers=40.702147,-74.015794,blues|40.711614,-74.012318,greeng&key=MAPS_API_KEY */
public static class MarkerUtils {
  public static String toString(MapMarker... markers) {
    if (markers.length > 0) {
      StringBuilder sb = new StringBuilder();

      sb.append(MarkersKey).append("=");

      for (int i = 0; i < markers.length; i++) {
        sb.append(markers[i].toString());
        if (i != markers.length - 1) sb.append(MarkerSeparator);
      }

      return sb.toString();
    }
    else {
      return "";
    }
  }
}// class MarkerUtils

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// self test method
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static void main(String[] args) {

  // make sure to set a valid license key
  setLicenseKey("");

  double lat = 38.931099;
  double lon = -77.3489;

  double lat1 = 40.742100;
  double lon1 = -74.001801;

  String u1 = getMap(lat, lon);
  System.out.println(u1);

  String u2 = getMap(lat, lon, 256, 256);
  System.out.println(u2);

  String u3 = getMap(lat, lon, new MapMarker(lat, lon, MapMarker.MarkerColor.blue, 'a'));
  System.out.println(u3);

  String u4 = getMap(lat, lon,
                     250, 500,
                     new MapMarker(lat, lon, MapMarker.MarkerColor.green, 'v'),
                     new MapMarker(lat1, lon1, MapMarker.MarkerColor.red, 'n')
  );
  System.out.println(u4);

}

}//end class MapLookup
