package com.luban.testdome;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ReflectionUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Object maybeSelectorImplClass = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                System.out.println("aaaaaaaaaaaaaaaa");
                try {
                    return Class.forName(
                            "sun.nio.ch.SelectorImpl",
                            false,
                            PlatformDependent.getSystemClassLoader());
                } catch (Throwable cause) {
                    return cause;
                }
            }
        });
        System.out.println(maybeSelectorImplClass);
        final Selector unwrappedSelector;
        try {
            unwrappedSelector = SelectorProvider.provider().openSelector();
            final Class<?> selectorImplClass = (Class<?>) maybeSelectorImplClass;
            SelectedSelectionKeySet selectionKeys=new SelectedSelectionKeySet();
            Object maybeException = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
                        Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
                        Throwable cause = ReflectionUtil.trySetAccessible(selectedKeysField, true);
                        if (cause != null) {
                            return cause;
                        }
                        cause = ReflectionUtil.trySetAccessible(publicSelectedKeysField, true);
                        if (cause != null) {
                            return cause;
                        }
                        selectedKeysField.set(unwrappedSelector, selectionKeys);
                        publicSelectedKeysField.set(unwrappedSelector, selectionKeys);
                        return null;
                    } catch (NoSuchFieldException e) {
                        return e;
                    } catch (IllegalAccessException e) {
                        return e;
                    }
                }
            });
            Set<SelectionKey> set = unwrappedSelector.selectedKeys();
            System.out.println(set.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
