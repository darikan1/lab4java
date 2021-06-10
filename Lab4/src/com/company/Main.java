package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        Path path = Paths.get("src/com/company/files/database.txt");
        Pattern workingDayPattern = Pattern.compile("(?<workingday>WorkingDay:) (?<date>\\d{2}\\.\\d{2}\\.\\d{4}), (?<hours>\\d*\\sworking hours), (?<project>\"[A-Za-zІЇїіА-Яа-я\\s\\d]+\")");
        Pattern employeePattern = Pattern.compile("(?<employee>Employee:) (?<name>([A-ZА-ЯЇІ][a-zа-яїі]+\\s){3})(?<appointment>- [A-ZА-ЯІЇ][a-zа-яії]+)");
        List<WorkingDay> workingDays = new ArrayList<>();
        readFromFile(path, workingDayPattern, employeePattern, workingDays);

        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("Виберіть дію:\n1 - Середня кількість робочих годин за період; \n2 - Кількість годин на проекті; \n3 - Дні з максимальним навантаженням; \n4 - Показати всі записи; \nq - Вихід; \ne - Редагування");
            switch (sc.nextLine()){
                case "1": {
                    getWorkingHoursForPeriod(sc, workingDays);
                    break;
                }
                case "2": {
                    getHoursCountInProject(sc, workingDays);
                    break;
                }
                case "3": {
                    getMaxPowerDays(workingDays);
                    break;
                }
                case "4": {
                    readFromFile(path, workingDayPattern, employeePattern, workingDays);
                    break;
                }
                case "q": {
                    return;
                }
                case "e":{
                    startCRUD(sc, workingDays, path);
                    break;
                }
                default: {
                    System.out.println("Неправильна дія! ");
                    break;
                }
            }
        }
    }

    private static void startCRUD(Scanner sc, List<WorkingDay> workingDays, Path path) {
        System.out.println("Виберіть дію:\n1 - Видалити запис; \n2 - Редагувати запис; \n3 - Додати новий запис; \nq - Вихід;");
        switch (sc.nextLine()){
            case "1": {
                delete(sc, workingDays, path);
                System.out.println("Успішно видалено!");
                break;
            }
            case "2": {
                edit(sc, workingDays, path);
                break;
            }
            case "3": {
                save(sc, path);
                break;
            }
            default: {
                break;
            }
        }
    }
    private static void showAllWithNum(List<WorkingDay> workingDays){
        for(int i = 0; i<workingDays.size(); i++){
            System.out.println((i + 1) + " " + workingDays.get(i).toString());
        }
    }

    private static void delete(Scanner sc, List<WorkingDay> workingDays, Path path) {
        showAllWithNum(workingDays);
        System.out.println("Виберіть номер запису для видалення: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;
        workingDays.remove(index);
        save(workingDays, path);
    }
    private static void edit(Scanner sc, List<WorkingDay> workingDays, Path path) {
        showAllWithNum(workingDays);
        System.out.println("Виберіть номер запису для редагування: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;
        WorkingDay workingDay = workingDays.get(index);
        System.out.println(workingDay);
        System.out.println("1 - Змінити ім'я співробітника; \n2 - Змінити посаду співробітника; \n3 - Змінити кількість годин; \n4 - Змінити ім'я проекту \n");
        switch (sc.nextLine()){
            case "1": {
                System.out.println("Введіть нове ім'я співробітника");
                workingDay.getEmploee().setName(sc.nextLine());
                break;
            }
            case "2": {
                System.out.println("Введіть нову посаду");
                workingDay.getEmploee().setAppointment(sc.nextLine());
                break;
            }
            case "3": {
                System.out.println("Введіть нову кількість затрачених годин");
                workingDay.setHoursCount(Long.parseLong(sc.nextLine()));
                break;
            }
            case "4": {
                System.out.println("Введіть нову назву проєкта");
                workingDay.setProjectName(String.format("\"%s\"", sc.nextLine()));
                break;
            }
        }
        workingDays.remove(index);
        workingDays.add(index, workingDay);
        save(workingDays, path);
        System.out.println("Зміни збережено!");
        System.out.println();
    }

    private static String mapToFileFormat(WorkingDay wd){
        StringBuilder sb = new StringBuilder();
        sb.append("WorkingDay: ")
                .append(wd.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .append(", ")
                .append(wd.getHoursCount()).append(" working hours, ")
                .append(wd.getProjectName())
                .append("\r\n");
        sb.append("Employee: ")
                .append(wd.getEmploee().getName())
                .append(" - ")
                .append(wd.getEmploee().getAppointment())
                .append("\r\n");
        sb.append("-------------------------------------------------------");

        return sb.toString();
    }
    private static void save(List<WorkingDay> workingDays, Path path) {
        try {
            List<String> workDay = workingDays.stream()
                    .map(Main::mapToFileFormat)
                    .collect(Collectors.toList());
            Files.write(path, workDay, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void save(Scanner sc, Path path){
        System.out.println("Дата робочого дня (dd.MM.yyyy): ");
        LocalDate date = LocalDate.parse(sc.nextLine().trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        System.out.println("Кількість затрачених годин: ");
        long hours = Long.parseLong(sc.nextLine());
        System.out.println("Назва проекту: ");
        String project = sc.nextLine();
        project = String.format("\"%s\"", project);
        System.out.println("ПІБ співробітника (Іванов Іван Іванович): ");
        String emplName = sc.nextLine();
        System.out.println("Посада співробітника: ");
        String appointment = sc.nextLine();
        WorkingDay workingDay = new WorkingDay(date, hours, project);
        workingDay.setEmploee(new Employee(emplName, appointment));
        System.out.println(workingDay);
        try {
            Files.write(path, ("\r\n" + mapToFileFormat(workingDay)).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            System.out.println("Збережено!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void getMaxPowerDays(List<WorkingDay> workingDays) {
        workingDays = workingDays.stream().sorted(Comparator.comparingDouble(WorkingDay::getHoursCount).reversed()).collect(Collectors.toList());
        List<WorkingDay> max = Arrays.asList(
                workingDays.get(0),
                workingDays.get(1),
                workingDays.get(2)
        );

        System.out.println("Топ-3 найзавантаженіших днів:");
        max.forEach(System.out::println);
        System.out.println();
    }

    private static void getHoursCountInProject(Scanner sc, List<WorkingDay> workingDays) {
        System.out.println("Введіть назву проекта");
        String project = sc.nextLine();
        if(!project.startsWith("\""))
            project = String.format("\"%s\"", project);

        String finalProject = project;
        long hoursCount = workingDays.stream()
                .filter(wd -> wd.getProjectName().equals(finalProject))
                .mapToLong(WorkingDay::getHoursCount)
                .sum();
        if(hoursCount == 0)
            System.out.println("Такого проекту ще не розпочато");
        else
            System.out.printf("Проект %s зайняв %d годин%n", project, hoursCount);

        System.out.println();
    }

    private static void getWorkingHoursForPeriod(Scanner sc, List<WorkingDay> workingDays) {
        System.out.println("Введіть дату початку (дд.ММ.рррр)");
        LocalDate from = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        System.out.println("Введіть дату закінчення (дд.ММ.рррр)");
        LocalDate till = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        double average = workingDays.stream()
                .filter(wd -> wd.getDate().isAfter(from) && wd.getDate().isBefore(till))
                .mapToLong(WorkingDay::getHoursCount)
                .average()
                .getAsDouble();

        System.out.printf("Середня кількість годин на проекті за період %s-%s: %.2f годин%n", from.toString(), till.toString(), average);

        System.out.println();
    }

    private static void readFromFile(Path path, Pattern workingDayPattern, Pattern employeePattern, List<WorkingDay> workingDays) throws IOException {
        List<String> lines = Files.readAllLines(path);
        Matcher matcher;
        String project = "", name = "", appointment = "";
        LocalDate date = LocalDate.MIN;
        long hours = 0;
        for(String line : lines){
            if(line.startsWith("WorkingDay")){
                matcher = workingDayPattern.matcher(line);
                if(matcher.find()) {
                    hours = Long.parseLong(matcher.group("hours").split("\\s")[0]);
                    date = LocalDate.parse(matcher.group("date"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    project = matcher.group("project");
                }

            } else if (line.startsWith("Employee")){
                matcher = employeePattern.matcher(line);
                if(matcher.find()) {
                    name = matcher.group("name").trim();
                    appointment = matcher.group("appointment").split("\\s")[1];
                }
            } else if (line.startsWith("-")) {
                Employee employee = new Employee(name, appointment);
                WorkingDay workingDay = new WorkingDay(date, hours, project);
                workingDay.setEmploee(employee);
                workingDays.add(workingDay);

                System.out.println(workingDay);
            }
        }
    }
}
