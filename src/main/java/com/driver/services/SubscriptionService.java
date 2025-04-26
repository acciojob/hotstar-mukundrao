package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay


        Optional<User> optionalUser = userRepository.findById(subscriptionEntryDto.getUserId());
        if(!optionalUser.isPresent()){
            return null;
        }
        User user = optionalUser.get();
        //return null;
        Subscription subscription = new Subscription();
        SubscriptionType subscriptionType = subscriptionEntryDto.getSubscriptionType();
        int noOfScreensRequired = subscriptionEntryDto.getNoOfScreensRequired();
        subscription.setSubscriptionType(subscriptionType);
        subscription.setNoOfScreensSubscribed(noOfScreensRequired);
        if(subscriptionType==SubscriptionType.BASIC){
            subscription.setTotalAmountPaid(500+(200*noOfScreensRequired));
        }
        else if(subscriptionType==SubscriptionType.PRO){
            subscription.setTotalAmountPaid(800+(250*noOfScreensRequired));
        }
        else if(subscriptionType==SubscriptionType.ELITE){
            subscription.setTotalAmountPaid(1000+(350*noOfScreensRequired));
        }
        user.setSubscription(subscription);
        subscription.setUser(user);

        userRepository.save(user);

        return subscription.getTotalAmountPaid();



    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            return null;
        }
        User user = optionalUser.get();
        Subscription subscription = user.getSubscription();
        SubscriptionType subscriptionType = subscription.getSubscriptionType();
        int noOfScreensRequired = subscription.getNoOfScreensSubscribed();
        if(subscriptionType==SubscriptionType.BASIC){
            int initialAmount = subscription.getTotalAmountPaid();
            subscription.setSubscriptionType(SubscriptionType.PRO);
            subscription.setTotalAmountPaid(800+(250*noOfScreensRequired));
            int updatedAmount = subscription.getTotalAmountPaid();
            user.setSubscription(subscription);
            userRepository.save(user);
            return updatedAmount-initialAmount;
        }
        else if(subscriptionType==SubscriptionType.PRO){
            int initialAmount = subscription.getTotalAmountPaid();
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            subscription.setTotalAmountPaid(1000+(350*noOfScreensRequired));
            int updatedAmount = subscription.getTotalAmountPaid();
            user.setSubscription(subscription);
            userRepository.save(user);
            return updatedAmount-initialAmount;
        }
        else if(subscriptionType==SubscriptionType.ELITE){
            throw new Exception("Already the best Subscription");
        }

        return null;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> allSubscriptions = subscriptionRepository.findAll();
        int totalRevenue = 0;
        for(Subscription subscription : allSubscriptions){
            totalRevenue+=subscription.getTotalAmountPaid();
        }
        //return null;
        return totalRevenue;
    }

}
