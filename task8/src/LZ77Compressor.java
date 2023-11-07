import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LZ77Compressor {

    static final int MAX_WINDOW_SIZE = 32 * 1024; // Example window size of 32KB

    static class Tuple implements Serializable {
        private static final long serialVersionUID = 1L; // Recommended for Serializable classes

        public final int offset;
        public final int length;
        public final byte nextByte;

        public Tuple(int offset, int length, byte nextByte) {
            this.offset = offset;
            this.length = length;
            this.nextByte = nextByte;
        }
    }

    public static void main(String[] args) {
        // Modify this flag to switch between compression and decompression.
        boolean compress = true; // Set to 'false' to decompress during a different run.

        String basePath = "task8/src/"; // The base path for input and output
        String inputFileName = "diverse.lyx"; // The original input file name
        String compressedFileName = "compressed.txt"; // The file name for compressed data
        String decompressedFileName = "decompressed.lyx"; // The file name for decompressed data

        try {
            if (compress) {
                // Read the original file's bytes
                byte[] originalContent = readFile(basePath + inputFileName);

                // Compress the content
                List<Tuple> compressedContent = compress(originalContent);

                // Save the compressed content to a file
                saveCompressed(basePath + compressedFileName, compressedContent);

                System.out.println("Compression has been completed successfully.");

            } else {
                // Read the compressed content from the file
                List<Tuple> loadedCompressedContent = readCompressed(basePath + compressedFileName);

                // Decompress the content
                byte[] decompressedContent = decompress(loadedCompressedContent);

                // Save the decompressed content as a new file
                writeFile(basePath + decompressedFileName, decompressedContent);

                System.out.println("Decompression has been completed successfully.");
            }
        } catch (IOException e) {
            System.err.println("There was an error processing the file: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("There was an error with the class definition: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static List<Tuple> compress(byte[] input) {
        List<Tuple> compressed = new ArrayList<>();
        int cursor = 0;

        while (cursor < input.length) {
            int matchLength = 0;
            int matchDistance = 0;
            int newCursor = cursor;

            // Search for the longest match
            for (int i = Math.max(cursor - MAX_WINDOW_SIZE, 0); i < cursor; ++i) {
                int currentMatchLength = 0;
                while (currentMatchLength < MAX_WINDOW_SIZE && cursor + currentMatchLength < input.length && input[i + currentMatchLength] == input[cursor + currentMatchLength]) {
                    currentMatchLength++;
                }

                if (currentMatchLength > matchLength) {
                    matchLength = currentMatchLength;
                    matchDistance = cursor - i;
                }
            }

            // Adjust the newCursor position based on matchLength found
            newCursor += matchLength;

            // Add a new Tuple for the match or for a single byte if no match was found
            if (matchLength > 0 && newCursor < input.length) {
                // Save the match and the subsequent byte
                compressed.add(new Tuple(matchDistance, matchLength, input[newCursor]));
                cursor = newCursor + 1; // Move past the match and the subsequent byte
            } else {
                // No match found or we're at the last byte, save a single byte
                compressed.add(new Tuple(0, 0, input[cursor]));
                cursor++; // Move to the next byte
            }
        }

        return compressed;
    }


    public static byte[] decompress(List<Tuple> compressed) {
        ArrayList<Byte> decompressed = new ArrayList<>();

        for (Tuple tuple : compressed) {
            if (tuple.length > 0) {
                int start = decompressed.size() - tuple.offset;
                for (int i = start; i < start + tuple.length; ++i) {
                    decompressed.add(decompressed.get(i));
                }
            }
            decompressed.add(tuple.nextByte);
        }

        // Convert the ArrayList<Byte> back to byte[]
        byte[] output = new byte[decompressed.size()];
        for (int i = 0; i < output.length; ++i) {
            output[i] = decompressed.get(i);
        }

        return output;
    }

    // Reads the entire file and returns its content as a byte array.
    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)file.length());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        fis.close();
        return bos.toByteArray();
    }

    // Writes the given byte array as a file.
    public static void writeFile(String filePath, byte[] content) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(content);
        fos.close();
    }

    // Serializes and saves the compressed list to a file.
    public static void saveCompressed(String filePath, List<Tuple> compressed) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(compressed);
        }
    }

    // Reads the compressed list from a file.
    @SuppressWarnings("unchecked") // for the cast
    public static List<Tuple> readCompressed(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Tuple>) ois.readObject();
        }
    }
}
