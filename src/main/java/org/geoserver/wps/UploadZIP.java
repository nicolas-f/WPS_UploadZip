package org.geoserver.wps;

import org.eclipse.emf.ecore.xml.type.internal.DataValue;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;

import java.io.*;

@DescribeProcess(title="UploadZIP", description="WPS process for uploading a zip file to geoserver")
public class UploadZIP implements GeoServerProcess {

    @DescribeResult(name = "result", description = "output result")
    public String execute(
            @DescribeParameter(name = "fileName", description = "Name of the zip file to transfert") String fileName,
            @DescribeParameter(name = "encode64ZIP", description = "Encoded zip file") String encode64ZIP) {
        try {
            byte[] data = DataValue.Base64.decode(encode64ZIP);
            File file = new File(fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "done";
    }
}