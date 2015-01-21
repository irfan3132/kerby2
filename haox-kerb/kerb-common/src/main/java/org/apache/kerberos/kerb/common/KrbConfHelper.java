/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *
 */
package org.apache.kerberos.kerb.common;

import org.apache.haox.config.Conf;
import org.apache.haox.config.Config;
import org.apache.kerberos.kerb.spec.common.EncryptionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Help KrbConfig and KdcConfig to load configs.
 */
public class KrbConfHelper {
    /**
     * The regex to split a config value(string) to a list of config value(string list).
     */
    private static final String LIST_SPLITTER = " ";

    public static String getStringUnderSection(Conf conf, SectionConfigKey key) {
        Config subConfig = conf.getConfig(key.getSectionName());
        if (subConfig != null) {
            return subConfig.getString(key);
        } else {
            return (String) key.getDefaultValue();
        }
    }

    public static boolean getBooleanUnderSection(Conf conf, SectionConfigKey key) {
        Config subConfig = conf.getConfig(key.getSectionName());
        if (subConfig != null) {
            return subConfig.getBoolean(key);
        } else {
            return (Boolean) key.getDefaultValue();
        }
    }

    public static long getLongUnderSection(Conf conf, SectionConfigKey key) {
        Config subConfig = conf.getConfig(key.getSectionName());
        if (subConfig != null) {
            return subConfig.getLong(key);
        } else {
            return (Long) key.getDefaultValue();
        }
    }

    public static List<EncryptionType> getEnctypesUnderSection(Conf conf, SectionConfigKey key) {
        String enctypesNamesString = getStringUnderSection(conf, key);
        String[] enctypesNames = enctypesNamesString.split(LIST_SPLITTER);
        return getEncryptionTypes(enctypesNames);
    }

    public static List<EncryptionType> getEncryptionTypes(String[] encTypeNames) {
        return getEncryptionTypes(Arrays.asList(encTypeNames));
    }

    public static List<EncryptionType> getEncryptionTypes(List<String> encTypeNames) {
        List<EncryptionType> results = new ArrayList<EncryptionType>(encTypeNames.size());

        EncryptionType etype;
        for (String etypeName : encTypeNames) {
            etype = EncryptionType.fromName(etypeName);
            if (etype != EncryptionType.NONE) {
                results.add(etype);
            }
        }
        return results;
    }

}
