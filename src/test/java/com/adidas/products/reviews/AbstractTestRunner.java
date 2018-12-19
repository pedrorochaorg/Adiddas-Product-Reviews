package com.adidas.products.reviews;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Abstract class to extend tests from.
 *
 * @author pedrorocha
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTestRunner extends AbstractTestNGSpringContextTests {

}
