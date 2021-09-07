package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;


public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;
    private char [][] graphic;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int newWidth = 0;
        int newHeight = 0;
        double ratio = 0;
        double coefficientWidth = 0;
        double coefficientHeight = 0;

        // проверка соотношения сторон изображения на максимально допустимое
        maximumAspectRatio(img.getWidth(), img.getHeight(), getMaxRatio(), ratio);

        // значение новой ширины/длинны
        int [] arrWH = imageСompression(img.getHeight(), img.getWidth(), maxHeight, maxWidth,
                coefficientHeight, coefficientWidth);

        newWidth = arrWH[0];
        newHeight = arrWH[1];

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();

        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();

        graphic = new char[newHeight][newWidth];

        // пиксели --> символы
        pixelToChar(graphic, bwRaster, newHeight, newWidth);

        // символы --> текст
        return charactersToText(graphic);

    }

    private int[] imageСompression(int height, int width, int maxHeight, int maxWidth,
                                  double coefficientHeight, double coefficientWidth) {

        int [] arr = new int [2];

        if (width > maxWidth || height > maxHeight) {
            if (maxWidth != 0) {
                coefficientWidth = width / maxWidth;
            } else coefficientWidth = 1;
            if (maxHeight != 0) {
                coefficientHeight = height / maxHeight;
            } else coefficientHeight = 1;

            if (coefficientWidth > coefficientHeight) {
                arr[0] = (int) (width / coefficientWidth);
                arr[1] = (int) (height / coefficientWidth);
            } else {
                arr[0] = (int) (width / coefficientHeight);
                arr[1] = (int) (height / coefficientHeight);
            }
        } else {
            arr[0] = width;
            arr[1] = height;
        }

        return arr;
    }

    private void maximumAspectRatio(int width, int height, double ratio, double maxRatio)
            throws BadImageSizeException {

        if (width / height > width / height) {
            ratio = (double) width / (double) height;
        } else {
            ratio = (double) height / (double) width;
        }

        if (ratio > maxRatio && maxRatio != 0) throw new BadImageSizeException(ratio, maxRatio);
    }

    private void pixelToChar(char[][] graphic, WritableRaster bwRaster, int height, int width) {

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                graphic[h][w] = c;
            }
        }
    }

    private String charactersToText(char [][] graphic) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < graphic.length; i++) {
            for (int j = 0; j < graphic[i].length; j++) {
                builder.append(graphic[i][j]);
                builder.append(graphic[i][j]);
            }
            builder.append("\n");
        }
        String str = builder.toString();

        return str;
    }

    @Override
    public void setMaxWidth(int width) {

        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {

        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {

        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema colorSchema) {

        this.schema = colorSchema;
    }

    //цветовая схема
    public TextGraphicsConverterImpl() {

        schema = new TextColorSchemaImpl();
    }

    public double getMaxRatio() {

        return maxRatio;
    }
}