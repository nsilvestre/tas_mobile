package com.company.framework.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.sikuli.script.Image;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;
import org.sikuli.script.ScreenUnion;

import org.sikuli.script.Finder;

public class SikuliX2014Helper {

    public static void clickOnImage(String imagePath, int msToWaitForImage, Boolean highLight, int highLightForSeconds) {

        try {
            ScreenUnion union = Screen.all();
            Match region = union.wait(imagePath, msToWaitForImage);
            if (region.getScore() > 0.0) {
                if (highLight) {
                    region.highlight(highLightForSeconds);
                }
                region.click(imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clickOnImageAndType(String imagePath, int msToWaitForImage, String textToType, Boolean highLight, int highLightForSeconds) {

        try {
            ScreenUnion union = Screen.all();
            Match region = union.wait(imagePath, msToWaitForImage);
            if (region.getScore() > 0.0) {
                if (highLight) {
                    region.highlight(highLightForSeconds);
                }
                region.click(imagePath);
                region.type(textToType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isImageInsideOtherImage(String bigImage_Path, String smallImage_Path) throws IOException {
        BufferedImage big = ImageIO.read(new File(bigImage_Path));
        BufferedImage small = ImageIO.read(new File(smallImage_Path));
        Image image_big = new Image(big);
        Image image_small = new Image(small);
        Finder f = new Finder(image_big);
        f.find(image_small);
        boolean s = f.hasNext();

        if (s)
            return true;
        else
            return false;
    }

    public static boolean isImagePresent(String imagePath, int msToWaitForImage, Boolean highLight, int highLightForSeconds) {
        try {
            ScreenUnion union = Screen.all();
            Match region = union.wait(imagePath, msToWaitForImage);
            if (region.getScore() > 0.0) {
                if (highLight) {
                    region.highlight(highLightForSeconds);
                }
                return true;
            }else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
