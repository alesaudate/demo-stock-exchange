package com.github.alesaudate.demostockexchange.fixtures;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SpringBeansMockProvider {


    public static ApplicationContext applicationContextMock() {

        var map = new HashMap<String, Object>();
        var mockConfigurableBeanFactory = mock(ConfigurableBeanFactory.class);
        doAnswer(args -> {

            var singletonKey = args.getArgument(0, String.class);
            var singleton = args.getArgument(1, Object.class);
            map.put(singletonKey, singleton);
            return null;
        }).when(mockConfigurableBeanFactory).registerSingleton(anyString(), any());

        when(mockConfigurableBeanFactory.containsSingleton(anyString())).thenAnswer(args -> {
            var key = args.getArgument(0, String.class);
            return map.containsKey(key);
        });

        var mockApplicationContext = mock(ApplicationContext.class);

        when(mockApplicationContext.getBean(anyString(), any(Class.class))).thenAnswer(args -> {
           var key = args.getArgument(0, String.class);
           return map.get(key);
        });

        when(mockApplicationContext.getParentBeanFactory()).thenReturn(mockConfigurableBeanFactory);

        return mockApplicationContext;
    }

}
