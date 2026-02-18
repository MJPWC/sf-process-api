package plantuml;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class PlantUMLEncoder {

    private static final String CHAR_MAP = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";

        /**
         * Returns the PlantUML server image URL for a given PlantUML script.
         */
        public static String getImageUrl(String plantUmlScript) throws Exception {
            String encoded = encode(plantUmlScript);
            return "http://www.plantuml.com/plantuml/png/" + encoded;
        }

    public static String encode(String text) throws Exception {
        byte[] deflated = compress(text.getBytes("UTF-8"));
        return encode6bit(deflated);
    }

    private static byte[] compress(byte[] data) throws Exception {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true); // 'true' = no zlib header (raw DEFLATE)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater);
        dos.write(data);
        dos.close();
        return baos.toByteArray();
    }

    private static String encode6bit(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < data.length) {
            int b1 = data[i++] & 0xFF;
            int b2 = (i < data.length) ? data[i++] & 0xFF : 0;
            int b3 = (i < data.length) ? data[i++] & 0xFF : 0;

            int c1 = (b1 >> 2) & 0x3F;
            int c2 = ((b1 & 0x3) << 4 | (b2 >> 4)) & 0x3F;
            int c3 = ((b2 & 0xF) << 2 | (b3 >> 6)) & 0x3F;
            int c4 = b3 & 0x3F;

            sb.append(CHAR_MAP.charAt(c1));
            sb.append(CHAR_MAP.charAt(c2));
            sb.append(CHAR_MAP.charAt(c3));
            sb.append(CHAR_MAP.charAt(c4));
        }
        return sb.toString();
    }
}
