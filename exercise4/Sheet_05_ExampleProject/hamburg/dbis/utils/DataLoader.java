package hamburg.dbis.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataLoader {

    public static RandomHashSet<String> loadExampleData() {
        RandomHashSet<String> data = new RandomHashSet<>();

        String filename = "exercise4\\Sheet_05_ExampleProject\\example_data.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;

    }
}
