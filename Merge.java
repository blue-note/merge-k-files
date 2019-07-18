import java.io.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Merge {
    public static void main(String[] args) {
        String dirName = args[0];
        String outputFile = args[1];
        new Merge().merge(dirName, outputFile);
    }

    public void verifyInput(String dirName) throws IllegalArgumentException {
        File[] files = new File(dirName).listFiles();
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String last, next;
                last = next = reader.readLine();
                while (last != null && next != null) {
                    while (next != null && next.isEmpty()) {
                        next = reader.readLine();
                    }
                    if (last.compareTo(next) > 0) {
                        throw new IllegalArgumentException("Input file was not sorted in lexicographic order.");
                    }
                    last = next;
                    next = reader.readLine();
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void merge(String dirName, String outputFile) {
        verifyInput(dirName);
        Comparator<FileIterator> comparator = (a, b) -> {
            return a.peek().compareTo(b.peek());
        };

        // Initialize priority queue
        PriorityQueue<FileIterator> heap = new PriorityQueue<FileIterator>(comparator);
        HashSet<String> seen = new HashSet<String>();

        File output = new File(outputFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
            output.createNewFile();
            File[] files = new File(dirName).listFiles();

            // Add all file iterators to the queue
            for (File file : files) {
                FileIterator iter = new FileIterator(file);
                if (iter.hasNext()) {
                    heap.offer(iter);
                }
            }

            FileIterator curr;
            while (heap.size() > 0) {
                curr = heap.poll();
                String s = curr.readLine();
                if (!seen.contains(s)) {
                    writer.write(s);
                    writer.newLine();
                }
                seen.add(s);
                if (curr.hasNext()) {
                    heap.offer(curr);
                } else {
                    curr.close();
                }
            }
        } catch (IOException ex) {
            System.out.println("Unexpected error while merging files: " + ex.getMessage());
        }
    }

    class FileIterator {
        String nextLine;
        BufferedReader reader;

        public FileIterator(File file) throws IOException {
            reader = new BufferedReader(new FileReader(file));
            readLine();
        }

        String readLine() throws IOException {
            String curr = nextLine;
            do {
                nextLine = reader.readLine();
            } while (nextLine != null && nextLine.isEmpty());

            return curr;
        }

        String peek() {
            return nextLine;
        }

        boolean hasNext() {
            return nextLine != null;
        }

        void close() throws IOException {
            reader.close();
        }
    }
}