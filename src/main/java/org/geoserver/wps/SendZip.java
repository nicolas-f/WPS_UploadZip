package org.geoserver.wps;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class SendZip {
    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("Need the zip path as argument");
            return;
        }
        try {
            //Create the connection
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/geoserver/wps").openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            String userpasswd = "admin"+":"+"geoserver";
            String basicAuth = new Base64().encode(userpasswd.getBytes());
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("Content-Type", "text/xml");

            //Write in a xml the zip file to send
            String xml = "";
            xml += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">\n" +
                    "\n" +
                    "  <ows:Identifier>gs:UploadZIP</ows:Identifier>\n" +
                    "  <wps:DataInputs>\n" +
                    "    <wps:Input>\n" +
                    "      <ows:Identifier>fileName</ows:Identifier>\n" +
                    "      <wps:Data>\n" +
                    "        <wps:LiteralData>";
            File zip = new File(args[0]);
            xml += zip.getName();
            xml += "</wps:LiteralData>\n" +
                    "      </wps:Data>\n" +
                    "    </wps:Input>\n" +
                    "    <wps:Input>\n" +
                    "      <ows:Identifier>encode64ZIP</ows:Identifier>\n" +
                    "      <wps:Data>\n" +
                    "        <wps:LiteralData>";
            byte[] b = Files.readAllBytes(zip.toPath());
            xml+=Base64.encode(b);
            xml += "</wps:LiteralData>\n" +
                    "      </wps:Data>\n" +
                    "    </wps:Input>\n" +
                    "  </wps:DataInputs>\n" +
                    "  <wps:ResponseForm>\n" +
                    "    <wps:RawDataOutput>\n" +
                    "      <ows:Identifier>result</ows:Identifier>\n" +
                    "    </wps:RawDataOutput>\n" +
                    "  </wps:ResponseForm>\n" +
                    "</wps:Execute>";

            connection.setRequestProperty("Content-Length", "" + Integer.toString(xml.getBytes().length));

            //Write the xml
            OutputStream stream = connection.getOutputStream();
            stream.write(xml.getBytes());
            stream.close();

            //Display the result
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line=rd.readLine())!= null){
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
