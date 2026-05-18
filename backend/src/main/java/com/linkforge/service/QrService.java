package com.linkforge.service;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class QrService {
  public byte[] png(String text, int size) throws Exception {
    var matrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size);
    var out = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(matrix, "PNG", out);
    return out.toByteArray();
  }
}
