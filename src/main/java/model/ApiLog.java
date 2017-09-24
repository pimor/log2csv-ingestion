package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created on 19/09/2017.
 */
public class ApiLog implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLog.class);

    private String messageId;
    private String date;
    private String hour;
    private String var001;
    private String accountId;
    private String gatewayId;
    private String country;
    private String status;
    private String messagePrice;
    private String transitCost;
    private String routeCost;


    public ApiLog(String messageId, String date, String hour, String var001, String accountId, String gatewayId,
                  String country, String status, String messagePrice, String transitCost, String routeCost) {
        this.messageId = messageId;
        this.date = date;
        this.hour = hour;
        this.var001 = var001;
        this.accountId = accountId;
        this.gatewayId = gatewayId;
        this.country = country;
        this.status = status;
        this.messagePrice = messagePrice;
        this.transitCost = transitCost;
        this.routeCost = routeCost;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getVar001() {
        return var001;
    }

    public void setVar001(String var001) {
        this.var001 = var001;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessagePrice() {
        return messagePrice;
    }

    public void setMessagePrice(String messagePrice) {
        this.messagePrice = messagePrice;
    }

    public String getTransitCost() {
        return transitCost;
    }

    public void setTransitCost(String transitCost) {
        this.transitCost = transitCost;
    }

    public String getRouteCost() {
        return routeCost;
    }

    public void setRouteCost(String routeCost) {
        this.routeCost = routeCost;
    }


    @Override
    public String toString() {
        return messageId + ',' +
               date + ',' +
               hour + ',' +
               var001 + ',' +
               accountId + ',' +
               gatewayId + ',' +
               country + ',' +
               status + ',' +
               messagePrice + ',' +
               transitCost + ',' +
               routeCost;
    }
}