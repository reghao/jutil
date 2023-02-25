package cn.reghao.jutil.media.image;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 对图像的操作
 *
 * @author reghao
 * @date 2021-08-04 16:26:13
 */
public class ImageOps {
    public static byte[] convert(File srcFile, String destFormat) {
        String srcFormat = getFormat(srcFile);
        if (srcFormat != null && srcFormat.equals(destFormat)) {
            return new byte[0];
        }

        try (ImageInputStream iis = ImageIO.createImageInputStream(srcFile)) {
            BufferedImage image = ImageIO.read(iis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
            ImageIO.write(image, destFormat, ios);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static String getFormat(File file) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(file);) {
            Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
            if ((iterator.hasNext())) {
                ImageReader ir = iterator.next();
                return ir.getFormatName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Size info(File file) throws IOException {
        BufferedImage bi = ImageIO.read(file);
        return new Size(bi.getWidth(), bi.getHeight());
    }

    public static BufferedImage merge(List<BufferedImage> bufferedImages, boolean isVertical) {
        int size = bufferedImages.size();
        int[][] imageArray = new int[size][];
        for (int i = 0; i < size; i++) {
            int width = bufferedImages.get(i).getWidth();
            int height = bufferedImages.get(i).getHeight();
            imageArray[i] = new int[width*height];
            imageArray[i] = bufferedImages.get(i).getRGB(0, 0, width, height, imageArray[i], 0, width);
        }

        int newHeight = 0, newWidth = 0;
        for (BufferedImage bufferedImage : bufferedImages) {
            if (!isVertical) {
                // 横向拼接，height 不变，width 增加
                newHeight = Math.max(newHeight, bufferedImage.getHeight());
                newWidth += bufferedImage.getWidth();
            } else {
                // 纵向拼接，width 不变，height 增加
                newWidth = Math.max(newWidth, bufferedImage.getWidth());
                newHeight += bufferedImage.getHeight();
            }
        }

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        int width = 0, height = 0;
        for (int i = 0; i < size; i++) {
            if (!isVertical) {
                // 横向拼接
                newImage.setRGB(width, 0, bufferedImages.get(i).getWidth(), newHeight, imageArray[i], 0, bufferedImages.get(i).getWidth());
                width += bufferedImages.get(i).getWidth();
            } else {
                // 纵向拼接
                newImage.setRGB(0, height, newWidth, bufferedImages.get(i).getHeight(), imageArray[i], 0, newWidth);
                height += bufferedImages.get(i).getHeight();
            }
        }

        return newImage;
    }

    /**
     * 缩小图片
     *
     * @param
     * @return
     * @date 2021-08-18 下午1:45
     */
    public static BufferedImage resize(BufferedImage srcImage, int size) {
        int width = srcImage.getWidth()/size;
        int height = srcImage.getHeight()/size;

        BufferedImage newImage = new BufferedImage(width, height, srcImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(srcImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    public static BufferedImage resize(String imagePath, int size) throws IOException {
        BufferedImage srcImage = ImageIO.read(new File(imagePath));
        int width = srcImage.getWidth()/size;
        int height = srcImage.getHeight()/size;

        BufferedImage newImage = new BufferedImage(width, height, srcImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(srcImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    public static void saveImage(BufferedImage bufferedImage, String filePath) throws IOException {
        ImageIO.write(bufferedImage, "jpg", new File(filePath));
    }

    public static void saveImage(ByteArrayOutputStream baos, String filePath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
        ImageIO.write(bufferedImage, "jpg", new File(filePath));
    }

    public static class Size {
        private final int width;
        private final int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
