import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

import fi.iki.elonen.NanoHTTPD;

// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

/**
 * The Main class
 *
 * Note it is crucial that this runs in a SINGLE THREADED environment
 * I was too lazy to write this thread safe lmao
 * plus it is not meant for multi-users
 *
 * Created by Derek Zhang on 5/2/18.
 */
public class Main extends NanoHTTPD {

    static final String[] INPUT_NAMES = {"start_hh%d", "start_mm%d", "start_ss%d", "end_hh%d", "end_mm%d", "end_ss%d",
            "desc%d"};
    private static final String ASSETS_PREFIX = "assets" + File.separator;

    //private InputStream iStream = null;

    //main html data
    private byte[] data = null;
    private byte[] scriptData = null;

    private LinksManager linksManager = null;
    private DataManager dataManager = null;

    Main() throws IOException {

        super(8080);
    }

    //initialize some resources
    private void init(String directory) throws IOException {

        {
            Path path;

            path = Paths.get(ASSETS_PREFIX + "page.html");
            data = Files.readAllBytes(path);

            path = Paths.get(ASSETS_PREFIX + "the_script.js");
            scriptData = Files.readAllBytes(path);
        }
        {
            File f1 = new File(directory + File.separator + "links.txt");
            File f2 = new File(directory + File.separator + "tracker.csv");
            File f3 = new File("id_number.txt");
            linksManager = new LinksManager(f1, f2, f3);
        }
        linksManager.init();

        File f4 = new File(directory + File.separator + "data.csv");
        dataManager = new DataManager(f4);
    }

    private void finish() throws IOException {

        linksManager.finish();
    }

    private Response handleDefault() {

        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html",
                "<html><head><title>Error</title></head><body>Error 404<br>You did something wrong (or me)." +
                        "</body></html>");
    }

    private Response handleGet(IHTTPSession session) {

        String uri = session.getUri();
        if ("/".equals(uri)) {

            return newChunkedResponse(Response.Status.OK, "text/html", new ByteArrayInputStream(data));
        }
        else if ("/link".equals(uri)) {

            String vidLink = linksManager.poll();

            if (vidLink == null) {

                return newFixedLengthResponse(Response.Status.OK, "text/html", "");
                        //"<html><head><title>Beeing DONE</title></head><body>DONE :)</body></html>");
            }

            return newFixedLengthResponse(Response.Status.OK, "text/plain", vidLink);
        }
        else if ("/the_script.js".equals(uri)) {

            return newChunkedResponse(Response.Status.OK,
                    "text/javascript", new ByteArrayInputStream(scriptData));
        }
        else if ("/done".equals(uri)) {

            return newFixedLengthResponse(Response.Status.OK, "text/html",
                    "<html><head><title>Beeing DONE</title></head><body>DONE :)</body></html>");
        }

        return handleDefault();
    }

    private Response handlePost(IHTTPSession session) {

        if ("/".equals(session.getUri())) {

            boolean last;

            try {
                {
                    Map<String, String> files = new HashMap<>();
                    session.parseBody(files);
                }

                //session.getParms();

                boolean track = dataManager.append(linksManager.getId(), session.getParms());
                last = linksManager.increment(track);
            }
            catch (IOException ioe) {

                System.err.println(ioe.getMessage());

                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR,
                        MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            }
            catch (ResponseException re) {

                System.err.println(re.getMessage());

                return newFixedLengthResponse(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
            }

            //redirect
            Response r = newFixedLengthResponse(Response.Status.REDIRECT, MIME_HTML, "");
            if (last) {

                r.addHeader("Location", "/done");
            }
            else {

                r.addHeader("Location", "/");
            }
            return r;
        }

        return handleDefault();
    }

    @Override
    public Response serve(IHTTPSession session) {

        Method method = session.getMethod();

        Response r;

        switch(method) {

            case GET:
                r = handleGet(session);
                break;
            case POST:
                r = handlePost(session);
                break;
            default:
                r = handleDefault();
                break;
        }

        return r;
    }

    public static void main(String[] args) {

        try {

            Scanner input = new Scanner(System.in);

            System.out.println("Enter workspace directory:");
            String directory = input.nextLine();

            Main m = new Main();

            m.init(directory);

            //SINGLE THREADED
            ExecutorService service = Executors.newSingleThreadExecutor();
            m.setAsyncRunner(new BoundRunner(service));

            m.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);

            System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");

            System.out.println("hit enter to exit");
            input.nextLine();
            System.out.println("attempting to stop...");
            System.out.println("closing connections...");
            m.closeAllConnections();
            System.out.println("connections closed...");
            m.stop();
            System.out.println("well I did just call stop");
            service.shutdown();
            System.out.println("well I did just call shutdown");
            System.out.println("now I am doing IO, fingers crossed");
            m.finish();
            System.out.println("good-bye");
        }
        catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }
}
