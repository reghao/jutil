package cn.reghao.jutil.jdk.file;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author reghao
 * @date 2021-12-05 03:35:30
 */
public class CaptchaUtil {
    // 常用颜色
    private static final int[][] COLOR = {{0, 135, 255}, {51, 153, 51}, {255, 102, 102}, {255, 153, 0}, {153, 102, 0}, 
            {153, 102, 153}, {51, 153, 153}, {102, 102, 255}, {0, 102, 204}, {204, 51, 51}, {0, 153, 204}, {0, 51, 102}};
    private static final Random random = new SecureRandom();

    public static ByteArrayOutputStream captchaWithDisturb(String randomStr, int width, int height) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        // 填充背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画干扰圆
        drawOval(2, null, g2d, width, height);
        // 设置线条特征
        g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        // 画干扰线
        drawLine(1, null, g2d, width, height);
        // 画贝塞尔曲线
        drawBesselLine(1, null, g2d, width, height);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        // 每一个字符所占的宽度
        int fW = width / randomStr.length();
        // 字符的左右边距
        int fSp = (fW - (int) fontMetrics.getStringBounds("W", g2d).getWidth()) / 2;
        for (int i = 0; i < randomStr.length(); i++) {
            g2d.setColor(color());
            // 文字的纵坐标
            int fY = height - ((height - (int) fontMetrics.getStringBounds(String.valueOf(randomStr.charAt(i)), g2d).getHeight()) >> 1);
            g2d.drawString(String.valueOf(randomStr.charAt(i)), i * fW + fSp + 3, fY - 3);
        }
        g2d.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) bi, "jpg", byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    /**
     * 随机画干扰圆
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    private static void drawOval(int num, Color color, Graphics2D g, int width, int height) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? color() : color);
            int w = 5 + num(10);
            g.drawOval(num(width - 25), num(height - 15), w, w);
        }
    }

    /**
     * 产生两个数之间的随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    private static int num(int min, int max) {
        return min + random.nextInt(max - min);
    }

    /**
     * 产生0-num的随机数,不包括num
     *
     * @param num 最大值
     * @return 随机数
     */
    private static int num(int num) {
        return random.nextInt(num);
    }

    /**
     * 随机获取常用颜色
     *
     * @return 随机颜色
     */
    private static Color color() {
        int[] color = COLOR[num(COLOR.length)];
        int r = color[0];
        int g = color[1];
        int b = color[2];
        return new Color(r, g, b);
    }

    /**
     * 随机画干扰线
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    private static void drawLine(int num, Color color, Graphics2D g, int width, int height) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? color() : color);
            int x1 = num(-10, width - 10);
            int y1 = num(5, height - 5);
            int x2 = num(10, width + 10);
            int y2 = num(2, height - 2);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 随机画贝塞尔曲线
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    private static void drawBesselLine(int num, Color color, Graphics2D g, int width, int height) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? color() : color);
            int x1 = 5, y1 = num(5, height / 2);
            int x2 = width - 5, y2 = num(height / 2, height - 5);
            int ctrlx = num(width / 4, width / 4 * 3), ctrly = num(5, height - 5);
            if (num(2) == 0) {
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            // 二阶贝塞尔曲线
            if (num(2) == 0) {
                QuadCurve2D shape = new QuadCurve2D.Double();
                shape.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
                g.draw(shape);
            } else {
                // 三阶贝塞尔曲线
                int ctrlx1 = num(width / 4, width / 4 * 3), ctrly1 = num(5, height - 5);
                CubicCurve2D shape = new CubicCurve2D.Double(x1, y1, ctrlx, ctrly, ctrlx1, ctrly1, x2, y2);
                g.draw(shape);
            }
        }
    }
}
