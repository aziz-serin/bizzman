package com.bizzman.dao;

import static org.assertj.core.api.Assertions.assertThat;
import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.BusinessInformationService;
import com.bizzman.entities.BusinessInformation;
import com.bizzman.exceptions.ExceptionMessages;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class BusinessInformationServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    // Testing the custom method

    @Autowired
    BusinessInformationService businessInformationService;

    @Test
    public void saveThrowsExceptionIfBusinessInformationAlreadyExists() {
        BusinessInformation businessInformation = new BusinessInformation();
        businessInformation.setBusinessName("Extra business name");
        businessInformation.setBusinessDescription("I am unnecessary");
        businessInformation.setEstablismentDate(LocalDate.of(2022, 9, 8));
        try {
            businessInformationService.save(businessInformation);
        } catch (Exception e) {
            assertThat(businessInformationService.count()).isEqualTo(1);
            assertThat(e.getClass()).isEqualTo(RuntimeException.class);
            assertThat(e.getMessage()).isEqualTo(ExceptionMessages.BUSINESS_INFORMATION_EXCEPTION_MESSAGE);
        }
    }

}
