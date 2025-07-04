//package com.example.create.scheduler;
//
//import com.example.create.model.EntityProxity;
//import com.example.create.model.EntityStatus;
//import com.example.create.model.Order;
//import com.example.create.repository.EntityProxityRepository;
//import com.example.create.service.OrderService;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Slf4j
//@Component
//public class OrderRetryJob implements Job {
//    @Autowired
//    private EntityProxityRepository entityProxityRepository;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Override
//    public void execute(JobExecutionContext context) {
//        // Fetch all orders with FAILED status
//        List<EntityProxity> failedOrders = entityProxityRepository.findByEntyStat(EntityStatus.FAILED);
//
//        for (EntityProxity entityProxity : failedOrders) {
//            System.out.println("Retrying Order ID: " + entityProxity.getEntyId());
//            Order order = orderService.getOrderById(entityProxity.getEntyId());
//            //System.out.print("tring to process the failed order");
//            orderService.processOrder(order);
//            //System.out.println("order processed");
//        }
//
//    }
//}
