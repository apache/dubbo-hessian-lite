/*
 * Copyright (c) 2001-2004 Caucho Technology, Inc.  All rights reserved.
 *
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Caucho Technology (http://www.caucho.com/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Hessian", "Resin", and "Caucho" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    info@caucho.com.
 *
 * 5. Products derived from this software may not be called "Resin"
 *    nor may "Resin" appear in their names without prior written
 *    permission of Caucho Technology.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL CAUCHO TECHNOLOGY OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Scott Ferguson
 */

package com.alibaba.com.caucho.hessian.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Loads a class from the classloader.
 */
public class ClassFactory
{
    protected static final Logger log
            = Logger.getLogger(ClassFactory.class.getName());
    private static final ArrayList<Allow> _staticAllowList;
    private static final Map<String, Object> _allowSubClassSet = new ConcurrentHashMap<>();
    private static final Map<String, Object> _allowClassSet = new ConcurrentHashMap<>();

    private ClassLoader _loader;
    private boolean _isWhitelist;

    private LinkedList<Allow> _allowList;

    ClassFactory(ClassLoader loader)
    {
        _loader = loader;
        initAllow();
    }

    public Class<?> load(String className)
            throws ClassNotFoundException
    {
        if (isAllow(className)) {
            Class<?> aClass = Class.forName(className, false, _loader);

            if (_allowClassSet.containsKey(className)) {
                return aClass;
            }

            if (aClass.getInterfaces().length > 0) {
                for (Class<?> anInterface : aClass.getInterfaces()) {
                    if(!isAllow(anInterface.getName())) {
                        log.log(Level.SEVERE, className + "'s interfaces: " + anInterface.getName() + " in blacklist or not in whitelist, deserialization with type 'HashMap' instead.");
                        return HashMap.class;
                    }
                }
            }

            List<Class<?>> allSuperClasses = new LinkedList<>();

            Class<?> superClass = aClass.getSuperclass();
            while (superClass != null) {
                // add current super class
                allSuperClasses.add(superClass);
                superClass = superClass.getSuperclass();
            }

            for (Class<?> aSuperClass : allSuperClasses) {
                if(!isAllow(aSuperClass.getName())) {
                    log.log(Level.SEVERE, className + "'s superClass: " + aSuperClass.getName() + " in blacklist or not in whitelist, deserialization with type 'HashMap' instead.");
                    return HashMap.class;
                }

            }

            _allowClassSet.put(className, className);
            return aClass;
        }
        else {
            log.log(Level.SEVERE, className + " in blacklist or not in whitelist, deserialization with type 'HashMap' instead.");
            return HashMap.class;
        }
    }

    private boolean isAllow(String className)
    {
        LinkedList<Allow> allowList = _allowList;

        if (allowList == null) {
            return true;
        }

        if (_allowSubClassSet.containsKey(className)) {
            return true;
        }

        for (Allow allow : allowList) {
            Boolean isAllow = allow.allow(className);

            if (isAllow != null) {
                if (isAllow) {
                    _allowSubClassSet.put(className, className);
                }
                return isAllow;
            }
        }

        if (_isWhitelist) {
            return false;
        }

        _allowSubClassSet.put(className, className);
        return true;
    }

    public void setWhitelist(boolean isWhitelist)
    {
        _allowClassSet.clear();
        _allowSubClassSet.clear();
        _isWhitelist = isWhitelist;

        initAllow();
    }

    public void allow(String pattern)
    {
        _allowClassSet.clear();
        _allowSubClassSet.clear();
        initAllow();

        synchronized (this) {
            _allowList.addFirst(new Allow(toPattern(pattern), true));
        }
    }

    public void deny(String pattern)
    {
        _allowClassSet.clear();
        _allowSubClassSet.clear();
        initAllow();

        synchronized (this) {
            _allowList.addFirst(new Allow(toPattern(pattern), false));
        }
    }

    private static String toPattern(String pattern)
    {
        pattern = pattern.replace(".", "\\.");
        pattern = pattern.replace("*", ".*");

        return pattern;
    }

    private void initAllow()
    {
        synchronized (this) {
            if (_allowList == null) {
                _allowList = new LinkedList<Allow>();
                _allowList.addAll(_staticAllowList);
            }
        }
    }

    static class Allow {
        private Boolean _isAllow;
        private Pattern _pattern;

        public Allow() {
        }

        private Allow(String pattern, boolean isAllow)
        {
            _isAllow = isAllow;
            _pattern = Pattern.compile(pattern);
        }

        Boolean allow(String className)
        {
            if (_pattern.matcher(className).matches()) {
                return _isAllow;
            }
            else {
                return null;
            }
        }
    }

    static class AllowPrefix extends Allow {
        private Boolean _isAllow;
        private String _prefix;

        private AllowPrefix(String prefix, boolean isAllow)
        {
            super();
            _isAllow = isAllow;
            _prefix = prefix;
        }

        @Override
        Boolean allow(String className)
        {
            if (className.startsWith(_prefix)) {
                return _isAllow;
            }
            else {
                return null;
            }
        }
    }

    static {
        _staticAllowList = new ArrayList<Allow>();

        ClassLoader classLoader = ClassFactory.class.getClassLoader();
        try {
            String[] denyClasses = readLines(classLoader.getResourceAsStream("DENY_CLASS"));
            for (String denyClass : denyClasses) {
                if (denyClass.startsWith("#")) {
                    continue;
                }
                if (denyClass.endsWith(".")) {
                    _staticAllowList.add(new AllowPrefix(denyClass, false));
                } else {
                    _staticAllowList.add(new Allow(toPattern(denyClass), false));
                }
            }
        } catch (IOException ignore) {

        }
    }

    /**
     * read lines.
     *
     * @param is input stream.
     * @return lines.
     * @throws IOException If an I/O error occurs
     */
    public static String[] readLines(InputStream is) throws IOException {
        List<String> lines = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines.toArray(new String[0]);
        }
    }
}