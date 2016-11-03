import static spark.Spark.*;


/**
 * Created by trosado on 03/11/16.
 */
public class ServerApplication {

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
        get("/bye", (req, res) -> "Good Bye");
    }
}
