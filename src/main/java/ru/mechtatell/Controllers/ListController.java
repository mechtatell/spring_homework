package ru.mechtatell.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mechtatell.DAO.UserListDAO;
import ru.mechtatell.DTO.ListInfoDTO;
import ru.mechtatell.DTO.UserDTO;
import ru.mechtatell.DTO.UserListDTO;
import ru.mechtatell.Models.User;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lists")
public class ListController {

    private final UserListDAO userListDAO;

    @Autowired
    public ListController(UserListDAO userListDAO) {
        this.userListDAO = userListDAO;
    }

    @GetMapping("/{id}")
    public UserListDTO getList(@PathVariable int id) {
        return UserListDTO.from(userListDAO.get(id));
    }

    @PostMapping
    public long createList(@RequestBody UserListDTO userList) {
        System.out.println(userList);
        return userListDAO.add(userList.toUserList());
    }

    @PostMapping("{id}/elements")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO user, @PathVariable int id) {
        User savedUser = userListDAO.addUser(user.toUser(), id);
        return ResponseEntity.ok(UserDTO.from(savedUser));
    }

    @GetMapping
    public ResponseEntity<List<ListInfoDTO>> getLists() {
        return ResponseEntity.ok(userListDAO.getAll().stream()
                .map(ListInfoDTO::from)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remove(@PathVariable long id) {
        if (userListDAO.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
