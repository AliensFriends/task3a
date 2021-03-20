package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import models.Days;
import models.Main;
import models.Resp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainService {
    public static void main(String[] args) throws JsonProcessingException {
        int way = 0;
        GetService service = new GetService();
        MainService weatherService = new MainService();
        Resp response = null;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите 0, если поиск будет по наименованию или по id города.\n" +
                "Либо введите 1, если поиск будет по географическому положению (lat,lon)");
        try{
            way = scanner.nextInt();
            if (way != 1 && way != 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Ввелась не ожидаемая строка. Введите либо 0 либо 1");
            System.exit(-1);
        }

        if(way == 0) {
            System.out.println("Укажите название города или его id");
            response = service.getDataByNameOrId(scanner.next());
        } else {
            try{
                System.out.println("Введите широту");
                int lat = scanner.nextInt();
                System.out.println("Введите долготу");
                int lon = scanner.nextInt();
                response = service.getByLatAndLon(lat, lon);
            } catch (Exception e) {
                System.out.println("ВХодные данные должны быть числами");
                System.exit(-1);
            }
        }

        if (response == null) {
            System.exit(-1);
        }

        List<Main> list = response.getList().stream()
                .filter(x -> weatherService.dateToCalendar(x.getDt()))
                .map(Days::getMain)
                .collect(Collectors.toList());

        double maxTemp = list.stream()
                .map(Main::getTemp_max)
                .mapToDouble(Double::doubleValue)
                .max()
                .getAsDouble();

        double averTemp = list.stream()
                .map(x-> (x.getTemp_max() + x.getTemp_min())/2)
                .mapToDouble(Double::doubleValue)
                .average()
                .getAsDouble();


        System.out.println(String.format("Максимальная температура = %.2f (Цельсий)", (maxTemp - 273.13)));
        System.out.println(String.format("Средняя температура = %.2f (Цельсий)", (averTemp - 273.13)));

    }

    private boolean dateToCalendar(long date1) {
        Date date = new Date(date1 * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        return timeOfDay >= 6 && timeOfDay < 12;
    }
}
