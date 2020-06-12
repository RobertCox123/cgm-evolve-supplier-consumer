package com.sainsburys.nonfoodsevolvesupplier.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Getter
public class DataConfig {


    @Value("${user_name}")
    private String Username;

    @Value("${password}")
    private String PASSWORD;

    @Value("${evolve_user_create_url}")
    private String CREATE_USER_URL;
    @Value("${evolve_contact_create_url}")
    private String CREATE_CONTACT_URL;

    @Value("${evolve_supplier_create_url}")
    private String CREATE_URL;

    @Value("${evolve_supplier_put_url}")
    private String PUT_URL;

    @Value("${evolve_supplier_get_url}")
    private String GET_URL;

    @Value("${bootstrapservers}")
    private String bootstrapservers;

    @Value("${consumer_group_id}")
    private String consumer_group_id;

    @Value("${max_poll_records}")
    private String max_poll_records;

    @Value("${auto_offset_reset}")
    private String auto_offset_reset;

    @Value("${enable_auto_commit}")
    private String enable_auto_commit;

    @Value("${topic_name}")
    private String myTopicName;

    //default Evolve Values

    @Value("${CONTACTROLE1}")
    private  String role1;
    @Value("${CONTACTROLE2}")
    private  String role2;
    @Value("${CONTACTROLE3}")
    private  String role3;
    @Value("${CONTACTROLE4}")
    private  String role4;
    @Value("${CONTACTROLE5}")
    private  String role5;
    @Value("${CONTACTROLE6}")
    private  String role6;
    @Value("${CONTACTROLE7}")
    private  String role7;
    @Value("${CONTACTROLE8}")
    private  String role8;
    @Value("${CONTACTROLE9}")
    private  String role9;

    @Value("${SITES}")
    private  String sites;

    @Value("${BILLINGCODE}")
    private  String billingcode;

    @Value("${BUSINESSUNIT}")
    private  String businessunit;

    @Value("${SUPPLIERTYPE}")
    private  String suppliertype;

    @Value("${USERROLE}")
    private  String userrole;



    public String getPUT_URL() {
        return PUT_URL;
    }

    public String getUsername() {
        return Username;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getCREATE_URL() {
        return CREATE_URL;
    }
    public String getCREATE_USER_URL() {
        return CREATE_USER_URL;
    }
    public String getCREATE_CONTACT_URL() {
        return CREATE_CONTACT_URL;
    }

    public String getGET_URL() {
        return GET_URL;
    }

    public String getBootstrapservers() {
        return bootstrapservers;
    }

    public String getConsumer_group_id() {
        return consumer_group_id;
    }

    public String getMax_poll_records() {
        return max_poll_records;
    }

    public String getAuto_offset_reset() {
        return auto_offset_reset;
    }

    public String getEnable_auto_commit() {
        return enable_auto_commit;
    }

    public String getMyTopicName() {
        return myTopicName;
    }

    public String getRole1() {
        return role1;
    }

    public String getRole2() {
        return role2;
    }

    public String getRole3() {
        return role3;
    }

    public String getRole4() {
        return role4;
    }

    public String getRole5() {
        return role5;
    }

    public String getRole6() {
        return role6;
    }

    public String getRole7() {
        return role7;
    }

    public String getRole8() {
        return role8;
    }

    public String getRole9() {
        return role9;
    }

    public String getSites() {
        return sites;
    }

    public String getBillingcode() {
        return billingcode;
    }

    public String getBusinessunit() {
        return businessunit;
    }

    public String getSuppliertype() {
        return suppliertype;
    }

    public String getUserrole() {
        return userrole;
    }
}
