package com.inn.cafe.serviceImpl;

import com.inn.cafe.POJO.User;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

@Autowired
    UserDao userDao;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
       log.info("Inside signUp {}",requestMap);
       try {
           if (validateSignMap(requestMap)) {
               User user = userDao.findByEmailId(requestMap.get("email"));
               if (Objects.isNull(user)) {
                   userDao.save(getUserFromMap(requestMap));
                   return CafeUtils.getResponceEntity("Successfully Registered", HttpStatus.OK);
               } else {
                   return CafeUtils.getResponceEntity("Emails allready exists.", HttpStatus.BAD_REQUEST);
               }
           } else {
               return CafeUtils.getResponceEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
           }
       } catch (Exception ex){
           ex.printStackTrace();
       }
       return CafeUtils.getResponceEntity(CafeConstants.Something_Went_Wrong,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private boolean validateSignMap(Map<String,String> requestMap){
      if( requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password"))
      {
          return true;
      }
       else {
           return false;
      }
    }
    private User getUserFromMap(Map<String,String> requestMap){
        User user=new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
}