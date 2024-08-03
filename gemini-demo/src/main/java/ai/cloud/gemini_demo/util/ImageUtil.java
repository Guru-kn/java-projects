package ai.cloud.gemini_demo.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

public class ImageUtil {
	
	public static BufferedImage readImageFromHttpUrl(String imageUrl) throws IOException {
		URL url = new URL(imageUrl);
        // Read the image from the URL
        return ImageIO.read(url);
	}

	public static BufferedImage readImageFromDataURL(String dataUrl) throws IOException {
		// Split the data URL to get the Base64 encoded string
		String[] parts = dataUrl.split(",");
		if (parts.length < 2) {
			throw new IllegalArgumentException("Invalid data URL");
		}
		String base64Image = parts[1];

		// Decode the Base64 string
		byte[] imageBytes = Base64.getDecoder().decode(base64Image);

		// Convert the byte array to BufferedImage
		ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
		BufferedImage bufferedImage = ImageIO.read(bis);

		return bufferedImage;
	}

	public static MultipartFile convertBufferedImageToMultipartFile(BufferedImage image, String formatName,
			String fileName) throws IOException {
		// Write the BufferedImage to ByteArrayOutputStream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, formatName, baos);
		baos.flush();

		// Convert ByteArrayOutputStream to byte array
		byte[] imageBytes = baos.toByteArray();
		baos.close();

		// Create MultipartFile
		MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "image/" + formatName, imageBytes);

		return multipartFile;
	}
}
