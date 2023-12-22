package pl.bartek;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {

        //getting max and min of integer list

        List<Integer> numbers = generateRandomNumbers(1000000, Integer.MAX_VALUE, Integer.MIN_VALUE);

        long startTime = System.currentTimeMillis();

        Integer maxNumber = numbers.stream()
                .parallel()
                .max(Integer::compare)
                .orElse(0);
        Integer minNumber = numbers.stream()
                .parallel()
                .min(Integer::compare)
                .orElse(0);

        long endTime = System.currentTimeMillis();

        System.out.println("Max number: " + maxNumber);
        System.out.println("Min number: " + minNumber);

        System.out.println("Time of execution with parallel streams: " + (endTime - startTime));

        startTime = System.currentTimeMillis();

        numbers.stream()
                .max(Integer::compare)
                .orElse(0);
        numbers.stream()
                .min(Integer::compare
                ).orElse(0);

        endTime = System.currentTimeMillis();

        System.out.println("Time of execution without parallel streams: " + (endTime - startTime));

        //grouping data

        List<Employee> employees = List.of(
                new Employee("jan","kowalski","dzial1",100),
                new Employee("imie2", "nazwisko2", "dzial3", 300),
                new Employee("anna", "nowak", "dzial2", 300),
                new Employee("janusz", "nowak", "dzial2", 200),
                new Employee("joanna", "kowalska", "dzial1", 150),
                new Employee("imie1", "nazwisko1", "dzial3", 300),
                new Employee("jan","kowalski","dzial1",100),
                new Employee("imie2", "nazwisko2", "dzial3", 300)
        );

        Map<String, Double> averageSalaries = employees.stream()
                .parallel()
                .collect(Collectors.groupingByConcurrent(Employee::getDepartment, Collectors.averagingDouble(Employee::getSalary)));


        System.out.println("Average salary by department:");
        averageSalaries.keySet().forEach(key -> {
            System.out.println(key + ": " + averageSalaries.get(key));
        });

        //processing an image

        File inputImageFile = new File("input.jpg");
        BufferedImage originalImage = ImageIO.read(inputImageFile);

        IntStream.range(0, originalImage.getWidth())
                .parallel()
                .forEach((i) -> {
                    IntStream.range(0, originalImage.getHeight())
                            .forEach((j) -> {
                                int rgb = originalImage.getRGB(i,j);
                                Color newColor = new Color(rgb);
                                int grey = Math.round((float) (0.2126 * newColor.getRed() / 255 + 0.7152 * newColor.getGreen() / 255 + 0.0722 * newColor.getBlue() / 255) * 255);
                                newColor = new Color(
                                        grey,
                                        grey,
                                        grey,
                                        255
                                );
                                int newColorInt = colorToInt(newColor);
                                originalImage.setRGB(i,j,newColorInt);
                            });
                });

        File outputImageFile = new File("output.jpg");
        ImageIO.write(originalImage, "jpg", outputImageFile);
    }


    public static Integer colorToInt(Color color){
        return ((color.getAlpha() << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue());
    }

    public static List<Integer> generateRandomNumbers(int quantity, Integer min, Integer max){
        List<Integer> result = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < quantity; i++){
            result.add(Math.abs(random.nextInt()) % (max + 1 - min) + min);
        }
        return result;
    }
}