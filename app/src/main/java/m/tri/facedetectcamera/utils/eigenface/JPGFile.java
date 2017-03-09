package m.tri.facedetectcamera.utils.eigenface;/*
 * Copyright (c) 2002 by Konrad Rzeszutek
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * JPG File reader/writer. Uses native com.sun libraries (which
 * may deprecate at any time)
 *
 *
 * @author Konrad Rzeszutek
 * @version 1.0
 */
public class JPGFile implements xxxFile {

    private byte bytes[]=null;      // bytes which make up binary PPM image
    private double doubles[] = null;
    private String filename = null;     // filename for PPM image
    private int height = 0;
    private int width = 0;
    /**
     * Read the PPM File.
     *
     * @throws FileNotFoundException   if the directory/image specified is wrong
     * @throws IOException  if there are problems reading the file.
     */
    public JPGFile(String filename)  throws FileNotFoundException, IOException {
        this.filename = filename;
        readImage();
    }
    /**
     * Get the height of the PPM image.
     *
     * @return the height of the image.
     */
    public int getHeight() {
        return height;
    }
    /**
     * Get the width of the PPM image.
     *
     * @return  the width of the image.
     */
    public int getWidth() {
        return width;
    }
    /**
     * Get the data as byte array. Data is of any type that
     * has been read from the file (usually 8bit RGB)
     *
     * @return  The data of the image.
     */
    public byte[] getBytes() {
        return bytes;
    }
    /**
     * Get the data as double array. Data is of any type that
     * has been read from the file (usually 8bit RGB put into an 64bit double)
     *
     * @return  The data of the image.
     */
    public double[] getDouble() {
        return doubles;
    }

    /**
     * Write to <code>fn</code> file the <code>data</code> using the
     * <code>width, height</code> variables. Data is assumed to be 8bit RGB.
     *
     * @throws FileNotFoundException   if the directory/image specified is wrong
     * @throws IOException  if there are problems reading the file.
     */
    public static void writeImage(String fn, byte[] data, int width, int height)
            throws FileNotFoundException, IOException {

        FileOutputStream fOut = new FileOutputStream(fn);
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        try {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int ints[] = new int[data.length];
        for (int i = 0; i< data.length; i++)
            ints[i] = 255 << 24 |
                    (int) (data[i] & 0xff) << 16 |
                    (int) (data[i] & 0xff) << 8 |
                    (int) (data[i] & 0xff);

        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
        image.setPixels(ints, 0, width, 0, 0, width, height);

        //bmp.encode(image);
        fOut.close();
    }
    /**
     * Read the image from the specified file.
     *
     * @throws FileNotFoundException pretty obvious
     * @throws IOException filesystem related problems
     */
    private void readImage()  throws FileNotFoundException, IOException {

        Bitmap jpeg_decode = BitmapFactory.decodeFile(filename);

        width = jpeg_decode.getWidth();
        height = jpeg_decode.getHeight();

        int[] rgbdata = new int[width * height];
        jpeg_decode.getPixels(rgbdata, 0, width, 0, 0, width, height);

        bytes = new byte[rgbdata.length];
        doubles = new double[rgbdata.length];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i]    = (byte)  (rgbdata[i] & 0xFF);
            doubles[i]  = (double)(rgbdata[i]);
        }
    }
}