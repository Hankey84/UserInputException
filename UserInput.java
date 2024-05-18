/* Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
Фамилия Имя Отчество датарождения номертелефона пол
Форматы данных:
фамилия, имя, отчество - строки
дата_рождения - строка формата dd.mm.yyyy
номер_телефона - целое беззнаковое число без форматирования
пол - символ латиницей f или m.
Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, вернуть код ошибки, обработать его 
и показать пользователю сообщение, что он ввел меньше и больше данных, чем требуется.
Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. Если форматы данных не совпадают, нужно 
бросить исключение, соответствующее типу проблемы. Можно использовать встроенные типы java и создать свои. Исключение должно быть корректно 
обработано, пользователю выведено сообщение с информацией, что именно неверно.
Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны записаться полученные 
данные, вида
<Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
Не забудьте закрыть соединение с файлом.
При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки. 
*/


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите данные: Фамилия Имя Отчество дата_рождения номер_телефона пол('m' или 'f') или /exit для выхода:");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("/exit")) {
                break;
            }

            String[] userData = input.split(" ");

            try {
                if (userData.length < 6) {
                    throw new IllegalArgumentException("Недостаточное количество данных. Ожидается 6 элементов.");
                }
                if (userData.length > 6) {
                    throw new IllegalArgumentException("Избыточное количество данных. Ожидается 6 элементов.");
                }
                String lastName = validateName(userData[0], "Фамилия");
                String firstName = validateName(userData[1], "Имя");
                String middleName = validateName(userData[2], "Отчество");
                String dateOfBirth = validateDateOfBirth(userData[3]);
                String phoneNumber = validatePhoneNumber(userData[4]);
                String gender = validateGender(userData[5]);

                String dataToWrite = String.join(" ", lastName, firstName, middleName, dateOfBirth, phoneNumber, gender);

                writeToFile(lastName, dataToWrite);
                System.out.println("Данные успешно сохранены.");

            } catch (IllegalArgumentException | IOException e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static String validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле " + fieldName + " не может быть пустым.");
        }
        return name;
    }

    private static String validateDateOfBirth(String dateOfBirth) {
        if (!Pattern.matches("\\d{2}\\.\\d{2}\\.\\d{4}", dateOfBirth)) {
            throw new IllegalArgumentException("Неверный формат даты рождения. Ожидается dd.mm.yyyy");
        }
        return dateOfBirth;
    }

    private static String validatePhoneNumber(String phoneNumber) {
        if (!Pattern.matches("\\d+", phoneNumber)) {
            throw new IllegalArgumentException("Номер телефона должен содержать только цифры.");
        }
        return phoneNumber;
    }

    private static String validateGender(String gender) {
        if (!gender.equalsIgnoreCase("m") && !gender.equalsIgnoreCase("f")) {
            throw new IllegalArgumentException("Пол должен быть указан символом 'm' или 'f'.");
        }
        return gender.toLowerCase();
    }

    private static void writeToFile(String lastName, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(lastName + ".txt", true))) {
            writer.write(data);
            writer.newLine();
        }
    }
}

