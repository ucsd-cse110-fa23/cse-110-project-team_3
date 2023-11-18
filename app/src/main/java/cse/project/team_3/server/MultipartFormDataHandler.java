package cse.project.team_3.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MultipartFormDataHandler {
    private InputStream inputStream;
    private String boundary;

    public MultipartFormDataHandler(InputStream inputStream, String boundary) {
        this.inputStream = inputStream;
        this.boundary = boundary;
    }

    public InputStream getFile(String fieldName) throws IOException {
        // Read the input stream to find the start of the file data
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("--" + boundary)) {
                break;
            }
        }

        // Read and discard headers until an empty line is encountered
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
        }

        // Return the remaining input stream as the file data
        return new NonCloseableInputStream(inputStream);
    }

    // Helper class to wrap the input stream and prevent it from being closed
    private static class NonCloseableInputStream extends InputStream {
        private final InputStream wrappedStream;

        public NonCloseableInputStream(InputStream wrappedStream) {
            this.wrappedStream = wrappedStream;
        }

        @Override
        public int read() throws IOException {
            return wrappedStream.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return wrappedStream.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return wrappedStream.read(b, off, len);
        }
    }
}