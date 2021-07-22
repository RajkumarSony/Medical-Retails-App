
package models;

import java.sql.Date;

/**
 *
 * @author the.bugs.cracker
 */
public class ModelTable3 {
    
    String medicine_id, medicine_name, company_name, quantity;
    Date mfg_date, expiry_date, purchage_date;
    float price, total_price;

    public ModelTable3(Date purchage_date, String medicine_id, String medicine_name, String company_name, Date mfg_date, 
             Date expiry_date, String quantity, float price, float total_price) {
        
        this.purchage_date = purchage_date;
        this.medicine_id = medicine_id;
        this.medicine_name = medicine_name;
        this.company_name = company_name;
        this.mfg_date = mfg_date;
        this.expiry_date = expiry_date;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
    }
    
    public Date getPurchage_date(){
        return purchage_date;  
    }
    public void setPurchage_date(Date purchage_date){
        this.purchage_date = purchage_date;
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
    public float getTotal_price(){
        return total_price;  
    }
    public void setTotal_price(float total_price){
        this.total_price = total_price;
    }
}
