/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.service;

import com.micro.entities.Customers;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author omozegieaziegbe
 */
@Named
@SessionScoped
public class ClientService implements Serializable {

    private List<Customers> customersList;
    private Customers customers;
    WebTarget target;
    private boolean showDo=false;
    private boolean showUndo=false;
   // Client client;
   private Client client = ClientBuilder.newClient();

//    @PostConstruct
//    public void init() {
////        client = ClientBuilder.newClient();
////        target = client
////                .target("http://localhost:8081/api/customers/");
//  //      customers = new Customers();
//        populateList();
//    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    public Customers[] getAllCustomers() {
        return target
                .request()
                .get(Customers[].class);
    }

    @PostConstruct
    public void populateList() {
        target = client.target("http://localhost:8081/api/customers/");
        setCustomersList(target.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Customers>>() {
                }));
    }
    
    public String addCustomer() {
//        Customers c = new Customers(this.fullname, this.email, this.address, this.city, this.course);
        Customers c = new Customers();
        c.setFullname(customers.getFullname());
        c.setEmail(customers.getEmail());
        c.setAddress(customers.getAddress());
        c.setCity(customers.getCity());
        c.setCourse(customers.getCourse());
        target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(c, MediaType.APPLICATION_JSON));
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage("Customer Added Successfully");
        context.addMessage("", message);
        context.getExternalContext().getFlash().setKeepMessages(true);
        populateList();
     //   getAllCustomers();
        this.customers.setAddress(null);
        this.customers.setCourse("");
        this.customers.setEmail(null);
        this.customers.setFullname(null);
        this.customers.setCity(null);
        return "index?faces-redirect=true";
    }

    public String clear() {
        this.customers = null;
        this.showDo = false;
        //this.customers = new Customers();
        return "index?faces-redirect=true";
    }

    public String deleteCustomer() {
        target.path("{id}").resolveTemplate("id", customers.getId())
                .request(MediaType.APPLICATION_JSON).delete();
        populateList();
        //getAllCustomers();
        return "index?faces-redirect=true";
    }

    public String updateCustomer() {
        target.path("{id}").resolveTemplate("id", customers.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(customers, MediaType.APPLICATION_JSON));
        FacesContext context = FacesContext.getCurrentInstance();
         FacesMessage message = new FacesMessage("Customer Details for " + customers.getFullname() + " Updated Successfully");
        context.addMessage("", message);
        context.getExternalContext().getFlash().setKeepMessages(true);
       // getAllCustomers();
        populateList();
        this.customers.setAddress("");
        this.customers.setCourse("");
        this.customers.setEmail("");
        this.customers.setFullname("");
        this.customers.setCity("");
        this.showDo = false;
       return "index?faces-redirect=true"; 
    }

    public List<Customers> getCustomersList() {
        if (customersList == null) {
            populateList();
        }
        return customersList;
    }

    public void setCustomersList(List<Customers> customersList) {
        this.customersList = customersList;
    }

    public Customers getCustomers() {
        if (customers == null) {
            customers = new Customers();
        }
        return this.customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }
    public void showDetails(Customers customer) {
        this.customers = customer;
        this.showDo = true;
//        Map<String, Object> options = new HashMap<String, Object>();
//        options.put("resizable", false);
//        options.put("modal", true);
//        options.put("height", 500);
//        options.put("width", 700);
//        options.put("contentWidth", "100%");
//        options.put("contentHeight", "100%");
//        options.put("headerElement", "customheader");
//        PrimeFaces.current().dialog().openDynamic("customer", options, null);
    }

    public boolean isShowDo() {
        return showDo;
    }

    public void setShowDo(boolean showDo) {
        this.showDo = showDo;
    }

    public boolean isShowUndo() {
        return showUndo;
    }

    public void setShowUndo(boolean showUndo) {
        this.showUndo = showUndo;
    }

    

}
