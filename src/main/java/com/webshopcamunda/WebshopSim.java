package com.webshopcamunda;

import com.google.errorprone.annotations.Var;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Random;

@Component
public class WebshopSim {
    @Autowired
    private ZeebeClient zeebeClient;
    private final Random random = new Random();

    @ZeebeWorker(type = "messageToWebshop", autoComplete = true)
    public void messageToWebshop() {
        HashMap<String, String> variables = new HashMap<>();
        zeebeClient.newPublishMessageCommand()
                .messageName("messageToWebshop")
                .correlationKey("messageToWebshop")
                .send();
    }

    @ZeebeWorker(type = "checkDatabase", autoComplete = true)
    public HashMap<String,Boolean> checkDatabase() {
        HashMap<String,Boolean> variables = new HashMap<>();
        if(random.nextBoolean()) {
            variables.put("isEmpty", true);
        } else {
            variables.put("isEmpty", false);
        }
        System.out.println(variables.get("isEmpty"));
        return variables;
    }

    @ZeebeWorker(type = "notifyCustomer", autoComplete = true)
    public void notifyCustomer(@Variable Boolean isEmpty) {
        HashMap<String,Boolean> variables = new HashMap<>();
        variables.put("isEmpty", isEmpty);
        System.out.println(variables.get("isEmpty"));
        /*zeebeClient.newPublishMessageCommand()
                .messageName("notifyCustomer")
                .correlationKey("sent")
                .variables(variables)
                .send();*/
    }

    @ZeebeWorker(type = "orderData", autoComplete = true)
    public void orderData() {
        HashMap<String,String> variables = new HashMap<>();
        variables.put("orderId", random.nextLong() + "");
        variables.put("customerId", random.nextLong() + "");
        variables.put("customerName", random.nextLong() + "");
        variables.put("customerAddress", random.nextLong() + "");
        zeebeClient.newPublishMessageCommand()
                .messageName("orderData")
                .correlationKey("data")
                .variables(variables)
                .send();
    }

    @ZeebeWorker(type = "processOrder", autoComplete = true)
    public void processOrder() {
        System.out.println("Processing...");
    }

    @ZeebeWorker(type = "confirm", autoComplete = true)
    public void confirm() {
        System.out.println("Confirmation sent");
    }

    @ZeebeWorker(type = "messageToWarehouse", autoComplete = true)
    public void messageToWarehouse(@Variable Long customerName, @Variable Long customerAddress) {
        HashMap<String,String> variables = new HashMap<>();
        variables.put("customerName", customerName.toString());
        variables.put("customerAddress", customerAddress.toString());
        zeebeClient.newPublishMessageCommand()
                .messageName("messageToWarehouse")
                .correlationKey("messageToWarehouse")
                .variables(variables)
                .send();
    }

    @ZeebeWorker(type = "findProduct", autoComplete = true)
    public void findProduct(){
        System.out.println("Finding product");
    }
}
