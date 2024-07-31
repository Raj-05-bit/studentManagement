package com.google.studentManagement.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.studentManagement.dto.MyUser;

import com.google.studentManagement.dto.Student;
import com.google.studentManagement.helper.Aes;
import com.google.studentManagement.helper.HelperForSendingMail;
import com.google.studentManagement.repository.MyUserRepository;
import com.google.studentManagement.repository.StudentRepository;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;







@Controller
@MultipartConfig
public class MyController {
 
@Autowired
MyUserRepository myUserRepository;

@Autowired
HelperForSendingMail helper;


@Autowired
StudentRepository studentRepository;




@GetMapping("/")
public String getMethodName() {
    return "home.html";
}
@GetMapping("/signup")
public String signup( ModelMap map)
{
    map.put("myUser", new MyUser());
    return "signup1.html";
}
@GetMapping("/login")
public String login()
{
    return "login.html";
}

@PostMapping("/signup")
public String loadSignup(@Valid MyUser myUser, BindingResult result, ModelMap map)
{
    if(myUserRepository.existsByEmail(myUser.getEmail()))
    {
        result.rejectValue("email","error.email","email aldready exist");
    }
    if(result.hasErrors())
    {
        return "signup1.html";
    }
    else{

        int otp=new Random().nextInt(100000,1000000);
        myUser.setOtp(otp);
        myUser.setPassword(Aes.encrypt(myUser.getPassword(), "123"));
        // System.out.println(myUser.getOtp());
        helper.sendEmail(myUser);
        myUserRepository.save(myUser);
        map.put("success", "otp sent success,check your email");
        map.put("id",myUser.getId());
        return "otp.html";
    }
}


@PostMapping("/verified-otp")
  public String otpVerification(@RequestParam int id,@RequestParam int otp,ModelMap map) {
      
   MyUser myuser= myUserRepository.findById(id).orElseThrow();

   if (myuser.getOtp()==otp) {
    myuser.setVerified(true);
    myUserRepository.save(myuser);  // save in data base
    map.put("success", "otp verified successfully welcome"); // to pass the data in front end
    return "home.html";
   }
   else{
           map.put("failure", "Invalid otp");
           map.put("id", myuser.getId());
           return "otp.html";
        }


  }


  @PostMapping("/login")
  public String postMethodName(HttpSession session,@RequestParam String email,@RequestParam String password,ModelMap map) {
  
    MyUser myUser=myUserRepository.findByEmail(email);
        if (myUser==null) 
        {
            map.put("failure", "Invalid Email Address");
            return "login.html";
        }
        else{
            if (password.equals(Aes.decrypt(myUser.getPassword(), "123"))) {
                if (myUser.isVerified()) {
                    session.setAttribute("user","myUser");
                    map.put("success", "login successfull");
                    return "home.html";
                }
                else{
                    int otp=new Random().nextInt(100000,1000000);
                    myUser.setOtp(otp);
                    myUser.setPassword(Aes.encrypt(myUser.getPassword(), "123"));

                    System.out.println(myUser.getOtp());
                    
                    helper.sendEmail(myUser);
                    myUserRepository.save(myUser);
                    map.put("success", "otp sent success,check your email");
                    map.put("id",myUser.getId());
                    return "otp.html";
                }

            }
            else{
                map.put("failure", "Invalid Password");
                return "login.html";
            }


        }
  }
        
  @GetMapping("/logout")
        public String logout(HttpSession session,ModelMap map)
        {
            session.removeAttribute("user");
            map.put("success", "logout successfuly");
            return "home.html";
        }

@GetMapping("/insert")
public String insert(HttpSession session,ModelMap map) {
    if (session.getAttribute("user")!=null) {
        return "insert.html";
    }
    else{
        map.put("faluere", "Invalid Session");
        return "login.html";
    }
}
@PostMapping("/insert")
public String print(Student student,HttpSession session, ModelMap map,@RequestParam MultipartFile gg) {
    if (session.getAttribute("user")!=null) {
        student.setPicture(addToCloudinary(gg));
        studentRepository.save(student);
        map.put("success", "data sved sucessfully");
        return "home.html";
    }
    else{
        map.put("faluere", "Invalid Session");
        return "login.html";
    }
}

    
@GetMapping("/fetch")
public String fetch(HttpSession session,ModelMap map) {
    if (session.getAttribute("user")!=null) {
        return "fetch.html";
    }
    else{
        map.put("faluere", "Invalid Session");
        return "login.html";
    }
}
  
@GetMapping("/update")
public String update(HttpSession session,ModelMap map) {
    if (session.getAttribute("user")!=null) {
        return "update.html";
    }
    else{
        map.put("faluere", "Invalid Session");
        return "login.html";
    }
}
    
@GetMapping("delete/")
public String getMethodName(HttpSession session,ModelMap map) {
    if (session.getAttribute("user")!=null) {
        return "delete.html";
    }
    else{
        map.put("faluere", "Invalid Session");
        return "login.html";
    }

}










public String addToCloudinary(MultipartFile image) {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "di5s7ohmo", "api_key",
				"972714922412351", "api_secret", "knoU4w-w44TtSfzkTCjOlAolc0Q", "secure", true));

		Map resume = null;
		try {
			Map<String, Object> uploadOptions = new HashMap<String, Object>();
			uploadOptions.put("folder", "Bus");
			resume = cloudinary.uploader().upload(image.getBytes(), uploadOptions);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) resume.get("url");
	}
  










// @GetMapping("/{name}")
// public String printName(@PathVariable String name,ModelMap map) {
//     map.put("name", name);
//     return "index.html";
// }

// @GetMapping("/color/{color}")
// public String printStyle(@PathVariable String color,ModelMap map) {
//     map.put("color", color);
//     return "index.html";
// }


// @GetMapping("/login")
// public String


}
// jrcu hkfp elns oqef