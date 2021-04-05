package ru.mechtatell.Models.DTO;

import lombok.Data;
import ru.mechtatell.Models.Client;
import ru.mechtatell.Models.ClientList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClientListDTO {
    private String name;
    private List<ClientDTO> ClientList = new ArrayList<>();

    public static ClientListDTO from(ClientList list) {
        ClientListDTO listDTO = new ClientListDTO();

        List<ClientDTO> clientDTOList = list.getClients().stream()
                .map(ClientDTO::from)
                .collect(Collectors.toList());

        listDTO.setName(list.getName());
        listDTO.setClientList(clientDTOList);

        return listDTO;
    }

    public ClientList toClientList() {
        ClientList clientList = new ClientList();
        clientList.setName(name);

        if (!this.ClientList.isEmpty()) {
            List<Client> clients = this.ClientList.stream()
                    .map(ClientDTO::toClient)
                    .peek(e -> e.setClientList(clientList))
                    .collect(Collectors.toList());

            clientList.getClients().addAll(clients);
        }

        return clientList;
    }
}
