package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User savedUser = userRepository.save(user);
        return savedUser.getId();
        //return null;
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository


        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            return null;
        }
        User user = optionalUser.get();
        SubscriptionType subscriptionType = user.getSubscription().getSubscriptionType();
        List<WebSeries> webSeriesForUser = webSeriesRepository.findAll();
        int age = user.getAge();
        int availableCountOfWebSeriesViewable = 0;
        for(WebSeries webSeries : webSeriesForUser){
            if(age>=webSeries.getAgeLimit()){
                if(subscriptionType==SubscriptionType.ELITE){
                    availableCountOfWebSeriesViewable+=1;
                }
                else if(subscriptionType==SubscriptionType.PRO){
                    if(webSeries.getSubscriptionType()!=SubscriptionType.ELITE){
                        availableCountOfWebSeriesViewable+=1;
                    }
                }
                else if(subscriptionType==SubscriptionType.BASIC){
                    if(webSeries.getSubscriptionType()==SubscriptionType.BASIC){
                        availableCountOfWebSeriesViewable+=1;
                    }
                }

            }
        }
        return availableCountOfWebSeriesViewable;
        //return null;
    }


}
