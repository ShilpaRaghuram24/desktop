package slidingwindow;


import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
public class SlidingWindowProtocol {
	
    private final int windowSize;
    private final int totalFrames;
    private int senderBase = 0;
    private int nextFrameToSend = 0;
    private final boolean[] acknowledged;
 public SlidingWindowProtocol(int windowSize, int totalFrames) {
        this.windowSize = windowSize;
        this.totalFrames = totalFrames;
        this.acknowledged = new boolean[totalFrames];
    }
 private boolean sendFrame(int frameNumber) {
        System.out.println("Sender: Sending frame " + frameNumber);
        // Simulate a random loss
        Random rand = new Random();
        return rand.nextInt(100) >= 10; // 90% chance to succeed
    }
  private void receiveAck(int frameNumber) {
        System.out.println("Receiver: Acknowledging frame " + frameNumber);
        acknowledged[frameNumber] = true;
    }
 public void slideWindow() {
        while (senderBase < totalFrames) {
            // Send frames in the current window
        while (nextFrameToSend < senderBase + windowSize && nextFrameToSend < totalFrames) {
                if (sendFrame(nextFrameToSend)) {
                    nextFrameToSend++;
                }
            }
      // Simulate receiving acknowledgments
            for (int i = senderBase; i < nextFrameToSend; i++) {
                if (!acknowledged[i]) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500); // Simulate delay for ACK
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    receiveAck(i);
                }
            }
            // Slide the window
            while (senderBase < totalFrames && acknowledged[senderBase]) {
                System.out.println("Sender: Frame " + senderBase + " acknowledged");
                senderBase++;
            }
        }
        System.out.println("All frames sent and acknowledged.");
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the window size: ");
        int windowSize = scanner.nextInt();

        System.out.print("Enter the total number of frames: ");
        int totalFrames = scanner.nextInt();
SlidingWindowProtocol protocol = new SlidingWindowProtocol(windowSize, totalFrames);
        protocol.slideWindow();
scanner.close();
    }
}
