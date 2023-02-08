package org.kq.addressbook;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class AddressBookApplicationTests {

    @Value(value="${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate; //why are you screaming
    @Autowired
    private TestRestTemplate restTemplatePatch; //why are you screaming

    @Before
    public void setup() {
        restTemplatePatch.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }
    @Test
    public void testIndexGet() throws Exception {
        String url = "http://localhost:" + port;
        assertThat(this.restTemplate.getForObject(url, String.class)).contains("addressBooks");
        assertThat(this.restTemplate.getForObject(url, String.class)).contains("buddyInfoes");
    }

    @Test
    public void testPostAddressBook() throws Exception {
        String url = "http://localhost:" + port + "/addressBooks";
        HttpEntity<AddressBook> request = new HttpEntity<>(new AddressBook());
        AddressBook ab = restTemplate.postForObject(url, request, AddressBook.class);
        assertThat(ab).isNotNull();
        assertThat(this.restTemplate.getForObject(url, String.class)).contains("addressBooks/1");
    }

    @Test
    public void testPostBuddyInfo() throws Exception {
        String url = "http://localhost:" + port + "/buddyInfoes";
        String name = "Bob";
        String phoneNumber = "555-333-1111";
        HttpEntity<BuddyInfo> request = new HttpEntity<>(new BuddyInfo(name, phoneNumber));
        BuddyInfo bud = restTemplate.postForObject(url, request, BuddyInfo.class);
        assertThat(bud).isNotNull();
        assertThat(bud.getName()).isEqualTo(name);
        assertThat(bud.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(this.restTemplate.getForObject(url, String.class)).contains("buddyInfoes/1");
    }
    @Test
    public void testPatchAddBuddiesToAddressBook() throws Exception {
        String url = "http://localhost:" + port;

        HttpEntity<AddressBook> requestAb = new HttpEntity<>(new AddressBook());
        restTemplate.postForObject(url + "/addressBooks", requestAb, Void.class);

        String name = "Bob";
        String phoneNumber = "555-333-1111";
        HttpEntity<BuddyInfo> requestBud = new HttpEntity<>(new BuddyInfo(name, phoneNumber));
        restTemplate.postForObject(url + "/buddyInfoes", requestBud, Void.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(RestMediaTypes.TEXT_URI_LIST);
        headers.setAccessControlRequestMethod(HttpMethod.PATCH); //need to fake PATCH as a POST for some reason
        HttpEntity<String> requestPatch = new HttpEntity<>("http://localhost:8080/buddyInfoes/1", headers);
        restTemplatePatch.postForObject(url + "/addressBooks/1/buddies", requestPatch, Void.class);

        String name2 = "Mob";
        String phoneNumber2 = "555-333-4444";
        requestBud = new HttpEntity<>(new BuddyInfo(name2, phoneNumber2));
        restTemplate.postForObject(url + "/buddyInfoes", requestBud, Void.class);
        requestPatch = new HttpEntity<>("http://localhost:8080/buddyInfoes/2", headers);
        restTemplatePatch.postForObject(url + "/addressBooks/1/buddies", requestPatch, Void.class);

        assertThat(this.restTemplate.getForObject(url + "/addressBooks/1/buddies", String.class)).contains(name);
        assertThat(this.restTemplate.getForObject(url + "/addressBooks/1/buddies", String.class)).contains(phoneNumber);
        assertThat(this.restTemplate.getForObject(url + "/addressBooks/1/buddies", String.class)).contains(name2);
        assertThat(this.restTemplate.getForObject(url + "/addressBooks/1/buddies", String.class)).contains(phoneNumber2);
    }
    @Test
    public void testDeleteBuddyFromAddressBook() throws Exception {
        String url = "http://localhost:" + port;

        HttpEntity<AddressBook> requestAb = new HttpEntity<>(new AddressBook());
        restTemplate.postForObject(url + "/addressBooks", requestAb, Void.class);

        String name = "Bob";
        String phoneNumber = "555-333-1111";
        HttpEntity<BuddyInfo> requestBud = new HttpEntity<>(new BuddyInfo(name, phoneNumber));
        restTemplate.postForObject(url + "/buddyInfoes", requestBud, Void.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(RestMediaTypes.TEXT_URI_LIST);
        headers.setAccessControlRequestMethod(HttpMethod.PATCH); //need to fake PATCH as a POST for some reason
        HttpEntity<String> requestPatch = new HttpEntity<>("http://localhost:8080/buddyInfoes/1", headers);
        restTemplatePatch.postForObject(url + "/addressBooks/1/buddies", requestPatch, Void.class);

        String name2 = "Mob";
        String phoneNumber2 = "555-333-4444";
        requestBud = new HttpEntity<>(new BuddyInfo(name2, phoneNumber2));
        restTemplate.postForObject(url + "/buddyInfoes", requestBud, Void.class);
        requestPatch = new HttpEntity<>("http://localhost:8080/buddyInfoes/2", headers);
        restTemplatePatch.postForObject(url + "/addressBooks/1/buddies", requestPatch, Void.class);

        restTemplate.delete(url + "/addressBooks/1/buddies/1");

        assertThat(this.restTemplate.getForObject(url + "/addressBooks/1/buddies", String.class)).doesNotContain(name);
        assertThat(this.restTemplate.getForObject(url + "/addressBooks/1/buddies", String.class)).doesNotContain(phoneNumber);
        assertThat(this.restTemplate.getForObject(url + "/addressBooks/1/buddies", String.class)).contains(name2);
        assertThat(this.restTemplate.getForObject(url + "/addressBooks/1/buddies", String.class)).contains(phoneNumber2);
    }

    @Test
    public void testHomeAddBuddies() throws Exception {
        String url = "http://localhost:" + port;

        HttpEntity<AddressBook> requestAb = new HttpEntity<>(new AddressBook());
        restTemplate.postForObject(url + "/addressBooks", requestAb, Void.class);

        String name = "Bob";
        String phoneNumber = "555-333-1111";
        HttpEntity<BuddyInfo> requestBud = new HttpEntity<>(new BuddyInfo(name, phoneNumber));
        restTemplate.postForObject(url + "/buddyInfoes", requestBud, Void.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(RestMediaTypes.TEXT_URI_LIST);
        headers.setAccessControlRequestMethod(HttpMethod.PATCH); //need to fake PATCH as a POST for some reason
        HttpEntity<String> requestPatch = new HttpEntity<>("http://localhost:8080/buddyInfoes/1", headers);
        restTemplatePatch.postForObject(url + "/addressBooks/1/buddies", requestPatch, Void.class);

        String name2 = "Mob";
        String phoneNumber2 = "555-333-4444";
        requestBud = new HttpEntity<>(new BuddyInfo(name2, phoneNumber2));
        restTemplate.postForObject(url + "/buddyInfoes", requestBud, Void.class);
        requestPatch = new HttpEntity<>("http://localhost:8080/buddyInfoes/2", headers);
        restTemplatePatch.postForObject(url + "/addressBooks/1/buddies", requestPatch, Void.class);

        assertThat(this.restTemplate.getForObject(url + "/home?id=1", String.class)).contains(name + "</td>");
        assertThat(this.restTemplate.getForObject(url + "/home?id=1", String.class)).contains(phoneNumber + "</td>");
        assertThat(this.restTemplate.getForObject(url + "/home?id=1", String.class)).contains(name2 + "</td>");
        assertThat(this.restTemplate.getForObject(url + "/home?id=1", String.class)).contains(phoneNumber2 + "</td>");
    }

    @Test
    public void testHomeDeleteBuddy() throws Exception {
        String url = "http://localhost:" + port;

        HttpEntity<AddressBook> requestAb = new HttpEntity<>(new AddressBook());
        restTemplate.postForObject(url + "/addressBooks", requestAb, Void.class);

        String name = "Bob";
        String phoneNumber = "555-333-1111";
        HttpEntity<BuddyInfo> requestBud = new HttpEntity<>(new BuddyInfo(name, phoneNumber));
        restTemplate.postForObject(url + "/buddyInfoes", requestBud, Void.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(RestMediaTypes.TEXT_URI_LIST);
        headers.setAccessControlRequestMethod(HttpMethod.PATCH); //need to fake PATCH as a POST for some reason
        HttpEntity<String> requestPatch = new HttpEntity<>("http://localhost:8080/buddyInfoes/1", headers);
        restTemplatePatch.postForObject(url + "/addressBooks/1/buddies", requestPatch, Void.class);

        String name2 = "Mob";
        String phoneNumber2 = "555-333-4444";
        requestBud = new HttpEntity<>(new BuddyInfo(name2, phoneNumber2));
        restTemplate.postForObject(url + "/buddyInfoes", requestBud, Void.class);
        requestPatch = new HttpEntity<>("http://localhost:8080/buddyInfoes/2", headers);
        restTemplatePatch.postForObject(url + "/addressBooks/1/buddies", requestPatch, Void.class);

        restTemplate.delete(url + "/addressBooks/1/buddies/1");

        assertThat(this.restTemplate.getForObject(url + "/home?id=1", String.class)).doesNotContain(name + "</td>");
        assertThat(this.restTemplate.getForObject(url + "/home?id=1", String.class)).doesNotContain(phoneNumber + "</td>");
        assertThat(this.restTemplate.getForObject(url + "/home?id=1", String.class)).contains(name2 + "</td>");
        assertThat(this.restTemplate.getForObject(url + "/home?id=1", String.class)).contains(phoneNumber2 + "</td>");
    }
}
