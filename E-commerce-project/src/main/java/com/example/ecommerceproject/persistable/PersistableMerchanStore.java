package com.example.ecommerceproject.persistable;
import com.example.ecommerceproject.dto.MerchantStoreDto;
import com.example.ecommerceproject.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersistableMerchanStore {

    @JsonProperty("user")
    private UserDto userDto;

    @JsonProperty("merchantStore")
    private MerchantStoreDto merchantStoreDto;

    public PersistableMerchanStore(UserDto userDto, MerchantStoreDto merchantStoreDto) {
        this.userDto = userDto;
        this.merchantStoreDto = merchantStoreDto;
    }

    public PersistableMerchanStore() {
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public MerchantStoreDto getMerchantStoreDto() {
        return merchantStoreDto;
    }

    public void setMerchantStoreDto(MerchantStoreDto merchantStoreDto) {
        this.merchantStoreDto = merchantStoreDto;
    }

}
