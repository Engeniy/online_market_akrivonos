package ru.mail.krivonos.al.service.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item", namespace = "https://www.it-academy.by")
public class ItemDTO {

    private Long id;
    private String name;
    private String uniqueNumber;
    private String price;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement(name = "name", namespace = "https://www.it-academy.by")
    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    @XmlElement(name = "uniqueNumber", namespace = "https://www.it-academy.by")
    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String getPrice() {
        return price;
    }

    @XmlElement(name = "price", namespace = "https://www.it-academy.by")
    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement(name = "description", namespace = "https://www.it-academy.by")
    public void setDescription(String description) {
        this.description = description;
    }
}
