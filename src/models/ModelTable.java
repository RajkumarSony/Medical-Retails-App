
package models;

import java.sql.Date;

/**
 *
 * @author the.bugs.cracker
 */
public class ModelTable {
    
    String medicine_id, medicine_name, company_name, batch_number, quantity;
    Date mfg_date, expiry_date;
    float price;

    public ModelTable(String medicine_id, String medicine_name, String company_name, String batch_number, 
            Date mfg_date, Date expiry_date, String quantity, float price) {
        
        this.medicine_id = medicine_id;
        this.medicine_name = medicine_name;
        this.company_name = company_name;
        this.batch_number = batch_number;
        this.mfg_date = mfg_date;
        this.expiry_date = expiry_date;
        this.quantity = quantity;
        this.price = price;
    }
    
    
    public String getMedicine_id(){
        return medicine_id;  
    }
    public void setMedicine_id(String medicine_id){
        this.medicine_id = medicine_id;
    }
    public String getMedicine_name(){
        return medicine_name;  
    }
    public void setMedicine_name(String medicine_name){
        this.medicine_name = medicine_name;
    }
    public String getCompany_name(){
        return company_name;  
    }
    public void setCompany_name(String company_name){
        this.company_name = company_name;
    }
    public String getBatch_number(){
        return batch_number;  
    }
    public void setBatch_number(String batch_number){
        this.batch_number = batch_number;
    }
    public Date getMfg_date(){
        return mfg_date;  
    }
    public void setMfg_date(Date mfg_date){
        this.mfg_date = mfg_date;
    }
    public Date getExpiry_date(){
        return expiry_date;  
    }
    public void setExpiry_date(Date expiry_date){
        this.expiry_date = expiry_date;
    }
    public String getQuantity(){
        return quantity;  
    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }
    public float getPrice(){
        return price;  
    }
    public void setPrice(float price){
        this.price = price;
    }

}
