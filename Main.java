/* Name: Hitesh Sah
 * NetId: hks200000
 * Course: 3345.005
 * 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        // check if right number of arguments are passed
        if (args.length < 2) {
            throw new IllegalArgumentException("Two command line arguments should be passed. " +
                    "The first argument will be the input file name and second will be output file name.");
        }

        // open both input and output files
        try (BufferedReader reader = Files.newBufferedReader(Path.of(args[0]));
             BufferedWriter writer = Files.newBufferedWriter(Path.of(args[1]))) {

            LazyBinarySearchTree tree = new LazyBinarySearchTree();

            // read input file line by line
            String line;
            while ((line = reader.readLine()) != null) {
                // split line on :
                String[] splits = line.split(":");
                // perform corresponding action based on command in a line
                switch (splits[0].trim()) {
                    case "Insert":      //case for insert
                        try {
                            boolean value = tree.insert(Integer.parseInt(splits[1]));
                            writer.write(string(value));
                        } catch (IllegalArgumentException ex) {
                            writer.write("Error in insert: IllegalArgumentException raised");
                        } catch (Exception ex) {
                            writer.write("Error in Line: " + line);
                        }
                        break;
                    case "PrintTree":
                        writer.write(tree.toString());
                        break;
                    case "Delete":
                        try {
                            boolean value = tree.delete(Integer.parseInt(splits[1]));
                            writer.write(string(value));
                        } catch (IllegalArgumentException ex) {
                            writer.write("Error in delete: IllegalArgumentException raised");
                        } catch (Exception ex) {
                            writer.write("Error in Line: " + line);
                        }
                        break;
                    case "Contains":         //contain
                        try {
                            boolean value = tree.contains(Integer.parseInt(splits[1]));
                            writer.write(string(value));
                        } catch (IllegalArgumentException ex) {
                            writer.write("Error in contains: IllegalArgumentException raised");
                        } catch (Exception ex) {
                            writer.write("Error in Line: " + line);
                        }
                        break;
                    case "FindMin":       //findMin
                        writer.write(String.valueOf(tree.findMin()));
                        break;
                    case "FindMax":         //findMax
                        writer.write(String.valueOf(tree.findMax()));
                        break;
                    case "Height":          //Height
                        writer.write(String.valueOf(tree.height()));
                        break;
                    case "Size":            //size
                        writer.write(String.valueOf(tree.size()));
                        break;
                    default:
                        writer.write("Error in Line: " + line);
                        break;
                }
                writer.newLine();
            }
        }
    }

    // converting the camelCase of boolean value
    static String string(boolean value) {
        return value ? "True" : "False";
    }
}
