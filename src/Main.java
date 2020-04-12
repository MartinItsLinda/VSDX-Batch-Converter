import java.io.File;

import static spark.Spark.get;
import static spark.Spark.port;

public class Main {

    public static void main(String[] args) {

        port(8080);

        get("/convert/:fileName", (req, resp) -> {
            System.out.println(String.format("received request: %s", req.params(":fileName")));

            if (req.params(":fileName") != null && !req.params(":fileName").isEmpty()) {
                System.out.println("file name isn't null or empty, checking if it exists");

                final File file = new File(req.params(":fileName"));
                if (file.exists()) {
                    System.out.println("File exists, converting it...");
                    try {
                        vsdxBatchConvert.execute(file);
                        return genSucc(file.getName() + ".xml");
                    } catch (final Exception ex) {
                        return genErr(ex.getMessage());
                    }
                } else {
                    System.out.println("File doesn't appear to exist, giving an error response");
                    return genErr("File doesn't exist");
                }
            }
            System.out.println("File name is empty, giving error response");
            return genErr("File name cannot be empty");
        });

    }

    private static String genSucc(final String fileName) {
        return "{\"response\":\"success\",\"fileLocation\":\"" + fileName + "\"}";
    }

    private static String genErr(final String err) {
        return "{\"response\":\"failure\",\"message\":\"" + err + "\"}";
    }

}
