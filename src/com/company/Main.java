package com.company;

import com.company.Objects.Coordinates;
import com.company.Objects.Ticket;
import com.company.Objects.TicketType;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String command = "";
        Socket socket = new Socket();
        String login = "", password = "";
        socket.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 1111));
        while (!command.startsWith("exit")){
            System.out.println("введите команду");
            command = in.nextLine();

            List<Object> argsComm = Arrays.stream(command.split(" ")).collect(Collectors.toList());
            switch ((String)argsComm.get(0)){
                case "insert":
                    if (argsComm.size() != 2){
                        print("Неверный ввод команды. Формат: insert null {element}");
                        continue;
                    }
                    Ticket toAdd = inputNew(in);
                    System.out.println("a");
                    argsComm.add(toAdd);
                    break;
                case "Login":
                    if(argsComm.size() != 3){
                        print("Неверный ввод команды. Формат: Login login password");
                        continue;
                    }
                    login = (String) argsComm.get(1);
                    password = (String) argsComm.get(2);
                    continue;

            }

            Command c = new Command(((String)argsComm.get(0)), argsComm.toArray());
            c.setPassword(password);
            c.setLogin(login);
            byte[] buff = Convert(c);
            socket.getOutputStream().write(buff);


            byte[] buff1 = new byte[2048];
            socket.getInputStream().read(buff1);
            Response response = (Response) Decode(buff1, buff1.length);
            response.getStrings().forEach(System.out::println);
        }
    }

    public static byte[] Convert(Object object) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();// поток, который записывает все данные в массив байтов(локально)
            ObjectOutputStream outputStream = new ObjectOutputStream(stream);//поток, который преобразует обьект в массив байтов, и запишет его в stream
            outputStream.writeObject(object);//вызов записи
            return stream.toByteArray();//читаем из stream байты
        }
        catch (Exception e){
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static Object Decode(byte[] buff, int length) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(buff, 0, length);
            ObjectInputStream inputStream = new ObjectInputStream(stream);
            return inputStream.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static Ticket inputNew(Scanner in) {
        String incorrect = "Некорректный ввод. Повторите попытку:";

        Ticket newAdd = new Ticket();
        Coordinates coordinates = new Coordinates();

        print("Введите координаты объекта. Формат ввода \"x y\"");
        while (inputCoords(in, coordinates) != 1)
            print(incorrect);
        newAdd.setCoordinates(coordinates);

        print("Введите тип билета. Возможные варианты:");
        for (TicketType form : TicketType.values())
            print(form);
        print("Для ввода null нажмите ENTER");

        while (inputTT(in, newAdd) != 1)
            print(incorrect);

        return newAdd;
    }

    private static int inputCoords(Scanner in, Coordinates coords){
        String input = in.nextLine();
        Pattern check = Pattern.compile("^\\d+\s\\d+(.\\d+)?$");
        Matcher matcher = check.matcher(input);
        if (matcher.find()){
            String[] nums = input.split(" ");
            coords.setX(Integer.parseInt(nums[0]));
            coords.setY(Integer.parseInt(nums[1]));
            return 1;
        }
        return 0;
    }

    private static int inputTT(Scanner in, Ticket holidays){
        String form = in.nextLine();
        TicketType ticketType;
        try {
            if (form.equals("")){
                holidays.setTicketType(null);
                return 1;
            }
            ticketType = TicketType.valueOf(form);
            holidays.setTicketType(ticketType);
        } catch (Exception e){
            return 0;
        }
        return 1;
    }

    private static void print(Object obj){
        System.out.println(obj);
    }
}
