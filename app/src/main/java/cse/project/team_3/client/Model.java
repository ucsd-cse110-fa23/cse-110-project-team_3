package cse.project.team_3.client;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Model {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Thread recordingThread;

    public Model() {
        audioFormat = getAudioFormat();
    }

    public AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }
    
    public void startRecording(String filePath) {
        if (recordingThread == null || !recordingThread.isAlive()) {
            recordingThread = new Thread(() -> {
                try {
                    DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                    targetDataLine.open(audioFormat);
                    targetDataLine.start();

                    AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);

                    File audioFile = new File(filePath);
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);

                    // TODO: Send the audio file to the server using HTTP POST
                    sendAudioToServer(audioFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    stopRecording();
                }
            });
            recordingThread.start();
        }
    }
    
    public void stopRecording() {
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
        }
    }

    private void sendAudioToServer(File audioFile) {
        // TODO: Implement HTTP POST request to send the audio file to the server
    }
}