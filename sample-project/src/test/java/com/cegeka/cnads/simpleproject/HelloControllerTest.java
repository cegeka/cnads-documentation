package com.cegeka.cnads.simpleproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelloControllerTest {

    private HelloController controller;

    @Mock
    private HelloUserRepository helloUserRepository;

    @BeforeEach
    void setUp() {
        this.controller = new HelloController(helloUserRepository);
    }

    @Test
    void hello() {
        String result = controller.hello("test");

        assertThat(result).isEqualTo("Hello test");
        ArgumentCaptor<HelloUser> captor = ArgumentCaptor.forClass(HelloUser.class);
        verify(helloUserRepository).save(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("test");
    }
}