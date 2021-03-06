package pe.edu.upc.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import pe.edu.upc.service.AdminService;
import pe.edu.upc.service.UserService;
import pe.edu.upc.exception.ModelNotFoundException;
import pe.edu.upc.model.entity.Admin;
import pe.edu.upc.model.entity.UserApp;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    //@ApiOperation("")
    @GetMapping
    public ResponseEntity<List<Admin>> listar(){
        List<Admin> admins = new ArrayList<>();
        admins =adminService.listar();
        return new ResponseEntity<List<Admin>>(admins,HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Admin> listarId(@PathVariable("id") Integer id){
        Optional<Admin> admin =adminService.listId(id);
        if(!admin.isPresent()){
            throw new ModelNotFoundException("ID:"+ id);
        }

        return new ResponseEntity<Admin>(admin.get(),HttpStatus.OK);
    }

    //@CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<Admin> registrar(@Valid @RequestBody String admin){
        JSONObject js = new JSONObject(admin);
        UserApp userNew = new UserApp(
            js.getString("name"),
            js.getString("lastName"),
            js.getString("username"),            
            js.getString("password"), 
            js.getLong("phone")
        );
        userNew = userService.registrar(userNew);
        Admin adminNew= new Admin(
            userNew,
            js.getString("cargo"),
            userNew.getId()
        );
        adminNew = adminService.registrar(adminNew);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(adminNew.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Admin> actualizar(@Valid @RequestBody Admin admin){
        adminService.modificar(admin);
        return new ResponseEntity<Admin>(HttpStatus.OK);
    }

    @DeleteMapping(value="/{id}")
    public void eliminar(@PathVariable Integer id){
        Optional<Admin> admin = adminService.listId(id);
        if(!admin.isPresent()){
            throw new ModelNotFoundException("ID: "+id);
        } else {
            adminService.eliminar(id);
        }
    }
}