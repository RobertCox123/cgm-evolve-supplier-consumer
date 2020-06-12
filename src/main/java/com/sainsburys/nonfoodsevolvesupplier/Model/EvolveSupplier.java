package com.sainsburys.nonfoodsevolvesupplier.Model;

/*<ns0:supplierFullDTO
        xmlns:ns1="http://www.micros.com/creations/core/domain/dto/v1p0/simple" xmlns:ns0="http://www.micros.com/creations/core/domain/dto/v1p0/full">
<ns0:code>1234</ns0:code>
<ns0:billingCode>
<ns1:code>OPAL TEST BILLING CODE1</ns1:code>
</ns0:billingCode>
<ns0:businessUnit>
<ns1:code>BUSINESS UNIT SAINSBURYS</ns1:code>
</ns0:businessUnit>
<ns0:email>supplier.contactemail@supplier.co.uk</ns0:email>
<ns0:name>Name of Supplier</ns0:name>
<ns0:status>AWAITING REGISTRATION</ns0:status>
<ns0:supplierCodeConfirmed>false</ns0:supplierCodeConfirmed>
<ns0:potentialSupplier>false</ns0:potentialSupplier>
<ns0:supplierContactName>Rob</ns0:supplierContactName>ns0:supplierContactName>
<ns0:supplierType> <ns1:code>SUPPLIER TYPEA</ns1:code> </ns0:supplierType>
</ns0:supplierFullDTO>*/

/*<>
<SupplierCode>260053</SupplierCode>
<SupplierName>Zhejiang Shengli Plastic Co Ltd</SupplierName>
<ParentSupplierCode>L000504</ParentSupplierCode>
<ParentSupplierName>Zhejiang Shengli Plastic Co Ltd</ParentSupplierName>
<OwnBrand>Yes</OwnBrand>
<Branded>No</Branded>
<Status_flag_Ind>I</Status_flag_Ind>
</SupplierSite>*/



    public  class EvolveSupplier {

        private String SupplierCode;
        private String SupplierName;
        private String AddressLine1;
        private String AddressLine2;
        private String AddressLine3;
        private String City;
        private String Province;
        private String Country;
       // private String SupplierChannel;
        private String Contact;
        private String Email;
        private String SupplierStatus;
       // private String SiteDivision;
        private String Action;




        public EvolveSupplier(String SupplierCode, String SupplierName, String AddressLine1, String AddressLine2, String AddressLine3,
                              String City, String Province, String Country,
                              //String SupplierChannel,
                              String Contact, String Email,
                              String SupplierStatus,
                              //String SiteDivision,
                              String Action) {
            this.SupplierCode = SupplierCode;
            this.SupplierName = SupplierName;
            this.AddressLine1 = AddressLine1;
            this.AddressLine2 = AddressLine2;
            this.AddressLine3 = AddressLine3;
            this.City = City;
            this.Province = Province;
            this.Country = Country;
          //  this.SupplierChannel = SupplierChannel;
            this.Contact = Contact;
            this.Email = Email;
            this.SupplierStatus = SupplierStatus;
          //  this.SiteDivision = SiteDivision;
            this.Action = Action;



        }


        public String getSupplierCode() {
            return SupplierCode;
        }

        public void setSupplierCode(String supplierCode) {
            SupplierCode = supplierCode;
        }

        public String getSupplierName() {
            return SupplierName;
        }

        public void setSupplierName(String supplierName) {
            SupplierName = supplierName;
        }

        public String getAddressLine1() {
            return AddressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            AddressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return AddressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            AddressLine2 = addressLine2;
        }

        public String getAddressLine3() {
            return AddressLine3;
        }

        public void setAddressLine3(String addressLine3) {
            AddressLine3 = addressLine3;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getProvince() {
            return Province;
        }

        public void setProvince(String province) {
            Province = province;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }

       // public String getSupplierChannel() {
       //     return SupplierChannel;
       // }

        public String getContact() {
            return Contact;
        }

        public void setContact(String contact) {
            Contact = contact;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getSupplierStatus() {
            return SupplierStatus;
        }

        public void setSupplierStatus(String supplierStatus) {
            SupplierStatus = supplierStatus;
        }

       // public String getSiteDivision() {
       //     return SiteDivision;
       // }

        //public void setSiteDivision(String siteDivision) {
        //    SiteDivision = siteDivision;
        //}

        public String getAction() {
            return Action;
        }

        public void setAction(String action) {
            Action = action;
        }
    }

