import java.io.IOException;
import java.net.URI;
import java.util.*;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    int num = 0;
    HashSet<String> myList = new HashSet<>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            String listString = "The list: ";
            for(String item : myList) {
                listString += item + ", ";
            }
            if(listString.length() > 10)
                listString = listString.substring(0, listString.length() - 2);
            return listString;
        } else if (url.getPath().equals("/add")) {
            System.out.println("Path: " + url.getPath());
            String[] parameters = url.getQuery().split("=");
            if (parameters.length > 1 && parameters[0].equals("s")) {
                if(myList.contains(parameters[1]))
                    return "The list already contains " + parameters[1];
                myList.add(parameters[1]);
                return String.format("%s added to the list!", parameters[1]);
            }
            return "Please add a String";
        } else if(url.getPath().equals("/search")) {
            System.out.println("Path: " + url.getPath());
            String[] parameters = url.getQuery().split("=");
            if (parameters.length > 1 && parameters[0].equals("s")) {
                String listString = "";
                for(String item : myList) {
                    if(item.contains(parameters[1])) {
                        listString += item + ", ";
                    }
                }
                if(listString.length() > 0) {
                    listString = listString.substring(0, listString.length() - 2);
                    return listString;
                } else {
                    return "There are no Strings that match your search";
                }
            } else {
                return "Please add a String";
            }
        } else {
            System.out.println("Path: " + url.getPath());
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
