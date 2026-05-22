
import java.io.*;
import java.util.*;


public class FileManager {
    public static <T> void saveToFile(List<T> data, String fileName) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(data);
            System.out.println("File saved successfully on " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving file " + fileName + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> loadFromFile(String fileName) {
        File file  = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading file " + fileName + e.getMessage());
            return new ArrayList<>();
        }
    }
}