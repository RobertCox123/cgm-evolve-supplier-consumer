package com.sainsburys.nonfoodsevolvesupplier.Listener;

import com.sainsburys.nonfoodsevolvesupplier.Config.DataConfig;
import com.sainsburys.nonfoodsevolvesupplier.Model.EvolveSupplier;
import com.sainsburys.nonfoodsevolvesupplier.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Service
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);


    int threadSleep = 120000;

    @Autowired
    DataConfig dataConfig;
    private String evolvefeed;
    @Autowired
    private Map<String, String> Country;

    private boolean commitOffsets;

    @KafkaListener(
            topics = "${topic_name}", groupId = "${consumer_group_id}", containerFactory = "kafkaListenerContainerFactory")
    public HttpStatus listen(String SupplierSite, Acknowledgment acknowledgment) throws ParserConfigurationException, InterruptedException, XPathExpressionException {
        LOGGER.info("Consumer message :" + SupplierSite );

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("Parser Config Error");
        }
        Document doc = null;
        try {
            doc = builder.parse(new InputSource(new StringReader(SupplierSite)));
        } catch (SAXException e) {
            //e.printStackTrace();
            LOGGER.error("Input message :" + SupplierSite + " failed to parse ");
            commitOffsets = true;
        }
        catch (IOException e) {
            LOGGER.error("Memory read Error");
        }
        NodeList nodeList = doc.getElementsByTagName("Supplier");
        // normalize XML response
        doc.getDocumentElement().normalize();
        Node node = nodeList.item(0);
        Element elem = (Element) node;
        EvolveSupplier evolvesupplier = new EvolveSupplier(
                elem.getElementsByTagName("SupplierCode").item(0).getTextContent(),
                elem.getElementsByTagName("SupplierName").item(0).getTextContent(),
                elem.getElementsByTagName("AddressLine1").item(0).getTextContent(),
                elem.getElementsByTagName("AddressLine2").item(0).getTextContent(),
                elem.getElementsByTagName("AddressLine3").item(0).getTextContent(),
                elem.getElementsByTagName("City").item(0).getTextContent(),
                elem.getElementsByTagName("Province").item(0).getTextContent(),
                elem.getElementsByTagName("Country").item(0).getTextContent(),
                //elem.getElementsByTagName("SupplierChannel").item(0).getTextContent(),
                elem.getElementsByTagName("ContactName").item(0).getTextContent(),
                elem.getElementsByTagName("EmailAddress").item(0).getTextContent(),
                elem.getElementsByTagName("SupplierStatus").item(0).getTextContent(),
               // elem.getElementsByTagName("Division").item(0).getTextContent(),
                elem.getElementsByTagName("Action").item(0).getTextContent()
        );


        String Ccode = (String)Country.get(evolvesupplier.getCountry());

        System.out.println(
                "Country Code " + Ccode


        );

        evolvefeed =
                "<ns0:supplierFullDTO" +
                        " xmlns:ns1=\"http://www.micros.com/creations/core/domain/dto/v1p0/simple\"" +
                        " xmlns:ns0=\"http://www.micros.com/creations/core/domain/dto/v1p0/full\">" +
                        "<ns0:code>"
                        + evolvesupplier.getSupplierCode() +
                        "</ns0:code>" +
                        "<ns0:billingCode>" +
                        "<ns1:code>" + dataConfig.getBillingcode() + "</ns1:code>" +
                        "</ns0:billingCode>" +
                        "<ns0:businessUnit>" +
                        "<ns1:code>" + dataConfig.getBusinessunit() + "</ns1:code>" +
                        "</ns0:businessUnit>" +
                        "<ns0:email>" + evolvesupplier.getEmail() + "</ns0:email>" +
                        "<ns0:name>" + evolvesupplier.getSupplierName() + "</ns0:name>" +
                        "<ns0:status>" + evolvesupplier.getSupplierStatus() + "</ns0:status>" +
                        "<ns0:supplierCodeConfirmed>true</ns0:supplierCodeConfirmed>" +
                        "<ns0:isActive>true</ns0:isActive>" +
                        "<ns0:deleted>false</ns0:deleted>" +
                        "<ns0:companyNumber>" + evolvesupplier.getSupplierCode() + "</ns0:companyNumber>" +
                        "<ns0:potentialSupplier>false</ns0:potentialSupplier>" +
                        "<ns0:supplierContactName>" + evolvesupplier.getContact() + "</ns0:supplierContactName>" +
                        "<ns0:supplierType><ns1:code>" + dataConfig.getSuppliertype() + "</ns1:code>" +
                        "</ns0:supplierType>" +
                        "<ns0:mainAddress>" +
                        "<ns0:country> " +
                        "<ns1:code>" + Ccode + "</ns1:code>" +
                        "</ns0:country>" +
                        "<ns0:line1>" + evolvesupplier.getAddressLine1() + "</ns0:line1>" +
                        "<ns0:line2>" + evolvesupplier.getAddressLine2() + " " +evolvesupplier.getAddressLine3() + "</ns0:line2>" +
                        "<ns0:line3>" + evolvesupplier.getCity() + "</ns0:line3>" +
                        "<ns0:line4>" + evolvesupplier.getProvince() + "</ns0:line4>" +
                        "</ns0:mainAddress>"+
                        "</ns0:supplierFullDTO>";

                       //System.out.println(
                        //"evolve feed " + evolvefeed);
        evolvefeed = evolvefeed.replaceAll("&","&amp;");
        byte[] bytes = evolvefeed.getBytes(StandardCharsets.UTF_8);

        String utf8evolvefeed = new String(bytes, StandardCharsets.UTF_8);

                       // evolvefeed = evolvefeed.replaceAll("&","&amp;");
        evolvefeed = utf8evolvefeed ;

                       

        System.out.println(
                "evolve feed UTF8 " + utf8evolvefeed);



        {
           // try {
                RestTemplate restTemplate = new RestTemplate();


                ResponseEntity<String> EvolveResponse;
                EvolveResponse = null;


                User evolveLPostRequest = new User();
                evolveLPostRequest.getUserName(dataConfig.getUsername());
                evolveLPostRequest.setPassword(dataConfig.getPASSWORD());


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_XML);
                headers.setBasicAuth(dataConfig.getUsername(), dataConfig.getPASSWORD());

                String geturl = dataConfig.getGET_URL() + evolvesupplier.getSupplierCode();




                HttpEntity request = new HttpEntity<>(null, headers);

               LOGGER.info("Get URL :" + geturl );
               LOGGER.info("Request Message :" + request );


            restTemplate.setErrorHandler(new ResponseErrorHandler() {
                                             public boolean hasError(ClientHttpResponse response) throws IOException {
                                                 return false;
                                             }

                                             public void handleError(ClientHttpResponse response) throws IOException {
                                             }
                                         }
            );
                try {
                    ResponseEntity<String> response = restTemplate.exchange(geturl, HttpMethod.GET, request, String.class);
                    String getmessage = response.getBody();

                    HttpStatus getStatus = response.getStatusCode();

                    if (getStatus == OK) {

                        Document getdoc = null;
                        try {
                            getdoc = builder.parse(new InputSource(new StringReader(getmessage)));
                        } catch (SAXException e) {
                            LOGGER.error("Get Response  message :" + getmessage + " failed to parse ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        XPath xPath = XPathFactory.newInstance().newXPath();

                        String expression = "/supplierFullDTO/id/text()";

                        String id = (String) xPath.compile(expression).evaluate(getdoc, XPathConstants.STRING);


                        String puturl = dataConfig.getPUT_URL() + id;
                        LOGGER.info("puturl URL :" + puturl );

                        HttpEntity putrequest = new HttpEntity<>(evolvefeed, headers);
                        ResponseEntity<String> putresponse = restTemplate.exchange(puturl, HttpMethod.PUT, putrequest, String.class);
                        HttpStatus putStatus = putresponse.getStatusCode();
                        if (putStatus == OK) {
                            LOGGER.info("Supplier" + putresponse + " successfully updated ");
                            commitOffsets = true;
                            //update offset
                        } else {
                            if (putStatus == EXPECTATION_FAILED || putStatus == BAD_REQUEST || putStatus == METHOD_NOT_ALLOWED)
                                LOGGER.error("Supplier" + putresponse + " Update rejected by Evolve");
                            commitOffsets = true;

                        }

                    } else {
                        if (getStatus == NOT_FOUND) {
                            String posturl = dataConfig.getCREATE_URL();
                            LOGGER.info("post URL :" + posturl );
                            UriComponentsBuilder builderURL = UriComponentsBuilder.fromUriString(posturl)
                                    // Add query parameter
                                    .queryParam("disableValidation", "Y");

                            HttpEntity postrequest = new HttpEntity<>(evolvefeed, headers);
                            ResponseEntity<String> postresponse = restTemplate.exchange(posturl, HttpMethod.POST, postrequest, String.class);
                            HttpStatus postStatus = postresponse.getStatusCode();
                            String postmessage = postresponse.getBody();
                            if (postStatus == OK) {
                                LOGGER.info("Supplier" + postresponse + " successfully inserted ");
                                //update offset
                                commitOffsets = true;
                                //obtain supplier id from response body
                                Document postdoc = null;
                                try {
                                    postdoc = builder.parse(new InputSource(new StringReader(postmessage)));
                                } catch (SAXException e) {
                                    LOGGER.error("Get Response  message :" + postmessage + " failed to parse ");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                XPath postxPath = XPathFactory.newInstance().newXPath();

                                String expression = "/SupplierLink/recordId/text()";

                                String supplierid = (String) postxPath.compile(expression).evaluate(postdoc, XPathConstants.STRING);
                                System.out.println(
                                        "supplier id " + supplierid);
                                // add initial supplier user
                                // build user message
                                String evolveuser =
                                        "<ns0:userFullDTO" +
                                                " xmlns:ns1=\"http://www.micros.com/creations/core/domain/dto/v1p0/simple\"" +
                                                " xmlns:ns0=\"http://www.micros.com/creations/core/domain/dto/v1p0/full\">" +
                                                "<ns0:code>" + evolvesupplier.getEmail() + "</ns0:code>" +
                                                "<ns0:person>" +
                                                "<ns0:email>" +
                                                        evolvesupplier.getEmail() +
                                                        "</ns0:email>" +
                                                "<ns0:name>" + evolvesupplier.getContact() + "</ns0:name>" +
                                                "<ns0:supplier>" +
                                                    "<ns1:code>" +
                                                    evolvesupplier.getSupplierCode() +
                                                    "</ns1:code>" +
                                                "</ns0:supplier>" +
                                                "</ns0:person>" +
                                                "<ns0:role>" +
                                                    "<ns1:code>" + dataConfig.getUserrole() + "</ns1:code>" +
                                                "</ns0:role>" +
                                                "<ns0:supplier>" +
                                                    "<ns1:code>" +
                                                        evolvesupplier.getSupplierCode() +
                                                     "</ns1:code>" +
                                                 "</ns0:supplier>" +
                                                "<ns0:userType>SUPPLIER</ns0:userType>" +
                                                "<ns0:timeZone>Europe/London</ns0:timeZone>" +
                                         "</ns0:userFullDTO>";
                                LOGGER.info("User request :" + evolveuser );

                                String userposturl = dataConfig.getCREATE_USER_URL();

                                LOGGER.info("Userurl :" + userposturl );

                                HttpEntity userpostrequest = new HttpEntity<>(evolveuser, headers);
                                ResponseEntity<String> userpostresponse = restTemplate.exchange(userposturl, HttpMethod.POST, userpostrequest, String.class);
                                HttpStatus userpostStatus = userpostresponse.getStatusCode();
                                if (userpostStatus == OK) {
                                    LOGGER.info("Supplier User : " + userpostresponse + " successfully inserted ");
                                    //obtain user id from response body
                                    String usermessage = userpostresponse.getBody();
                                    Document userdoc = null;
                                    try {
                                        userdoc = builder.parse(new InputSource(new StringReader(usermessage)));
                                    } catch (SAXException e) {
                                        LOGGER.error("User Response  message :" + usermessage + " failed to parse ");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    XPath userxPath = XPathFactory.newInstance().newXPath();

                                    String userexpression = "/UserLink/recordId/text()";

                                    String userid = (String) userxPath.compile(userexpression).evaluate(userdoc, XPathConstants.STRING);
                                    System.out.println(
                                            "user id " + userid);
                                    // build contact message
                                    String contact =
                                    "<ns0:ContactAndPersonDTO" +
                                    " xmlns:ns4=\"http://www.oracle.com/orbcmcs/service/rest/model\"" +
                                    " xmlns:ns0=\"http://www.micros.com/creations/core/domain/dto/v1p0/full\"" +
                                    " xmlns:ns1=\"http://www.micros.com/creations/core/domain/dto/v1p0/simple\">" +
                                    "<ns0:contactFullDTO>" +
	                                "<ns0:contactDetails>" +
                                            "<ns0:fax>fax</ns0:fax> <ns0:phoneNumber>phone</ns0:phoneNumber> " +
                                            "<ns0:id>" + userid + "</ns0:id>"+
                                    "</ns0:contactDetails>" +
                                    "<ns0:dtype>SupplierContact</ns0:dtype>" +
                                            "<ns0:person>" +
                                                    "<ns1:email>" + evolvesupplier.getEmail() + "</ns1:email>" +
                                                    "<ns1:name>"+ evolvesupplier.getContact() + "</ns1:name>" +
                                    "<ns1:supplier> <ns1:code>"+ evolvesupplier.getSupplierCode() + "</ns1:code></ns1:supplier>" +
                                            "<ns1:id>" + supplierid  + "</ns1:id>" +
                                            "</ns0:person>" +
                                    "<ns0:siteContact>true</ns0:siteContact>" +
                                    "<ns0:siteSelection>" +dataConfig.getSites() + "</ns0:siteSelection>" +
                                    "<ns0:supplierContact>true</ns0:supplierContact> " +
                                    "<ns0:supplierContactRole> <ns1:code>" +dataConfig.getRole1()+ "</ns1:code></ns0:supplierContactRole>" +
                                    "<ns0:supplierContactRole> <ns1:code>" +dataConfig.getRole2()+ "</ns1:code></ns0:supplierContactRole>" +
                                    "<ns0:supplierContactRole> <ns1:code>" +dataConfig.getRole3()+ "</ns1:code></ns0:supplierContactRole>" +
                                    "<ns0:supplierContactRole> <ns1:code>" +dataConfig.getRole4()+ "</ns1:code></ns0:supplierContactRole>" +
                                    "<ns0:supplierContactRole> <ns1:code>" +dataConfig.getRole5()+ "</ns1:code></ns0:supplierContactRole>" +
                                    "<ns0:supplierContactRole> <ns1:code>" +dataConfig.getRole6()+ "</ns1:code></ns0:supplierContactRole>" +
                                    "<ns0:supplierContactRole> <ns1:code>" +dataConfig.getRole7()+ "</ns1:code></ns0:supplierContactRole>" +
                                    "<ns0:supplierContactRole> <ns1:code>" +dataConfig.getRole8()+ "</ns1:code></ns0:supplierContactRole>" +
                                    "<ns0:supplierContactRole> <ns1:code>" +dataConfig.getRole9()+ "</ns1:code></ns0:supplierContactRole>" +
                                    "<ns0:siteContactRole> <ns1:code>" +dataConfig.getRole1()+ "</ns1:code></ns0:siteContactRole>" +
                                    "<ns0:siteContactRole> <ns1:code>" +dataConfig.getRole2()+ "</ns1:code></ns0:siteContactRole>" +
                                    "<ns0:siteContactRole> <ns1:code>" +dataConfig.getRole3()+ "</ns1:code></ns0:siteContactRole>" +
                                    "<ns0:siteContactRole> <ns1:code>" +dataConfig.getRole4()+ "</ns1:code></ns0:siteContactRole>" +
                                    "<ns0:siteContactRole> <ns1:code>" +dataConfig.getRole5()+ "</ns1:code></ns0:siteContactRole>" +
                                    "<ns0:siteContactRole> <ns1:code>" +dataConfig.getRole6()+ "</ns1:code></ns0:siteContactRole>" +
                                    "<ns0:siteContactRole> <ns1:code>" +dataConfig.getRole7()+ "</ns1:code></ns0:siteContactRole>" +
                                    "<ns0:siteContactRole> <ns1:code>" +dataConfig.getRole8()+ "</ns1:code></ns0:siteContactRole>" +
                                    "<ns0:siteContactRole> <ns1:code>" +dataConfig.getRole9()+ "</ns1:code></ns0:siteContactRole>" +
                                    "<ns0:company> <ns1:code>" + evolvesupplier.getSupplierCode() + "</ns1:code> </ns0:company>" +
                                    "</ns0:contactFullDTO>" +
                                    "<ns0:personFullDTO> " +
                                        "<ns0:contactDetails>" +
                                            "<ns0:id>"+ userid +"</ns0:id>" +
                                        "</ns0:contactDetails>" +
                                        "<ns0:email>" + evolvesupplier.getEmail() + "</ns0:email>" +
                                        "<ns0:name>"+ evolvesupplier.getContact() +"</ns0:name>" +
                                        "<ns0:supplier> <ns1:code>" + evolvesupplier.getSupplierCode() + "</ns1:code>" +
                                            " <ns1:companyNumber>123</ns1:companyNumber> " +
                                        "</ns0:supplier><ns0:id>"+ supplierid  + "</ns0:id>" +
                                    "</ns0:personFullDTO>" +
                                    "</ns0:ContactAndPersonDTO>";
                                    // end contact message
                                    //LOGGER.info("Before Sleep :"  );
                                    //Thread.sleep(threadSleep);


                                    LOGGER.info("Contact request :" + contact );

                                    String contactposturl = dataConfig.getCREATE_CONTACT_URL();

                                    LOGGER.info("ContactURL :" + contactposturl );

                                    HttpEntity contactpostrequest = new HttpEntity<>(contact, headers);
                                    ResponseEntity<String> contactpostresponse = restTemplate.exchange(contactposturl, HttpMethod.POST, contactpostrequest, String.class);
                                    HttpStatus contactpostStatus = contactpostresponse.getStatusCode();
                                    if (contactpostStatus == OK) {
                                        LOGGER.info("Supplier Contact : " + contactpostresponse + " successfully inserted ");
                                    } else {
                                        if (contactpostStatus == EXPECTATION_FAILED || contactpostStatus == BAD_REQUEST)
                                            LOGGER.error("Supplier Contact :" + contactpostresponse + " Contact creation failed");
                                    }
                                    // POST call to contact
                                 } else {
                                     if (postStatus == EXPECTATION_FAILED || postStatus == BAD_REQUEST)
                                          LOGGER.error("Supplier User :" + userpostresponse + " User creation failed");
                                        }
                            } else {
                                if (postStatus == EXPECTATION_FAILED || postStatus == BAD_REQUEST)
                                    LOGGER.error("Supplier" + postresponse + " creation rejected by Evolve");
                                commitOffsets = true;
                            }
                        } else {
                            LOGGER.error(" Evolve Server Error");
                            Thread.sleep(threadSleep);
                            threadSleep = threadSleep + 12000;
                            throw new RuntimeException("Evolve Server Error !");
                        }
                    }


                } catch (HttpStatusCodeException e) {
                    e.getMessage();
                    LOGGER.error(" Evolve Server Error " + e.toString());
                    ;
                    Thread.sleep(threadSleep);
                    threadSleep = threadSleep + 12000;
                    commitOffsets = false;
                    throw new RuntimeException("Evolve Server Error !");
                }

                if (commitOffsets) {
            /*
            Committing the offset to Kafka.
             */
                    acknowledgment.acknowledge();
                    LOGGER.info(SupplierSite + " Message committed !");

                }

                return null;
            }



    }
}



