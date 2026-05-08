package utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class ImageHelper {

    /**
     * Chuẩn hóa và tải ảnh từ:
     * - Đường dẫn tuyệt đối / tương đối trên đĩa (user.dir là thư mục project khi chạy Eclipse)
     * - Classpath: /img_product/... và /img/... (resource copy vào bin khi build)
     * - File: img_product/, src/img_product/, src/img/, bin/img/, img/
     * <p>
     * Lưu ý: {@code src/img} trong code chỉ là thư mục nguồn — khi chạy app phải dùng classpath {@code /img/ten.png}
     * hoặc đường dẫn file {@code src/img/ten.png} so với thư mục project. Tiền tố {@code @} trong DB sẽ được bỏ.
     */
    private static ImageIcon loadImageIcon(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return null;
        }
        String raw = imagePath.trim();
        if (raw.startsWith("@")) {
            raw = raw.substring(1).trim();
        }

        File direct = new File(raw);
        if (direct.isFile()) {
            return iconIfRenderable(new ImageIcon(direct.getAbsolutePath()));
        }

        String projectPath = System.getProperty("user.dir");
        File relativeToProject = new File(projectPath, raw);
        if (relativeToProject.isFile()) {
            return iconIfRenderable(new ImageIcon(relativeToProject.getAbsolutePath()));
        }

        String normalized = raw.replace('\\', '/');
        String pathAfterImgFolder = null;
        int imgSlash = indexOfIgnoreCase(normalized, "/img/");
        if (imgSlash >= 0) {
            pathAfterImgFolder = normalized.substring(imgSlash + "/img/".length());
        }

        String fileName = normalized;
        int slash = fileName.lastIndexOf('/');
        if (slash >= 0) {
            fileName = fileName.substring(slash + 1);
        }
        fileName = fileName.replaceFirst("(?i)^img_product/", "");

        ImageIcon fromClasspath = tryClasspath("/img_product/", pathAfterImgFolder, fileName);
        if (fromClasspath != null) {
            return fromClasspath;
        }
        fromClasspath = tryClasspath("/img/", pathAfterImgFolder, fileName);
        if (fromClasspath != null) {
            return fromClasspath;
        }

        String[][] pathsToTry = {
            { projectPath, "img_product", fileName },
            { projectPath, "src", "img_product", fileName },
            { projectPath, "src", "img", fileName },
            { projectPath, "bin", "img", fileName },
            { projectPath, "img", fileName },
        };
        for (String[] parts : pathsToTry) {
            File candidate = new File(String.join(File.separator, parts));
            if (candidate.isFile()) {
                return iconIfRenderable(new ImageIcon(candidate.getAbsolutePath()));
            }
        }

        if (pathAfterImgFolder != null && !pathAfterImgFolder.isEmpty()) {
            File nested = new File(projectPath + File.separator + "src" + File.separator + "img", pathAfterImgFolder);
            if (nested.isFile()) {
                return iconIfRenderable(new ImageIcon(nested.getAbsolutePath()));
            }
            nested = new File(projectPath + File.separator + "bin" + File.separator + "img", pathAfterImgFolder);
            if (nested.isFile()) {
                return iconIfRenderable(new ImageIcon(nested.getAbsolutePath()));
            }
        }

        return null;
    }

    private static int indexOfIgnoreCase(String haystack, String needle) {
        return haystack.toLowerCase().indexOf(needle.toLowerCase());
    }

    /** Thử /prefix + đường dẫn đầy đủ sau img/, hoặc chỉ tên file. */
    private static ImageIcon tryClasspath(String prefix, String pathAfterImgFolder, String fileName) {
        if (pathAfterImgFolder != null && !pathAfterImgFolder.isEmpty()) {
            ImageIcon ic = iconIfRenderable(loadUrl(ImageHelper.class.getResource(prefix + pathAfterImgFolder)));
            if (ic != null) {
                return ic;
            }
        }
        return iconIfRenderable(loadUrl(ImageHelper.class.getResource(prefix + fileName)));
    }

    private static ImageIcon loadUrl(URL url) {
        return url == null ? null : new ImageIcon(url);
    }

    /** SVG hoặc ảnh lỗi thường có width ≤ 0 — coi như không load được. */
    private static ImageIcon iconIfRenderable(ImageIcon icon) {
        if (icon == null) {
            return null;
        }
        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
        }
        return icon;
    }

    public static void setImageToLabel(JLabel label, String imagePath, int width, int height) {
        try {
            if (imagePath == null || imagePath.trim().isEmpty()) {
                setNoImage(label);
                return;
            }

            ImageIcon icon = loadImageIcon(imagePath);

            if (icon != null) {
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImage));
                label.setText("");
            } else {
                setNoImage(label);
            }

        } catch (Exception e) {
            e.printStackTrace();
            setErrorImage(label);
        }
    }

    public static void setImageKeepRatio(JLabel label, String imagePath, int maxWidth, int maxHeight) {
        try {
            if (imagePath == null || imagePath.trim().isEmpty()) {
                setNoImage(label);
                return;
            }

            ImageIcon originalIcon = loadImageIcon(imagePath);

            if (originalIcon != null) {
                Image originalImage = originalIcon.getImage();

                int originalWidth = originalIcon.getIconWidth();
                int originalHeight = originalIcon.getIconHeight();

                double widthRatio = (double) maxWidth / originalWidth;
                double heightRatio = (double) maxHeight / originalHeight;
                double ratio = Math.min(widthRatio, heightRatio);

                int newWidth = (int) (originalWidth * ratio);
                int newHeight = (int) (originalHeight * ratio);

                Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImage));
                label.setText("");
            } else {
                setNoImage(label);
            }

        } catch (Exception e) {
            e.printStackTrace();
            setErrorImage(label);
        }
    }

    private static void setNoImage(JLabel label) {
        label.setIcon(null);
        label.setText("No Image");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.GRAY);
    }

    private static void setErrorImage(JLabel label) {
        label.setIcon(null);
        label.setText("Error Loading Image");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.RED);
    }
}
