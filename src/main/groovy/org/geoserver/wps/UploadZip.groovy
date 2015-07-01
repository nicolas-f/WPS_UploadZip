package org.geoserver.wps

import org.eclipse.emf.ecore.xml.type.internal.DataValue;


title = 'UploadZip'
description = 'Upload a zip file'

inputs = [
        fileName: [name: 'fileName', title: 'fileName', type: String.class],
        encode64ZIP: [name: 'encode64ZIP', title: 'encode64ZIP', type: String.class]
]

def run(input) {
    byte[] data = DataValue.Base64.decode(input.encode64ZIP)
    File file = new File(input.fileName)
    FileOutputStream outputStream = new FileOutputStream(file)
    outputStream.write(data)
}
