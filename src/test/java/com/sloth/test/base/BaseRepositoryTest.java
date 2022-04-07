package com.sloth.test.base;

import com.sloth.test.config.TestJpaConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestJpaConfiguration.class)
public class BaseRepositoryTest {

}