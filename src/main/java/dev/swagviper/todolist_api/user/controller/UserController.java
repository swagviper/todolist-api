package dev.swagviper.todolist_api.user.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.swagviper.todolist_api.user.IUserRepository;
import dev.swagviper.todolist_api.user.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    /*
    GET - Buscar uma informação
    POST - Adicionar um dado/informação
    PUT - Alterar um dado/info
    DELETE - Remover um dado
    PATCH - Alterar somente uma parte da info/dado

    methods http.
    */

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){

        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um usuario com este mesmo nome");
        }

        var passwordHashred = BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);
        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.ok().body(userCreated);
    }


}
