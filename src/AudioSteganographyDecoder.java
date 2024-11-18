import java.io.*;

public class AudioSteganographyDecoder {

    // Trích xuất tin nhắn từ tệp âm thanh
	 public static String extractData(String inputAudioPath) throws IOException {
	        File inputFile = new File(inputAudioPath);
	        FileInputStream fis = new FileInputStream(inputFile);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();

	        // Đọc toàn bộ nội dung tệp âm thanh
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = fis.read(buffer)) != -1) {
	            baos.write(buffer, 0, bytesRead);
	        }
	        fis.close();
	        byte[] audioBytes = baos.toByteArray();

	        // Trích xuất các bit ít quan trọng nhất
	        StringBuilder extractedBits = new StringBuilder();
	        for (int i = 44; i < audioBytes.length; i++) {
	            extractedBits.append(audioBytes[i] & 1);
	        }

	        // Chuyển chuỗi nhị phân thành thông điệp
	        StringBuilder secretMessage = new StringBuilder();
	        for (int i = 0; i < extractedBits.length(); i += 8) {
	            String byteString = extractedBits.substring(i, Math.min(i + 8, extractedBits.length()));
	            int charCode = Integer.parseInt(byteString, 2);
	            secretMessage.append((char) charCode);
	            if (secretMessage.toString().endsWith("###")) {
	                break;
	            }
	        }

	        // Loại bỏ ký hiệu kết thúc
	        return secretMessage.toString().replace("###", "");
	    }

    public static void main(String[] args) {
  
        String inputAudioPath = "inputMp3.mp3";
        String outputAudioPath = "outputMp3.mp3";

        try {
            // Trích xuất thông tin
            String extractedMessage = AudioSteganographyDecoder.extractData(outputAudioPath);
            System.out.println("Thông điệp được trích xuất: " + extractedMessage);

        } catch (IOException e) {
             System.err.println("Lỗi: " + e.getMessage());
        }
    }
}
