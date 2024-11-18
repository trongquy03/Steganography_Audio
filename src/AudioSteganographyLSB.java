
import java.io.*;

public class AudioSteganographyLSB {

    public static void hideData(String inputAudioPath, String outputAudioPath, String secretMessage) throws IOException {
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

        // Chuyển thông điệp bí mật thành chuỗi nhị phân
        secretMessage += "###"; // Ký hiệu kết thúc
        StringBuilder secretBits = new StringBuilder();
        for (char c : secretMessage.toCharArray()) {
            secretBits.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }

        // Nhúng chuỗi nhị phân vào các bit ít quan trọng nhất
        int bitIndex = 0;
        for (int i = 44; i < audioBytes.length && bitIndex < secretBits.length(); i++) {
            audioBytes[i] = (byte) ((audioBytes[i] & 0xFE) | (secretBits.charAt(bitIndex++) - '0'));
        }

        // Ghi lại file âm thanh đã được nhúng thông tin
        FileOutputStream fos = new FileOutputStream(outputAudioPath);
        fos.write(audioBytes);
        fos.close();

        System.out.println("Dữ liệu đã được giấu vào tệp: " + outputAudioPath);
    }
    

    public static void main(String[] args) {
           String inputAudioPath = "inputMp3.mp3";
           String outputAudioPath = "outputMp3.mp3";
           String secretMessage = "Hello, this is a hidden message!";

           try {
               // Nhúng thông tin
               AudioSteganographyLSB.hideData(inputAudioPath, outputAudioPath, secretMessage);

           } catch (IOException e) {
                System.err.println("Lỗi: " + e.getMessage());
           }
       }
}



