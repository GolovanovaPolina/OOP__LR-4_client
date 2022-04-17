import javax.imageio.*;
import javax.imageio.plugins.bmp.BMPImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.Iterator;

public class CompressorClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Compressor c = (Compressor) Naming.lookup("rmi://localhost/CalculatorService");

            /* Читаем файл и конвертируем в массив байт. */
            BufferedImage image = ImageIO.read(new File("D:\\in2.bmp")); // 8-битное изображение.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "bmp", baos);
            c.SetPicture(baos.toByteArray());

            byte[] bytes = c.GetCompressedPicture();

            //BufferedImage compressed = ImageIO.read(new ByteArrayInputStream(bytes));

            File outputFile = new File("test22.bmp");
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(bytes);
            }

            /* TEST */
            BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_INDEXED);

            ImageWriter writer  = ImageIO.getImageWritersByFormatName("bmp").next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionType("BI_RLE8");

            writer.setOutput(new FileImageOutputStream(new File("test33.bmp")));
            writer.write(null, new IIOImage(bi, null, null), param);

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(bi, "bmp", bs);

            byte[] a = bs.toByteArray();

            for (int i = 0; i< a.length; i++ ) System.out.println(i + "\t" + bytes[i] + "\t" + a[i]);
             writer.dispose();

            /* BufferedImage compressedImage = ImageIO.read(new ByteArrayInputStream(bytes));

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("bmp");
            ImageWriter writer = writers.next();

            try (ImageOutputStream output = new FileImageOutputStream(new File("test.bmp"))) {
                writer.setOutput(output);

                // Optionally, listen to progress, warnings, etc.
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionType("BI_RLE8");

                // Optionally, provide thumbnails and image/stream metadata.
                writer.write(null, new IIOImage(compressedImage, null, null), param);

            }*/
        }
        catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe) {
            System.out.println();
            System.out.println("NotBoundException");
            System.out.println(nbe);
        }
        catch (
                java.lang.ArithmeticException
                        ae) {
            System.out.println();
            System.out.println("java.lang.ArithmeticException");
            System.out.println(ae);
        }
        catch (
                IOException ioe) {
            System.out.println();
            System.out.println("java.lang.IOException");
            System.out.println(ioe);
        }
    }

}

