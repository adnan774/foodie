package com.example.foodie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodie.entity.User;
import com.example.foodie.service.UserService;
import com.example.foodie.utility.AppConstants;
import com.example.foodie.utility.Status;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
		response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
		response.put("users", users);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
		User user = userService.getUserById(id);

		if (user == null) {  
			Map<String, Object> response = new HashMap<>();
			response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
			response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.ErrorMessages.USER_NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // ✅ Return 404
		}

		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
		response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
		response.put("user", user);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/signup")
	public ResponseEntity<Map<String, Object>> signupUser(@RequestBody User user) {
		User createdUser = userService.createUser(user);
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
		response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
		response.put("user", createdUser);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		if (userService.getUserById(id) == null) {  
			response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
			response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.ErrorMessages.USER_NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // ✅ Return 404
		}

		userService.deleteUser(id);
		response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
		response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> loginRequest) {
	    String username = loginRequest.get("username");
	    String password = loginRequest.get("password");

	    User user = userService.getUserByUsername(username).orElse(null);
	    Map<String, Object> response = new HashMap<>();

	    if (user != null && user.getPassword().equals(password)) { 
	        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
	        response.put(AppConstants.ResponseKeys.MESSAGE, "Login successful");
	        response.put("user", user); 

	        return ResponseEntity.ok(response);
	    } else {
	        response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
	        response.put(AppConstants.ResponseKeys.MESSAGE, "Invalid username or password");
	        return ResponseEntity.badRequest().body(response);
	    }
	}
}
