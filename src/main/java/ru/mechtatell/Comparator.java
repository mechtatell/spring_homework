package ru.mechtatell;

import ru.mechtatell.Models.Client;

public class Comparator implements java.util.Comparator<Client> {
    @Override
    public int compare(Client o1, Client o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
