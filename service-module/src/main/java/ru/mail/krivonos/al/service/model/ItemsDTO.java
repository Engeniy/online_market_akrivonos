package ru.mail.krivonos.al.service.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Items", namespace = "https://www.it-academy.by")
public class ItemsDTO {

    private List<ItemDTO> items;

    public List<ItemDTO> getItems() {
        return items;
    }

    @XmlElement(name = "Item", namespace = "https://www.it-academy.by")
    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
