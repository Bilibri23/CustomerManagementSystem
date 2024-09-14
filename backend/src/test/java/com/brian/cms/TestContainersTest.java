package com.brian.cms;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestContainersTest  extends AbstractTestContainers{

    @Test
    void canStartPostgresDB(){
        postgreSQLContainer.start();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }

    //our testing container to make sure postgres is setup on docker as required
}
