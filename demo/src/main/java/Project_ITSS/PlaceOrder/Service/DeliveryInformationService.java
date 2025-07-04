package Project_ITSS.PlaceOrder.Service;

import Project_ITSS.PlaceOrder.Entity.DeliveryInformation;
import Project_ITSS.PlaceOrder.Repository.DeliveryInfoRepository_PlaceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryInformationService {

    @Autowired
    private DeliveryInfoRepository_PlaceOrder deliveryInformationRepo;
    public int saveDeliveryInfo(DeliveryInformation deliveryInformation) {
        return deliveryInformationRepo.saveDeliveryInfo(deliveryInformation);
    }

    public DeliveryInformation createDeliveryInfo(String name, String phone, String email, String address, String province, String delivery_message,int delivery_fee){
        DeliveryInformation deliveryInformation = new DeliveryInformation();
        deliveryInformation.setName(name);
        deliveryInformation.setPhone(phone);
        deliveryInformation.setEmail(email);
        deliveryInformation.setAddress(address);
        deliveryInformation.setProvince(province);
        deliveryInformation.setDelivery_message(delivery_message);
        deliveryInformation.setDelivery_fee(delivery_fee);
        return deliveryInformation;
    }
} 