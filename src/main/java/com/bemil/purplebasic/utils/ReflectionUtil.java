package com.bemil.purplebasic.utils;


import com.bemil.purplebasic.PurpleBasic;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射工具类
 */
public class ReflectionUtil {
    private final String basePackages;
    private final Boolean recursive;
    private Set<Class<?>> classes;

    public ReflectionUtil(String basePackages, Boolean recursive) {
        this.basePackages = basePackages;
        this.recursive = recursive;
        this.classes = new LinkedHashSet<>();
        scanner();
    }

    /**
     * 获取所有类
     */
    public Set<Class<?>> getAll() {
        return this.classes;
    }

    /**
     * 获取所有包含注解的类
     * @param annotation 注解
     */
    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        for (Class<?> clz : this.classes) {
            if (clz.getAnnotation(annotation) != null) {
                classes.add(clz);
            }
        }
        return classes;
    }

    /**
     * 获取所有继承父类的类
     * @param superClass 父类
     */
    public Set<Class<?>> getTypesSuperclassWith(Class<?> superClass) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        for (Class<?> clz : this.classes) {
            if (clz.getSuperclass().isAssignableFrom(superClass)) {
                classes.add(clz);
            }
        }
        return classes;
    }

    private void scanner() {
        Set<Class<?>> classes = new LinkedHashSet<>();
        String packageName = this.basePackages;
        String packagePath = packageName.replace(".", "/");
        Enumeration<URL> dirs;
        try {
//            dirs = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            dirs = PurpleBasic.class.getClassLoader().getResources(packagePath);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    String filepath = URLDecoder.decode(url.getFile(), "UTF-8");
                    addClassesByFilepath(packageName, filepath, recursive, classes);
                } else if (protocol.equals("jar")) {
                    JarFile jarFile;
                    try {
                        jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry = entries.nextElement();
                            String name = jarEntry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packagePath)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace("/", ".");
                                }
                                if ((idx != -1) || recursive) {
                                    if (name.endsWith(".class") && !jarEntry.isDirectory()) {
                                        String className = name.substring(packageName.length() + 1,
                                                name.length() - 6);
                                        try {
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.classes = classes;
    }

    private void addClassesByFilepath(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (recursive && pathname.isDirectory()) || (pathname.getName().endsWith(".class"));
            }
        });
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                addClassesByFilepath(packageName + '.' + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
//                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(
//                            packageName + '.' + className));
                    classes.add(PurpleBasic.class.getClassLoader().loadClass(
                            packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
