package com.example.ecommerceproject.readable;

import com.example.ecommerceproject.dto.ReadAbleMerchantStore;
import com.example.ecommerceproject.dto.ReadAbleStoreAddress;
import com.example.ecommerceproject.dto.ReadAbleUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
@Data
public class ReadableMerchantStore {
    @JsonProperty("user")
    private ReadAbleUser readAbleUser;

    @JsonProperty("merchantStore")
    private ReadAbleMerchantStore readableMerchantStore;


    public ReadableMerchantStore(ReadAbleUser readAbleUser, ReadAbleMerchantStore readableMerchantStore) {
        this.readAbleUser = readAbleUser;
        this.readableMerchantStore = readableMerchantStore;
    }

    public ReadableMerchantStore() {
    }


    public ReadAbleUser getReadAbleUser() {
        return readAbleUser;
    }

    public void setReadAbleUser(ReadAbleUser readAbleUser) {
        this.readAbleUser = readAbleUser;
    }

    public ReadAbleMerchantStore getReadableMerchantStore() {
        return readableMerchantStore;
    }

    public void setReadableMerchantStore(ReadAbleMerchantStore readableMerchantStore) {
        this.readableMerchantStore = readableMerchantStore;
    }

}