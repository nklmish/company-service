package com.nklmish

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = CompanyServiceApplication)
@ActiveProfiles("test")
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class SpringSpecification extends Specification {

    @Value("\${local.server.port}")
    int port
}
