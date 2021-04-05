package ru.mechtatell.Models.DTO;

import lombok.Data;
import ru.mechtatell.Models.Client;

@Data
public class ClientDTO {
    private String name;
    private int age;
    private double sum;

    public static ClientDTO from(Client client) {
        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setName(client.getName());
        clientDTO.setAge(client.getAge());
        clientDTO.setSum(client.getSum());

        return clientDTO;
    }

    public Client toClient() {
        Client client = new Client();

        client.setName(name);
        client.setAge(age);
        client.setSum(sum);

        return client;
    }
}
