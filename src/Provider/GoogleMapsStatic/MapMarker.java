package Provider.GoogleMapsStatic;

import java.io.*;
import java.util.regex.*;

/**
 * {latitude} (required) specifies a latitudinal value with precision to 6 decimal places.
 * {longitude} (required) specifies a longitudinal value with precision to 6 decimal places.
 * {color} (optional) specifies a color from the set {red,blue,green}.
 * {alpha-character} (optional) specifies a single lowercase alphabetic character from the set {a-z}.
 * <p/>
 * An example marker declaration is of the form {latitude},{longitude},{color}{alpha-character}. Note
 * in particular that the color and alpha-character values of the string are not separated by a comma.
 * A sample marker declaration is shown below.
 * <p/>
 * markers=40.702147,-74.015794,blues|40.711614,-74.012318,greeng&key=MAPS_API_KEY
 */

// MapMarker adjusted to match Google API standard of marker implementation
public class MapMarker implements Serializable {
static final long serialVersionUID = 5805831996822361347L;
public static final String MarkerSeparator = "%7C";

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// enum for marker colors
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public enum MarkerColor {
  red, green, blue
}// enum MarkerColor

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private char _alpha = '1';
private MarkerColor _color = null;
private double _lat = -1;
private double _lon = -1;


//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructor
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public MapMarker(double lat, double lon, MarkerColor color, char alpha) {
  _lat = lat;
  _lon = lon;
  _color = color;
  _alpha = alpha;

  StringBuffer buf = new StringBuffer();
  buf.append(alpha);
  if (!Pattern.matches("[a-zA-Z]", buf))
    throw new IllegalArgumentException("marker alpha is not a char between a-z");

  if (color == null) throw new IllegalArgumentException("marker color can not be null");
}

public MapMarker(double lat, double lon) {
  _lat = lat;
  _lon = lon;
}
public double getLat() { return _lat; }
public double getLon() { return _lon; }
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// generate Google Maps uri
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public String toString() {
  StringBuilder sb = new StringBuilder();

  sb.append("&markers=");

  if (_color != null && _alpha != '1') {
    sb.
        append("color:").
        append(_color.toString()).
        append(MarkerSeparator).
        append("label:").
        append(_alpha);
  }
  sb.append(MarkerSeparator).append(_lat).append(",").append(_lon);
  return sb.toString();
}

}// class Marker
