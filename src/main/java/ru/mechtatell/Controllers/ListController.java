package ru.mechtatell.Controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mechtatell.DTO.ListInfoDTO;
import ru.mechtatell.DTO.UserDTO;
import ru.mechtatell.DTO.UserListDTO;
import ru.mechtatell.Models.UserComparator;
import ru.mechtatell.Models.User;
import ru.mechtatell.Services.UsersListService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lists")
public class ListController {

    private final UsersListService service;

    @Autowired
    public ListController(UsersListService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserListDTO> getList(@PathVariable int id) throws NotFoundException {
        UserListDTO list = UserListDTO.from(service.findListById(id));
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Long> createList(@RequestBody UserListDTO userList) {
        long listId = service.createList(userList.toUserList());
        return ResponseEntity.ok(listId);
    }

    @PostMapping("{id}/elements")
    public ResponseEntity<Long> addUser(@RequestBody UserDTO user,
                                        @PathVariable(name = "id") int listId) throws NotFoundException {
        long userId = service.addUserToList(user.toUser(), listId);
        return ResponseEntity.ok(userId);
    }

    @GetMapping
    public ResponseEntity<List<ListInfoDTO>> getLists() {
        return ResponseEntity.ok(service.findAllLists().stream()
                .map(ListInfoDTO::from)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("{id}/elements/{user_id}")
    public ResponseEntity remove(@PathVariable(name = "id") long listId,
                                 @PathVariable(name = "user_id") long userId) throws NotFoundException {
        service.deleteUser(userId, listId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/elements/{user_id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable(name = "id") long listId,
                                                @PathVariable(name = "user_id") long userId) throws NotFoundException {
        User user = service.findUserById(userId, listId);
        return ResponseEntity.ok(UserDTO.from(user));
    }

    @GetMapping("{id}/size")
    public ResponseEntity<Integer> getSize(@PathVariable long id) throws NotFoundException {
        int size = service.getListSize(id);
        return ResponseEntity.ok(size);
    }

    @PutMapping("{id}/elements")
    public ResponseEntity<Long> addElementsToList(@RequestBody List<UserDTO> usersDTO, @PathVariable long id) {
        List<User> users = usersDTO.stream()
                .map(UserDTO::toUser)
                .collect(Collectors.toList());

        long countOfAdded = service.addUsersToList(users, id);
        return ResponseEntity.ok(countOfAdded);
    }

    @GetMapping("{id}/find")
    public ResponseEntity<Integer> getOccurrencesCount(@PathVariable long id,
                                                       @RequestParam(name = "element") UserDTO userDTO) throws NotFoundException {
        int occurrencesCount = service.findOccurrencesCount(userDTO.toUser(), id);
        return ResponseEntity.ok(occurrencesCount);
    }

    @GetMapping("{id}/shuffle")
    public ResponseEntity<UserListDTO> shuffle(@PathVariable long id) throws NotFoundException {
        UserListDTO shuffledList = UserListDTO.from(service.shuffle(id));
        return ResponseEntity.ok(shuffledList);
    }

    @GetMapping("{id}/sort")
    public ResponseEntity<UserListDTO> sort(@PathVariable long id,
                                            @RequestParam(name = "comparator", required = false) String comparatorName) throws NotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        UserListDTO sortedList = UserListDTO.from(service.sort(id, comparatorName));
        return ResponseEntity.ok(sortedList);
    }

    @PostMapping("/comparator")
    public ResponseEntity<Long> createComparator(@RequestBody UserComparator userComparator) {
        long comparatorId = service.createComparator(userComparator);
        return ResponseEntity.ok(comparatorId);
    }
}
