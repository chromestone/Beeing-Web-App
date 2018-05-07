import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The LinksManager class
 * Created by Derek Zhang on 5/5/18.
 */
class LinksManager {

    //number of links we expect
    private static final int LINKS_SIZE = 1000;

    private ArrayList<String> links;

    private final File linkFile;
    private final File trackerFile;
    private final File idNumFile;

    private int id = 0;

    LinksManager(File linkFile, File trackerFile, File idNumFile) {

        links = new ArrayList<>(LINKS_SIZE);
        this.linkFile = linkFile;
        this.trackerFile = trackerFile;
        this.idNumFile = idNumFile;
    }

    void init() throws IOException {

        try (Scanner idScanner = new Scanner(idNumFile);
                BufferedReader linksReader = new BufferedReader(new FileReader(linkFile))){

            id = idScanner.nextInt();

            String line;
            while ((line = linksReader.readLine()) != null) {

                if (links.size() >= LINKS_SIZE) {

                    throw new RuntimeException("too many lines (links) in link file!");
                }

                line = line.trim();

                if (!line.isEmpty()) {

                    links.add(line);
                }
            }
        }
    }

    void finish() throws IOException {

        try (FileWriter writer = new FileWriter(idNumFile)) {

            writer.write(String.valueOf(id));
        }
    }

    //get the next link
    String poll() {

        if (links.isEmpty()) {

            return null;
        }

        return links.get(links.size() - 1);
    }

    int getId() {

        return id;
    }

    private void truncateLinkFile() throws IOException {
        //admittly inefficient without buffering
        try (RandomAccessFile f = new RandomAccessFile(linkFile, "rw")) {

            byte b;
            long length = f.length();

            //remove any newline at the end of the file
            do {

                length -= 1;
                //if all we've seen is newlines then this file is empty
                if (length < 0) {

                    break;
                }
                f.seek(length);
                b = f.readByte();
            } while (b == 10);

            do {

                length -= 1;
                if (length < 0) {

                    length = 0;
                    break;
                }
                f.seek(length);
                b = f.readByte();
            } while (b != 10);

            //discard newline too
            f.setLength(length);
        }
    }

    private void updateTrackerFile() throws IOException {

        try (FileWriter writer = new FileWriter(trackerFile, true)) {

            writer.write(String.valueOf(id));
            writer.write(',');
            writer.write(links.get(links.size() - 1));
            writer.write('\n');
        }
    }

    //move on to the next link
    //DOES IO
    boolean increment(boolean track) throws IOException {//Map<String, String> records) {

        if (links.isEmpty()) {

            return true;
        }

        truncateLinkFile();

        if (track) {

            System.out.println("track " + id);
            updateTrackerFile();
            id += 1;
        }

        links.remove(links.size() - 1);

        return links.isEmpty();
    }

/*
    public static void main(String[] args) throws IOException {

        File f1 = new File("test/links.txt");
        File f2 = new File("test/tracker.csv");
        File f3 = new File("test/number.txt");
        LinksManager manager = new LinksManager(f1, f2);
        manager.init(f3);
        //manager.truncateLinkFile();
        manager.updateTrackerFile();
    }
    */
}
