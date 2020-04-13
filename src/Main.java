import com.mxgraph.io.mxVsdxCodec;
import com.mxgraph.online.Utils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
                System.out.println(String.format("Invalid integer value: %s, using default port...", args[0]));
            }
        }

        System.out.println(String.format("Starting Spark on port %d", port));

        port(port);

        post("/convert/", (req, resp) -> {
            System.out.println("Received new request, converting vsdx to xml...");
            try {
                final String decode = new mxVsdxCodec().decodeVsdx(req.bodyAsBytes(), Utils.CHARSET_FOR_URL_ENCODING);
                if (decode != null) {
                    System.out.println("Setting response body...");

                    resp.body(decode);

                    System.out.println("Conversion successful, giving back response...");

                    return resp.body();
                }
            } catch (IllegalArgumentException | IOException ex) {
                System.out.println("An error occurred and the conversion could not occur, giving error 400 (bad request)");
                System.out.println(String.format("Error: %s", ex.getMessage()));
            }
            resp.status(400);
            return null;
        });

    }

}
